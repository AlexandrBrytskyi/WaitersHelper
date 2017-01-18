package brytskyi.waitershelperclient.app.restService.serviceInterfaces;


import transferFiles.exceptions.IngridientWithIDNotFoundException;
import transferFiles.exceptions.ProductByIdNotFoundException;
import transferFiles.exceptions.UserAccessException;
import transferFiles.model.dish.Dish;
import transferFiles.model.dish.DishType;
import transferFiles.model.dish.ingridient.Ingridient;
import transferFiles.model.dish.ingridient.Product;
import transferFiles.service.restService.restRequstObjects.AddIngridientRequest;
import transferFiles.service.restService.restRequstObjects.RemoveDishRequest;

import java.util.List;

public interface IDishService extends IProductsService {


    Ingridient addIngridient(AddIngridientRequest addIngridientRequest);

    Ingridient removeIngridientById(int i) throws IngridientWithIDNotFoundException;

    List<Ingridient> getIngridientsByDish(Dish dish);

    Dish addDish(Dish dish);

    Dish updateDish(Dish dish);

    List<Dish> getAllDishes();

    Dish removeDish(RemoveDishRequest removeDishRequest) throws UserAccessException;

    List<Dish> getDishesByDishType(DishType dishType);


}
