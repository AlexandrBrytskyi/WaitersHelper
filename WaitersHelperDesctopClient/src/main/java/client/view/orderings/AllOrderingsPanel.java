package client.view.orderings;


import net.sourceforge.jdatepicker.impl.DateComponentFormatter;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.SqlDateModel;
import org.apache.log4j.Logger;
import transferFiles.model.order.Ordering;
import transferFiles.model.user.User;
import transferFiles.service.rmiService.IBarmenService;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;


public class AllOrderingsPanel {
    private JPanel mainPanel;
    private JButton addButton;
    private JPanel filtersPanel;
    private JComboBox dateTypeComboBox;
    private JPanel datePanel;
    private JComboBox userComboBox;
    private JScrollPane filteredOrdersPane;
    private JPanel filteredOrdersPanel;
    private JButton findButton;
    private Logger LOGGER;
    private IBarmenService service;
    private User logined;
    private boolean isAlreadyInitialised = false;
    private SqlDateModel model1;
    private JDatePickerImpl picker1;
    private SqlDateModel model2;
    private JDatePickerImpl picker2;
    private java.util.List<Ordering> filteredOrderings;
    private AllOrderingsPanel thiss;
    private JPanel myOrderings = new JPanel();
    private Map<Integer,DenominationsPanel> denomsTobeUpdated = new HashMap<Integer, DenominationsPanel>();

    public AllOrderingsPanel(final Logger logger, final IBarmenService service, final User logged) {
        this.logined = logged;
        this.service = service;
        this.LOGGER = logger;
        thiss = this;
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AddOrderingFrame.getAddOrderFrame(logger, service, logined, thiss);
            }
        });
    }


    public void initialiseAllOrdersPanel() {
        if (!isAlreadyInitialised) {
            LOGGER.info("initializing all Orders panel components");
            initFiltersPanel();
            isAlreadyInitialised = true;
        } else {
            initUsersComboBox();
            LOGGER.info("Just updating info in all orderings panel");
        }
    }

    private void initFiltersPanel() {
        initDatePanel();
        initDateTypeComboBox();
        initUsersComboBox();
        findButton.setEnabled(true);
        findButton.addActionListener(findButtonActionListener);
    }

    private ActionListener findButtonActionListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            getFilteredAndBuildPanel();
        }
    };

    protected void getFilteredAndBuildPanel() {
        if (userComboBox.getSelectedItem().equals("All Users")) {
            if (dateTypeComboBox.getSelectedItem().equals("Concrette Date")) {
                filteredOrderings = service.getOrderings(model1.getValue().toLocalDate());
            } else {
                filteredOrderings = service.getOrderings(model1.getValue().toLocalDate(), model2.getValue().toLocalDate());
            }
        } else {
            if (dateTypeComboBox.getSelectedItem().equals("Concrette Date")) {
                filteredOrderings = service.getOrderings((User) userComboBox.getSelectedItem(), model1.getValue().toLocalDate());
            } else {
                filteredOrderings = service.getOrderings((User) userComboBox.getSelectedItem(), model1.getValue().toLocalDate(), model2.getValue().toLocalDate());
            }
        }
        if (filteredOrderings == null || filteredOrderings.isEmpty()) {
            JOptionPane.showMessageDialog(mainPanel, "No results", ":(", JOptionPane.WARNING_MESSAGE);
            filteredOrdersPanel.removeAll();
            filteredOrdersPanel.updateUI();
        } else {
            buildFilteredOrdersPanel(filteredOrdersPanel);
        }
    }

    private void buildFilteredOrdersPanel(JPanel filteredOrdersPanel) {
        filteredOrdersPanel.removeAll();
        TreeSet<Ordering> orderingTreeSet = new TreeSet<Ordering>(filteredOrderings);
        int rows;
        if (orderingTreeSet.size() < 5) {
            rows = 5;
        } else {
            rows = orderingTreeSet.size();
        }
        filteredOrdersPanel.setLayout(new GridLayout(rows, 1));
        for (Ordering filteredOrdering : orderingTreeSet) {
            OrderingPanel orderingPanel = new OrderingPanel(filteredOrdering, service, this, logined, denomsTobeUpdated);
            filteredOrdersPanel.add(orderingPanel.getOrderingPanel());
        }
        filteredOrdersPanel.updateUI();
    }

    private void initDateTypeComboBox() {
        dateTypeComboBox.addItem("Concrette Date");
        dateTypeComboBox.addItem("Interval");
        dateTypeComboBox.addActionListener(dateTypeComboBoxListener);
    }

    private ActionListener dateTypeComboBoxListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (dateTypeComboBox.getSelectedItem().equals("Concrette Date")) {
                findButton.setEnabled(true);
                picker1.setVisible(true);
                picker2.setVisible(false);
                filtersPanel.updateUI();
            }
            if (dateTypeComboBox.getSelectedItem().equals("Interval")) {
                findButton.setEnabled(true);
                picker1.setVisible(true);
                picker2.setVisible(true);
                filtersPanel.updateUI();
            }
        }
    };

    private void initDatePanel() {
        datePanel.setLayout(new BorderLayout());
        model1 = new SqlDateModel(Date.valueOf(LocalDate.now()));
        JDatePanelImpl panel1 = new JDatePanelImpl(model1);
        picker1 = new JDatePickerImpl(panel1, new DateComponentFormatter());
        datePanel.add(picker1, BorderLayout.WEST);
        model2 = new SqlDateModel(Date.valueOf(LocalDate.now()));
        JDatePanelImpl panel2 = new JDatePanelImpl(model2);
        picker2 = new JDatePickerImpl(panel2, new DateComponentFormatter());
        datePanel.add(picker2, BorderLayout.EAST);
        picker1.setAlignmentX(Component.CENTER_ALIGNMENT);
        picker2.setAlignmentX(Component.CENTER_ALIGNMENT);
        picker1.setVisible(false);
        picker2.setVisible(false);
        model1.addChangeListener(model1ChangeListener);
        model2.addChangeListener(model2ChangeListener);
        findButton.setEnabled(true);
        picker1.setVisible(true);
        picker2.setVisible(false);
    }

    private ChangeListener model1ChangeListener = new ChangeListener() {
        public void stateChanged(ChangeEvent e) {
            model2.setValue(model1.getValue());
        }
    };

    private ChangeListener model2ChangeListener = new ChangeListener() {
        public void stateChanged(ChangeEvent e) {
            if (!checkDate()) {
                JOptionPane.showMessageDialog(mainPanel, "Wrong Interval of date", "Change date", JOptionPane.WARNING_MESSAGE);
            }
        }
    };

    private boolean checkDate() {
        LocalDate current = LocalDate.of(model1.getYear(), model1.getMonth() + 1, model1.getDay());
        LocalDate modelDate = LocalDate.of(model2.getYear(), model2.getMonth() + 1, model2.getDay());
        return (modelDate.compareTo(current) >= 0);

    }

    private void initUsersComboBox() {
        userComboBox.removeAllItems();
        userComboBox.addItem("All Users");
        for (User user : service.getAllUsers()) {
            userComboBox.addItem(user);
        }
    }

    public JPanel getAllOrderingsPanel() {
        return mainPanel;
    }

    public JPanel getFilteredOrdersPanelUserServesOrdering() {
        filteredOrderings = service.getOrderingsUserServes(logined,LocalDate.now().minusDays(1),LocalDate.now());
        buildFilteredOrdersPanel(myOrderings);
        return myOrderings;
    }

    public Map<Integer, DenominationsPanel> getDenomsTobeUpdated() {
        return denomsTobeUpdated;
    }
}
