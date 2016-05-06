package client.view.dialog;

import javax.swing.*;

/**
 * User: huyti
 * Date: 14.03.2016
 */
public class JDialogTest {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        new JDialogExt(frame.getOwner(), "Test", " huy psdfffffffffffffffffffffffffffffffffffffffffffffff" + "\n" +
                "sddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" + "\n" +
                "sddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" + "\n" +
                "sddddddddddddddddddddddddddddddddddddddddddddddddddddddddd" + "\n" +
                "sddddddddddddddddddddddddddddddddddddddddddddddddddddddddd");
    }
}
