package transferFiles.service.restService;


import org.joda.time.LocalDate;
import transferFiles.exceptions.UserFieldIsEmptyException;
import transferFiles.exceptions.WrongLoginException;
import transferFiles.model.denomination.Denomination;
import transferFiles.model.dish.ingridient.Ingridient;
import transferFiles.model.user.User;
import transferFiles.service.restService.restRequstObjects.GenerateReportRequest;
import transferFiles.service.restService.restRequstObjects.GetByDateBeginEndRequest;
import transferFiles.service.restService.restRequstObjects.LockUserRequest;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;


public interface IAdminService extends IWaitersService {



    /*User transferFiles.service*/

    User addNewUser(User user) throws UserFieldIsEmptyException, WrongLoginException;

    User removeUser(User user);

    void lockUser(LockUserRequest request);


    List<Denomination> getSoltDenominationsForReport(LocalDate concreteDate);

    List<Denomination> getSoltDenominationsForReport(GetByDateBeginEndRequest request);

    List<Denomination> getRequireDenominationsForReport(LocalDate concreteDate);

    List<Denomination> getRequireDenominationsForReport(GetByDateBeginEndRequest request);

    List<Ingridient> countIngridientsForReport(List<Denomination> denominations);

    File generateReport(GenerateReportRequest request) throws FileNotFoundException;

}
