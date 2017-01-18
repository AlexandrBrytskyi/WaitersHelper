package transferFiles.model.dish.ingridient;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import transferFiles.model.IdUtil.IdSupport;
import transferFiles.model.dish.Dish;

@JsonDeserialize(as = Ingridient.class)
@JsonSerialize(as = Ingridient.class)
public class Ingridient extends IdSupport {

    private double amount;
    private Product product;
    @JsonBackReference
    private Dish dish;


    public Ingridient(int id, double amount, Product product, Dish dish) {
        this.setId(id);
        this.amount = amount;
        this.product = product;
        this.dish = dish;
    }

    @JsonCreator
    public Ingridient() {
    }

    public Ingridient(int id, double amount, Product product) {
        this.setId(id);
        this.amount = amount;
        this.product = product;
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
