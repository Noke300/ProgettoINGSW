/*
 * Created by JFormDesigner on Tue Apr 22 11:59:05 CEST 2025
 */

package gui;

import controller.Controller;
import gui.costum_components.MapLoader;
import net.miginfocom.swing.MigLayout;
import org.jxmapviewer.viewer.GeoPosition;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.Arrays;
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

    public InterfacciaListaImmobili(JFrame frame, List<JCheckBox> checkBoxesList, GeoPosition geoPosition, Controller control,String token) {
        initComponents();

        internalFrameFiltri.setVisible(false);

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

            frame.setContentPane(new LogIn(frame, control, token));
            frame.revalidate();
            frame.repaint();
        });

        ricercaButton.addActionListener(e-> {

            frame.setContentPane(new Ricerca().createRicercaPanel(frame,control,token));
            frame.revalidate();
            frame.repaint();
        });
        
        buttonCerca.addActionListener(e -> {
            panelMap.removeAll();
            mapLoader.createMapPanel(roma,checkBoxesList,panelMap);
            panelMap.revalidate();
            panelMap.repaint();
            
        });
        //--- 7. Filtri  ---
        buttonFiltri.addActionListener(e ->
                // Alterna la visibilità del pannello interno dei filtri
                internalFrameFiltri.setVisible(!internalFrameFiltri.isVisible()));



        buttonConfermaFiltri.addActionListener(e -> {
            internalFrameFiltri.setVisible(false);

        });
        
        buttonCancellaFiltri.addActionListener(e -> {
            comboBoxTipVendita.setSelectedIndex(0);
            for (JSpinner jSpinner : Arrays.asList(spinnerBagni, spinnerStanze)) {
                jSpinner.setValue("Indifferente");
            }
            comboBoxClasseE.setSelectedIndex(0);
            spinnerPiani.setValue("Indifferente");
            hospitalsCheckBox.setSelected(false);
            parkCheckBox.setSelected(false);
            schoolsCheckBox.setSelected(false);
        });
        
        mapLoader.createMapPanel(geoPosition,checkBoxesList,panelMap);
        // Create and add the first panel

        // In the initComponents method, replace the GridLayout with MigLayout
        panelListaImmobile.setLayout(new MigLayout("wrap 1, align center")); // One component per row

        JPanel firstPanel = createImmobileSingoloPanel("Casa 1",control,token);
        panelListaImmobile.add(firstPanel,"align center");

        // Create and add the second panel
        JPanel secondPanel = createImmobileSingoloPanel("Casa 2",control,token);
        panelListaImmobile.add(secondPanel,"align center");

        JPanel thirdPanel = createImmobileSingoloPanel("Casa 3",control,token);
        thirdPanel.setBorder(new TitledBorder("Appartamento"));
        panelListaImmobile.add(thirdPanel,"align center");

        JPanel forthPanel = createImmobileSingoloPanel("Casa 4",control,token);
        forthPanel.setBorder(new TitledBorder("Appartamento"));
        panelListaImmobile.add(forthPanel,"align center");

        JPanel fifthPanel = createImmobileSingoloPanel("Casa 5",control,token);
        panelListaImmobile.add(fifthPanel,"align center");
        // Launch map loading in the background
    }

    private JPanel createImmobileSingoloPanel(String casaName,Controller control,String token) {
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
                    navigateToDetailsPage(panel,control,token);
                }
            }
        });


        return panel;
    }
    private void navigateToDetailsPage(JPanel panel,Controller control,String token) {
        // Get the property name/ID from the panel border
        String title;
        Border currentBorder = panel.getBorder();

        if (currentBorder instanceof CompoundBorder) {
            // Extract title from an inner border if it's a CompoundBorder
            TitledBorder innerBorder = (TitledBorder) ((CompoundBorder) currentBorder).getInsideBorder();
            title = innerBorder.getTitle();
        } else if (currentBorder instanceof TitledBorder) {
            // Extract title directly for a simple TitledBorder
            title = ((TitledBorder) currentBorder).getTitle();
        } else {
            // If no title is found, assign a default value
            title = "Unknown";
        }
        // Create and navigate to the details page
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (frame != null) {
            // Navigate to the details page, passing the `title` to the new JPanel or JFrame
            PaginaImmobileDettagliata detailsPage = new PaginaImmobileDettagliata(frame,control,token);
            frame.setContentPane(detailsPage);
            frame.revalidate();
            frame.repaint();
        } else {
            System.err.println("Parent frame not found for navigation.");
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
        // Generated using JFormDesigner Evaluation license - Simone Sommella
        panelTopBar = new JPanel();
        menuBarOpzioni = new JMenuBar();
        menuOpzioni = new JMenu();
        logInButton = new JButton();
        ricercaButton = new JButton();
        backButtonHome = new JButton();
        buttonCerca = new JButton();
        searchField = new JTextField();
        listaTipi = new JComboBox<>();
        menuBarContenitorePrezziBox = new JMenuBar();
        menuPrezzi = new JMenu();
        comboBoxPrezzoMax = new JComboBox<>();
        comboBoxPrezzoMinimo = new JComboBox<>();
        comboBoxSuperfice = new JComboBox<>();
        buttonFiltri = new JButton();
        panelMap = new JPanel();
        panelListaImmobileScroll = new JScrollPane();
        panelListaImmobile = new JPanel();
        panelBottomBar = new JPanel();
        buttonAggiungImmobile = new JButton();
        buttonAggiungiOfferta = new JButton();
        internalFrameFiltri = new JPanel();
        labelTipoVendita = new JLabel();
        comboBoxTipVendita = new JComboBox<>();
        labelNumeroBagni = new JLabel();
        spinnerBagni = new JSpinner();
        labelStanze = new JLabel();
        spinnerStanze = new JSpinner();
        labelClasseE = new JLabel();
        comboBoxClasseE = new JComboBox<>();
        labelPiani = new JLabel();
        spinnerPiani = new JSpinner();
        schoolsCheckBox = new JCheckBox();
        parkCheckBox = new JCheckBox();
        hospitalsCheckBox = new JCheckBox();
        label1 = new JLabel();
        toggleButtonAscensore = new JToggleButton();
        toggleButtonBalcone = new JToggleButton();
        toggleButtonCantina = new JToggleButton();
        toggleButtonClimatizzazione = new JToggleButton();
        toggleButtonPortineria = new JToggleButton();
        separator1 = new JSeparator();
        buttonCancellaFiltri = new JButton();
        buttonConfermaFiltri = new JButton();

        //======== this ========
        setBorder(new BevelBorder(BevelBorder.LOWERED));
        setBorder ( new javax . swing. border .CompoundBorder ( new javax . swing. border .TitledBorder ( new javax . swing. border .EmptyBorder (
        0, 0 ,0 , 0) ,  "JF\u006frmD\u0065sig\u006eer \u0045val\u0075ati\u006fn" , javax. swing .border . TitledBorder. CENTER ,javax . swing. border .TitledBorder
        . BOTTOM, new java. awt .Font ( "Dia\u006cog", java .awt . Font. BOLD ,12 ) ,java . awt. Color .
        red ) , getBorder () ) );  addPropertyChangeListener( new java. beans .PropertyChangeListener ( ){ @Override public void propertyChange (java .
        beans. PropertyChangeEvent e) { if( "\u0062ord\u0065r" .equals ( e. getPropertyName () ) )throw new RuntimeException( ) ;} } );
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
                "[662,grow]0" +
                "[662,grow]0" +
                "[fill]0" +
                "[255,grow,center]" +
                "[fill]",
                // rows
                "[22,grow,fill]0" +
                "[75,grow,top]0" +
                "[45,grow,shrink 0,fill]para"));

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
            panelTopBar.add(menuBarOpzioni, "cell 4 0 2 1,align right top,grow 0 0");

            //---- buttonCerca ----
            buttonCerca.setSelectedIcon(UIManager.getIcon("FileChooser.homeFolderIcon"));
            buttonCerca.setMinimumSize(new Dimension(50, 36));
            buttonCerca.setMaximumSize(new Dimension(50, 36));
            buttonCerca.setIcon(UIManager.getIcon("FileChooser.homeFolderIcon"));
            buttonCerca.setBorder(null);
            buttonCerca.setBorderPainted(false);
            panelTopBar.add(buttonCerca, "cell 3 1,align right center,grow 0 0");

            //---- searchField ----
            searchField.setBackground(Color.white);
            searchField.setFont(new Font("Segoe UI", Font.PLAIN, 24));
            panelTopBar.add(searchField, "cell 0 1 4 1,aligny center,grow 100 0");

            //---- listaTipi ----
            listaTipi.setModel(new DefaultComboBoxModel<>(new String[] {
                "Casa",
                "Appartamento"
            }));
            listaTipi.setSelectedIndex(0);
            listaTipi.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            panelTopBar.add(listaTipi, "cell 0 2,align center center,grow 0 0");

            //======== menuBarContenitorePrezziBox ========
            {

                //======== menuPrezzi ========
                {
                    menuPrezzi.setIcon(UIManager.getIcon("Table.descendingSortIcon"));
                    menuPrezzi.setText("Prezzi");
                    menuPrezzi.setHorizontalAlignment(SwingConstants.RIGHT);
                    menuPrezzi.setHorizontalTextPosition(SwingConstants.RIGHT);
                    menuPrezzi.setBorder(UIManager.getBorder("ComboBox.border"));
                    menuPrezzi.setForeground(Color.white);
                    menuPrezzi.setFont(new Font("Segoe UI", Font.PLAIN, 18));

                    //---- comboBoxPrezzoMax ----
                    comboBoxPrezzoMax.setModel(new DefaultComboBoxModel<>(new String[] {
                        "Prezzo Massimo",
                        "Poco",
                        "Medio ",
                        "Simone"
                    }));
                    comboBoxPrezzoMax.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                    menuPrezzi.add(comboBoxPrezzoMax);

                    //---- comboBoxPrezzoMinimo ----
                    comboBoxPrezzoMinimo.setModel(new DefaultComboBoxModel<>(new String[] {
                        "Prezzo Minimo",
                        "Tanto ",
                        "Poco"
                    }));
                    comboBoxPrezzoMinimo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                    menuPrezzi.add(comboBoxPrezzoMinimo);
                }
                menuBarContenitorePrezziBox.add(menuPrezzi);
            }
            panelTopBar.add(menuBarContenitorePrezziBox, "cell 1 2,align center center,grow 0 0");

            //---- comboBoxSuperfice ----
            comboBoxSuperfice.setModel(new DefaultComboBoxModel<>(new String[] {
                "Superfice"
            }));
            comboBoxSuperfice.setForeground(Color.black);
            comboBoxSuperfice.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            panelTopBar.add(comboBoxSuperfice, "cell 2 2,align center center,grow 0 0");

            //---- buttonFiltri ----
            buttonFiltri.setText("Filtri");
            buttonFiltri.setIcon(UIManager.getIcon("FileChooser.detailsViewIcon"));
            buttonFiltri.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            panelTopBar.add(buttonFiltri, "cell 3 2,align center center,grow 0 0");
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
                "[48,fill]"));

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

        //======== internalFrameFiltri ========
        {
            internalFrameFiltri.setFont(new Font("Inter", Font.PLAIN, 22));
            internalFrameFiltri.setBorder(null);
            internalFrameFiltri.setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder(new javax.swing.border.
            EmptyBorder(0,0,0,0), "JF\u006frmDes\u0069gner \u0045valua\u0074ion",javax.swing.border.TitledBorder.CENTER,javax.swing
            .border.TitledBorder.BOTTOM,new java.awt.Font("D\u0069alog",java.awt.Font.BOLD,12),
            java.awt.Color.red),internalFrameFiltri. getBorder()));internalFrameFiltri. addPropertyChangeListener(new java.beans.PropertyChangeListener()
            {@Override public void propertyChange(java.beans.PropertyChangeEvent e){if("\u0062order".equals(e.getPropertyName()))
            throw new RuntimeException();}});
            internalFrameFiltri.setLayout(new MigLayout(
                "fill,hidemode 3,align center center",
                // columns
                "[94,fill]rel" +
                "[94,fill]rel" +
                "[123,fill]",
                // rows
                "[22]" +
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]0" +
                "[]para" +
                "[]para" +
                "[]para" +
                "[]para" +
                "[]0" +
                "[27,center]"));

            //---- labelTipoVendita ----
            labelTipoVendita.setText("Tipo Di Vendita");
            labelTipoVendita.setIcon(UIManager.getIcon("FileView.fileIcon"));
            labelTipoVendita.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            internalFrameFiltri.add(labelTipoVendita, "cell 0 0");

            //---- comboBoxTipVendita ----
            comboBoxTipVendita.setModel(new DefaultComboBoxModel<>(new String[] {
                "Qualsiasi"
            }));
            comboBoxTipVendita.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            internalFrameFiltri.add(comboBoxTipVendita, "cell 2 0");

            //---- labelNumeroBagni ----
            labelNumeroBagni.setText("Numero Bagni");
            labelNumeroBagni.setIcon(UIManager.getIcon("FileView.fileIcon"));
            labelNumeroBagni.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            internalFrameFiltri.add(labelNumeroBagni, "cell 0 1");

            //---- spinnerBagni ----
            spinnerBagni.setModel(new SpinnerListModel(new String[] {"Indifferente", "0", "1", "2", "3", "4", "5", "6+"}));
            spinnerBagni.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            internalFrameFiltri.add(spinnerBagni, "cell 2 1");

            //---- labelStanze ----
            labelStanze.setText("Numero Stanze");
            labelStanze.setIcon(UIManager.getIcon("FileView.fileIcon"));
            labelStanze.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            internalFrameFiltri.add(labelStanze, "cell 0 2");

            //---- spinnerStanze ----
            spinnerStanze.setModel(new SpinnerListModel(new String[] {"Indifferente", "1", "2", "3", "4", "5", "6+"}));
            spinnerStanze.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            internalFrameFiltri.add(spinnerStanze, "cell 2 2");

            //---- labelClasseE ----
            labelClasseE.setText("Classe Energetica");
            labelClasseE.setIcon(UIManager.getIcon("FileView.fileIcon"));
            labelClasseE.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            internalFrameFiltri.add(labelClasseE, "cell 0 3");

            //---- comboBoxClasseE ----
            comboBoxClasseE.setModel(new DefaultComboBoxModel<>(new String[] {
                "Qualsiasi"
            }));
            comboBoxClasseE.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            internalFrameFiltri.add(comboBoxClasseE, "cell 2 3");

            //---- labelPiani ----
            labelPiani.setText("Piano/i");
            labelPiani.setIcon(UIManager.getIcon("FileView.fileIcon"));
            labelPiani.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            internalFrameFiltri.add(labelPiani, "cell 0 4 2 1");

            //---- spinnerPiani ----
            spinnerPiani.setModel(new SpinnerListModel(new String[] {"Indifferente", "Terra", "1", "2", "3", "4", "5+"}));
            spinnerPiani.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            internalFrameFiltri.add(spinnerPiani, "cell 2 4");

            //---- schoolsCheckBox ----
            schoolsCheckBox.setHorizontalAlignment(SwingConstants.TRAILING);
            schoolsCheckBox.setText("Scuole");
            schoolsCheckBox.setVerticalAlignment(SwingConstants.BOTTOM);
            internalFrameFiltri.add(schoolsCheckBox, "cell 0 5,align left bottom,grow 0 0");

            //---- parkCheckBox ----
            parkCheckBox.setHorizontalAlignment(SwingConstants.CENTER);
            parkCheckBox.setText("Parchi");
            internalFrameFiltri.add(parkCheckBox, "cell 1 5,align left bottom,grow 0 0");

            //---- hospitalsCheckBox ----
            hospitalsCheckBox.setHorizontalAlignment(SwingConstants.CENTER);
            hospitalsCheckBox.setText("Trasporti");
            hospitalsCheckBox.setToolTipText("");
            hospitalsCheckBox.setVerticalAlignment(SwingConstants.CENTER);
            internalFrameFiltri.add(hospitalsCheckBox, "cell 0 6,align left bottom,grow 0 0");

            //---- label1 ----
            label1.setText("Altre Caratteristiche");
            label1.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            label1.setIcon(UIManager.getIcon("FileChooser.listViewIcon"));
            internalFrameFiltri.add(label1, "cell 0 7 3 1,alignx center,growx 0");

            //---- toggleButtonAscensore ----
            toggleButtonAscensore.setText("Ascensore");
            toggleButtonAscensore.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            toggleButtonAscensore.setIcon(UIManager.getIcon("FileChooser.listViewIcon"));
            internalFrameFiltri.add(toggleButtonAscensore, "cell 0 8");

            //---- toggleButtonBalcone ----
            toggleButtonBalcone.setText("Balcone ");
            toggleButtonBalcone.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            toggleButtonBalcone.setIcon(UIManager.getIcon("FileChooser.listViewIcon"));
            internalFrameFiltri.add(toggleButtonBalcone, "cell 1 8");

            //---- toggleButtonCantina ----
            toggleButtonCantina.setText("Cantina");
            toggleButtonCantina.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            toggleButtonCantina.setIcon(UIManager.getIcon("FileChooser.listViewIcon"));
            internalFrameFiltri.add(toggleButtonCantina, "cell 2 8");

            //---- toggleButtonClimatizzazione ----
            toggleButtonClimatizzazione.setText("Climatizzazione");
            toggleButtonClimatizzazione.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            toggleButtonClimatizzazione.setIcon(UIManager.getIcon("FileChooser.listViewIcon"));
            internalFrameFiltri.add(toggleButtonClimatizzazione, "cell 0 9");

            //---- toggleButtonPortineria ----
            toggleButtonPortineria.setText("Portineria");
            toggleButtonPortineria.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            toggleButtonPortineria.setIcon(UIManager.getIcon("FileChooser.listViewIcon"));
            internalFrameFiltri.add(toggleButtonPortineria, "cell 1 9");

            //---- separator1 ----
            separator1.setBackground(Color.black);
            internalFrameFiltri.add(separator1, "cell 0 10 3 1,aligny bottom,grow 100 0");

            //---- buttonCancellaFiltri ----
            buttonCancellaFiltri.setText("Cancella");
            buttonCancellaFiltri.setBackground(new Color(0x603c3e));
            internalFrameFiltri.add(buttonCancellaFiltri, "cell 1 11,alignx center,grow 0 100");

            //---- buttonConfermaFiltri ----
            buttonConfermaFiltri.setText("Conferma");
            buttonConfermaFiltri.setBackground(new Color(0x3b6b3d));
            internalFrameFiltri.add(buttonConfermaFiltri, "cell 2 11,alignx center,grow 0 100");
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    // Generated using JFormDesigner Evaluation license - Simone Sommella
    private JPanel panelTopBar;
    private JMenuBar menuBarOpzioni;
    private JMenu menuOpzioni;
    private JButton logInButton;
    private JButton ricercaButton;
    private JButton backButtonHome;
    private JButton buttonCerca;
    private JTextField searchField;
    private JComboBox<String> listaTipi;
    private JMenuBar menuBarContenitorePrezziBox;
    private JMenu menuPrezzi;
    private JComboBox<String> comboBoxPrezzoMax;
    private JComboBox<String> comboBoxPrezzoMinimo;
    private JComboBox<String> comboBoxSuperfice;
    private JButton buttonFiltri;
    private JPanel panelMap;
    private JScrollPane panelListaImmobileScroll;
    private JPanel panelListaImmobile;
    private JPanel panelBottomBar;
    private JButton buttonAggiungImmobile;
    private JButton buttonAggiungiOfferta;
    private JPanel internalFrameFiltri;
    private JLabel labelTipoVendita;
    private JComboBox<String> comboBoxTipVendita;
    private JLabel labelNumeroBagni;
    private JSpinner spinnerBagni;
    private JLabel labelStanze;
    private JSpinner spinnerStanze;
    private JLabel labelClasseE;
    private JComboBox<String> comboBoxClasseE;
    private JLabel labelPiani;
    private JSpinner spinnerPiani;
    private JCheckBox schoolsCheckBox;
    private JCheckBox parkCheckBox;
    private JCheckBox hospitalsCheckBox;
    private JLabel label1;
    private JToggleButton toggleButtonAscensore;
    private JToggleButton toggleButtonBalcone;
    private JToggleButton toggleButtonCantina;
    private JToggleButton toggleButtonClimatizzazione;
    private JToggleButton toggleButtonPortineria;
    private JSeparator separator1;
    private JButton buttonCancellaFiltri;
    private JButton buttonConfermaFiltri;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
