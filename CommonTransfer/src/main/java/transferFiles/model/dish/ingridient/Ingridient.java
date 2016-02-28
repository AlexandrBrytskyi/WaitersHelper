package transferFiles.model.dish.ingridient;

import transferFiles.model.IdUtil.IdAutoGenerator;
import transferFiles.model.dish.Dish;

import javax.persistence.*;

@Entity
@Table(name = "ingridient")
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ingridient that = (Ingridient) o;

        return product.equals(that.product);

    }

    @Override
    public int hashCode() {
        return product.hashCode();
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
