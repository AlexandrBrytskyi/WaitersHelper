package transferFiles.service.restService.restRequstObjects;


import transferFiles.model.order.Ordering;

public class SetAmountOfPeopleRequest {
   private int amount;private Ordering ordering;

    public SetAmountOfPeopleRequest() {
    }

    public SetAmountOfPeopleRequest(int amount, Ordering ordering) {
        this.amount = amount;
        this.ordering = ordering;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Ordering getOrdering() {
        return ordering;
    }

    public void setOrdering(Ordering ordering) {
        this.ordering = ordering;
    }
}
