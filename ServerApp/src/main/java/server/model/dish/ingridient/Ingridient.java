package server.model.dish.ingridient;

import server.model.IdUtil.IdAutoGenerator;
import server.model.dish.Dish;

import javax.persistence.*;

@Entity
@Table
public class Ingridient extends IdAutoGenerator {

    @Column
    private double amount;

    @ManyToOne()
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @ManyToOne(targetEntity = Dish.class)
    private Dish dish;


    public Ingridient() {
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    @Override
    public String toString() {
        return "Ingridient{" +
                "amount=" + amount +
                ", product=" + product +
                ", dish=" + dish.getId() +
                "} " + super.toString();
    }
}
