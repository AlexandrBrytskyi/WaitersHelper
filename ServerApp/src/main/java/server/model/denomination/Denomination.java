package server.model.denomination;

import server.model.IdUtil.IdAutoGenerator;
import server.model.dish.Dish;
import server.model.order.Ordering;

import javax.persistence.*;
import java.time.LocalDateTime;

//denomination is entity which is used like wrapper for dish to link one with order

@Entity
@Table
public class Denomination extends IdAutoGenerator {

    @ManyToOne()
    @JoinColumn(name = "dish_id", referencedColumnName = "id")
    private Dish dish;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id")
    private Ordering order;


    @Column
    private double portion;


    @Column
    private double price;


    @Column
    private LocalDateTime timeWhenAdded;


    @Column
    private LocalDateTime timeWhenIsReady;


    @Column
    private DenominationState state;

    public Denomination() {
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
