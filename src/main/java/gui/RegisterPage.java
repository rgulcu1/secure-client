package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterPage extends JPanel {

    public RegisterPage() {
        setLayout(null);
        addComponents();
    }

    private void addComponents() {

        JLabel registerLabel = new JLabel("Register");
        registerLabel.setFont(registerLabel.getFont().deriveFont(64.0f));
        this.add(registerLabel);
        registerLabel.setBounds(500 - registerLabel.getPreferredSize().width/2,100,registerLabel.getPreferredSize().width, registerLabel.getPreferredSize().height);


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

        JButton button = new JButton("Register");
        add(button);
        button.setBounds(400,270,200,75);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Insert code here
                MainPage.register(usernameInput.getText());
            }
        });
    }
}
