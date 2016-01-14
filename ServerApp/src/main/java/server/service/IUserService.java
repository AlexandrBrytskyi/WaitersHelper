package server.service;

import server.exceptions.*;
import server.model.denomination.Denomination;
import server.model.denomination.DenominationState;
import server.model.dish.Dish;
import server.model.dish.DishType;
import server.model.dish.ingridient.Ingridient;
import server.model.dish.ingridient.Mesuarment;
import server.model.dish.ingridient.Product;
import server.model.fund.Fund;
import server.model.order.OrderType;
import server.model.order.Ordering;
import server.model.user.User;

import java.time.LocalDateTime;
import java.util.List;

/**
 * User: huyti
 * Date: 07.12.2015
 */
public interface IUserService {

    Product addProduct(Product product);

    Product setMesuarmentById(int id, Mesuarment mesuarment) throws ProductByIdNotFoundException;

    Product getProductById(int id) throws ProductByIdNotFoundException;

    List<Product> getProductsByName(String name);

    Product removeProductById(int id) throws ProductByIdNotFoundException;

    List<Product> showAllProducts();


    Ingridient addIngridient(Product product, int amount);

    Ingridient addIngridient(Product product, int amount, Dish dish);

    Ingridient setDishOfIngridient(int id, Dish dish) throws IngridientWithIDNotFoundException;

    List<Ingridient> getIngridientsByProductName(String name);

    Ingridient getIngridientById(int id) throws IngridientWithIDNotFoundException;

    Ingridient removeIngridientById(int id) throws IngridientWithIDNotFoundException;

    List<Ingridient> getAllIngridients();

    List<Ingridient> getIngridientsByDish(Dish dish);


    Dish addDish(Dish dish);

    List<Dish> findDishByName(String name);

    Dish getDishById(int id) throws NoDishWithIdFoundedException;

    Dish setDishType(Dish dish, DishType dishType);

    Dish setPriceForPortionToDish(double price, Dish dish);

    Dish setDescriptionOfDish(String description, Dish dish);

    Dish addIngridientToDish(Ingridient ingridient, Dish dish);

    List<Ingridient> getAllIngridientsByDishId(int id);

    Dish removeDish(Dish dish);

    Dish removeDishById(int id) throws NoDishWithIdFoundedException;

    List<Dish> getDishesByDishType(DishType dishType);

    List<Dish> getAllDishes();


    Denomination addDenomination(Denomination denomination);

    Denomination getDenominationById(int id) throws DenominationWithIdNotFoundException;

    Denomination setDish(Dish dish, Denomination denomination);

    Denomination setOrder(Ordering ordering, Denomination denomination);

    /*after portion was set counting price for denomination*/
    Denomination setPortion(double portion, Denomination denomination);

    Denomination setDenominationState(DenominationState state, Denomination denomination);

    Denomination removeDenomination(int id) throws DenominationWithIdNotFoundException;

    Denomination removeDenomination(Denomination denomination);

    List<Denomination> getDenominationsByOrder(Ordering ordering);


    Ordering addOrder(Ordering ordering);

    Ordering getOrderingById(int id) throws NoOrderingWithIdException;

    Ordering setTimeClientsCome(Ordering ordering, LocalDateTime time);

    Ordering setWhoServesOrder(Ordering ordering, User user);

    Ordering setOrderType(Ordering ordering,OrderType type);

    Ordering addDenominationToOrder(Ordering ordering, Denomination denomination);

    Ordering setDescription(String description, Ordering ordering);

    Ordering setAmountOfPeople(int amount, Ordering ordering);

    Ordering setKO(double ko, Ordering ordering);

    Fund getFinalFund(Ordering ordering);

    Ordering removeOrdering(Ordering ordering);

    /*User service*/



}
