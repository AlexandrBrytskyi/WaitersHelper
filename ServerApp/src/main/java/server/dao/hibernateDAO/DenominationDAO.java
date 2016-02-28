package server.dao.hibernateDAO;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import server.dao.IDenominationDAO;
import transferFiles.exceptions.DenominationWithIdNotFoundException;
import transferFiles.model.denomination.Denomination;
import transferFiles.model.denomination.DenominationState;
import transferFiles.model.dish.Dish;
import transferFiles.model.order.Ordering;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * User: huyti
 * Date: 10.01.2016
 */
@Repository("hibernateDenominationDAO")
public class DenominationDAO implements IDenominationDAO, Serializable {
    private static final Logger LOGGER = Logger.getLogger(DenominationDAO.class);

    @Autowired(required = true)
    private SessionFactory sessionFactory;

    public Denomination addDenomination(Denomination denomination) {
        try {
            if (denomination.getDish() != null && denomination.getPortion() != 0) countPrice(denomination);
            denomination.setTimeWhenAdded(LocalDateTime.now());
            sessionFactory.getCurrentSession().save(denomination);
            return denomination;
        } catch (Throwable e) {
            LOGGER.error(e);
        }
        return null;
    }

    public Denomination getDenominationById(int id) throws DenominationWithIdNotFoundException {
        Denomination founded = null;
        try {
            founded = sessionFactory.getCurrentSession().get(Denomination.class, id);
        } catch (Throwable e) {
            LOGGER.error(e);
        }
        if (founded == null) throw new DenominationWithIdNotFoundException(id);
        return founded;
    }

    public Denomination setDish(Dish dish, Denomination denomination) {
        try {
            denomination.setDish(dish);
            if (denomination.getPortion() != 0) countPrice(denomination);
            sessionFactory.getCurrentSession().update(denomination);
            return denomination;
        } catch (Throwable e) {
            LOGGER.error(e);
            return null;
        }
    }

    public Denomination setOrder(Ordering ordering, Denomination denomination) {
        try {
            denomination.setOrder(ordering);
            sessionFactory.getCurrentSession().update(denomination);
            return denomination;
        } catch (Throwable e) {
            LOGGER.error(e);
            return null;
        }
    }

    public Denomination setPortion(double portion, Denomination denomination) {
        try {
            denomination.setPortion(portion);
            countPrice(denomination);
            sessionFactory.getCurrentSession().update(denomination);
            return denomination;
        } catch (Throwable e) {
            LOGGER.error(e);
            return null;
        }
    }

    private void countPrice(Denomination denomination) {
        double priceForPortion = denomination.getDish().getPriceForPortion();
        denomination.setPrice(priceForPortion * denomination.getPortion());
    }

    private void setTimeWhenIsReady(Denomination denomination) {
        denomination.setTimeWhenIsReady(LocalDateTime.now());
    }

    public Denomination setDenominationState(DenominationState state, Denomination denomination) {
        try {
            denomination.setState(state);
            if (state == DenominationState.READY) setTimeWhenIsReady(denomination);
            sessionFactory.getCurrentSession().update(denomination);
            return denomination;
        } catch (Throwable e) {
            LOGGER.error(e);
            return null;
        }
    }


    public Denomination removeDenomination(int id) throws DenominationWithIdNotFoundException {
        Denomination founded = getDenominationById(id);
        try {
            sessionFactory.getCurrentSession().delete(founded);
            return founded;
        } catch (Throwable e) {
            LOGGER.error(e);
        }
        return null;
    }

    public Denomination removeDenomination(Denomination denomination) {
        try {
            sessionFactory.getCurrentSession().delete(denomination);
            return denomination;
        } catch (Throwable e) {
            LOGGER.error(e);
        }
        return null;
    }

    public List<Denomination> getDenominationsByOrder(Ordering ordering) {
        try {
            return sessionFactory.getCurrentSession().createQuery("SELECT d FROM Denomination d where d.order = :orderin").
                    setParameter("orderin", ordering).list();
        } catch (Throwable e) {
            LOGGER.error(e);
            return null;
        }
    }

    @Override
    public List<Denomination> getDenominationsByOrderForFund(Ordering ordering) {
        try {
            return sessionFactory.getCurrentSession().createQuery("SELECT d FROM Denomination d where d.order = :orderin and d.state not in :canc").
                    setParameter("orderin", ordering).
                    setParameterList("canc", new DenominationState[]{DenominationState.CANCELED_BY_ADMIN, DenominationState.CANCELED_BY_BARMEN, DenominationState.CANCELED_BY_COCK, DenominationState.CANCELED_BY_BARMEN}).list();
        } catch (Throwable e) {
            LOGGER.error(e);
            return null;
        }
    }

    @Override
    public List<Denomination> getReadyDenominationsByDate(LocalDateTime concreteDate) {
        try {
            return sessionFactory.getCurrentSession().createQuery("SELECT d FROM Denomination d where d.order.dateClientsCome between :date1 and :date2 and d.state = :ready").
                    setParameter("date1", concreteDate).
                    setParameter("date2", LocalDateTime.of(concreteDate.toLocalDate(), LocalTime.of(23, 59))).
                    setParameter("ready", DenominationState.READY).list();
        } catch (Throwable e) {
            LOGGER.error(e);
            return null;
        }
    }

    @Override
    public List<Denomination> getReadyDenominationsByDate(LocalDateTime periodStart, LocalDateTime periodEnd) {
        try {
            return sessionFactory.getCurrentSession().createQuery("SELECT d FROM Denomination d where d.order.dateClientsCome between :dateStart and :dateEnd and d.state = :ready").
                    setParameter("dateStart", periodStart).
                    setParameter("dateEnd", periodEnd).
                    setParameter("ready", DenominationState.READY).list();
        } catch (Throwable e) {
            LOGGER.error(e);
            return null;
        }
    }

    @Override
    public List<Denomination> getDenominationsByDate(LocalDateTime concreteDate) {
        try {
            return sessionFactory.getCurrentSession().createQuery("SELECT d FROM Denomination d where d.order.dateClientsCome between :date1 and :date2").
                    setParameter("date1", concreteDate).
                    setParameter("date2", LocalDateTime.of(concreteDate.toLocalDate(), LocalTime.of(23, 59))).list();
        } catch (Throwable e) {
            LOGGER.error(e);
            return null;
        }
    }

    @Override
    public List<Denomination> getDenominationsByDate(LocalDateTime periodStart, LocalDateTime periodEnd) {
        try {
            return sessionFactory.getCurrentSession().createQuery("SELECT d FROM Denomination d where d.order.dateClientsCome between :dateStart and :dateEnd").
                    setParameter("dateStart", periodStart).
                    setParameter("dateEnd", periodEnd).list();
        } catch (Throwable e) {
            LOGGER.error(e);
            return null;
        }
    }
}
