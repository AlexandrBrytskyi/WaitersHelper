package server.service.rest_service;


import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;
import server.persistentModel.ConvertDate;
import transferFiles.exceptions.*;
import transferFiles.model.denomination.Denomination;
import transferFiles.model.dish.Dish;
import transferFiles.model.dish.ingridient.Ingridient;
import transferFiles.model.dish.ingridient.Product;
import transferFiles.model.user.User;
import transferFiles.service.restService.IAdminService;
import transferFiles.service.restService.restRequstObjects.AddIngridientRequest;
import transferFiles.service.restService.restRequstObjects.GenerateReportRequest;
import transferFiles.service.restService.restRequstObjects.GetByDateBeginEndRequest;
import transferFiles.service.restService.restRequstObjects.LockUserRequest;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

@RestController("restAdminService")
@RequestMapping("service/admin")
public class RestAdminService extends RestWaitersService implements IAdminService {


    @Autowired
    @Qualifier("adminService")
    private services.rmiService.IAdminService adminService;


    @RequestMapping(value = "/addNewUser", method = RequestMethod.POST)
    public User addNewUser(@RequestBody User user) throws UserFieldIsEmptyException, WrongLoginException {
        return adminService.addNewUser(user);
    }


    @RequestMapping(value = "/removeUser", method = RequestMethod.POST)
    public User removeUser(@RequestBody User user) {
        return adminService.removeUser(user);
    }

    @Override
    @RequestMapping(value = "/lockUser", method = RequestMethod.POST)
    public void lockUser(@RequestBody LockUserRequest request) {
        adminService.lockUser(request.getUser(), request.isLock());
    }

    @Override
    @RequestMapping(value = "/getSoltDenominationsForReport", method = RequestMethod.POST)
    public List<Denomination> getSoltDenominationsForReport(@RequestBody org.joda.time.LocalDate concreteDate) {
        return adminService.getSoltDenominationsForReport(ConvertDate.toJavaFromJoda(concreteDate));
    }


    @Override
    @RequestMapping(value = "/getSoltDenominationsForReportPeriod", method = RequestMethod.POST)
    public List<Denomination> getSoltDenominationsForReport(@RequestBody GetByDateBeginEndRequest request) {
        return adminService.getSoltDenominationsForReport(ConvertDate.toJavaFromJoda(request.getBegin()), ConvertDate.toJavaFromJoda(request.getEnd()));
    }

    @Override
    @RequestMapping(value = "/getRequireDenominationsForReport", method = RequestMethod.POST)
    public List<Denomination> getRequireDenominationsForReport(@RequestBody LocalDate concreteDate) throws DateException {
        return adminService.getRequireDenominationsForReport(ConvertDate.toJavaFromJoda(concreteDate));
    }

    @RequestMapping(value = "/addProduct", method = RequestMethod.POST)
    public Product addProduct(@RequestBody Product product) {
        return adminService.addProduct(product);
    }

    @RequestMapping(value = "/getAllProducts", method = RequestMethod.GET)
    public List<Product> getAllProducts() {
        return adminService.getAllProducts();
    }

    @Override
    @RequestMapping(value = "/getRequireDenominationsForReportPeriod", method = RequestMethod.POST)
    public List<Denomination> getRequireDenominationsForReport(@RequestBody GetByDateBeginEndRequest request) throws DateException {
        return adminService.getRequireDenominationsForReport(ConvertDate.toJavaFromJoda(request.getBegin()),
                ConvertDate.toJavaFromJoda(request.getEnd()));
    }

    @Override
    @RequestMapping(value = "/countIngridientsForReport", method = RequestMethod.POST)
    public List<Ingridient> countIngridientsForReport(@RequestBody List<Denomination> denominationss) {
        return adminService.countIngridientsForReport(denominationss);
    }

    @RequestMapping(value = "/removeProductById", method = RequestMethod.GET)
    public Product removeProductById(@RequestParam("id") int id) throws ProductByIdNotFoundException, UserAccessException {
        try{
            return adminService.removeProductById(id);
        } catch (Exception e) {
            if (e.getClass().equals(DataIntegrityViolationException.class)) throw new UserAccessException("Product already used, remove it from dish first");
        }
        return null;
    }

    @Override
    @RequestMapping(value = "/generateReport", method = RequestMethod.POST)
    public File generateReport(@RequestBody GenerateReportRequest request) throws FileNotFoundException {
        return adminService.generateReport(request.getDenominations(),
                request.getIngridients(),
                request.getHeadString());
    }


    @RequestMapping(value = "/addIngridient", method = RequestMethod.POST)
    public Ingridient addIngridient(@RequestBody AddIngridientRequest request) {
        return adminService.addIngridient(request.getProduct(), request.getAmount(), request.getDish());
    }

    @RequestMapping(value = "/removeIngridientById", method = RequestMethod.GET)
    public Ingridient removeIngridientById(@RequestParam int id) throws IngridientWithIDNotFoundException {
        return adminService.removeIngridientById(id);
    }


    @RequestMapping(value = "/getIngridientsByDish", method = RequestMethod.POST)
    public List<Ingridient> getIngridientsByDish(@RequestBody Dish dish) {
        return adminService.getIngridientsByDish(dish);
    }
}
