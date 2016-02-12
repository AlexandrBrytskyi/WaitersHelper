package server.service;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import server.exceptions.*;
import server.model.denomination.Denomination;
import server.model.denomination.DenominationState;
import server.model.dish.Dish;
import server.model.dish.DishType;
import server.model.dish.ingridient.Mesuarment;
import server.model.dish.ingridient.Product;
import server.model.order.OrderType;
import server.model.order.Ordering;
import server.model.user.User;
import server.model.user.UserType;
import server.service.password_utils.Password;
import server.validator.IValidator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;


public class UserServiceTest {

    private static IAdminService service;
    private static IValidator validator;

    @BeforeClass
    public static void intitService() {
        ApplicationContext context = new ClassPathXmlApplicationContext("app-context.xml");
        service = (IAdminService) context.getBean("adminService");
        validator = (IValidator) context.getBean("myValidator");
    }

    public UserServiceTest() {
    }

    @Test
    @Ignore
    public void productAddTest() {
        Product product = new Product();
        product.setMesuarment(Mesuarment.PACKET);
        product.setName("Prirodnie Djerelo");
        service.addProduct(product);
    }

    @Test
    @Ignore
    public void findProductsByNameTest() {
        System.out.println(service.getProductsByName("Prirodnie Djerelo").toString());
    }

    @Test
    @Ignore
    public void showAllproductsTest() {
        System.out.println(service.getAllProducts().toString());
    }

    @Test
    @Ignore
    public void getProductByIdTest() {
        try {
            System.out.println(service.getProductById(1));
        } catch (ProductByIdNotFoundException e) {
            e.printStackTrace();
        }
    }


    @Test
    @Ignore
    public void setMesurementByIdTest() {
        try {
            service.setMesuarmentById(service.getProductsByName("Tea").get(0).getId(), Mesuarment.GRAMS);
        } catch (ProductByIdNotFoundException e) {
            e.printStackTrace();
        }
    }


    @Test
    @Ignore
    public void removeByIdTest() {
        try {
            service.removeProductById(service.getProductsByName("Tea").get(0).getId());
        } catch (ProductByIdNotFoundException e) {
            e.printStackTrace();
        }
    }


    /*WARN! possible to save ingridient with product existing in db*/
    @Test
    @Ignore
    public void addIngridientTest() {
        Product product = null;
        try {
            product = service.getProductById(9);
        } catch (ProductByIdNotFoundException e) {
            e.printStackTrace();
        }
        service.addIngridient(product, 150);
        service.addIngridient(product, 200);
    }

    /*WARN! possible to save ingridient with product and dish existing in db*/
    @Test
    @Ignore
    public void addIngridientWithDishTest() {

    }

    @Test
    @Ignore
    public void setDishOfIngridientTest() {

    }

    @Test
    @Ignore
    public void getIngridientsByProductTest() {
        System.out.println(service.getIngridientsByProductName(service.getProductsByName("kurinoe file").get(0).getName()).toString());
    }

