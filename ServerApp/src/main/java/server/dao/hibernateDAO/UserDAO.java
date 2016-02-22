package server.dao.hibernateDAO;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import server.dao.IUserDAO;
import server.exceptions.AccountBlockedException;
import server.exceptions.UserFieldIsEmptyException;
import server.exceptions.WrongLoginException;
import server.exceptions.WrongPasswordException;
import server.model.user.User;
import server.service.password_utils.Password;

import java.io.Serializable;

@Repository("hibernateUserDAO")
public class UserDAO implements IUserDAO,Serializable {
    private static final Logger LOGGER = Logger.getLogger(UserDAO.class);

    @Autowired(required = true)
    private SessionFactory sessionFactory;


    public User addNewUser(User user) throws UserFieldIsEmptyException, WrongLoginException {
        validateSaving(user);
        try {
            sessionFactory.getCurrentSession().save(user);
        } catch (Throwable e) {
            LOGGER.error(e);
            return null;
        }
        return user;
    }

    /*when saving new user look for all fields of new user are filled*/
    private void validateSaving(User user) throws UserFieldIsEmptyException, WrongLoginException {
        if (user.getLogin() == null || user.getName() == null || user.getPass() == 0 || user.getType() == null)
            throw new UserFieldIsEmptyException("All fields must be filled!");
        User founded = sessionFactory.getCurrentSession().get(User.class, user.getLogin());
        if (founded != null) throw new WrongLoginException("User with login " + user.getLogin() + " already exists");
    }


    public User removeUser(User user) {
        try {
            sessionFactory.getCurrentSession().delete(user);
        } catch (Throwable e) {
            LOGGER.error(e);
            return null;
        }
        return user;
    }

    public User getUser(String login, Password password) throws WrongLoginException, WrongPasswordException, AccountBlockedException {
        User founded = null;
        founded = sessionFactory.getCurrentSession().get(User.class, login);
        if (founded == null) throw new WrongLoginException("User with login " + login + " doesn`t exist");
        if (password.compare(founded.getPass()) == false) throw new WrongPasswordException("Wrong password");
        if (founded.isLocked()) throw new AccountBlockedException("Account blocked");
        return founded;
    }

    @Override
    public User mergeUser(User user) {
        try {
            sessionFactory.getCurrentSession().merge(user);
        } catch (Throwable e) {
            LOGGER.error(e);
            return null;
        }
        return user;
    }


}
