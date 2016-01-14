package server.exceptions;

/**
 * User: huyti
 * Date: 07.12.2015
 */
public class ProductByIdNotFoundException extends Throwable {
    public ProductByIdNotFoundException(int id) {
        super("No product with id " + id);
    }
}
