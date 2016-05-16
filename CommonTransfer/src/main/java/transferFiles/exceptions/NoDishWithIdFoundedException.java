package transferFiles.exceptions;

/**
 * User: huyti
 * Date: 27.12.2015
 */
public class NoDishWithIdFoundedException extends Exception {
    public NoDishWithIdFoundedException(int id) {
        super("no dish with id " + id + " founded");
    }

    public NoDishWithIdFoundedException(String message) {
        super(message);
    }
}
