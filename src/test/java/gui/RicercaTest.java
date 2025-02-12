package gui;

import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import static org.junit.Assert.*;

public class RicercaTest {

    @Test
    public void testCreateRicercaPanel_NotNull() {
        JFrame frame = new JFrame("Test Frame");
        Ricerca ricerca = new Ricerca();

        JPanel resultPanel = ricerca.createRicercaPanel(frame);

        assertNotNull("The returned panel should not be null.", resultPanel);
    }

    @Test
    public void testCreateRicercaPanel_Layout() {
        JFrame frame = new JFrame("Test Frame");
        Ricerca ricerca = new Ricerca();

        JPanel resultPanel = ricerca.createRicercaPanel(frame);

        assertTrue("The main panel's layout should be of BoxLayout type.",
                resultPanel.getLayout() instanceof BoxLayout);
    }

    @Test
    public void testCreateRicercaPanel_ContainsComponents() {
        JFrame frame = new JFrame("Test Frame");
        Ricerca ricerca = new Ricerca();

        JPanel resultPanel = ricerca.createRicercaPanel(frame);

        boolean containsSearchField = false;
        boolean containsSearchButton = false;
        boolean containsFilterCheckboxes = false;

        for (Component component : resultPanel.getComponents()) {
            if (component instanceof JPanel) {
                JPanel panel = (JPanel) component;
                for (Component innerComponent : panel.getComponents()) {
                    if (innerComponent instanceof JTextField) {
                        containsSearchField = true;
                    }
                    if (innerComponent instanceof JButton && ((JButton) innerComponent).getText().equals("Cerca")) {
                        containsSearchButton = true;
                    }
                    if (innerComponent instanceof JCheckBox) {
                        containsFilterCheckboxes = true;
                    }
                }
            }
        }

        assertTrue("The panel should contain a search text field.", containsSearchField);
        assertTrue("The panel should contain a search button.", containsSearchButton);
        assertTrue("The panel should contain some filter checkboxes.", containsFilterCheckboxes);
    }

    @Test
    public void testCreateRicercaPanel_BackButtonFunctionality() {
        JFrame frame = new JFrame("Test Frame");
        Ricerca ricerca = new Ricerca();
        JPanel resultPanel = ricerca.createRicercaPanel(frame);

        boolean backButtonExists = false;
        JButton backButton = null;

        for (Component component : resultPanel.getComponents()) {
            if (component instanceof JButton && ((JButton) component).getText().equals("Torna alla Home")) {
                backButtonExists = true;
                backButton = (JButton) component;
                break;
            }
        }

        assertTrue("The panel should contain a back button.", backButtonExists);
        assertNotNull("Back button should not be null.", backButton);

        backButton.doClick();
        JPanel currentContentPane = (JPanel) frame.getContentPane();
        assertNotSame("After clicking the back button, the content pane should change.",
                resultPanel, currentContentPane);
    }

    @Test
    public void testCreateRicercaPanel_SearchFieldEditable() {
        JFrame frame = new JFrame("Test Frame");
        Ricerca ricerca = new Ricerca();
        JPanel resultPanel = ricerca.createRicercaPanel(frame);

        JTextField searchField = null;
        for (Component component : resultPanel.getComponents()) {
            if (component instanceof JPanel) {
                JPanel panel = (JPanel) component;
                for (Component innerComponent : panel.getComponents()) {
                    if (innerComponent instanceof JTextField) {
                        searchField = (JTextField) innerComponent;
                        break;
                    }
                }
            }
        }

        assertNotNull("The panel should contain a search text field.", searchField);
        assertTrue("The search field should be editable.", searchField.isEditable());
    }
}