package server.exceptions;

/**
 * User: huyti
 * Date: 10.01.2016
 */
public class DenominationWithIdNotFoundException extends Throwable {
    public DenominationWithIdNotFoundException(int id) {
        super("no denomination with id " + id);
    }
}
