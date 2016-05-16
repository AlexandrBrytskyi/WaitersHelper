package server.service.rmi_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.dao.IDenominationDAO;
import server.dao.IDishDAO;
import server.dao.IOrderingDAO;
import server.dao.IUserDAO;
import server.persistentModel.denomination.CurrentDenomination;
import server.service.CoocksMonitor.CoocksMonitor;
import server.service.printing.FundPdfGenerator;
import server.service.printing.PdfPrinter;
import transferFiles.exceptions.*;
import transferFiles.model.denomination.Denomination;
import transferFiles.model.denomination.DenominationState;
import transferFiles.model.dish.Dish;
import transferFiles.model.dish.DishType;
import transferFiles.model.fund.Fund;
import transferFiles.model.order.OrderType;
import transferFiles.model.order.Ordering;
import transferFiles.model.user.User;
import transferFiles.model.user.UserType;
import transferFiles.password_utils.Password;
import transferFiles.service.rmiService.IBarmenService;
import transferFiles.to.LoginLabel;
import transferFiles.validator.rmiValidator.IValidator;

import java.awt.print.PrinterException;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;


@Transactional
@Service("barmenService")
@Scope("singleton")
public class BarmenService implements IBarmenService, Serializable {

    @Autowired(required = true)
    @Qualifier("hibernateDenominationDAO")
    IDenominationDAO denominationDAO;

    @Autowired(required = true)
    @Qualifier("Ordering_hibernate_dao")
    IOrderingDAO orderingDAO;

    @Autowired(required = true)
    @Qualifier("hibernateUserDAO")
    IUserDAO userDAO;

    @Autowired(required = true)
    @Qualifier(value = "hibernateDishDAO")
    IDishDAO dishDAO;

    @Autowired(required = true)
    @Qualifier("myValidator")
    IValidator validator;

    @Autowired(required = true)
    CoocksMonitor coocksMonitor;

    @Autowired(required = true)
    @Qualifier("fundGenerator")
    private FundPdfGenerator fundPdfGenerator;




    @Override
    public Dish addDish(Dish dish) {
        return dishDAO.addDish(server.persistentModel.dish.Dish.toPersistentDish(dish)).toTransferDish();
    }

    @Override
    public Dish updateDish(Dish dish) {
        return dishDAO.updateDish(server.persistentModel.dish.Dish.toPersistentDish(dish)).toTransferDish();
    }

    @Override
    public Ordering addOrder(Ordering ordering) {
        return orderingDAO.addOrder(server.persistentModel.order.Ordering.toPersistentOrdering(ordering)).toTransferOrdering();
    }

    @Override
    public Ordering addDenominationToOrder(Ordering ordering, Denomination denomination) {
        return orderingDAO.addDenominationToOrder(server.persistentModel.order.Ordering.toPersistentOrdering(ordering),
                server.persistentModel.denomination.Denomination.toPersistentDenomination(denomination)).toTransferOrdering();
    }

    @Override
    public Ordering setKO(double ko, Ordering ordering) {
        return orderingDAO.setKO(ko, server.persistentModel.order.Ordering.toPersistentOrdering(ordering)).toTransferOrdering();
    }

    @Override
    public Fund getFinalFund(Ordering ordering) {
        return orderingDAO.getFinalFund(server.persistentModel.order.Ordering.toPersistentOrdering(ordering)).toTransferFund();
    }

    @Override
    public List<Ordering> getOrderings(LocalDate concretteDay) {
        return server.persistentModel.order.Ordering.getTransferOrderingList(orderingDAO.getOrderings(concretteDay));
    }

    @Override
    public List<Ordering> getOrderings(LocalDate begin, LocalDate end) {
        return server.persistentModel.order.Ordering.getTransferOrderingList(orderingDAO.getOrderings(begin, end));
    }

    @Override
    public List<Ordering> getOrderings(User whoTaken, LocalDate concretteDay) {
        return server.persistentModel.order.Ordering.getTransferOrderingList(orderingDAO.getOrderings(server.persistentModel.user.User.toPersistentUser(whoTaken), concretteDay));
    }

    @Override
    public List<Ordering> getOrderings(User whoTaken, LocalDate begin, LocalDate end) {
        return server.persistentModel.order.Ordering.getTransferOrderingList(orderingDAO.getOrderings(server.persistentModel.user.User.toPersistentUser(whoTaken), begin, end));
    }

