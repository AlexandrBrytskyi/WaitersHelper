package server.service.rmi_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.dao.IDenominationDAO;
import server.dao.IUserDAO;
import server.service.CoocksMonitor.CoocksMonitor;
import transferFiles.exceptions.DenominationWithIdNotFoundException;
import transferFiles.exceptions.UserAccessException;
import transferFiles.exceptions.WrongPasswordException;
import transferFiles.model.denomination.Denomination;
import transferFiles.model.denomination.DenominationState;
import transferFiles.model.user.User;
import transferFiles.password_utils.Password;
import services.rmiService.ICookService;
import transferFiles.to.LoginLabel;
import transferFiles.validator.rmiValidator.IValidator;

import java.io.Serializable;
import java.util.List;


@Service("cookService")
@Transactional
@Scope("singleton")
public class CookService implements ICookService, Serializable {

    @Autowired
    @Qualifier("hibernateDenominationDAO")
    IDenominationDAO denominationDAO;

    @Autowired(required = true)
    @Qualifier("hibernateUserDAO")
    IUserDAO userDAO;

    @Autowired(required = true)
    @Qualifier("myValidator")
    IValidator validator;

    @Autowired(required = true)
    CoocksMonitor coocksMonitor;

    @Override
    public Denomination cancelDenomination(Denomination denomination, User logined) throws UserAccessException {
        if (denomination.getState().equals(DenominationState.READY))
            throw new UserAccessException("Can`t change state, already ready, admin can cancel");
        return coocksMonitor.setCurrDenomStateCanceledByCoock(server.persistentModel.denomination.Denomination.toPersistentDenomination(denomination)).toDenominationTO();
    }

    @Override
    public Denomination setDenomStateReady(Denomination denomination) throws UserAccessException {
        server.persistentModel.denomination.Denomination den = null;
        try {
            den = denominationDAO.getDenominationById(denomination.getId());
        } catch (DenominationWithIdNotFoundException e) {
            throw new UserAccessException("Already removed");
        }
        if (!(den.getState().equals(DenominationState.CANCELED_BY_ADMIN) ||
                den.getState().equals(DenominationState.CANCELED_BY_WAITER)))
            return coocksMonitor.setCurrDenomStateReady(server.persistentModel.denomination.Denomination.toPersistentDenomination(denomination)).toDenominationTO();
        throw new UserAccessException("Cancelled already by " + den.getState());
    }

    @Override
    public void sentUIobjectToValidator(LoginLabel loginLabel) {
        validator.sendLabelToValidator(loginLabel);
    }

    @Override
    public List<Denomination> getWorkingDenoms(User user) {
        return server.persistentModel.denomination.Denomination.getListTOdenoms(coocksMonitor.getWorkingTask(server.persistentModel.user.User.toPersistentUser(user)));
    }

    @Override
    public List<Denomination> getNewDenominations(User user) {
        return server.persistentModel.denomination.Denomination.getListTOdenoms(coocksMonitor.getNewTask(server.persistentModel.user.User.toPersistentUser(user)));
    }

    @Override
    public List<Denomination> getMessages(User user) {
        return server.persistentModel.denomination.Denomination.getListTOdenoms(coocksMonitor.getMessage(server.persistentModel.user.User.toPersistentUser(user)));
    }

    @Override
    public User changeName(User user, String name) {
        user.setName(name);
        return userDAO.mergeUser(server.persistentModel.user.User.toPersistentUser(user)).toTransferUser();
    }

    @Override
    public User changePassword(User user, String password) throws WrongPasswordException {
        user.setPass(new Password(password).hashCode());
        return userDAO.mergeUser(server.persistentModel.user.User.toPersistentUser(user)).toTransferUser();
    }
}
