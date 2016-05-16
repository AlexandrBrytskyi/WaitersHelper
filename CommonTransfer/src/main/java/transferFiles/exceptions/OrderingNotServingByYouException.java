package transferFiles.exceptions;

import transferFiles.model.order.Ordering;

/**
 * User: huyti
 * Date: 07.02.2016
 */
public class OrderingNotServingByYouException extends Exception {
    public OrderingNotServingByYouException(Ordering ordering) {
        super("Ordering #" + ordering.getId() + " is not serving by you");
    }

    public OrderingNotServingByYouException(String message) {
        super(message);
    }
}
