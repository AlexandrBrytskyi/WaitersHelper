package server.model.fund;

import server.model.order.Ordering;

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
}
