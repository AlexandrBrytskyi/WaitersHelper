package server.dao;

import server.persistentModel.denomination.CurrentDenomination;
import server.persistentModel.denomination.Denomination;
import server.persistentModel.dish.Dish;
import server.persistentModel.order.Ordering;
import transferFiles.exceptions.DenominationWithIdNotFoundException;
import transferFiles.model.denomination.DenominationState;

import java.time.LocalDateTime;
import java.util.List;

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

    List<Denomination> getDenominationsByOrderForFund(Ordering ordering);

    List<Denomination> getReadyDenominationsByDate(LocalDateTime concreteDate);

    List<Denomination> getReadyDenominationsByDate(LocalDateTime periodStart, LocalDateTime periodEnd);

    List<Denomination> getDenominationsByDate(LocalDateTime concreteDate);

    List<Denomination> getDenominationsByDate(LocalDateTime periodStart, LocalDateTime periodEnd);

    List<CurrentDenomination> getCurrentDenominations();

    CurrentDenomination getCurrentDenomination(int id);

    CurrentDenomination removeCurrentDenomination(int id);

    CurrentDenomination mergeCurrentDenomination(CurrentDenomination currentDenomination);

    CurrentDenomination addCurrentDenomination(CurrentDenomination currentDenomination);
}
