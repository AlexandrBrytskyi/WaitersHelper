package brytskyi.waitershelperclient.app.restService;

import brytskyi.waitershelperclient.app.restService.serviceInterfaces.IOrderingService;
import brytskyi.waitershelperclient.app.restService.serviceInterfaces.IProductsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import transferFiles.exceptions.DenominationWithIdNotFoundException;
import transferFiles.exceptions.NoDishWithIdFoundedException;
import transferFiles.exceptions.NoOrderingWithIdException;
import transferFiles.model.denomination.Denomination;
import transferFiles.model.dish.Dish;
import transferFiles.model.dish.ingridient.Ingridient;
import transferFiles.model.order.Ordering;
import transferFiles.service.restService.IWaitersService;
import transferFiles.service.restService.restRequstObjects.*;
import brytskyi.waitershelperclient.app.tasksGetter.*;
import java.net.URI;
import java.util.List;


public class RestWaiterService extends RestBarmenService implements IWaitersService, IOrderingService, IProductsService {

    public RestWaiterService(RestTemplate template, ObjectMapper mapper) {
        super(template, mapper);
        this.template = template;
        this.mapper = mapper;
        uri = UriComponentsBuilder.fromHttpUrl("http://" + host + ":" + port + "/service/waiter/").build().toUri();
    }

    @Override
    public List<Ingridient> getIngridientsByProductName(String s) {
        return null;
    }


    @Override
    public List<Ingridient> getAllIngridients() {
        return null;
    }


    @Override
    public List<Dish> findDishByName(String s) {
        return null;
    }

    @Override
    public Dish getDishById(int i) throws NoDishWithIdFoundedException {
        return null;
    }

    @Override
    public Dish setDishType(SetDishTypeRequest setDishTypeRequest) {
        return null;
    }

    @Override
    public Dish setPriceForPortionToDish(SetPriceForPortionRequest setPriceForPortionRequest) {
        return null;
    }

    @Override
    public Dish setDescriptionOfDish(SetDescriptionOfDishRequest setDescriptionOfDishRequest) {
        return null;
    }

    @Override
    public Dish removeDishById(int i) throws NoDishWithIdFoundedException {
        return null;
    }

    @Override
    public Denomination setPortion(SetPortionRequest setPortionRequest) {
        return null;
    }

    @Override
    public Denomination removeDenomination(int i) throws DenominationWithIdNotFoundException {
        return null;
    }

    @Override
    public Ordering getOrderingById(int i) throws NoOrderingWithIdException {
        return null;
    }

    @Override
    public Ordering setTimeClientsCome(SetTimeClientsComeRequest setTimeClientsComeRequest) {
        return null;
    }

    @Override
    public Ordering setDescription(SetOrderingDescriptionRequest setOrderingDescriptionRequest) {
        return null;
    }

    @Override
    public Ordering setAmountOfPeople(SetAmountOfPeopleRequest setAmountOfPeopleRequest) {
        return null;
    }


}