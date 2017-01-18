package brytskyi.waitershelperclient.app.restService.serviceInterfaces;


import transferFiles.exceptions.UserFieldIsEmptyException;
import transferFiles.exceptions.WrongLoginException;
import transferFiles.model.user.User;
import transferFiles.service.restService.restRequstObjects.LockUserRequest;

import java.util.List;

public interface IUserControl extends IAllUsersGetter {

    User addNewUser(User user) throws UserFieldIsEmptyException, WrongLoginException;

    void lockUser(LockUserRequest lockUserRequest);

}
