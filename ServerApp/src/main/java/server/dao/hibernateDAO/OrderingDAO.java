package server.dao.hibernateDAO;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import server.dao.IOrderingDAO;
import server.exceptions.NoOrderingWithIdException;
import server.model.denomination.Denomination;
import server.model.fund.Fund;
import server.model.order.OrderType;
import server.model.order.Ordering;
import server.model.user.User;

import java.time.LocalDateTime;


@Repository(value = "Ordering_hibernate_dao")
public class OrderingDAO implements IOrderingDAO {

    private static final Logger LOGGER = Logger.getLogger(OrderingDAO.class);

    @Autowired(required = true)
    private SessionFactory sessionFactory;


    public Ordering addOrder(Ordering ordering) {
        try {
            Fund fund = new Fund();
            fund.setOrder(ordering);
            ordering.setFund(fund);
            ordering.setDateOrderCreated(LocalDateTime.now());
            sessionFactory.getCurrentSession().save(ordering);
            fund.setId(ordering.getId());
            sessionFactory.getCurrentSession().save(fund);
            return ordering;
        } catch (Throwable e) {
            LOGGER.error(e);
        }
        return null;
    }

    public Ordering getOrderingById(int id) throws NoOrderingWithIdException {
        Ordering founded = null;
        try {
            founded = sessionFactory.getCurrentSession().get(Ordering.class, id);
        } catch (Throwable e) {
            LOGGER.error(e);
        }
        if (founded == null) throw new NoOrderingWithIdException(id);
        return founded;
    }

    public Ordering setTimeClientsCome(Ordering ordering, LocalDateTime time) {
        try {
            ordering.setDateClientsCome(time);
            sessionFactory.getCurrentSession().update(ordering);
            return ordering;
        } catch (Throwable e) {
            LOGGER.error(e);
            return null;
        }
    }

    public Ordering setWhoServesOrder(Ordering ordering, User user) {
        try {
            ordering.setWhoServesOrder(user);
            sessionFactory.getCurrentSession().update(ordering);
            return ordering;
        } catch (Throwable e) {
            LOGGER.error(e);
        }
        return null;
    }

    public Ordering setOrderType(Ordering ordering, OrderType type) {
        try {
            ordering.setType(type);
            sessionFactory.getCurrentSession().update(ordering);
            return ordering;
        } catch (Throwable e) {
            LOGGER.error(e);
        }
        return null;
    }

    public Ordering addDenominationToOrder(Ordering ordering, Denomination denomination) {
        try {
            denomination.setOrder(ordering);
            sessionFactory.getCurrentSession().update(denomination);
            return sessionFactory.getCurrentSession().get(Ordering.class, ordering.getId());
        } catch (Throwable e) {
            LOGGER.error(e);
        }
        return null;
    }

    public Ordering setDescription(String description, Ordering ordering) {
        try {
            ordering.setDescription(description);
            sessionFactory.getCurrentSession().update(ordering);
            return ordering;
        } catch (Throwable e) {
            LOGGER.error(e);
        }
        return null;
    }

    public Ordering setAmountOfPeople(int amount, Ordering ordering) {
        try {
            ordering.setAmountOfPeople(amount);
            sessionFactory.getCurrentSession().update(ordering);
            return ordering;
        } catch (Throwable e) {
            LOGGER.error(e);
        }
        return null;
    }

    public Ordering setKO(double ko, Ordering ordering) {
        try {
            Fund fund = ordering.getFund();
            fund.setKo(ko);
            sessionFactory.getCurrentSession().update(fund);
            return ordering;
        } catch (Throwable e) {
            LOGGER.error(e);
        }
        return null;
    }

    public Fund getFinalFund(Ordering ordering) {
        Fund resultFund = countPrice(ordering);
        try {
            sessionFactory.getCurrentSession().update(resultFund);
            return resultFund;
        } catch (Throwable e) {
            LOGGER.error(e);
            return null;
        }
    }

    private Fund countPrice(Ordering ordering) {
        double result = 0;
        for (Denomination denomination : ordering.getDenominations()) {
            result += denomination.getPrice();
        }
        Fund fund = ordering.getFund();
        fund.setPrice(result);
        fund.setFinalPrice(result + result * fund.getKo());
        return fund;
    }

    public Ordering removeOrdering(Ordering ordering) {
        try {
            sessionFactory.getCurrentSession().delete(ordering);
            return ordering;
        } catch (Throwable e) {
            LOGGER.error(e);
        }
        return null;
    }


}
