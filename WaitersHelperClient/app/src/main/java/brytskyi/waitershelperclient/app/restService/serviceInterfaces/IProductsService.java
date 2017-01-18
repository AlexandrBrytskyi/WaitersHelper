package brytskyi.waitershelperclient.app.restService.serviceInterfaces;


import transferFiles.exceptions.ProductByIdNotFoundException;
import transferFiles.exceptions.UserAccessException;
import transferFiles.model.dish.ingridient.Product;

import java.util.List;

public interface IProductsService {

    Product addProduct(Product product);

    Product removeProductById(int id) throws ProductByIdNotFoundException, UserAccessException;

    List<Product> getAllProducts();
}
