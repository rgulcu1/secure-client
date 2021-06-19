package gui;

import org.apache.commons.io.FilenameUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import server.MainService;


import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Objects;


public class MainFrame extends JFrame {

    static CardLayout cardLayout;
    static Container container;
    static MainService mainService = new MainService();

    public MainFrame(String title) throws HeadlessException {
        super(title);
        cardLayout = new CardLayout(0, 0);
        setResizable(false);
        container = getContentPane();
        container.setLayout(cardLayout);
        container.add("login", new LoginPage());
        container.add("register", new RegisterPage());
        addActionListener();
    }

    public static void register(String username, String password) {
        final Boolean registerSuccess = mainService.register(username,password);
        if(registerSuccess){
            container.add("flow", new FlowPage(username));
            cardLayout.show(container, "flow");
        }
        else System.err.println("Register Failed!");
    }

    public static void login(String username, String password) {
        final Boolean loginSuccess = mainService.login(username,password);
        if(loginSuccess) {
            container.add("flow", new FlowPage(username));
            cardLayout.show(container, "flow");
        }
        else System.err.println("Login Failed!");
    }

    public static void logout() {
        final Boolean logoutSuccess = mainService.logout();
        if(logoutSuccess) {
            cardLayout.show(container, "login");
        }
        else System.err.println("Logout Failed!");
    }

    public static void postImage(File file) {
        final String extension = FilenameUtils.getExtension(file.toString());
        if(!extension.equals("jpeg") && !extension.equals("jpg") && !extension.equals("png")) {
            System.err.println("File type is not accepted!");
            return;
        }
        mainService.postImage(file);
    }

    public static void goToRegisterPage() {
       cardLayout.show(container, "register");
    }

    public static void askForNewImage() {
        final JSONArray newImagePosted = mainService.askForNewImage();
        if(Objects.nonNull(newImagePosted)) {
            for (int i = 0; i < newImagePosted.length(); i++) {
                final JSONObject imageInfo = newImagePosted.getJSONObject(i);
                final NotificationFrame notificationFrame = new NotificationFrame(mainService.getUsername(), imageInfo.getString("imageName"), imageInfo.getString("owner"));
                notificationFrame.setVisible(true);
                notificationFrame.setSize(400,170);
                notificationFrame.setLocationRelativeTo(null);
            }
        }
    }

    public static void displayImage(String imageName){

        final BufferedImage bfImage = mainService.displayImage(imageName);

        if (Objects.isNull(bfImage)) return;

        final ImageFrame imageFrame = new ImageFrame(bfImage);
        imageFrame.setVisible(true);
        imageFrame.setSize(imageFrame.getPreferredSize());
        imageFrame.setLocationRelativeTo(null);
    }

    private void addActionListener() {
        this.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                mainService.logout();
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });
    }
}

