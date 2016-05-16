package transferFiles.to;


import transferFiles.exceptions.WrongPasswordException;
import transferFiles.model.user.User;

public interface IAccountable {

    User changeName(User user, String name);

    User changePassword(User user, String password) throws WrongPasswordException;

}
