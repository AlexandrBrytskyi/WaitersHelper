package server.service;

import server.exceptions.UserAccessException;
import server.model.denomination.Denomination;
import server.model.user.User;

import java.io.Serializable;


public interface ICookService extends Serializable {

    Denomination cancelDenomination(Denomination denomination, User logined) throws UserAccessException;

    Denomination setDenomStateReady();
}
