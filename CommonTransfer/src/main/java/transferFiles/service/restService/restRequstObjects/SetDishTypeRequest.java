package transferFiles.service.restService.restRequstObjects;

import transferFiles.model.dish.Dish;
import transferFiles.model.dish.DishType;

/**
 * User: huyti
 * Date: 16.05.2016
 */
public class SetDishTypeRequest {

   private Dish dish; private DishType dishType ;

    public SetDishTypeRequest() {
    }

    public SetDishTypeRequest(Dish dish, DishType dishType) {
        this.dish = dish;
        this.dishType = dishType;
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public DishType getDishType() {
        return dishType;
    }

    public void setDishType(DishType dishType) {
        this.dishType = dishType;
    }
}
