package server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import server.dao.IDenominationDAO;
import server.exceptions.UserAccessException;
import server.model.denomination.Denomination;
import server.model.denomination.DenominationState;
import server.model.user.User;

import java.io.Serializable;


@Component("cookService")
@Transactional
@Scope("singleton")
public class CookService implements ICookService, Serializable {

    @Autowired
    @Qualifier("hibernateDenominationDAO")
    IDenominationDAO denominationDAO;


    @Override
    public Denomination cancelDenomination(Denomination denomination, User logined) throws UserAccessException {
        if (denomination.getState().equals(DenominationState.READY))
            throw new UserAccessException("Can`t change state, already ready, admin can cancel");
        return denominationDAO.setDenominationState(DenominationState.CANCELED_BY_COCK, denomination);
    }

    @Override
    public Denomination setDenomStateReady() {
        return null;
    }
}
