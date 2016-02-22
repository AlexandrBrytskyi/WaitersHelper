package server.dao.hibernateDAO;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import server.dao.IIngredientDAO;
import server.exceptions.IngridientWithIDNotFoundException;
import server.model.dish.Dish;
import server.model.dish.ingridient.Ingridient;
import server.model.dish.ingridient.Product;

import java.io.Serializable;
import java.util.List;

/**
 * User: huyti
 * Date: 27.12.2015
 */
@Repository(value = "hibernateIngridientDAO")
public class IngridientDao implements IIngredientDAO,Serializable {
    private static final Logger LOGGER = Logger.getLogger(ProductDAO.class);

    @Autowired(required = true)
    private SessionFactory sessionFactory;


    public Ingridient addIngridient(Product product, double amount) {
        Ingridient newIngridient = new Ingridient();
        newIngridient.setProduct(product);
        newIngridient.setAmount(amount);
        try {
            sessionFactory.getCurrentSession().save(newIngridient);
            return newIngridient;
        } catch (Throwable e) {
            LOGGER.error(e);
        }
        return null;
    }

    public Ingridient addIngridient(Product product, double amount, Dish dish) {
        Ingridient newIngridient = new Ingridient();
        newIngridient.setProduct(product);
        newIngridient.setAmount(amount);
        newIngridient.setDish(dish);
        try {
            sessionFactory.getCurrentSession().save(newIngridient);
            return newIngridient;
        } catch (Throwable e) {
            LOGGER.error(e);
        }
        return null;
    }

    public Ingridient setDishOfIngridient(int id, Dish dish) throws IngridientWithIDNotFoundException {
        Ingridient founded = getIngridientById(id);
        founded.setDish(dish);
        try {
            sessionFactory.getCurrentSession().update(founded);
            return founded;
        } catch (Throwable e) {
            LOGGER.error(e);
        }
        return null;
    }

    public List<Ingridient> getIngridientsByProductName(String name) {
        try {
            return sessionFactory.getCurrentSession().
                    createQuery("SELECT ing from Ingridient ing where ing.product.name like :namee").
                    setParameter("prod", name).list();
        } catch (Throwable e) {
            LOGGER.error(e);
            return null;
        }
    }

    public Ingridient getIngridientById(int id) throws IngridientWithIDNotFoundException {
        Ingridient founded = null;
        try {
            founded = sessionFactory.getCurrentSession().get(Ingridient.class, id);
        } catch (Throwable e) {
            LOGGER.error(e);
        }
        if (founded == null) throw new IngridientWithIDNotFoundException(id);
        return founded;
    }

    public Ingridient removeIngridientById(int id) throws IngridientWithIDNotFoundException {
        Ingridient founded = getIngridientById(id);
        try {
            sessionFactory.getCurrentSession().delete(founded);
            return founded;
        } catch (Throwable e) {
            LOGGER.error(e);
            return null;
        }
    }

    public List<Ingridient> getAllIngridients() {
        try {
            return sessionFactory.getCurrentSession().createCriteria(Ingridient.class).list();
        } catch (Throwable e) {
            LOGGER.error(e);
            return null;
        }
    }

    public List<Ingridient> getIngridientsByDish(Dish dish) {
        try {
            return sessionFactory.getCurrentSession().
                    createQuery("SELECT ing from Ingridient ing where ing.dish like :dishh").
                    setParameter("dishh", dish).list();
        } catch (Throwable e) {
            LOGGER.error(e);
            return null;
        }
    }
}
