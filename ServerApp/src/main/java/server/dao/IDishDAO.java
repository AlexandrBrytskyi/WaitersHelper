package server.dao;

import server.exceptions.NoDishWithIdFoundedException;
import server.model.dish.Dish;
import server.model.dish.DishType;
import server.model.dish.ingridient.Ingridient;

import java.util.List;

/**
 * User: huyti
 * Date: 27.12.2015
 */
public interface IDishDAO {

    Dish addDish(Dish dish);

    List findDishByName(String name);

    Dish getDishById(int id) throws NoDishWithIdFoundedException;

    Dish updateDish(Dish dish);

    Dish setDishType(Dish dish, DishType dishType);

    Dish setPriceForPortionToDish(double price, Dish dish);

    Dish setDescriptionOfDish(String description, Dish dish);

    Dish addIngridientToDish(Ingridient ingridient, Dish dish);

    Dish removeDish(Dish dish);

    Dish removeDishById(int id) throws NoDishWithIdFoundedException;

    List<Dish> getDishesByDishType(DishType dishType);

    List<Dish> getAllDishes();


    List<Ingridient> getAllIngridientsByDishId(int id);
}
