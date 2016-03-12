package transferFiles.model.dish;


import transferFiles.model.IdUtil.IdAutoGenerator;
import transferFiles.model.dish.ingridient.Ingridient;
import transferFiles.model.user.UserType;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "dish")
public class Dish extends IdAutoGenerator {

    @Column
    private String name;

    @Column
    @Enumerated
    private DishType type;

    @Column
    private UserType whoCoockDishType;

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

    public UserType getWhoCoockDishType() {
        return whoCoockDishType;
    }

    public void setWhoCoockDishType(UserType whoCoockDishType) {
        this.whoCoockDishType = whoCoockDishType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Dish dish = (Dish) o;

        if (Double.compare(dish.priceForPortion, priceForPortion) != 0) return false;
        if (name != null ? !name.equals(dish.name) : dish.name != null) return false;
        if (type != dish.type) return false;
        return !(ingridients != null ? !ingridients.equals(dish.ingridients) : dish.ingridients != null);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = name != null ? name.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        temp = Double.doubleToLongBits(priceForPortion);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (ingridients != null ? ingridients.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return name + ",\n" + type;
    }
}
