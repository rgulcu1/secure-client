package gui;

import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;
import server.MainService;
import server.ServerComm;
import user.User;

import javax.swing.*;
import java.awt.*;
import java.io.File;


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
        container.add("login", new LoginPage());
        container.add("register", new RegisterPage());
        container.add("flow", new FlowPage());
    }

    public static void register(String username, String password) {
        final Boolean registerSuccess = mainService.register(username,password);
        if(registerSuccess) cardLayout.show(container, "flow");
        else System.out.println("Register Failed!");
    }

    public static void login(String username, String password) {
        final Boolean loginSuccess = mainService.login(username,password);
        if(loginSuccess) cardLayout.show(container, "flow");
        else System.out.println("Login Failed!");
    }

    public static void postImage(File file) {
        final String extension = FilenameUtils.getExtension(file.toString());
        if(!extension.equals("jpeg") && !extension.equals("jpg") && !extension.equals("png")) {
            System.out.println("File type is not accepted!");
            return;
        }
        mainService.postImage(file);
    }

    public static void goToRegisterPage() {
       cardLayout.show(container, "register");
    }
}

