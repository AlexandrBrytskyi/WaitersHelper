package client.view.account;

import org.apache.log4j.Logger;
import server.exceptions.WrongPasswordException;
import server.model.user.User;
import server.service.IBarmenService;
import server.service.password_utils.Password;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class AccountFrame extends JFrame {
    private static boolean isAlreadyInited = false;
    private JPanel mainPanel;
    private JTextField nameField;
    private JPasswordField passwordField;
    private JButton changeButton;
    private JButton setNewButton;
    private JLabel nameLabel;
    private JLabel passwordLabel;
    private JPanel changePanel;
    private static final Logger LOGGER = Logger.getLogger(AccountFrame.class);
    private IBarmenService service;
    private User loginedUser;
    private static JFrame frame;

    public AccountFrame(IBarmenService service, User loginedUser) throws HeadlessException {
        super();
        setSize(500, 250);
        setResizable(false);
        this.service = service;
        this.loginedUser = loginedUser;
        initPanel();
        add(mainPanel);
        setTitle("My Account");
        setVisible(true);
    }

    private void initPanel() {
        nameField.setText(loginedUser.getName());
        changeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (JOptionPane.showConfirmDialog(mainPanel, "Reaaly change on " + nameField.getText() + "?", "Name changing", JOptionPane.YES_NO_OPTION) == 00) {
                    loginedUser = service.changeName(loginedUser, nameField.getText());
                    JOptionPane.showMessageDialog(mainPanel, "Name reseted");
                }
            }
        });

        setNewButton.addActionListener(new ActionListener() {
            boolean passChecked = false;

            public void actionPerformed(ActionEvent e) {

                if (setNewButton.getText().equals("Set New")) {
                    passwordField.setEnabled(true);
                    JOptionPane.showMessageDialog(mainPanel, "Enter current password");
                    setNewButton.setText("Submit");
                } else {
                    if (setNewButton.getText().equals("Submit")) {
                        if (!passChecked) {
                            try {
                                if (new Password(passwordField.getText()).compare(loginedUser.getPass())) {
                                    JOptionPane.showMessageDialog(mainPanel, "Enter new now!");
                                    passChecked = true;
                                } else{
                                    JOptionPane.showMessageDialog(mainPanel, "Invalid Password");
                                }
                            } catch (WrongPasswordException e1) {
                                JOptionPane.showMessageDialog(mainPanel, e1);
                            }
                        } else {
                            try {
                                loginedUser = service.changePassword(loginedUser, passwordField.getText());
                            } catch (WrongPasswordException e1) {
                                e1.printStackTrace();
                            }
                            JOptionPane.showMessageDialog(mainPanel, "Password resetted");
                            passChecked = false;
                            passwordField.setText(null);
                            passwordField.setEnabled(false);
                            setNewButton.setText("Set New");
                        }
                    }

                }
            }
        });

    }

    public static void getAccountFrame(IBarmenService service, User logged) {
        if (isAlreadyInited) {
            frame.pack();
            frame.toFront();
            frame.setVisible(true);
        } else {
            new AccountFrame(service, logged);
        }
    }
}
