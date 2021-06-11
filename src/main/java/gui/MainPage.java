package gui;

import org.json.JSONObject;
import server.MainService;
import server.ServerComm;
import user.User;

import javax.swing.*;
import java.awt.*;


public class MainPage extends JFrame {

    static CardLayout cardLayout;
    static Container container;
    static MainService mainService = new MainService();

    public MainPage(String title) throws HeadlessException {
        super(title);
        cardLayout = new CardLayout(0, 0);
        setResizable(false);
        container = getContentPane();
        container.setLayout(cardLayout);
        container.add("a", new RegisterPage());
        container.add("b", new JLabel("as"));
        //cardLayout.next(container);
    }

    public static void register(String username) {
        final Boolean registerSuccess = mainService.register(username);
        if(registerSuccess) cardLayout.next(container);
        else System.out.println("Register Failed!");
    }
}

