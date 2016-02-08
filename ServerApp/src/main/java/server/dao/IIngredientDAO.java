package server.dao;

import server.exceptions.IngridientWithIDNotFoundException;
import server.model.dish.Dish;
import server.model.dish.ingridient.Ingridient;
import server.model.dish.ingridient.Product;

import java.util.List;

/**
 * User: huyti
 * Date: 27.12.2015
 */
public interface IIngredientDAO {


    Ingridient addIngridient(Product product, double amount);

    Ingridient addIngridient(Product product, double amount, Dish dish);

    Ingridient setDishOfIngridient(int id, Dish dish) throws IngridientWithIDNotFoundException;

    List<Ingridient> getIngridientsByProductName(String name);

    Ingridient getIngridientById(int id) throws IngridientWithIDNotFoundException;

    Ingridient removeIngridientById(int id) throws IngridientWithIDNotFoundException;

    List<Ingridient> getAllIngridients();

    List<Ingridient> getIngridientsByDish(Dish dish);
}
