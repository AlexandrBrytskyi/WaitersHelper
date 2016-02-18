package server.view.admin;

import org.apache.log4j.Logger;
import server.model.user.User;
import server.service.IAdminService;
import server.view.account.AccountFrame;
import server.view.dishes.AllDishPanel;
import server.view.orderings.AllOrderingsPanel;
import server.view.products.AllProductPanel;
import server.view.reports.ReportsPanel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;


public class AdminUI extends JFrame {
    private static final Logger LOGGER = Logger.getLogger(AdminUI.class);

    private JPanel mainPanel;
    private JTabbedPane accountPane;
    private JPanel usersPanel;
    private JPanel ordersPanel;
    private JPanel dishesPanel;
    private JPanel productsPanel;
    private JTabbedPane productsMenuPane;
    private JTabbedPane dishesPane;
    private JTabbedPane ordersPane;
    private JPanel accountPanel;
    private JPanel reportsPanel;
    private AllProductPanel allProductsPanel;
    private AllDishPanel allDishPanel;
    private AllOrderingsPanel allOrderingsPanel;
    private AllUsersPanel allUsersPanel;
    private ReportsPanel reportsPanell;

    private User loggedUser;
    private JPanel myOrderingsPanel;


    private IAdminService service;


    public AdminUI(IAdminService service, User logged) throws HeadlessException {
        super();
        this.service = service;
        this.loggedUser = logged;
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setSize(900, 700);
        setResizable(false);
        setTitle("Administrator");
        setVisible(true);
        add(mainPanel);
        initAllProductsPanel();
        initAllDishPanel();
        initAllOrdersPanel();
        initAllUsersPanel();
        initReportsPanel();
        accountPane.addChangeListener(menuTabChangeListener);

    }

    private void initAllProductsPanel() {
        allProductsPanel = new AllProductPanel(service, LOGGER);
        productsMenuPane.addTab("All Products", allProductsPanel.getAllProductsPanel());
    }

    private void initAllDishPanel() {
        allDishPanel = new AllDishPanel(service, LOGGER);
        dishesPane.addTab("All Dishes", allDishPanel.getAllDishesPanel());
    }

    private void initAllOrdersPanel() {
        allOrderingsPanel = new AllOrderingsPanel(LOGGER, service, loggedUser);
        ordersPane.addTab("AllOrders", allOrderingsPanel.getAllOrderingsPanel());
        myOrderingsPanel = allOrderingsPanel.getFilteredOrdersPanelUserServesOrdering();
        JScrollPane pane = new JScrollPane(myOrderingsPanel);
        pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        ordersPane.addTab("Orders I Serve", pane);
        allOrderingsPanel.initialiseAllOrdersPanel();
        LOGGER.info("initialized all orders panel");
        ordersPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (ordersPane.getTabRunCount() == 1) {
                    myOrderingsPanel = allOrderingsPanel.getFilteredOrdersPanelUserServesOrdering();
                    LOGGER.info("initialized my orderings panel");
                }
                if (ordersPane.getTabRunCount() == 0) {
                    allOrderingsPanel.initialiseAllOrdersPanel();
                    LOGGER.info("initialized all orders panel");
                }
            }

        });
    }

    private void initAllUsersPanel() {
        allUsersPanel = new AllUsersPanel(service);
        usersPanel.setLayout(new BorderLayout());
        usersPanel.add(allUsersPanel.getAllUsersPanel(), BorderLayout.PAGE_START);
        allUsersPanel.initialiseAllUsersPanel();
    }

    private void initReportsPanel() {
        reportsPanell = new ReportsPanel(service);
        reportsPanel.add(reportsPanell.getMainPanel());
        reportsPanel.updateUI();
    }

    private ChangeListener menuTabChangeListener = new ChangeListener() {
        public void stateChanged(ChangeEvent e) {
            if (productsPanel.isShowing()) {
                allProductsPanel.initialiseAllProductsPanel();
                LOGGER.info("initialized all product panel");
            }
            if (dishesPanel.isShowing()) {
                allDishPanel.initialiseAllDishesPanel();
                LOGGER.info("initialized all dishes panel");
            }
            if (accountPanel.isShowing()) {
                AccountFrame.getAddOrderFrame(service, loggedUser);
                LOGGER.info("initialized account frame");
            }
            if (usersPanel.isShowing()) {
                allUsersPanel.initialiseAllUsersPanel();
            }
        }
    };


}
