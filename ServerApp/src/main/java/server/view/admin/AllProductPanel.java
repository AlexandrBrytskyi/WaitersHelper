package server.view.admin;

import org.apache.log4j.Logger;
import server.exceptions.ProductByIdNotFoundException;
import server.model.dish.ingridient.Mesuarment;
import server.model.dish.ingridient.Product;
import server.service.IAdminService;

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

public class AllProductPanel {


    private JPanel allProductsPanel;
    private JScrollPane srollPaneProducts;
    private JTable allProductsTable;
    private JPanel addProductPanel;
    private JButton addProductButton;
    private JTextField productNameField;
    private JComboBox productMesurementComboBox;
    private JPanel selectedProductPanel;
    private JTextField selectedProductTextField;
    private JComboBox selectedProductComboBox;
    private JButton changeSelectedProductButton;
    private JButton removeProductButton;
    private JPanel panel;
    private ProductTableModel productTableModel;
    private boolean isAllProductsPanelInitialised = false;
    private IAdminService service;
    private Logger LOGGER;

    public AllProductPanel(IAdminService service, Logger LOGGER) {
        super();
        this.service = service;
        this.LOGGER = LOGGER;
    }

    public JPanel getAllProductsPanel() {
        return allProductsPanel;
    }

    public void initialiseAllProductsPanel() {
        if (!isAllProductsPanelInitialised) {
            LOGGER.info("initializing all product panel components");
            initialiseProductTable();
            initialiseAddProductPanel();
            initialiseSelectedProductPanel();
            isAllProductsPanelInitialised = true;
        } else {
            productTableModel.updateList();
            allProductsTable.updateUI();
            LOGGER.info("Just updating info in product tables");
        }
    }

    private void initialiseSelectedProductPanel() {
        for (Mesuarment mesuarment : Mesuarment.values()) {
            selectedProductComboBox.addItem(mesuarment);
        }
        selectedProductComboBox.setEnabled(false);
        removeProductButton.setEnabled(false);
        changeSelectedProductButton.setEnabled(false);
        selectedProductTextField.setEnabled(false);
        removeProductButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (JOptionPane.showConfirmDialog(selectedProductPanel, "Really Delete product?", "Delete",
                        JOptionPane.YES_NO_OPTION) == 0) {
                    try {
                        productTableModel.removeSelected();
                    } catch (ProductByIdNotFoundException e1) {
                        LOGGER.error(e1);
                    }
                    JOptionPane.showMessageDialog(selectedProductPanel, "Removed Successfully");
                    productTableModel.updateList();
                    allProductsTable.updateUI();
                    selectedProductTextField.setText("");
                }

            }
        });
        changeSelectedProductButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (changeSelectedProductButton.getText().equals("Submit")) {
                    if (JOptionPane.showConfirmDialog(selectedProductPanel, "Really change product", "Change",
                            JOptionPane.YES_NO_OPTION) == 0) {
                        try {
                            service.setMesuarmentById(productTableModel.getSelectedProduct().getId(),
                                    (Mesuarment) selectedProductComboBox.getSelectedItem());
                        } catch (ProductByIdNotFoundException e1) {
                            LOGGER.error(e1);
                        }
                    }
                    productTableModel.updateList();
                    allProductsTable.updateUI();
                    selectedProductComboBox.setEnabled(false);
                    changeSelectedProductButton.setText("Change");
                    removeProductButton.setEnabled(true);
                    allProductsTable.setEnabled(true);
                } else {
                    allProductsTable.setEnabled(false);
                    removeProductButton.setEnabled(false);
                    selectedProductComboBox.setEnabled(true);
                    changeSelectedProductButton.setText("Submit");
                }
            }
        });
    }

    private void initialiseAddProductPanel() {
        for (Mesuarment mesuarment : Mesuarment.values()) {
            productMesurementComboBox.addItem(mesuarment);
        }
        addProductButton.addActionListener(addProductButtonActionListener);
    }

    private ActionListener addProductButtonActionListener = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            Product product = new Product();
            product.setName(productNameField.getText());
            product.setMesuarment((Mesuarment) productMesurementComboBox.getSelectedItem());
            service.addProduct(product);
            productTableModel.updateList();
            allProductsTable.updateUI();
            JOptionPane.showMessageDialog(addProductPanel, "New Product was successfully added");
        }
    };

    private void initialiseProductTable() {
        String[] head = new String[]{"Product", "Mesurement"};
        java.util.List<Product> productList = service.getAllProducts();
        productTableModel = new ProductTableModel(head);
        allProductsTable.setModel(productTableModel);
    }

    private class ProductTableModel extends AbstractTableModel {

        private java.util.List<Product> products;
        private String[] colNames;
        private Product selectedProduct;
        private ListSelectionListener listSelectionListener = new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                int selectedIndex = allProductsTable.getSelectedRow();
                selectedProduct = products.get(selectedIndex);
                selectedProductTextField.setText(selectedProduct.getName());
                selectedProductComboBox.setSelectedItem(selectedProduct.getMesuarment());
                removeProductButton.setEnabled(true);
                changeSelectedProductButton.setEnabled(true);
            }
        };


        public ProductTableModel(String[] colNames) {
            this.colNames = colNames;
            updateList();
            allProductsTable.getSelectionModel().addListSelectionListener(listSelectionListener);
            initSorter();
        }

        private void initSorter() {
            TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(this);
            allProductsTable.setRowSorter(sorter);
            List<RowSorter.SortKey> sortKeys = new ArrayList<>();
            sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
            sorter.setSortKeys(sortKeys);
        }


        public int getRowCount() {
            return products.size();
        }

        public int getColumnCount() {
            return colNames.length;
        }

        @Override
        public String getColumnName(int column) {
            return colNames[column];
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            Product selected = products.get(rowIndex);

            switch (columnIndex) {
                case 0:
                    return selected.getName();
                case 1:
                    return selected.getMesuarment();
                default:
                    return null;
            }
        }

        public void updateList() {
            this.products = service.getAllProducts();
        }

        public void removeSelected() throws ProductByIdNotFoundException {
            service.removeProductById(selectedProduct.getId());
            products.remove(selectedProduct);
        }

        public Product getSelectedProduct() {
            return selectedProduct;
        }

    }

}