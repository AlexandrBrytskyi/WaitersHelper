package transferFiles.model.dish.ingridient;

import transferFiles.model.IdUtil.IdAutoGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Table;

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
        return  name + ", "  + mesuarment.toString();
    }
}
