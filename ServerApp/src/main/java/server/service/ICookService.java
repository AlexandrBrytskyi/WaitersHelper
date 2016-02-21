package server.service;

import server.exceptions.UserAccessException;
import server.model.denomination.Denomination;
import server.model.user.User;


public interface ICookService {

    Denomination cancelDenomination(Denomination denomination, User logined) throws UserAccessException;

    Denomination setDenomStateReady();
}
