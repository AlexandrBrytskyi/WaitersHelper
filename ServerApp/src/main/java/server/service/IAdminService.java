package server.service;

import server.exceptions.UserFieldIsEmptyException;
import server.exceptions.WrongLoginException;
import server.model.denomination.Denomination;
import server.model.dish.ingridient.Ingridient;
import server.model.order.Ordering;
import server.model.user.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.List;


public interface IAdminService extends IWaitersService {


    Ordering removeOrdering(Ordering ordering);

    /*User service*/

    User addNewUser(User user) throws UserFieldIsEmptyException, WrongLoginException;

    User removeUser(User user);

    void lockUser(User user, boolean lock);


    List<Denomination> getSoltDenominationsForReport(LocalDate concreteDate);

    List<Denomination> getSoltDenominationsForReport(LocalDate periodStart, LocalDate periodEnd);

    List<Denomination> getRequireDenominationsForReport(LocalDate concreteDate);

    List<Denomination> getRequireDenominationsForReport(LocalDate periodStart, LocalDate periodEnd);

    List<Ingridient> countIngridientsForReport(List<Denomination> denominations);

    File generateReport(List<Denomination> denominations, List<Ingridient> ingridients, String headString) throws FileNotFoundException;

}
