package client.view.dialog;


import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class JDialogExt extends JDialog {
    private JPanel mainPanel;
    private JTextArea textArea;
    private JButton okButton;

    public JDialogExt(Window owner, String title, String text) {
        super(owner, title);
        textArea.setText(text);
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                dispose();
            }
        });
        add(mainPanel);
        setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(new Dimension(textArea.getWidth() + 30, textArea.getHeight() + 90));
        setLocationRelativeTo(owner);
        setResizable(false);
        Thread colorChangerThread = new Thread(new ColorChanger());
        colorChangerThread.start();
        playSound("WaitersHelperDesctopClient/src/main/java/client/view/dialog/sample.mp3");
    }


    private void playSound(String file) {
        try {
            Player player = new Player(new FileInputStream(file));
            player.play();
        } catch (JavaLayerException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private class ColorChanger implements Runnable {

        @Override
        public void run() {
            boolean jGot = false;
            boolean kGot = false;
            int i = 255; int j = 255; int k = 255;
            while (true) {
                for (i = 255; i >= 0; i--) {
                    if (!jGot)
                        for (j = 255; j >= 0; j--) {
                            if (!kGot)
                                for (k = 255; k >= 0; k--) {
                                    try {
                                        Thread.currentThread().sleep(10);
                                        mainPanel.setBackground(new Color(i, j, k));
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    if (k == 0) kGot = true;
                                }
                            if (j == 0) jGot = true;
                        }
                }

                for (i = 0; i <= 255; i++) {
                    if (jGot)
                        for (j = 0; j <= 255; j++) {
                            if (kGot);
                                for (k = 0; k <= 255; k++) {
                                    try {
                                        Thread.currentThread().sleep(10);
                                        mainPanel.setBackground(new Color(i, j, k));
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    if (k == 255) kGot = false;
                                }
                            if (j == 255) jGot = false;
                        }
                }
            }
        }

    }
}
