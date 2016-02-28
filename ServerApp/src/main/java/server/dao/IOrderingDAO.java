package server.dao;

import transferFiles.exceptions.NoOrderingWithIdException;
import transferFiles.exceptions.OrderingAlreadyServingException;
import transferFiles.exceptions.OrderingNotServingByYouException;
import transferFiles.model.denomination.Denomination;
import transferFiles.model.fund.Fund;
import transferFiles.model.order.OrderType;
import transferFiles.model.order.Ordering;
import transferFiles.model.user.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * User: huyti
 * Date: 12.01.2016
 */
public interface IOrderingDAO {

    Ordering addOrder(Ordering ordering);

    Ordering getOrderingById(int id) throws NoOrderingWithIdException;

    Ordering setTimeClientsCome(Ordering ordering, LocalDateTime time);

    Ordering setWhoServesOrder(Ordering ordering, User user) throws NoOrderingWithIdException, OrderingAlreadyServingException;

    Ordering setOrderType(Ordering ordering, OrderType type);

    Ordering addDenominationToOrder(Ordering ordering, Denomination denomination);

    Ordering setDescription(String description, Ordering ordering);

    Ordering setAmountOfPeople(int amount, Ordering ordering);

    Ordering setKO(double ko, Ordering ordering);

    Fund getFinalFund(Ordering ordering);

    Ordering removeOrdering(Ordering ordering);

    List<Ordering> getOrderings(LocalDate concretteDay);

    List<Ordering> getOrderings(LocalDate begin, LocalDate end);

    List<Ordering> getOrderings(User whoTaken, LocalDate concretteDay);

    List<Ordering> getOrderings(User whoTaken, LocalDate begin, LocalDate end);

    List<Ordering> getOrderingsUserServes(User whoServing, LocalDate begin, LocalDate end);

    List<User> getAllUsers();

    Ordering updateOrdering(Ordering orderingSource);

    Ordering setWhoServesOrderNull(Ordering ordering, User user) throws OrderingNotServingByYouException, NoOrderingWithIdException;
}
