package server.dao.hibernateDAO;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import server.dao.IDishDAO;
import server.exceptions.NoDishWithIdFoundedException;
import server.model.dish.Dish;
import server.model.dish.DishType;
import server.model.dish.ingridient.Ingridient;

import java.util.List;

/**
 * User: huyti
 * Date: 27.12.2015
 */

@Repository(value = "hibernateDishDAO")
public class DishDAO implements IDishDAO {
    private static final Logger LOGGER = Logger.getLogger(DishDAO.class);

    @Autowired(required = true)
    private SessionFactory sessionFactory;

    public Dish addDish(Dish dish) {
        return saveDish(dish);
    }

    private Dish saveDish(Dish newDish) {
        try {
            sessionFactory.getCurrentSession().save(newDish);
            return newDish;
        } catch (Throwable e) {
            LOGGER.error(e);
            return null;
        }
    }


    public List<Dish> findDishByName(String name) {
        try {
            return sessionFactory.getCurrentSession().createQuery("SELECT d from Dish d where d.name LIKE :nam").
                    setParameter("nam", name).list();
        } catch (Throwable e) {
            LOGGER.error(e);
            return null;
        }
    }

    public Dish getDishById(int id) throws NoDishWithIdFoundedException {
        Dish founded = null;
        try {
            founded = sessionFactory.getCurrentSession().get(Dish.class, id);
        } catch (Throwable e) {
            LOGGER.error(e);
        }
        if (founded == null) throw new NoDishWithIdFoundedException(id);
        return founded;
    }

    public Dish updateDish(Dish dish) {
        try {
            sessionFactory.getCurrentSession().update(dish);
            return dish;
        } catch (Throwable e) {
            LOGGER.error(e+ " while updating dish");
        }
        return null;
    }


    /*As parameter dish given existing in db dish*/
    public Dish setDishType(Dish dish, DishType dishType) {
        try {
            dish.setType(dishType);
            sessionFactory.getCurrentSession().update(dish);
            return dish;
        } catch (Throwable e) {
            LOGGER.error(e);
            return null;
        }
    }

    public Dish setPriceForPortionToDish(double price, Dish dish) {
        try {
            dish.setPriceForPortion(price);
            sessionFactory.getCurrentSession().update(dish);
            return dish;
        } catch (Throwable e) {
            LOGGER.error(e);
            return null;
        }
    }

    public Dish setDescriptionOfDish(String description, Dish dish) {
        try {
            dish.setDescription(description);
            sessionFactory.getCurrentSession().update(dish);
            return dish;
        } catch (Throwable e) {
            LOGGER.error(e);
            return null;
        }
    }

    /*ingridient must already be existing in db*/
    public Dish addIngridientToDish(Ingridient ingridient, Dish dish) {
        try {
            dish.getIngridients().add(ingridient);
            ingridient.setDish(dish);
            sessionFactory.getCurrentSession().update(ingridient);
            sessionFactory.getCurrentSession().update(dish);
            return dish;
        } catch (Throwable e) {
            LOGGER.error(e);
            return null;
        }
    }


    public Dish removeDish(Dish dish) {
        try {
            sessionFactory.getCurrentSession().delete(dish);
            return dish;
        } catch (Throwable e) {
            LOGGER.error(e);
            return null;
        }
    }

    public Dish removeDishById(int id) throws NoDishWithIdFoundedException {
        Dish founded = null;
        try {
            founded = getDishById(id);
            sessionFactory.getCurrentSession().delete(founded);
            return founded;
        } catch (Throwable e) {
            LOGGER.error(e);
            return null;
        }
    }

    public List<Dish> getDishesByDishType(DishType dishType) {
        try {
            return sessionFactory.getCurrentSession().createQuery("SELECT d from Dish d where d.type like :typ").
                    setParameter("typ", dishType).list();
        } catch (Throwable e) {
            LOGGER.error(e);
            return null;
        }
    }

    public List<Dish> getAllDishes() {
        try {
            return sessionFactory.getCurrentSession().createQuery("SELECT d from Dish d").list();
        } catch (Throwable e) {
            LOGGER.error(e);
            return null;
        }
    }

    public List<Ingridient> getAllIngridientsByDishId(int id) {
        try {
            return sessionFactory.getCurrentSession().get(Dish.class, id).getIngridients();
        } catch (Throwable e) {
            LOGGER.error(e);
            return null;
        }
    }
}
