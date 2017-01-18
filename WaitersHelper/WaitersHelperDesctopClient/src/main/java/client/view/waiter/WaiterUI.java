package client.view.waiter;

import client.view.account.AccountFrame;
import client.view.barmen.BarmenUIDecorator;
import client.view.dishes.AllDishPanel;
import org.apache.log4j.Logger;
import transferFiles.model.user.User;
import services.rmiService.IWaitersService;
import transferFiles.to.LoginLabel;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;


public class WaiterUI extends BarmenUIDecorator {
    private static final Logger LOGGER = Logger.getLogger(WaiterUI.class);

    private IWaitersService service;


    public WaiterUI(IWaitersService service, User logged) throws HeadlessException {
        super(service, logged);
        this.service = service;
        setTitle("Waiter " + logged.getName());
        initAllDishPanel();
        setChangeListener();
    }


    @Override
    protected void initAllDishPanel() {
        if (service == null) return;
        allDishPanel = new AllDishPanel(service);
        dishesPane.addTab("All Dishes", allDishPanel.getAllDishesPanel());
    }


    protected ChangeListener MymenuTabChangeListener = new ChangeListener() {
        public void stateChanged(ChangeEvent e) {
            if (dishesPanel.isShowing()) {
                allDishPanel.initialiseAllDishesPanel();
                LOGGER.info("initialized all dishes panel");
            }
            if (accountPanel.isShowing()) {
                AccountFrame.getAccountFrame(service, loggedUser);
                LOGGER.info("initialized account frame");
            }
        }
    };


    private void setChangeListener() {
        accountPane.addChangeListener(MymenuTabChangeListener);
    }

    @Override
    public void sendUIToLoginedList() {
        loginLabel = new LoginLabel(loggedUser, loggedUser.getType().toString());
        service.sentUIobjectToValidator(loginLabel);
    }

}
