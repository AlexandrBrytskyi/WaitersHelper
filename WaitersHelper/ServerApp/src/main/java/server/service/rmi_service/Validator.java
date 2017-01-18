package server.service.rmi_service;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import server.dao.IUserDAO;
import server.service.CoocksMonitor.CoocksMonitor;
import transferFiles.exceptions.AccountBlockedException;
import transferFiles.exceptions.WrongLoginException;
import transferFiles.exceptions.WrongPasswordException;
import transferFiles.model.user.User;
import transferFiles.password_utils.Password;
import transferFiles.to.LoginLabel;
import transferFiles.validator.rmiValidator.IValidator;

import java.io.Serializable;
import java.util.*;


@Service("myValidator")
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON, proxyMode = ScopedProxyMode.INTERFACES)
public class Validator implements IValidator, Serializable {
    private final static Logger LOGGER = Logger.getLogger(Validator.class);

    @Autowired(required = true)
    @Qualifier("hibernateUserDAO")
    IUserDAO userDAO;

    @Autowired
    CoocksMonitor monitor;


    public Validator() {
        Thread thread = new Thread(new LoginedChecker());
        thread.start();
    }


    private Map<LoginLabel, Long> loggedUsersLabels = Collections.synchronizedMap(new HashMap<LoginLabel, Long>());

    public User login(String login, String pass) throws WrongLoginException, WrongPasswordException, AccountBlockedException {
        server.persistentModel.user.User logged = userDAO.getUser(login, new Password(pass));
        loggedUsersLabels.put(new LoginLabel(logged.toTransferUser(), logged.getType().toString()), System.currentTimeMillis());
        LOGGER.info("user " + logged.getName() + " has loged in");
        monitor.updateLoginedUsersList(getLoginedUsers());
        monitor.putUserTasks();
        return logged.toTransferUser();
    }


    @Override
    public void sendLabelToValidator(LoginLabel loginLable) {
        loggedUsersLabels.put(loginLable, System.currentTimeMillis());
    }


    public Map<LoginLabel, Long> getLoggedUsersLabels() {
        return loggedUsersLabels;
    }

    public List<server.persistentModel.user.User> getLoginedUsers() {
        List<server.persistentModel.user.User> result = new LinkedList<>();
        for (LoginLabel loginLabel : loggedUsersLabels.keySet()) {
            result.add(server.persistentModel.user.User.toPersistentUser(loginLabel.getUser()));
        }
        return result;
    }

    private class LoginedChecker implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.currentThread().sleep(30000);
                    checkTimePassAfterLabelsUpdated();
                    monitor.updateLoginedUsersList(getLoginedUsers());
                    System.out.println(getLoggedUsersLabels());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void checkTimePassAfterLabelsUpdated() {
            Iterator<Map.Entry<LoginLabel, Long>> iterator = loggedUsersLabels.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<LoginLabel, Long> pair = iterator.next();
                if ((System.currentTimeMillis() - pair.getValue()) > 40000)
                    iterator.remove();
            }
        }
    }

}
