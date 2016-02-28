//package server.transferFiles.service;
//
//import org.junit.BeforeClass;
//import org.junit.Ignore;
//import org.junit.Test;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.support.ClassPathXmlApplicationContext;
//import transferFiles.exceptions.*;
//import transferFiles.model.denomination.Denomination;
//import transferFiles.model.denomination.DenominationState;
//import transferFiles.model.dish.Dish;
//import transferFiles.model.dish.DishType;
//import transferFiles.model.dish.ingridient.Mesuarment;
//import transferFiles.model.dish.ingridient.Product;
//import transferFiles.model.order.OrderType;
//import transferFiles.model.order.Ordering;
//import transferFiles.model.user.User;
//import transferFiles.model.user.UserType;
//import server.validator.IValidator;
//
//import java.rmi.RemoteException;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.LinkedList;
//import java.util.List;
//
//
//public class UserServiceTest {
//
//    private static IAdminService transferFiles.service;
//    private static IValidator validator;
//
//    @BeforeClass
//    public static void intitService() {
//        ApplicationContext context = new ClassPathXmlApplicationContext("app-context.xml");
//        transferFiles.service = (IAdminService) context.getBean("adminService");
//        validator = (IValidator) context.getBean("myValidator");
//    }
//
//    public UserServiceTest() {
//    }
//
//    @Test
//    @Ignore
//    public void productAddTest() {
//        Product product = new Product();
//        product.setMesuarment(Mesuarment.PACKET);
//        product.setName("Prirodnie Djerelo");
//        transferFiles.service.addProduct(product);
//    }
//
//    @Test
//    @Ignore
//    public void findProductsByNameTest() {
//        System.out.println(transferFiles.service.getProductsByName("Prirodnie Djerelo").toString());
//    }
//
//    @Test
//    @Ignore
//    public void showAllproductsTest() {
//        System.out.println(transferFiles.service.getAllProducts().toString());
//    }
//
//    @Test
//    @Ignore
//    public void getProductByIdTest() {
//        try {
//            System.out.println(transferFiles.service.getProductById(1));
//        } catch (ProductByIdNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    @Test
//    @Ignore
//    public void setMesurementByIdTest() {
//        try {
//            transferFiles.service.setMesuarmentById(transferFiles.service.getProductsByName("Tea").get(0).getId(), Mesuarment.GRAMS);
//        } catch (ProductByIdNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    @Test
//    @Ignore
//    public void removeByIdTest() {
//        try {
//            transferFiles.service.removeProductById(transferFiles.service.getProductsByName("Tea").get(0).getId());
//        } catch (ProductByIdNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    /*WARN! possible transferFiles.to save ingridient with product existing in db*/
//    @Test
//    @Ignore
//    public void addIngridientTest() {
//        Product product = null;
//        try {
//            product = transferFiles.service.getProductById(9);
//        } catch (ProductByIdNotFoundException e) {
//            e.printStackTrace();
//        }
//        transferFiles.service.addIngridient(product, 150);
//        transferFiles.service.addIngridient(product, 200);
//    }
//
//    /*WARN! possible transferFiles.to save ingridient with product and dish existing in db*/
//    @Test
//    @Ignore
//    public void addIngridientWithDishTest() {
//
//    }
//
//    @Test
//    @Ignore
//    public void setDishOfIngridientTest() {
//
//    }
//
//    @Test
//    @Ignore
//    public void getIngridientsByProductTest() {
//        System.out.println(transferFiles.service.getIngridientsByProductName(transferFiles.service.getProductsByName("kurinoe file").get(0).getName()).toString());
//    }
//
//    @Test
//    @Ignore
//    public void getIngridientByIdTest() {
//        try {
//            System.out.println(transferFiles.service.getIngridientById(13));
//        } catch (IngridientWithIDNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    @Ignore
//    public void removeIngridientByIdTest() {
//        try {
//            transferFiles.service.removeIngridientById(13);
//        } catch (IngridientWithIDNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    @Test
//    @Ignore
//    public void getAllIngridientsTest() {
//        System.out.println(transferFiles.service.getAllIngridients().toString());
//    }
//
//
//    @Test
//    @Ignore
//    public void getIngridientsByDishTest() {
//        System.out.println(transferFiles.service.getAllIngridientsByDishId(21).toString());
//    }
//
//
//    @Test
//    @Ignore
//    public void addDishTest() {
////         transferFiles.service.addDish("Zesar", DishType.DISH, 14.00);
//        Dish dish = new Dish();
//        dish.setName("kurva");
//        dish.setPriceForPortion(1000);
//        dish.setDescription("teschu deleting");
//        dish.setType(DishType.DISH);
//
//        transferFiles.service.addDish(dish);
//    }
//
//
//    @Test
//    @Ignore
//    public void findDishByNameTest() {
//        System.out.println(transferFiles.service.findDishByName("Tea").toString());
//    }
//
//    @Test
//    @Ignore
//    public void getDishByIdTest() {
//        try {
//            System.out.println(transferFiles.service.getDishById(28));
//        } catch (NoDishWithIdFoundedException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    @Test
//    @Ignore
//    public void setDishTypeTest() {
//        try {
//            transferFiles.service.setDishType(transferFiles.service.getDishById(28), DishType.COFFEE);
//        } catch (NoDishWithIdFoundedException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    @Test
//    @Ignore
//    public void setPriceForPortionTest() {
//        try {
//            transferFiles.service.setPriceForPortionToDish(12.50, transferFiles.service.getDishById(28));
//        } catch (NoDishWithIdFoundedException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    @Ignore
//    public void setDescriptionOfDishTest() {
//        try {
//            transferFiles.service.setDescriptionOfDish("The ahuevshiy chay", transferFiles.service.getDishById(28));
//        } catch (NoDishWithIdFoundedException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    @Ignore
//    public void addIngridientToDishTest() {
//        try {
//            transferFiles.service.addIngridientToDish(transferFiles.service.getIngridientById(38), transferFiles.service.getDishById(37));
//            transferFiles.service.addIngridientToDish(transferFiles.service.getIngridientById(39), transferFiles.service.getDishById(37));
//        } catch (IngridientWithIDNotFoundException e) {
//            e.printStackTrace();
//        } catch (NoDishWithIdFoundedException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    @Ignore
//    public void removeIngridientFromDishTest() {
//        try {
//            System.out.println(transferFiles.service.getAllIngridientsByDishId(37).toString());
//            transferFiles.service.removeIngridientById(38);
//            System.out.println(transferFiles.service.getAllIngridientsByDishId(37).toString());
//        } catch (IngridientWithIDNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    @Ignore
//    public void removeDishTest() {
//        try {
//            transferFiles.service.removeDish(transferFiles.service.getDishById(37),null);
//        } catch (NoDishWithIdFoundedException e) {
//            e.printStackTrace();
//        } catch (UserAccessException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    @Test
//    public void getAllDishesTest() {
//        System.out.println(transferFiles.service.getAllDishes().toString());
//    }
//
//
//
//
//  /*denominationTests*/
//
////    @Test
////    @Ignore
////    public void addDenominationTest() {
////        Denomination denomination = new Denomination();
////        Denomination denomination1 = new Denomination();
////        denomination.setState(DenominationState.JUST_ADDED);
////        try {
////            denomination.setDish(transferFiles.service.getDishById(14));
////        } catch (NoDishWithIdFoundedException e) {
////            e.printStackTrace();
////        }
////        denomination.setPortion(1.5);
////        transferFiles.service.addDenomination(denomination);
////        transferFiles.service.addDenomination(denomination1);
////    }
//
//    @Test
//    @Ignore
//    public void getDenominationByIdTest() {
//        try {
//            System.out.println(transferFiles.service.getDenominationById(47));
//        } catch (DenominationWithIdNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    @Ignore
//    public void setDishTest() throws DenominationWithIdNotFoundException, NoDishWithIdFoundedException {
//        transferFiles.service.setDish(transferFiles.service.getDishById(15), transferFiles.service.getDenominationById(46));
//    }
//
//
//    /*Look*/
//    @Test
//    @Ignore
//    public void setOrderTest() {
////        transferFiles.service.setOrder()
//    }
//
//    @Test
//    @Ignore
//    public void setPortionTest() throws DenominationWithIdNotFoundException {
//        transferFiles.service.setPortion(5, transferFiles.service.getDenominationById(46));
//    }
//
////    @Test
////    @Ignore
////    public void setDenominationStateTest() throws DenominationWithIdNotFoundException {
////        transferFiles.service.setDenominationState(DenominationState.READY, transferFiles.service.getDenominationById(46));
////    }
//
//    @Test
//    @Ignore
//    public void removeDenominationsTest() throws DenominationWithIdNotFoundException, UserAccessException {
//        transferFiles.service.removeDenomination(transferFiles.service.getDenominationById(49),null);
//        transferFiles.service.removeDenomination(47);
//    }
//
//    /*Test after Order Dao creating*/
//    @Test
//    @Ignore
//    public void getDenominationsByOrderTest() {
//        Ordering ordering = new Ordering();
//        ordering.setId(200);
//        System.out.println(transferFiles.service.getDenominationsByOrder(ordering));
//    }
//
//
//
//    /*ordering tests*/
//
//
//    @Test
//    @Ignore
//    public void addOrderingTest() {
//
//        Ordering ordering = new Ordering();
//        ordering.setAmountOfPeople(11);
//        ordering.setDescription("Huylaki");
//        ordering.setType(OrderType.PREVIOUS);
//        ordering.setDateClientsCome(LocalDateTime.now());
//        try {
//            ordering.setWhoTakenOrder(validator.login("vasia","12345"));
//        } catch (WrongLoginException e) {
//            e.printStackTrace();
//        } catch (WrongPasswordException e) {
//            e.printStackTrace();
//        } catch (AccountBlockedException e) {
//            e.printStackTrace();
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//        List<Denomination> denominations = new LinkedList<Denomination>();
//        Denomination denomination = new Denomination();
//        denomination.setPortion(1);
//        denomination.setState(DenominationState.JUST_ADDED);
//        try {
//            denomination.setDish(transferFiles.service.getDishById(52));
//        } catch (NoDishWithIdFoundedException e) {
//            e.printStackTrace();
//        }
//        denominations.add(denomination);
//        ordering.setDenominations(denominations);
//        transferFiles.service.addOrder(ordering);
//    }
//
//    @Test
//    @Ignore
//    public void getOrderByIdTest() {
//        try {
//            System.out.println(transferFiles.service.getOrderingById(3));
//        } catch (NoOrderingWithIdException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    @Ignore
//    public void setTimeClientsComeTest() {
//        LocalDateTime time = LocalDateTime.of(2016, 1, 2, 19, 0);
//        try {
//            transferFiles.service.setTimeClientsCome(transferFiles.service.getOrderingById(3), time);
//        } catch (NoOrderingWithIdException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    @Ignore
//    public void setKOTest() {
//        try {
//            transferFiles.service.setKO(0.1, transferFiles.service.getOrderingById(3));
//        } catch (NoOrderingWithIdException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    @Ignore
//    public void addDenominationToOrderTest() {
//        try {
//            transferFiles.service.addDenominationToOrder(transferFiles.service.getOrderingById(4), transferFiles.service.getDenominationById(46));
//            transferFiles.service.addDenominationToOrder(transferFiles.service.getOrderingById(4), transferFiles.service.getDenominationById(48));
//        } catch (NoOrderingWithIdException e) {
//            e.printStackTrace();
//        } catch (DenominationWithIdNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    @Ignore
//    public void getFinalFundTest() {
//        try {
//            transferFiles.service.setKO(0.10, transferFiles.service.getOrderingById(4));
//            System.out.println(transferFiles.service.getFinalFund(transferFiles.service.getOrderingById(4)));
//        } catch (NoOrderingWithIdException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    @Ignore
//    public void removeOrderingTest() {
//        try {
//            transferFiles.service.removeOrdering(transferFiles.service.getOrderingById(4));
//        } catch (NoOrderingWithIdException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    @Test
//    public void addUserTest() {
//        User user = new User();
//        user.setName("Vasia2");
//        user.setLogin("vasia");
//        try {
//            user.setPass("12345/");
//        } catch (WrongPasswordException e) {
//            e.printStackTrace();
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//        user.setType(UserType.ADMIN);
//        try {
//            transferFiles.service.addNewUser(user);
//        } catch (UserFieldIsEmptyException e) {
//            e.printStackTrace();
//        } catch (WrongLoginException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    @Ignore
//    public void getOrderingByDateTest() {
//        System.out.println(transferFiles.service.getOrderings(LocalDate.now()).toString());
//    }
//
//
//    @Test
//    @Ignore
//    public void getDenominationsByOrderingForFundTest() {
//        try {
//            System.out.println(transferFiles.service.getDenominationsByOrderForFund(transferFiles.service.getOrderingById(100)));
//        } catch (NoOrderingWithIdException e) {
//            e.printStackTrace();
//        }
//    }
//
//}