    @Test
    @Ignore
    public void getIngridientByIdTest() {
        try {
            System.out.println(service.getIngridientById(13));
        } catch (IngridientWithIDNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Ignore
    public void removeIngridientByIdTest() {
        try {
            service.removeIngridientById(13);
        } catch (IngridientWithIDNotFoundException e) {
            e.printStackTrace();
        }
    }


    @Test
    @Ignore
    public void getAllIngridientsTest() {
        System.out.println(service.getAllIngridients().toString());
    }


    @Test
    @Ignore
    public void getIngridientsByDishTest() {
        System.out.println(service.getAllIngridientsByDishId(21).toString());
    }


    @Test
    @Ignore
    public void addDishTest() {
//         service.addDish("Zesar", DishType.DISH, 14.00);
        Dish dish = new Dish();
        dish.setName("kurva");
        dish.setPriceForPortion(1000);
        dish.setDescription("teschu deleting");
        dish.setType(DishType.DISH);

        service.addDish(dish);
    }


    @Test
    @Ignore
    public void findDishByNameTest() {
        System.out.println(service.findDishByName("Tea").toString());
    }

    @Test
    @Ignore
    public void getDishByIdTest() {
        try {
            System.out.println(service.getDishById(28));
        } catch (NoDishWithIdFoundedException e) {
            e.printStackTrace();
        }
    }


    @Test
    @Ignore
    public void setDishTypeTest() {
        try {
            service.setDishType(service.getDishById(28), DishType.COFFEE);
        } catch (NoDishWithIdFoundedException e) {
            e.printStackTrace();
        }
    }


    @Test
    @Ignore
    public void setPriceForPortionTest() {
        try {
            service.setPriceForPortionToDish(12.50, service.getDishById(28));
        } catch (NoDishWithIdFoundedException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Ignore
    public void setDescriptionOfDishTest() {
        try {
            service.setDescriptionOfDish("The ahuevshiy chay", service.getDishById(28));
        } catch (NoDishWithIdFoundedException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Ignore
    public void addIngridientToDishTest() {
        try {
            service.addIngridientToDish(service.getIngridientById(38), service.getDishById(37));
            service.addIngridientToDish(service.getIngridientById(39), service.getDishById(37));
        } catch (IngridientWithIDNotFoundException e) {
            e.printStackTrace();
        } catch (NoDishWithIdFoundedException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Ignore
    public void removeIngridientFromDishTest() {
        try {
            System.out.println(service.getAllIngridientsByDishId(37).toString());
            service.removeIngridientById(38);
            System.out.println(service.getAllIngridientsByDishId(37).toString());
        } catch (IngridientWithIDNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Ignore
    public void removeDishTest() {
        try {
            service.removeDish(service.getDishById(37));
        } catch (NoDishWithIdFoundedException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void getAllDishesTest() {
        System.out.println(service.getAllDishes().toString());
    }




  /*denominationTests*/

//    @Test
//    @Ignore
//    public void addDenominationTest() {
//        Denomination denomination = new Denomination();
//        Denomination denomination1 = new Denomination();
//        denomination.setState(DenominationState.JUST_ADDED);
//        try {
//            denomination.setDish(service.getDishById(14));
//        } catch (NoDishWithIdFoundedException e) {
//            e.printStackTrace();
//        }
//        denomination.setPortion(1.5);
//        service.addDenomination(denomination);
//        service.addDenomination(denomination1);
//    }

    @Test
    @Ignore
    public void getDenominationByIdTest() {
        try {
            System.out.println(service.getDenominationById(47));
        } catch (DenominationWithIdNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Ignore
    public void setDishTest() throws DenominationWithIdNotFoundException, NoDishWithIdFoundedException {
        service.setDish(service.getDishById(15), service.getDenominationById(46));
    }


    /*Look*/
    @Test
    @Ignore
    public void setOrderTest() {
//        service.setOrder()
    }

    @Test
    @Ignore
    public void setPortionTest() throws DenominationWithIdNotFoundException {
        service.setPortion(5, service.getDenominationById(46));
    }

//    @Test
//    @Ignore
//    public void setDenominationStateTest() throws DenominationWithIdNotFoundException {
//        service.setDenominationState(DenominationState.READY, service.getDenominationById(46));
//    }

    @Test
    @Ignore
    public void removeDenominationsTest() throws DenominationWithIdNotFoundException, UserAccessException {
        service.removeDenomination(service.getDenominationById(49),null);
        service.removeDenomination(47);
    }

    /*Test after Order Dao creating*/
    @Test
    @Ignore
    public void getDenominationsByOrderTest() {
        Ordering ordering = new Ordering();
        ordering.setId(200);
        System.out.println(service.getDenominationsByOrder(ordering));
    }



    /*ordering tests*/


    @Test
    @Ignore
    public void addOrderingTest() {

        Ordering ordering = new Ordering();
        ordering.setAmountOfPeople(11);
        ordering.setDescription("Huylaki");
        ordering.setType(OrderType.PREVIOUS);
        ordering.setDateClientsCome(LocalDateTime.now());
        try {
            ordering.setWhoTakenOrder(validator.login("vasia", new Password("12345")));
        } catch (WrongLoginException e) {
            e.printStackTrace();
        } catch (WrongPasswordException e) {
            e.printStackTrace();
        } catch (AccountBlockedException e) {
            e.printStackTrace();
        }
        List<Denomination> denominations = new LinkedList<Denomination>();
        Denomination denomination = new Denomination();
        denomination.setPortion(1);
        denomination.setState(DenominationState.JUST_ADDED);
        try {
            denomination.setDish(service.getDishById(52));
        } catch (NoDishWithIdFoundedException e) {
            e.printStackTrace();
        }
        denominations.add(denomination);
        ordering.setDenominations(denominations);
        service.addOrder(ordering);
    }

    @Test
    @Ignore
    public void getOrderByIdTest() {
        try {
            System.out.println(service.getOrderingById(3));
        } catch (NoOrderingWithIdException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Ignore
    public void setTimeClientsComeTest() {
        LocalDateTime time = LocalDateTime.of(2016, 1, 2, 19, 0);
        try {
            service.setTimeClientsCome(service.getOrderingById(3), time);
        } catch (NoOrderingWithIdException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Ignore
    public void setKOTest() {
        try {
            service.setKO(0.1, service.getOrderingById(3));
        } catch (NoOrderingWithIdException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Ignore
    public void addDenominationToOrderTest() {
        try {
            service.addDenominationToOrder(service.getOrderingById(4), service.getDenominationById(46));
            service.addDenominationToOrder(service.getOrderingById(4), service.getDenominationById(48));
        } catch (NoOrderingWithIdException e) {
            e.printStackTrace();
        } catch (DenominationWithIdNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Ignore
    public void getFinalFundTest() {
        try {
            service.setKO(0.10, service.getOrderingById(4));
            System.out.println(service.getFinalFund(service.getOrderingById(4)));
        } catch (NoOrderingWithIdException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Ignore
    public void removeOrderingTest() {
        try {
            service.removeOrdering(service.getOrderingById(4));
        } catch (NoOrderingWithIdException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void addUserTest() {
        User user = new User();
        user.setName("Vasia2");
        user.setLogin("vasia");
        try {
            user.setPass("12345/");
        } catch (WrongPasswordException e) {
            e.printStackTrace();
        }
        user.setType(UserType.ADMIN);
        try {
            service.addNewUser(user);
        } catch (UserFieldIsEmptyException e) {
            e.printStackTrace();
        } catch (WrongLoginException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Ignore
    public void getOrderingByDateTest() {
        System.out.println(service.getOrderings(LocalDate.now()).toString());
    }


}
