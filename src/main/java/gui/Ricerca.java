package gui;

import javax.swing.border.*;
import com.formdev.flatlaf.extras.*;
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
import org.kordamp.ikonli.fontawesome.FontAwesome;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
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
    public Ricerca() {
        initComponents();
        cursoreComponents();
        initializePopupMenu(); // Inizializza il menu
        addPopupToSearchField();// Collega il menu al searchField
    }

    @Override
    public void addNotify() {
        super.addNotify();
        // Assicurati che tutti i menu popup siano nascosti quando il pannello viene aggiunto
        MenuSelectionManager.defaultManager().clearSelectedPath();
    }

    public JPanel createRicercaPanel(JFrame frame) {

        labelTitolo.setIcon(iconSet.getHomeIcon());
        internalFrameFiltri.setVisible(false);

        List<JCheckBox> checkBoxesList = new ArrayList<>();
        checkBoxesList.add(schoolsCheckBox);
        checkBoxesList.add(hospitalsCheckBox);
        checkBoxesList.add(parkCheckBox);


        // --- 3. Popup per i suggerimenti ---
        JPopupMenu suggestionPopup = new JPopupMenu();

        // --- 4. Auto-completamento ---
        searchField.getDocument().addDocumentListener(createAutoSuggestListener(searchField, suggestionPopup, this::fetchAutocompleteSuggestions)
        );

        // --- 5. Azione del pulsante "Cerca" ---
        searchButton.setIcon(iconSet.getSearcherIcon());
        searchButton.addActionListener(e -> {
            GeoPosition geoPos = MapLoader.getCoordinatesFromAddress(searchField.getText());
            if (geoPos != null) {
                // Passa a un altro pannello, ad esempio una mappa (ipotesi: esiste MapPanel)
                frame.setContentPane(new InterfacciaListaImmobili(frame,checkBoxesList,geoPos));
                frame.revalidate();
                frame.repaint();
            } else {
                JOptionPane.showMessageDialog(this, "Indirizzo non valido.");
            }
        });

        //--- 6. Menu Opzioni ---

        FontIcon homeIcon = FontIcon.of(FontAwesome.HOME, 24); // 24px di dimensione
        backButtonHome.setIcon(homeIcon);

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

        buttonAccountAmministrazione.addActionListener(e -> {
            menuOpzioni.setVisible(false);
            menuBarOpzioni.setVisible(false);

            frame.setContentPane(new InterfacciaAgenteImmobiliare(frame));
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

        labelPiani.setIcon(iconSet.getPianiIcon());
        labelTipoVendita.setIcon(iconSet.getTipoVenditaIcon());
        labelNumeroBagni.setIcon(iconSet.getNumeroBagniIcon());
        labelClasseE.setIcon(iconSet.getClasseEnergeticaIcon());
        labelStanze.setIcon(iconSet.getNumeroStanzeIcon());

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
        mapButton.setIcon(iconSet.getMapIcon());

        return this;
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

    private void cursoreComponents() {
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
        separator1 = new JSeparator();
        buttonConfermaFiltri = new JButton();
        buttonCancellaFiltri = new JButton();
        LogoOpzioni2 = new JLabel();

        //======== this ========
        setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder(new javax.swing.
        border.EmptyBorder(0,0,0,0), "JF\u006frm\u0044es\u0069gn\u0065r \u0045va\u006cua\u0074io\u006e",javax.swing.border.TitledBorder.CENTER
        ,javax.swing.border.TitledBorder.BOTTOM,new java.awt.Font("D\u0069al\u006fg",java.awt.Font
        .BOLD,12),java.awt.Color.red), getBorder())); addPropertyChangeListener(
        new java.beans.PropertyChangeListener(){@Override public void propertyChange(java.beans.PropertyChangeEvent e){if("\u0062or\u0064er"
        .equals(e.getPropertyName()))throw new RuntimeException();}});
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
                "[94,fill]" +
                "[fill]para" +
                "[fill]0" +
                "[102,fill]",
                // rows
                "[22]" +
                "[]" +
                "[]" +
                "[]" +
                "[]" +
                "[]para" +
                "[]para" +
                "[]0" +
                "[27,center]"));

            //---- labelTipoVendita ----
            labelTipoVendita.setText("Tipo Di Vendita");
            labelTipoVendita.setIcon(UIManager.getIcon("FileView.fileIcon"));
            internalFrameFiltri.add(labelTipoVendita, "cell 0 0 2 1");

            //---- comboBoxTipVendita ----
            comboBoxTipVendita.setModel(new DefaultComboBoxModel<>(new String[] {
                "Qualsiasi"
            }));
            internalFrameFiltri.add(comboBoxTipVendita, "cell 3 0");

            //---- labelNumeroBagni ----
            labelNumeroBagni.setText("Numero Bagni");
            labelNumeroBagni.setIcon(UIManager.getIcon("FileView.fileIcon"));
            internalFrameFiltri.add(labelNumeroBagni, "cell 0 1 2 1");

            //---- spinnerBagni ----
            spinnerBagni.setModel(new SpinnerListModel(new String[] {"Indifferente", "0", "1", "2", "3", "4", "5", "6+"}));
            internalFrameFiltri.add(spinnerBagni, "cell 3 1");

            //---- labelStanze ----
            labelStanze.setText("Numero Stanze");
            labelStanze.setIcon(UIManager.getIcon("FileView.fileIcon"));
            internalFrameFiltri.add(labelStanze, "cell 0 2 2 1");

            //---- spinnerStanze ----
            spinnerStanze.setModel(new SpinnerListModel(new String[] {"Indifferente", "1", "2", "3", "4", "5", "6+"}));
            internalFrameFiltri.add(spinnerStanze, "cell 3 2");

            //---- labelClasseE ----
            labelClasseE.setText("Classe Energetica");
            labelClasseE.setIcon(UIManager.getIcon("FileView.fileIcon"));
            internalFrameFiltri.add(labelClasseE, "cell 0 3 2 1");

            //---- comboBoxClasseE ----
            comboBoxClasseE.setModel(new DefaultComboBoxModel<>(new String[] {
                "Qualsiasi"
            }));
            internalFrameFiltri.add(comboBoxClasseE, "cell 3 3");

            //---- labelPiani ----
            labelPiani.setText("Piano/i");
            labelPiani.setIcon(UIManager.getIcon("FileView.fileIcon"));
            internalFrameFiltri.add(labelPiani, "cell 0 4 2 1");

            //---- spinnerPiani ----
            spinnerPiani.setModel(new SpinnerListModel(new String[] {"Indifferente", "Terra", "1", "2", "3", "4", "5+"}));
            internalFrameFiltri.add(spinnerPiani, "cell 3 4");

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
            internalFrameFiltri.add(hospitalsCheckBox, "cell 3 5,align left bottom,grow 0 0");

            //---- separator1 ----
            separator1.setBackground(Color.black);
            internalFrameFiltri.add(separator1, "cell 0 7 4 1,aligny bottom,grow 100 0");

            //---- buttonConfermaFiltri ----
            buttonConfermaFiltri.setText("Conferma");
            buttonConfermaFiltri.setBackground(new Color(0x3b6b3d));
            internalFrameFiltri.add(buttonConfermaFiltri, "cell 3 8");

            //---- buttonCancellaFiltri ----
            buttonCancellaFiltri.setText("Cancella");
            buttonCancellaFiltri.setBackground(new Color(0x603c3e));
            internalFrameFiltri.add(buttonCancellaFiltri, "cell 1 8");
        }
        add(internalFrameFiltri, "east,alignx center,grow 0 100");

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
    private JSeparator separator1;
    private JButton buttonConfermaFiltri;
    private JButton buttonCancellaFiltri;
    private JLabel LogoOpzioni2;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
