package server.service;


import transferFiles.exceptions.UserAccessException;
import transferFiles.model.denomination.Denomination;
import transferFiles.model.user.User;
import transferFiles.to.IAccountable;
import transferFiles.to.LoginLabel;

import java.io.Serializable;
import java.util.List;


public interface ICookService extends Serializable, IAccountable {

    Denomination cancelDenomination(Denomination denomination, User logined) throws UserAccessException;

    Denomination setDenomStateReady(Denomination denomination) throws UserAccessException;

    void sentUIobjectToValidator(LoginLabel loginLabel);

    List<Denomination> getWorkingDenoms(User user);

    List<Denomination> getNewDenominations(User user);

    List<Denomination> getMessages(User user);
}
