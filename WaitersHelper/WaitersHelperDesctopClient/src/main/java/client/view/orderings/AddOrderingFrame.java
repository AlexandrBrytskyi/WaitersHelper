package client.view.orderings;


import client.service.dateUtils.ConvertDate;
import net.sourceforge.jdatepicker.impl.SqlDateModel;
import org.apache.log4j.Logger;
import transferFiles.model.order.OrderType;
import transferFiles.model.order.Ordering;
import transferFiles.model.user.User;
import services.rmiService.IBarmenService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class AddOrderingFrame extends JFrame {
    private JPanel mainPanel;
    private JScrollPane scrollPane;
    private JPanel addOrderingPanel;
    private JTextField descriptionField;
    private JComboBox typeComboBox;
    private JTextField amountOfPeopleField;
    private JPanel dateWhenPeopleComePanel;
    private JButton addButton;
    private JButton cancelButton;
    private JPanel datePanel;
    private JPanel timePanel;
    private JComboBox hoursComboBox;
    private JComboBox minutesCombobox;
    private JLabel hoursLabel;
    private JLabel minutesLabel;
    private JTextField advancedPaymentField;
    private JLabel adancedPaymentLabel;
    private SqlDateModel model;
    private Logger LOGGER;
    private IBarmenService service;
    private static boolean isAlreadyInited = false;
    private static JFrame frame;
    private User logged;
    private AllOrderingsPanel allOrderingsPanel;

    public static void getAddOrderFrame(Logger logger, IBarmenService servise, User logged, AllOrderingsPanel allOrderingsPanel) {
        if (isAlreadyInited) {
            frame.pack();
            frame.toFront();
            frame.setVisible(true);
        } else {
            new AddOrderingFrame(logger, servise, logged, allOrderingsPanel);
        }
    }

    private AddOrderingFrame(Logger logger, IBarmenService servise, User logged, AllOrderingsPanel allOrderingsPanel) throws HeadlessException {
        super();
        frame = this;
        this.allOrderingsPanel = allOrderingsPanel;
        this.logged = logged;
        this.service = servise;
        this.LOGGER = logger;
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setSize(350, 400);
        setVisible(true);
        add(mainPanel);
        model = DateUtils.initDateWhenPeopleComePanel(datePanel, hoursComboBox, minutesCombobox);
        initTypeComboBox();
        addButton.addActionListener(addButtonActionListener);
        cancelButton.addActionListener(cancelButtonActionListener);
        typeComboBox.addActionListener(typeBoxActionListener);
        isAlreadyInited = true;
    }

    private ActionListener typeBoxActionListener = new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            if (typeComboBox.getSelectedItem().equals(OrderType.PREVIOUS)) {
                advancedPaymentField.setEnabled(true);
                advancedPaymentField.setText("0");
            } else {
                advancedPaymentField.setEnabled(false);
                advancedPaymentField.setText("0");
            }
        }
    };

    private ActionListener addButtonActionListener = new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            if (validateFields()) {
                if (JOptionPane.showConfirmDialog(mainPanel, "Really create new ordering?", "Create ordering?", JOptionPane.YES_NO_OPTION) == 0) {
                    Ordering ordering = new Ordering();
                    ordering.setDateOrderCreated(ConvertDate.toJoda(LocalDateTime.now()));
                    LocalTime time = LocalTime.of((Integer) hoursComboBox.getSelectedItem(), (Integer) minutesCombobox.getSelectedItem());
                    LocalDateTime dateTime = LocalDateTime.of(model.getValue().toLocalDate(), time);
                    ordering.setDateClientsCome(ConvertDate.toJoda(dateTime));
                    ordering.setType((OrderType) typeComboBox.getSelectedItem());
                    ordering.setDescription(descriptionField.getText());
                    ordering.setAmountOfPeople(Integer.valueOf(amountOfPeopleField.getText()));
                    ordering.setWhoTakenOrder(logged);
                    ordering.setAdvancePayment(Double.valueOf(advancedPaymentField.getText()));
                    if (typeComboBox.getSelectedItem().equals(OrderType.CURRENT)) ordering.setWhoServesOrder(logged);
                    service.addOrder(ordering);
                }
            }
        }
    };

    private boolean validateFields() {
        if (descriptionField == null) {
            JOptionPane.showMessageDialog(mainPanel, "Description field must not be null");
            return false;
        }
        if (!checkDate()) {
            JOptionPane.showMessageDialog(mainPanel, "Wrong date");
            return false;
        }
        try {
            Integer.valueOf(amountOfPeopleField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(mainPanel, "Wrong amount of people");
            return false;
        }
        try {
            if (advancedPaymentField.isEnabled()) Double.valueOf(advancedPaymentField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(mainPanel, "Advanced Payment is wrong");
            return false;
        }
        return true;
    }

    private boolean checkDate() {
        LocalDate current = LocalDate.now();
        LocalDate modelDate = LocalDate.of(model.getYear(), model.getMonth() + 1, model.getDay());
        return (typeComboBox.getSelectedItem().equals(OrderType.PREVIOUS)) ?
                (modelDate.compareTo(current) >= 0) : (modelDate.compareTo(current) == 0);

    }

    private boolean isDigitsOnly(String text) {
        for (char c : text.toCharArray()) {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }

    private ActionListener cancelButtonActionListener = new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            allOrderingsPanel.getFilteredAndBuildPanel();
            frame.dispose();
        }
    };


    private void initTypeComboBox() {
        for (OrderType orderType : OrderType.values()) {
            typeComboBox.addItem(orderType);
        }
        typeComboBox.setSelectedIndex(0);
    }


}
