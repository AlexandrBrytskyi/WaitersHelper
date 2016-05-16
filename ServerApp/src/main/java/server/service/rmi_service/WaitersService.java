package server.service.rmi_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
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
import transferFiles.service.rmiService.IWaitersService;

import java.time.LocalDateTime;
import java.util.List;

@Service(value = "waitersService")
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
        return productDAO.addProduct(server.persistentModel.dish.ingridient.Product.toPersistentProduct(product)).toTransferProduct();
    }

    public Product setMesuarmentById(int id, Mesuarment mesuarment) throws ProductByIdNotFoundException {
        return productDAO.setMesuarmentById(id, mesuarment).toTransferProduct();
    }

    public Product getProductById(int id) throws ProductByIdNotFoundException {
        return productDAO.getProductById(id).toTransferProduct();
    }

    public List<Product> getProductsByName(String name) {
        return server.persistentModel.dish.ingridient.Product.getListProductTO(productDAO.getProductsByName(name));
    }

    public Product removeProductById(int id) throws ProductByIdNotFoundException {
        return productDAO.removeById(id).toTransferProduct();
    }

    public List<Product> getAllProducts() {
        return server.persistentModel.dish.ingridient.Product.getListProductTO(productDAO.showAll());
    }


    /*Ingridients*/
    public Ingridient addIngridient(Product product, double amount) {
        return ingredientDao.addIngridient(server.persistentModel.dish.ingridient.Product.toPersistentProduct(product), amount).toTransferIngridient();
    }

    public Ingridient addIngridient(Product product, double amount, Dish dish) {
        return ingredientDao.addIngridient(server.persistentModel.dish.ingridient.Product.toPersistentProduct(product),
                amount, server.persistentModel.dish.Dish.toPersistentDish(dish)).toTransferIngridient();
    }

    public Ingridient setDishOfIngridient(int id, Dish dish) throws IngridientWithIDNotFoundException {
        return ingredientDao.setDishOfIngridient(id, server.persistentModel.dish.Dish.toPersistentDish(dish)).toTransferIngridient();
    }

    public List<Ingridient> getIngridientsByProductName(String name) {
        return server.persistentModel.dish.ingridient.Ingridient.getListIngridsTO(ingredientDao.getIngridientsByProductName(name));
    }

    public Ingridient getIngridientById(int id) throws IngridientWithIDNotFoundException {
        return ingredientDao.getIngridientById(id).toTransferIngridient();
    }

    public Ingridient removeIngridientById(int id) throws IngridientWithIDNotFoundException {
        return ingredientDao.removeIngridientById(id).toTransferIngridient();
    }

    public List<Ingridient> getAllIngridients() {
        return server.persistentModel.dish.ingridient.Ingridient.getListIngridsTO(ingredientDao.getAllIngridients());
    }

    public List<Ingridient> getIngridientsByDish(Dish dish) {
        return server.persistentModel.dish.ingridient.Ingridient.getListIngridsTO(ingredientDao.getIngridientsByDish(server.persistentModel.dish.Dish.toPersistentDish(dish)));
    }


    /*Dishes*/


    public List<Dish> findDishByName(String name) {
        return dishDAO.findDishByName(name);
    }

    public Dish getDishById(int id) throws NoDishWithIdFoundedException {
        return dishDAO.getDishById(id).toTransferDish();
    }

    public Dish setDishType(Dish dish, DishType dishType) {
        return dishDAO.setDishType(server.persistentModel.dish.Dish.toPersistentDish(dish), dishType).toTransferDish();
    }

    public Dish setPriceForPortionToDish(double price, Dish dish) {
        return dishDAO.setPriceForPortionToDish(price, server.persistentModel.dish.Dish.toPersistentDish(dish)).toTransferDish();
    }

    public Dish setDescriptionOfDish(String description, Dish dish) {
        return dishDAO.setDescriptionOfDish(description, server.persistentModel.dish.Dish.toPersistentDish(dish)).toTransferDish();
    }

    public Dish addIngridientToDish(Ingridient ingridient, Dish dish) {
        return dishDAO.addIngridientToDish(server.persistentModel.dish.ingridient.Ingridient.toPersistentIngridient(ingridient),
                server.persistentModel.dish.Dish.toPersistentDish(dish)).toTransferDish();
    }

    public List<Ingridient> getAllIngridientsByDishId(int id) {
        return server.persistentModel.dish.ingridient.Ingridient.getListIngridsTO(dishDAO.getAllIngridientsByDishId(id));
    }


    public Dish removeDishById(int id) throws NoDishWithIdFoundedException {
        return dishDAO.removeDishById(id).toTransferDish();
    }





/*denomination*/


    public Denomination getDenominationById(int id) throws DenominationWithIdNotFoundException {
        return denominationDAO.getDenominationById(id).toDenominationTO();
    }

    public Denomination setDish(Dish dish, Denomination denomination) {
        return denominationDAO.setDish(server.persistentModel.dish.Dish.toPersistentDish(dish),
                server.persistentModel.denomination.Denomination.toPersistentDenomination(denomination)).toDenominationTO();
    }

    public Denomination setOrder(Ordering ordering, Denomination denomination) {
        return denominationDAO.setOrder(server.persistentModel.order.Ordering.toPersistentOrdering(ordering),
                server.persistentModel.denomination.Denomination.toPersistentDenomination(denomination)).toDenominationTO();
    }

    public Denomination setPortion(double portion, Denomination denomination) {
        return denominationDAO.setPortion(portion, server.persistentModel.denomination.Denomination.toPersistentDenomination(denomination))
                .toDenominationTO();
    }


    public Denomination removeDenomination(int id) throws DenominationWithIdNotFoundException {
        return denominationDAO.removeDenomination(id).toDenominationTO();
    }






    /*orderings*/


    public Ordering getOrderingById(int id) throws NoOrderingWithIdException {
        return orderingDAO.getOrderingById(id).toTransferOrdering();
    }

    public Ordering setTimeClientsCome(Ordering ordering, LocalDateTime time) {
        return orderingDAO.setTimeClientsCome(server.persistentModel.order.Ordering.toPersistentOrdering(ordering), time)
                .toTransferOrdering();
    }


    public Ordering setOrderType(Ordering ordering, OrderType type) {
        return orderingDAO.setOrderType(server.persistentModel.order.Ordering.toPersistentOrdering(ordering), type)
                .toTransferOrdering();
    }


    public Ordering setDescription(String description, Ordering ordering) {
        return orderingDAO.setDescription(description, server.persistentModel.order.Ordering.toPersistentOrdering(ordering))
                .toTransferOrdering();
    }

    public Ordering setAmountOfPeople(int amount, Ordering ordering) {
        return orderingDAO.setAmountOfPeople(amount, server.persistentModel.order.Ordering.toPersistentOrdering(ordering))
                .toTransferOrdering();
    }


}
