package server.remote;

import transferFiles.model.denomination.Denomination;
import transferFiles.model.dish.Dish;
import transferFiles.model.dish.ingridient.Ingridient;
import transferFiles.model.dish.ingridient.Product;
import transferFiles.model.fund.Fund;
import transferFiles.model.order.Ordering;
import transferFiles.model.user.User;
import org.springframework.stereotype.Service;
import transferFiles.service.IRemoteService;
import transferFiles.to.*;

import java.util.LinkedList;
import java.util.List;

@Service("iRemoteServiceImpl")
public class IRemoteServiceImpl implements IRemoteService {


    @Override
    public ProductTO getProduct(Product product) {
        return new ProductTO(product.getId(), product.getName(), product.getMesuarment());
    }

    @Override
    public List<ProductTO> getListProduct(List<Product> productList) {
        List<ProductTO> productTOs = new LinkedList<ProductTO>();
        for (Product product : productList) {
            productTOs.add(getProduct(product));
        }
        return productTOs;
    }

    @Override
    public IngridientTO getIngridient(Ingridient ingridient, DishTO dishTO) {
        return new IngridientTO(ingridient.getId(), ingridient.getAmount(), getProduct(ingridient.getProduct()), dishTO);
    }

    @Override
    public List<IngridientTO> getListIngridient(List<Ingridient> ingridientList, DishTO dishTO) {
        List<IngridientTO> ingridientTOs = new LinkedList<IngridientTO>();
        for (Ingridient ingridient : ingridientList) {
            ingridientTOs.add(getIngridient(ingridient, dishTO));
        }
        return ingridientTOs;
    }



    @Override
    public DishTO getDish(Dish dish) {
        DishTO dishTO = new DishTO();
        dishTO.setId(dish.getId());
        dishTO.setName(dish.getName());
        dishTO.setType(dish.getType());
        dishTO.setWhoCoockDishType(dish.getWhoCoockDishType());
        dishTO.setPriceForPortion(dish.getPriceForPortion());
        dishTO.setDescription(dish.getDescription());
        dishTO.setAvailable(dish.isAvailable());
        dishTO.setIngridients(getListIngridient(dish.getIngridients(), dishTO));
        return dishTO;
    }

    @Override
    public List<DishTO> getListDish(List<Dish> dishList) {
        List<DishTO> dishTOList = new LinkedList<DishTO>();
        for (Dish dish : dishList) {
            dishTOList.add(getDish(dish));
        }
        return dishTOList;
    }

    @Override
    public DenominationTO getDenomination(Denomination denomination, OrderingTO orderingTO) {
        DenominationTO denominationTO = new DenominationTO(denomination.getId(), getDish(denomination.getDish()),
                denomination.getPortion(), denomination.getPrice(), denomination.getTimeWhenAdded(), denomination.getTimeWhenIsReady(),
                denomination.getState());
        denominationTO.setOrder(orderingTO);
        return denominationTO;
    }

    @Override
    public List<DenominationTO> getListDenomination(List<Denomination> denominationList, OrderingTO orderingTO) {
        List<DenominationTO> denominationTOList = new LinkedList<DenominationTO>();
        for (Denomination denomination : denominationList) {
            denominationTOList.add(getDenomination(denomination, orderingTO));
        }
        return denominationTOList;
    }

    @Override
    public FundTO getFund(Fund fund, OrderingTO orderingTO) {
        return new FundTO(fund.getId(), orderingTO, fund.getKo(), fund.getPrice(), fund.getFinalPrice());
    }

    public OrderingTO getOrdering(Ordering ordering) {
        OrderingTO orderingTO = new OrderingTO(ordering.getId(), ordering.getDateOrderCreated(),
                ordering.getDateClientsCome(), ordering.getAmountOfPeople(),
                ordering.getDescription(), ordering.getAdvancePayment(),
                getUser(ordering.getWhoTakenOrder()), getUser(ordering.getWhoServesOrder()),
                ordering.getType());

        orderingTO.setDenominations(getListDenomination(ordering.getDenominations(),orderingTO));
        orderingTO.setFund(getFund(ordering.getFund(), orderingTO));
        return orderingTO;
    }


    @Override
    public List<OrderingTO> getListOrdering(List<Ordering> orderingList) {
        List<OrderingTO> orderingTOList = new LinkedList<OrderingTO>();
        for (Ordering ordering : orderingList) {
            orderingTOList.add(getOrdering(ordering));
        }
        return orderingTOList;
    }

    @Override
    public UserTO getUser(User user) {
        return new UserTO(user.getName(), user.getLogin(), user.getPass(), user.getType(), user.isLocked());
    }

