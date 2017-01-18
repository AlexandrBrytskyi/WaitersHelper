package server.service.rest_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import server.persistentModel.ConvertDate;
import transferFiles.exceptions.*;
import transferFiles.model.denomination.Denomination;
import transferFiles.model.dish.Dish;
import transferFiles.model.dish.ingridient.Ingridient;
import transferFiles.model.dish.ingridient.Product;
import transferFiles.model.order.Ordering;
import transferFiles.service.restService.IWaitersService;
import transferFiles.service.restService.restRequstObjects.*;

import java.util.List;

@RestController("restWaitersService")
@RequestMapping("service/waiter")
class RestWaitersService extends RestBarmenService implements IWaitersService {


    @Autowired
    @Qualifier("waitersService")
   private services.rmiService.IWaitersService waitersService;

    @RequestMapping(value = "/addProduct", method = RequestMethod.POST)
    public Product addProduct(@RequestBody Product product) {
        return waitersService.addProduct(product);
    }

    @RequestMapping(value = "/getProductById", method = RequestMethod.GET)
    public Product getProductById(@RequestParam int id) throws ProductByIdNotFoundException {
        return waitersService.getProductById(id);
    }

    @RequestMapping(value = "/getProductById", method = RequestMethod.POST)
    public List<Product> getProductsByName(@RequestBody String name) {
        return waitersService.getProductsByName(name);
    }

    @RequestMapping(value = "/removeProductById", method = RequestMethod.GET)
    public Product removeProductById(@RequestParam("id") int id) throws ProductByIdNotFoundException, UserAccessException {
        return waitersService.removeProductById(id);
    }

    @RequestMapping(value = "/getAllProducts", method = RequestMethod.GET)
    public List<Product> getAllProducts() {
        return waitersService.getAllProducts();
    }


    /*Ingridients*/

    @RequestMapping(value = "/addIngridient", method = RequestMethod.POST)
    public Ingridient addIngridient(@RequestBody AddIngridientRequest request) {
        return waitersService.addIngridient(request.getProduct(), request.getAmount(), request.getDish());
    }

    @RequestMapping(value = "/getIngridientsByProductName", method = RequestMethod.POST)
    public List<Ingridient> getIngridientsByProductName(@RequestBody String name) {
        return waitersService.getIngridientsByProductName(name);
    }

    @RequestMapping(value = "/removeIngridientById", method = RequestMethod.GET)
    public Ingridient removeIngridientById(@RequestParam int id) throws IngridientWithIDNotFoundException {
        return waitersService.removeIngridientById(id);
    }

    @RequestMapping(value = "/getAllIngridients", method = RequestMethod.GET)
    public List<Ingridient> getAllIngridients() {
        return waitersService.getAllIngridients();
    }

    @RequestMapping(value = "/getIngridientsByDish", method = RequestMethod.POST)
    public List<Ingridient> getIngridientsByDish(@RequestBody Dish dish) {
        return waitersService.getIngridientsByDish(dish);
    }


    /*Dishes*/

    @RequestMapping(value = "/findDishByName", method = RequestMethod.POST)
    public List<Dish> findDishByName(@RequestBody String name) {
        return waitersService.findDishByName(name);
    }


    @RequestMapping(value = "/getDishById", method = RequestMethod.GET)
    public Dish getDishById(@RequestParam int id) throws NoDishWithIdFoundedException {
        return waitersService.getDishById(id);
    }


    @RequestMapping(value = "/setDishType", method = RequestMethod.POST)
    public Dish setDishType(@RequestBody SetDishTypeRequest request) {
        return waitersService.setDishType(request.getDish(), request.getDishType());
    }


    @RequestMapping(value = "/setPriceForPortionToDish", method = RequestMethod.POST)
    public Dish setPriceForPortionToDish(SetPriceForPortionRequest request) {
        return waitersService.setPriceForPortionToDish(request.getPrice(), request.getDish());
    }

    @RequestMapping(value = "/setDescriptionOfDish", method = RequestMethod.POST)
    public Dish setDescriptionOfDish(@RequestBody SetDescriptionOfDishRequest request) {
        return waitersService.setDescriptionOfDish(request.getDescription(), request.getDish());
    }


    @RequestMapping(value = "/removeDishById", method = RequestMethod.GET)
    public Dish removeDishById(@RequestParam int id) throws NoDishWithIdFoundedException {
        return waitersService.removeDishById(id);
    }





/*denomination*/

    @RequestMapping(value = "/setPortion", method = RequestMethod.POST)
    public Denomination setPortion(@RequestBody SetPortionRequest request) {
        return waitersService.setPortion(request.getPortion(), request.getDenomination());
    }


    @RequestMapping(value = "/removeDenomination", method = RequestMethod.GET)
    public Denomination removeDenomination(@RequestParam int id) throws DenominationWithIdNotFoundException {
        return waitersService.removeDenomination(id);
    }






    /*orderings*/

    @RequestMapping(value = "/getOrderingById", method = RequestMethod.GET)
    public Ordering getOrderingById(@RequestParam int id) throws NoOrderingWithIdException {
        return waitersService.getOrderingById(id);
    }

    @RequestMapping(value = "/setTimeClientsCome", method = RequestMethod.POST)
    public Ordering setTimeClientsCome(@RequestBody SetTimeClientsComeRequest request) {
        return waitersService.setTimeClientsCome(request.getOrdering(), ConvertDate.toJavaFromJoda(request.getTime()));
    }

    @RequestMapping(value = "/setDescription", method = RequestMethod.POST)
    public Ordering setDescription(@RequestBody SetOrderingDescriptionRequest request) {
        return waitersService.setDescription(request.getDescription(), request.getOrdering());
    }

    @RequestMapping(value = "/setAmountOfPeople", method = RequestMethod.POST)
    public Ordering setAmountOfPeople(@RequestBody SetAmountOfPeopleRequest request) {
        return waitersService.setAmountOfPeople(request.getAmount(), request.getOrdering());
    }
}

