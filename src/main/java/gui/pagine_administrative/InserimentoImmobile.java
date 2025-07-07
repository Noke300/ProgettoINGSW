/*
 * Created by JFormDesigner on Thu Jul 03 23:38:53 CEST 2025
 */

package gui.pagine_administrative;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.formdev.flatlaf.extras.*;
import controller.ControllerAdministration;
import gui.costum_components.AutoCompleteAddress;
import gui.costum_components.IconClass;
import gui.costum_components.MapLoader;
import model.AgenteImmobiliare;
import model.Immobile;
import model.Indirizzo;
import net.miginfocom.swing.*;
import org.jetbrains.annotations.NotNull;
import org.jxmapviewer.viewer.GeoPosition;

/**
 * @author marco
 */

//TODO modificare  il collegamento della pagina per ora in crea azzienda non chiedere
public class InserimentoImmobile extends JPanel {

    IconClass iconClass = new IconClass();
    List<String> base64ListImmagini;
    List<String> listNomeImmagini;

    JCheckBox ascensore = new JCheckBox("Assencore");
    JCheckBox cantina = new JCheckBox("Cantina");
    JCheckBox balcone = new JCheckBox("Balcone");
    JCheckBox climatizzazione = new JCheckBox("Climatizzazione");
    JCheckBox portineria = new JCheckBox("Portineria");

    //TODO da rimovere e refactorare le funzioni al sup interno

    public InserimentoImmobile(JFrame frame, ControllerAdministration controllerA,String token) {
        initComponents();
        base64ListImmagini = new ArrayList<>();
        listNomeImmagini = new ArrayList<>();
        Font inputFont = new Font("SansSerif", Font.PLAIN, 18);
        Font labelFont = new Font("SansSerif", Font.PLAIN, 22);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

// Pannello Indirizzo
        JPanel addressPanel = new JPanel(new GridBagLayout());
        addressPanel.setBorder(BorderFactory.createTitledBorder("Indirizzo"));
        addressPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));
        addressPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

// Indirizzo Completo
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4;
        JLabel fullLabel = new JLabel("Indirizzo completo:");
        fullLabel.setFont(labelFont);
        addressPanel.add(fullLabel, gbc);

        gbc.gridy = 1;
        JTextField fullField = new JTextField();
        fullField.setFont(inputFont);
        fullField.setPreferredSize(new Dimension(800, 35));
        addressPanel.add(fullField, gbc);
        gbc.gridwidth = 1;

//  Via e Numero Civico
        gbc.gridy = 2; gbc.gridx = 0;
        JLabel viaLabel = new JLabel("Via:");
        viaLabel.setFont(labelFont);
        addressPanel.add(viaLabel, gbc);

        gbc.gridx = 1; gbc.weightx = 0.6;
        JTextField viaField = new JTextField();
        viaField.setFont(inputFont);
        addressPanel.add(viaField, gbc);

        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel numeroLabel = new JLabel("Numero civico:");
        numeroLabel.setFont(labelFont);
        addressPanel.add(numeroLabel, gbc);

        gbc.gridx = 3; gbc.weightx = 0.4;
        JTextField numeroField = new JTextField();
        numeroField.setFont(inputFont);
        addressPanel.add(numeroField, gbc);

//  CAP e Città
        gbc.gridy = 3; gbc.gridx = 0;
        JLabel capLabel = new JLabel("CAP:");
        capLabel.setFont(labelFont);
        addressPanel.add(capLabel, gbc);

        gbc.gridx = 1; gbc.weightx = 0.3;
        JTextField capField = new JTextField();
        capField.setFont(inputFont);
        addressPanel.add(capField, gbc);

        gbc.gridx = 2;
        JLabel cittaLabel = new JLabel("Città:");
        cittaLabel.setFont(labelFont);
        addressPanel.add(cittaLabel, gbc);

        gbc.gridx = 3; gbc.weightx = 0.7;
        JTextField cittaField = new JTextField();
        cittaField.setFont(inputFont);
        addressPanel.add(cittaField, gbc);

