package client.service;


import transferFiles.exceptions.UserFieldIsEmptyException;
import transferFiles.exceptions.WrongLoginException;
import transferFiles.model.denomination.Denomination;
import transferFiles.model.dish.ingridient.Ingridient;
import transferFiles.model.order.Ordering;
import transferFiles.model.user.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.List;


public interface IAdminService extends IWaitersService {


    Ordering removeOrdering(Ordering ordering);

    /*User transferFiles.service*/

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
