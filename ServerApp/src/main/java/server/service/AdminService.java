package server.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.exceptions.UserFieldIsEmptyException;
import server.exceptions.WrongLoginException;
import server.model.order.Ordering;
import server.model.user.User;

@Service("adminService")
@Transactional
public class AdminService extends WaitersService implements IAdminService {


    public Ordering removeOrdering(Ordering ordering) {
        return orderingDAO.removeOrdering(ordering);
    }


    /*users */
    public User addNewUser(User user) throws UserFieldIsEmptyException, WrongLoginException {
        return userDAO.addNewUser(user);
    }

    public User removeUser(User user) {
        return userDAO.removeUser(user);
    }

    @Override
    public void lockUser(User user, boolean lock) {
        user.setLocked(lock);
        userDAO.mergeUser(user);
    }


}
