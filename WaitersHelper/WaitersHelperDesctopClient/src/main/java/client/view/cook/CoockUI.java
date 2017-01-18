package client.view.cook;

import client.view.account.AccountFrame;
import client.view.dialog.JDialogExt;
import org.apache.log4j.Logger;
import org.springframework.remoting.RemoteLookupFailureException;
import transferFiles.model.denomination.Denomination;
import transferFiles.model.user.User;
import services.rmiService.ICookService;
import transferFiles.to.LoginLabel;
import transferFiles.to.Loginable;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.List;


public class CoockUI extends JFrame implements Loginable {
    protected static Logger LOGGER = Logger.getLogger(CoockUI.class);

    private JPanel mainPanel;
    private JTabbedPane accountPane;
    private JPanel workPanel;
    private JTabbedPane workPane;
    private JPanel accountPanel;
    private User loggedUser;
    private LoginLabel loginLabel;
    private WorkPanel workPanelExemp;
    private JScrollPane scrollPane;
    private JFrame me;


    private ICookService service;


    public CoockUI(ICookService service, User logged) throws HeadlessException {
        super();
        this.service = service;
        this.loggedUser = logged;
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(900, 700);
        setResizable(false);
        setTitle("Cooker " + logged.getName());
        setVisible(true);
        add(mainPanel);
        initWorkPanel();
        mainPanel.updateUI();
        accountPane.addChangeListener(menuTabChangeListener);
        initMessageGetter();
        me = this;
    }

    private void initMessageGetter() {
        Thread thread = new Thread(new MessageGetter());
        thread.start();
    }


    protected void initWorkPanel() {
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


    private ChangeListener menuTabChangeListener = new ChangeListener() {
        public void stateChanged(ChangeEvent e) {
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
                    try {
                        newMessage = service.getMessages(loggedUser);
                        executeMessages(newMessage);
                    } catch (RemoteLookupFailureException e) {
                        Thread.currentThread().sleep(20000);
                    }
                } catch (InterruptedException e) {
                    LOGGER.error(e);
                    Thread.currentThread().start();
                }
            }
        }

        private void executeMessages(List<Denomination> newMessage) {
            if (newMessage != null && !newMessage.isEmpty()) {
                String result = "";
                for (Denomination denomination : newMessage) {
                    workPanelExemp.removeCurrentDenom(denomination);
                    result += denomination.getDish().getName() + ", " + denomination.getPortion() + ", " +
                            denomination.getOrder().getWhoServesOrder() + " state changed on " + denomination.getState() + "\n";
                }
                new JDialogExt(me.getOwner(), "Job", result);
            }
        }
    }
}
