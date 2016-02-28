package server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import server.dao.IDenominationDAO;
import transferFiles.exceptions.UserAccessException;
import transferFiles.model.denomination.Denomination;
import transferFiles.model.denomination.DenominationState;
import transferFiles.model.user.User;
import transferFiles.service.IRemoteService;

import java.io.Serializable;


@Component("cookService")
@Transactional
@Scope("singleton")
public class CookService implements ICookService, Serializable {

    @Autowired
    @Qualifier("hibernateDenominationDAO")
    IDenominationDAO denominationDAO;

    @Autowired
    IRemoteService converter;


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
