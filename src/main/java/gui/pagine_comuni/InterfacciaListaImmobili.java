/*
 * Created by JFormDesigner on Tue Apr 22 11:59:05 CEST 2025
 */

package gui.pagine_comuni;

import controller.Controller;
import controller.NotificationClient;
import gui.costum_components.*;
import gui.costum_components.maps.MapLoader;
import gui.costum_components.maps.SimplifiedMapLoader;
import model.Immobile;
import model.ImmobileEImmagini;
import net.miginfocom.swing.MigLayout;
import org.jxmapviewer.viewer.GeoPosition;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

/**
 * @author marco
 */
public class InterfacciaListaImmobili extends JPanel {
    private final ImmobilePanelSingolo  immobilePanelSingolo = new ImmobilePanelSingolo();
    private AutoCompleteAddress autoCompleteAddress = new AutoCompleteAddress();
    protected JComboBox<String> comboPrezzoMax = new JComboBox<>();
    protected JComboBox<String> comboPrezzoMin = new JComboBox<>();


    String token;

    private JPanel[] selectedPanel = new JPanel[1]; // inizialmente vuoto

//Rimuove i popup quando la pagina viene caricata
    @Override
    public void addNotify() {
        super.addNotify();
        // Assicurati che tutti i menu popup siano nascosti quando il pannello viene aggiunto
        MenuSelectionManager.defaultManager().clearSelectedPath();
    }
    public InterfacciaListaImmobili(JFrame frame, BottoniOpzioni bottoniOpzioni, GeoPosition geoPosition, Controller control, String token, JPanel internalFrameFiltri, List<ImmobileEImmagini> immobili) {
        this.token=token;
        initComponents();
        bottoniOpzioni.setUpOpzioniMenu(frame,control,this.token,menuBarOpzioni,menuOpzioni);
        internalFrameFiltri.setVisible(false);
        setUpOpzioniMenu(frame,control);
        setUpComponents();
        autoCompleteAddress.setupSearchAutocomplete(searchField);


        List<Immobile> immobiliS = new ArrayList<>();
        for (ImmobileEImmagini i : immobili){
            immobiliS.add(i.getImmobile());
        }

        searchButton.addActionListener(e -> {
            if(!searchField.getText().isEmpty()) {
                GeoPosition geoPos = MapLoader.getCoordinatesFromAddress(searchField.getText());
                Ricerca ricerca = new Ricerca(frame,control,token);
                BottoniOpzioni.getMapLog().put(LocalDateTime.now(),"Ricerca di Immobili : "+searchField.getText());
                assert geoPos != null;
                List<ImmobileEImmagini> immobiles = control.ricerca(geoPos, filtriEsterni(ricerca.fetchFiltri()));
                if (!immobiles.isEmpty()) {
                    frame.setContentPane(new InterfacciaListaImmobili(frame, bottoniOpzioni, geoPos, control, token, internalFrameFiltri, immobiles));
                    frame.revalidate();
                    frame.repaint();
                }else{
                    JOptionPane.showMessageDialog(this, "Nessun Immobile trovato.");
                }
            }else{
                JOptionPane.showMessageDialog(frame,
                        "Per favore, inserisci un indirizzo.",
                        "Nessun indirizzo inserito", JOptionPane.WARNING_MESSAGE);
            }

        });
        //--- 7. Filtri  ---
        JButton buttonConfermaFiltri = new JButton("Conferma");
        internalFrameFiltri.remove(18);
        internalFrameFiltri.add(buttonConfermaFiltri, "cell 2 11,alignx center,grow 0 100");


        buttonFiltri.addActionListener(e -> {
            internalFrameFiltri.setVisible(true);
            final JDialog filterDialog = new JDialog(frame, "Filtri", true);
            filterDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            filterDialog.setSize(700, 550);
            filterDialog.setResizable(false);
            filterDialog.setContentPane(internalFrameFiltri);

            ActionListener actionListener = e1 -> {
               internalFrameFiltri.setVisible(false);
                filterDialog.setVisible(false);
                filterDialog.dispose();
            };

            buttonConfermaFiltri.addActionListener(actionListener);
            filterDialog.setLocationRelativeTo(frame);
            filterDialog.setVisible(true);
        });

        new MapLoader(geoPosition,panelMap,immobiliS);
        // Create and add the first panel

        // In the initComponents method, replace the GridLayout with MigLayout
        panelListaImmobile.setLayout(new MigLayout("wrap 1, align center")); // One component per row

        Dimension dim  = new Dimension(780,270);
        for (ImmobileEImmagini immobileEImmagini : immobili) {
            JPanel panel = immobilePanelSingolo.createImmobileSingoloPanel(frame,immobileEImmagini,control,token,selectedPanel);
            panel.setName(immobileEImmagini.getImmobile().getId());
            panel.setMaximumSize(dim);
            panel.setPreferredSize(dim);
            panelListaImmobile.add(panel);
        }
        //Mappa
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
            GeoPosition initialPos = geoPosition; // Roma
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
            Ricerca.notifiche.addListener(messaggio -> SwingUtilities.invokeLater(() -> NotificationClient.notificheWindow(messaggio)));
        }

