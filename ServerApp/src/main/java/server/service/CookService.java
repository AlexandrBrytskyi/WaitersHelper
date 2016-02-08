package server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.dao.IDenominationDAO;
import server.model.denomination.Denomination;
import server.model.denomination.DenominationState;


@Service      ("cookService")
@Transactional
public class CookService implements ICookService {

    @Autowired
    @Qualifier("hibernateDenominationDAO")
    IDenominationDAO denominationDAO;

    public Denomination setDenominationState(DenominationState state, Denomination denomination) {
        return denominationDAO.setDenominationState(state, denomination);
    }

}
