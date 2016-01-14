package server.exceptions;

/**
 * User: huyti
 * Date: 27.12.2015
 */
public class NoDishWithIdFoundedException extends Throwable {
    public NoDishWithIdFoundedException(int id) {
        super("no dish with id " + id + " founded");
    }
}
