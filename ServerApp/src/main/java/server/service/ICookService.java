package server.service;

import server.model.denomination.Denomination;
import server.model.denomination.DenominationState;

/**
 * User: huyti
 * Date: 14.01.2016
 */
public interface ICookService {

    Denomination setDenominationState(DenominationState state, Denomination denomination);

}
