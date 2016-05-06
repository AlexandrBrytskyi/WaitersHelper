package client.view.barmen;

import client.service.IBarmenService;
import client.view.account.AccountFrame;
import client.view.cook.WorkPanel;
import client.view.dialog.JDialogExt;
import client.view.dishes.AllDishPanel;
import client.view.orderings.AllOrderingsPanel;
import client.view.orderings.DenominationsPanel;
import org.apache.log4j.Logger;
import transferFiles.model.denomination.Denomination;
import transferFiles.model.user.User;
import transferFiles.model.user.UserType;
import transferFiles.to.LoginLabel;
import transferFiles.to.Loginable;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class BarmenUI extends JFrame implements Loginable {
    protected static Logger LOGGER = Logger.getLogger(BarmenUI.class);

    private JPanel mainPanel;
    protected JTabbedPane accountPane;
    private JPanel ordersPanel;
    protected JPanel dishesPanel;
    protected JTabbedPane dishesPane;
    private JTabbedPane ordersPane;
    protected JPanel accountPanel;
    private JPanel workPanel;
    private WorkPanel workPanelExemp;
    protected AllDishPanel allDishPanel;
    protected AllOrderingsPanel allOrderingsPanel;
    protected JPanel myOrderingsPanel;
    protected User loggedUser;
    protected LoginLabel loginLabel;
    private JScrollPane scrollPane;
    private JFrame me;


    private IBarmenService service;


    public BarmenUI(IBarmenService service, User logged) throws HeadlessException {
        super();
        me = this;
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
        if (logged.getType().equals(UserType.BARMEN)) {
            initWorkPanel();
        } else {
            accountPane.remove(0);
        }
        initMessageGetter();
    }

    private void initMessageGetter() {
        Thread thread = new Thread(new MessageGetter());
        thread.start();
    }

    private void initWorkPanel() {
        workPanelExemp = new WorkPanel(service, loggedUser);
        workPanel.setLayout(new BorderLayout());
        scrollPane = new JScrollPane(workPanelExemp.getMainPanel());
        scrollPane.setMaximumSize(new Dimension(800, 650));
        scrollPane.setPreferredSize(new Dimension(800, 650));
        workPanel.add(scrollPane, BorderLayout.CENTER);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        workPanelExemp.buildPanel(service.getWorkingDenoms(loggedUser));
        mainPanel.updateUI();
    }


    protected void initAllDishPanel() {
        allDishPanel = new AllDishPanel(service, loggedUser);
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
        loginLabel = new LoginLabel(loggedUser, loggedUser.getType().toString());
        service.sentUIobjectToValidator(loginLabel);
    }

    @Override
    public LoginLabel getLoginLable() {
        return loginLabel;
    }

    private class MessageGetter implements Runnable {
        private java.util.List<Denomination> newMessage;

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.currentThread().sleep(10000);
                    newMessage = service.getMessages(loggedUser);
                    executeMessages(newMessage);
                } catch (Throwable e) {
                    LOGGER.error(e);
                    Thread.currentThread().start();
                }
            }
        }

        private void executeMessages(java.util.List<Denomination> newMessage) {
            if (newMessage != null && !newMessage.isEmpty()) {
                String result = "";
                List<Integer> ordersToUpdate = new LinkedList<Integer>();
                for (Denomination denomination : newMessage) {
                    if (loggedUser.getType().equals(UserType.BARMEN))
                        workPanelExemp.removeCurrentDenom(denomination);
                    ordersToUpdate.add(denomination.getOrder().getId());
                    result += denomination.getDish().getName() + ", " + denomination.getPortion() + ", " +
                            denomination.getOrder().getDescription() + " state changed on " + denomination.getState() + "\n";
                }
                updateOrders(ordersToUpdate);
                new JDialogExt(SwingUtilities.windowForComponent(mainPanel),"Something changed", result);
            }
        }

        private void updateOrders(List<Integer> ordersToUpdate) {
            Map<Integer, DenominationsPanel> views = allOrderingsPanel.getDenomsTobeUpdated();
            System.out.println(views);
            for (Integer integer : ordersToUpdate) {
                if (views.containsKey(integer) && views.get(integer) != null) {
                    views.get(integer).updateValuesOfTable();
                }
            }
        }
    }

}
