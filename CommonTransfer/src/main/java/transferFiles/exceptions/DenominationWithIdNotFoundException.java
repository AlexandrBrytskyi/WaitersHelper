package transferFiles.exceptions;

/**
 * User: huyti
 * Date: 10.01.2016
 */
public class DenominationWithIdNotFoundException extends Exception {
    public DenominationWithIdNotFoundException(int id) {
        super("no denomination with id " + id);
    }

    public DenominationWithIdNotFoundException(String message) {
        super(message);
    }
}
