package transferFiles.service.restService;


import transferFiles.exceptions.*;
import transferFiles.model.denomination.Denomination;
import transferFiles.model.dish.Dish;
import transferFiles.model.dish.ingridient.Ingridient;
import transferFiles.model.dish.ingridient.Product;
import transferFiles.model.order.Ordering;
import transferFiles.service.restService.restRequstObjects.*;

import java.util.List;


public interface IWaitersService extends IBarmenService {

    Product addProduct(Product product);

    Product getProductById(int id) throws ProductByIdNotFoundException;

    List<Product> getProductsByName(String name);

    Product removeProductById(int id) throws ProductByIdNotFoundException, UserAccessException;




    Ingridient addIngridient(AddIngridientRequest request);

    List<Ingridient> getIngridientsByProductName(String name);

    Ingridient removeIngridientById(int id) throws IngridientWithIDNotFoundException;

    List<Ingridient> getAllIngridients();

    List<Ingridient> getIngridientsByDish(Dish dish);



    List<Dish> findDishByName(String name);

    Dish getDishById(int id) throws NoDishWithIdFoundedException;

    Dish setDishType(SetDishTypeRequest request);

    Dish setPriceForPortionToDish(SetPriceForPortionRequest request);

    Dish setDescriptionOfDish(SetDescriptionOfDishRequest request);

    Dish removeDishById(int id) throws NoDishWithIdFoundedException;



    /*after portion was set counting price for denomination*/
    Denomination setPortion(SetPortionRequest request);

    Denomination removeDenomination(int id) throws DenominationWithIdNotFoundException;






    Ordering getOrderingById(int id) throws NoOrderingWithIdException;

    Ordering setTimeClientsCome(SetTimeClientsComeRequest request);

    Ordering setDescription(SetOrderingDescriptionRequest request);

    Ordering setAmountOfPeople(SetAmountOfPeopleRequest request);



}
