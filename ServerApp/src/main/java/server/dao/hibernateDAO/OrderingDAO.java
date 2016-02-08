package server.dao.hibernateDAO;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import server.dao.IOrderingDAO;
import server.exceptions.NoOrderingWithIdException;
import server.exceptions.OrderingAlreadyServingException;
import server.exceptions.OrderingNotServingByYouException;
import server.model.denomination.Denomination;
import server.model.fund.Fund;
import server.model.order.OrderType;
import server.model.order.Ordering;
import server.model.user.User;

import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;


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

    public Ordering setWhoServesOrder(Ordering ordering, User user) throws NoOrderingWithIdException, OrderingAlreadyServingException {
        Ordering founded = getOrderingById(ordering.getId());
        if (founded.getWhoServesOrder() == null) {
            ordering.setWhoServesOrder(user);
            try {
                sessionFactory.getCurrentSession().merge(ordering);
                return ordering;
            } catch (Throwable e) {
                LOGGER.error(e);
            }
        } else throw new OrderingAlreadyServingException(ordering);
        return null;
    }

    public Ordering setWhoServesOrderNull(Ordering ordering, User user) throws OrderingNotServingByYouException, NoOrderingWithIdException {
        Ordering founded = getOrderingById(ordering.getId());
        if (founded.getWhoServesOrder().getLogin().equals(user.getLogin())) {
            ordering.setWhoServesOrder(null);
            try {
                sessionFactory.getCurrentSession().merge(ordering);
                return ordering;
            } catch (Throwable e) {
                LOGGER.error(e);
            }
        } else throw new OrderingNotServingByYouException(ordering);


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
            sessionFactory.getCurrentSession().merge(resultFund);
            return resultFund;
        } catch (Throwable e) {
            LOGGER.error(e);
            return null;
        }
    }

    private Fund countPrice(Ordering ordering) {
        double result = 0;
        for (Denomination denomination : sessionFactory.getCurrentSession().get(Ordering.class, ordering.getId()).getDenominations()) {
            result += denomination.getPrice();
        }
        Fund fund = ordering.getFund();
        fund.setPrice(result);
        fund.setFinalPrice(result + result * fund.getKo() - ordering.getAdvancePayment());
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

    @Override
    public List<Ordering> getOrderings(LocalDate concretteDay) {
        try {
            return sessionFactory.getCurrentSession().createCriteria(Ordering.class).
                    add(Restrictions.between("dateClientsCome", (LocalDateTime.of(concretteDay, LocalTime.of(0, 0))),
                            (LocalDateTime.of(concretteDay, LocalTime.of(23, 59))))).
                    list();
        } catch (Throwable e) {
            LOGGER.error(e);
        }
        return null;
    }

    @Override
    public List<Ordering> getOrderings(LocalDate begin, LocalDate end) {
        try {
            return sessionFactory.getCurrentSession().createCriteria(Ordering.class).
                    add(Restrictions.between("dateClientsCome", (LocalDateTime.of(begin, LocalTime.of(0, 0))),
                            (LocalDateTime.of(end, LocalTime.of(23, 59))))).
                    list();
        } catch (Throwable e) {
            LOGGER.error(e);
        }
        return null;
    }

    @Override
    public List<Ordering> getOrderings(User whoTaken, LocalDate concretteDay) {
        try {
            return sessionFactory.getCurrentSession().createCriteria(Ordering.class).
                    add(Restrictions.between("dateClientsCome", (LocalDateTime.of(concretteDay, LocalTime.of(0, 0))),
                            (LocalDateTime.of(concretteDay, LocalTime.of(23, 59))))).
                    add(Restrictions.eq("whoTakenOrder", whoTaken)).
                    list();
        } catch (Throwable e) {
            LOGGER.error(e);
        }
        return null;
    }

    @Override
    public List<Ordering> getOrderings(User whoTaken, LocalDate begin, LocalDate end) {
        try {
            return sessionFactory.getCurrentSession().createCriteria(Ordering.class).
                    add(Restrictions.between("dateClientsCome", (LocalDateTime.of(begin, LocalTime.of(0, 0))),
                            (LocalDateTime.of(end, LocalTime.of(23, 59))))).
                    add(Restrictions.eq("whoTakenOrder", whoTaken)).
                    list();
        } catch (Throwable e) {
            LOGGER.error(e);
        }
        return null;
    }

    @Override
    public List<Ordering> getOrderingsUserServes(User whoServing, LocalDate begin, LocalDate end) {
        try {
            return sessionFactory.getCurrentSession().createCriteria(Ordering.class).
                    add(Restrictions.between("dateClientsCome", (LocalDateTime.of(begin, LocalTime.of(0, 0))),
                            (LocalDateTime.of(end, LocalTime.of(23, 59))))).
                    add(Restrictions.eq("whoServesOrder", whoServing)).
                    list();
        } catch (Throwable e) {
            LOGGER.error(e);
        }
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        try {
            return sessionFactory.getCurrentSession().createQuery("SELECT u FROM User u").list();
        } catch (Throwable e) {
            LOGGER.error(e);
            return null;
        }
    }

    @Override
    public Ordering updateOrdering(Ordering orderingSource) {
        try {
            return (Ordering) sessionFactory.getCurrentSession().merge(orderingSource);
        } catch (Throwable e) {
            LOGGER.error(e);
        }
        return null;
    }


}
