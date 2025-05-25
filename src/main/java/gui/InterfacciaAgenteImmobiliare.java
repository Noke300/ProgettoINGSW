/*
 * Created by JFormDesigner on Tue Apr 22 11:59:05 CEST 2025
 */

package gui;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;

import java.time.LocalDate;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import com.toedter.calendar.*;

import controller.ControllerAdministration;
import controller.ControllerClienti;

import gui.costum_components.*;
import net.miginfocom.swing.*;
import org.jxmapviewer.viewer.GeoPosition;


/**
 * @author marco
 */
public class InterfacciaAgenteImmobiliare extends JPanel {
    MapLoader mapLoader;
    private static final IconClass iconSet = new IconClass();
    GeoPosition napoli = new GeoPosition(45.464170, 11.885890);
    List<JCheckBox> checkBoxesList = new ArrayList<>();
    private JPanel selectedPanel = null;
    //--Border per gli immobili selected
    private final Color borderColor = new Color(24, 122, 26);
    private final Font borderFont = new Font("Segoe UI", Font.BOLD, 18);
    private List<ColoredDate> coloredDates = new ArrayList<>();



    private static class BorderStyler {
        public static TitledBorder createStyledBorder(String title, Color lineColor, int thickness, Font font) {
            TitledBorder titledBorder = new TitledBorder(title);
            titledBorder.setBorder(BorderFactory.createLineBorder(lineColor, thickness));
            titledBorder.setTitleColor(new Color(0, 0, 0));
            titledBorder.setTitleFont(font);
            return titledBorder;
        }
        public static Border createSelectedBorder(String title, Color lineColor, Font font) {
            TitledBorder innerBorder = createStyledBorder(title, lineColor, 1, font);
            return BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(lineColor, 2, true),
                    innerBorder
            );
        }

    }

    @Override
    public void addNotify() {
        super.addNotify();
        // Assicurati che tutti i menu popup siano nascosti quando il pannello viene aggiunto
        MenuSelectionManager.defaultManager().clearSelectedPath();
    }

    public InterfacciaAgenteImmobiliare(JFrame frame, ControllerClienti control, String token) {
        initComponents();

        /*
        ControllerAdministration controller = new ControllerAdministration();
        ArrayList date = new ArrayList();

        date = controller.recuperoData(token);
         */


        backButtonHome.addActionListener(e -> {
            // Supponiamo che la classe Home abbia il metodo createHomePanel
            menuOpzioni.setVisible(false);
            menuBarOpzioni.setVisible(false);

            frame.setContentPane(new Home().createHomePanel(frame));
            frame.revalidate();
            frame.repaint();
        });

        logInButton.addActionListener(e -> {
            menuOpzioni.setVisible(false);
            menuBarOpzioni.setVisible(false);

            frame.setContentPane(new LogIn(frame,control,token));
            frame.revalidate();
            frame.repaint();
        });

        ricercaButton.addActionListener(e-> {

            frame.setContentPane(new Ricerca().createRicercaPanel(frame,control,token));
            frame.revalidate();
            frame.repaint();
        });


        mapLoader = new MapLoader(napoli,checkBoxesList,panelMap);
        // Create and add the first panel

        // In the initComponents method, replace the GridLayout with MigLayout
        panelListaImmobile.setLayout(new MigLayout("wrap 1, align center")); // One component per row

        JPanel firstPanel = createImmobileSingoloPanel("Casa 1");
        panelListaImmobile.add(firstPanel,"align center");

        // Create and add the second panel
        JPanel secondPanel = createImmobileSingoloPanel("Casa 2");
        panelListaImmobile.add(secondPanel,"align center");

        JPanel thirdPanel = createImmobileSingoloPanel("Casa 3");
        thirdPanel.setBorder(new TitledBorder("Appartamento"));
        panelListaImmobile.add(thirdPanel,"align center");

        JPanel forthPanel = createImmobileSingoloPanel("Casa 4");
        forthPanel.setBorder(new TitledBorder("Appartamento"));
        panelListaImmobile.add(forthPanel,"align center");

        JPanel fifthPanel = createImmobileSingoloPanel("Casa 5");
        panelListaImmobile.add(fifthPanel,"align center");


        buttonElimina.addActionListener(e -> {
            if (selectedPanel != null) {
                panelListaImmobile.remove(selectedPanel);
                selectedPanel = null;
                buttonElimina.setEnabled(false);
                panelListaImmobile.revalidate();
                panelListaImmobile.repaint();
            }
        });

        //--Calendario
        // Then in your initialization or constructor method
        buttonCalendario.setIcon(iconSet.getCalendarioIcon());
        buttonCalendario.setText(LocalDate.now().toString());

        JCalendar calendar = new JCalendar();
        calendar.setLocale(Locale.ITALY);
        JDayChooser dayChooser = calendar.getDayChooser();

// Create a new implementation of DateColorManager
        DateColorManager dateColorManager = new DateColorManager(dayChooser);

// Create dates properly using Calendar
        Calendar cal = Calendar.getInstance();
        cal.set(2025, Calendar.MAY, 14);
        coloredDates.add(new ColoredDate(cal.getTime()));
        cal.set(2025, Calendar.MAY, 20);
        coloredDates.add(new ColoredDate(cal.getTime()));

// Initialize the date manager with the colored dates right away
        dateColorManager.setColoredDates(coloredDates);

        calendar.addPropertyChangeListener(evt -> {
            if ("calendar".equals(evt.getPropertyName())) {
                dateColorManager.updateDayColors();
            }
        });

        JPanel controlPanel = new JPanel(new BorderLayout());
        JButton addDateButton = new JButton("Info Giorno");
        controlPanel.add(addDateButton, BorderLayout.SOUTH);

        buttonCalendario.addActionListener(e -> {
            final JDialog calendarPopup = new JDialog(frame, "Calendario", true);
            calendarPopup.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            calendarPopup.setSize(700, 450);
            calendarPopup.setResizable(false);
            calendarPopup.add(calendar, BorderLayout.CENTER);
            calendarPopup.add(controlPanel, BorderLayout.SOUTH);
            calendarPopup.setLocationRelativeTo(frame);
            calendarPopup.setVisible(true);

            // We don't need to add this listener every time the calendar button is clicked
            // This should be set up once outside the action listener
        });

// Set up the addDateButton action listener outside the calendar button action listener
        addDateButton.addActionListener(h -> {
            // Get the currently selected date from the calendar
            Date selectedDate = calendar.getDate();
            //selectedDate.setDate(selectedDate.getDate() + 7);
            // Create a new Calendar instance for the selected date
            Calendar selectedCal = Calendar.getInstance();
            selectedCal.setTime(selectedDate);

            // Create a properly normalized date to ensure we're working with the exact date selected
            Calendar normalizedCal = Calendar.getInstance();
            normalizedCal.clear(); // Clear all fields
            normalizedCal.set(Calendar.YEAR, selectedCal.get(Calendar.YEAR));
            normalizedCal.set(Calendar.MONTH, selectedCal.get(Calendar.MONTH));
            normalizedCal.set(Calendar.DAY_OF_MONTH, selectedCal.get(Calendar.DAY_OF_MONTH));

            // Create ColoredDate with the normalized date
            ColoredDate newColoredDate = new ColoredDate(normalizedCal.getTime());

            // Check if this date is already colored to avoid duplicates
            boolean dateAlreadyExists = false;
            for (ColoredDate existingDate : coloredDates) {
                if (existingDate.isSameDay(normalizedCal.getTime())) {
                    dateAlreadyExists = true;
                    break;
                }
            }

            // Only add if it's a new date
            if (!dateAlreadyExists) {
                coloredDates.add(newColoredDate);
                dateColorManager.setColoredDates(coloredDates);
                System.out.println("Added colored date: " + normalizedCal.getTime());
            } else {
                System.out.println("Date already colored: " + normalizedCal.getTime());
            }
        });

// Set up the addDateButton action listener outside the calendar button action listener

        
    }

    private JPanel createImmobileSingoloPanel(String casaName) {
        JPanel panel = new JPanel();
        panel.setBorder(BorderStyler.createStyledBorder(casaName, borderColor, 1, borderFont));
        panel.setPreferredSize(new Dimension(700, 170));
        panel.setLayout(new MigLayout(
                "hidemode 3",
                "[325][325][325]",
                "[95][95][95][95]"));

        // Panel with the image as a background
        try {
            URL imageUrl = getClass().getResource("/CasaProva2.jpeg");
            if (imageUrl != null) {
                ImageIcon icon = new ImageIcon(imageUrl);
                JLabel imageLabel = new JLabel(icon);
                imageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                panel.add(imageLabel, "cell 0 0 2 2");
            } else {
                JLabel errorLabel = new JLabel("Image not found");
                errorLabel.setBackground(Color.RED);
                errorLabel.setOpaque(true);
                panel.add(errorLabel, "cell 0 0 2 2");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Adding other components to the main pane
        JLabel labelIndirizzo = new JLabel("Indirizzo");
        panel.add(labelIndirizzo, "cell 2 0 2 1");

        JLabel labelNStanze = new JLabel("Numero Stanze");
        panel.add(labelNStanze, "cell 2 1");

        JLabel labelNBagni = new JLabel("Numero Bagni");
        panel.add(labelNBagni, "cell 3 1");

        JLabel labelPrezzo = new JLabel("Prezzo");
        panel.add(labelPrezzo, "cell 0 2 2 2,alignx center,growx 0");

        JLabel labelMetriQuadri = new JLabel("M^2");
        panel.add(labelMetriQuadri, "cell 2 2");

        JLabel labelClasseEnergetica = new JLabel("A4");
        panel.add(labelClasseEnergetica, "cell 3 2");

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectPanel(panel);
            }
        });

        return panel;
    }
    private void selectPanel(JPanel panel) {
        // Deselect a previous panel if exists
        if (selectedPanel != null) {
            // Get the title from the current border
            String title;
            Border currentBorder = selectedPanel.getBorder();

            if (currentBorder instanceof CompoundBorder) {
                // It's currently selected - extract title from an inner border
                TitledBorder innerBorder = (TitledBorder)((CompoundBorder)currentBorder).getInsideBorder();
                title = innerBorder.getTitle();
            } else {
                // It's a simple TitledBorder
                title = ((TitledBorder)currentBorder).getTitle();
            }

            // Restore standard styled border with the same title
            selectedPanel.setBorder(BorderStyler.createStyledBorder(title, borderColor, 1, borderFont));
        }

        // Select a new panel
        if (panel != selectedPanel) {
            selectedPanel = panel;

            // Get the title from the current border
            String title = ((TitledBorder)panel.getBorder()).getTitle();

            // Apply the selected border with consistent styling
            panel.setBorder(BorderStyler.createSelectedBorder(title, borderColor, borderFont));
            buttonElimina.setEnabled(true);
        } else {
            // Clicking the same panel deselects it
            selectedPanel = null;
            buttonElimina.setEnabled(false);
        }

        repaint();
    }


    public JPanel getSelectedPanel() {
        return selectedPanel;
    }


    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY //GEN-BEGIN:initComponents @formatter:off
        // Generated using JFormDesigner Evaluation license - Marco
        panelOpzioni = new JPanel();
        labelNomeAzzienda = new JLabel();
        labelNotifiche = new JLabel();
        buttonCalendario = new JButton();
        menuBarOpzioni = new JMenuBar();
        menuOpzioni = new JMenu();
        logInButton = new JButton();
        ricercaButton = new JButton();
        backButtonHome = new JButton();
        panelListaImmobileScroll = new JScrollPane();
        panelListaImmobile = new JPanel();
        panelMap = new JPanel();
        panelBottomBar = new JPanel();
        buttonAggiungImmobile = new JButton();
        buttonAggiungiOfferta = new JButton();
        buttonElimina = new JButton();

        //======== this ========
        setBorder(new BevelBorder(BevelBorder.LOWERED));
        setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(0
        ,0,0,0), "JF\u006frmDes\u0069gner \u0045valua\u0074ion",javax.swing.border.TitledBorder.CENTER,javax.swing.border.TitledBorder.BOTTOM
        ,new java.awt.Font("D\u0069alog",java.awt.Font.BOLD,12),java.awt.Color.red),
         getBorder())); addPropertyChangeListener(new java.beans.PropertyChangeListener(){@Override public void propertyChange(java.beans.PropertyChangeEvent e
        ){if("\u0062order".equals(e.getPropertyName()))throw new RuntimeException();}});
        setLayout(new MigLayout(
            "fill,insets 0,hidemode 3,align center center",
            // columns
            "[fill]0" +
            "[139,grow,fill]0" +
            "[117,fill]0" +
            "[96,fill]0" +
            "[96,fill]0" +
            "[93,fill]0" +
            "[fill]0" +
            "[112,fill]0" +
            "[0,fill]0" +
            "[71,fill]0" +
            "[0,center]0" +
            "[24,grow,fill]0" +
            "[9,fill]0" +
            "[14,grow,fill]",
            // rows
            "[48,grow,fill]" +
            "[48,grow,fill]" +
            "[48,grow,center]" +
            "[grow,center]" +
            "[grow,fill]" +
            "[grow,fill]" +
            "[grow,fill]" +
            "[grow,fill]"));

        //======== panelOpzioni ========
        {
            panelOpzioni.setBackground(UIManager.getColor("Actions.Green"));
            panelOpzioni.setBorder(new BevelBorder(BevelBorder.RAISED, null, Color.black, null, null));
            panelOpzioni.setLayout(new MigLayout(
                "fill,insets 0,hidemode 3,gap 5 5",
                // columns
                "[0,fill]0" +
                "[fill]" +
                "[fill]0" +
                "[138,fill]" +
                "[fill]",
                // rows
                "[67,grow,fill]0"));

            //---- labelNomeAzzienda ----
            labelNomeAzzienda.setText("Nome");
            labelNomeAzzienda.setFont(new Font("Segoe UI", Font.PLAIN, 20));
            panelOpzioni.add(labelNomeAzzienda, "cell 0 0,align left center,grow 0 0");

            //---- labelNotifiche ----
            labelNotifiche.setText("Notifiche");
            labelNotifiche.setIcon(UIManager.getIcon("FileView.directoryIcon"));
            labelNotifiche.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            panelOpzioni.add(labelNotifiche, "cell 1 0,align center center,grow 0 0");

            //---- buttonCalendario ----
            buttonCalendario.setIcon(UIManager.getIcon("FileView.fileIcon"));
            buttonCalendario.setHorizontalTextPosition(SwingConstants.CENTER);
            buttonCalendario.setVerticalTextPosition(SwingConstants.BOTTOM);
            buttonCalendario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            buttonCalendario.setBackground(new Color(0x00ffffff, true));
            buttonCalendario.setBorder(null);
            panelOpzioni.add(buttonCalendario, "cell 2 0,grow");

            //======== menuBarOpzioni ========
            {
                menuBarOpzioni.setBorderPainted(false);
                menuBarOpzioni.setBackground(new Color(0x4e5052));
                menuBarOpzioni.setFont(new Font("Segoe UI", Font.PLAIN, 16));

                //======== menuOpzioni ========
                {
                    menuOpzioni.setText("Opzioni");
                    menuOpzioni.setIcon(UIManager.getIcon("Table.descendingSortIcon"));
                    menuOpzioni.setFont(new Font("Segoe UI", Font.PLAIN, 22));

                    //---- logInButton ----
                    logInButton.setText("Log In");
                    logInButton.setFont(new Font("Segoe UI", Font.PLAIN, 20));
                    menuOpzioni.add(logInButton);

                    //---- ricercaButton ----
                    ricercaButton.setText("Ricerca");
                    ricercaButton.setIcon(UIManager.getIcon("PasswordField.revealIcon"));
                    ricercaButton.setFont(new Font("Segoe UI", Font.PLAIN, 20));
                    menuOpzioni.add(ricercaButton);

                    //---- backButtonHome ----
                    backButtonHome.setText("Home");
                    backButtonHome.setIcon(UIManager.getIcon("FileChooser.homeFolderIcon"));
                    backButtonHome.setFont(new Font("Segoe UI", Font.PLAIN, 20));
                    menuOpzioni.add(backButtonHome);
                }
                menuBarOpzioni.add(menuOpzioni);
            }
            panelOpzioni.add(menuBarOpzioni, "cell 4 0,alignx trailing,grow 0 100");
        }
        add(panelOpzioni, "north");

        //======== panelListaImmobileScroll ========
        {
            panelListaImmobileScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            panelListaImmobileScroll.setViewportBorder(null);

            //======== panelListaImmobile ========
            {
                panelListaImmobile.setBorder(new LineBorder(UIManager.getColor("Actions.Green"), 2, true));
                panelListaImmobile.setLayout(new GridLayout(2, 0));
            }
            panelListaImmobileScroll.setViewportView(panelListaImmobile);
        }
        add(panelListaImmobileScroll, "cell 0 0 4 7,grow");

        //======== panelMap ========
        {
            panelMap.setBorder(new LineBorder(UIManager.getColor("Actions.Green"), 3, true));
            panelMap.setLayout(new BorderLayout());
        }
        add(panelMap, "cell 4 0 10 5,grow");

        //======== panelBottomBar ========
        {
            panelBottomBar.setBorder(new EtchedBorder());
            panelBottomBar.setBackground(UIManager.getColor("Actions.Green"));
            panelBottomBar.setLayout(new MigLayout(
                "insets 0,hidemode 3,gap 5 5",
                // columns
                "[88,grow 1,fill]" +
                "[grow 1,fill]" +
                "[grow 1,fill]" +
                "[grow 1,fill]",
                // rows
                "[33,fill]" +
                "[]"));

            //---- buttonAggiungImmobile ----
            buttonAggiungImmobile.setIcon(UIManager.getIcon("FileChooser.homeFolderIcon"));
            buttonAggiungImmobile.setBackground(UIManager.getColor("Actions.Green"));
            panelBottomBar.add(buttonAggiungImmobile, "cell 0 0,aligny center,grow 100 0");

            //---- buttonAggiungiOfferta ----
            buttonAggiungiOfferta.setText("Aggiungi Offerta");
            buttonAggiungiOfferta.setBorderPainted(false);
            buttonAggiungiOfferta.setBackground(UIManager.getColor("Actions.Green"));
            panelBottomBar.add(buttonAggiungiOfferta, "cell 2 0,aligny center,growy 0");

            //---- buttonElimina ----
            buttonElimina.setText("Elimina");
            buttonElimina.setBorder(new EmptyBorder(5, 5, 5, 5));
            buttonElimina.setBackground(UIManager.getColor("Actions.Green"));
            panelBottomBar.add(buttonElimina, "cell 3 0,aligny center,grow 100 0");
        }
        add(panelBottomBar, "south");
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    // Generated using JFormDesigner Evaluation license - Marco
    private JPanel panelOpzioni;
    private JLabel labelNomeAzzienda;
    private JLabel labelNotifiche;
    private JButton buttonCalendario;
    private JMenuBar menuBarOpzioni;
    private JMenu menuOpzioni;
    private JButton logInButton;
    private JButton ricercaButton;
    private JButton backButtonHome;
    private JScrollPane panelListaImmobileScroll;
    private JPanel panelListaImmobile;
    private JPanel panelMap;
    private JPanel panelBottomBar;
    private JButton buttonAggiungImmobile;
    private JButton buttonAggiungiOfferta;
    private JButton buttonElimina;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
