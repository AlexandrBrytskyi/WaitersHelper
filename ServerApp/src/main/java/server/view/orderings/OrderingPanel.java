package server.view.orderings;

import net.sourceforge.jdatepicker.impl.SqlDateModel;
import org.apache.log4j.Logger;
import server.exceptions.NoOrderingWithIdException;
import server.exceptions.OrderingAlreadyServingException;
import server.exceptions.OrderingNotServingByYouException;
import server.exceptions.UserAccessException;
import server.model.fund.Fund;
import server.model.order.Ordering;
import server.model.user.User;
import server.service.IBarmenService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class OrderingPanel extends JPanel {
    private JPanel mainPanel;
    private JPanel datePanel;
    private JPanel createdDatePanel;
    private JPanel comingClientDatePanel;
    private JPanel numberPanel;
    private JLabel numberLabel;
    private JPanel takenServingPanel;
    private JLabel userTakenLabel;
    private JLabel userServingLabel;
    private JPanel fundPanel;
    private JPanel denominationsPanel;
    private JCheckBox hideDenominationsCheckBox;
    private JTextArea descriptionField;
    private JPanel orderDenominationsPanel;
    private JLabel createdLabel;
    private JLabel clientComeLabel;
    private JLabel typeLabel;
    private JLabel descriptionLabel;
    private JTextField priceField;
    private JTextField pricePlusKOField;
    private JButton printButton;
    private JButton countButton;
    private JLabel koLabel;
    private JLabel priceLabel;
    private JLabel finalPriceLabel;
    private JButton removeButton;
    private JButton changeButton;
    private JComboBox minutesComboBox;
    private JComboBox hoursComboBox;
    private JPanel datePanell;
    private JLabel createdDateLabel;
    private JComboBox koComboBox;
    private JScrollPane pannnn;
    private JLabel amountOfPeopleLable;
    private JPanel razdelitel;
    private JTextField avanceField;
    private JTextField finalPriceFild;
    private JLabel minusAvanceLabel;
    private JLabel avanceTextLabel;
    private JButton serveButton;
    private JButton dropButton;
    private JPanel takeServePanel;
    private Ordering orderingSource;
    private static final Logger LOGGER = Logger.getLogger(OrderingPanel.class);
    private SqlDateModel model;
    private IBarmenService service;
    private AllOrderingsPanel allOrderingsPanel;
    private User logined;

    public OrderingPanel(Ordering ordering, IBarmenService service, AllOrderingsPanel allOrderingsPanel, User logined) {
        this.logined = logined;
        orderingSource = ordering;
        this.service = service;
        this.allOrderingsPanel = allOrderingsPanel;
        initPanels();
    }

    public JPanel getOrderingPanel() {
        return mainPanel;
    }


    private void initPanels() {
        initNumberPanel();
        initDatePanel();
        initTakenServingPanel();
        initDescriptionField();
        initDenominationPanel();
        initFundPanel();
        initChangeRemoveButtons();
        initTakeServePanel();
    }

    private void initTakeServePanel() {
        if (orderingSource.getWhoServesOrder() == null) {
            serveButton.setEnabled(true);
            dropButton.setEnabled(false);
        } else {
            serveButton.setEnabled(false);
            dropButton.setEnabled(true);
        }
        serveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    service.setWhoServesOrder(orderingSource, logined);
                    serveButton.setEnabled(false);
                    dropButton.setEnabled(true);
                    JOptionPane.showMessageDialog(mainPanel, "GO AND SERVE NOW");
                    allOrderingsPanel.getFilteredAndBuildPanel();
                } catch (OrderingAlreadyServingException e1) {
                    JOptionPane.showMessageDialog(mainPanel, e1);
                } catch (NoOrderingWithIdException e1) {
                    JOptionPane.showMessageDialog(mainPanel, e1);
                } catch (UserAccessException e1) {
                    JOptionPane.showMessageDialog(mainPanel, e1);
                }
            }
        });
        dropButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    service.setWhoServesOrderNull(orderingSource, logined);
                    serveButton.setEnabled(true);
                    dropButton.setEnabled(false);
                    JOptionPane.showMessageDialog(mainPanel, "You Have time to sleep now");
                    allOrderingsPanel.getFilteredAndBuildPanel();
                } catch (NoOrderingWithIdException e1) {
                    JOptionPane.showMessageDialog(mainPanel, e1);
                } catch (OrderingNotServingByYouException e1) {
                    JOptionPane.showMessageDialog(mainPanel, e1);
                } catch (UserAccessException e1) {
                    JOptionPane.showMessageDialog(mainPanel, e1);
                }
            }
        });

    }


    private void initChangeRemoveButtons() {
        changeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (changeButton.getText().equals("Change")) {
                    changeButton.setText("Submit");
                    hideDenominationsCheckBox.setEnabled(false);
                    orderDenominationsPanel.setEnabled(false);
                    descriptionField.setEnabled(true);
                    enableDatePanel(true);
                    fundPanel.setEnabled(false);
                    removeButton.setEnabled(false);
                    avanceField.setEnabled(true);
                } else {
                    if (checkDate() && checkAvance()) {
                        changeButton.setText("Change");
                        hideDenominationsCheckBox.setEnabled(true);
                        orderDenominationsPanel.setEnabled(true);
                        enableDatePanel(false);
                        fundPanel.setEnabled(true);
                        descriptionField.setEnabled(false);
                        removeButton.setEnabled(true);
                        avanceField.setEnabled(false);
                        setValuesUpdateOrder();
                    } else {
                        JOptionPane.showMessageDialog(mainPanel, "Wrong Date!!", "Wrong Date", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        });
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (JOptionPane.showConfirmDialog(mainPanel, "Really remove?", "Removing ordering", JOptionPane.YES_NO_OPTION) == 0) {
                    try {
                        service.removeOrdering(orderingSource);
                        JOptionPane.showMessageDialog(mainPanel, "Removed Succesfully");
                        allOrderingsPanel.getFilteredAndBuildPanel();
                    } catch (UserAccessException e1) {
                        JOptionPane.showMessageDialog(mainPanel, e1);
                    }
                }
            }
        });
    }

    private void setValuesUpdateOrder() {
        orderingSource.setAdvancePayment(Double.valueOf(avanceField.getText()));
        orderingSource.setDescription(descriptionField.getText());
        LocalDateTime modelDateTime = LocalDateTime.of(model.getYear(), model.getMonth() + 1, model.getDay(),
                (Integer) hoursComboBox.getSelectedItem(), (Integer) minutesComboBox.getSelectedItem());
        orderingSource.setDateClientsCome(modelDateTime);
        orderingSource = service.updateOrdering(orderingSource);
    }

    private boolean checkAvance() {
        try {
            Double.valueOf(avanceField.getText());
            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(mainPanel, "Check avance, please", "Wrong avance", JOptionPane.WARNING_MESSAGE);
            return false;
        }
    }

    private boolean checkDate() {
        LocalDate current = LocalDate.now();
        LocalDate modelDate = LocalDate.of(model.getYear(), model.getMonth() + 1, model.getDay());
        return (modelDate.compareTo(current) >= 0);

    }

    private void enableDatePanel(boolean en) {
        datePanell.setEnabled(en);
        hoursComboBox.setEnabled(en);
        minutesComboBox.setEnabled(en);
    }

    private void initFundPanel() {
        double ko = 0;
        for (int i = 0; i < 30; i++) {
            koComboBox.addItem(ko);
            ko += 0.05;
        }
        priceField.setEnabled(false);
        pricePlusKOField.setEnabled(false);
        countButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                service.setKO((Double) koComboBox.getSelectedItem(), orderingSource);
                Fund counted = service.getFinalFund(orderingSource);
                priceField.setText(String.valueOf(counted.getPrice()));
                pricePlusKOField.setText(String.valueOf(counted.getFinalPrice() + orderingSource.getAdvancePayment()));
                finalPriceFild.setText(String.valueOf(counted.getFinalPrice()));
            }
        });
        printButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (JOptionPane.showConfirmDialog(fundPanel, "Have you already checked all denominations, " +
                        "KO?", "Are you sure", JOptionPane.YES_NO_OPTION) == 0) {

                } else {

                }
            }
        });
    }

    private void initDenominationPanel() {
        orderDenominationsPanel.setVisible(false);

        hideDenominationsCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (hideDenominationsCheckBox.isSelected()) {
                    DenominationsPanel denominationsPanel = new DenominationsPanel(service, orderingSource, logined);
                    orderDenominationsPanel.add(denominationsPanel.getMainPanel(), BorderLayout.PAGE_START);
                    orderDenominationsPanel.setVisible(true);
                    mainPanel.updateUI();
                } else {
                    orderDenominationsPanel.removeAll();
                    orderDenominationsPanel.setVisible(false);
                }
            }
        });
    }

    private void initDescriptionField() {
        avanceField.setText(String.valueOf(orderingSource.getAdvancePayment()));
        descriptionField.setText(orderingSource.getDescription());
        descriptionField.setLineWrap(true);
        descriptionField.setEnabled(false);
        descriptionField.setSize(new Dimension(600, 250));
    }

    private void initTakenServingPanel() {
        userTakenLabel.setText(userTakenLabel.getText() + " " + orderingSource.getWhoTakenOrder().getName());
        User whoServing = orderingSource.getWhoServesOrder();
        amountOfPeopleLable.setText(orderingSource.getAmountOfPeople() + amountOfPeopleLable.getText());
        if (whoServing == null) {
            userServingLabel.setText(userServingLabel.getText() + " " + "no");
        } else {
            userServingLabel.setText(userServingLabel.getText() + " " + orderingSource.getWhoServesOrder().getName());
        }
    }

    private void initDatePanel() {
        createdDateLabel.setText(orderingSource.getDateOrderCreated().format(DateTimeFormatter.ISO_LOCAL_DATE));
        model = DateUtils.initDateWhenPeopleComePanel(datePanell, hoursComboBox, minutesComboBox, orderingSource);
        enableDatePanel(false);
    }


    private void initNumberPanel() {
        numberLabel.setText(numberLabel.getText() + " " + orderingSource.getId());
        typeLabel.setText(typeLabel.getText() + " " + orderingSource.getType());
    }


}
