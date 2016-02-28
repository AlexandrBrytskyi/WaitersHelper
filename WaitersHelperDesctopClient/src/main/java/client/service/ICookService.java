package client.service;


import transferFiles.exceptions.UserAccessException;
import transferFiles.model.denomination.Denomination;
import transferFiles.model.user.User;

import java.io.Serializable;


public interface ICookService extends Serializable {

    Denomination cancelDenomination(Denomination denomination, User logined) throws UserAccessException;

    Denomination setDenomStateReady();
}
