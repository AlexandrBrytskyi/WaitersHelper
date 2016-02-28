package server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import server.dao.IIngredientDAO;
import server.dao.IProductDAO;
import transferFiles.exceptions.*;
import transferFiles.model.denomination.Denomination;
import transferFiles.model.dish.Dish;
import transferFiles.model.dish.DishType;
import transferFiles.model.dish.ingridient.Ingridient;
import transferFiles.model.dish.ingridient.Mesuarment;
import transferFiles.model.dish.ingridient.Product;
import transferFiles.model.order.OrderType;
import transferFiles.model.order.Ordering;

import java.time.LocalDateTime;
import java.util.List;

@Component(value = "waitersService")
@Transactional
@Scope("singleton")
public class WaitersService extends BarmenService implements IWaitersService {


    @Autowired(required = true)
    @Qualifier(value = "hibernateProductDAO")
    IProductDAO productDAO;

    @Autowired(required = true)
    @Qualifier(value = "hibernateIngridientDAO")
    IIngredientDAO ingredientDao;


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

    public List<Product> getAllProducts() {
        return productDAO.showAll();
    }


    /*Ingridients*/
    public Ingridient addIngridient(Product product, double amount) {
        return ingredientDao.addIngridient(product, amount);
    }

    public Ingridient addIngridient(Product product, double amount, Dish dish) {
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
        return  dishDAO.setPriceForPortionToDish(price, dish);
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


    public Dish removeDishById(int id) throws NoDishWithIdFoundedException {
        return dishDAO.removeDishById(id);
    }





/*denomination*/


    public Denomination getDenominationById(int id) throws DenominationWithIdNotFoundException {
        return denominationDAO.getDenominationById(id);
    }

    public Denomination setDish(Dish dish, Denomination denomination) {
        return  denominationDAO.setDish(dish, denomination);
    }

    public Denomination setOrder(Ordering ordering, Denomination denomination) {
        return  denominationDAO.setOrder(ordering, denomination);
    }

    public Denomination setPortion(double portion, Denomination denomination) {
        return denominationDAO.setPortion(portion, denomination);
    }


    public Denomination removeDenomination(int id) throws DenominationWithIdNotFoundException {
        return denominationDAO.removeDenomination(id);
    }






    /*orderings*/


    public Ordering getOrderingById(int id) throws NoOrderingWithIdException {
        return orderingDAO.getOrderingById(id);
    }

    public Ordering setTimeClientsCome(Ordering ordering, LocalDateTime time) {
        return orderingDAO.setTimeClientsCome(ordering, time);
    }


    public Ordering setOrderType(Ordering ordering, OrderType type) {
        return orderingDAO.setOrderType(ordering, type);
    }


    public Ordering setDescription(String description, Ordering ordering) {
        return orderingDAO.setDescription(description, ordering);
    }

    public Ordering setAmountOfPeople(int amount, Ordering ordering) {
        return orderingDAO.setAmountOfPeople(amount,ordering);
    }


}
