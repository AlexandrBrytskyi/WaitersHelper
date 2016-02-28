package server.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import server.service.printing.ReportsPDFGenerator;
import transferFiles.exceptions.UserAccessException;
import transferFiles.exceptions.UserFieldIsEmptyException;
import transferFiles.exceptions.WrongLoginException;
import transferFiles.model.denomination.Denomination;
import transferFiles.model.dish.Dish;
import transferFiles.model.dish.ingridient.Ingridient;
import transferFiles.model.order.Ordering;
import transferFiles.model.user.User;
import transferFiles.model.user.UserType;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component("adminService")
@Transactional
@Scope("singleton")
public class AdminService extends WaitersService implements IAdminService {

    @Autowired
    @Qualifier("reportsGenerator")
    private ReportsPDFGenerator reportsPDFGenerator;

    public Ordering removeOrdering(Ordering ordering) {
        return orderingDAO.removeOrdering(ordering);
    }

    public Dish removeDish(Dish dish, User logined) throws UserAccessException {
        if (logined.getType() != UserType.ADMIN) throw new UserAccessException("Only admin can delete");
        return dishDAO.removeDish(dish);
    }

    /*users */
    public User addNewUser(User user) throws UserFieldIsEmptyException, WrongLoginException {
        return userDAO.addNewUser(user);
    }

    public User removeUser(User user) {
        return userDAO.removeUser(user);
    }

    @Override
    public void lockUser(User user, boolean lock) {
        user.setLocked(lock);
        userDAO.mergeUser(user);
    }

    @Override
    public List<Denomination> getSoltDenominationsForReport(LocalDate concreteDate) {
        List<Denomination> founded = denominationDAO.getReadyDenominationsByDate(LocalDateTime.of(concreteDate, LocalTime.of(0, 0)));
        return plusPortions(founded);
    }

    private List<Denomination> plusPortions(List<Denomination> founded) {
        Map<Integer, Denomination> result = new HashMap<Integer, Denomination>();
        if (founded != null)
            for (Denomination denomination : founded) {
                Denomination newDenom = new Denomination();
                newDenom.setDish(denomination.getDish());
                newDenom.setPrice(denomination.getPrice());
                newDenom.setPortion(denomination.getPortion());
                if (!result.containsKey(newDenom.hashCode())) {
                    result.put(newDenom.hashCode(), newDenom);
                } else {
                    Denomination term = result.get(newDenom.hashCode());
                    term.setPortion(term.getPortion() + newDenom.getPortion());
                    term.setPrice(term.getPortion() * term.getDish().getPriceForPortion());
                }
            }
        return new LinkedList<Denomination>(result.values());
    }

    @Override
    public List<Denomination> getSoltDenominationsForReport(LocalDate periodStart, LocalDate periodEnd) {
        List<Denomination> founded = denominationDAO.getReadyDenominationsByDate(LocalDateTime.of(periodStart, LocalTime.of(0, 0)), LocalDateTime.of(periodEnd, LocalTime.of(23, 59)));
        return plusPortions(founded);
    }

    @Override
    public List<Denomination> getRequireDenominationsForReport(LocalDate concreteDate) {
        List<Denomination> founded = denominationDAO.getDenominationsByDate(LocalDateTime.of(concreteDate, LocalTime.of(0, 0)));
        return plusPortions(founded);
    }

    @Override
    public List<Denomination> getRequireDenominationsForReport(LocalDate periodStart, LocalDate periodEnd) {
        List<Denomination> founded = denominationDAO.getDenominationsByDate(LocalDateTime.of(periodStart, LocalTime.of(0, 0)), LocalDateTime.of(periodEnd, LocalTime.of(23, 59)));
        return plusPortions(founded);

    }


    @Override
    public List<Ingridient> countIngridientsForReport(List<Denomination> denominationss) {
        Map<Integer, Ingridient> result = new HashMap<Integer, Ingridient>();
        if (denominationss != null) {
            for (Denomination denomination : denominationss) {
                for (Ingridient ingridient : denomination.getDish().getIngridients()) {
                    Ingridient newIngr = new Ingridient();
                    newIngr.setDish(ingridient.getDish());
                    newIngr.setProduct(ingridient.getProduct());
                    newIngr.setAmount(ingridient.getAmount() * denomination.getPortion());
                    if (!result.containsKey(newIngr.hashCode())) {
                        result.put(newIngr.hashCode(), newIngr);
                    } else {
                        Ingridient term = result.get(newIngr.hashCode());
                        term.setAmount(term.getAmount() + newIngr.getAmount());
                    }
                }
            }
        }
        return new LinkedList<Ingridient>(result.values());
    }

    @Override
    public File generateReport(List<Denomination> denominations, List<Ingridient> ingridients, String headString) throws FileNotFoundException {
        return reportsPDFGenerator.generatePdf(denominations, ingridients, headString);
    }


}
