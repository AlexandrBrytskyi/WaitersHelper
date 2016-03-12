package client.view.orderings;

import client.service.IBarmenService;
import net.sourceforge.jdatepicker.impl.SqlDateModel;
import org.apache.log4j.Logger;
import transferFiles.exceptions.NoOrderingWithIdException;
import transferFiles.exceptions.OrderingAlreadyServingException;
import transferFiles.exceptions.OrderingNotServingByYouException;
import transferFiles.exceptions.UserAccessException;
import transferFiles.model.fund.Fund;
import transferFiles.model.order.Ordering;
import transferFiles.model.user.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class OrderingPanel extends JPanel {
    private JPanel mainPanel;
    private JPanel datePanel;
    private JPanel createdDatePanel;
    private JPanel comingclientDatePanel;
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
    private JLabel amountOfPeopleLable;
    private JPanel razdelitel;
    private JTextField avanceField;
    private JTextField finalPriceFild;
    private JLabel minusAvanceLabel;
    private JLabel avanceTextLabel;
    private JButton serveButton;
    private JButton dropButton;
    private JPanel takeServePanel;
    private JScrollPane panee;
    private JPanel descPan;
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

            public void actionPerformed(ActionEvent e) {
                try {
                    service.setWhoServesOrderNull(orderingSource, logined);
                    serveButton.setEnabled(true);
                    dropButton.setEnabled(false);
                    JOptionPane.showMessageDialog(mainPanel, "You Have time transferFiles.to sleep now");
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

            public void actionPerformed(ActionEvent e) {
                if (JOptionPane.showConfirmDialog(mainPanel, "Really remove?", "Removing ordering", JOptionPane.YES_NO_OPTION) == 0) {
                    try {
                        service.removeOrdering(orderingSource, logined);
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
            ko += 0.01;
        }
        priceField.setEnabled(false);
        pricePlusKOField.setEnabled(false);
        countButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                orderingSource = service.setKO((Double) koComboBox.getSelectedItem(), orderingSource);
                Fund counted = service.getFinalFund(orderingSource);
                priceField.setText(String.valueOf(counted.getPrice()));
                pricePlusKOField.setText(String.valueOf(counted.getFinalPrice() + orderingSource.getAdvancePayment()));
                finalPriceFild.setText(String.valueOf(counted.getFinalPrice()));
                orderingSource.setFund(counted);
            }
        });
        printButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (JOptionPane.showConfirmDialog(fundPanel, "Have you already checked all denominations, " +
                        "KO?", "Are you sure", JOptionPane.YES_NO_OPTION) == 0) {
                    try {
                        service.generatePrintPdf(orderingSource);
                    } catch (FileNotFoundException e1) {
                        JOptionPane.showMessageDialog(mainPanel, "Document is open, close it and try again");
                    } catch (PrinterException e1) {
                        JOptionPane.showMessageDialog(mainPanel, "Problem with printer " + e1);
                    } catch (IOException e1) {
                        JOptionPane.showMessageDialog(mainPanel, "Problem with generating pdf " + e1);
                    }
                } else {
                    JOptionPane.showMessageDialog(mainPanel, "You should be more carefull with this!");
                }
            }
        });
    }

    private void initDenominationPanel() {
        orderDenominationsPanel.setVisible(false);

        hideDenominationsCheckBox.addActionListener(new ActionListener() {

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
        descriptionField.setPreferredSize(new Dimension(600, 100));
        descriptionField.setMaximumSize(descriptionField.getPreferredSize());

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
