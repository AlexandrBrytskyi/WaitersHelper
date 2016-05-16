package server.dao;

import transferFiles.exceptions.AccountBlockedException;
import transferFiles.exceptions.UserFieldIsEmptyException;
import transferFiles.exceptions.WrongLoginException;
import transferFiles.exceptions.WrongPasswordException;
import server.persistentModel.user.User;
import transferFiles.password_utils.Password;

import java.util.List;


public interface IUserDAO {

    User addNewUser(User user) throws UserFieldIsEmptyException, WrongLoginException;

    User removeUser(User user);

    User getUser(String login, Password password) throws WrongLoginException, WrongPasswordException, AccountBlockedException;

    User mergeUser(User user);

    User getUser(String userCoockingLogin);

    List<User> getAllUsers();
}
