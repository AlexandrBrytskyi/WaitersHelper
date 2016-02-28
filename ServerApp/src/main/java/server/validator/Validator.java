package server.validator;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import server.dao.IUserDAO;
import transferFiles.exceptions.AccountBlockedException;
import transferFiles.exceptions.WrongLoginException;
import transferFiles.exceptions.WrongPasswordException;
import transferFiles.model.user.User;
import transferFiles.password_utils.Password;
import transferFiles.to.LoginLabel;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Transactional
@Service("myValidator")
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON, proxyMode = ScopedProxyMode.INTERFACES)
public class Validator implements IValidator, Serializable {
    private final static Logger LOGGER = Logger.getLogger(Validator.class);

    @Autowired(required = true)
    @Qualifier("hibernateUserDAO")
    IUserDAO userDAO;


    public Validator() {
        Thread thread = new Thread(new LoginedChecker());
        thread.start();
    }

    /*while login called method "login" first, it returns logged User
        * having logged user MainFrame try transferFiles.to get transferFiles.service from validator
        * after transferFiles.service got it try transferFiles.to create UIFrame,
        * UIFrame must implement Loginable so it realises method sendUIToLoginedList()
        * ui is UIFrame, so if UIFrame is null, user will not be logined more
        */

    private Map<LoginLabel,Long> loggedUsersLabels = Collections.synchronizedMap(new HashMap<LoginLabel, Long>());

    public User login(String login, String pass) throws WrongLoginException, WrongPasswordException, AccountBlockedException, RemoteException {
        User logged = userDAO.getUser(login, new Password(pass));
        loggedUsersLabels.put(new LoginLabel(logged, logged.getType().toString()),System.currentTimeMillis());
        LOGGER.info("user " + logged.getName() + " has loged in");
        return logged;
    }


    @Override
    public void sendLabelToValidator(LoginLabel loginLable) {
        loggedUsersLabels.put(loginLable, System.currentTimeMillis());
    }


    public Map<LoginLabel, Long> getLoggedUsersLabels() {
        return loggedUsersLabels;
    }

    private class LoginedChecker implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.currentThread().sleep(30000);
                    checkTimePassAfterLabelsUpdated();
                    System.out.println(getLoggedUsersLabels());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void checkTimePassAfterLabelsUpdated() {
            Iterator<Map.Entry<LoginLabel,Long>> iterator = loggedUsersLabels.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<LoginLabel,Long> pair = iterator.next();
                if ((System.currentTimeMillis() - pair.getValue()) > 40000)
                    iterator.remove();
            }
        }
    }

}
