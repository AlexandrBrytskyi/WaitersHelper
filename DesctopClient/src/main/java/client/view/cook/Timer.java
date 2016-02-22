package client.view.cook;

import org.apache.log4j.Logger;

import javax.swing.*;


public class Timer {
    private JPanel mainPanel;
    private JLabel timeLeftLabel;
    private static final Logger LOGGER = Logger.getLogger(Timer.class);
    private long secondsLeft = 0;
    private long minutesLeft = 0;
    private Thread timeCountingThread;
    private boolean flag;


    public Timer() {
        timeCountingThread = new Thread(new TimeCounter());
        flag = true;
        timeCountingThread.start();
    }


    public void stopConting() {
        flag = false;
    }


    private class TimeCounter implements Runnable {

        public void run() {
            while (flag) {
                try {
                    Thread.currentThread().sleep(1000);
                    secondsLeft++;
                    checkSeconds();
                    timeLeftLabel.setText(minutesLeft + ":" + secondsLeft);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void checkSeconds() {
            if (secondsLeft == 60) {
                minutesLeft++;
                secondsLeft = 0;
            }
        }
    }


    public JPanel getMainPanel() {
        return mainPanel;
    }

}
