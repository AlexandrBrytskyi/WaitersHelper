package server.service.CoocksMonitor;

/*
* that component must control sending added denominations and put them to
* needed userQueue for coocking (User will ask for this queue periodically)
* after changing state it must inform both sides
* for example: waiter adds new denomination, it is sent to cock, after cock canceled or prepared dish waiter has message
* if waiter canceled denomination, cock also must know about it*/

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.dao.IDenominationDAO;
import server.dao.IUserDAO;
import transferFiles.exceptions.DenominationWithIdNotFoundException;
import transferFiles.model.denomination.CurrentDenomination;
import transferFiles.model.denomination.Denomination;
import transferFiles.model.denomination.DenominationState;
import transferFiles.model.user.User;
import transferFiles.model.user.UserType;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
@Scope("singleton")
public class CoocksMonitor {

    @Autowired
    @Qualifier("hibernateDenominationDAO")
    private IDenominationDAO denominationDAO;

    @Autowired(required = true)
    @Qualifier("hibernateUserDAO")
    private IUserDAO userDAO;

    private static final Logger LOGGER = Logger.getLogger(CoocksMonitor.class);

    private List<User> loginedUsers;

    private Queue<CurrentDenomination> currentDenominationsQueue = new ConcurrentLinkedQueue<CurrentDenomination>();
    private Map<UserType, HashMap<User, UserTask>> tasksByWhoCoockingType =
            new HashMap<UserType, HashMap<User, UserTask>>();
    private Map<User, List<Denomination>> messages = Collections.synchronizedMap(new HashMap<User, List<Denomination>>());

    private boolean isInited = false;

    public CoocksMonitor() {

    }

    public void init() {
        if (isInited) return;
        getCurrentDenominations();
        initTasks();
        fillMessagesKeys();
        isInited = true;
    }

    private void initTasks() {
        initTasksByWhoCoockingType();
        putUserTasks();
    }

    private void runCurrentDemsQueue() {
        Queue<CurrentDenomination> curDemsWitoutWork = new LinkedList<CurrentDenomination>();
        while (!currentDenominationsQueue.isEmpty()) {
            try {
                Denomination denom = denominationDAO.getDenominationById(currentDenominationsQueue.peek().getDenomId());
                if (putNewDenomInTask(tasksByWhoCoockingType.get(denom.getDish().getWhoCoockDishType()), denom)) {
                    currentDenominationsQueue.remove();
                } else {
                    curDemsWitoutWork.add(currentDenominationsQueue.remove());
                }
            } catch (DenominationWithIdNotFoundException e) {
                LOGGER.error(e);
            }
        }
        currentDenominationsQueue.addAll(curDemsWitoutWork);
        curDemsWitoutWork = null;
    }


    //    here is pizda with putting
    private boolean putNewDenomInTask(HashMap<User, UserTask> userTaskHashMap, Denomination denom) {
        if (userTaskHashMap == null || userTaskHashMap.isEmpty()) return false;
        if (!isInited) {
            tasksByWhoCoockingType.get(denom.getDish().getWhoCoockDishType()).get(userDAO.getUser(currentDenominationsQueue.peek().getUserCoockingLogin())).addWorkingTask(denom);
            return true;
        }
        User mostLazyUser = findMostLazyUser(userTaskHashMap);
        if (mostLazyUser != null) {
            userTaskHashMap.get(mostLazyUser).addNewTask(denom);
            CurrentDenomination toUpdate = denominationDAO.getCurrentDenomination(denom.getId());
            toUpdate.setUserCoockingLogin(mostLazyUser.getLogin());
            setCurrDenomStateWaitingForCoock(denom);
            denominationDAO.mergeCurrentDenomination(toUpdate);
            return true;
        }
//       else when list of logined users is updated and there is needed user denom will be added;
        return false;
    }

    private User findMostLazyUser(HashMap<User, UserTask> userTaskHashMap) {
        double work = 0;
        boolean wasFirst = false;
        User lazyUser = null;
        for (Map.Entry<User, UserTask> userTaskEntry : userTaskHashMap.entrySet()) {
            if (!wasFirst) {
                work = userTaskEntry.getValue().getPortionsIDo();
                wasFirst = true;
            }
            if (userTaskEntry.getValue().getPortionsIDo() <= work &&
                    loginedUsers.contains(userTaskEntry.getKey())) {
                work = userTaskEntry.getValue().getPortionsIDo();
                lazyUser = userTaskEntry.getKey();
            }
        }
        return lazyUser;
    }

