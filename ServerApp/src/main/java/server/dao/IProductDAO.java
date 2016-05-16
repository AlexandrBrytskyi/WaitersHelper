package server.dao;


import server.persistentModel.dish.ingridient.Product;
import transferFiles.exceptions.ProductByIdNotFoundException;
import transferFiles.model.dish.ingridient.Mesuarment;

import java.util.List;

public interface IProductDAO {

    Product addProduct(Product product);

    Product setMesuarmentById(int id, Mesuarment mesuarment) throws ProductByIdNotFoundException;

    Product getProductById(int id) throws ProductByIdNotFoundException;

    Product removeById(int id) throws ProductByIdNotFoundException;

    List<Product> showAll();

    List<Product> getProductsByName(String name);



}