    @Override
    public List<UserTO> getListUser(List<User> userList) {
        List<UserTO> userTOList = new LinkedList<UserTO>();
        for (User user : userList) {
            userTOList.add(getUser(user));
        }
        return userTOList;
    }

    @Override
    public Product getProduct(ProductTO productTO) {
        Product product = new Product();
        product.setId(productTO.getId());
        product.setMesuarment(productTO.getMesuarment());
        product.setName(productTO.getName());
        return product;
    }

    @Override
    public Ingridient getIngridient(IngridientTO ingridientTO) {
        Ingridient ing = new Ingridient();
        ing.setId(ingridientTO.getId());
        ing.setProduct(getProduct(ingridientTO.getProduct()));
        ing.setAmount(ingridientTO.getAmount());
        ing.setDish(getDish(ingridientTO.getDish()));
        return ing;
    }

    @Override
    public List<Ingridient> getListIngridientFromTO(List<IngridientTO> ingridientTOs) {
        List<Ingridient> ingridientList = new LinkedList<Ingridient>();
        for (IngridientTO ingridientTO : ingridientTOs) {
            ingridientList.add(getIngridient(ingridientTO));
        }
        return ingridientList;
    }

    @Override
    public Dish getDish(DishTO dishTO) {
        Dish dish = new Dish();
        dish.setId(dishTO.getId());
        dish.setName(dishTO.getName());
        dish.setAvailable(dishTO.isAvailable());
        dish.setDescription(dishTO.getDescription());
        dish.setPriceForPortion(dishTO.getPriceForPortion());
        dish.setType(dishTO.getType());
        dish.setWhoCoockDishType(dishTO.getWhoCoockDishType());
        List<Ingridient> ingridientList = new LinkedList<>();
        for (IngridientTO ingridientTO : dishTO.getIngridients()) {
            ingridientList.add(getIngridient(ingridientTO));
        }
        dish.setIngridients(ingridientList);
        return dish;
    }

    @Override
    public Denomination getDenomination(DenominationTO denominationTO)  {
        Denomination den = new Denomination();
        den.setId(denominationTO.getId());
        den.setDish(getDish(denominationTO.getDish()));
        den.setOrder(getOrdering(denominationTO.getOrder()));
        den.setPortion(denominationTO.getPortion());
        den.setPrice(denominationTO.getPrice());
        den.setState(denominationTO.getState());
        den.setTimeWhenAdded(denominationTO.getTimeWhenAdded());
        den.setTimeWhenIsReady(denominationTO.getTimeWhenIsReady());
        return den;
    }

    @Override
    public List<Denomination> getListDenominationFromTO(List<DenominationTO> denominationTOs) {
        List<Denomination> denominations = new LinkedList<Denomination>();
        for (DenominationTO denominationTO : denominationTOs) {
            denominations.add(getDenomination(denominationTO));
        }
        return denominations;
    }

    @Override
    public Fund getFund(FundTO fundTO) {
        Fund fund = new Fund();
        fund.setId(fundTO.getId());
        fund.setOrder(getOrdering(fundTO.getOrder()));
        fund.setFinalPrice(fundTO.getFinalPrice());
        fund.setKo(fundTO.getKo());
        fund.setPrice(fundTO.getPrice());
        return fund;
    }

    @Override
    public Ordering getOrdering(OrderingTO orderingTO){
        Ordering ordering = new Ordering();
        ordering.setId(orderingTO.getId());
        ordering.setAdvancePayment(orderingTO.getAdvancePayment());
        ordering.setDateClientsCome(orderingTO.getDateClientsCome());
        ordering.setWhoServesOrder(getUser(orderingTO.getWhoServesOrder()));
        ordering.setWhoTakenOrder(getUser(orderingTO.getWhoTakenOrder()));
        ordering.setAmountOfPeople(orderingTO.getAmountOfPeople());
        ordering.setDateOrderCreated(orderingTO.getDateOrderCreated());
        ordering.setDescription(orderingTO.getDescription());
        ordering.setType(orderingTO.getType());
        ordering.setFund(getFund(orderingTO.getFund()));
        List<Denomination> denominations = new LinkedList<Denomination>();
        for (DenominationTO denominationTO : orderingTO.getDenominations()) {
            denominations.add(getDenomination(denominationTO));
        }
        ordering.setDenominations(denominations);
        return ordering;
    }

    @Override
    public User getUser(UserTO userTO) {
        User user = new User();
        user.setName(userTO.getName());
        user.setLogin(userTO.getLogin());
        user.setLocked(userTO.isLocked());
        user.setPass(userTO.getPass());
        user.setType(userTO.getType());
        return  user;
    }
}
