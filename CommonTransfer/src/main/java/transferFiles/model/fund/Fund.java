package transferFiles.model.fund;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import transferFiles.model.order.Ordering;

import java.io.Serializable;

//one order = one fund

@JsonDeserialize(as = Fund.class)
@JsonSerialize(as = Fund.class)
public class Fund implements Serializable {

    private int id;
    @JsonBackReference
    private Ordering order;
    private double ko;
    private Double price;
    private Double finalPrice;

    public Fund(int id, Ordering order, double ko, Double price, Double finalPrice) {
        this.id = id;
        this.order = order;
        this.ko = ko;
        this.price = price;
        this.finalPrice = finalPrice;
    }


    @JsonCreator
    public Fund() {
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
}
