package server.service;

import server.exceptions.UserFieldIsEmptyException;
import server.exceptions.WrongLoginException;
import server.model.order.Ordering;
import server.model.user.User;


public interface IAdminService extends IWaitersService {


    Ordering removeOrdering(Ordering ordering);

    /*User service*/

    User addNewUser(User user) throws UserFieldIsEmptyException, WrongLoginException;

    User removeUser(User user);

    void lockUser(User user, boolean lock);


}
