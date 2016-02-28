package transferFiles.exceptions;

/**
 * User: huyti
 * Date: 15.01.2016
 */
public class WrongPasswordException extends Throwable {
    public WrongPasswordException(String message) {
        super(message);
    }
}