//  Regione
        Map<String, List<String>> regioneMap = regioniProvinceLabelMap();
        String[] regioni = regioneMap.keySet().toArray(new String[0]);
        gbc.gridy = 4; gbc.gridx = 0;
        JLabel regioneLabel = new JLabel("Regione:");
        regioneLabel.setFont(labelFont);
        addressPanel.add(regioneLabel, gbc);

        gbc.gridx = 1; gbc.gridwidth = 3;
        JComboBox<String> regioneCombo = new JComboBox<>(regioni);
        regioneCombo.setFont(inputFont);
        regioneCombo.setSelectedIndex(0); // ✅ Prima regione selezionata automaticamente
        addressPanel.add(regioneCombo, gbc);

// Provincia
        gbc.gridy = 5; gbc.gridx = 0; gbc.gridwidth = 1;
        JLabel provinciaLabel = new JLabel("Provincia:");
        provinciaLabel.setFont(labelFont);
        addressPanel.add(provinciaLabel, gbc);

        gbc.gridx = 1; gbc.gridwidth = 3;
        JComboBox<String> provinciaCombo = new JComboBox<>();
        provinciaCombo.setFont(inputFont);
        addressPanel.add(provinciaCombo, gbc);

// Popolamento iniziale della provincia
        String regioneIniziale = (String) regioneCombo.getSelectedItem();
        regioneMap.getOrDefault(regioneIniziale, new ArrayList<>())
                .stream()
                .sorted(Comparator.comparing(label -> label.substring(label.indexOf("-") + 2))) // Ordina per nome
                .forEach(provinciaCombo::addItem);

        regioneCombo.addActionListener(e -> {
            String regioneSelezionata = (String) regioneCombo.getSelectedItem();
            List<String> provinceLabels = regioneMap.getOrDefault(regioneSelezionata, new ArrayList<>());

            provinciaCombo.removeAllItems();
            provinceLabels.stream()
                    .sorted(Comparator.comparing(label -> label.substring(label.indexOf("-") + 2)))
                    .forEach(provinciaCombo::addItem);
        });

        mainPanel.add(addressPanel);
        mainPanel.add(Box.createVerticalStrut(10));

        // Immagini
        JPanel imageBarPanel = new JPanel();
        imageBarPanel.setLayout(new BoxLayout(imageBarPanel, BoxLayout.X_AXIS));

        JPanel imageContainer = new JPanel(new BorderLayout());
        imageContainer.setBorder(BorderFactory.createTitledBorder("Anteprima immagini"));
        imageContainer.setPreferredSize(new Dimension(800, 200));
        imageContainer.setAlignmentX(Component.CENTER_ALIGNMENT);
        imageContainer.add(imageBarPanel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.Y_AXIS));
        btnPanel.setPreferredSize(new Dimension(140, 160));
        JButton uploadButton = new JButton("Carica immagine");
        uploadButton.setFont(inputFont);
        btnPanel.add(Box.createVerticalGlue());
        btnPanel.add(uploadButton);
        btnPanel.add(Box.createVerticalGlue());
        imageContainer.add(btnPanel, BorderLayout.EAST);

        JScrollPane horizontalScroll = new JScrollPane(imageContainer);
        horizontalScroll.setPreferredSize(new Dimension(800, 200));
        horizontalScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        horizontalScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        horizontalScroll.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(horizontalScroll);
        mainPanel.add(Box.createVerticalStrut(10));

        //Prezzo e tipo vendita
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        infoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        infoPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel priceLabel = new JLabel("Prezzo immobile:");
        priceLabel.setFont(labelFont);
        JTextField priceField = new JTextField(10);
        priceField.setFont(inputFont);
        priceField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));

        JButton caratteristicheButton = getJButton(frame, inputFont);


        JLabel typeLabel = new JLabel("Tipo Vendita:");
        typeLabel.setFont(labelFont);
        JComboBox<String> tipoVendita = new JComboBox<>(new String[]{"Vendita", "Affitto"});
        tipoVendita.setFont(inputFont);

        JLabel typeImmobileLabel = new JLabel("Tipo Immobile:");
        typeImmobileLabel.setFont(labelFont);
        JComboBox<String> tipoImmobile = new JComboBox<>(new String[]{"Casa", "Appartamento"});
        tipoImmobile.setFont(inputFont);

        infoPanel.add(priceLabel);
        infoPanel.add(priceField);
        infoPanel.add(Box.createHorizontalStrut(20));
        infoPanel.add(typeLabel);
        infoPanel.add(tipoVendita);
        infoPanel.add(Box.createHorizontalStrut(20));
        infoPanel.add(typeImmobileLabel);
        infoPanel.add(tipoImmobile);
        infoPanel.add(Box.createHorizontalStrut(10));
        infoPanel.add(caratteristicheButton);
        infoPanel.add(Box.createHorizontalStrut(10));
        mainPanel.add(infoPanel);

        //Descrizione
        JTextArea descriptionArea = new JTextArea(4, 50);
        descriptionArea.setFont(inputFont);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descriptionScroll = new JScrollPane(descriptionArea);
        descriptionScroll.setBorder(BorderFactory.createTitledBorder("Descrizione immobile"));
        descriptionScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        descriptionScroll.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(descriptionScroll);
        mainPanel.add(Box.createVerticalStrut(15));

        // Dettagli immobile
        JPanel dettagliPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        dettagliPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        dettagliPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        dettagliPanel.setBorder(BorderFactory.createTitledBorder("Dettagli immobile"));

        // Tutti gli elementi in una singola riga
        JSpinner stanzeSpinner = new JSpinner(new SpinnerNumberModel(1, 0, 10, 1));
        JSpinner bagniSpinner = new JSpinner(new SpinnerNumberModel(1, 0, 10, 1));
        JSpinner pianoSpinner = new JSpinner(new SpinnerNumberModel(0, -1, 10, 1));
        stanzeSpinner.setFont(inputFont);
        bagniSpinner.setFont(inputFont);
        pianoSpinner.setFont(inputFont);

        JTextField superficieField = new JTextField(6);
        superficieField.setFont(inputFont);
        superficieField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));

        JComboBox<String> classeEnergeticaCombo = new JComboBox<>(new String[]{"A+", "A", "B", "C", "D", "E", "F", "G"});
        classeEnergeticaCombo.setFont(inputFont);

        JLabel stanzeLabel = new JLabel("Stanze:");
        JLabel bagniLabel = new JLabel("Bagni:");
        JLabel pianoLabel = new JLabel("Piano/i:");
        JLabel superficieLabel = new JLabel("Superficie (m²):");
        JLabel classeLabel = new JLabel("Classe energetica:");
        stanzeLabel.setFont(labelFont);
        bagniLabel.setFont(labelFont);
        pianoLabel.setFont(labelFont);
        superficieLabel.setFont(labelFont);
        classeLabel.setFont(labelFont);

        // Aggiunta di tutti gli elementi in sequenza
        dettagliPanel.add(stanzeLabel);
        dettagliPanel.add(stanzeSpinner);
        dettagliPanel.add(Box.createHorizontalStrut(15));
        dettagliPanel.add(bagniLabel);
        dettagliPanel.add(bagniSpinner);
        dettagliPanel.add(Box.createHorizontalStrut(15));
        dettagliPanel.add(pianoLabel);
        dettagliPanel.add(pianoSpinner);
        dettagliPanel.add(Box.createHorizontalStrut(15));
        dettagliPanel.add(superficieLabel);
        dettagliPanel.add(superficieField);
        dettagliPanel.add(Box.createHorizontalStrut(15));
        dettagliPanel.add(classeLabel);
        dettagliPanel.add(classeEnergeticaCombo);
        dettagliPanel.add(Box.createHorizontalStrut(10));

        mainPanel.add(dettagliPanel);
        mainPanel.add(Box.createVerticalStrut(10));

        //Invio Immobile
        JButton buttonConferma =  new JButton("Conferma");
        buttonConferma.setFont(labelFont);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(buttonConferma);

        fullField.getDocument().addDocumentListener(new DocumentListener() {
            private void aggiornaCampi() {
                boolean isEmpty = fullField.getText().isEmpty();

                viaField.setEnabled(isEmpty);
                cittaField.setEnabled(isEmpty);
                provinciaCombo.setEnabled(isEmpty);
                numeroField.setEnabled(isEmpty);
                capField.setEnabled(isEmpty);
                regioneCombo.setEnabled(isEmpty);

                if (isEmpty) {
                    // Svuoto i campi
                    viaField.setText("");
                    cittaField.setText("");
                    numeroField.setText("");
                    capField.setText("");
                    provinciaCombo.setSelectedIndex(0);
                    regioneCombo.setSelectedIndex(0);
                }
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                aggiornaCampi();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                aggiornaCampi();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                aggiornaCampi(); // Di solito non serve per JTextField, ma lo includiamo per completezza
            }
        });

        DocumentListener altriCampiListener = new DocumentListener() {

            private void aggiornaFullField() {

                boolean tuttiVuoti =
                        viaField.getText().isEmpty() &&
                        cittaField.getText().isEmpty() &&
                        numeroField.getText().isEmpty() &&
                        capField.getText().isEmpty();

                if (fullField.getText().isEmpty()) fullField.setEnabled(tuttiVuoti);
            }

            public void insertUpdate(DocumentEvent e) {
                aggiornaFullField();
            }
            public void removeUpdate(DocumentEvent e) {
                aggiornaFullField();
            }
            public void changedUpdate(DocumentEvent e) {
                aggiornaFullField();
            }
        };

        viaField.getDocument().addDocumentListener(altriCampiListener);
        cittaField.getDocument().addDocumentListener(altriCampiListener);
        numeroField.getDocument().addDocumentListener(altriCampiListener);
        capField.getDocument().addDocumentListener(altriCampiListener);


        buttonConferma.addActionListener(e -> {
            /* TODO possibile modifica di salvamento dati in login ---MapLoder da modificare */
            boolean tuttiCampiCompilati = true;

            if(viaField.getText().isEmpty()) tuttiCampiCompilati = false;
            if(cittaField.getText().isEmpty()) tuttiCampiCompilati = false;
            if(numeroField.getText().isEmpty()) tuttiCampiCompilati = false;
            if(capField.getText().isEmpty()) tuttiCampiCompilati = false;

            if(superficieField.getText().isEmpty()) tuttiCampiCompilati = false;
            if(priceField.getText().isEmpty()) tuttiCampiCompilati = false;
            if(descriptionArea.getText().isEmpty()) tuttiCampiCompilati = false;
            if(base64ListImmagini.isEmpty()) tuttiCampiCompilati = false;

            if(tuttiCampiCompilati){

                AgenteImmobiliare agenteTemp;
                agenteTemp = controllerA.fetchAgenteImmobiliare(token);
                GeoPosition geoP = MapLoader.getCoordinatesFromAddress(fullField.getText());

                assert geoP != null;
                boolean scuoleB = MapLoader.fetchPlaces(geoP,"education.school");
                boolean transportB = MapLoader.fetchPlaces(geoP,"public_transport");
                boolean parchiB = MapLoader.fetchPlaces(geoP,"leisure.park");

                Indirizzo indirizzoT = new Indirizzo(
                        viaField.getText(),
                        cittaField.getText(),
                        String.valueOf(provinciaCombo.getSelectedItem()),
                        Integer.parseInt(numeroField.getText()),
                        Integer.parseInt(capField.getText()),
                        String.valueOf(regioneCombo.getSelectedItem()),
                        geoP.getLatitude(),geoP.getLongitude());
                Immobile immobileTemp = new Immobile(
                        agenteTemp.getAgenziaImmobiliare(),
                        indirizzoT,
                        agenteTemp,
                        transportB,
                        parchiB,
                        scuoleB,
                        false,
                        climatizzazione.isSelected(),
                        ascensore.isSelected(),
                        cantina.isSelected(),
                        portineria.isSelected(),
                        balcone.isSelected(),
                        (int) bagniSpinner.getValue(),
                        (int) pianoSpinner.getValue(),
                        Integer.parseInt(superficieField.getText()),
                        (int)stanzeSpinner.getValue(),
                        Double.parseDouble(priceField.getText()),
                        classeEnergeticaCombo.getSelectedItem().toString(),
                        tipoVendita.getSelectedItem().toString(),
                        descriptionArea.getText(),
                        String.valueOf(tipoImmobile.getSelectedItem()));

              if( controllerA.insertImmobili(immobileTemp,base64ListImmagini,listNomeImmagini,token)){
                  System.out.println("Inserimento immagini sucesso");
              }else {
                  System.out.println("Errore inserimento immagini");
              }

            }else{
                JOptionPane.showMessageDialog(null, "Compilare tutti i campi obbligatori");
            }

        });
        // Logica caricamento immagini
        uploadButton.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                File imgFile = chooser.getSelectedFile();
                ImageIcon icon = new ImageIcon(imgFile.getAbsolutePath());
                Image thumb = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                JPanel cell = new JPanel(new BorderLayout());
                cell.add(new JLabel(new ImageIcon(thumb)), BorderLayout.CENTER);
                cell.add(new JLabel("<html>" + imgFile.getName() + "<br/>" + (imgFile.length() / 1024) + " KB</html>",
                        SwingConstants.CENTER), BorderLayout.SOUTH);
                cell.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                cell.setPreferredSize(new Dimension(200, 150));
                try {
                    String base64Image = imageToBase64(imgFile);
                    base64ListImmagini.add(base64Image);
                    listNomeImmagini.add(imgFile.getName());
                } catch (IOException ex) {
                    System.out.println("Error: " + ex.getMessage());
                    ex.printStackTrace();
                }
                imageBarPanel.add(cell);
                imageBarPanel.revalidate();
                imageBarPanel.repaint();
            }
        });

        AutoCompleteAddress autoCompleteAddress = new AutoCompleteAddress();

        autoCompleteAddress.setupSearchAutocomplete(fullField,viaField,capField,numeroField,cittaField,regioneCombo,provinciaCombo);

        scrollPaneImmobile.setViewportView(mainPanel);
        scrollPaneImmobile.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPaneImmobile.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }

    @NotNull
    private JButton getJButton(JFrame frame, Font inputFont) {
        JButton caratteristicheButton = new JButton("Caratteristiche");
        caratteristicheButton.setFont(inputFont);
        caratteristicheButton.setPreferredSize(new Dimension(180, 40));

        caratteristicheButton.addActionListener(e -> {
            // Crea pannello con le checkbox già presenti nell’istanza
            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(ascensore);
            panel.add(cantina);
            panel.add(balcone);
            panel.add(climatizzazione);
            panel.add(portineria);

            int result = JOptionPane.showConfirmDialog(
                    frame, panel, "Inserisci Caratteristiche",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.CANCEL_OPTION) {
                // Reset manuale su false
                ascensore.setSelected(false);
                cantina.setSelected(false);
                balcone.setSelected(false);
                climatizzazione.setSelected(false);
                portineria.setSelected(false);
            }
        });
        return caratteristicheButton;
    }

    public String imageToBase64(File imageFile) throws IOException {
        byte[] fileContent = Files.readAllBytes(imageFile.toPath());
        return Base64.getEncoder().encodeToString(fileContent);
    }

    //TODO Spostare in classe Util //chiedo scusa

    public static Map<String, List<String>> regioniProvinceLabelMap() {
        Map<String, List<String>> map = new TreeMap<>(); // Ordina le regioni alfabeticamente

        map.put("Abruzzo", Arrays.asList("AQ - L'Aquila", "CH - Chieti", "PE - Pescara", "TE - Teramo"));
        map.put("Basilicata", Arrays.asList("MT - Matera", "PZ - Potenza"));
        map.put("Calabria", Arrays.asList("CS - Cosenza", "CZ - Catanzaro", "KR - Crotone", "RC - Reggio Calabria", "VV - Vibo Valentia"));
        map.put("Campania", Arrays.asList("AV - Avellino", "BN - Benevento", "CE - Caserta", "NA - Napoli", "SA - Salerno"));
        map.put("Emilia-Romagna", Arrays.asList("BO - Bologna", "FE - Ferrara", "FC - Forlì-Cesena", "MO - Modena", "PC - Piacenza", "PR - Parma", "RA - Ravenna", "RE - Reggio Emilia", "RN - Rimini"));
        map.put("Friuli Venezia Giulia", Arrays.asList("GO - Gorizia", "PN - Pordenone", "TS - Trieste", "UD - Udine"));
        map.put("Lazio", Arrays.asList("FR - Frosinone", "LT - Latina", "RI - Rieti", "RM - Roma", "VT - Viterbo"));
        map.put("Liguria", Arrays.asList("GE - Genova", "IM - Imperia", "SP - La Spezia", "SV - Savona"));
        map.put("Lombardia", Arrays.asList("BG - Bergamo", "BS - Brescia", "CO - Como", "CR - Cremona", "LC - Lecco", "LO - Lodi", "MN - Mantova", "MI - Milano", "MB - Monza e Brianza", "PV - Pavia", "SO - Sondrio", "VA - Varese"));
        map.put("Marche", Arrays.asList("AN - Ancona", "AP - Ascoli Piceno", "FM - Fermo", "MC - Macerata", "PU - Pesaro e Urbino"));
        map.put("Molise", Arrays.asList("CB - Campobasso", "IS - Isernia"));
        map.put("Piemonte", Arrays.asList("AL - Alessandria", "AT - Asti", "BI - Biella", "CN - Cuneo", "NO - Novara", "TO - Torino", "VB - Verbano-Cusio-Ossola", "VC - Vercelli"));
        map.put("Puglia", Arrays.asList("BA - Bari", "BT - Barletta-Andria-Trani", "BR - Brindisi", "FG - Foggia", "LE - Lecce", "TA - Taranto"));
        map.put("Sardegna", Arrays.asList("CA - Cagliari", "NU - Nuoro", "OR - Oristano", "SS - Sassari", "SU - Sud Sardegna"));
        map.put("Sicilia", Arrays.asList("AG - Agrigento", "CL - Caltanissetta", "CT - Catania", "EN - Enna", "ME - Messina", "PA - Palermo", "RG - Ragusa", "SR - Siracusa", "TP - Trapani"));
        map.put("Toscana", Arrays.asList("AR - Arezzo", "FI - Firenze", "GR - Grosseto", "LI - Livorno", "LU - Lucca", "MS - Massa-Carrara", "PI - Pisa", "PT - Pistoia", "PO - Prato", "SI - Siena"));
        map.put("Trentino-Alto Adige", Arrays.asList("BZ - Bolzano", "TN - Trento"));
        map.put("Umbria", Arrays.asList("PG - Perugia", "TR - Terni"));
        map.put("Valle d'Aosta", List.of("AO - Aosta"));
        map.put("Veneto", Arrays.asList("BL - Belluno", "PD - Padova", "RO - Rovigo", "TV - Treviso", "VE - Venezia", "VR - Verona", "VI - Vicenza"));

        return map;
    }


    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Evaluation license - Marco Scotto
        scrollPaneImmobile = new JScrollPane();
        panelMenuBarOpzioniColore = new JPanel();
        menuBarOpzioni = new JMenuBar();
        menuOpzioni = new JMenu();
        LogoOpzioni = new JLabel();
        labelTitolo = new JLabel();

        //======== this ========
        setLayout(new MigLayout(
            "fill,hidemode 3,align center center",
            // columns
            "[292,grow,fill]" +
            "[292,grow,fill]" +
            "[292,grow,fill]" +
            "[292,grow,fill]",
            // rows
            "[87,grow,fill]0" +
            "[186,grow,fill]0" +
            "[0,fill]0" +
            "[186,grow,fill]0" +
            "[186,grow,top]0" +
            "[21,grow,fill]0" +
            "[]"));
        add(scrollPaneImmobile, "cell 0 0 4 5");

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
            panelMenuBarOpzioniColore.add(menuBarOpzioni, BorderLayout.EAST);

            //---- LogoOpzioni ----
            LogoOpzioni.setIcon(new FlatSVGIcon("Logo.svg"));
            LogoOpzioni.setIconTextGap(2);
            panelMenuBarOpzioniColore.add(LogoOpzioni, BorderLayout.WEST);

            //---- labelTitolo ----
            labelTitolo.setText("DietiEstates25 ");
            labelTitolo.setFont(new Font("Felix Titling", Font.BOLD | Font.ITALIC, 50));
            labelTitolo.setForeground(UIManager.getColor("Button.background"));
            labelTitolo.setHorizontalAlignment(SwingConstants.CENTER);
            panelMenuBarOpzioniColore.add(labelTitolo, BorderLayout.CENTER);
        }
        add(panelMenuBarOpzioniColore, "north");
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    // Generated using JFormDesigner Evaluation license - Marco Scotto
    private JScrollPane scrollPaneImmobile;
    private JPanel panelMenuBarOpzioniColore;
    private JMenuBar menuBarOpzioni;
    private JMenu menuOpzioni;
    private JLabel LogoOpzioni;
    private JLabel labelTitolo;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
