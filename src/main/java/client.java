import gui.MainPage;
import server.ServerComm;
import util.Helper;

import java.io.IOException;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;


public class client {


    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Helper.calculateFirstFewPrime();
        MainPage mainPage = new MainPage("sa");
        mainPage.setVisible(true);
        mainPage.setSize(1000, 700);
        mainPage.setLocationRelativeTo(null);
        mainPage.setDefaultCloseOperation(EXIT_ON_CLOSE);

    }
}
