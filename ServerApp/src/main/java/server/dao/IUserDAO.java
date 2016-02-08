package server.dao;

import server.exceptions.AccountBlockedException;
import server.exceptions.UserFieldIsEmptyException;
import server.exceptions.WrongLoginException;
import server.exceptions.WrongPasswordException;
import server.model.user.User;
import server.service.password_utils.Password;


public interface IUserDAO {

    User addNewUser(User user) throws UserFieldIsEmptyException, WrongLoginException;

    User removeUser(User user);

    User getUser(String login, Password password) throws WrongLoginException, WrongPasswordException, AccountBlockedException;

    User mergeUser(User user);

}
