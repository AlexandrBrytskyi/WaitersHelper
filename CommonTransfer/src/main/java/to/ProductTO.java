package to;

import java.io.Serializable;

/**
 * Created by serhii on 22.02.16.
 */
public class ProductTO implements Serializable {

    private String name;
    private Mesuarment mes;

    public ProductTO() {
    }

    public ProductTO(String name, Mesuarment mes) {
        this.name = name;
        this.mes = mes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Mesuarment getMes() {
        return mes;
    }

    public void setMes(Mesuarment mes) {
        this.mes = mes;
    }
}
