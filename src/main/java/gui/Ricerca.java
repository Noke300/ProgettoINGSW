package gui;

import javax.swing.border.*;
import com.formdev.flatlaf.extras.*;
import controller.Controller;
import gui.costum_components.*;
import model.Immobile;
import net.miginfocom.swing.*;
import org.jxmapviewer.viewer.GeoPosition;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class Ricerca extends JPanel {

    private JPopupMenu popupMenu;
    private final String FilterString = "Indifferente" ;
    private static final IconClass iconSet = new IconClass();
    private String token;
    private final BottoniOpzioni bottoniOpzioni = new BottoniOpzioni();
    private final AutoCompleteAddress autoCompleteAddress = new AutoCompleteAddress();
    public Ricerca() {
        initComponents();
        initializePopupMenu(); // Inizializza il menu
        addPopupToSearchField();// Collega il menu al searchField
    }

    @Override
    public void addNotify() {
        super.addNotify();
        // Assicurati che tutti i menu popup siano nascosti quando il pannello viene aggiunto
        MenuSelectionManager.defaultManager().clearSelectedPath();
    }

    public JPanel createRicercaPanel(JFrame frame, Controller control, String token) {

        this.token = token;
        internalFrameFiltri.setVisible(false);

        setUpComponents();
        List<JCheckBox> checkBoxesList = new ArrayList<>();
        checkBoxesList.add(schoolsCheckBox);
        checkBoxesList.add(hospitalsCheckBox);
        checkBoxesList.add(parkCheckBox);

        // --- 4. Auto-completamento ---
        autoCompleteAddress.setupSearchAutocomplete(searchField);

        // --- 5. Azione del pulsante "Cerca" ---//--- 6. Menu Opzioni ---
        bottoniOpzioni.setUpOpzioniMenu(frame,control,token,menuBarOpzioni,menuOpzioni);

        setUpOpzioniMenu(frame,control,checkBoxesList);

        //--- 7. Filtri  ---
        buttonFiltri.addActionListener(e -> internalFrameFiltri.setVisible(!internalFrameFiltri.isVisible()));

        buttonConfermaFiltri.addActionListener(e ->internalFrameFiltri.setVisible(false));

        buttonCancellaFiltri.addActionListener(e -> {
            comboBoxTipVendita.setSelectedIndex(0);
            for (JSpinner jSpinner : Arrays.asList(spinnerBagni, spinnerStanze)) {
                jSpinner.setValue(FilterString);
            }
            comboBoxClasseE.setSelectedIndex(0);
            spinnerPiani.setValue(FilterString);
            hospitalsCheckBox.setSelected(false);
            parkCheckBox.setSelected(false);
            schoolsCheckBox.setSelected(false);

            toggleButtonBalcone.setSelected(false);
            toggleButtonBalcone.setBackground(Color.WHITE);
            toggleButtonCantina.setSelected(false);
            toggleButtonCantina.setBackground(Color.WHITE);
            toggleButtonAscensore.setSelected(false);
            toggleButtonAscensore.setBackground(Color.WHITE);
            toggleButtonClimatizzazione.setSelected(false);
            toggleButtonClimatizzazione.setBackground(Color.WHITE);
            toggleButtonPortineria.setSelected(false);
            toggleButtonPortineria.setBackground(Color.WHITE);

        });
        // Usage for each button
        configureToggleButton(toggleButtonCantina);
        configureToggleButton(toggleButtonBalcone);
        configureToggleButton(toggleButtonAscensore);
        configureToggleButton(toggleButtonPortineria);
        configureToggleButton(toggleButtonClimatizzazione);

        // --- 8. Prezzi ---
        menuPrezzi.getPopupMenu().addPopupMenuListener(new PopupMenuListener() {
            private boolean ignoreVisibilityFlag = false;

            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                if (!ignoreVisibilityFlag) {
                    ignoreVisibilityFlag = true;
                    menuPrezzi.setPopupMenuVisible(true);
                    ignoreVisibilityFlag = false;
                }
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                // Prevent the menu from being hidden when combo boxes are focused/open
                SwingUtilities.invokeLater(() -> {
                    if (comboBoxPrezzoMax.isPopupVisible() || comboBoxPrezzoMinimo.isPopupVisible()) {
                        ignoreVisibilityFlag = true;
                        menuPrezzi.setPopupMenuVisible(true);
                        ignoreVisibilityFlag = false;
                    }
                });
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
                // No action needed for canceled events
            }
        });

        // Add listeners to ensure the combo boxes keep the menu open
        comboBoxPrezzoMinimo.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                // Keep the parent menu open
                menuPrezzi.setPopupMenuVisible(true);
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                // Prevent a parent menu from closing with a small delay
                SwingUtilities.invokeLater(() -> menuPrezzi.setPopupMenuVisible(true));
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
                // No action needed here
            }
        });

        comboBoxPrezzoMax.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                // Keep the parent menu open
                menuPrezzi.setPopupMenuVisible(true);
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                // Prevent a parent menu from closing with a small delay
                SwingUtilities.invokeLater(() -> menuPrezzi.setPopupMenuVisible(true));
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
                // No action needed here
            }
        });
        //--9. button Mappa
        mapButton.addActionListener(e -> {
            final JPanel mapPanelInDialog = new JPanel(); // Deve essere final per essere usato nel lambda
            // Crea il JDialog
            final JDialog mapDialogInstance = new JDialog(frame, "Seleziona Indirizzo da Mappa", true);
            mapDialogInstance.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            mapDialogInstance.setSize(700, 550); // Imposta una dimensione per il dialogo della mappa
            mapDialogInstance.setContentPane(mapPanelInDialog);

            // Bottone di conferma e campo indirizzo (se vuoi mostrarlo nel dialogo)
            final JTextField selectedAddressField = new JTextField(30);
            selectedAddressField.setEditable(false); // L'utente non lo modifica direttamente
            JButton confirmButton = new JButton("Usa questo indirizzo");

            JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            bottomPanel.add(new JLabel("Indirizzo Selezionato:"));
            bottomPanel.add(selectedAddressField);
            bottomPanel.add(confirmButton);

            mapPanelInDialog.setLayout(new BorderLayout());
            mapPanelInDialog.add(bottomPanel, BorderLayout.SOUTH);


            confirmButton.addActionListener(h -> {
                if (!selectedAddressField.getText().isEmpty()) {
                    searchField.setText(selectedAddressField.getText()); // searchField della classe Ricerca
                    mapDialogInstance.setVisible(false);
                    mapDialogInstance.dispose();
                } else {
                    JOptionPane.showMessageDialog(mapDialogInstance,
                            "Per favore, clicca sulla mappa per selezionare un indirizzo.",
                            "Nessun indirizzo selezionato", JOptionPane.WARNING_MESSAGE);
                }
            });

            // Posizione iniziale (es. Centro Italia o ultima posizione nota)
            GeoPosition initialPos = new GeoPosition(41.902782, 12.496366); // Roma
            // Prova a prendere le coordinate dall'indirizzo attuale in searchField, se presente e valido
            String currentSearchText = searchField.getText();
            if (currentSearchText != null && !currentSearchText.trim().isEmpty() && !currentSearchText.equals("Cerca Qualcosa")) {
                GeoPosition searchGeo = MapLoader.getCoordinatesFromAddress(currentSearchText); // Usa il tuo vecchio MapLoader.getCoordinatesFromAddress se lo hai ancora
                if (searchGeo != null) {
                    initialPos = searchGeo;
                }
            }
            // Crea e aggiungi la mappa al mapPanelInDialog
            // Il pannello della mappa vero e proprio verrà messo al centro di mapPanelInDialog
            JPanel actualMapDisplayPanel = new JPanel(new BorderLayout()); // Pannello per JXMapViewer
            mapPanelInDialog.add(actualMapDisplayPanel, BorderLayout.CENTER);

            SimplifiedMapLoader.createClickableMap(actualMapDisplayPanel, initialPos, 10, event -> SwingUtilities.invokeLater(() -> { // Assicurati che l'aggiornamento della UI sia sull'EDT
                if (event.getAddress() != null) {
                    selectedAddressField.setText(event.getAddress());
                } else {
                    selectedAddressField.setText("Indirizzo non valido o fuori confine Italiano");
                }
            }));

            mapDialogInstance.setLocationRelativeTo(frame);
            mapDialogInstance.setVisible(true); // Mostra il dialogo
        });
        return this;
    }

    private void performSearchAction(JFrame frame, Controller control, List<JCheckBox> checkBoxesList) {
        GeoPosition geoPos = MapLoader.getCoordinatesFromAddress(searchField.getText());
        if (geoPos != null) {
            List<Immobile> immobiles = control.ricerca(geoPos, fetchFiltri());
            frame.setContentPane(new InterfacciaListaImmobili(frame, checkBoxesList,bottoniOpzioni, geoPos, control, this.token,internalFrameFiltri,immobiles));
            frame.revalidate();
            frame.repaint();
        } else {
            JOptionPane.showMessageDialog(this, "Indirizzo non valido.");
        }
    }

    private void configureToggleButton(JToggleButton button) {
        button.addActionListener(e -> {
            button.setBackground(button.isSelected() ? iconSet.getMainColor() : Color.WHITE);
        });
    }

    //Funzione Cambio Cursore

    private void setUpOpzioniMenu(JFrame frame, Controller control, List<JCheckBox> checkBoxesList) {

        searchButton.addActionListener(e -> performSearchAction(frame,control,checkBoxesList));
        //Cerca con Invio
        searchField.addActionListener(e -> searchButton.doClick());

    }

    private void setUpComponents() {
        //--Cursor
        for (JButton jButton : Arrays.asList(mapButton, searchButton, buttonFiltri, buttonConfermaFiltri, buttonCancellaFiltri)) {
            jButton.addMouseListener(iconSet.cursorButton());
        }
        for(JToggleButton toggleButton : Arrays.asList(toggleButtonAscensore, toggleButtonBalcone, toggleButtonCantina, toggleButtonClimatizzazione, toggleButtonPortineria)){
            toggleButton.addMouseListener(iconSet.cursorButton());
        }
        listaTipi.addMouseListener(iconSet.cursorButton());
        comboBoxTipVendita.addMouseListener(iconSet.cursorButton());
        menuOpzioni.addMouseListener(iconSet.cursorButton());
        comboBoxSuperfice.addMouseListener(iconSet.cursorButton());

        //--Icon
        labelTitolo.setIcon(iconSet.getHomeIcon());
        searchButton.setIcon(iconSet.getSearcherIcon());
        mapButton.setIcon(iconSet.getMapIcon());

        labelPiani.setIcon(iconSet.getPianiIcon());
        labelTipoVendita.setIcon(iconSet.getTipoVenditaIcon());
        labelNumeroBagni.setIcon(iconSet.getNumeroBagniIcon());
        labelClasseE.setIcon(iconSet.getClasseEnergeticaIcon());
        labelStanze.setIcon(iconSet.getNumeroStanzeIcon());

        toggleButtonAscensore.setIcon(iconSet.getAscensoreIcon());
        toggleButtonBalcone.setIcon(iconSet.getBalconeIcon());
        toggleButtonCantina.setIcon(iconSet.getCantinaIcon());
        toggleButtonClimatizzazione.setIcon(iconSet.getClimatizzazioneIcon());
        toggleButtonPortineria.setIcon(iconSet.getPortineriaIcon());
    }

    private void initializePopupMenu() {
        // Crea un nuovo JPopupMenu
        popupMenu = new JPopupMenu();

        // Aggiungi un'opzione per pulire il campo di testo
        JMenuItem clearFieldItem = new JMenuItem("Clear Field");
        clearFieldItem.addActionListener(e -> searchField.setText("")); // Pulisce il campo quando selezionato


        // Aggiungi un'opzione per avviare la ricerca
        JMenuItem searchItem = new JMenuItem("Search");
        searchItem.addActionListener(e -> JOptionPane.showMessageDialog(null, "Search triggered for: " + searchField.getText()));

        // Aggiungi le opzioni al menu
        popupMenu.add(clearFieldItem);
        popupMenu.add(searchItem);
    }

    private void addPopupToSearchField() {
        // Aggiungi un MouseListener al campo di testo per mostrare il popup
        searchField.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                showPopupMenu(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                showPopupMenu(e);
            }

            private void showPopupMenu(MouseEvent e) {
                if (e.isPopupTrigger()) { // Controlla se è un click destro
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }

    private Map<String, Object> fetchFiltri(){
        Map<String, Object> filtri = new HashMap<>();

        //filtri.put("prezzoMax", comboBoxPrezzoMax.getSelectedItem());
        //filtri.put("prezzoMin", comboBoxPrezzoMinimo.getSelectedItem());
        //filtri.put("superficie", comboBoxSuperfice.getSelectedItem());
        //filtri.put("tipoVendita", comboBoxTipVendita.getSelectedItem());
        //filtri.put("tipoImmobile", listaTipi.getSelectedItem());
        //filtri.put("numBagni", spinnerBagni.getValue());
        //filtri.put("numStanze", spinnerStanze.getValue());
        //filtri.put("classeEnergetica", comboBoxClasseE.getSelectedItem());
        //filtri.put("piano", spinnerPiani.getValue());
        filtri.put("ascensore", toggleButtonAscensore.isSelected());
        filtri.put("balcone", toggleButtonBalcone.isSelected());
        filtri.put("cantina", toggleButtonCantina.isSelected());
        filtri.put("climatizzazione", toggleButtonClimatizzazione.isSelected());
        filtri.put("portineria", toggleButtonPortineria.isSelected());

        return filtri;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Evaluation license - Marco Scotto
        labelTitolo = new JLabel();
        mapButton = new JButton();
        searchButton = new JButton();
        searchField = new JTextField();
        listaTipi = new JComboBox<>();
        menuBarPrezziTutto = new JMenuBar();
        menuPrezzi = new JMenu();
        menuBarContenitorePrezziBox = new JMenuBar();
        comboBoxPrezzoMax = new JComboBox<>();
        comboBoxPrezzoMinimo = new JComboBox<>();
        comboBoxSuperfice = new JComboBox<>();
        buttonFiltri = new JButton();
        panelMenuBarOpzioniColore = new JPanel();
        menuBarOpzioni = new JMenuBar();
        menuOpzioni = new JMenu();
        LogoOpzioni = new JLabel();
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
        LogoOpzioni2 = new JLabel();

        //======== this ========
        setBorder ( new javax . swing. border .CompoundBorder ( new javax . swing. border .TitledBorder ( new javax . swing. border .EmptyBorder (
        0, 0 ,0 , 0) ,  "JFor\u006dDesi\u0067ner \u0045valu\u0061tion" , javax. swing .border . TitledBorder. CENTER ,javax . swing. border .TitledBorder
        . BOTTOM, new java. awt .Font ( "Dia\u006cog", java .awt . Font. BOLD ,12 ) ,java . awt. Color .
        red ) , getBorder () ) );  addPropertyChangeListener( new java. beans .PropertyChangeListener ( ){ @Override public void propertyChange (java .
        beans. PropertyChangeEvent e) { if( "bord\u0065r" .equals ( e. getPropertyName () ) )throw new RuntimeException( ) ;} } );
        setLayout(new MigLayout(
            "fill,insets 0,hidemode 3,align center center,gap 10 5",
            // columns
            "0[74,fill]0" +
            "[shrink 0,center]0" +
            "[left]0" +
            "[fill]0" +
            "[24,center]0" +
            "[102,center]0" +
            "[62,shrink 0,fill]0",
            // rows
            "[73,top]0" +
            "[73,center]0" +
            "[73,center]0" +
            "[73,center]0" +
            "[84,top]0" +
            "[90,center]0" +
            "[29]0" +
            "[77]0" +
            "[21,top]0"));

        //---- labelTitolo ----
        labelTitolo.setText("DietiEstates25 ");
        labelTitolo.setFont(new Font("Felix Titling", Font.BOLD | Font.ITALIC, 58));
        labelTitolo.setForeground(UIManager.getColor("Actions.Green"));
        labelTitolo.setHorizontalAlignment(SwingConstants.CENTER);
        labelTitolo.setIcon(UIManager.getIcon("FileChooser.homeFolderIcon"));
        labelTitolo.setHorizontalTextPosition(SwingConstants.CENTER);
        labelTitolo.setVerticalTextPosition(SwingConstants.BOTTOM);
        add(labelTitolo, "cell 1 1 5 1,align center center,grow 0 0");

        //---- mapButton ----
        mapButton.setIcon(UIManager.getIcon("FileChooser.listViewIcon"));
        mapButton.setMaximumSize(new Dimension(34, 35));
        mapButton.setMinimumSize(new Dimension(34, 35));
        mapButton.setBorder(null);
        mapButton.setBorderPainted(false);
        mapButton.setBackground(new Color(0x00ffffff, true));
        add(mapButton, "cell 0 2,alignx right,growx 0");

        //---- searchButton ----
        searchButton.setIcon(UIManager.getIcon("FileChooser.homeFolderIcon"));
        searchButton.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        searchButton.setIconTextGap(10);
        searchButton.setPreferredSize(new Dimension(38, 47));
        searchButton.setMinimumSize(new Dimension(40, 45));
        searchButton.setMaximumSize(new Dimension(40, 47));
        searchButton.setBackground(new Color(0x00ffffff, true));
        searchButton.setBorderPainted(false);
        searchButton.setBorder(null);
        add(searchButton, "cell 5 2,align right center,grow 0 0");

        //---- searchField ----
        searchField.setBackground(null);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 30));
        searchField.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
        searchField.setText("Cerca Qualcosa");
        add(searchField, "cell 1 2 5 1,aligny center,grow 100 0");

        //---- listaTipi ----
        listaTipi.setModel(new DefaultComboBoxModel<>(new String[] {
            "Casa",
            "Appartamento"
        }));
        listaTipi.setSelectedIndex(0);
        listaTipi.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        add(listaTipi, "cell 1 3,align center center,grow 0 0");

        //======== menuBarPrezziTutto ========
        {
            menuBarPrezziTutto.setBackground(new Color(0x999999));
            menuBarPrezziTutto.setBorder(UIManager.getBorder("CheckBox.border"));

            //======== menuPrezzi ========
            {
                menuPrezzi.setIcon(UIManager.getIcon("Table.descendingSortIcon"));
                menuPrezzi.setText("Prezzi");
                menuPrezzi.setHorizontalAlignment(SwingConstants.RIGHT);
                menuPrezzi.setHorizontalTextPosition(SwingConstants.RIGHT);
                menuPrezzi.setBorder(UIManager.getBorder("ComboBox.border"));
                menuPrezzi.setForeground(Color.white);
                menuPrezzi.setFont(new Font("Segoe UI", Font.PLAIN, 18));

                //======== menuBarContenitorePrezziBox ========
                {

                    //---- comboBoxPrezzoMax ----
                    comboBoxPrezzoMax.setModel(new DefaultComboBoxModel<>(new String[] {
                        "Prezzo Massimo",
                        "Poco",
                        "Medio ",
                        "Simone"
                    }));
                    comboBoxPrezzoMax.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                    menuBarContenitorePrezziBox.add(comboBoxPrezzoMax);

                    //---- comboBoxPrezzoMinimo ----
                    comboBoxPrezzoMinimo.setModel(new DefaultComboBoxModel<>(new String[] {
                        "Prezzo Minimo",
                        "Tanto ",
                        "Poco"
                    }));
                    comboBoxPrezzoMinimo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                    menuBarContenitorePrezziBox.add(comboBoxPrezzoMinimo);
                }
                menuPrezzi.add(menuBarContenitorePrezziBox);
            }
            menuBarPrezziTutto.add(menuPrezzi);
        }
        add(menuBarPrezziTutto, "cell 2 3,align left center,grow 0 0");

        //---- comboBoxSuperfice ----
        comboBoxSuperfice.setModel(new DefaultComboBoxModel<>(new String[] {
            "Superfice"
        }));
        comboBoxSuperfice.setForeground(Color.black);
        comboBoxSuperfice.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        add(comboBoxSuperfice, "cell 3 3,align left center,grow 0 0");

        //---- buttonFiltri ----
        buttonFiltri.setText("Filtri");
        buttonFiltri.setIcon(UIManager.getIcon("FileChooser.detailsViewIcon"));
        buttonFiltri.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        add(buttonFiltri, "cell 4 3,align left center,grow 0 0");

        //======== panelMenuBarOpzioniColore ========
        {
            panelMenuBarOpzioniColore.setForeground(Color.green);
            panelMenuBarOpzioniColore.setBackground(UIManager.getColor("Actions.Green"));
            panelMenuBarOpzioniColore.setBorder(new MatteBorder(1, 1, 1, 1, Color.black));
            panelMenuBarOpzioniColore.setLayout(new BorderLayout());

            //======== menuBarOpzioni ========
            {
                menuBarOpzioni.setBackground(new Color(0x66ff66));
                menuBarOpzioni.setBorderPainted(false);

                //======== menuOpzioni ========
                {
                    menuOpzioni.setText("Opzioni");
                    menuOpzioni.setIcon(UIManager.getIcon("Table.descendingSortIcon"));
                    menuOpzioni.setHorizontalAlignment(SwingConstants.CENTER);
                    menuOpzioni.setHorizontalTextPosition(SwingConstants.LEFT);
                    menuOpzioni.setForeground(new Color(0x66ff66));
                    menuOpzioni.setBackground(Color.green);
                    menuOpzioni.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                }
                menuBarOpzioni.add(menuOpzioni);
            }
            panelMenuBarOpzioniColore.add(menuBarOpzioni, BorderLayout.LINE_END);

            //---- LogoOpzioni ----
            LogoOpzioni.setIcon(new FlatSVGIcon("Logo.svg"));
            LogoOpzioni.setIconTextGap(2);
            panelMenuBarOpzioniColore.add(LogoOpzioni, BorderLayout.LINE_START);
        }
        add(panelMenuBarOpzioniColore, "north,growx");

        //======== internalFrameFiltri ========
        {
            internalFrameFiltri.setFont(new Font("Inter", Font.PLAIN, 22));
            internalFrameFiltri.setBorder(new LineBorder(UIManager.getColor("Actions.Green"), 2, true));
            internalFrameFiltri.setVisible(true);
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
            internalFrameFiltri.add(labelStanze, "cell 0 2 2 1");

            //---- spinnerStanze ----
            spinnerStanze.setModel(new SpinnerListModel(new String[] {"Indifferente", "1", "2", "3", "4", "5", "6+"}));
            spinnerStanze.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            internalFrameFiltri.add(spinnerStanze, "cell 2 2");

            //---- labelClasseE ----
            labelClasseE.setText("Classe Energetica");
            labelClasseE.setIcon(UIManager.getIcon("FileView.fileIcon"));
            labelClasseE.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            internalFrameFiltri.add(labelClasseE, "cell 0 3 2 1");

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
            buttonConfermaFiltri.setBackground(UIManager.getColor("Actions.Green"));
            internalFrameFiltri.add(buttonConfermaFiltri, "cell 2 11,alignx center,grow 0 100");
        }
        add(internalFrameFiltri, "east");

        //---- LogoOpzioni2 ----
        LogoOpzioni2.setIcon(new FlatSVGIcon("Logo.svg"));
        LogoOpzioni2.setIconTextGap(2);
        add(LogoOpzioni2, "cell 0 7,align center bottom,grow 0 0");
        // JFormDesigner - End of component initialization //GEN-END:initComponents @formatter:on
    }


    // JFormDesigner - Variables declaration - DO NOT MODIFY //GEN-BEGIN:variables @formatter:off
    // Generated using JFormDesigner Evaluation license - Marco Scotto
    private JLabel labelTitolo;
    private JButton mapButton;
    private JButton searchButton;
    private JTextField searchField;
    private JComboBox<String> listaTipi;
    private JMenuBar menuBarPrezziTutto;
    private JMenu menuPrezzi;
    private JMenuBar menuBarContenitorePrezziBox;
    private JComboBox<String> comboBoxPrezzoMax;
    private JComboBox<String> comboBoxPrezzoMinimo;
    private JComboBox<String> comboBoxSuperfice;
    private JButton buttonFiltri;
    private JPanel panelMenuBarOpzioniColore;
    private JMenuBar menuBarOpzioni;
    private JMenu menuOpzioni;
    private JLabel LogoOpzioni;
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
    private JLabel LogoOpzioni2;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
