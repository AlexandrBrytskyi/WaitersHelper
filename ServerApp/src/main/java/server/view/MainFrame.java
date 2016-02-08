package server.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import server.exceptions.AccountBlockedException;
import server.exceptions.WrongLoginException;
import server.exceptions.WrongPasswordException;
import server.model.user.User;
import server.model.user.UserType;
import server.service.password_utils.Password;
import server.validator.IValidator;
import server.view.admin.AdminUI;
import server.view.barmen.BarmenUI;
import server.view.waiter.WaiterUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


@org.springframework.stereotype.Component
public class MainFrame extends JFrame {

    @Autowired(required = true)
    @Qualifier("myValidator")
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
                    logged = validator.login(login, new Password(pass));
                } catch (WrongLoginException e1) {
                    JOptionPane.showMessageDialog(mainPanel, e1.getMessage());
                    return;
                } catch (WrongPasswordException e1) {
                    JOptionPane.showMessageDialog(mainPanel, e1.getMessage());
                    return;
                } catch (AccountBlockedException e1) {
                    JOptionPane.showMessageDialog(mainPanel, e1.getMessage());
                }

                JOptionPane.showMessageDialog(mainPanel, logged.toString());
                loginField.setText(null);
                passwordField.setText(null);

                defineUI(logged);
            }
        });

        add(mainPanel);
    }


    private void defineUI(User logged) {
        if (logged.getType().equals(UserType.ADMIN)) new AdminUI(validator.getAdminService(logged), logged);
        if (logged.getType().equals(UserType.WAITER)) new WaiterUI(validator.getWaitersService(logged), logged);
        if (logged.getType().equals(UserType.COOK)) return;
        if (logged.getType().equals(UserType.BARMEN)) new BarmenUI(validator.getBarmenService(logged), logged);
    }

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("app-context.xml");
    }

}
