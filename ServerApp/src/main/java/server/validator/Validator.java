package server.validator;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
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

import java.util.ArrayList;
import java.util.List;

@Transactional
@Component("myValidator")
public class Validator implements IValidator {
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


    List<User> loggedUsers = new ArrayList<User>();

    public User login(String login, Password pass) throws WrongLoginException, WrongPasswordException, AccountBlockedException {
        User logged = userDAO.getUser(login, pass);
        loggedUsers.add(logged);
        LOGGER.info("user " + logged.getName() + " has loged in");
        return logged;
    }

    public IAdminService getAdminService(User user) {
        if (loggedUsers.contains(user) && user.getType().equals(UserType.ADMIN)) {
            LOGGER.info("admin service was given to user " + user.getName());
            return adminService;
        } else {
            LOGGER.error("problem with giving service to " + user.toString());
            return null;
        }
    }

    public IWaitersService getWaitersService(User user) {
        if (loggedUsers.contains(user) && user.getType().equals(UserType.WAITER)) {
            LOGGER.info("waiter service was given to user " + user.getName());
            return waitersService;
        } else {
            LOGGER.error("problem with giving service to " + user.toString());
            return null;
        }
    }

    public ICookService getCookService(User user) {
        if (loggedUsers.contains(user) && user.getType().equals(UserType.COOK)) {
            LOGGER.info("Cook service was given to user " + user.getName());
            return cookService;
        } else {
            LOGGER.error("problem with giving service to " + user.toString());
            return null;
        }
    }


    public IBarmenService getBarmenService(User user) {
        if (loggedUsers.contains(user) && user.getType().equals(UserType.BARMEN)) {
            LOGGER.info("Barmen service was given to user " + user.getName());
            return barmenService;
        } else {
            LOGGER.error("problem with giving service to " + user.toString());
            return null;
        }
    }

}
