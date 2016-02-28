package transferFiles.service;

import transferFiles.model.denomination.Denomination;
import transferFiles.model.dish.Dish;
import transferFiles.model.dish.ingridient.Ingridient;
import transferFiles.model.dish.ingridient.Product;
import transferFiles.model.fund.Fund;
import transferFiles.model.order.Ordering;
import transferFiles.model.user.User;
import transferFiles.to.*;

import java.util.List;


public interface IRemoteService {

    ProductTO getProduct(Product product);

    List<ProductTO> getListProduct(List<Product> productList);

    IngridientTO getIngridient(Ingridient ingridient, DishTO dishTO);

    List<IngridientTO> getListIngridient(List<Ingridient> ingridientList, DishTO dishTO);

    DishTO getDish(Dish dish);

    List<DishTO> getListDish(List<Dish> dishList);

    DenominationTO getDenomination(Denomination denomination, OrderingTO orderingTO);

    List<DenominationTO> getListDenomination(List<Denomination> denominationList, OrderingTO orderingTO) ;

    FundTO getFund(Fund fund, OrderingTO orderingTO);

    OrderingTO getOrdering(Ordering ordering);

    List<OrderingTO> getListOrdering(List<Ordering> orderingList);

    UserTO getUser(User user);

    List<UserTO> getListUser(List<User> userList);

    Product getProduct(ProductTO productTO);

    Ingridient getIngridient(IngridientTO ingridientTO);

    List<Ingridient> getListIngridientFromTO(List<IngridientTO> ingridientTOs);

    Dish getDish(DishTO dishTO);

    Denomination getDenomination(DenominationTO denominationTO);

    List<Denomination> getListDenominationFromTO(List<DenominationTO> denominationTOs);

    Fund getFund(FundTO fundTO);

    Ordering getOrdering(OrderingTO orderingTO);

    User getUser(UserTO userTO);




}
