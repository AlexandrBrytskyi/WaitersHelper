package server.exceptions;

/**
 * User: huyti
 * Date: 27.12.2015
 */
public class IngridientWithIDNotFoundException extends Throwable {
    public IngridientWithIDNotFoundException(int id) {
        super("no ingridient with id " + id + " founded");
    }
}
