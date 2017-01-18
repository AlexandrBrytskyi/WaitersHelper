package transferFiles.service.restService;


import transferFiles.exceptions.UserAccessException;
import transferFiles.model.denomination.Denomination;
import transferFiles.model.user.User;
import transferFiles.service.restService.restRequstObjects.CockCancelDenomRequest;
import transferFiles.to.LoginLabel;

import java.io.Serializable;
import java.util.List;


public interface ICookService extends Serializable, transferFiles.service.restService.IAccountable {

    Denomination cancelDenomination(CockCancelDenomRequest request) throws UserAccessException;

    Denomination setDenomStateReady(Denomination denomination) throws UserAccessException;

    void sentUIobjectToValidator(LoginLabel loginLabel);

    List<Denomination> getWorkingDenoms(User user);

    List<Denomination> getNewDenominations(User user);

    List<Denomination> getMessages(User user);
}