        // Prezzo Minimo
        JMenuItem minItem = new JMenuItem("Minimo:");
        minItem.setEnabled(false);
        menuPrezzi.add(minItem);
        menuPrezzi.add(comboPrezzoMin);

        // Prezzo Massimo
        JMenuItem maxItem = new JMenuItem("Massimo:");
        maxItem.setEnabled(false);
        menuPrezzi.add(maxItem);
        menuPrezzi.add(comboPrezzoMax);

        // Listener su listaTipi
        comboBoxTipVendita.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String tipo = e.getItem().toString();
                Ricerca.aggiornaPrezzi(comboPrezzoMin, comboPrezzoMax, tipo);
            }
        });

        SwingUtilities.invokeLater(() -> {
            String tipoIniziale = (String) comboBoxTipVendita.getSelectedItem();
            if (tipoIniziale != null) {
                Ricerca.aggiornaPrezzi(comboPrezzoMin, comboPrezzoMax, tipoIniziale);
            }
        });
        Ricerca.aggiornaPrezzi(comboPrezzoMin, comboPrezzoMax, "Vendita");

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
                    if (comboPrezzoMax.isPopupVisible() || comboPrezzoMin.isPopupVisible()) {
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
        comboPrezzoMin.addPopupMenuListener(new PopupMenuListener() {
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

        comboPrezzoMax.addPopupMenuListener(new PopupMenuListener() {
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

        FocusListener focusCloser = new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                Timer timer = new Timer(150, ev -> {
                    Component focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
                    boolean isInsideMenu = SwingUtilities.isDescendingFrom(focusOwner, menuPrezzi);

                    if (!isInsideMenu &&
                            !comboPrezzoMin.isPopupVisible() &&
                            !comboPrezzoMax.isPopupVisible()) {
                        menuPrezzi.setPopupMenuVisible(false);
                    }
                });
                timer.setRepeats(false);
                timer.start();
            }
        };


        comboPrezzoMin.addFocusListener(focusCloser);
        comboPrezzoMax.addFocusListener(focusCloser);
        menuPrezzi.addFocusListener(focusCloser);

    }
    private void setUpOpzioniMenu(JFrame frame, Controller control) {
            backButton.addActionListener(e-> {
            frame.setContentPane(new Ricerca(frame,control,token));

            menuPrezzi.setPopupMenuVisible(false);
            menuPrezzi.setVisible(false);
            frame.revalidate();
            frame.repaint();
        });

    }
    private void setUpComponents() {
        //--Cursor

        for (JButton jButton : Arrays.asList(mapButton, buttonFiltri,backButton)) {
            jButton.addMouseListener(IconClass.cursorButton());
        }
        comboBoxTipVendita.addMouseListener(IconClass.cursorButton());
        menuOpzioni.addMouseListener(IconClass.cursorButton());
        comboBoxSuperfice.addMouseListener(IconClass.cursorButton());

        //--Icon
        backButton.setIcon(IconClass.getBackButton());
        mapButton.setIcon(IconClass.getMapIcon());
        searchButton.setIcon(IconClass.getSearcherIcon());


    }

    private Map<String,Object> filtriEsterni(Map<String, Object> filtri){

        filtri.put("prezzoMax", Ricerca.traduciPrezzo((String) comboPrezzoMax.getSelectedItem()));
        filtri.put("prezzoMin", Ricerca.traduciPrezzo((String) comboPrezzoMin.getSelectedItem()));
        filtri.put("superficie", Ricerca.getSuperficie((String) comboBoxSuperfice.getSelectedItem()));

        if(comboBoxTipVendita.getSelectedItem().equals("Vendita e Affitto")){
            filtri.put("tipoVendita", null);
        }else{
            filtri.put("tipoVendita", comboBoxTipVendita.getSelectedItem());
        }

        return filtri;
    }


    public JPanel getSelectedPanel() {
        return selectedPanel[0];
    }


    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY //GEN-BEGIN:initComponents @formatter:off
        // Generated using JFormDesigner Educational license - Marco Scotto di Uccio Scotto di Uccio (Marco Scotto Di Uccio)
        panelTopBar = new JPanel();
        backButton = new JButton();
        menuBarOpzioni = new JMenuBar();
        menuOpzioni = new JMenu();
        mapButton = new JButton();
        searchButton = new JButton();
        searchField = new JTextField();
        comboBoxTipVendita = new JComboBox<>();
        menuBarPrezziTutto = new JMenuBar();
        menuPrezzi = new JMenu();
        comboBoxSuperfice = new JComboBox<>();
        buttonFiltri = new JButton();
        panelMap = new JPanel();
        panelListaImmobileScroll = new JScrollPane();
        panelListaImmobile = new JPanel();
        panelBottomBar = new JPanel();

        //======== this ========
        setBorder(new BevelBorder(BevelBorder.LOWERED));
        setLayout(new MigLayout(
            "fill,insets 0,hidemode 3,align center center",
            // columns
            "[fill]0" +
            "[139,fill]0" +
            "[117,fill]0" +
            "[fill]0" +
            "[106,grow,center]0" +
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
                "[fill]" +
                "[121,grow]0" +
                "[0,grow]rel" +
                "[158,grow]rel" +
                "[fill]0" +
                "[120,grow,center]rel" +
                "[255,grow,center]para" +
                "[255,grow,center]para" +
                "[fill]",
                // rows
                "[22,grow,fill]0" +
                "[75,grow,top]0" +
                "[]para"));

            //---- backButton ----
            backButton.setIcon(UIManager.getIcon("PasswordField.revealIcon"));
            backButton.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            backButton.setBackground(new Color(0x00ffffff, true));
            backButton.setBorderPainted(false);
            panelTopBar.add(backButton, "cell 0 0,grow");

            //======== menuBarOpzioni ========
            {
                menuBarOpzioni.setBorderPainted(false);
                menuBarOpzioni.setBackground(new Color(0x4e5052));
                menuBarOpzioni.setFont(new Font("Segoe UI", Font.PLAIN, 20));

                //======== menuOpzioni ========
                {
                    menuOpzioni.setText("Opzioni");
                    menuOpzioni.setIcon(UIManager.getIcon("Table.descendingSortIcon"));
                    menuOpzioni.setFont(new Font("Segoe UI", Font.PLAIN, 20));
                    menuOpzioni.setHorizontalAlignment(SwingConstants.CENTER);
                    menuOpzioni.setHorizontalTextPosition(SwingConstants.LEFT);
                }
                menuBarOpzioni.add(menuOpzioni);
            }
            panelTopBar.add(menuBarOpzioni, "cell 5 0 4 1,align right top,grow 0 0");

            //---- mapButton ----
            mapButton.setIcon(UIManager.getIcon("FileChooser.listViewIcon"));
            mapButton.setMaximumSize(new Dimension(34, 35));
            mapButton.setMinimumSize(new Dimension(34, 35));
            mapButton.setBorder(null);
            mapButton.setBorderPainted(false);
            mapButton.setBackground(new Color(0x00ffffff, true));
            panelTopBar.add(mapButton, "cell 0 1,align center center,grow 0 0");

            //---- searchButton ----
            searchButton.setSelectedIcon(UIManager.getIcon("FileChooser.homeFolderIcon"));
            searchButton.setMinimumSize(new Dimension(50, 36));
            searchButton.setMaximumSize(new Dimension(50, 36));
            searchButton.setIcon(UIManager.getIcon("FileChooser.homeFolderIcon"));
            searchButton.setBorder(null);
            searchButton.setBorderPainted(false);
            panelTopBar.add(searchButton, "cell 5 1 3 1,align right center,grow 0 0");

            //---- searchField ----
            searchField.setBackground(Color.white);
            searchField.setFont(new Font("Segoe UI", Font.PLAIN, 24));
            panelTopBar.add(searchField, "cell 1 1 7 1,aligny center,grow 100 0");

            //---- comboBoxTipVendita ----
            comboBoxTipVendita.setModel(new DefaultComboBoxModel<>(new String[] {
                "Vendita e Affitto",
                "Affitto",
                "Vendita"
            }));
            comboBoxTipVendita.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            panelTopBar.add(comboBoxTipVendita, "cell 1 2");

            //======== menuBarPrezziTutto ========
            {
                menuBarPrezziTutto.setBackground(UIManager.getColor("Button.background"));
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
                }
                menuBarPrezziTutto.add(menuPrezzi);
            }
            panelTopBar.add(menuBarPrezziTutto, "cell 3 2");

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
            panelTopBar.add(comboBoxSuperfice, "cell 5 2,align left center,grow 0 0");

            //---- buttonFiltri ----
            buttonFiltri.setText("Filtri");
            buttonFiltri.setIcon(UIManager.getIcon("FileChooser.detailsViewIcon"));
            buttonFiltri.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            panelTopBar.add(buttonFiltri, "cell 6 2,align left center,grow 0 0");
        }
        add(panelTopBar, "north,aligny top,growy 0");

        //======== panelMap ========
        {
            panelMap.setBorder(new LineBorder(UIManager.getColor("Actions.Green"), 2, true));
            panelMap.setLayout(new BorderLayout());
        }
        add(panelMap, "cell 5 0 8 8,grow");

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
        add(panelListaImmobileScroll, "cell 1 0 4 8,grow");

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
                "[48,fill]"));
        }
        add(panelBottomBar, "south");
        // JFormDesigner - End of component initialization //GEN-END:initComponents @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    // Generated using JFormDesigner Educational license - Marco Scotto di Uccio Scotto di Uccio (Marco Scotto Di Uccio)
    private JPanel panelTopBar;
    private JButton backButton;
    private JMenuBar menuBarOpzioni;
    private JMenu menuOpzioni;
    private JButton mapButton;
    private JButton searchButton;
    private JTextField searchField;
    private JComboBox<String> comboBoxTipVendita;
    private JMenuBar menuBarPrezziTutto;
    private JMenu menuPrezzi;
    private JComboBox<String> comboBoxSuperfice;
    private JButton buttonFiltri;
    private JPanel panelMap;
    private JScrollPane panelListaImmobileScroll;
    private JPanel panelListaImmobile;
    private JPanel panelBottomBar;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
