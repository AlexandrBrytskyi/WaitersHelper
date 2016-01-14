package server.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.dao.*;
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

@Service("userService")
@Transactional
public class UserService implements IUserService {

    @Autowired(required = true)
    @Qualifier(value = "hibernateProductDAO")
    IProductDAO productDAO;

    @Autowired(required = true)
    @Qualifier(value = "hibernateIngridientDAO")
    IIngredientDAO ingredientDao;

    @Autowired(required = true)
    @Qualifier(value = "hibernateDishDAO")
    IDishDAO dishDAO;

    @Autowired
    @Qualifier("hibernateDenominationDAO")
    IDenominationDAO denominationDAO;

    @Autowired
    @Qualifier("Ordering_hibernate_dao")
    IOrderingDAO orderingDAO;


    /*products*/
    public Product addProduct(Product product) {
        return productDAO.addProduct(product);
    }

    public Product setMesuarmentById(int id, Mesuarment mesuarment) throws ProductByIdNotFoundException {
        return productDAO.setMesuarmentById(id, mesuarment);
    }

    public Product getProductById(int id) throws ProductByIdNotFoundException {
        return productDAO.getProductById(id);
    }

    public List<Product> getProductsByName(String name) {
        return productDAO.getProductsByName(name);
    }

    public Product removeProductById(int id) throws ProductByIdNotFoundException {
        return productDAO.removeById(id);
    }

    public List<Product> showAllProducts() {
        return productDAO.showAll();
    }


    /*Ingridients*/
    public Ingridient addIngridient(Product product, int amount) {
        return ingredientDao.addIngridient(product, amount);
    }

    public Ingridient addIngridient(Product product, int amount, Dish dish) {
        return ingredientDao.addIngridient(product, amount, dish);
    }

    public Ingridient setDishOfIngridient(int id, Dish dish) throws IngridientWithIDNotFoundException {
        return ingredientDao.setDishOfIngridient(id, dish);
    }

    public List<Ingridient> getIngridientsByProductName(String name) {
        return ingredientDao.getIngridientsByProductName(name);
    }

    public Ingridient getIngridientById(int id) throws IngridientWithIDNotFoundException {
        return ingredientDao.getIngridientById(id);
    }

    public Ingridient removeIngridientById(int id) throws IngridientWithIDNotFoundException {
        return ingredientDao.removeIngridientById(id);
    }

    public List<Ingridient> getAllIngridients() {
        return ingredientDao.getAllIngridients();
    }

    public List<Ingridient> getIngridientsByDish(Dish dish) {
        return ingredientDao.getIngridientsByDish(dish);
    }


    /*Dishes*/
    public Dish addDish(Dish dish) {
        return dishDAO.addDish(dish);
    }


    public List<Dish> findDishByName(String name) {
        return dishDAO.findDishByName(name);
    }

    public Dish getDishById(int id) throws NoDishWithIdFoundedException {
        return dishDAO.getDishById(id);
    }

    public Dish setDishType(Dish dish, DishType dishType) {
        return dishDAO.setDishType(dish, dishType);
    }

    public Dish setPriceForPortionToDish(double price, Dish dish) {
        return dishDAO.setPriceForPortionToDish(price, dish);
    }

    public Dish setDescriptionOfDish(String description, Dish dish) {
        return dishDAO.setDescriptionOfDish(description, dish);
    }

    public Dish addIngridientToDish(Ingridient ingridient, Dish dish) {
        return dishDAO.addIngridientToDish(ingridient, dish);
    }

    public List<Ingridient> getAllIngridientsByDishId(int id) {
        return dishDAO.getAllIngridientsByDishId(id);
    }

    public Dish removeDish(Dish dish) {
        return dishDAO.removeDish(dish);
    }

    public Dish removeDishById(int id) throws NoDishWithIdFoundedException {
        return dishDAO.removeDishById(id);
    }

    public List<Dish> getDishesByDishType(DishType dishType) {
        return dishDAO.getDishesByDishType(dishType);
    }

    public List<Dish> getAllDishes() {
        return dishDAO.getAllDishes();
    }



/*denomination*/

    public Denomination addDenomination(Denomination denomination) {
        return denominationDAO.addDenomination(denomination);
    }

    public Denomination getDenominationById(int id) throws DenominationWithIdNotFoundException {
        return denominationDAO.getDenominationById(id);
    }

    public Denomination setDish(Dish dish, Denomination denomination) {
        return denominationDAO.setDish(dish, denomination);
    }

    public Denomination setOrder(Ordering ordering, Denomination denomination) {
        return denominationDAO.setOrder(ordering, denomination);
    }

    public Denomination setPortion(double portion, Denomination denomination) {
        return denominationDAO.setPortion(portion, denomination);
    }

    public Denomination setDenominationState(DenominationState state, Denomination denomination) {
        return denominationDAO.setDenominationState(state, denomination);
    }

    public Denomination removeDenomination(int id) throws DenominationWithIdNotFoundException {
        return denominationDAO.removeDenomination(id);
    }

    public Denomination removeDenomination(Denomination denomination) {
        return denominationDAO.removeDenomination(denomination);
    }

    public List<Denomination> getDenominationsByOrder(Ordering ordering) {
        return denominationDAO.getDenominationsByOrder(ordering);
    }



    /*orderings*/

    public Ordering addOrder(Ordering ordering) {
        return orderingDAO.addOrder(ordering);
    }

    public Ordering getOrderingById(int id) throws NoOrderingWithIdException {
        return orderingDAO.getOrderingById(id);
    }

    public Ordering setTimeClientsCome(Ordering ordering, LocalDateTime time) {
        return orderingDAO.setTimeClientsCome(ordering, time);
    }

    public Ordering setWhoServesOrder(Ordering ordering, User user) {
        return orderingDAO.setWhoServesOrder(ordering, user);
    }

    public Ordering setOrderType(Ordering ordering, OrderType type) {
        return orderingDAO.setOrderType(ordering, type);
    }

    public Ordering addDenominationToOrder(Ordering ordering, Denomination denomination) {
        return orderingDAO.addDenominationToOrder(ordering, denomination);
    }

    public Ordering setDescription(String description, Ordering ordering) {
        return orderingDAO.setDescription(description, ordering);
    }

    public Ordering setAmountOfPeople(int amount, Ordering ordering) {
        return orderingDAO.setAmountOfPeople(amount, ordering);
    }

    public Ordering setKO(double ko, Ordering ordering) {
        return orderingDAO.setKO(ko, ordering);
    }

    public Fund getFinalFund(Ordering ordering) {
        return orderingDAO.getFinalFund(ordering);
    }

    public Ordering removeOrdering(Ordering ordering) {
        return orderingDAO.removeOrdering(ordering);
    }


}
