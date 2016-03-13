package client.view.cook;

import client.service.ICookService;
import client.view.cook.denomination.Denomination;
import org.apache.log4j.Logger;
import transferFiles.model.user.User;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;


public class WorkPanel {


    private static final Logger LOGGER = Logger.getLogger(WorkPanel.class);
    private Map<Integer, Denomination> denominations;
    private ICookService service;
    private User logined;
    private JPanel mainPanel;


    public WorkPanel(ICookService service, User logined) {
        mainPanel = new JPanel();
        mainPanel.setMaximumSize(new Dimension(780, 650));
        mainPanel.setPreferredSize(new Dimension(780, 650));
        denominations = new HashMap<Integer, Denomination>();
        this.service = service;
        this.logined = logined;
        mainPanel.setLayout(new FlowLayout());
        initTasksAdder();
    }

    private void initTasksAdder() {
        Thread thread = new Thread(new TasksGetter());
        thread.start();
    }

    public void buildPanel(List<transferFiles.model.denomination.Denomination> currDenoms) {
        fillSetDenoms(currDenoms);
        for (Map.Entry<Integer, Denomination> entry : denominations.entrySet()) {
            mainPanel.add(entry.getValue().getMainPanel());
        }
        mainPanel.updateUI();
    }

    public void addNewCurrentDenom(transferFiles.model.denomination.Denomination denomination) {
        denominations.put(denomination.getId(), new Denomination(denomination, service, logined, this));
        mainPanel.add(denominations.get(denomination.getId()).getMainPanel());
        mainPanel.updateUI();
    }

    public void removeCurrentDenom(transferFiles.model.denomination.Denomination denomination) {
        if (denominations.containsKey(denomination.getId())) {
            mainPanel.remove(denominations.get(denomination.getId()).getMainPanel());
            denominations.remove(denomination.getId());
            mainPanel.updateUI();
        }
    }

    private void fillSetDenoms(List<transferFiles.model.denomination.Denomination> currDenoms) {
        for (transferFiles.model.denomination.Denomination currDenom : currDenoms) {
            Denomination denomination = new Denomination(currDenom, service, logined, this);
            denominations.put(currDenom.getId(), denomination);
        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    private class TasksGetter implements Runnable {
        private List<transferFiles.model.denomination.Denomination> newTasks;

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.currentThread().sleep(10000);
                    newTasks = service.getNewDenominations(logined);
                    putOnView(newTasks);
                } catch (InterruptedException e) {
                    LOGGER.error(e);
                }
            }
        }

        private void putOnView(List<transferFiles.model.denomination.Denomination> newTasks) {
            if (newTasks != null && !newTasks.isEmpty()) {
                String message = "";
                for (transferFiles.model.denomination.Denomination newTask : newTasks) {
                    addNewCurrentDenom(newTask);
                    message = message + newTask.getDish().getName() + ", " + newTask.getPortion() + ", " + newTask.getOrder().getWhoServesOrder() + "\n";
                }
//                here must be single
                JOptionPane.showMessageDialog(mainPanel, "New Job :)" + "\n" + message);
            }
        }
    }

}
