/*
 * Created by JFormDesigner on Tue Apr 22 11:59:05 CEST 2025
 */

package gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;

import gui.costumComponents.MapLoader;
import net.miginfocom.swing.*;
import org.jxmapviewer.viewer.GeoPosition;

/**
 * @author marco
 */
public class InterfacciaAgenteImmobiliare extends JPanel {
    MapLoader mapLoader = new MapLoader();
    GeoPosition napoli = new GeoPosition(45.464170, 11.885890);
    List<JCheckBox> checkBoxesList = new ArrayList<>();

    private JPanel selectedPanel = null;
    private final Color DEFAULT_COLOR = new Color(192, 192, 192);
    private final Border SELECTED_BORDER = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(DEFAULT_COLOR, 2, true),
            new TitledBorder("Casa")
    );
    //--Border per gli immobili selected
    private final Color BORDER_COLOR = new Color(24, 122, 26);
    private final Color SELECTION_COLOR = new Color(24, 122, 26);
    private final Font BORDER_FONT = new Font("Segoe UI", Font.BOLD, 18);

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

    public InterfacciaAgenteImmobiliare(JFrame frame) {
        initComponents();

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

            frame.setContentPane(new LogIn(frame));
            frame.revalidate();
            frame.repaint();
        });

        ricercaButton.addActionListener(e-> {

            frame.setContentPane(new Ricerca().createRicercaPanel(frame));
            frame.revalidate();
            frame.repaint();
        });
        
        mapLoader.createMapPanel(napoli,checkBoxesList,panelMap);
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
        
        
    }

    public static class BackgroundImagePane extends JPanel {
        private Image backgroundImage;

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

    private JPanel createImmobileSingoloPanel(String casaName) {
        JPanel panel = new JPanel();
        panel.setBorder(BorderStyler.createStyledBorder(casaName, BORDER_COLOR, 1, BORDER_FONT));
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
        // Deselect previous panel if exists
        if (selectedPanel != null) {
            // Get the title from the current border
            String title;
            Border currentBorder = selectedPanel.getBorder();

            if (currentBorder instanceof CompoundBorder) {
                // It's currently selected - extract title from inner border
                TitledBorder innerBorder = (TitledBorder)((CompoundBorder)currentBorder).getInsideBorder();
                title = innerBorder.getTitle();
            } else {
                // It's a simple TitledBorder
                title = ((TitledBorder)currentBorder).getTitle();
            }

            // Restore standard styled border with the same title
            selectedPanel.setBorder(BorderStyler.createStyledBorder(title, BORDER_COLOR, 1, BORDER_FONT));
        }

        // Select a new panel
        if (panel != selectedPanel) {
            selectedPanel = panel;

            // Get the title from the current border
            String title = ((TitledBorder)panel.getBorder()).getTitle();

            // Apply the selected border with consistent styling
            panel.setBorder(BorderStyler.createSelectedBorder(title, SELECTION_COLOR, BORDER_FONT));
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

    private void menuBar1MouseClicked(MouseEvent e) {
        // TODO add your code here
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Evaluation license - Marco GuIdo Scotto di Uccio
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
        setBorder (new javax. swing. border. CompoundBorder( new javax .swing .border .TitledBorder (new javax. swing. border.
        EmptyBorder( 0, 0, 0, 0) , "JFor\u006dDesi\u0067ner \u0045valu\u0061tion", javax. swing. border. TitledBorder. CENTER, javax. swing
        . border. TitledBorder. BOTTOM, new java .awt .Font ("Dia\u006cog" ,java .awt .Font .BOLD ,12 ),
        java. awt. Color. red) , getBorder( )) );  addPropertyChangeListener (new java. beans. PropertyChangeListener( )
        { @Override public void propertyChange (java .beans .PropertyChangeEvent e) {if ("bord\u0065r" .equals (e .getPropertyName () ))
        throw new RuntimeException( ); }} );
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
                "[66,fill]0" +
                "[34,grow,top]0"));

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
            buttonCalendario.setIcon(UIManager.getIcon("OptionPane.informationIcon"));
            panelOpzioni.add(buttonCalendario, "cell 2 0,align center center,grow 0 0");

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
            panelOpzioni.add(menuBarOpzioni, "cell 4 0 1 2,align center top,grow 0 0");
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
        add(panelListaImmobileScroll, "cell 1 0 3 7,grow");

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
                "[33,fill]"));

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
    // Generated using JFormDesigner Evaluation license - Marco GuIdo Scotto di Uccio
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
