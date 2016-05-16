package server.persistentModel.fund;

import server.persistentModel.order.Ordering;

import javax.persistence.*;
import java.io.Serializable;

//one order = one fund

@Entity
@Table(name = "fund")
public class Fund implements Serializable {

    @Column
    @Id
    private int id;

    @OneToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Ordering order;

    private double ko;

    private Double price;

    private Double finalPrice;

    public Fund() {
    }

    public Fund(int id, Ordering order, double ko, Double price, Double finalPrice) {
        this.id = id;
        this.order = order;
        this.ko = ko;
        this.price = price;
        this.finalPrice = finalPrice;
    }

    public Fund(int id, double ko, Double price, Double finalPrice) {
        this.id = id;
        this.ko = ko;
        this.price = price;
        this.finalPrice = finalPrice;
    }

    public Ordering getOrder() {
        return order;
    }

    public void setOrder(Ordering order) {
        this.order = order;
    }

    public double getKo() {
        return ko;
    }

    public void setKo(double ko) {
        this.ko = ko;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(Double finalPrice) {
        this.finalPrice = finalPrice;
    }

    @Override
    public String toString() {
        return "Fund{" +
                "id=" + id +
                ", order=" + order.getId() +
                ", ko=" + ko +
                ", price=" + price +
                ", finalPrice=" + finalPrice +
                '}';
    }

    public transferFiles.model.fund.Fund toTransferFund() {
        transferFiles.model.fund.Fund fundTo = new transferFiles.model.fund.Fund(this.id, this.getKo(), this.getPrice(),
                this.getFinalPrice());
        fundTo.setOrder(this.getOrder().toTransferOrdering(fundTo));
        return fundTo;
    }

    public transferFiles.model.fund.Fund toTransferFund(transferFiles.model.order.Ordering orderingTO) {
        return new transferFiles.model.fund.Fund(this.id, orderingTO, this.getKo(), this.getPrice(),
                this.getFinalPrice());
    }

    public static Fund toPersistentFund(transferFiles.model.fund.Fund fund, Ordering ordering) {
        return new Fund(fund.getId(), ordering, fund.getKo(), fund.getPrice(), fund.getFinalPrice());
    }

    public static Fund toPersistentFund(transferFiles.model.fund.Fund fund) {
        Fund persistentFund = new Fund(fund.getId(), fund.getKo(), fund.getPrice(), fund.getFinalPrice());
        persistentFund.setOrder(Ordering.toPersistentOrdering(fund.getOrder(), persistentFund));
        return persistentFund;
    }

}
