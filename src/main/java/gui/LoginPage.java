package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginPage extends JPanel {

    public LoginPage() {
        setLayout(null);
        addComponents();
    }

    private void addComponents() {

        JLabel loginLabel = new JLabel("Login");
        loginLabel.setFont(loginLabel.getFont().deriveFont(64.0f));
        this.add(loginLabel);
        loginLabel.setBounds(500 - loginLabel.getPreferredSize().width/2,100,loginLabel.getPreferredSize().width, loginLabel.getPreferredSize().height);


        JLabel userNameLabel = new JLabel("username: ");
        userNameLabel.setFont(userNameLabel.getFont().deriveFont(20.0f));
        this.add(userNameLabel);
        userNameLabel.setBounds(250,220,userNameLabel.getPreferredSize().width, userNameLabel.getPreferredSize().height);

        JTextField usernameInput = new JTextField();
        this.add(usernameInput);
        usernameInput.setBorder(BorderFactory.createCompoundBorder(
                usernameInput.getBorder(),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        usernameInput.setBounds(360,210,300,50);
        usernameInput.setFont(new Font("SansSerif", Font.BOLD, 20));

        JLabel passLabel = new JLabel("password: ");
        passLabel.setFont(passLabel.getFont().deriveFont(20.0f));
        this.add(passLabel);
        passLabel.setBounds(250,280,passLabel.getPreferredSize().width, passLabel.getPreferredSize().height);

        JPasswordField passInput = new JPasswordField(20);
        this.add(passInput);
        passInput.setBorder(BorderFactory.createCompoundBorder(
                passInput.getBorder(),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
        passInput.setBounds(360,270,300,50);
        passInput.setFont(new Font("SansSerif", Font.BOLD, 20));

        JButton button = new JButton("Login");
        add(button);
        button.setBounds(400,340,200,75);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Insert code here
                MainPage.login(usernameInput.getText(), String.valueOf(passInput.getPassword()));
            }
        });

        JLabel goToRegisterLabel = new JLabel("Don't have an account yet.");
        goToRegisterLabel.setFont(goToRegisterLabel.getFont().deriveFont(10));
        this.add(goToRegisterLabel);
        goToRegisterLabel.setBounds(390,420,goToRegisterLabel.getPreferredSize().width, goToRegisterLabel.getPreferredSize().height);

        JLabel goToRegisterLabel2 = new JLabel("Sign Up!");
        goToRegisterLabel2.setFont(goToRegisterLabel2.getFont().deriveFont(10));
        this.add(goToRegisterLabel2);
        goToRegisterLabel2.setCursor(new Cursor(Cursor.HAND_CURSOR));
        goToRegisterLabel2.setForeground(Color.blue);
        goToRegisterLabel2.setBounds(560,420,goToRegisterLabel.getPreferredSize().width, goToRegisterLabel.getPreferredSize().height);
        goToRegisterLabel2.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent me)
            {
                MainPage.goToRegisterPage();
            }
        });

    }
}
