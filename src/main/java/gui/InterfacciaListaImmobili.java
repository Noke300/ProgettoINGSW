/*
 * Created by JFormDesigner on Tue Apr 22 11:59:05 CEST 2025
 */

package gui;

import controller.Controller;
import controller.ControllerClienti;
import gui.costum_components.*;
import model.Immobile;
import model.Indirizzo;
import net.miginfocom.swing.MigLayout;
import org.jxmapviewer.viewer.GeoPosition;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author marco
 */
public class InterfacciaListaImmobili extends JPanel {
    private static final IconClass iconSet = new IconClass();
    private final ImmobilePanelSingolo  immobilePanelSingolo = new ImmobilePanelSingolo();
    String token;

    private JPanel selectedPanel = null;

    //--Border per gli immobili selected


//Rimuove i popup quando la pagina viene caricata
    @Override
    public void addNotify() {
        super.addNotify();
        // Assicurati che tutti i menu popup siano nascosti quando il pannello viene aggiunto
        MenuSelectionManager.defaultManager().clearSelectedPath();
    }

    public InterfacciaListaImmobili(JFrame frame, List<JCheckBox> checkBoxesList, BottoniOpzioni bottoniOpzioni, GeoPosition geoPosition, Controller control, String token, JPanel internalFrameFiltri, List<Immobile> immobili) {
        this.token=token;
        initComponents();
        bottoniOpzioni.setUpOpzioniMenu(frame,control,this.token,menuBarOpzioni,menuOpzioni);
        internalFrameFiltri.setVisible(false);
        setUpOpzioniMenu(frame,control);
        setUpComponents();

        searchButton.addActionListener(e -> {
            if(!searchField.getText().isEmpty()) {
                panelMap.removeAll();
                new MapLoader(Objects.requireNonNull(MapLoader.getCoordinatesFromAddress(searchField.getText())), checkBoxesList, panelMap,immobili);
                panelMap.revalidate();
                panelMap.repaint();
            }else{
                JOptionPane.showMessageDialog(frame,
                        "Per favore, inserisci un indirizzo.",
                        "Nessun indirizzo inserito", JOptionPane.WARNING_MESSAGE);
            }

        });
        //--- 7. Filtri  ---
        JButton buttonConfermaFiltri = new JButton("Conferma");
        internalFrameFiltri.remove(21);
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

        new MapLoader(geoPosition,checkBoxesList,panelMap,immobili);
        // Create and add the first panel

        // In the initComponents method, replace the GridLayout with MigLayout
        panelListaImmobile.setLayout(new MigLayout("wrap 1, align center")); // One component per row

        Dimension dim  = new Dimension(820,289);
        for (Immobile immobile : immobili) {
            JPanel panel = immobilePanelSingolo.createImmobileSingoloPanel(frame,immobile,control,token,selectedPanel);
            panel.setMaximumSize(dim);
            panel.setPreferredSize(dim);
            panelListaImmobile.add(panel);
        }

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

            SimplifiedMapLoader.createClickableMap(actualMapDisplayPanel, initialPos, 10, event -> {
                SwingUtilities.invokeLater(() -> { // Assicurati che l'aggiornamento della UI sia sull'EDT
                    if (event.getAddress() != null) {
                        selectedAddressField.setText(event.getAddress());
                    } else {
                        selectedAddressField.setText("Indirizzo non valido o fuori confine Italiano");
                    }
                });
            });

            mapDialogInstance.setLocationRelativeTo(frame);
            mapDialogInstance.setVisible(true); // Mostra il dialogo
        });

    }
    private void setUpOpzioniMenu(JFrame frame, Controller control) {
            ricercaButton.addActionListener(e-> {
            frame.setContentPane(new Ricerca().createRicercaPanel(frame,control,token));
            frame.revalidate();
            frame.repaint();
        });

    }
    private void setUpComponents() {
        //--Cursor

        for (JButton jButton : Arrays.asList(mapButton, buttonFiltri)) {
            jButton.addMouseListener(iconSet.cursorButton());
        }
        listaTipi.addMouseListener(iconSet.cursorButton());
        menuOpzioni.addMouseListener(iconSet.cursorButton());
        comboBoxSuperfice.addMouseListener(iconSet.cursorButton());

        //--Icon
        ricercaButton.setIcon(iconSet.getSearcherIcon());
        mapButton.setIcon(iconSet.getMapIcon());
        searchButton.setIcon(iconSet.getSearcherIcon());

    }


    public JPanel getSelectedPanel() {
        return selectedPanel;
    }


    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY //GEN-BEGIN:initComponents @formatter:off
        // Generated using JFormDesigner Evaluation license - Marco Scotto
        panelTopBar = new JPanel();
        ricercaButton = new JButton();
        menuBarOpzioni = new JMenuBar();
        menuOpzioni = new JMenu();
        mapButton = new JButton();
        searchButton = new JButton();
        searchField = new JTextField();
        buttonFiltri = new JButton();
        listaTipi = new JComboBox<>();
        menuBarContenitorePrezziBox = new JMenuBar();
        menuPrezzi = new JMenu();
        comboBoxPrezzoMax = new JComboBox<>();
        comboBoxPrezzoMinimo = new JComboBox<>();
        comboBoxSuperfice = new JComboBox<>();
        panelMap = new JPanel();
        panelListaImmobileScroll = new JScrollPane();
        panelListaImmobile = new JPanel();
        panelBottomBar = new JPanel();
        buttonAggiungImmobile = new JButton();
        buttonAggiungiOfferta = new JButton();

        //======== this ========
        setBorder(new BevelBorder(BevelBorder.LOWERED));
        setBorder (new javax. swing. border. CompoundBorder( new javax .swing .border .TitledBorder (new
        javax. swing. border. EmptyBorder( 0, 0, 0, 0) , "JFor\u006dDesi\u0067ner \u0045valu\u0061tion", javax
        . swing. border. TitledBorder. CENTER, javax. swing. border. TitledBorder. BOTTOM, new java
        .awt .Font ("Dia\u006cog" ,java .awt .Font .BOLD ,12 ), java. awt
        . Color. red) , getBorder( )) );  addPropertyChangeListener (new java. beans.
        PropertyChangeListener( ){ @Override public void propertyChange (java .beans .PropertyChangeEvent e) {if ("bord\u0065r" .
        equals (e .getPropertyName () )) throw new RuntimeException( ); }} );
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
                "[159,grow]rel" +
                "[96,grow]rel" +
                "[123,grow]rel" +
                "[fill]0" +
                "[255,grow,center]0" +
                "[255,grow,center]para" +
                "[fill]",
                // rows
                "[22,grow,fill]0" +
                "[75,grow,top]0" +
                "[]para"));

            //---- ricercaButton ----
            ricercaButton.setIcon(UIManager.getIcon("PasswordField.revealIcon"));
            ricercaButton.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            panelTopBar.add(ricercaButton, "cell 0 0,grow");

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
            panelTopBar.add(menuBarOpzioni, "cell 5 0 3 1,align right top,grow 0 0");

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
            panelTopBar.add(searchButton, "cell 5 1 2 1,align right center,grow 0 0");

            //---- searchField ----
            searchField.setBackground(Color.white);
            searchField.setFont(new Font("Segoe UI", Font.PLAIN, 24));
            panelTopBar.add(searchField, "cell 1 1 6 1,aligny center,grow 100 0");

            //---- buttonFiltri ----
            buttonFiltri.setText("Filtri");
            buttonFiltri.setIcon(UIManager.getIcon("FileChooser.detailsViewIcon"));
            buttonFiltri.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            panelTopBar.add(buttonFiltri, "cell 1 2,align left center,grow 0 0");

            //---- listaTipi ----
            listaTipi.setModel(new DefaultComboBoxModel<>(new String[] {
                "Casa",
                "Appartamento"
            }));
            listaTipi.setSelectedIndex(0);
            listaTipi.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            panelTopBar.add(listaTipi, "cell 2 2,align left center,grow 0 0");

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
            panelTopBar.add(menuBarContenitorePrezziBox, "cell 3 2,align left center,grow 0 0");

            //---- comboBoxSuperfice ----
            comboBoxSuperfice.setModel(new DefaultComboBoxModel<>(new String[] {
                "Superfice"
            }));
            comboBoxSuperfice.setForeground(Color.black);
            comboBoxSuperfice.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            panelTopBar.add(comboBoxSuperfice, "cell 5 2,align left center,grow 0 0");
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
                "[shrink 0]" +
                "[48,fill]"));

            //---- buttonAggiungImmobile ----
            buttonAggiungImmobile.setIcon(UIManager.getIcon("FileChooser.homeFolderIcon"));
            buttonAggiungImmobile.setBackground(UIManager.getColor("Actions.Green"));
            buttonAggiungImmobile.setText("Aggiungi Immobile");
            panelBottomBar.add(buttonAggiungImmobile, "cell 0 1,alignx left,grow 0 100");

            //---- buttonAggiungiOfferta ----
            buttonAggiungiOfferta.setText("Aggiungi Offerta");
            buttonAggiungiOfferta.setBorderPainted(false);
            buttonAggiungiOfferta.setBackground(UIManager.getColor("Actions.Green"));
            panelBottomBar.add(buttonAggiungiOfferta, "cell 3 0 1 2,aligny center,growy 0");
        }
        add(panelBottomBar, "south");
        // JFormDesigner - End of component initialization //GEN-END:initComponents @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    // Generated using JFormDesigner Evaluation license - Marco Scotto
    private JPanel panelTopBar;
    private JButton ricercaButton;
    private JMenuBar menuBarOpzioni;
    private JMenu menuOpzioni;
    private JButton mapButton;
    private JButton searchButton;
    private JTextField searchField;
    private JButton buttonFiltri;
    private JComboBox<String> listaTipi;
    private JMenuBar menuBarContenitorePrezziBox;
    private JMenu menuPrezzi;
    private JComboBox<String> comboBoxPrezzoMax;
    private JComboBox<String> comboBoxPrezzoMinimo;
    private JComboBox<String> comboBoxSuperfice;
    private JPanel panelMap;
    private JScrollPane panelListaImmobileScroll;
    private JPanel panelListaImmobile;
    private JPanel panelBottomBar;
    private JButton buttonAggiungImmobile;
    private JButton buttonAggiungiOfferta;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
