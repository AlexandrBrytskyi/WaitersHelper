package brytskyi.waitershelperclient.app.restService.serviceInterfaces;


import org.joda.time.LocalDate;
import transferFiles.exceptions.*;
import transferFiles.model.denomination.Denomination;
import transferFiles.model.dish.Dish;
import transferFiles.model.dish.DishType;
import transferFiles.model.order.Ordering;
import transferFiles.service.restService.restRequstObjects.*;

import java.io.IOException;
import java.util.List;

public interface IOrderingService extends IAllUsersGetter {

    Ordering addOrder(Ordering ordering);

    Ordering setKO(String s, Ordering ordering);

    List<Ordering> getOrderings(LocalDate localDate);

    List<Ordering> getOrderings(GetByDateBeginEndRequest getByDateBeginEndRequest);

    List<Ordering> getOrderings(GetOrderingByDateUserTakenRequest getOrderingByDateUserTakenRequest);

    List<Ordering> getOrderings(GetOrderingByDateBeginEndUserTakenRequest getOrderingByDateBeginEndUserTakenRequest);

    List<Ordering> getOrderingsUserServes(GetOrderingByDateBeginEndUserServesRequest getOrderingByDateBeginEndUserServesRequest);

    Ordering updateOrdering(Ordering ordering);

    Ordering setWhoServesOrder(SetWhoServesOrderingRequest setWhoServesOrderingRequest) throws OrderingAlreadyServingException, NoOrderingWithIdException, UserAccessException;

    Denomination addDenomination(AddCancelDenominationRequest addCancelDenominationRequest) throws UserAccessException, NoOrderingWithIdException;

    List<Denomination> getDenominationsByOrder(Ordering ordering);

    Denomination removeDenomination(RemoveDenominationRequest removeDenominationRequest) throws UserAccessException;

    Ordering setWhoServesOrderNull(SetWhoServesOrderingRequest setWhoServesOrderingRequest) throws
            OrderingNotServingByYouException, NoOrderingWithIdException, UserAccessException;

    void generatePrintPdf(Ordering ordering) throws IOException, PrintingException;

    void cancelDenomination(AddCancelDenominationRequest addCancelDenominationRequest) throws
            UserAccessException, DenominationWithIdNotFoundException;

    List<Dish> getDishesByDishType(DishType dishType);

    Ordering removeOrdering(RemoveOrderingRequest removeOrderingRequest) throws UserAccessException;


}
