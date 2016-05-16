package transferFiles.model.dish;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import transferFiles.model.IdUtil.IdSupport;
import transferFiles.model.dish.ingridient.Ingridient;
import transferFiles.model.user.UserType;

import java.util.List;


@JsonDeserialize(as = Dish.class)
@JsonSerialize(as = Dish.class)
public class Dish extends IdSupport {

    private String name;
    private DishType type;
    private UserType whoCoockDishType;
    private double priceForPortion;
    private String description;
    private boolean available = true;

    private List<Ingridient> ingridients;


    public Dish(int id, String name, DishType type, UserType whoCoockDishType,
                double priceForPortion, String description,
                boolean available, List<Ingridient> ingridients) {
        this.setId(id);
        this.name = name;
        this.type = type;
        this.whoCoockDishType = whoCoockDishType;
        this.priceForPortion = priceForPortion;
        this.description = description;
        this.available = available;
        this.ingridients = ingridients;
    }

    public Dish(int id, String name, DishType type, UserType whoCoockDishType,
                double priceForPortion, String description,
                boolean available) {
        this.setId(id);
        this.name = name;
        this.type = type;
        this.whoCoockDishType = whoCoockDishType;
        this.priceForPortion = priceForPortion;
        this.description = description;
        this.available = available;
        this.ingridients = ingridients;
    }

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
