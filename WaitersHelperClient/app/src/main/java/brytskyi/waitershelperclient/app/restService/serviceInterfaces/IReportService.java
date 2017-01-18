package brytskyi.waitershelperclient.app.restService.serviceInterfaces;


import org.joda.time.LocalDate;
import transferFiles.exceptions.DateException;
import transferFiles.model.denomination.Denomination;
import transferFiles.model.dish.ingridient.Ingridient;
import transferFiles.service.restService.restRequstObjects.GenerateReportRequest;
import transferFiles.service.restService.restRequstObjects.GetByDateBeginEndRequest;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public interface IReportService {

    List<Denomination> getSoltDenominationsForReport(LocalDate localDate);

    List<Denomination> getSoltDenominationsForReport(GetByDateBeginEndRequest getByDateBeginEndRequest);

    List<Denomination> getRequireDenominationsForReport(LocalDate localDate) throws DateException;

    List<Denomination> getRequireDenominationsForReport(GetByDateBeginEndRequest getByDateBeginEndRequest) throws DateException;

    List<Ingridient> countIngridientsForReport(List<Denomination> list);

    File generateReport(GenerateReportRequest generateReportRequest) throws FileNotFoundException;



}
