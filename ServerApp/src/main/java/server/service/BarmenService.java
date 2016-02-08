package server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.dao.IDenominationDAO;
import server.dao.IDishDAO;
import server.dao.IOrderingDAO;
import server.dao.IUserDAO;
import server.exceptions.*;
import server.model.denomination.Denomination;
import server.model.dish.Dish;
import server.model.dish.DishType;
import server.model.fund.Fund;
import server.model.order.Ordering;
import server.model.user.User;

import java.time.LocalDate;
import java.util.List;

@Transactional
@Service("barmenService")
public class BarmenService implements IBarmenService {

    @Autowired
    @Qualifier("hibernateDenominationDAO")
    IDenominationDAO denominationDAO;

    @Autowired
    @Qualifier("Ordering_hibernate_dao")
    IOrderingDAO orderingDAO;

    @Autowired(required = true)
    @Qualifier("hibernateUserDAO")
    IUserDAO userDAO;

    @Autowired(required = true)
    @Qualifier(value = "hibernateDishDAO")
    IDishDAO dishDAO;

    @Override
    public Dish addDish(Dish dish) {
        return dishDAO.addDish(dish);
    }

    @Override
    public Dish updateDish(Dish dish) {
        return dishDAO.updateDish(dish);
    }

    @Override
    public Ordering addOrder(Ordering ordering) {
        return orderingDAO.addOrder(ordering);
    }

    @Override
    public Ordering addDenominationToOrder(Ordering ordering, Denomination denomination) {
        return orderingDAO.addDenominationToOrder(ordering, denomination);
    }

    @Override
    public Ordering setKO(double ko, Ordering ordering) {
        return orderingDAO.setKO(ko, ordering);
    }

    @Override
    public Fund getFinalFund(Ordering ordering) {
        return orderingDAO.getFinalFund(ordering);
    }

    @Override
    public List<Ordering> getOrderings(LocalDate concretteDay) {
        return orderingDAO.getOrderings(concretteDay);
    }

    @Override
    public List<Ordering> getOrderings(LocalDate begin, LocalDate end) {
        return orderingDAO.getOrderings(begin, end);
    }

    @Override
    public List<Ordering> getOrderings(User whoTaken, LocalDate concretteDay) {
        return orderingDAO.getOrderings(whoTaken, concretteDay);
    }

    @Override
    public List<Ordering> getOrderings(User whoTaken, LocalDate begin, LocalDate end) {
        return orderingDAO.getOrderings(whoTaken, begin, end);
    }

    @Override
    public List<Ordering> getOrderingsUserServes(User whoServing, LocalDate begin, LocalDate end) {
        return orderingDAO.getOrderingsUserServes(whoServing, begin, end);
    }

    @Override
    public Ordering updateOrdering(Ordering orderingSource) {
        return orderingDAO.updateOrdering(orderingSource);
    }

    @Override
    public User changeName(User user, String name) {
        user.setName(name);
        return userDAO.mergeUser(user);
    }

    @Override
    public User changePassword(User user, String password) throws WrongPasswordException {
        user.setPass(password);
        return userDAO.mergeUser(user);
    }

    public Denomination addDenomination(Denomination denomination, User logined) throws UserAccessException, NoOrderingWithIdException {
        if (orderingDAO.getOrderingById(denomination.getOrder().getId()).getWhoServesOrder() != null) {
            if (orderingDAO.getOrderingById(denomination.getOrder().getId()).getWhoServesOrder().getLogin().equals(logined.getLogin()))
                return denominationDAO.addDenomination(denomination);
            throw new UserAccessException("Only user who serves can add denomination now");
        }  else {
            return denominationDAO.addDenomination(denomination);
        }
    }


    public List<Denomination> getDenominationsByOrder(Ordering ordering) {
        return denominationDAO.getDenominationsByOrder(ordering);
    }

    @Override
    public Denomination removeDenomination(Denomination denomination) {
        return denominationDAO.removeDenomination(denomination);
    }

    public Dish removeDish(Dish dish) {
        return dishDAO.removeDish(dish);
    }

    @Override
    public Ordering removeOrdering(Ordering source) throws UserAccessException {
        throw new UserAccessException("Only admin can remove orderings");
    }

    public Ordering setWhoServesOrder(Ordering ordering, User user) throws OrderingAlreadyServingException, NoOrderingWithIdException, UserAccessException {
        Ordering updated = orderingDAO.setWhoServesOrder(ordering, user);
        if (updated.getDateClientsCome().toLocalDate().equals(LocalDate.now())) {
            return updated;
        } else {
            throw new UserAccessException("You can serve only orders of today");
        }
    }

    public List<Dish> getDishesByDishType(DishType dishType) {
        return dishDAO.getDishesByDishType(dishType);
    }

    public List<Dish> getAllDishes() {
        return dishDAO.getAllDishes();
    }


    @Override
    public List<User> getAllUsers() {
        return orderingDAO.getAllUsers();
    }

    @Override
    public Ordering setWhoServesOrderNull(Ordering ordering, User user) throws OrderingNotServingByYouException, NoOrderingWithIdException, UserAccessException {
        Ordering updated = orderingDAO.setWhoServesOrderNull(ordering, user);
        if (updated.getDateClientsCome().toLocalDate().equals(LocalDate.now())) {
            return updated;
        } else {
            throw new UserAccessException("You can drop serving only orders of today");
        }
    }

}
