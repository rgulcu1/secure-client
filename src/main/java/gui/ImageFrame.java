package gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class ImageFrame extends JFrame {

    public ImageFrame(BufferedImage image) throws HeadlessException {
        final ImagePanel contentPane = new ImagePanel(image);
        setContentPane(contentPane);
        setPreferredSize(contentPane.getPreferredSize());
    }


}

class ImagePanel extends JPanel {

    private BufferedImage image;

    public ImagePanel(BufferedImage image) {
        this.image = image;
        setPreferredSize(new Dimension(this.image.getWidth(), this.image.getHeight()));
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this); // see javadoc for more info on the parameters
    }
}
