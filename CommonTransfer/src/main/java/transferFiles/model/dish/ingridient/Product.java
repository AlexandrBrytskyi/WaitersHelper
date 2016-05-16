package transferFiles.model.dish.ingridient;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import transferFiles.model.IdUtil.IdSupport;

@JsonDeserialize(as = Product.class)
@JsonSerialize(as = Product.class)
public class Product extends IdSupport {

    private String name;
    private Mesuarment mesuarment;

    @JsonCreator
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
}
