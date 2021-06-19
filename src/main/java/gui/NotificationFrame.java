package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class NotificationFrame extends JFrame {
    Frame frame = this;
    public NotificationFrame(String title, String imageName, String username) throws HeadlessException {
        super(title);
        addElements(imageName, username);
    }

    private void addElements(String imageName, String username) {
        final JPanel panel = new JPanel();
        panel.setLayout(null);
        this.setContentPane(panel);

        final JLabel infoLabel = new JLabel( "New Image Posted!");
        panel.add(infoLabel);
        infoLabel.setFont(infoLabel.getFont().deriveFont(20.0f));
        infoLabel.setBounds(120,10,infoLabel.getPreferredSize().width, infoLabel.getPreferredSize().height);

        final JLabel imgNameLabel = new JLabel( "Image Name:");
        panel.add(imgNameLabel);
        imgNameLabel.setFont(imgNameLabel.getFont().deriveFont(Font.BOLD, 15f));
        imgNameLabel.setBounds(100,40,imgNameLabel.getPreferredSize().width, imgNameLabel.getPreferredSize().height);

        final JLabel imgName = new JLabel(imageName);
        panel.add(imgName);
        imgName.setFont(imgName.getFont().deriveFont(15f));
        imgName.setBounds(210,40,imgName.getPreferredSize().width, imgName.getPreferredSize().height);

        final JLabel ownerNameLabel = new JLabel( "Owner Name:");
        panel.add(ownerNameLabel);
        ownerNameLabel.setFont(ownerNameLabel.getFont().deriveFont(Font.BOLD, 15f));
        ownerNameLabel.setBounds(100,60,ownerNameLabel.getPreferredSize().width, ownerNameLabel.getPreferredSize().height);

        final JLabel ownerName = new JLabel(username);
        panel.add(ownerName);
        ownerName.setFont(ownerName.getFont().deriveFont(15f));
        ownerName.setBounds(210,60,ownerName.getPreferredSize().width, ownerName.getPreferredSize().height);

        JButton button = new JButton("Display");
        panel.add(button);
        button.setBounds(150,90,100,50);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Insert code here
                dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                MainFrame.displayImage(imageName);
            }
        });
    }
}
