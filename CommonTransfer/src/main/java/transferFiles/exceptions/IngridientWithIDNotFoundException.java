package transferFiles.exceptions;

/**
 * User: huyti
 * Date: 27.12.2015
 */
public class IngridientWithIDNotFoundException extends Exception {
    public IngridientWithIDNotFoundException(int id) {
        super("no ingridient with id " + id + " founded");
    }

    public IngridientWithIDNotFoundException(String message) {
        super(message);
    }
}
