package server.model.dish;


import server.model.IdUtil.IdAutoGenerator;
import server.model.dish.ingridient.Ingridient;

import javax.persistence.*;
import java.util.List;

@Entity
@Table
public class Dish extends IdAutoGenerator {

    @Column
    private String name;

    @Column
    @Enumerated
    private DishType type;

    @Column
    private double priceForPortion;

    @Column
    private String description;

    @Column
    private boolean available = true;

    @OneToMany(mappedBy = "dish", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<Ingridient> ingridients;


    public Dish() {
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DishType getType() {
        return type;
    }

    public void setType(DishType type) {
        this.type = type;
    }

    public double getPriceForPortion() {
        return priceForPortion;
    }

    public void setPriceForPortion(double priceForPortion) {
        this.priceForPortion = priceForPortion;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Ingridient> getIngridients() {
        return ingridients;
    }

    public void setIngridients(List<Ingridient> ingridients) {
        this.ingridients = ingridients;
    }

    @Override
    public String toString() {
        return name + ",\n" + type;
    }
}
