package transferFiles.service.restService.restRequstObjects;

import transferFiles.model.dish.Dish;
import transferFiles.model.dish.ingridient.Product;

/**
 * User: huyti
 * Date: 16.05.2016
 */
public class AddIngridientRequest {
   private Product product; private double amount; private Dish dish;

    public AddIngridientRequest() {
    }

    public AddIngridientRequest(Product product, double amount, Dish dish) {
        this.product = product;
        this.amount = amount;
        this.dish = dish;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }
}
