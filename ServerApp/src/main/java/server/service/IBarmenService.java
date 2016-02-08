package server.service;


import server.exceptions.*;
import server.model.denomination.Denomination;
import server.model.dish.Dish;
import server.model.dish.DishType;
import server.model.fund.Fund;
import server.model.order.Ordering;
import server.model.user.User;

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

    Denomination removeDenomination(Denomination denomination);

    Dish addDish(Dish dish);

    Dish updateDish(Dish dish);

    List<Dish> getDishesByDishType(DishType dishType);

    List<Dish> getAllDishes();

    Dish removeDish(Dish dish);

    Ordering removeOrdering(Ordering source) throws UserAccessException;

    List<User> getAllUsers();

    Ordering setWhoServesOrderNull(Ordering ordering, User user) throws OrderingNotServingByYouException, NoOrderingWithIdException, UserAccessException;



}
