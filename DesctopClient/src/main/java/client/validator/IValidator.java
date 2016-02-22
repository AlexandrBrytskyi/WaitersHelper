package client.validator;


import server.exceptions.AccountBlockedException;
import server.exceptions.WrongLoginException;
import server.exceptions.WrongPasswordException;
import server.model.user.User;
import server.service.IAdminService;
import server.service.IBarmenService;
import server.service.ICookService;
import server.service.IWaitersService;
import server.service.password_utils.Password;

public interface IValidator {

    User login(String login, Password pass) throws WrongLoginException, WrongPasswordException, AccountBlockedException;

    IAdminService getAdminService(User user);

    IWaitersService getWaitersService(User user);

    ICookService getCookService(User user);

    IBarmenService getBarmenService(User user);

    void setObjectToUser(User user, Object ui);
}