    public void putUserTasks() {
        boolean wasAdded = false;
        if (!isInited) {
            Set<User> users = getUsersCoocking();
            if (users != null && !users.isEmpty()) {
                for (User user : users) {
                    Map userTasks = tasksByWhoCoockingType.get(user.getType());
                    if (!userTasks.containsKey(user)) {
                        userTasks.put(user, new UserTask());
                        wasAdded = true;
                    }
                }
            }
            if (wasAdded) runCurrentDemsQueue();
        } else {
            if (loginedUsers != null && !loginedUsers.isEmpty()) {
                for (User loginedUser : loginedUsers) {
                    if (tasksByWhoCoockingType.containsKey(loginedUser.getType())) {
                        Map userTasks = tasksByWhoCoockingType.get(loginedUser.getType());
                        if (!userTasks.containsKey(loginedUser)) {
                            userTasks.put(loginedUser, new UserTask());
                            wasAdded = true;
                        }
                    }
                }
            }
            if (wasAdded) runCurrentDemsQueue();
        }
    }

    private Set<User> getUsersCoocking() {
        Set<User> result = new HashSet<User>();
        for (CurrentDenomination currentDenomination : currentDenominationsQueue) {
            if (currentDenomination.getUserCoockingLogin() != null) {
                result.add(userDAO.getUser(currentDenomination.getUserCoockingLogin()));
                System.out.println(userDAO.getUser(currentDenomination.getUserCoockingLogin()));
            }
        }
        return result;
    }

    private void initTasksByWhoCoockingType() {
        for (UserType whoCoockDishType : UserType.valuesWhoCoock()) {
            tasksByWhoCoockingType.put(whoCoockDishType, new HashMap<User, UserTask>());
        }
    }

    private void getCurrentDenominations() {
        List<CurrentDenomination> fromdb = denominationDAO.getCurrentDenominations();
        if (fromdb != null) currentDenominationsQueue.addAll(fromdb);
    }

    public void updateLoginedUsersList(List<User> loginedUsers) {
        this.loginedUsers = loginedUsers;
        putUserTasks();
        runCurrentDemsQueue();
    }

    public void addNewCurrentDenomination(CurrentDenomination curDenom) {
        currentDenominationsQueue.add(curDenom);
        denominationDAO.addCurrentDenomination(curDenom);
        runCurrentDemsQueue();
    }

    public List<Denomination> getNewTask(User user) {
        return tasksByWhoCoockingType.get(user.getType()).get(user).takeNewTask();
    }


    public List<Denomination> getWorkingTask(User user) {
        return tasksByWhoCoockingType.get(user.getType()).get(user).getWorkingTask();
    }

    public void setCurrDenomStateWaitingForCoock(Denomination denom) {
        denom = denominationDAO.setDenominationState(DenominationState.WAITING_FOR_COCK, denom);
    }

    public void setCurrDenomStateIsCoocking(Denomination denom) {
        denom = denominationDAO.setDenominationState(DenominationState.IS_COOKING, denom);
    }

    //    message
    public Denomination setCurrDenomStateCanceledByCoock(Denomination denom) {
        tasksByWhoCoockingType.get(denom.getDish().getWhoCoockDishType()).get(userDAO.getUser(denominationDAO.getCurrentDenomination(denom.getId()).getUserCoockingLogin())).removeTask(denom);
        messages.get(denom.getOrder().getWhoServesOrder()).add(denom);
        return denominationDAO.setDenominationState(DenominationState.CANCELED_BY_COCK, denom);
    }

    //    message
    public Denomination setCurrDenomStateCanceledByWaiter(Denomination denom) {
        tasksByWhoCoockingType.get(denom.getDish().getWhoCoockDishType()).get(userDAO.getUser(denominationDAO.getCurrentDenomination(denom.getId()).getUserCoockingLogin())).removeTask(denom);
        messages.get(denom.getOrder().getWhoServesOrder()).add(denom);
        return denominationDAO.setDenominationState(DenominationState.CANCELED_BY_WAITER, denom);
    }

