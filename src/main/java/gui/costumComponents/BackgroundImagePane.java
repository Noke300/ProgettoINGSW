package gui.costumComponents;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class BackgroundImagePane extends JPanel {
    public Image backgroundImage;

    public BackgroundImagePane(String resourcePath) {
        try {
            URL resourceUrl = getClass().getResource(resourcePath);
            if (resourceUrl != null) {
                // Load the image using ImageIO, which can tell us if it's valid
                try {
                    BufferedImage buffImg = ImageIO.read(resourceUrl);
                    if (buffImg != null) {
                        backgroundImage = buffImg;
                        System.out.println("Successfully loaded valid image: " +
                                buffImg.getWidth() + "x" + buffImg.getHeight());
                    } else {
                        System.err.println("ImageIO could not read the image - format may be invalid");
                    }
                } catch (IOException ioe) {
                    System.err.println("Error reading image: " + ioe.getMessage());
                }
            }
        } catch (Exception e) {
            // Error handling
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            // Draw a placeholder indicating the image wasn't found
            g.setColor(Color.RED);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.WHITE);
            g.drawString("Image not found", 10, 20);
        }
    }
}