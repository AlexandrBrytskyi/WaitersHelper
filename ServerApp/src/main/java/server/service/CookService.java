package server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.dao.IDenominationDAO;
import server.exceptions.UserAccessException;
import server.model.denomination.Denomination;
import server.model.denomination.DenominationState;
import server.model.user.User;


@Service("cookService")
@Transactional
public class CookService implements ICookService {

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
