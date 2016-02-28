package transferFiles.exceptions;

import transferFiles.model.order.Ordering;

public class OrderingAlreadyServingException extends Throwable {
    public OrderingAlreadyServingException(Ordering ordering) {
        super("Ordering with id " + ordering.getId() + " is already serving by " + ordering.getWhoServesOrder().getName());
    }
}