    @Override
    public List<Ordering> getOrderingsUserServes(User whoServing, LocalDate begin, LocalDate end) {
        return server.persistentModel.order.Ordering.getTransferOrderingList(orderingDAO.getOrderingsUserServes(server.persistentModel.user.User.toPersistentUser(whoServing), begin, end));
    }

    @Override
    public Ordering updateOrdering(Ordering orderingSource) {
        return orderingDAO.updateOrdering(server.persistentModel.order.Ordering.toPersistentOrdering(orderingSource)).toTransferOrdering();
    }

    @Override
    public User changeName(User user, String name) {
        user.setName(name);
        return userDAO.mergeUser(server.persistentModel.user.User.toPersistentUser(user)).toTransferUser();
    }

    @Override
    public User changePassword(User user, String password) throws WrongPasswordException {
        user.setPass(new Password(password).hashCode());
        return userDAO.mergeUser(server.persistentModel.user.User.toPersistentUser(user)).toTransferUser();
    }

    public Denomination addDenomination(Denomination denomination, User logined) throws UserAccessException, NoOrderingWithIdException {
        if (orderingDAO.getOrderingById(denomination.getOrder().getId()).getWhoServesOrder() != null) {
            if (orderingDAO.getOrderingById(denomination.getOrder().getId()).getWhoServesOrder().getLogin().equals(logined.getLogin())) {
                Denomination addedDenenom = denominationDAO.addDenomination(server.persistentModel.denomination.Denomination.toPersistentDenomination(denomination)).toDenominationTO();
                CurrentDenomination curDen = new CurrentDenomination(addedDenenom.getId(), logined.getLogin(), null);
                coocksMonitor.addNewCurrentDenomination(curDen);
                return addedDenenom;
            }
            throw new UserAccessException("Only user who serves can add denomination now");
        } else {
            if (denomination.getOrder().getType().equals(OrderType.PREVIOUS))
                return denominationDAO.addDenomination(server.persistentModel.denomination.Denomination.toPersistentDenomination(denomination)).toDenominationTO();
            throw new UserAccessException("Somebody must serve current order to add denominations");
        }
    }


    public List<Denomination> getDenominationsByOrder(Ordering ordering) {
        return server.persistentModel.denomination.Denomination.getListTOdenoms(
                denominationDAO.getDenominationsByOrder(server.persistentModel.order.Ordering.toPersistentOrdering(ordering)));
    }

    @Override
    public List<Denomination> getDenominationsByOrderForFund(Ordering ordering) {
        return server.persistentModel.denomination.Denomination.getListTOdenoms(
                denominationDAO.getDenominationsByOrderForFund(server.persistentModel.order.Ordering.toPersistentOrdering(ordering)));
    }

    @Override
    public Denomination removeDenomination(Denomination denomination, User logined) throws UserAccessException {
        if (!logined.getType().equals(UserType.ADMIN)) throw new UserAccessException("Only admin can delete");
        if (denomination.getOrder().getWhoServesOrder().getLogin() != logined.getLogin())
            throw new UserAccessException("Can`t remove, ordering is already serving");
        server.persistentModel.denomination.Denomination removed = denominationDAO.removeDenomination(server.persistentModel.denomination.Denomination.toPersistentDenomination(denomination));
        coocksMonitor.removeDenomination(removed);
        return removed.toDenominationTO();
    }


    public Dish removeDish(Dish dish, User logined) throws UserAccessException {
        if (!(logined.getType().equals(UserType.BARMEN) && !dish.getType().equals(DishType.DISH)))
            throw new UserAccessException("Barmen can remove only bar dishes");
        return dishDAO.removeDish(server.persistentModel.dish.Dish.toPersistentDish(dish)).toTransferDish();
    }

    @Override
    public Ordering removeOrdering(Ordering source, User logined) throws UserAccessException {
        if (!logined.getType().equals(UserType.ADMIN)) throw new UserAccessException("Only admin can remove orderings");
        return orderingDAO.removeOrdering(server.persistentModel.order.Ordering.toPersistentOrdering(source)).toTransferOrdering();
    }

    public Ordering setWhoServesOrder(Ordering ordering, User user) throws OrderingAlreadyServingException, NoOrderingWithIdException, UserAccessException {
        server.persistentModel.order.Ordering updated = orderingDAO.getOrderingById(ordering.getId());
        if (updated.getDateClientsCome().toLocalDate().equals(LocalDate.now())) {
            return orderingDAO.setWhoServesOrder(updated, server.persistentModel.user.User.toPersistentUser(user)).toTransferOrdering();
        } else {
            throw new UserAccessException("You can serve only orders of today");
        }
    }

