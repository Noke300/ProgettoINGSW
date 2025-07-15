package gui;

import javax.swing.border.*;
import com.formdev.flatlaf.extras.*;
import controller.Controller;
import controller.NotificationClient;
import gui.costum_components.*;
import gui.costum_components.maps.MapLoader;
import gui.costum_components.maps.SimplifiedMapLoader;
import gui.pagine_administrative.LogInAmministrazione;
import model.ImmobileEImmagini;
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
    private String token;
    public static NotificationClient notifiche;
    private final BottoniOpzioni bottoniOpzioni = new BottoniOpzioni();
    private final AutoCompleteAddress autoCompleteAddress = new AutoCompleteAddress();
    public Ricerca(JFrame frame, Controller control, String token) {
        this.token = token;
        initComponents();
        initializePopupMenu(); // Inizializza il menu
        addPopupToSearchField();// Collega il menu al searchField
        createRicercaPanel(frame,control);
        System.out.println("T : "+this.token+"\n");
    }

    @Override
    public void addNotify() {
        super.addNotify();
        // Assicurati che tutti i menu popup siano nascosti quando il pannello viene aggiunto
        MenuSelectionManager.defaultManager().clearSelectedPath();
    }

    public void createRicercaPanel(JFrame frame, Controller control) {

        internalFrameFiltri.setVisible(false);

        setUpComponents();
        // --- 4. Auto-completamento ---
        autoCompleteAddress.setupSearchAutocomplete(searchField);

        // --- 5. Azione del pulsante "Cerca" ---//--- 6. Menu Opzioni ---
        bottoniOpzioni.setUpOpzioniMenu(frame,control,this.token,menuBarOpzioni,menuOpzioni);

        setUpOpzioniMenu(frame,control);

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

        comboBoxTipVendita.addActionListener(e -> {
            String tipoSelezionato = (String) comboBoxTipVendita.getSelectedItem();
            aggiornaPrezzi(comboBoxPrezzoMinimo, comboBoxPrezzoMax, tipoSelezionato);
        });
        // Usage for each button
        configureToggleButton(toggleButtonCantina);
        configureToggleButton(toggleButtonBalcone);
        configureToggleButton(toggleButtonAscensore);
        configureToggleButton(toggleButtonPortineria);
        configureToggleButton(toggleButtonClimatizzazione);

        // --- 8. Prezzi ---
        aggiornaPrezzi(comboBoxPrezzoMinimo, comboBoxPrezzoMax, "Vendita");

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


        if(!token.isEmpty() && control.getNameLogIn().equals("[AGENTE_IMMOBILIARE]")){
                notifiche.addListener(messaggio -> SwingUtilities.invokeLater(() -> {
                NotificationClient.notificheWindow(messaggio);
            }));
        }

    }

    private void aggiornaPrezzi(JComboBox<String> comboMin, JComboBox<String> comboMax, String tipo) {

        if ("Vendita".equals(tipo)) {
            comboMax.removeAllItems();
            comboMin.removeAllItems();
            comboMax.addItem("Prezzo Massimo");
            comboMin.addItem("Prezzo Minimo");

            comboMax.addItem("200.000€");
            comboMax.addItem("250.000€");
            comboMax.addItem("300.000€");
            comboMax.addItem("350.000€");
            comboMax.addItem("400.000€");
            comboMax.addItem("450.000€");
            comboMax.addItem("500.000€");
            comboMax.addItem("550.000€");
            comboMax.addItem("600.000€");
            comboMax.addItem("650.000€");
            comboMax.addItem("700.000€");
            comboMax.addItem("750.000€");
            comboMax.addItem("800.000€");
            comboMax.addItem("850.000€");
            comboMax.addItem("900.000€");
            comboMax.addItem("950.000€");
            comboMax.addItem("1.000.000€");

            comboMin.addItem("50.000€");
            comboMin.addItem("100.000€");
            comboMin.addItem("150.000€");
            comboMin.addItem("200.000€");
            comboMin.addItem("250.000€");
            comboMin.addItem("300.000€");
            comboMin.addItem("350.000€");
            comboMin.addItem("400.000€");
            comboMin.addItem("450.000€");
            comboMin.addItem("500.000€");
            comboMin.addItem("550.000€");
            comboMin.addItem("600.000€");
            comboMin.addItem("650.000€");
            comboMin.addItem("700.000€");
            comboMin.addItem("750.000€");
            comboMin.addItem("800.000€");
        } else if ("Affitto".equals(tipo)) {
            comboMax.removeAllItems();
            comboMin.removeAllItems();
            comboMax.addItem("Prezzo Massimo");
            comboMin.addItem("Prezzo Minimo");

            comboMax.addItem("200€");
            comboMax.addItem("250€");
            comboMax.addItem("300€");
            comboMax.addItem("350€");
            comboMax.addItem("400€");
            comboMax.addItem("450€");
            comboMax.addItem("500€");
            comboMax.addItem("550€");
            comboMax.addItem("600€");
            comboMax.addItem("650€");
            comboMax.addItem("700€");
            comboMax.addItem("750€");
            comboMax.addItem("800€");
            comboMax.addItem("850€");
            comboMax.addItem("900€");
            comboMax.addItem("950€");
            comboMax.addItem("1.000€");
            comboMax.addItem("1.100€");
            comboMax.addItem("1.200€");
            comboMax.addItem("1.300€");
            comboMax.addItem("1.400€");
            comboMax.addItem("1.500€");

            comboMin.addItem("50€");
            comboMin.addItem("100€");
            comboMin.addItem("150€");
            comboMin.addItem("200€");
            comboMin.addItem("250€");
            comboMin.addItem("300€");
            comboMin.addItem("350€");
            comboMin.addItem("400€");
            comboMin.addItem("450€");
            comboMin.addItem("500€");
            comboMin.addItem("550€");
            comboMin.addItem("600€");
            comboMin.addItem("650€");
            comboMin.addItem("700€");
            comboMin.addItem("750€");
            comboMin.addItem("800€");
            comboMin.addItem("850€");
            comboMin.addItem("900€");
            comboMin.addItem("950€");
            comboMin.addItem("1.000€");
        }
    }

    private void performSearchAction(JFrame frame, Controller control) {
        GeoPosition geoPos = MapLoader.getCoordinatesFromAddress(searchField.getText());
        if (geoPos != null) {
            List<ImmobileEImmagini> immobiles = control.ricerca(geoPos, fetchFiltri());
            if (!immobiles.isEmpty()) {
                frame.setContentPane(new InterfacciaListaImmobili(frame,bottoniOpzioni, geoPos, control, this.token,internalFrameFiltri,immobiles));
                frame.revalidate();
                frame.repaint();
            }else{
                JOptionPane.showMessageDialog(this, "Nessun Immobile trovato.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Indirizzo non valido.");
        }
    }

    private void configureToggleButton(JToggleButton button) {
        button.addActionListener(e -> button.setBackground(button.isSelected() ? IconClass.getMainColor() : Color.WHITE));
    }

    //Funzione Cambio Cursore

    private void setUpOpzioniMenu(JFrame frame, Controller control) {

        searchButton.addActionListener(e -> performSearchAction(frame,control));
        //Cerca con Invio
        searchField.addActionListener(e -> searchButton.doClick());

    }

    private void setUpComponents() {
        //--Cursor
        for (JButton jButton : Arrays.asList(mapButton, searchButton, buttonFiltri, buttonConfermaFiltri, buttonCancellaFiltri)) {
            jButton.addMouseListener(IconClass.cursorButton());
        }
        for(JToggleButton toggleButton : Arrays.asList(toggleButtonAscensore, toggleButtonBalcone, toggleButtonCantina, toggleButtonClimatizzazione, toggleButtonPortineria)){
            toggleButton.addMouseListener(IconClass.cursorButton());
        }
        listaTipi.addMouseListener(IconClass.cursorButton());
        comboBoxTipVendita.addMouseListener(IconClass.cursorButton());
        menuOpzioni.addMouseListener(IconClass.cursorButton());
        comboBoxSuperfice.addMouseListener(IconClass.cursorButton());

        //--Icon
        labelTitolo.setIcon(IconClass.getHomeIcon());
        searchButton.setIcon(IconClass.getSearcherIcon());
        mapButton.setIcon(IconClass.getMapIcon());

        labelPiani.setIcon(IconClass.getPianiIcon());
        labelTipoVendita.setIcon(IconClass.getTipoVenditaIcon());
        labelNumeroBagni.setIcon(IconClass.getNumeroBagniIcon());
        labelClasseE.setIcon(IconClass.getClasseEnergeticaIcon());
        labelStanze.setIcon(IconClass.getNumeroStanzeIcon());

        toggleButtonAscensore.setIcon(IconClass.getAscensoreIcon());
        toggleButtonBalcone.setIcon(IconClass.getBalconeIcon());
        toggleButtonCantina.setIcon(IconClass.getCantinaIcon());
        toggleButtonClimatizzazione.setIcon(IconClass.getClimatizzazioneIcon());
        toggleButtonPortineria.setIcon(IconClass.getPortineriaIcon());
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

        filtri.put("prezzoMax", traduciPrezzo((String) comboBoxPrezzoMax.getSelectedItem()));
        filtri.put("prezzoMin", traduciPrezzo((String) comboBoxPrezzoMinimo.getSelectedItem()));
        filtri.put("superficie", getSuperficie((String) comboBoxSuperfice.getSelectedItem()));

        if(comboBoxTipVendita.getSelectedItem().equals("Qualsiasi")){
            filtri.put("tipoVendita", null);
        }else{
            filtri.put("tipoVendita", comboBoxTipVendita.getSelectedItem());
        }

        if(listaTipi.getSelectedItem().equals("Case e Appartamenti")){
            filtri.put("tipoImmobile", null);
        }else{
            filtri.put("tipoImmobile", listaTipi.getSelectedItem());
        }

        //TODO Testare tutti i filtri, in particolare spinner bagni e stanze, se non funziona trasforma le stinghe in int
        if(spinnerBagni.getValue().equals("Indifferente")){
            filtri.put("numBagni", -1);
        }else{
            filtri.put("numBagni", ((String) spinnerBagni.getValue()).replaceAll("[^\\d]", ""));  //toglie il +
        }

        if(spinnerStanze.getValue().equals("Indifferente")){
            filtri.put("numStanze", 0);
        }else{
            filtri.put("numStanze", ((String) spinnerStanze.getValue()).replaceAll("[^\\d]", ""));  //toglie il +
        }

        if(comboBoxClasseE.getSelectedItem().equals("Indifferente")){
            filtri.put("classeEnergetica", null);
        }else{
            filtri.put("classeEnergetica", ((String) comboBoxClasseE.getSelectedItem()).replace(" ", ""));
        }

        if(spinnerPiani.getValue().equals("Indifferente")){
            filtri.put("piano", -2);
        }else if (spinnerPiani.getValue().equals("Scantinato")){
            filtri.put("piano", -1);
        }else if (spinnerPiani.getValue().equals("Terra")){
            filtri.put("piano", 0);
        }else{
            filtri.put("piano", ((String) spinnerPiani.getValue()).replaceAll("[^\\d]", ""));  //toglie il +
        }
        filtri.put("ascensore", toggleButtonAscensore.isSelected());
        filtri.put("balcone", toggleButtonBalcone.isSelected());
        filtri.put("cantina", toggleButtonCantina.isSelected());
        filtri.put("climatizzazione", toggleButtonClimatizzazione.isSelected());
        filtri.put("portineria", toggleButtonPortineria.isSelected());

        return filtri;
    }

    private Integer getSuperficie(String superficieStr) {
        if (superficieStr.equals("Superficie")) {
            return 0;
        }

        // Rimuove tutti i caratteri non numerici
        String pulito = superficieStr.replaceAll("[^\\d]", "");

        try {
            return Integer.parseInt(pulito);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Formato non valido: " + superficieStr);
        }
    }

    private Double traduciPrezzo(String prezzoStr) {
        if (prezzoStr.equals("Prezzo Minimo") || prezzoStr.equals("Prezzo Massimo")) {
            return 0.0;
        }
        // Rimuove il simbolo € e i punti di separazione
        String pulito = prezzoStr.replace("€", "").replace(".", "").trim();

        try {
            return Double.parseDouble(pulito);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Formato non valido: " + prezzoStr);
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Evaluation license - Alessio Nesi
        labelTitolo = new JLabel();
        mapButton = new JButton();
        searchButton = new JButton();
        searchField = new JTextField();
        listaTipi = new JComboBox<>();
        menuBarPrezziTutto = new JMenuBar();
        menuPrezzi = new JMenu();
        menuBarContenitorePrezziBox = new JMenuBar();
        comboBoxPrezzoMinimo = new JComboBox<>();
        comboBoxPrezzoMax = new JComboBox<>();
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
        setBorder (new javax. swing. border. CompoundBorder( new javax .swing .border .TitledBorder (new javax. swing. border
        . EmptyBorder( 0, 0, 0, 0) , "JF\u006frmD\u0065sig\u006eer \u0045val\u0075ati\u006fn", javax. swing. border. TitledBorder. CENTER, javax
        . swing. border. TitledBorder. BOTTOM, new java .awt .Font ("Dia\u006cog" ,java .awt .Font .BOLD ,
        12 ), java. awt. Color. red) , getBorder( )) );  addPropertyChangeListener (new java. beans
        . PropertyChangeListener( ){ @Override public void propertyChange (java .beans .PropertyChangeEvent e) {if ("\u0062ord\u0065r" .equals (e .
        getPropertyName () )) throw new RuntimeException( ); }} );
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
            "Case e Appartamenti",
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

                    //---- comboBoxPrezzoMinimo ----
                    comboBoxPrezzoMinimo.setModel(new DefaultComboBoxModel<>(new String[] {
                        "Prezzo Minimo",
                        "Poco",
                        "Medio ",
                        "Simone"
                    }));
                    comboBoxPrezzoMinimo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                    menuBarContenitorePrezziBox.add(comboBoxPrezzoMinimo);

                    //---- comboBoxPrezzoMax ----
                    comboBoxPrezzoMax.setModel(new DefaultComboBoxModel<>(new String[] {
                        "Prezzo Massimo",
                        "Tanto ",
                        "Poco"
                    }));
                    comboBoxPrezzoMax.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                    menuBarContenitorePrezziBox.add(comboBoxPrezzoMax);
                }
                menuPrezzi.add(menuBarContenitorePrezziBox);
            }
            menuBarPrezziTutto.add(menuPrezzi);
        }
        add(menuBarPrezziTutto, "cell 2 3,align left center,grow 0 0");

        //---- comboBoxSuperfice ----
        comboBoxSuperfice.setModel(new DefaultComboBoxModel<>(new String[] {
            "Superficie",
            "10m\u00b2",
            "30m\u00b2",
            "50m\u00b2",
            "70m\u00b2",
            "90m\u00b2",
            "110m\u00b2",
            "130m\u00b2",
            "150m\u00b2",
            "170m\u00b2",
            "190m\u00b2",
            "210m\u00b2",
            "230m\u00b2",
            "250m\u00b2",
            "270m\u00b2",
            "290m\u00b2",
            "310m\u00b2",
            "330m\u00b2",
            "350m\u00b2",
            "370m\u00b2",
            "390m\u00b2",
            "410m\u00b2",
            "430m\u00b2",
            "450m\u00b2",
            "470m\u00b2",
            "500m\u00b2"
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
                "[]para" +
                "[]rel" +
                "[]para" +
                "[]para" +
                "[]0" +
                "[27,center]ind"));

            //---- labelTipoVendita ----
            labelTipoVendita.setText("Tipo Di Vendita");
            labelTipoVendita.setIcon(UIManager.getIcon("FileView.fileIcon"));
            labelTipoVendita.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            internalFrameFiltri.add(labelTipoVendita, "cell 0 0");

            //---- comboBoxTipVendita ----
            comboBoxTipVendita.setModel(new DefaultComboBoxModel<>(new String[] {
                "Qualsiasi",
                "Affitto",
                "Vendita"
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
            comboBoxClasseE.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            comboBoxClasseE.setModel(new DefaultComboBoxModel<>(new String[] {
                "Indifferente",
                "A+ ",
                "A ",
                "B ",
                "C ",
                "D ",
                "E ",
                "F ",
                "G"
            }));
            internalFrameFiltri.add(comboBoxClasseE, "cell 2 3");

            //---- labelPiani ----
            labelPiani.setText("Piano/i");
            labelPiani.setIcon(UIManager.getIcon("FileView.fileIcon"));
            labelPiani.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            internalFrameFiltri.add(labelPiani, "cell 0 4 2 1");

            //---- spinnerPiani ----
            spinnerPiani.setModel(new SpinnerListModel(new String[] {"Indifferente", "Scantinato", "Terra", "1", "2", "3", "4", "5+"}));
            spinnerPiani.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            internalFrameFiltri.add(spinnerPiani, "cell 2 4");

            //---- label1 ----
            label1.setText("Altre Caratteristiche");
            label1.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            label1.setIcon(UIManager.getIcon("FileChooser.listViewIcon"));
            internalFrameFiltri.add(label1, "cell 0 5 3 1,alignx center,growx 0");

            //---- toggleButtonAscensore ----
            toggleButtonAscensore.setText("Ascensore");
            toggleButtonAscensore.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            toggleButtonAscensore.setIcon(UIManager.getIcon("FileChooser.listViewIcon"));
            internalFrameFiltri.add(toggleButtonAscensore, "cell 0 6");

            //---- toggleButtonBalcone ----
            toggleButtonBalcone.setText("Balcone ");
            toggleButtonBalcone.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            toggleButtonBalcone.setIcon(UIManager.getIcon("FileChooser.listViewIcon"));
            internalFrameFiltri.add(toggleButtonBalcone, "cell 1 6");

            //---- toggleButtonCantina ----
            toggleButtonCantina.setText("Cantina");
            toggleButtonCantina.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            toggleButtonCantina.setIcon(UIManager.getIcon("FileChooser.listViewIcon"));
            internalFrameFiltri.add(toggleButtonCantina, "cell 2 6");

            //---- toggleButtonClimatizzazione ----
            toggleButtonClimatizzazione.setText("Climatizzazione");
            toggleButtonClimatizzazione.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            toggleButtonClimatizzazione.setIcon(UIManager.getIcon("FileChooser.listViewIcon"));
            internalFrameFiltri.add(toggleButtonClimatizzazione, "cell 0 7");

            //---- toggleButtonPortineria ----
            toggleButtonPortineria.setText("Portineria");
            toggleButtonPortineria.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            toggleButtonPortineria.setIcon(UIManager.getIcon("FileChooser.listViewIcon"));
            internalFrameFiltri.add(toggleButtonPortineria, "cell 1 7");

            //---- separator1 ----
            separator1.setBackground(Color.black);
            internalFrameFiltri.add(separator1, "cell 0 8 3 1,aligny bottom,grow 100 0");

            //---- buttonCancellaFiltri ----
            buttonCancellaFiltri.setText("Cancella");
            buttonCancellaFiltri.setBackground(new Color(0x603c3e));
            internalFrameFiltri.add(buttonCancellaFiltri, "cell 1 9,alignx center,grow 0 100");

            //---- buttonConfermaFiltri ----
            buttonConfermaFiltri.setText("Conferma");
            buttonConfermaFiltri.setBackground(UIManager.getColor("Actions.Green"));
            internalFrameFiltri.add(buttonConfermaFiltri, "cell 2 9,alignx center,grow 0 100");
        }
        add(internalFrameFiltri, "east");

        //---- LogoOpzioni2 ----
        LogoOpzioni2.setIcon(new FlatSVGIcon("Logo.svg"));
        LogoOpzioni2.setIconTextGap(2);
        add(LogoOpzioni2, "cell 0 7,align center bottom,grow 0 0");
        // JFormDesigner - End of component initialization //GEN-END:initComponents @formatter:on
    }


    // JFormDesigner - Variables declaration - DO NOT MODIFY //GEN-BEGIN:variables @formatter:off
    // Generated using JFormDesigner Evaluation license - Alessio Nesi
    private JLabel labelTitolo;
    private JButton mapButton;
    private JButton searchButton;
    private JTextField searchField;
    private JComboBox<String> listaTipi;
    private JMenuBar menuBarPrezziTutto;
    private JMenu menuPrezzi;
    private JMenuBar menuBarContenitorePrezziBox;
    private JComboBox<String> comboBoxPrezzoMinimo;
    private JComboBox<String> comboBoxPrezzoMax;
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
    protected JComboBox<String> comboBoxClasseE;
    private JLabel labelPiani;
    private JSpinner spinnerPiani;
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
