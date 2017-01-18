package transferFiles.service.restService.restRequstObjects;

import transferFiles.model.denomination.Denomination;
import transferFiles.model.order.Ordering;


public class AddDenominationToOrderRequest {

    private Ordering ordering;
    private Denomination denomination;

    public AddDenominationToOrderRequest() {
    }

    public AddDenominationToOrderRequest(Ordering ordering, Denomination denomination) {
        this.ordering = ordering;
        this.denomination = denomination;
    }

    public Ordering getOrdering() {
        return ordering;
    }

    public void setOrdering(Ordering ordering) {
        this.ordering = ordering;
    }

    public Denomination getDenomination() {
        return denomination;
    }

    public void setDenomination(Denomination denomination) {
        this.denomination = denomination;
    }
}
