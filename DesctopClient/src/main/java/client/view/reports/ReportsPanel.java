package client.view.reports;

import net.sourceforge.jdatepicker.impl.DateComponentFormatter;
import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.SqlDateModel;
import server.model.denomination.Denomination;
import server.model.dish.ingridient.Ingridient;
import server.service.IAdminService;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;


public class ReportsPanel {
    private JPanel mainPanel;
    private JTabbedPane tabbedPane;
    private JPanel soltPanel;
    private JPanel requirePanel;
    private JRadioButton concretteDayRadioButton;
    private JRadioButton periodRadioButton;
    private JButton findButton;
    private JPanel filterPanel;
    private JPanel datePanel;
    private JPanel soltDishesPanel;
    private JPanel soltProductsPanel;
    private JPanel requireDishesPanel;
    private JPanel requireProductsPanel;
    private JTable soltDishesTable;
    private JTable soltProductsTable;
    private JTable requireDishesTable;
    private JTable requireProductsTable;
    private JScrollPane pane1;
    private JScrollPane pane2;
    private JScrollPane pane3;
    private JScrollPane pane4;
    private JButton pdfButton;
    private JPanel panelll;
    private IAdminService service;
    private SqlDateModel model1;
    private JDatePickerImpl picker1;
    private SqlDateModel model2;
    private JDatePickerImpl picker2;
    IngridientTableModel ingridientTableModel;
    DenominationTableModel denominationTableModel;


    public ReportsPanel(IAdminService service) {
        this.service = service;
        initComponents();
    }

