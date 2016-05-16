package server.service.rest_service;


import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import server.persistentModel.ConvertDate;
import transferFiles.exceptions.UserFieldIsEmptyException;
import transferFiles.exceptions.WrongLoginException;
import transferFiles.model.denomination.Denomination;
import transferFiles.model.dish.ingridient.Ingridient;
import transferFiles.model.user.User;
import transferFiles.service.restService.IAdminService;
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
    private transferFiles.service.rmiService.IAdminService adminService;



    /*users */

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
    public List<Denomination> getRequireDenominationsForReport(@RequestBody LocalDate concreteDate) {
        return adminService.getRequireDenominationsForReport(ConvertDate.toJavaFromJoda(concreteDate));
    }

    @Override
    @RequestMapping(value = "/getRequireDenominationsForReportPeriod", method = RequestMethod.POST)
    public List<Denomination> getRequireDenominationsForReport(@RequestBody GetByDateBeginEndRequest request) {
        return adminService.getRequireDenominationsForReport(ConvertDate.toJavaFromJoda(request.getBegin()),
                ConvertDate.toJavaFromJoda(request.getEnd()));
    }

    @Override
    @RequestMapping(value = "/countIngridientsForReport", method = RequestMethod.POST)
    public List<Ingridient> countIngridientsForReport(@RequestBody List<Denomination> denominationss) {
        return adminService.countIngridientsForReport(denominationss);
    }

    @Override
    @RequestMapping(value = "/generateReport", method = RequestMethod.POST)
    public File generateReport(@RequestBody GenerateReportRequest request) throws FileNotFoundException {
        return adminService.generateReport(request.getDenominations(),
                request.getIngridients(),
                request.getHeadString());
    }
}
