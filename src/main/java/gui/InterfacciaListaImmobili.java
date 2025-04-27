/*
 * Created by JFormDesigner on Tue Apr 22 11:59:05 CEST 2025
 */

package gui;

import gui.costumComponents.MapLoader;
import net.miginfocom.swing.MigLayout;
import org.jxmapviewer.viewer.GeoPosition;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.List;

/**
 * @author marco
 */
public class InterfacciaListaImmobili extends JPanel {
    MapLoader mapLoader = new MapLoader();
    private static final String fontName = "Segue UI";
    /*
    Dati di prova Eliminare
     */
    GeoPosition napoli = new GeoPosition(45.464170, 11.885890);
    GeoPosition roma = new GeoPosition(41.893333, 12.482778);
    GeoPosition milano = new GeoPosition(45.468555, 9.182440);

    private JPanel selectedPanel = null;
    private final Color defaultColor = new Color(192, 192, 192);
    private final Border compoundBorder = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(defaultColor, 2, true),
            new TitledBorder("Casa")
    );
    //--Border per gli immobili selected
    private final Color borderColor = new Color(24, 122, 26);
    private final Color selectionColor = new Color(24, 122, 26);
    private final Font borderFont = new Font(fontName, Font.BOLD, 18);

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
//Rimuove i popup quando la pagina viene caricata
    @Override
    public void addNotify() {
        super.addNotify();
        // Assicurati che tutti i menu popup siano nascosti quando il pannello viene aggiunto
        MenuSelectionManager.defaultManager().clearSelectedPath();
    }

    public InterfacciaListaImmobili(JFrame frame,  List<JCheckBox> checkBoxesList,GeoPosition geoPosition) {
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
        
        buttonCerca.addActionListener(e -> {
            panelMap.removeAll();
            mapLoader.createMapPanel(roma,checkBoxesList,panelMap);
            panelMap.revalidate();
            panelMap.repaint();
            
        });
        
        mapLoader.createMapPanel(geoPosition,checkBoxesList,panelMap);
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
        // Launch map loading in the background
    }

    private JPanel createImmobileSingoloPanel(String casaName) {
        JPanel panel = new JPanel();
        panel.setBorder(BorderStyler.createStyledBorder(casaName, borderColor, 1, borderFont));
        panel.setPreferredSize(new Dimension(780, 170));
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
        JLabel labelIndirizzo = new JLabel("Indirizzo");//getAddressFromCoordinates(milano) mega lag
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

        // MouseAdaptor doppio click
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    // Single click - select/deselect the panel
                    selectPanel(panel);
                } else if (e.getClickCount() == 2) {
                    // Double-click - navigate to the details page
                    navigateToDetailsPage(panel);
                }
            }
        });


        return panel;
    }
    private void navigateToDetailsPage(JPanel panel) {
        // Get the property name/ID from the panel border
        String title;
        Border currentBorder = panel.getBorder();

        if (currentBorder instanceof CompoundBorder) {
            // It's currently selected - extract title from an inner border
            TitledBorder innerBorder = (TitledBorder)((CompoundBorder)currentBorder).getInsideBorder();
            title = innerBorder.getTitle();
        } else {
            // It's a simple TitledBorder
            title = ((TitledBorder)currentBorder).getTitle();
        }

        // Create and navigate to the details page
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (frame != null) {
            // Example of navigation - replace with your actual details page class
            // You could pass the title or other identifying information to the details page
            frame.setContentPane(new PaginaImmobileDettagliata(frame));
            frame.revalidate();
            frame.repaint();
        }
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
            panel.setBorder(BorderStyler.createSelectedBorder(title, selectionColor, borderFont));
        } else {
            // Clicking the same panel deselects it
            selectedPanel = null;
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
        panelTopBar = new JPanel();
        buttonCerca = new JButton();
        searchField = new JTextField();
        menuBarOpzioni = new JMenuBar();
        menuOpzioni = new JMenu();
        logInButton = new JButton();
        ricercaButton = new JButton();
        backButtonHome = new JButton();
        panelMap = new JPanel();
        panelListaImmobileScroll = new JScrollPane();
        panelListaImmobile = new JPanel();
        panelBottomBar = new JPanel();
        buttonAggiungImmobile = new JButton();
        buttonAggiungiOfferta = new JButton();

        //======== this ========
        setBorder(new BevelBorder(BevelBorder.LOWERED));
        setBorder ( new javax . swing. border .CompoundBorder ( new javax . swing. border .TitledBorder ( new javax .
        swing. border .EmptyBorder ( 0, 0 ,0 , 0) ,  "JF\u006frmDes\u0069gner \u0045valua\u0074ion" , javax. swing .border
        . TitledBorder. CENTER ,javax . swing. border .TitledBorder . BOTTOM, new java. awt .Font ( "D\u0069alog"
        , java .awt . Font. BOLD ,12 ) ,java . awt. Color .red ) , getBorder
        () ) );  addPropertyChangeListener( new java. beans .PropertyChangeListener ( ){ @Override public void propertyChange (java
        . beans. PropertyChangeEvent e) { if( "\u0062order" .equals ( e. getPropertyName () ) )throw new RuntimeException
        ( ) ;} } );
        setLayout(new MigLayout(
            "fill,insets 0,hidemode 3,align center center",
            // columns
            "[fill]0" +
            "[139,fill]0" +
            "[117,fill]0" +
            "[fill]0" +
            "[96,grow,fill]0" +
            "[96,fill]0" +
            "[96,grow,fill]0" +
            "[93,fill]0" +
            "[fill]0" +
            "[112,fill]0" +
            "[0,fill]0" +
            "[130,fill]0" +
            "[0,center]0",
            // rows
            "[48,grow,fill]" +
            "[48,grow,shrink 0,fill]0" +
            "[grow,center]0" +
            "[45,grow,center]" +
            "[grow,fill]" +
            "[grow,fill]" +
            "[grow,fill]" +
            "[grow,fill]"));

        //======== panelTopBar ========
        {
            panelTopBar.setBackground(UIManager.getColor("Actions.Green"));
            panelTopBar.setBorder(new LineBorder(Color.black, 2));
            panelTopBar.setLayout(new MigLayout(
                "fill,hidemode 3,aligny center",
                // columns
                "[662,grow]0" +
                "[fill]0" +
                "[255,grow,center]" +
                "[fill]",
                // rows
                "[30,grow,fill]para" +
                "[75,grow,top]0" +
                "[45,grow,shrink 0,fill]0"));

            //---- buttonCerca ----
            buttonCerca.setSelectedIcon(UIManager.getIcon("FileChooser.homeFolderIcon"));
            buttonCerca.setMinimumSize(new Dimension(50, 37));
            buttonCerca.setMaximumSize(new Dimension(50, 38));
            buttonCerca.setIcon(UIManager.getIcon("FileChooser.homeFolderIcon"));
            panelTopBar.add(buttonCerca, "cell 1 1,align right center,grow 0 0");

            //---- searchField ----
            searchField.setBackground(Color.white);
            searchField.setFont(new Font("Segoe UI", Font.PLAIN, 24));
            panelTopBar.add(searchField, "cell 0 1 2 1,aligny center,grow 100 0");

            //======== menuBarOpzioni ========
            {
                menuBarOpzioni.setBorderPainted(false);
                menuBarOpzioni.setBackground(new Color(0x4e5052));
                menuBarOpzioni.setFont(new Font("Segoe UI", Font.PLAIN, 20));
                menuBarOpzioni.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        menuBar1MouseClicked(e);
                    }
                });

                //======== menuOpzioni ========
                {
                    menuOpzioni.setText("Opzioni");
                    menuOpzioni.setIcon(UIManager.getIcon("Table.descendingSortIcon"));
                    menuOpzioni.setFont(new Font("Segoe UI", Font.PLAIN, 20));

                    //---- logInButton ----
                    logInButton.setText("Log In");
                    logInButton.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                    menuOpzioni.add(logInButton);

                    //---- ricercaButton ----
                    ricercaButton.setText("Ricerca");
                    ricercaButton.setIcon(UIManager.getIcon("PasswordField.revealIcon"));
                    ricercaButton.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                    menuOpzioni.add(ricercaButton);

                    //---- backButtonHome ----
                    backButtonHome.setText("Home");
                    backButtonHome.setIcon(UIManager.getIcon("FileChooser.homeFolderIcon"));
                    backButtonHome.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                    menuOpzioni.add(backButtonHome);
                }
                menuBarOpzioni.add(menuOpzioni);
            }
            panelTopBar.add(menuBarOpzioni, "cell 2 1 2 1,align right top,grow 0 0");
        }
        add(panelTopBar, "cell 0 0 13 2,grow");

        //======== panelMap ========
        {
            panelMap.setBorder(new LineBorder(UIManager.getColor("Actions.Green"), 2, true));
            panelMap.setLayout(new BorderLayout());
        }
        add(panelMap, "cell 5 3 8 5,grow");

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
        add(panelListaImmobileScroll, "cell 1 3 4 5,grow");

        //======== panelBottomBar ========
        {
            panelBottomBar.setBorder(new LineBorder(Color.black, 2, true));
            panelBottomBar.setBackground(UIManager.getColor("Actions.Green"));
            panelBottomBar.setLayout(new MigLayout(
                "insets 0,hidemode 3,gap 5 5",
                // columns
                "[88,grow 1,fill]" +
                "[grow 1,fill]" +
                "[grow 1,fill]" +
                "[grow 1,fill]",
                // rows
                "[shrink 0]" +
                "[33,fill]"));

            //---- buttonAggiungImmobile ----
            buttonAggiungImmobile.setIcon(UIManager.getIcon("FileChooser.homeFolderIcon"));
            buttonAggiungImmobile.setBackground(UIManager.getColor("Actions.Green"));
            panelBottomBar.add(buttonAggiungImmobile, "cell 0 0 1 2,aligny center,grow 100 0");

            //---- buttonAggiungiOfferta ----
            buttonAggiungiOfferta.setText("Aggiungi Offerta");
            buttonAggiungiOfferta.setBorderPainted(false);
            buttonAggiungiOfferta.setBackground(UIManager.getColor("Actions.Green"));
            panelBottomBar.add(buttonAggiungiOfferta, "cell 3 0 1 2,aligny center,growy 0");
        }
        add(panelBottomBar, "south");
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    // Generated using JFormDesigner Evaluation license - Marco GuIdo Scotto di Uccio
    private JPanel panelTopBar;
    private JButton buttonCerca;
    private JTextField searchField;
    private JMenuBar menuBarOpzioni;
    private JMenu menuOpzioni;
    private JButton logInButton;
    private JButton ricercaButton;
    private JButton backButtonHome;
    private JPanel panelMap;
    private JScrollPane panelListaImmobileScroll;
    private JPanel panelListaImmobile;
    private JPanel panelBottomBar;
    private JButton buttonAggiungImmobile;
    private JButton buttonAggiungiOfferta;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
