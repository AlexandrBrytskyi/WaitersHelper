package server.view.waiter;

import org.apache.log4j.Logger;
import server.model.user.User;
import server.service.IWaitersService;
import server.view.account.AccountFrame;
import server.view.dishes.AllDishPanel;
import server.view.orderings.AllOrderingsPanel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;


public class WaiterUI extends JFrame {
    private static final Logger LOGGER = Logger.getLogger(WaiterUI.class);

    private JPanel mainPanel;
    private JTabbedPane accountPane;
    private JPanel ordersPanel;
    private JPanel dishesPanel;
    private JTabbedPane dishesPane;
    private JTabbedPane ordersPane;
    private JPanel accountPanel;
    private AllDishPanel allDishPanel;
    private AllOrderingsPanel allOrderingsPanel;
    private User loggedUser;
    private IWaitersService service;
    private JPanel myOrderingsPanel;


    public WaiterUI(IWaitersService service, User logged) throws HeadlessException {
        super();
        this.service = service;
        this.loggedUser = logged;
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setSize(900, 700);
        setResizable(false);
        setTitle("Waiter " + logged.getName());
        setVisible(true);
        add(mainPanel);
        initAllDishPanel();
        initAllOrdersPanel();
        accountPane.addChangeListener(menuTabChangeListener);

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


    private ChangeListener menuTabChangeListener = new ChangeListener() {
        public void stateChanged(ChangeEvent e) {
            if (dishesPanel.isShowing()) {
                allDishPanel.initialiseAllDishesPanel();
                LOGGER.info("initialized all dishes panel");
            }
            if (accountPanel.isShowing()) {
                AccountFrame.getAddOrderFrame(service, loggedUser);
                LOGGER.info("initialized account frame");
            }
        }
    };


}
