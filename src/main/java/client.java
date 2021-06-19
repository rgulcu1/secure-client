import gui.MainFrame;
import util.Helper;

import java.io.IOException;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;


public class client {


    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Helper.calculateFirstFewPrime();
        MainFrame mainFrame = new MainFrame("Secure Image Sharing Platform");
        mainFrame.setVisible(true);
        mainFrame.setSize(1000, 700);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);

    }
}
