package client.view.cook.denomination;

import client.service.ICookService;
import client.view.cook.Timer;
import org.apache.log4j.Logger;
import transferFiles.exceptions.UserAccessException;
import transferFiles.model.dish.ingridient.Ingridient;
import transferFiles.model.user.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Denomination {
    private JPanel mainPanel;
    private JButton readyButton;
    private JButton cancelButton;
    private JPanel timerPanel;
    private JLabel dishLabel;
    private JLabel portionLabel;
    private JLabel userLabel;
    private JLabel timeAddedLabel;
    private JPanel buttonsTimerPanel;
    private static final Logger LOGGER = Logger.getLogger(Denomination.class);
    private transferFiles.model.denomination.Denomination denomination;
    private ICookService service;
    private Timer timer;
    private User logined;

    public Denomination(transferFiles.model.denomination.Denomination denomination, ICookService service, User logined) {
        this.denomination = denomination;
        this.service = service;
        initComponents();
    }

    private void initComponents() {
        initLabels();
        initTimer();
        initButtons();
    }

    private void initTimer() {
        timer = new Timer();
        timerPanel.add(timer.getMainPanel());
    }

    private void initButtons() {
        readyButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                    service.setDenomStateReady();
                    timer.stopConting();

            }
        });

        cancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    service.cancelDenomination(denomination, logined);
                    timer.stopConting();
                } catch (UserAccessException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }

    private void initLabels() {
        dishLabel.setText(denomination.getDish().getName());
        dishLabel.setToolTipText(denomination.getDish().getDescription());
        portionLabel.setText(String.valueOf(denomination.getPortion()));
        String ingridients = "Ingridients \n";
        for (Ingridient ingridient : denomination.getDish().getIngridients()) {
            ingridients += ingridient.getProduct().getName() + ", " + ingridient.getAmount() + "\n";
        }
        portionLabel.setToolTipText(ingridients);
        userLabel.setText(denomination.getOrder().getWhoServesOrder().getName());
        timeAddedLabel.setText(denomination.getTimeWhenAdded().toLocalTime().toString());
        timeAddedLabel.setToolTipText(denomination.getTimeWhenAdded().toString());
    }
}
