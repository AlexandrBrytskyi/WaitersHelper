package server.validator;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.dao.IUserDAO;
import server.exceptions.AccountBlockedException;
import server.exceptions.WrongLoginException;
import server.exceptions.WrongPasswordException;
import server.model.user.User;
import server.model.user.UserType;
import server.service.IAdminService;
import server.service.IBarmenService;
import server.service.ICookService;
import server.service.IWaitersService;
import server.service.password_utils.Password;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Transactional
@Service("myValidator")
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON, proxyMode = ScopedProxyMode.INTERFACES)
public class Validator implements IValidator, Serializable {
    private final static Logger LOGGER = Logger.getLogger(Validator.class);

    @Autowired(required = true)
    @Qualifier("hibernateUserDAO")
    IUserDAO userDAO;

    @Autowired(required = true)
    @Qualifier("adminService")
    IAdminService adminService;

    @Autowired(required = true)
    @Qualifier("waitersService")
    IWaitersService waitersService;

    @Autowired(required = true)
    @Qualifier("cookService")
    ICookService cookService;

    @Autowired(required = true)
    @Qualifier("barmenService")
    IBarmenService barmenService;

    /*while login called method "login" first, it returns logged User
    * having logged user MainFrame try to get service from validator
    * after service got it try to create UIFrame,
    * UIFrame must implement Loginable so it realises method sendUIToLoginedList()
    * ui is UIFrame, so if UIFrame is null, user will not be logined more
    */
    private Map<User, Object> loggedUsers = new HashMap<User, Object>();

    public User login(String login, Password pass) throws WrongLoginException, WrongPasswordException, AccountBlockedException {
        User logged = userDAO.getUser(login, pass);
        loggedUsers.put(logged, null);
        LOGGER.info("user " + logged.getName() + " has loged in");
        return logged;
    }

    public IAdminService getAdminService(User user) {
        if (loggedUsers.containsKey(user) && user.getType().equals(UserType.ADMIN)) {
            LOGGER.info("admin service was given to user " + user.getName());
            return adminService;
        } else {
            LOGGER.error("problem with giving service to " + user.toString());
            return null;
        }
    }

    public IWaitersService getWaitersService(User user) {
        if (loggedUsers.containsKey(user) && user.getType().equals(UserType.WAITER)) {
            LOGGER.info("waiter service was given to user " + user.getName());
            return waitersService;
        } else {
            LOGGER.error("problem with giving service to " + user.toString());
            return null;
        }
    }

    public ICookService getCookService(User user) {
        if (loggedUsers.containsKey(user) && (user.getType().equals(UserType.HOT_KITCHEN_COCK) ||
                user.getType().equals(UserType.COLD_KITCHEN_COCK) ||
                user.getType().equals(UserType.MANGAL_COCK))) {
            LOGGER.info("Cook service was given to user " + user.getName());
            return cookService;
        } else {
            LOGGER.error("problem with giving service to " + user.toString());
            return null;
        }
    }


    public IBarmenService getBarmenService(User user) {
        if (loggedUsers.containsKey(user) && user.getType().equals(UserType.BARMEN)) {
            LOGGER.info("Barmen service was given to user " + user.getName());
            return barmenService;
        } else {
            LOGGER.error("problem with giving service to " + user.toString());
            return null;
        }
    }

    @Override
    public void setObjectToUser(User user, Object ui) {
        if (loggedUsers.containsKey(user)) loggedUsers.put(user, ui);
        System.out.println(loggedUsers.toString());
    }

    public Map<User, Object> getLoggedUsers() {
        return loggedUsers;
    }



}
