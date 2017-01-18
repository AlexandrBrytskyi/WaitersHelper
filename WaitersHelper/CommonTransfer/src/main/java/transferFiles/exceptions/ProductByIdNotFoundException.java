package transferFiles.exceptions;

/**
 * User: huyti
 * Date: 07.12.2015
 */
public class ProductByIdNotFoundException extends Exception {
    public ProductByIdNotFoundException(int id) {
        super("No product with id " + id);
    }

    public ProductByIdNotFoundException(String message) {
        super(message);
    }
}
