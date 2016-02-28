package client.validator;


import transferFiles.exceptions.AccountBlockedException;
import transferFiles.exceptions.WrongLoginException;
import transferFiles.exceptions.WrongPasswordException;
import transferFiles.model.user.User;
import transferFiles.to.LoginLabel;

import java.rmi.RemoteException;

public interface IValidator {

    User login(String login, String pass) throws WrongLoginException, WrongPasswordException, AccountBlockedException, RemoteException;

//    IAdminService getAdminService(User user);
//
//    IWaitersService getWaitersService(User user);
//
//    ICookService getCookService(User user);
//
//    IBarmenService getBarmenService(User user);

    void sendLabelToValidator(LoginLabel loginLable);
}
