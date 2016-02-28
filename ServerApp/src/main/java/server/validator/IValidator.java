package server.validator;


import transferFiles.exceptions.AccountBlockedException;
import transferFiles.exceptions.WrongLoginException;
import transferFiles.exceptions.WrongPasswordException;
import transferFiles.model.user.User;
import transferFiles.to.LoginLabel;

import java.rmi.RemoteException;

public interface IValidator {

    User login(String login, String pass) throws WrongLoginException, WrongPasswordException, AccountBlockedException, RemoteException;

    void sendLabelToValidator(LoginLabel loginLable);

}
