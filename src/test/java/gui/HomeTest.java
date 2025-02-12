package gui;

import org.junit.Test;

import javax.swing.*;
import java.awt.*;

import static org.junit.Assert.*;

public class HomeTest {

    @Test
    public void testCreateHomePanel_ReturnsValidPanel() {
        // Arrange
        JFrame testFrame = new JFrame();
        Home home = new Home();

        // Act
        JPanel homePanel = home.createHomePanel(testFrame);

        // Assert
        assertNotNull(homePanel);
        assertEquals(BorderLayout.class, homePanel.getLayout().getClass());
        assertEquals(2, homePanel.getComponentCount());
    }

    @Test
    public void testCreateHomePanel_TitleLabelExists() {
        // Arrange
        JFrame testFrame = new JFrame();
        Home home = new Home();

        // Act
        JPanel homePanel = home.createHomePanel(testFrame);
        Component component = homePanel.getComponent(0);

        // Assert
        assertTrue(component instanceof JLabel);
        JLabel titleLabel = (JLabel) component;
        assertEquals("Benvenuto nella Home", titleLabel.getText());
        assertEquals(SwingConstants.CENTER, titleLabel.getHorizontalAlignment());
    }

    @Test
    public void testCreateHomePanel_ButtonExistsWithCorrectText() {
        // Arrange
        JFrame testFrame = new JFrame();
        Home home = new Home();

        // Act
        JPanel homePanel = home.createHomePanel(testFrame);
        Component component = homePanel.getComponent(1);

        // Assert
        assertTrue(component instanceof JButton);
        JButton accessoButton = (JButton) component;
        assertEquals("Accedi alla Mappa", accessoButton.getText());
    }

    @Test
    public void testCreateHomePanel_ButtonActionChangesContentPane() {
        // Arrange
        JFrame testFrame = new JFrame();
        Home home = new Home();
        Ricerca mockRicerca = new Ricerca(); // Use the actual Ricerca class.

        // Act
        JPanel homePanel = home.createHomePanel(testFrame);
        JButton accessoButton = (JButton) homePanel.getComponent(1);

        accessoButton.doClick();

        // Assert
        assertNotNull(testFrame.getContentPane());
        assertEquals(mockRicerca.createRicercaPanel(testFrame).getClass(), testFrame.getContentPane().getClass());
    }
}