    //    message
    public Denomination setCurrDenomStateCanceledByBarmen(Denomination denom) {
        tasksByWhoCoockingType.get(denom.getDish().getWhoCoockDishType()).get(userDAO.getUser(denominationDAO.getCurrentDenomination(denom.getId()).getUserCoockingLogin())).removeTask(denom);
        messages.get(denom.getOrder().getWhoServesOrder()).add(denom);
        return denominationDAO.setDenominationState(DenominationState.CANCELED_BY_BARMEN, denom);
    }

    //    message
    public Denomination setCurrDenomStateCanceledByAdmin(Denomination denom) {
        tasksByWhoCoockingType.get(denom.getDish().getWhoCoockDishType()).get(userDAO.getUser(denominationDAO.getCurrentDenomination(denom.getId()).getUserCoockingLogin())).removeTask(denom);
        messages.get(denom.getOrder().getWhoServesOrder()).add(denom);
        if (denominationDAO.getCurrentDenomination(denom.getId()).getUserCoockingLogin() != null)
            messages.get(userDAO.getUser(denominationDAO.getCurrentDenomination(denom.getId()).getUserCoockingLogin())).add(denom);
        return denominationDAO.setDenominationState(DenominationState.CANCELED_BY_ADMIN, denom);
    }

    //    message
    public Denomination setCurrDenomStateReady(Denomination denom) {
        tasksByWhoCoockingType.get(denom.getDish().getWhoCoockDishType()).get(userDAO.getUser(denominationDAO.getCurrentDenomination(denom.getId()).getUserCoockingLogin())).removeTask(denom);
        messages.get(denom.getOrder().getWhoServesOrder()).add(denom);
        return denominationDAO.setDenominationState(DenominationState.READY, denom);
    }

    //    message
    public void removeDenomination(Denomination denom) {
        messages.get(denom.getOrder().getWhoServesOrder()).add(denom);
        tasksByWhoCoockingType.get(denom.getDish().getWhoCoockDishType()).get(denom.getOrder().getWhoServesOrder()).removeTask(denom);
    }

    private void fillMessagesKeys() {
        for (User user : userDAO.getAllUsers()) {
            messages.put(user, new LinkedList<Denomination>());
        }
    }

    public List<Denomination> getMessage(User user) {
        List<Denomination> result = new LinkedList<Denomination>(messages.get(user));
        messages.get(user).removeAll(result);
        return result;
    }

    private class UserTask {

        private int portionsIDo = 0;
        private List<Denomination> newTask = new LinkedList<Denomination>();
        private List<Denomination> workingTask = new LinkedList<Denomination>();


        public UserTask() {
        }

        public UserTask(List<Denomination> newTask, List<Denomination> workingTask) {

            this.newTask = newTask;
            this.workingTask = workingTask;
        }


        public List<Denomination> getNewTask() {
            return newTask;
        }

        public List<Denomination> takeNewTask() {
            List<Denomination> res = new LinkedList<>(getNewTask());
            for (Denomination denomination : res) {
                setCurrDenomStateIsCoocking(denomination);
            }
            workingTask.addAll(getNewTask());
            newTask.removeAll(newTask);
            return res;
        }

        public void addNewTask(Denomination denomination) {
            portionsIDo += denomination.getPortion();
            newTask.add(denomination);
        }

        public void addWorkingTask(Denomination denomination) {
            portionsIDo += denomination.getPortion();
            workingTask.add(denomination);
        }

        public List<Denomination> getWorkingTask() {
            return workingTask;
        }

        public int getPortionsIDo() {
            return portionsIDo;
        }

        public void removeTask(Denomination denomination) {
            if (newTask.contains(denomination)) newTask.remove(denomination);
            if (workingTask.contains(denomination)) workingTask.remove(denomination);
            portionsIDo -= denomination.getPortion();
            denominationDAO.removeCurrentDenomination(denomination.getId());
        }

        @Override
        public String toString() {
            return "UserTask{" +
                    "workingTask=" + workingTask +
                    ", newTask=" + newTask +
                    ", portionsIDo=" + portionsIDo +
                    '}';
        }
    }


}
