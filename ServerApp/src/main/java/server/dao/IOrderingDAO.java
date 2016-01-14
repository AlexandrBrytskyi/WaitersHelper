package server.dao;

import server.exceptions.NoOrderingWithIdException;
import server.model.denomination.Denomination;
import server.model.fund.Fund;
import server.model.order.OrderType;
import server.model.order.Ordering;
import server.model.user.User;

import java.time.LocalDateTime;

/**
 * User: huyti
 * Date: 12.01.2016
 */
public interface IOrderingDAO {

    Ordering addOrder(Ordering ordering);

    Ordering getOrderingById(int id) throws NoOrderingWithIdException;

    Ordering setTimeClientsCome(Ordering ordering, LocalDateTime time);

    Ordering setWhoServesOrder(Ordering ordering, User user);

    Ordering setOrderType(Ordering ordering,OrderType type);

    Ordering addDenominationToOrder(Ordering ordering, Denomination denomination);

    Ordering setDescription(String description, Ordering ordering);

    Ordering setAmountOfPeople(int amount, Ordering ordering);

    Ordering setKO(double ko, Ordering ordering);

    Fund getFinalFund(Ordering ordering);

    Ordering removeOrdering(Ordering ordering);

}
