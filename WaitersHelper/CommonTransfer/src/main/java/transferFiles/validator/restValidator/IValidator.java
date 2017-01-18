package transferFiles.validator.restValidator;


import transferFiles.exceptions.AccountBlockedException;
import transferFiles.exceptions.WrongLoginException;
import transferFiles.exceptions.WrongPasswordException;
import transferFiles.model.user.User;
import transferFiles.to.LoginLabel;

public interface IValidator {

    User login(String login, String pass) throws WrongLoginException, WrongPasswordException, AccountBlockedException;

    void sendLabelToValidator(LoginLabel loginLable);

}
