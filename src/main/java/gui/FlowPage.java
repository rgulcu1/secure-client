package gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;

public class FlowPage extends JPanel {

    private static final String basePath = System.getProperty("user.dir");

    public FlowPage(String username) {
        setLayout(null);
        addElements(username);
        addNotificationHandler();
    }

    private void addElements(String username) {

        JLabel helloLabel = new JLabel("Hello "+username+"!");
        helloLabel.setFont(helloLabel.getFont().deriveFont(64.0f));
        this.add(helloLabel);
        helloLabel.setBounds(500 - helloLabel.getPreferredSize().width/2,100,helloLabel.getPreferredSize().width, helloLabel.getPreferredSize().height);

        JButton logoutBtn = new JButton("Logout");
        add(logoutBtn);
        logoutBtn.setBounds(850, 20, 100, 50);
        logoutBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                MainFrame.logout();
            }
        });

        JButton button = new JButton("IMAGE POST");
        add(button);
        button.setBounds(350, 270, 300, 120);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                browseButtonActionPerformed(e);
            }
        });

    }

    private void addNotificationHandler() {
        int delay = 500; //milliseconds
        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                MainFrame.askForNewImage();
            }
        };
        new Timer(delay, taskPerformer).start();
    }


    private void browseButtonActionPerformed(java.awt.event.ActionEvent evt) {
        JFileChooser fc = new JFileChooser(basePath);
        fc.setFileFilter(new JPEGImageFileFilter());
        int res = fc.showOpenDialog(null);
        // We have an image!
        try {
            if (res == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                MainFrame.postImage(file);
            } // Oops!
            else {
                JOptionPane.showMessageDialog(null,
                        "You must select one image to be the reference.", "Aborting...",
                        JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception iOException) {
        }

    }
}

class JPEGImageFileFilter extends javax.swing.filechooser.FileFilter implements FileFilter {
    public boolean accept(File f) {
        if (f.getName().toLowerCase().endsWith(".jpeg")) return true;
        if (f.getName().toLowerCase().endsWith(".jpg")) return true;
        if (f.getName().toLowerCase().endsWith(".png")) return true;
        if (f.isDirectory()) return true;
        return false;
    }

    public String getDescription() {
        return "JPEG files";
    }

}