    private void initComponents() {
        initTables();
        initFilterPanel();
        tabbedPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                denominationTableModel.setListNull();
                ingridientTableModel.setListNull();
                findButton.setEnabled(false);
            }
        });

    }

    private void initTables() {
        String[] denominationsHead = new String[]{"Dish", "Type", "Portion", "Price", "*portion"};
        String[] ingridientsHead = new String[]{"Product", "Mesurement", "Amount"};
        denominationTableModel = new DenominationTableModel(denominationsHead);
        ingridientTableModel = new IngridientTableModel(ingridientsHead);
        soltDishesTable.setModel(denominationTableModel);
        requireDishesTable.setModel(denominationTableModel);
        soltProductsTable.setModel(ingridientTableModel);
        requireProductsTable.setModel(ingridientTableModel);
    }

    private void initFilterPanel() {
        ButtonGroup group = new ButtonGroup();
        group.add(concretteDayRadioButton);
        group.add(periodRadioButton);
        concretteDayRadioButton.addActionListener(radioButtonsActionListener);
        periodRadioButton.addActionListener(radioButtonsActionListener);
        initDatePanel();
        findButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                denominationTableModel.updateList();
                ingridientTableModel.updateList();
            }
        });
        pdfButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String headString = "";
                if (soltDishesPanel.isShowing()) {
                    headString += "Solt on ";
                } else {
                    headString += "Require for ";
                }
                headString += model1.getValue().toString();
                if (picker2.isVisible()) headString += "  -  " + model2.getValue();
                try {
                    File generated = service.generateReport(denominationTableModel.getDenominations(), ingridientTableModel.getIngridients(), headString);
                    if (JOptionPane.showConfirmDialog(mainPanel, "Generated! Would you like to open?", "Open?", JOptionPane.YES_NO_OPTION) == 0)
                        if (Desktop.isDesktopSupported()) {
                        Desktop.getDesktop().open(generated);
                    }
                } catch (FileNotFoundException e1) {
                    JOptionPane.showMessageDialog(mainPanel, "Problems with writing pdf, select directory to save your pdf first!", e1.toString(), JOptionPane.WARNING_MESSAGE);
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(mainPanel, "Install application to read pdf, please");
                }
            }
        });
    }

    private ActionListener radioButtonsActionListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (concretteDayRadioButton.isSelected()) {
                findButton.setEnabled(true);
                picker1.setVisible(true);
                picker2.setVisible(false);
                filterPanel.updateUI();
            }
            if (periodRadioButton.isSelected()) {
                findButton.setEnabled(true);
                picker1.setVisible(true);
                picker2.setVisible(true);
                filterPanel.updateUI();
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
        filterPanel.updateUI();
    }

    private ChangeListener model1ChangeListener = new ChangeListener() {
        public void stateChanged(ChangeEvent e) {
            model2.setValue(model1.getValue());
            if (!checkDate()) {
                JOptionPane.showMessageDialog(mainPanel, "Wrong date, you can`t watch sells after today", "Change date", JOptionPane.WARNING_MESSAGE);
            } else {
                findButton.setEnabled(true);
            }
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
        LocalDate model1Date = LocalDate.of(model1.getYear(), model1.getMonth() + 1, model1.getDay());
        LocalDate model2Date = LocalDate.of(model2.getYear(), model2.getMonth() + 1, model2.getDay());
        LocalDate currentDate = LocalDate.now();
        if (soltPanel.isShowing())
            return ((model2Date.compareTo(model1Date) >= 0) && (model2Date.compareTo(currentDate) <= 0));
        if (requireDishesPanel.isShowing())
            return ((model2Date.compareTo(model1Date) >= 0) && (model2Date.compareTo(currentDate) >= 0));
        return false;
    }


    private class DenominationTableModel extends AbstractTableModel {

        private java.util.List<Denomination> denominations;
        private String[] colNames;


        public DenominationTableModel(String[] colNames) {
            this.colNames = colNames;
        }

        private void initSorter() {
            TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(this);
            soltDishesTable.setRowSorter(sorter);
            requireDishesTable.setRowSorter(sorter);
            java.util.List<RowSorter.SortKey> sortKeys = new ArrayList<>();
            sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
            sorter.setSortKeys(sortKeys);
        }


        public int getRowCount() {
            if (denominations == null || denominations.isEmpty()) return 0;
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
            if (denominations.isEmpty()) return null;
            Denomination selected = denominations.get(rowIndex);

            switch (columnIndex) {
                case 0:
                    return selected.getDish().getName();
                case 1:
                    return selected.getDish().getType();
                case 2:
                    return selected.getPortion();
                case 3:
                    return selected.getDish().getPriceForPortion();
                case 4:
                    return selected.getPrice();
                default:
                    return null;
            }
        }

        public void updateList() {
            if (soltDishesPanel.isShowing()) {
                if (concretteDayRadioButton.isSelected())
                    this.denominations = service.getSoltDenominationsForReport(LocalDate.of(model1.getYear(), model1.getMonth() + 1, model1.getDay()));
                if (periodRadioButton.isSelected())
                    this.denominations = service.getSoltDenominationsForReport(LocalDate.of(model1.getYear(), model1.getMonth() + 1, model1.getDay()),
                            LocalDate.of(model2.getYear(), model2.getMonth() + 1, model2.getDay()));
            }
            if (requirePanel.isShowing()) {
                if (concretteDayRadioButton.isSelected())
                    this.denominations = service.getRequireDenominationsForReport(LocalDate.of(model1.getYear(), model1.getMonth() + 1, model1.getDay()));
                if (periodRadioButton.isSelected())
                    this.denominations = service.getRequireDenominationsForReport(LocalDate.of(model1.getYear(), model1.getMonth() + 1, model1.getDay()),
                            LocalDate.of(model2.getYear(), model2.getMonth() + 1, model2.getDay()));
            }
            if (!denominations.isEmpty()) initSorter();
            ingridientTableModel.updateList();
        }

        public void setListNull() {
            this.denominations = new LinkedList<Denomination>();
        }

        public java.util.List<Denomination> getDenominations() {
            return denominations;
        }

    }


    private class IngridientTableModel extends AbstractTableModel {

        private java.util.List<Ingridient> ingridients;
        private String[] colNames;

        public IngridientTableModel(String[] colNames) {
            this.colNames = colNames;
        }

        private void initSorter() {
            TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(this);
            soltProductsTable.setRowSorter(sorter);
            requireProductsTable.setRowSorter(sorter);
            java.util.List<RowSorter.SortKey> sortKeys = new ArrayList<>();
            sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
            sorter.setSortKeys(sortKeys);
        }

        public int getRowCount() {
            if (ingridients == null || ingridients.isEmpty()) return 0;
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
            if (ingridients.isEmpty()) return null;
            Ingridient selected = ingridients.get(rowIndex);

            switch (columnIndex) {
                case 0:
                    return selected.getProduct().getName();
                case 1:
                    return selected.getProduct().getMesuarment();
                case 2:
                    return selected.getAmount();
                default:
                    return null;
            }
        }

        public void updateList() {
            this.ingridients = service.countIngridientsForReport(denominationTableModel.getDenominations());
            if (!ingridients.isEmpty()) initSorter();
        }

        public java.util.List<Ingridient> getIngridients() {
            return ingridients;
        }

        public void setListNull() {
            this.ingridients = new LinkedList<Ingridient>();
        }

    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
