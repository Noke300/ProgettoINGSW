package gui;

import javax.swing.border.*;
import com.formdev.flatlaf.extras.*;
import controller.Controller;
import gui.costum_components.IconClass;
import gui.costum_components.MapLoader;
import net.miginfocom.swing.*;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jxmapviewer.viewer.GeoPosition;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.function.Function;

public class Ricerca extends JPanel {

    private static final String GEOAPIFY_API_KEY = "df7445014a484278a655999c41bc28c5";
    private JPopupMenu popupMenu;
    IconClass iconSet = new IconClass();
    String token;
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

        // --- 3. Popup per i suggerimenti ---
        JPopupMenu suggestionPopup = new JPopupMenu();

        // --- 4. Auto-completamento ---
        searchField.getDocument().addDocumentListener(createAutoSuggestListener(searchField, suggestionPopup, this::fetchAutocompleteSuggestions));


        // --- 5. Azione del pulsante "Cerca" ---
        searchButton.addActionListener(e -> {
            GeoPosition geoPos = MapLoader.getCoordinatesFromAddress(searchField.getText());
            if (geoPos != null) {
                // Passa a un altro pannello, ad esempio una mappa (ipotesi: esiste MapPanel)
                frame.setContentPane(new InterfacciaListaImmobili(frame,checkBoxesList,geoPos,control,this.token));
                frame.revalidate();
                frame.repaint();
            } else {
                JOptionPane.showMessageDialog(this, "Indirizzo non valido.");
            }
        });

        //--- 6. Menu Opzioni ---
        backButtonHome.addActionListener(e -> {
            // Supponiamo che la classe Home abbia il metodo createHomePanel
            menuOpzioni.setVisible(false);
            menuBarOpzioni.setVisible(false);

            frame.setContentPane(new Home().createHomePanel(frame));
            frame.revalidate();
            frame.repaint();
        });
        logInButton.addActionListener(e -> {
        if (this.token.isEmpty()) {

                menuOpzioni.setVisible(false);
                menuBarOpzioni.setVisible(false);
                frame.setContentPane(new LogIn(frame, control,this.token ));

        }else {
            this.token = "";
            setUpLogInOutButton(this.token);
        }
            frame.revalidate();
            frame.repaint();
        });

        buttonAccountAmministrazione.addActionListener(e -> {
            menuOpzioni.setVisible(false);
            menuBarOpzioni.setVisible(false);
            frame.setContentPane(new InterfacciaAgenteImmobiliare(frame,control,this.token));
            frame.revalidate();
            frame.repaint();
        });


        
        //--- 7. Filtri  ---
        buttonFiltri.addActionListener(e ->
            // Alterna la visibilità del pannello interno dei filtri
            internalFrameFiltri.setVisible(!internalFrameFiltri.isVisible()));

        buttonConfermaFiltri.addActionListener(e ->internalFrameFiltri.setVisible(false));

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

