package client.view.dishes;

import org.apache.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
import server.exceptions.IngridientWithIDNotFoundException;
import server.exceptions.UserAccessException;
import server.model.dish.Dish;
import server.model.dish.DishType;
import server.model.dish.WhoCoockDishType;
import server.model.dish.ingridient.Ingridient;
import server.model.dish.ingridient.Product;
import server.model.user.User;
import server.service.IAdminService;
import server.service.IBarmenService;
import server.service.IWaitersService;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;


public class AllDishPanel {

    private JPanel allDishesPanel;
    private JScrollPane allDishesScrollPane;
    private JTable allDishesTable;
    private JPanel addChangeDishPanel;
    private JTextField dishNameField;
    private JTextField dishPriceField;
    private JTextArea dishDescriptionField;
    private JButton removeDishButton;
    private JButton addDishButton;
    private JButton changeDishButton;
    private JComboBox dishTypeComboBox;
    private JTable chosenDishIngridientsTable;
    private JScrollPane dishIngridientScrolPane;
    private JLabel leybaIngridients;
    private JPanel addChangeIngridientPanel;
    private JComboBox productsComboBox;
    private JLabel productMesurementLabel;
    private JTextField inridientAmountField;
    private JButton addNewIngridientButton;
    private JButton removeSelectedIngridientButton;
    private JLabel grnLabel;
    private JPanel isDishAvailablePanel;
    private JComboBox whoCookComboBox;
    private Dish selectedDish;
    private JButton changeDishAvailable;
    private IAdminService adminService;
    private IBarmenService barmenService;
    private IWaitersService waitersService;
    private static final Logger LOGGER = Logger.getLogger(AllDishPanel.class);
    private boolean isAlreadyInitialised = false;
    private DishTableModel dishTableModel;
    private IngridientTableModel ingridientTableModel;
    private User logined;


    public AllDishPanel(IAdminService service, User logined) {
        this.logined = logined;
        this.adminService = service;
    }

    public AllDishPanel(IBarmenService service, User logined) {
        this.logined = logined;
        this.barmenService = service;
        allDishesPanel.remove(dishIngridientScrolPane);
        allDishesPanel.remove(leybaIngridients);
        allDishesPanel.remove(addChangeIngridientPanel);
        initIsDishAvailablePanel();
    }

    public AllDishPanel(IWaitersService service) {
        this.waitersService = service;
        allDishesPanel.remove(addChangeIngridientPanel);
        addDishButton.setVisible(false);
        changeDishButton.setVisible(false);
        removeDishButton.setVisible(false);
    }


