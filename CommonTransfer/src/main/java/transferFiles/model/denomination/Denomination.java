package transferFiles.model.denomination;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import transferFiles.model.IdUtil.IdSupport;
import transferFiles.model.dish.Dish;
import transferFiles.model.order.Ordering;

import org.joda.time.LocalDateTime;

//denomination is entity which is used like wrapper for dish to link one with order

@JsonDeserialize(as = Denomination.class)
@JsonSerialize(as = Denomination.class)
public class Denomination extends IdSupport {

    private Dish dish;
    @JsonBackReference
    private Ordering order;
    private double portion;
    private double price;
    private LocalDateTime timeWhenAdded;
    private LocalDateTime timeWhenIsReady;
    private DenominationState state;

    public Denomination(int id, Dish dish, Ordering order, double portion,
                        double price, LocalDateTime timeWhenAdded,
                        LocalDateTime timeWhenIsReady, DenominationState state) {
        this.setId(id);
        this.dish = dish;
        this.order = order;
        this.portion = portion;
        this.price = price;
        this.timeWhenAdded = timeWhenAdded;
        this.timeWhenIsReady = timeWhenIsReady;
        this.state = state;
    }

    @JsonCreator
    public Denomination() {
    }

    public Denomination(int id, Dish dish, double portion, double price, LocalDateTime timeWhenAdded, LocalDateTime timeWhenIsReady, DenominationState state) {
        this.setId(id);
        this.dish = dish;
        this.portion = portion;
        this.price = price;
        this.timeWhenAdded = timeWhenAdded;
        this.timeWhenIsReady = timeWhenIsReady;
        this.state = state;
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public Ordering getOrder() {
        return order;
    }

    public void setOrder(Ordering order) {
        this.order = order;
    }

    public double getPortion() {
        return portion;
    }

    public void setPortion(double portion) {
        this.portion = portion;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public LocalDateTime getTimeWhenAdded() {
        return timeWhenAdded;
    }

    public void setTimeWhenAdded(LocalDateTime timeWhenAdded) {
        this.timeWhenAdded = timeWhenAdded;
    }

    public LocalDateTime getTimeWhenIsReady() {
        return timeWhenIsReady;
    }

    public void setTimeWhenIsReady(LocalDateTime timeWhenIsReady) {
        this.timeWhenIsReady = timeWhenIsReady;
    }

    public DenominationState getState() {
        return state;
    }

    public void setState(DenominationState state) {
        this.state = state;
    }

    public String printForOrdering() {
        return "Denomination: " + dish.getName() + ", portions: " + portion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Denomination that = (Denomination) o;

        if (Double.compare(that.portion, portion) != 0) return false;
        if (Double.compare(that.price, price) != 0) return false;
        if (dish != null ? !dish.equals(that.dish) : that.dish != null) return false;
        if (timeWhenAdded != null ? !timeWhenAdded.equals(that.timeWhenAdded) : that.timeWhenAdded != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return dish != null ? dish.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Denomination{" +
                "dish=" + dish +
                ", order=" + order +
                ", portion=" + portion +
                ", price=" + price +
                ", timeWhenAdded=" + timeWhenAdded +
                ", timeWhenIsReady=" + timeWhenIsReady +
                ", state=" + state +
                "} " + super.toString();
    }

}