    public List<Dish> getDishesByDishType(DishType dishType) {
        return server.persistentModel.dish.Dish.getListTransferDish(dishDAO.getDishesByDishType(dishType));
    }

    public List<Dish> getAllDishes() {
        return server.persistentModel.dish.Dish.getListTransferDish(dishDAO.getAllDishes());
    }


    @Override
    public List<User> getAllUsers() {
        return server.persistentModel.user.User.getListTOUsers(orderingDAO.getAllUsers());
    }

    @Override
    public Ordering setWhoServesOrderNull(Ordering ordering, User user) throws OrderingNotServingByYouException, NoOrderingWithIdException, UserAccessException {
        server.persistentModel.order.Ordering updated = orderingDAO.getOrderingById(ordering.getId());
        if (updated.getDateClientsCome().toLocalDate().equals(LocalDate.now())) {
            return orderingDAO.setWhoServesOrderNull(updated, server.persistentModel.user.User.toPersistentUser(user)).toTransferOrdering();
        } else {
            throw new UserAccessException("You can drop serving only orders of today");
        }
    }

    @Override
    public void generatePrintPdf(Ordering ordering) throws IOException, PrinterException {
        PdfPrinter.print(fundPdfGenerator.generatePdf(ordering));
    }

    //    this is for denominations you serve
    @Override
    public void cancelDenomination(User serving, Denomination selectedDenomination) throws UserAccessException, DenominationWithIdNotFoundException {
        if (!selectedDenomination.getOrder().getWhoServesOrder().getLogin().equals(serving.getLogin()) && !serving.getType().equals(UserType.ADMIN))
            throw new UserAccessException("You can`t cancel, you don`n serve, admin can");
        if (serving.getType().equals(UserType.BARMEN) || serving.getType().equals(UserType.WAITER))

            if (denominationDAO.getDenominationById(selectedDenomination.getId()).getState().equals(DenominationState.READY)) {
                throw new UserAccessException("Already ready, only admin can cancel");
            } else {
                coocksMonitor.setCurrDenomStateCanceledByWaiter(server.persistentModel.denomination.Denomination.toPersistentDenomination(selectedDenomination));
            }

        if (serving.getType().equals(UserType.ADMIN))
            coocksMonitor.setCurrDenomStateCanceledByAdmin(server.persistentModel.denomination.Denomination.toPersistentDenomination(selectedDenomination));
    }

    @Override
    public List<Denomination> getMessages(User user) {
        return server.persistentModel.denomination.Denomination.getListTOdenoms(coocksMonitor.getMessage(server.persistentModel.user.User.toPersistentUser(user)));
    }

    //    this is for denomination you coock
    @Override
    public Denomination cancelDenomination(Denomination denomination, User logined) throws UserAccessException {
        if (denomination.getDish().getWhoCoockDishType().equals(logined.getType()))
            return coocksMonitor.setCurrDenomStateCanceledByBarmen(server.persistentModel.denomination.Denomination.toPersistentDenomination(denomination)).toDenominationTO();
        throw new UserAccessException("You cant cancel");
    }

    @Override
    public Denomination setDenomStateReady(Denomination denomination) throws UserAccessException {
        server.persistentModel.denomination.Denomination den = null;
        try {
            den = denominationDAO.getDenominationById(denomination.getId());
        } catch (DenominationWithIdNotFoundException e) {
            throw new UserAccessException("Already removed");
        }
        if (!(den.getState().equals(DenominationState.CANCELED_BY_ADMIN) ||
                den.getState().equals(DenominationState.CANCELED_BY_WAITER)))
            return coocksMonitor.setCurrDenomStateReady(server.persistentModel.denomination.Denomination.toPersistentDenomination(denomination)).toDenominationTO();
        throw new UserAccessException("Cancelled already by " + den.getState());
    }

    @Override
    public void sentUIobjectToValidator(LoginLabel loginLabel) {

    }

    @Override
    public List<Denomination> getWorkingDenoms(User user) {
        return server.persistentModel.denomination.Denomination.getListTOdenoms(coocksMonitor.getWorkingTask(server.persistentModel.user.User.toPersistentUser(user)));
    }

    @Override
    public List<Denomination> getNewDenominations(User user) {
        return server.persistentModel.denomination.Denomination.getListTOdenoms(coocksMonitor.getNewTask(server.persistentModel.user.User.toPersistentUser(user)));
    }

}