    private void initIsDishAvailablePanel() {
        changeDishAvailable = new JButton("");
        changeDishAvailable.setEnabled(false);
        changeDishAvailable.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (changeDishAvailable.getText().equals("Enable")) {
                    selectedDish.setAvailable(true);
                    barmenService.updateDish(selectedDish);
                } else {
                    selectedDish.setAvailable(false);
                    barmenService.updateDish(selectedDish);
                }
                dishTableModel.updateList();
                changeDishAvailable.setEnabled(false);
                JOptionPane.showMessageDialog(allDishesPanel, "State changed");
            }
        });
        isDishAvailablePanel.add(changeDishAvailable);
    }

    public JPanel getAllDishesPanel() {
        return allDishesPanel;
    }

    public void initialiseAllDishesPanel() {
        if (!isAlreadyInitialised) {
            LOGGER.info("initializing all dishes panel components");
            initialiseDishTable();
            initialiseAddChangeDishPanel();
            if (adminService != null || waitersService != null) {
                initialiseSelectedDishIngridientsTable();
                if (adminService != null) initialiseAddchangeIngridientPanel();
            }
            isAlreadyInitialised = true;
        } else {
            dishTableModel.updateList();
            allDishesTable.updateUI();
            LOGGER.info("Just updating info in alldish table");
        }
    }

    private void initialiseSelectedDishIngridientsTable() {
        ingridientTableModel = new IngridientTableModel(new String[]{"Dish", "Product", "Amount", "Mesurement"});
        chosenDishIngridientsTable.setModel(ingridientTableModel);
    }

    private void initialiseAddchangeIngridientPanel() {
        initProductComboBox();
        inridientAmountField.setText("Amount");
        addNewIngridientButton.addActionListener(addIngridientActionListener);
        removeSelectedIngridientButton.addActionListener(removeIngridientActionListener);
        addNewIngridientButton.setEnabled(false);
        removeSelectedIngridientButton.setEnabled(false);
    }

    private void initProductComboBox() {
        productsComboBox.removeAllItems();
        for (Product product : adminService.getAllProducts()) {
            productsComboBox.addItem(product);
        }
    }

    private void initialiseAddChangeDishPanel() {
        changeDishButton.setText("Change");
        if (adminService != null) {
            for (DishType type : DishType.values()) {
                dishTypeComboBox.addItem(type);
            }
            for (WhoCoockDishType whoCoockDishType : WhoCoockDishType.values()) {
                whoCookComboBox.addItem(whoCoockDishType);
            }
        }
        if (barmenService != null) {
            for (DishType type : DishType.values()) {
                if (!type.equals(DishType.DISH)) dishTypeComboBox.addItem(type);
            }
            whoCookComboBox.addItem(WhoCoockDishType.BARMEN);
        }
        changeDishButton.setEnabled(false);
        removeDishButton.setEnabled(false);
//        listeners
        addDishButton.addActionListener(addDishButtonActionListener);
        changeDishButton.addActionListener(changeDishButtonActionListener);
        removeDishButton.addActionListener(removeDishActionListener);
        setDishFieldsEnable(false);
    }

    private void setDishFieldsEnable(boolean enable) {
        dishPriceField.setEnabled(enable);
        dishDescriptionField.setEnabled(enable);
        dishDescriptionField.setLineWrap(true);
        dishNameField.setEnabled(enable);
        dishTypeComboBox.setEnabled(enable);
        whoCookComboBox.setEnabled(enable);
    }

    private ActionListener addDishButtonActionListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (addDishButton.getText().equals("Add")) {
                changeDishButton.setEnabled(false);
                removeDishButton.setEnabled(false);
                setDishFieldsEnable(true);
                dishPriceField.setText(null);
                dishDescriptionField.setText(null);
                dishNameField.setText(null);
                dishTypeComboBox.setSelectedIndex(0);
                addDishButton.setText("Submit");
                allDishesTable.setEnabled(false);
            } else {
                try {
                    Dish dish = new Dish();
                    dish.setName(dishNameField.getText());
                    dish.setType((DishType) dishTypeComboBox.getSelectedItem());
                    dish.setPriceForPortion(Double.valueOf(dishPriceField.getText()));
                    dish.setDescription(dishDescriptionField.getText());
                    dish.setWhoCoockDishType((WhoCoockDishType) whoCookComboBox.getSelectedItem());
                    if (adminService != null) adminService.addDish(dish);
                    if (barmenService != null) barmenService.addDish(dish);
                    dishTableModel.updateList();
                    allDishesTable.updateUI();
                    JOptionPane.showMessageDialog(addChangeDishPanel, "New Dish was successfully added");
                    LOGGER.info("new Dish was Added" + dish.toString());
                    addDishButton.setText("Add");
                    setDishFieldsEnable(false);
                    changeDishButton.setEnabled(true);
                    removeDishButton.setEnabled(true);
                    allDishesTable.setEnabled(true);
                } catch (NumberFormatException e1) {
                    JOptionPane.showMessageDialog(allDishesPanel, "Wrong price!");
                }
            }

        }
    };

    private ActionListener changeDishButtonActionListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (changeDishButton.getText().equals("Change")) {
                setDishFieldsEnable(true);
                changeDishButton.setText("Submit");
//                blocking table
                allDishesTable.setEnabled(false);
                addDishButton.setEnabled(false);
                removeDishButton.setEnabled(false);
            } else { //submit
                try {
                    if (JOptionPane.showConfirmDialog(addChangeDishPanel, "Apply changes?", "Dish change", JOptionPane.YES_NO_OPTION) == 0) {
                        Dish selectedDish = dishTableModel.getSelectedDish();
                        selectedDish.setName(dishNameField.getText());
                        selectedDish.setType((DishType) dishTypeComboBox.getSelectedItem());
                        selectedDish.setPriceForPortion(Double.valueOf(dishPriceField.getText()));
                        selectedDish.setDescription(dishDescriptionField.getText());
                        selectedDish.setWhoCoockDishType((WhoCoockDishType) whoCookComboBox.getSelectedItem());
                        if (adminService != null) adminService.updateDish(selectedDish);
                        if (barmenService != null) barmenService.updateDish(selectedDish);
                    }
                    allDishesTable.setEnabled(true);
                    addDishButton.setEnabled(true);
                    removeDishButton.setEnabled(true);
                    changeDishButton.setText("Change");
                    setDishFieldsEnable(false);
                    dishTableModel.updateList();
                    allDishesTable.updateUI();
                } catch (NumberFormatException e1) {
                    JOptionPane.showMessageDialog(allDishesPanel, "Wrong price!");
                }
            }
        }
    };

    private ActionListener removeDishActionListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (JOptionPane.showConfirmDialog(addChangeDishPanel, "Really remove dish?", "Dish deleting", JOptionPane.YES_NO_OPTION) == 0) {
                try {

                    if (adminService != null) adminService.removeDish(dishTableModel.getSelectedDish(), logined);
                    if (barmenService != null) barmenService.removeDish(dishTableModel.getSelectedDish(), logined);
                    JOptionPane.showMessageDialog(addChangeDishPanel, "Removed successfuly");
                    dishTableModel.updateList();
                    allDishesTable.updateUI();
                    if (adminService != null) {
                        addNewIngridientButton.setEnabled(false);
                        removeSelectedIngridientButton.setEnabled(false);
                    }
                } catch (ConstraintViolationException e1) {
                    JOptionPane.showMessageDialog(allDishesPanel, "Can`t delete dish as it is included in one of orderings, remove ordering firstly");
                } catch (UserAccessException e1) {
                    JOptionPane.showMessageDialog(allDishesPanel, e1);
                }
            }
        }
    };

    private ActionListener addIngridientActionListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            try {
                Ingridient added = adminService.addIngridient((Product) productsComboBox.getSelectedItem(),
                        Double.valueOf(inridientAmountField.getText()),
                        dishTableModel.getSelectedDish());
                ingridientTableModel.updateList();
                chosenDishIngridientsTable.updateUI();
                JOptionPane.showMessageDialog(addChangeIngridientPanel, "New Ingridient was successfully added");
                LOGGER.info("new Ingridient was Added" + added);
            } catch (NumberFormatException er) {
                JOptionPane.showMessageDialog(allDishesPanel, "Wrong ammount", "Error", JOptionPane.WARNING_MESSAGE);
            }
        }
    };

    private ActionListener removeIngridientActionListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (JOptionPane.showConfirmDialog(addChangeIngridientPanel, "Really remove Ingridient?", "Ingridient deleting", JOptionPane.YES_NO_OPTION) == 0) {
                try {
                    adminService.removeIngridientById(ingridientTableModel.getSelectedIngridient().getId());
                    JOptionPane.showMessageDialog(addChangeIngridientPanel, "Removed successfuly");
                    ingridientTableModel.updateList();
                    chosenDishIngridientsTable.updateUI();
                } catch (IngridientWithIDNotFoundException e1) {
                    LOGGER.error("cant delete ingridient " + e1);
                }
            }
        }
    };


    private void initialiseDishTable() {
        String[] head = new String[]{"Dish", "Type", "price", "Description", "Available"};
        dishTableModel = new DishTableModel(head);
        allDishesTable.setModel(dishTableModel);
    }

    private class DishTableModel extends AbstractTableModel {

        private java.util.List<Dish> dishs;
        private String[] colNames;


        public DishTableModel(String[] colNames) {
            this.colNames = colNames;
            updateList();
            allDishesTable.getSelectionModel().addListSelectionListener(listSelectionListener);

            initSorter();
        }

        private void initSorter() {
            TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(this);
            allDishesTable.setRowSorter(sorter);
            List<RowSorter.SortKey> sortKeys = new ArrayList<RowSorter.SortKey>();
            sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
            sorter.setSortKeys(sortKeys);
        }


        private ListSelectionListener listSelectionListener = new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                int selectedIndex = allDishesTable.getSelectedRow();
                selectedDish = dishs.get(selectedIndex);
                dishNameField.setText(selectedDish.getName());
                dishTypeComboBox.setSelectedItem(selectedDish.getType());
                dishPriceField.setText(String.valueOf(selectedDish.getPriceForPortion()));
                dishDescriptionField.setText(selectedDish.getDescription());
                whoCookComboBox.setSelectedItem(selectedDish.getWhoCoockDishType());
                removeDishButton.setEnabled(true);
                changeDishButton.setEnabled(true);
                if (adminService != null || waitersService != null) {
                    ingridientTableModel.updateList();
                    chosenDishIngridientsTable.updateUI();
                    if (adminService != null) {
                        addNewIngridientButton.setEnabled(true);
                        initProductComboBox();
                    }
                }
                if (barmenService != null) {
                    changeDishAvailable.setEnabled(true);
                    if (!selectedDish.getType().equals(DishType.DISH)) {
                        if (selectedDish.isAvailable()) {
                            changeDishAvailable.setText("Disable");
                        } else {
                            changeDishAvailable.setText("Enable");
                        }
                    } else {
                        changeDishAvailable.setEnabled(false);
                    }
                }
            }
        };


        public int getRowCount() {
            if (dishs != null) return dishs.size();
            return 0;
        }

        public int getColumnCount() {
            return colNames.length;
        }

        @Override
        public String getColumnName(int column) {
            return colNames[column];
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            Dish selected = dishs.get(rowIndex);

            switch (columnIndex) {
                case 0:
                    return selected.getName();
                case 1:
                    return selected.getType();
                case 2:
                    return selected.getPriceForPortion();
                case 3:
                    return selected.getDescription();
                case 4:
                    return selected.isAvailable();
                default:
                    return null;
            }
        }

        public void updateList() {
            if (adminService != null) this.dishs = adminService.getAllDishes();
            if (barmenService != null) this.dishs = barmenService.getAllDishes();
            if (waitersService != null) this.dishs = waitersService.getAllDishes();
        }


        public Dish getSelectedDish() {
            return selectedDish;
        }

    }


    private class IngridientTableModel extends AbstractTableModel {

        private java.util.List<Ingridient> ingridients;
        private String[] colNames;
        private Ingridient selectedIngridient;

        public IngridientTableModel(String[] colNames) {
            this.colNames = colNames;
            updateList();
            chosenDishIngridientsTable.getSelectionModel().addListSelectionListener(listSelectionListener);
        }


        private ListSelectionListener listSelectionListener = new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                int selectedIndex = chosenDishIngridientsTable.getSelectedRow();
                selectedIngridient = ingridients.get(selectedIndex);
                if (adminService != null) {
                    removeSelectedIngridientButton.setEnabled(true);
                    addNewIngridientButton.setEnabled(true);
                }
            }
        };


        public int getRowCount() {
            return ingridients.size();
        }

        public int getColumnCount() {
            return colNames.length;
        }

        @Override
        public String getColumnName(int column) {
            return colNames[column];
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            Ingridient selected = ingridients.get(rowIndex);

            switch (columnIndex) {
                case 0:
                    return selected.getDish().getName();
                case 1:
                    return selected.getProduct().getName();
                case 2:
                    return selected.getAmount();
                case 3:
                    return selected.getProduct().getMesuarment();
                default:
                    return null;
            }
        }

        public void updateList() {
            if (adminService != null)
                this.ingridients = adminService.getIngridientsByDish(dishTableModel.getSelectedDish());
            if (waitersService != null)
                this.ingridients = waitersService.getIngridientsByDish(dishTableModel.getSelectedDish());
        }


        public Ingridient getSelectedIngridient() {
            return selectedIngridient;
        }

    }

}
