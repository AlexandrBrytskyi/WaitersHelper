package transferFiles.service.restService.restRequstObjects;

import transferFiles.model.dish.Dish;

/**
 * User: huyti
 * Date: 16.05.2016
 */
public class SetDescriptionOfDishRequest {

    private String description; private Dish dish;

    public SetDescriptionOfDishRequest() {
    }

    public SetDescriptionOfDishRequest(String description, Dish dish) {
        this.description = description;
        this.dish = dish;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }
}
