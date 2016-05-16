package server.dao.hibernateDAO;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import server.dao.IProductDAO;
import server.persistentModel.dish.ingridient.Product;
import transferFiles.exceptions.ProductByIdNotFoundException;
import transferFiles.model.dish.ingridient.Mesuarment;

import java.io.Serializable;
import java.util.List;

@Transactional
@Repository("hibernateProductDAO")
public class ProductDAO implements IProductDAO,Serializable {
    private static final Logger LOGGER = Logger.getLogger(ProductDAO.class);

    @Autowired(required = true)
    private SessionFactory sessionFactory;


    public Product addProduct(Product product) {
        try {
            sessionFactory.getCurrentSession().save(product);
            return product;
        } catch (Exception e) {
            LOGGER.error(e);
            return null;
        }
    }

    public Product setMesuarmentById(int id, Mesuarment mesuarment) throws ProductByIdNotFoundException {
        try {
            Product founded = getProductById(id);
            founded.setMesuarment(mesuarment);
            sessionFactory.getCurrentSession().update(founded);
            return founded;
        } catch (Exception e) {
            LOGGER.error(e);
            return null;
        }
    }

    public Product getProductById(int id) throws ProductByIdNotFoundException {
        Product founded;
        try {
            founded = sessionFactory.getCurrentSession().get(Product.class, id);

        } catch (Exception e) {
            LOGGER.error(e);
            return null;
        }
        if (founded == null) throw new ProductByIdNotFoundException(id);
        return founded;
    }

    public Product removeById(int id) throws ProductByIdNotFoundException {
        Product founded = getProductById(id);
        try {
            sessionFactory.getCurrentSession().delete(founded);
        } catch (Exception e) {
            LOGGER.error(e);
            return null;
        }
        return founded;
    }

    public List<Product> showAll() {
        return sessionFactory.getCurrentSession().createCriteria(Product.class).list();
    }

    public List<Product> getProductsByName(String name) {
        try {
            return sessionFactory.getCurrentSession().
                    createQuery("select p from Product p where p.name like :namm").setParameter("namm", name).
                    list();
        } catch (Throwable e) {
            LOGGER.error(e);
            return null;
        }
    }



}
