package client.view.barmen;

import client.view.account.AccountFrame;
import client.view.dishes.AllDishPanel;
import client.view.orderings.AllOrderingsPanel;
import org.apache.log4j.Logger;
import server.model.user.User;
import server.service.IBarmenService;
import server.validator.Loginable;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;


public class BarmenUI extends JFrame implements Loginable {
    protected static Logger LOGGER = Logger.getLogger(BarmenUI.class);

    private JPanel mainPanel;
    protected JTabbedPane accountPane;
    private JPanel ordersPanel;
    protected JPanel dishesPanel;
    protected JTabbedPane dishesPane;
    private JTabbedPane ordersPane;
    protected JPanel accountPanel;
    protected AllDishPanel allDishPanel;
    protected AllOrderingsPanel allOrderingsPanel;
    protected JPanel myOrderingsPanel;
    protected User loggedUser;
    protected Object sentService;


    private IBarmenService service;


    public BarmenUI(IBarmenService service, User logged) throws HeadlessException {
        super();
        this.service = service;
        this.loggedUser = logged;
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(900, 700);
        setResizable(false);
        setTitle("Barmen " + logged.getName());
        setVisible(true);
        add(mainPanel);
        initAllDishPanel();
        initAllOrdersPanel();
        accountPane.addChangeListener(menuTabChangeListener);
    }


    protected void initAllDishPanel() {
        allDishPanel = new AllDishPanel(service,loggedUser);
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


    private ChangeListener menuTabChangeListener = new ChangeListener() {
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


    public void sendUIToLoginedList() {
        service.sentUIobjectToValidator(loggedUser,this);
    }
}
