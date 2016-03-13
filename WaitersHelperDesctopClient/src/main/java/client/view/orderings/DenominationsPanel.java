package client.view.orderings;

import client.service.IBarmenService;
import org.apache.log4j.Logger;
import transferFiles.exceptions.NoOrderingWithIdException;
import transferFiles.exceptions.UserAccessException;
import transferFiles.model.denomination.Denomination;
import transferFiles.model.denomination.DenominationState;
import transferFiles.model.dish.Dish;
import transferFiles.model.dish.DishType;
import transferFiles.model.order.OrderType;
import transferFiles.model.order.Ordering;
import transferFiles.model.user.User;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class DenominationsPanel {
    private JPanel mainPanel;
    private JComboBox dishComboBox;
    private JTextField portionField;
    private JTextField priceField;
    private JTextField addedDateField;
    private JTextField readyDateField;
    private JTable denominationsTable;
    private JButton addButton;
    private JButton removeButton;
    private JLabel dishLabel;
    private JLabel portionLabel;
    private JLabel priceLabel;
    private JLabel addedDate;
    private JLabel readyDate;
    private JPanel addRemovePanel;
    private JPanel datePanel;
    private JPanel panel;
    private JLabel lab5;
    private JLabel lab1;
    private JLabel lab2;
    private JLabel lab3;
    private JLabel lab4;
    private JPanel panel2;
    private JComboBox dishTypeComboBox;
    private JLabel dishTypeLabel;
    private JLabel lab6;
    private static final Logger LOGGER = Logger.getLogger(DenominationsPanel.class);
    private IBarmenService service;
    private User logined;
    private Ordering orderingSource;
    private DenominationsTableModel denominationsTableModel;
    private DenominationsPanel me;


    /*every time when showDenominations combo box from ordering panel is selected
    * new Denomination panel is created*/
    public DenominationsPanel(IBarmenService service, Ordering ordering, User logined) {
        this.service = service;
        orderingSource = ordering;
        this.logined = logined;
        initTable();
        initAddRemovePanel();
        LOGGER.trace("Creating Denominations panel for ordering" + ordering.getId());
        me = this;
    }

    public JPanel getMainPanel() {
        mainPanel.updateUI();
        return mainPanel;
    }

    private void initAddRemovePanel() {
        initDishTypeComboBox();
        initPortionPriceField();
        initDatepanel();
        initAddRemoveButtons();

    }

    private void initDishTypeComboBox() {
        dishTypeComboBox.addItem("All");
        for (DishType dishType : DishType.values()) {
            dishTypeComboBox.addItem(dishType);
        }
        dishTypeComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                initDishesComboBox();
            }
        });
        initDishesComboBox();
    }

    private void initAddRemoveButtons() {
        addButton.setText("Add Dish");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (addButton.getText().equals("Add Dish")) {
                    setFieldsNull();
                    removeButton.setEnabled(false);
                    dishComboBox.setEnabled(true);
                    denominationsTable.setEnabled(false);
                    portionField.setEnabled(true);
                    addButton.setText("Confirm");
                } else {
                    if (validateFields()) {
                        Denomination denomination = createDenomination();
                        try {
                            JOptionPane.showMessageDialog(addRemovePanel, "Just added " + service.addDenomination(denomination, logined).printForOrdering());

                        } catch (UserAccessException e1) {
                            JOptionPane.showMessageDialog(mainPanel, e1);
                        } catch (NoOrderingWithIdException e1) {
                            JOptionPane.showMessageDialog(mainPanel, e1 + ", so it might have been removed");
                        }
                        addButton.setText("Add Dish");
                        removeButton.setEnabled(true);
                        denominationsTable.setEnabled(true);
                        portionField.setEnabled(false);
                        dishComboBox.setEnabled(false);
                        updateValues();
                    }
                }
            }
        });
        addButton.setEnabled(true);
        removeButton.setText("Remove");
        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (JOptionPane.showConfirmDialog(addRemovePanel, "Really remove this denomination?", "Denomination removing",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == 0) {
                    try {
                        service.removeDenomination(denominationsTableModel.getSelectedDenomination(), logined);
                    } catch (UserAccessException e1) {
                        if (JOptionPane.showConfirmDialog(mainPanel, e1 + ", but you can cancel it, would you like?", "Maybe cancel?", JOptionPane.YES_NO_OPTION) == 0)
                            try {
                                service.cancelDenomination(logined, denominationsTableModel.getSelectedDenomination());
                            } catch (UserAccessException e2) {
                                JOptionPane.showMessageDialog(mainPanel, e2);
                            }
                    }
                    updateValues();
                }
            }
        });
    }

    private Denomination createDenomination() {
        Denomination denomination = new Denomination();
        denomination.setDish((Dish) dishComboBox.getSelectedItem());
        denomination.setPortion(Double.valueOf(portionField.getText()));
        denomination.setState(DenominationState.JUST_ADDED);
        denomination.setOrder(orderingSource);
        denomination.setTimeWhenAdded(LocalDateTime.now());
        return denomination;
    }

    private boolean validateFields() {
        if (checkPortionField()) {
            priceField.setText(countPrice());
        } else {
            JOptionPane.showMessageDialog(addRemovePanel, "Wrong Portion, reset please",
                    "Portion Setting", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void updateValues() {
        denominationsTableModel.updateList();
        denominationsTable.updateUI();
        setFieldsNull();
    }

    private void setFieldsNull() {
        dishComboBox.setSelectedIndex(0);
        portionField.setText(null);
        priceField.setText(null);
        if (datePanel.isVisible()) {
            addedDateField.setText(null);
            readyDateField.setText(null);
        }
    }

    private void initDatepanel() {
        if (orderingSource.getType().equals(OrderType.CURRENT)) {
            datePanel.setVisible(true);
            addedDateField.setEnabled(false);
            readyDateField.setEnabled(false);
        } else {
            datePanel.setVisible(false);
        }
    }

    private void initPortionPriceField() {
        portionField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (checkPortionField())
                    priceField.setText(String.valueOf(((Dish) dishComboBox.getSelectedItem()).getPriceForPortion() * Double.valueOf(portionField.getText())));
            }
        });
        portionField.setEnabled(false);
        priceField.setEnabled(false);


    }


    private boolean checkPortionField() {
        try {
            Double.valueOf(portionField.getText());
        } catch (NumberFormatException e) {
            LOGGER.error("portion field is setted wrong " + e);
            return false;
        }
        return true;
    }

    private String countPrice() {
        Dish selected = (Dish) dishComboBox.getSelectedItem();
        double priceForPortion = selected.getPriceForPortion();
        return String.valueOf(priceForPortion * Double.valueOf(portionField.getText()));
    }

    private void initDishesComboBox() {
        dishComboBox.removeAllItems();
        if (dishTypeComboBox.getSelectedItem().equals("All")) {
            if (orderingSource.getType().equals(OrderType.PREVIOUS)) {
                for (Dish dish : service.getAllDishes()) {
                    dishComboBox.addItem(dish);
                }
            } else {
                for (Dish dish : service.getAllDishes()) {
                    if (dish.isAvailable()) dishComboBox.addItem(dish);
                }
            }
        } else {
            if (orderingSource.getType().equals(OrderType.PREVIOUS)) {
                for (Dish dish : service.getDishesByDishType((DishType) dishTypeComboBox.getSelectedItem())) {
                    dishComboBox.addItem(dish);
                }
            } else {
                for (Dish dish : service.getDishesByDishType((DishType) dishTypeComboBox.getSelectedItem())) {
                    if (dish.isAvailable()) dishComboBox.addItem(dish);
                }
            }
        }
        dishComboBox.setEnabled(true);
    }

    private void initTable() {
        String[] head;
        if (orderingSource.getType().equals(OrderType.CURRENT)) {
            head = new String[]{"Dish", "Portion", "Price", "Dish added", "Dish ready", "State"};
            lab1.setText("Dish");
            lab2.setText("Portion");
            lab3.setText("Price");
            lab4.setText("Dish added");
            lab5.setText("Dish ready");
            lab6.setText("State");
        } else {
            head = new String[]{"Dish", "Portion", "Price", "State"};
            lab1.setText("Dish");
            lab2.setText("Portion");
            lab3.setText("Price");
            lab4.setText("State");
            lab5.setVisible(false);
            lab6.setVisible(false);

        }
        denominationsTableModel = new DenominationsTableModel(head);
        denominationsTable.setModel(denominationsTableModel);
        denominationsTable.getTableHeader().setVisible(true);
    }


    private class DenominationsTableModel extends AbstractTableModel {

        private java.util.List<Denomination> denominations;
        private String[] colNames;
        private Denomination selectedDenomination;
        private TableRowSorter<TableModel> sorter;

        public DenominationsTableModel(String[] colNames) {
            this.colNames = colNames;
            initSorter();
            updateList();
            denominationsTable.getSelectionModel().addListSelectionListener(listSelectionListener);

        }

        private void initSorter() {
            sorter = new TableRowSorter<TableModel>(this);
            denominationsTable.setRowSorter(sorter);
            List<RowSorter.SortKey> sortKeys = new ArrayList<RowSorter.SortKey>();
            sortKeys.add(new RowSorter.SortKey(2, SortOrder.ASCENDING));
            sorter.setSortKeys(sortKeys);
        }


        private ListSelectionListener listSelectionListener = new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                int selectedIndex = denominationsTable.getSelectedRow();
                selectedDenomination = denominations.get(selectedIndex);
                dishComboBox.setSelectedItem((Dish) selectedDenomination.getDish());
                portionField.setText(String.valueOf(selectedDenomination.getPortion()));
                priceField.setText(String.valueOf(selectedDenomination.getPrice()));
                if (getColumnCount() == 6) {
                    addedDateField.setText(selectedDenomination.getTimeWhenAdded().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                    if (selectedDenomination.getTimeWhenIsReady() != null)
                        readyDateField.setText(selectedDenomination.getTimeWhenIsReady().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                }
                addButton.setEnabled(true);
                removeButton.setEnabled(true);
            }
        };


        public int getRowCount() {

            if (denominations == null) return 0;
            return denominations.size();
        }

        public int getColumnCount() {
            return colNames.length;
        }

        @Override
        public String getColumnName(int column) {
            return colNames[column];
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            Denomination selected = denominations.get(rowIndex);
            if (colNames.length == 6) {
                switch (columnIndex) {
                    case 0:
                        return selected.getDish();
                    case 1:
                        return selected.getPortion();
                    case 2:
                        return selected.getPrice();
                    case 3:
                        return selected.getTimeWhenAdded();
                    case 4:
                        return selected.getTimeWhenIsReady();
                    case 5:
                        return selected.getState();
                    default:
                        return null;
                }
            } else {
                switch (columnIndex) {
                    case 0:
                        return selected.getDish();
                    case 1:
                        return selected.getPortion();
                    case 2:
                        return selected.getPrice();
                    case 3:
                        return selected.getState();
                    default:
                        return null;
                }
            }
        }

        public void updateList() {
            try {
                this.denominations = service.getDenominationsByOrder(orderingSource);
                sorter.sort();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }


        public Denomination getSelectedDenomination() {
            return selectedDenomination;
        }

    }

    public void updateValuesOfTable() {
        denominationsTableModel.updateList();
        mainPanel.updateUI();
    }

}
