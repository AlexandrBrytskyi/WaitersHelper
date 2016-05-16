package transferFiles.service.restService;

import org.joda.time.LocalDate;
import transferFiles.exceptions.*;
import transferFiles.model.denomination.Denomination;
import transferFiles.model.dish.Dish;
import transferFiles.model.dish.DishType;
import transferFiles.model.fund.Fund;
import transferFiles.model.order.Ordering;
import transferFiles.model.user.User;
import transferFiles.service.restService.restRequstObjects.*;

import java.awt.print.PrinterException;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public interface IBarmenService extends Serializable,ICookService {

    Ordering addOrder(Ordering ordering);

    Ordering addDenominationToOrder(AddDenominationToOrderRequest request);

    Ordering setKO(String ko, Ordering ordering);

    Fund getFinalFund(Ordering ordering);

    List<Ordering> getOrderings(LocalDate concretteDay);

    List<Ordering> getOrderings(GetByDateBeginEndRequest request);

    List<Ordering> getOrderings(GetOrderingByDateUserTakenRequest request);

    List<Ordering> getOrderings(GetOrderingByDateBeginEndUserTakenRequest request);

    List<Ordering> getOrderingsUserServes(GetOrderingByDateBeginEndUserServesRequest request);

    Ordering updateOrdering(Ordering orderingSource);

    Ordering setWhoServesOrder(SetWhoServesOrderingRequest request) throws OrderingAlreadyServingException, NoOrderingWithIdException, UserAccessException;

    Denomination addDenomination(AddCancelDenominationRequest request) throws UserAccessException, NoOrderingWithIdException;

    List<Denomination> getDenominationsByOrder(Ordering ordering);

    List<Denomination> getDenominationsByOrderForFund(Ordering ordering);

    Denomination removeDenomination(RemoveDenominationRequest request) throws UserAccessException;

    Dish addDish(Dish dish);

    Dish updateDish(Dish dish);

    List<Dish> getDishesByDishType(DishType dishType);

    List<Dish> getAllDishes();

    Dish removeDish(RemoveDishRequest request) throws UserAccessException;

    Ordering removeOrdering(RemoveOrderingRequest request) throws UserAccessException;

    List<User> getAllUsers();

    Ordering setWhoServesOrderNull(SetWhoServesOrderingRequest request) throws OrderingNotServingByYouException, NoOrderingWithIdException, UserAccessException;

    void generatePrintPdf(Ordering ordering) throws IOException, PrinterException;

    void cancelDenomination(AddCancelDenominationRequest request) throws UserAccessException, DenominationWithIdNotFoundException;

    List<Denomination> getMessages(User user);


}
