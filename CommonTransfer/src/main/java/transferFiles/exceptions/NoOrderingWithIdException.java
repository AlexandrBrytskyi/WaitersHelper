package transferFiles.exceptions;

/**
 * User: huyti
 * Date: 12.01.2016
 */
public class NoOrderingWithIdException extends Throwable {
    public NoOrderingWithIdException(int id) {
        super("No ordering with id " + id);
    }
}
