package server.exceptions;

import server.model.order.Ordering;

public class OrderingAlreadyServingException extends Throwable {
    public OrderingAlreadyServingException(Ordering ordering) {
        super("Ordering with id " + ordering.getId() + " is already serving by " + ordering.getWhoServesOrder().getName());
    }
}
