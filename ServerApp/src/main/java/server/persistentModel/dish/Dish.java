package server.persistentModel.dish;


import server.persistentModel.IdUtil.IdAutoGenerator;
import server.persistentModel.dish.ingridient.Ingridient;
import transferFiles.model.dish.DishType;
import transferFiles.model.user.UserType;

import javax.persistence.*;
import java.util.LinkedList;
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

    public Dish(int id, String name, DishType type, UserType whoCoockDishType, double priceForPortion, String description, boolean available) {
        this.setId(id);
        this.name = name;
        this.type = type;
        this.whoCoockDishType = whoCoockDishType;
        this.priceForPortion = priceForPortion;
        this.description = description;
        this.available = available;
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


    public transferFiles.model.dish.Dish toTransferDish() {
        transferFiles.model.dish.Dish dishTO = new transferFiles.model.dish.Dish(this.getId(),
                this.name, this.getType(), this.getWhoCoockDishType(), this.getPriceForPortion(), this.getDescription(),
                this.isAvailable());
        List<transferFiles.model.dish.ingridient.Ingridient> ingridientsTO = new LinkedList<>();
        for (Ingridient ingridient : ingridients) {
            ingridientsTO.add(ingridient.toTransferIngridient(dishTO));
        }
        dishTO.setIngridients(ingridientsTO);
        return dishTO;
    }

    public transferFiles.model.dish.Dish toTransferDish(transferFiles.model.dish.ingridient.Ingridient ingridientTO) {
        List<transferFiles.model.dish.ingridient.Ingridient> toIngridients = new LinkedList<>();
        transferFiles.model.dish.Dish dishTO = new transferFiles.model.dish.Dish(this.getId(), this.name, this.getType(), this.getWhoCoockDishType(),
                this.getPriceForPortion(), this.getDescription(), this.isAvailable());
        for (Ingridient ingrid : ingridients) {
            if (ingridientTO.getId() == ingrid.getId()) {
                ingridientTO.setDish(dishTO);
                toIngridients.add(ingridientTO);
            } else {
                toIngridients.add(ingrid.toTransferIngridient(dishTO));
            }
        }
        dishTO.setIngridients(toIngridients);
        return dishTO;
    }


    public static Dish toPersistentDish(transferFiles.model.dish.Dish dishTO) {
        if (dishTO == null) return null;
        Dish dish = createPersistentDishFromTO(dishTO);
        List<Ingridient> dishIngrids = new LinkedList<>();
        if (dishTO.getIngridients() != null) {
            for (transferFiles.model.dish.ingridient.Ingridient ingridientTO : dishTO.getIngridients()) {
                dishIngrids.add(Ingridient.toPersistentIngridient(ingridientTO, dish));
            }
            dish.setIngridients(dishIngrids);
        }
        return dish;
    }

    private static Dish createPersistentDishFromTO(transferFiles.model.dish.Dish dishTO) {
        return new Dish(dishTO.getId(), dishTO.getName(),
                dishTO.getType(), dishTO.getWhoCoockDishType(),
                dishTO.getPriceForPortion(), dishTO.getDescription(),
                dishTO.isAvailable());
    }

    public static Dish toPersistentDish(transferFiles.model.dish.Dish dishTO, Ingridient ingridient) {
        Dish dish = createPersistentDishFromTO(dishTO);
        List<Ingridient> dishIngrids = new LinkedList<>();
        for (transferFiles.model.dish.ingridient.Ingridient ingridientTO : dishTO.getIngridients()) {
            if ((ingridient != null) && (ingridientTO.getId() == ingridient.getId())) {
                dishIngrids.add(ingridient);
            } else {
                dishIngrids.add(Ingridient.toPersistentIngridient(ingridientTO, dish));
            }
        }
        dish.setIngridients(dishIngrids);
        return dish;
    }

    public static List<transferFiles.model.dish.Dish> getListTransferDish(List<Dish> persistentDishesList) {
        List<transferFiles.model.dish.Dish> toDishes = new LinkedList<>();
        for (Dish dish : persistentDishesList) {
            toDishes.add(dish.toTransferDish());
        }
        return toDishes;
    }
}
