package server.service;


import server.exceptions.*;
import server.model.denomination.Denomination;
import server.model.denomination.DenominationState;
import server.model.dish.Dish;
import server.model.dish.DishType;
import server.model.fund.Fund;
import server.model.order.Ordering;
import server.model.user.User;

import java.awt.print.PrinterException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface IBarmenService {




    Ordering addOrder(Ordering ordering);

    Ordering addDenominationToOrder(Ordering ordering, Denomination denomination);

    Ordering setKO(double ko, Ordering ordering);

    Fund getFinalFund(Ordering ordering);

    List<Ordering> getOrderings(LocalDate concretteDay);

    List<Ordering> getOrderings(LocalDate begin, LocalDate end);

    List<Ordering> getOrderings(User whoTaken, LocalDate concretteDay);

    List<Ordering> getOrderings(User whoTaken, LocalDate begin, LocalDate end);

    List<Ordering> getOrderingsUserServes(User whoServing, LocalDate begin, LocalDate end);

    Ordering updateOrdering(Ordering orderingSource);

    Ordering setWhoServesOrder(Ordering ordering, User user) throws OrderingAlreadyServingException, NoOrderingWithIdException, UserAccessException;

    User changeName(User user, String name);

    User changePassword(User user, String password) throws WrongPasswordException;

    Denomination addDenomination(Denomination denomination, User logined) throws UserAccessException, NoOrderingWithIdException;

    List<Denomination> getDenominationsByOrder(Ordering ordering);

    List<Denomination> getDenominationsByOrderForFund(Ordering ordering);

    Denomination removeDenomination(Denomination denomination, User logined) throws UserAccessException;

    Denomination changeDenominationState(Denomination denomination, DenominationState state, User logined) throws UserAccessException;

    Dish addDish(Dish dish);

    Dish updateDish(Dish dish);

    List<Dish> getDishesByDishType(DishType dishType);

    List<Dish> getAllDishes();

    Dish removeDish(Dish dish, User logined) throws UserAccessException;

    Ordering removeOrdering(Ordering source, User logined) throws UserAccessException;

    List<User> getAllUsers();

    Ordering setWhoServesOrderNull(Ordering ordering, User user) throws OrderingNotServingByYouException, NoOrderingWithIdException, UserAccessException;

    void generatePrintPdf(Ordering ordering) throws IOException, PrinterException;

    void cancelDenomination(User logined, Denomination selectedDenomination) throws UserAccessException;
}
