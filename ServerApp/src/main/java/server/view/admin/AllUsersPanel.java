package server.view.admin;

import org.apache.log4j.Logger;
import server.exceptions.UserFieldIsEmptyException;
import server.exceptions.WrongLoginException;
import server.exceptions.WrongPasswordException;
import server.model.user.User;
import server.model.user.UserType;
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

public class AllUsersPanel {
    private JPanel mainPanel;
    private JTable usersTable;
    private JButton addNewUserButton;
    private JTextField loginField;
    private JTextField nameField;
    private JComboBox typeComboBox;
    private JScrollPane pane;
    private JButton lockSelectedButton;
    private IAdminService service;
    private static final Logger LOGGER = Logger.getLogger(AllUsersPanel.class);
    private AllUsersTableModel allUsersTableModel;
    private boolean isAlreadyInitialised = false;


    public AllUsersPanel(IAdminService service) {
        this.service = service;

    }

    public void initialiseAllUsersPanel() {
        if (!isAlreadyInitialised) {
            LOGGER.info("initializing all Users panel components");
            initTable();
            initAddUserPanel();
            isAlreadyInitialised = true;
            mainPanel.updateUI();
        } else {
            allUsersTableModel.updateList();
            usersTable.updateUI();
            LOGGER.info("Just updating info in allUsers table");
        }
    }

    public JPanel getAllUsersPanel() {
        return mainPanel;
    }

    private void initAddUserPanel() {
        initTypeComboBox();
        addNewUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (nameField.getText() != null && loginField != null &&
                        JOptionPane.showConfirmDialog(mainPanel, "Are you sure?", "New User", JOptionPane.YES_NO_OPTION) == 0) {
                    User newUser = new User();
                    newUser.setName(nameField.getText());
                    newUser.setLogin(loginField.getText());
                    newUser.setType((UserType) typeComboBox.getSelectedItem());
                    try {
                        newUser.setPass("00000");
                    } catch (WrongPasswordException e1) {
                        e1.printStackTrace();
                    }
                    try {
                        service.addNewUser(newUser);
                        allUsersTableModel.updateList();
                        usersTable.updateUI();
                        JOptionPane.showMessageDialog(mainPanel, "Added successfully");
                    } catch (UserFieldIsEmptyException e1) {
                        JOptionPane.showMessageDialog(mainPanel, e1);
                    } catch (WrongLoginException e1) {
                        JOptionPane.showMessageDialog(mainPanel, e1);
                    }
                }
            }
        });
        lockSelectedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!allUsersTableModel.getSelectedUser().isLocked()) {
                    if (JOptionPane.showConfirmDialog(mainPanel, "Reaaly lock this user?", "User lock", JOptionPane.YES_NO_OPTION) == 0) {
                        service.lockUser(allUsersTableModel.getSelectedUser(), true);
                    }
                } else {
                    if (JOptionPane.showConfirmDialog(mainPanel, "Reaaly unlock this user?", "User unlock", JOptionPane.YES_NO_OPTION) == 0) {
                        service.lockUser(allUsersTableModel.getSelectedUser(), false);
                    }
                }
                allUsersTableModel.updateList();
                usersTable.updateUI();
            }
        });
    }

    private void initTypeComboBox() {
        for (UserType userType : UserType.values()) {
            typeComboBox.addItem(userType);
        }
        typeComboBox.setSelectedIndex(0);
    }

    private void initTable() {
        allUsersTableModel = new AllUsersTableModel(new String[]{"Login", "Name", "Type", "Locked status"});
        usersTable.setModel(allUsersTableModel);
        usersTable.updateUI();
    }


    private class AllUsersTableModel extends AbstractTableModel {

        private java.util.List<User> users;
        private String[] colNames;
        private User selectedUser;
        private ListSelectionListener listSelectionListener = new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                int selectedIndex = usersTable.getSelectedRow();
                selectedUser = users.get(selectedIndex);
                if (selectedUser.isLocked()) {
                    lockSelectedButton.setText("Unlock Selected");
                } else {
                    lockSelectedButton.setText("Lock selected");
                }
            }
        };


        public AllUsersTableModel(String[] colNames) {
            this.colNames = colNames;
            updateList();
            initSorter();
            usersTable.getSelectionModel().addListSelectionListener(listSelectionListener);
        }

        private void initSorter() {
            TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(this);
            usersTable.setRowSorter(sorter);
            List<RowSorter.SortKey> sortKeys = new ArrayList<>();
            sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
            sorter.setSortKeys(sortKeys);
        }

        public int getRowCount() {
            return users.size();
        }

        public int getColumnCount() {
            return colNames.length;
        }

        @Override
        public String getColumnName(int column) {
            return colNames[column];
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            User selected = users.get(rowIndex);

            switch (columnIndex) {
                case 0:
                    return selected.getLogin();
                case 1:
                    return selected.getName();
                case 2:
                    return selected.getType();
                case 3:
                    return selected.isLocked();
                default:
                    return null;
            }
        }

        public void updateList() {
            this.users = service.getAllUsers();
        }

        public User getSelectedUser() {
            return selectedUser;
        }

    }


}
