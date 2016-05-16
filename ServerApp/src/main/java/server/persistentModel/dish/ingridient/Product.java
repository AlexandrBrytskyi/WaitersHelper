package server.persistentModel.dish.ingridient;

import server.persistentModel.IdUtil.IdAutoGenerator;
import transferFiles.model.dish.ingridient.Mesuarment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "product")
public class Product extends IdAutoGenerator {

    @Column
    private String name;


    @Column
    @Enumerated
    private Mesuarment mesuarment;

    public Product() {
    }

    public Product(int id, String name, Mesuarment mesuarment) {
        this.setId(id);
        this.name = name;
        this.mesuarment = mesuarment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Mesuarment getMesuarment() {
        return mesuarment;
    }

    public void setMesuarment(Mesuarment mesuarment) {
        this.mesuarment = mesuarment;
    }

    @Override
    public String toString() {
        return name + ", " + mesuarment.toString();
    }

    public transferFiles.model.dish.ingridient.Product toTransferProduct() {
        return new transferFiles.model.dish.ingridient.Product(this.getId(),this.name, this.mesuarment);
    }

    public static Product toPersistentProduct(transferFiles.model.dish.ingridient.Product product) {
        return new Product(product.getId(), product.getName(), product.getMesuarment());
    }

    public static List<transferFiles.model.dish.ingridient.Product> getListProductTO(List<Product> PersistenseProductList) {
        List<transferFiles.model.dish.ingridient.Product> productsTO = new LinkedList<>();
        for (Product product : PersistenseProductList) {
            productsTO.add(product.toTransferProduct());
        }
        return productsTO;
    }
}
