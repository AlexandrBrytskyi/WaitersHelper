package server.persistentModel.dish.ingridient;

import server.persistentModel.IdUtil.IdAutoGenerator;
import server.persistentModel.dish.Dish;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

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

    public Ingridient(int id, double amount, Product product, Dish dish) {
        this.amount = amount;
        this.product = product;
        this.dish = dish;
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

    public transferFiles.model.dish.ingridient.Ingridient toTransferIngridient() {
        transferFiles.model.dish.ingridient.Ingridient ingridientTo = new transferFiles.model.dish.ingridient.Ingridient(this.getId(), this.amount, this.product.toTransferProduct());
        ingridientTo.setDish(this.dish.toTransferDish(ingridientTo));
        return ingridientTo;
    }

    public transferFiles.model.dish.ingridient.Ingridient toTransferIngridient(transferFiles.model.dish.Dish dish) {
        return new transferFiles.model.dish.ingridient.Ingridient(this.getId(),this.amount, this.product.toTransferProduct(), dish);
    }

    public static Ingridient toPersistentIngridient(transferFiles.model.dish.ingridient.Ingridient ingridientTO) {
        Ingridient ingridient = new Ingridient(ingridientTO.getId(), ingridientTO.getAmount(),
                Product.toPersistentProduct(ingridientTO.getProduct()));
        ingridient.setDish(Dish.toPersistentDish(ingridientTO.getDish(), ingridient));
        return ingridient;
    }

    public static Ingridient toPersistentIngridient(transferFiles.model.dish.ingridient.Ingridient ingridient, Dish dish) {
        return new Ingridient(ingridient.getId(), ingridient.getAmount(), Product.toPersistentProduct(ingridient.getProduct()), dish);
    }

    public static List<transferFiles.model.dish.ingridient.Ingridient> getListIngridsTO(List<Ingridient> persistentIngridsList) {
        List<transferFiles.model.dish.ingridient.Ingridient> ingridsTO = new LinkedList<>();
        for (Ingridient ingridient : persistentIngridsList) {
            ingridsTO.add(ingridient.toTransferIngridient());
        }
        return ingridsTO;
    }
}
