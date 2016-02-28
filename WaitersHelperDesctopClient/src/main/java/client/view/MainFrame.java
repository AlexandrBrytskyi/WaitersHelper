package client.view;


import client.service.IAdminService;
import client.validator.IValidator;
import client.view.admin.AdminUI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import transferFiles.exceptions.AccountBlockedException;
import transferFiles.exceptions.WrongLoginException;
import transferFiles.exceptions.WrongPasswordException;
import transferFiles.model.user.User;
import transferFiles.model.user.UserType;
import transferFiles.to.Loginable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;


@org.springframework.stereotype.Component
public class MainFrame extends JFrame {

    @Autowired(required = true)
    private ApplicationContext appContext;

    @Autowired (required = true)
    @Qualifier("LoginValidator")
    IValidator validator;

    private JLabel topLabel;
    private JPanel mainPanel;
    private JTextField passwordField;
    private JTextField loginField;
    private JButton connectButton;
    private JLabel loginLabel;
    private JLabel passportLabel;



    public MainFrame() throws HeadlessException {

        super();
        setTitle("Authorization");
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setSize(500, 250);
        setResizable(false);
        setVisible(true);

        connectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String login = loginField.getText();
                String pass = passwordField.getText();
                User logged = null;
                try {
                    logged = validator.login(login, passwordField.getText());
                } catch (WrongLoginException e1) {
                    JOptionPane.showMessageDialog(mainPanel, e1.getMessage());
                    return;
                } catch (WrongPasswordException e1) {
                    JOptionPane.showMessageDialog(mainPanel, e1.getMessage());
                    return;
                } catch (AccountBlockedException e1) {
                    JOptionPane.showMessageDialog(mainPanel, e1.getMessage());
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }

                JOptionPane.showMessageDialog(mainPanel, logged.toString());
                loginField.setText(null);
                passwordField.setText(null);

                defineUI(logged);
            }
        });
        System.out.println("i am after adding main panel");
        add(mainPanel);

    }


    private void defineUI(User logged) {
        Loginable ui = null;
        if (logged.getType().equals(UserType.ADMIN)) ui = new AdminUI((IAdminService) appContext.getBean("adminService"), logged);
        if (logged.getType().equals(UserType.WAITER)) return;
        if (logged.getType().equals(UserType.HOT_KITCHEN_COCK) ||
                logged.getType().equals(UserType.COLD_KITCHEN_COCK) ||
                logged.getType().equals(UserType.MANGAL_COCK)) return;
        if (logged.getType().equals(UserType.BARMEN)) return;
//        if (ui != null) ui.sendUIToLoginedList();
    }


}
