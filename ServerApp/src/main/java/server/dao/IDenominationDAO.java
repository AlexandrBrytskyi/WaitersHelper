package server.dao;

import server.exceptions.DenominationWithIdNotFoundException;
import server.model.denomination.Denomination;
import server.model.denomination.DenominationState;
import server.model.dish.Dish;
import server.model.order.Ordering;

import java.util.List;

/**
 * User: huyti
 * Date: 10.01.2016
 */
public interface IDenominationDAO {

    Denomination addDenomination(Denomination denomination);

    Denomination getDenominationById(int id) throws DenominationWithIdNotFoundException;

    Denomination setDish(Dish dish, Denomination denomination);

    Denomination setOrder(Ordering ordering, Denomination denomination);

    /*after portion was set counting price for denomination*/
    Denomination setPortion(double portion, Denomination denomination);

    Denomination setDenominationState(DenominationState state, Denomination denomination);

    Denomination removeDenomination(int id) throws DenominationWithIdNotFoundException;

    Denomination removeDenomination(Denomination denomination);

    List<Denomination> getDenominationsByOrder(Ordering ordering);
}
