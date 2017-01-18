package transferFiles.service.restService.restRequstObjects;

import transferFiles.model.dish.Dish;

/**
 * User: huyti
 * Date: 16.05.2016
 */
public class SetPriceForPortionRequest {

   private double price; private Dish dish;

    public SetPriceForPortionRequest() {
    }

    public SetPriceForPortionRequest(double price, Dish dish) {
        this.price = price;
        this.dish = dish;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }
}
