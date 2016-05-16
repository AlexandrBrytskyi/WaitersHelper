package client.view.admin;

import client.view.account.AccountFrame;
import client.view.barmen.BarmenUIDecorator;
import client.view.dishes.AllDishPanel;
import client.view.products.AllProductPanel;
import client.view.reports.ReportsPanel;
import org.apache.log4j.Logger;
import transferFiles.model.user.User;
import transferFiles.service.rmiService.IAdminService;
import transferFiles.to.LoginLabel;
import transferFiles.to.Loginable;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;


public class AdminUI extends BarmenUIDecorator implements Loginable {

    private JPanel usersPanel;
    private JTabbedPane productsMenuPane;
    private AllProductPanel allProductsPanel;
    private AllUsersPanel allUsersPanel;
    private ReportsPanel reportsPanel;


    private IAdminService service;


    public AdminUI(IAdminService service, User logged) throws HeadlessException {
        super(service, logged);
        System.out.println("creating view");
        LOGGER = Logger.getLogger(AdminUI.class);
        this.service = service;
        setTitle("Administrator");
        initAllDishPanel();
        initAllProductsPanel();
        initAllUsersPanel();
        initReportsPanel();
        setChangeListener();
    }


    private void initAllProductsPanel() {
        productsMenuPane = new JTabbedPane(SwingConstants.TOP);
        accountPane.addTab("Products", productsMenuPane);
        allProductsPanel = new AllProductPanel(service, LOGGER);
        productsMenuPane.addTab("All Products", allProductsPanel.getAllProductsPanel());
    }


    /*this method should be realised by ovverriding, but all dishes panel require concrette transferFiles.service
    * so we need IAdminService transferFiles.to run this method correct, but superclass has in constructor IBarmenService,
    * it would be better if method will be invoked after super(), such as IAdminService won`t be null*/
    protected void initAllDishPanel() {
        if (service == null) return;
        allDishPanel = new AllDishPanel(service, loggedUser);
        dishesPane.addTab("All Dishes", allDishPanel.getAllDishesPanel());
    }


    private void initAllUsersPanel() {
        usersPanel = new JPanel();
        accountPane.addTab("Users", usersPanel);
        allUsersPanel = new AllUsersPanel(service);
        usersPanel.setLayout(new BorderLayout());
        usersPanel.add(allUsersPanel.getAllUsersPanel(), BorderLayout.PAGE_START);
        allUsersPanel.initialiseAllUsersPanel();
    }

    private void initReportsPanel() {
        reportsPanel = new ReportsPanel(service);
        accountPane.addTab("Reports", reportsPanel.getMainPanel());
        accountPane.updateUI();
    }

    protected ChangeListener MymenuTabChangeListener = new ChangeListener() {
        public void stateChanged(ChangeEvent e) {
            if (productsMenuPane.isShowing()) {
                allProductsPanel.initialiseAllProductsPanel();
                LOGGER.info("initialized all product panel");
            }
            if (dishesPanel.isShowing()) {
                allDishPanel.initialiseAllDishesPanel();
                dishesPanel.updateUI();
                LOGGER.info("initialized all dishes panel");
            }
            if (accountPanel.isShowing()) {
                AccountFrame.getAccountFrame(service, loggedUser);
                LOGGER.info("initialized account frame");
            }
            if (usersPanel.isShowing()) {
                allUsersPanel.initialiseAllUsersPanel();
            }
        }
    };


    protected void setChangeListener() {
        super.accountPane.addChangeListener(MymenuTabChangeListener);
    }


    public void sendUIToLoginedList() {
        loginLabel = new LoginLabel(loggedUser, loggedUser.getType().toString());
        service.sentUIobjectToValidator(loginLabel);
    }


}