        return this;
    }

    private void setUpLogInOutButton(String token){
        if (token.isEmpty()){
            logInButton.setText("Login");
            logInButton.setIcon(iconSet.getLogInIcon());
        }else{
            logInButton.setText("Logout");
            logInButton.setIcon(iconSet.getLogOutIcon());
        }
    }

    private DocumentListener createAutoSuggestListener(JTextField searchField, JPopupMenu suggestionPopup,
                                                       Function<String, List<String>> fetchSuggestionsFn) {
        return new DocumentListener() {
            private static final int TIMER_DELAY_MS = 700;

            private Timer initializeTimer() {
                Timer timer = new Timer(TIMER_DELAY_MS, e -> handleSuggestionInput());
                timer.setRepeats(false);
                return timer;
            }

            private final Timer timer = initializeTimer();

            @Override
            public void insertUpdate(DocumentEvent e) {
                timer.restart();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                timer.restart();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                timer.restart();
            }

            private void handleSuggestionInput() {
                SwingUtilities.invokeLater(() -> {
                    String inputText = searchField.getText().trim();
                    if (inputText.isEmpty()) {
                        hideSuggestions();
                        return;
                    }

                    List<String> suggestions = fetchSuggestionsFn.apply(inputText);
                    if (suggestions.isEmpty()) {
                        hideSuggestions();
                        return;
                    }

                    populateSuggestions(suggestions);
                    displaySuggestionPopup();
                });
            }

            private void hideSuggestions() {
                suggestionPopup.setVisible(false);
            }

            private void populateSuggestions(List<String> suggestions) {
                suggestionPopup.removeAll();
                for (String suggestion : suggestions) {
                    JMenuItem item = new JMenuItem(suggestion);
                    item.addActionListener((ActionEvent ev) -> {
                        searchField.setText(suggestion);
                        suggestionPopup.setVisible(false);
                    });
                    suggestionPopup.add(item);
                }
            }

            private void displaySuggestionPopup() {
                try {
                    Rectangle2D caretCoords2D = searchField.modelToView2D(searchField.getCaretPosition());
                    if (caretCoords2D != null) {
                        Rectangle caretCoords = new Rectangle(
                                (int) caretCoords2D.getX(),
                                (int) caretCoords2D.getY(),
                                (int) caretCoords2D.getWidth(),
                                (int) caretCoords2D.getHeight()
                        );
                        Point popupLocation = new Point(caretCoords.x, caretCoords.y + caretCoords.height);
                        SwingUtilities.convertPointToScreen(popupLocation, searchField);
                        suggestionPopup.setLocation(popupLocation);
                        suggestionPopup.setInvoker(searchField);
                        suggestionPopup.setVisible(true);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
    }

    //Funzione Cambio Cursore
    private MouseAdapter cursorButton(){
        return new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                e.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                // Ripristina il cursore quando il mouse esce dal bottone
                e.getComponent().setCursor(Cursor.getDefaultCursor());
            }
        };
    }

    private void setUpComponents() {
        //--Cursor
        mapButton.addMouseListener(cursorButton());
        searchButton.addMouseListener(cursorButton());
        buttonFiltri.addMouseListener(cursorButton());
        buttonConfermaFiltri.addMouseListener(cursorButton());
        buttonCancellaFiltri.addMouseListener(cursorButton());
        logInButton.addMouseListener(cursorButton());
        buttonAccountAmministrazione.addMouseListener(cursorButton());
        backButtonHome.addMouseListener(cursorButton());
        listaTipi.addMouseListener(cursorButton());
        comboBoxTipVendita.addMouseListener(cursorButton());
        menuOpzioni.addMouseListener(cursorButton());
        comboBoxSuperfice.addMouseListener(cursorButton());

        //--Icon
        setUpLogInOutButton(this.token);
        labelTitolo.setIcon(iconSet.getHomeIcon());
        searchButton.setIcon(iconSet.getSearcherIcon());
        backButtonHome.setIcon(iconSet.getHomeButtonIcon());
        mapButton.setIcon(iconSet.getMapIcon());
        labelPiani.setIcon(iconSet.getPianiIcon());
        labelTipoVendita.setIcon(iconSet.getTipoVenditaIcon());
        labelNumeroBagni.setIcon(iconSet.getNumeroBagniIcon());
        labelClasseE.setIcon(iconSet.getClasseEnergeticaIcon());
        labelStanze.setIcon(iconSet.getNumeroStanzeIcon());
        buttonAccountAmministrazione.setIcon(iconSet.getAccauntAministazioneIcon());

    }

    private List<String> fetchAutocompleteSuggestions(String input) {
        List<String> results = new ArrayList<>();
        Set<String> uniqueLocations = new HashSet<>(); // Usa un Set per rimuovere duplicati

        OkHttpClient client = new OkHttpClient();
        String url = String.format("https://api.geoapify.com/v1/geocode/autocomplete?text=%s&limit=10&lang=it&filter=countrycode:it&apiKey=%s",
                input, GEOAPIFY_API_KEY);

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.body() != null) {
                String jsonData = response.body().string();
                JSONParser parser = new JSONParser();
                JSONObject jsonObject = (JSONObject) parser.parse(jsonData);
                JSONArray features = (JSONArray) jsonObject.get("features");

                for (Object o : features) {
                    JSONObject feature = (JSONObject) o;
                    JSONObject properties = (JSONObject) feature.get("properties");
                    String formatted = (String) properties.get("formatted");

                    // Normalizza il nome rimuovendo "Italia" o "Italy" dalla fine
                    String normalized = formatted.replaceAll(",?\\s*(Italia|Italy)\\s*$", "").trim();

                    if (uniqueLocations.add(normalized)) {
                        results.add(formatted); // Mantiene il formato originale
                    }
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return results;
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

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Evaluation license - Marco GuIdo Scotto di Uccio
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
        logInButton = new JButton();
        buttonAccountAmministrazione = new JButton();
        backButtonHome = new JButton();
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
        setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(0
        ,0,0,0), "JF\u006frmDes\u0069gner \u0045valua\u0074ion",javax.swing.border.TitledBorder.CENTER,javax.swing.border.TitledBorder.BOTTOM
        ,new java.awt.Font("D\u0069alog",java.awt.Font.BOLD,12),java.awt.Color.red),
         getBorder())); addPropertyChangeListener(new java.beans.PropertyChangeListener(){@Override public void propertyChange(java.beans.PropertyChangeEvent e
        ){if("\u0062order".equals(e.getPropertyName()))throw new RuntimeException();}});
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
        labelTitolo.setText("DietaEstate25 ");
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

                    //---- logInButton ----
                    logInButton.setText("Log In");
                    logInButton.setIcon(UIManager.getIcon("FileChooser.detailsViewIcon"));
                    logInButton.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                    menuOpzioni.add(logInButton);

                    //---- buttonAccountAmministrazione ----
                    buttonAccountAmministrazione.setText("Account Amministrazione");
                    buttonAccountAmministrazione.setIcon(UIManager.getIcon("FileChooser.newFolderIcon"));
                    buttonAccountAmministrazione.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                    menuOpzioni.add(buttonAccountAmministrazione);

                    //---- backButtonHome ----
                    backButtonHome.setHorizontalTextPosition(SwingConstants.RIGHT);
                    backButtonHome.setText("Home");
                    backButtonHome.setVerticalAlignment(SwingConstants.TOP);
                    backButtonHome.setVerticalTextPosition(SwingConstants.TOP);
                    backButtonHome.setIcon(UIManager.getIcon("FileChooser.homeFolderIcon"));
                    backButtonHome.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                    menuOpzioni.add(backButtonHome);
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
            buttonConfermaFiltri.setBackground(new Color(0x3b6b3d));
            internalFrameFiltri.add(buttonConfermaFiltri, "cell 2 11,alignx center,grow 0 100");
        }
        add(internalFrameFiltri, "east");

        //---- LogoOpzioni2 ----
        LogoOpzioni2.setIcon(new FlatSVGIcon("Logo.svg"));
        LogoOpzioni2.setIconTextGap(2);
        add(LogoOpzioni2, "cell 0 7,align center bottom,grow 0 0");
        // JFormDesigner - End of component initialization //GEN-END:initComponents @formatter:on
    }


    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    // Generated using JFormDesigner Evaluation license - Marco GuIdo Scotto di Uccio
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
    private JButton logInButton;
    private JButton buttonAccountAmministrazione;
    private JButton backButtonHome;
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
