/*
 * Created by JFormDesigner on Thu Jul 03 23:38:53 CEST 2025
 */

package gui.pagine_administrative;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import com.formdev.flatlaf.extras.*;
import controller.ControllerAdministration;
import gui.costum_components.AutoCompleteAddress;
import gui.costum_components.IconClass;
import gui.costum_components.MapLoader;
import model.AgenteImmobiliare;
import model.Immobile;
import model.Indirizzo;
import net.miginfocom.swing.*;
import org.jxmapviewer.viewer.GeoPosition;

/**
 * @author marco
 */

//TODO modificare  il collegamento della pagina per ora in crea azzienda non chiedere
public class InserimentoImmobile extends JPanel {

    IconClass iconClass = new IconClass();
    List<String> base64ListImmagini;
    List<String> listNomeImmagini;
    //TODO da rimovere e refactorare le funzioni al sup interno

    public InserimentoImmobile(JFrame frame, ControllerAdministration controllerA,String token) {
        initComponents();
        Font inputFont = new Font("SansSerif", Font.PLAIN, 24);
        Font labelFont = new Font("SansSerif", Font.PLAIN, 18);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        //Indirizzo
        JPanel addressPanel = new JPanel(new GridBagLayout());
        addressPanel.setBorder(BorderFactory.createTitledBorder("Indirizzo"));
        addressPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 170));
        addressPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Indirizzo completo
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4;
        JLabel fullLabel = new JLabel("Indirizzo completo:");
        fullLabel.setFont(labelFont);
        addressPanel.add(fullLabel, gbc);

        gbc.gridy = 1;
        JTextField fullField = new JTextField();
        fullField.setFont(inputFont);
        fullField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        fullField.setPreferredSize(new Dimension(800, 35));
        addressPanel.add(fullField, gbc);
        gbc.gridwidth = 1;

        // Seconda riga: Via e Numero civico con pesi bilanciati
        gbc.gridy = 2; gbc.gridx = 0; gbc.weightx = 0.0;
        JLabel viaLabel = new JLabel("Via:");
        viaLabel.setFont(labelFont);
        addressPanel.add(viaLabel, gbc);

        gbc.gridx = 1; gbc.weightx = 0.6; // Via prende più spazio
        JTextField viaField = new JTextField();
        viaField.setFont(inputFont);
        viaField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        addressPanel.add(viaField, gbc);

        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel numeroLabel = new JLabel("Numero civico:");
        numeroLabel.setFont(labelFont);
        addressPanel.add(numeroLabel, gbc);

        gbc.gridx = 3; gbc.weightx = 0.4; // Numero civico prende meno spazio
        JTextField numeroField = new JTextField();
        numeroField.setFont(inputFont);
        numeroField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        addressPanel.add(numeroField, gbc);

        // Terza riga: CAP e Città con pesi bilanciati
        gbc.gridy = 3; gbc.gridx = 0; gbc.weightx = 0.0;
        JLabel capLabel = new JLabel("CAP:");
        capLabel.setFont(labelFont);
        addressPanel.add(capLabel, gbc);

        gbc.gridx = 1; gbc.weightx = 0.3; // CAP prende meno spazio
        JTextField capField = new JTextField();
        capField.setFont(inputFont);
        capField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        addressPanel.add(capField, gbc);

        gbc.gridx = 2; gbc.weightx = 0.0;
        JLabel cittaLabel = new JLabel("Città:");
        cittaLabel.setFont(labelFont);
        addressPanel.add(cittaLabel, gbc);

        gbc.gridx = 3; gbc.weightx = 0.7; // Città prende più spazio
        JTextField cittaField = new JTextField();
        cittaField.setFont(inputFont);
        cittaField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        addressPanel.add(cittaField, gbc);

        // Quarta riga: Regione centrata
        gbc.gridy = 4; gbc.gridx = 0; gbc.weightx = 0.0;
        JLabel regioneLabel = new JLabel("Regione:");
        regioneLabel.setFont(labelFont);
        addressPanel.add(regioneLabel, gbc);

        gbc.gridx = 1; gbc.gridwidth = 3; gbc.weightx = 1.0;
        JTextField regioneField = new JTextField();
        regioneField.setFont(inputFont);
        regioneField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        addressPanel.add(regioneField, gbc);

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

        JButton caratteristicheButton = new JButton("Caratteristiche");
        caratteristicheButton.setFont(inputFont);
        caratteristicheButton.setPreferredSize(new Dimension(180, 40));

        caratteristicheButton.addActionListener(e -> {

            //TODO JDialog panel per liserimento dei campi booleani
        });

        JLabel typeLabel = new JLabel("Tipo:");
        typeLabel.setFont(labelFont);
        JComboBox<String> tipoBox = new JComboBox<>(new String[]{"Vendita", "Affitto"});
        tipoBox.setFont(inputFont);

        infoPanel.add(priceLabel);
        infoPanel.add(priceField);
        infoPanel.add(Box.createHorizontalStrut(20));
        infoPanel.add(typeLabel);
        infoPanel.add(tipoBox);
        infoPanel.add(Box.createHorizontalStrut(20));
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

        buttonConferma.addActionListener(e -> {
            /* Todo chiamate api per i campi parchi trasporti scuole---campi boolean da definire
                possibile modifica di salvamento dati in login ---MapLoder da modificare */
            AgenteImmobiliare agenteTemp;
            agenteTemp = controllerA.fetchAgenteImmobiliare(token);
            GeoPosition geoP = MapLoader.getCoordinatesFromAddress(fullField.getText());
            Indirizzo indirizzoT = new Indirizzo(viaField.getText(),cittaField.getText(),"NA",Integer.valueOf(numeroField.getText()),Integer.valueOf(capField.getText()),"Campania",geoP.getLatitude(),geoP.getLongitude());
            Immobile immobileTemp = new Immobile(agenteTemp.getAgenziaImmobiliare(),indirizzoT,agenteTemp,false,false,true,false,true,true,false,false,true,bagniSpinner.getX(), pianoSpinner.getX(),Integer.valueOf(superficieField.getText()),stanzeSpinner.getX(),Double.valueOf(priceField.getText()),classeEnergeticaCombo.getSelectedItem().toString(),tipoBox.getSelectedItem().toString(),descriptionArea.getText(),"Casa");

          if( controllerA.insertImmobili(immobileTemp,base64ListImmagini,listNomeImmagini,token)){
              System.out.println("Inserimento immagini sucesso");
          }else {
              System.out.println("Erro ao inserir immagini");
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
        autoCompleteAddress.setupSearchAutocomplete(fullField,viaField,capField,numeroField,cittaField);

        scrollPaneImmobile.setViewportView(mainPanel);
        scrollPaneImmobile.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPaneImmobile.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }

    public String imageToBase64(File imageFile) throws IOException {
        byte[] fileContent = Files.readAllBytes(imageFile.toPath());
        return Base64.getEncoder().encodeToString(fileContent);
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
