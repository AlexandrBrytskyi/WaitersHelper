package server.remote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.model.dish.ingridient.Product;
import server.service.IAdminService;
import service.IRemoteService;
import to.Mesuarment;
import to.ProductTO;

import java.util.List;

@Service("iRemoteServiceImpl")
public class IRemoteServiceImpl implements IRemoteService {

    @Autowired
    private IAdminService barmenService;

    @Override
    public ProductTO getProduct() {
        List<Product> productList = barmenService.getAllProducts();
        Product product = productList.get(0);

        // converter from model to "TO Object"
        ProductTO productTO = new ProductTO(product.getName(), Mesuarment.KILOGRAMS);


        return productTO;
    }

    public IAdminService getBarmenService() {
        return barmenService;
    }

    public void setBarmenService(IAdminService barmenService) {
        this.barmenService = barmenService;
    }
}
