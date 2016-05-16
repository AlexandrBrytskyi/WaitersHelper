package transferFiles.exceptions;


public class NoOrderingWithIdException extends Exception {
    public NoOrderingWithIdException(int id) {
        super("No ordering with id " + id);
    }

    public NoOrderingWithIdException(String message) {
        super(message);
    }
}
