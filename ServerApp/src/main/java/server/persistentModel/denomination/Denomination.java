package server.persistentModel.denomination;


import server.persistentModel.ConvertDate;
import server.persistentModel.IdUtil.IdAutoGenerator;
import server.persistentModel.dish.Dish;
import server.persistentModel.order.Ordering;
import transferFiles.model.denomination.DenominationState;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

//denomination is entity which is used like wrapper for dish to link one with order

@Entity
@Table(name = "denomination")
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

    public Denomination(int id, double portion, double price, LocalDateTime timeWhenAdded, LocalDateTime timeWhenIsReady, DenominationState state) {
        this.setId(id);
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

    public transferFiles.model.denomination.Denomination toDenominationTO() {
        transferFiles.model.denomination.Denomination denominationTO = new transferFiles.model.denomination.Denomination(
                this.getId(), this.dish.toTransferDish(),
                this.getPortion(), this.getPrice(),
                ConvertDate.toJoda(this.getTimeWhenAdded()),
                ConvertDate.toJoda(this.getTimeWhenIsReady()), this.getState());
        denominationTO.setOrder(this.order.toTransferOrdering(denominationTO));
        return denominationTO;
    }

    public transferFiles.model.denomination.Denomination toDenominationTO(transferFiles.model.order.Ordering orderingTO) {
        return new transferFiles.model.denomination.Denomination(this.getId(), this.dish.toTransferDish(),
                orderingTO,
                this.getPortion(), this.getPrice(), ConvertDate.toJoda(this.getTimeWhenAdded()),
                ConvertDate.toJoda(this.getTimeWhenIsReady()), this.getState());
    }

    public static Denomination toPersistentDenomination(transferFiles.model.denomination.Denomination denominationTO) {
        Denomination denomination = new Denomination(
                denominationTO.getId(),
                denominationTO.getPortion(),
                denominationTO.getPrice(),
                ConvertDate.toJavaFromJoda(denominationTO.getTimeWhenAdded()),
                ConvertDate.toJavaFromJoda(denominationTO.getTimeWhenIsReady()),
                denominationTO.getState()
        );
        denomination.setDish(Dish.toPersistentDish(denominationTO.getDish(), null));
        if (denominationTO.getOrder() != null)
            denomination.setOrder(Ordering.toPersistentOrdering(denominationTO.getOrder(), denomination));
        return denomination;
    }

    public static Denomination toPersistentDenomination(transferFiles.model.denomination.Denomination denominationTO, Ordering ordering) {
        Denomination denomination = new Denomination(
                denominationTO.getId(),
                denominationTO.getPortion(),
                denominationTO.getPrice(),
                ConvertDate.toJavaFromJoda(denominationTO.getTimeWhenAdded()),
                ConvertDate.toJavaFromJoda(denominationTO.getTimeWhenIsReady()),
                denominationTO.getState()
        );
        denomination.setDish(Dish.toPersistentDish(denominationTO.getDish(), null));
        denomination.setOrder(ordering);
        return denomination;
    }

    public static List<transferFiles.model.denomination.Denomination> getListTOdenoms(List<Denomination> persistentDenoms) {
        List<transferFiles.model.denomination.Denomination> denominations = new LinkedList<>();
        for (Denomination persistentDenom : persistentDenoms) {
            denominations.add(persistentDenom.toDenominationTO());
        }
        return denominations;
    }
}
