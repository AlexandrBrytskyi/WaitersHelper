package server.service;

import server.exceptions.*;
import server.model.denomination.Denomination;
import server.model.dish.Dish;
import server.model.dish.DishType;
import server.model.dish.ingridient.Ingridient;
import server.model.dish.ingridient.Mesuarment;
import server.model.dish.ingridient.Product;
import server.model.order.OrderType;
import server.model.order.Ordering;

import java.time.LocalDateTime;
import java.util.List;

/**
 * User: huyti
 * Date: 14.01.2016
 */
public interface IWaitersService extends IBarmenService {

    Product addProduct(Product product);

    Product setMesuarmentById(int id, Mesuarment mesuarment) throws ProductByIdNotFoundException;

    Product getProductById(int id) throws ProductByIdNotFoundException;

    List<Product> getProductsByName(String name);

    Product removeProductById(int id) throws ProductByIdNotFoundException;

    List<Product> getAllProducts();


    Ingridient addIngridient(Product product, double amount);

    Ingridient addIngridient(Product product, double amount, Dish dish);

    Ingridient setDishOfIngridient(int id, Dish dish) throws IngridientWithIDNotFoundException;

    List<Ingridient> getIngridientsByProductName(String name);

    Ingridient getIngridientById(int id) throws IngridientWithIDNotFoundException;

    Ingridient removeIngridientById(int id) throws IngridientWithIDNotFoundException;

    List<Ingridient> getAllIngridients();

    List<Ingridient> getIngridientsByDish(Dish dish);



    List<Dish> findDishByName(String name);

    Dish getDishById(int id) throws NoDishWithIdFoundedException;

    Dish setDishType(Dish dish, DishType dishType);

    Dish setPriceForPortionToDish(double price, Dish dish);

    Dish setDescriptionOfDish(String description, Dish dish);

    Dish addIngridientToDish(Ingridient ingridient, Dish dish);

    List<Ingridient> getAllIngridientsByDishId(int id);

    Dish removeDishById(int id) throws NoDishWithIdFoundedException;






    Denomination getDenominationById(int id) throws DenominationWithIdNotFoundException;

    Denomination setDish(Dish dish, Denomination denomination);

    Denomination setOrder(Ordering ordering, Denomination denomination);

    /*after portion was set counting price for denomination*/
    Denomination setPortion(double portion, Denomination denomination);

    Denomination removeDenomination(int id) throws DenominationWithIdNotFoundException;








    Ordering getOrderingById(int id) throws NoOrderingWithIdException;

    Ordering setTimeClientsCome(Ordering ordering, LocalDateTime time);

    Ordering setOrderType(Ordering ordering, OrderType type);


    Ordering setDescription(String description, Ordering ordering);

    Ordering setAmountOfPeople(int amount, Ordering ordering);



}
