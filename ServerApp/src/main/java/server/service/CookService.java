package server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import server.dao.IDenominationDAO;
import server.dao.IUserDAO;
import server.service.CoocksMonitor.CoocksMonitor;
import server.validator.IValidator;
import transferFiles.exceptions.DenominationWithIdNotFoundException;
import transferFiles.exceptions.UserAccessException;
import transferFiles.exceptions.WrongPasswordException;
import transferFiles.model.denomination.Denomination;
import transferFiles.model.denomination.DenominationState;
import transferFiles.model.user.User;
import transferFiles.password_utils.Password;
import transferFiles.to.LoginLabel;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.List;


@Component("cookService")
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
        return denominationDAO.setDenominationState(DenominationState.CANCELED_BY_COCK, denomination);
    }

    @Override
    public Denomination setDenomStateReady(Denomination denomination) throws UserAccessException {
        Denomination den = null;
        try {
            den = denominationDAO.getDenominationById(denomination.getId());
        } catch (DenominationWithIdNotFoundException e) {
            throw new UserAccessException("Already removed");
        }
        if (!(den.getState().equals(DenominationState.CANCELED_BY_ADMIN) ||
                den.getState().equals(DenominationState.CANCELED_BY_WAITER)))
            return coocksMonitor.setCurrDenomStateReady(denomination);
        throw new UserAccessException("Cancelled already by " + den.getState());
    }

    @Override
    public void sentUIobjectToValidator(LoginLabel loginLabel) {
        validator.sendLabelToValidator(loginLabel);
    }

    @Override
    public List<Denomination> getWorkingDenoms(User user) {
        return coocksMonitor.getWorkingTask(user);
    }

    @Override
    public List<Denomination> getNewDenominations(User user) {
        return coocksMonitor.getWorkingTask(user);
    }

    @Override
    public List<Denomination> getMessages(User user) {
        return coocksMonitor.getMessage(user);
    }

    @Override
    public User changeName(User user, String name) {
        user.setName(name);
        return userDAO.mergeUser(user);
    }

    @Override
    public User changePassword(User user, String password) throws WrongPasswordException, RemoteException {
        user.setPass(new Password(password).hashCode());
        return userDAO.mergeUser(user);
    }
}
