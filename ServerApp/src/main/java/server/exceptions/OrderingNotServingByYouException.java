package server.exceptions;

import server.model.order.Ordering;

/**
 * User: huyti
 * Date: 07.02.2016
 */
public class OrderingNotServingByYouException extends Throwable {
    public OrderingNotServingByYouException(Ordering ordering) {
        super("Ordering #" + ordering.getId() + " is not serving by you");
    }
}
