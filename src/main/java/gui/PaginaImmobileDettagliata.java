/*
 * Created by JFormDesigner on Sat Apr 26 03:06:22 CEST 2025
 */

package gui;


import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import controller.Controller;
import gui.costum_components.BottoniOpzioni;
import gui.costum_components.IconClass;
import gui.pagine_clienti.LogIn;
import model.Immobile;
import model.ImmobileEImmagini;
import model.Offerta;
import model.Visita;
import net.miginfocom.swing.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author marco
 */
public class PaginaImmobileDettagliata extends JPanel {
    private static final int GIORNI_METEO = 14;
    IconClass iconSet = new IconClass();
    BottoniOpzioni bottoniOpzioni = new BottoniOpzioni();
    JLabel imageLabel = new JLabel();
    int index = 0;

    //TODO distanzia i bottoni di aggiungi offerta e richiedi visita nella GUI pls

    public PaginaImmobileDettagliata(JFrame frame, ImmobileEImmagini immobileEImmagini, Controller control, String token) {
        initComponents();

        Immobile immobile = immobileEImmagini.getImmobile();
        List<String> immagini = immobileEImmagini.getImmagini();

        labelPrezzo.setText(immobile.getPrezzo() +" €");
        labelPrezzo.setFont(new Font("Segoe UI", Font.BOLD,28));

        //TODO da rendere come funzione ausilare che carica i dati nella chiamata (Thread)

        List<Offerta> offerteIM = control.getOffertePerImmobile(immobile);
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        Double maxOfferta = 0.0;

        for (Offerta offerta : offerteIM) {
            if(offerta.getPrezzoOfferta() > maxOfferta) maxOfferta = offerta.getPrezzoOfferta();
            String dataStr = offerta.getDataOfferta().toString();
            dataset.addValue(offerta.getPrezzoOfferta(), "Prezzo", dataStr);
        }

        if (maxOfferta <= 0) labelPrezzoAlto.setText("Non ci sono offerte");
            else labelPrezzoAlto.setText(maxOfferta +" €");
        labelPrezzoAlto.setFont(new Font("Segoe UI", Font.BOLD,28));

        if (maxOfferta <= 0) labelPrezzoUltimo.setText("Non ci sono offerte");
        else labelPrezzoUltimo.setText(offerteIM.getLast().getPrezzoOfferta() +" €");
        labelPrezzoUltimo.setFont(new Font("Segoe UI", Font.BOLD,28));

        //TODO non far modificare le proprietà del grafico
        JFreeChart chart = ChartFactory.createLineChart(
                "Offerte",
                "Data",
                "Prezzo Offerta (€)",
                dataset
        );
        NumberAxis yAxis = (NumberAxis) chart.getCategoryPlot().getRangeAxis();
        yAxis.setLowerBound(150000);
        yAxis.setTickUnit(new NumberTickUnit(20000));

        ChartPanel chartPanel = new ChartPanel(chart);
        panelChart.add(chartPanel, BorderLayout.CENTER);

        CategoryPlot plot = chart.getCategoryPlot();
        LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();

        // Mostra i punti (forme) sui dati
        renderer.setDefaultShapesVisible(true);
        renderer.setDefaultShapesFilled(true);

        try {
            if (!immagini.isEmpty()) {
                // Decodifica la stringa Base64 in un array di byte
                byte[] imageBytes = Base64.getDecoder().decode(immagini.getFirst());

                // Converte l'array di byte in un'immagine
                ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
                Image image = ImageIO.read(bis);
                bis.close();

                Image scaledImage = image.getScaledInstance(600, 450, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaledImage));

                imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
                imageLabel.setVerticalAlignment(SwingConstants.CENTER);

                imageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                panelImmagineImmobile.add(imageLabel, BorderLayout.CENTER);
            } else {
                JLabel errorLabel = new JLabel("Image not found");
                errorLabel.setBackground(Color.RED);
                errorLabel.setOpaque(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        bottoniOpzioni.setUpOpzioniMenu(frame,control,token,menuBarOpzioni,menuOpzioni);
        //Bottoni
        buttonOfferta.addActionListener(e -> {
            if(!"".equals(token)) {
                JDialog offerteDialog = new JDialog(frame, "Proponi prezzo", true);
                offerteDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                offerteDialog.setSize(500, 450);
                offerteDialog.setResizable(false);

// Pannello contenitore
                JPanel contenuto = new JPanel();
                contenuto.setLayout(new BoxLayout(contenuto, BoxLayout.Y_AXIS));
                contenuto.setBorder(BorderFactory.createEmptyBorder(10, 50, 30, 50));

// Etichetta

                JPanel etichettaPanel = new JPanel(new GridLayout(0, 1, 10, 10));

                JLabel etichettaLabel = new JLabel("<html>Proponi un prezzo per la tua offerta che sia entro il limite consentito</html>");
                etichettaLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
                etichettaLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                etichettaPanel.add(etichettaLabel);

                JPanel partenzaPanel = new JPanel(new GridLayout(2, 2, 5, 5));

                JLabel partenzaLabel = new JLabel("Prezzo di partenza");
                partenzaLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
                partenzaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                JLabel prezzoLabel = new JLabel(immobile.getPrezzo() + " €");
                prezzoLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
                prezzoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

                partenzaPanel.add(partenzaLabel);
                partenzaPanel.add(prezzoLabel);

                contenuto.add(etichettaPanel);
                contenuto.add(Box.createRigidArea(new Dimension(0, 10)));

// Campo input
                JLabel prezzoLabel2 = new JLabel("La tua proposta");
                prezzoLabel2.setFont(new Font("SansSerif", Font.BOLD, 16));
                prezzoLabel2.setAlignmentX(Component.LEFT_ALIGNMENT);

                JTextField campoOfferta = new JTextField();
                campoOfferta.setMaximumSize(new Dimension(20, 5));
                campoOfferta.setFont(new Font("SansSerif", Font.PLAIN, 16));
 // Placeholder
                String placeholder = "es: " + (int) (immobile.getPrezzo() * 1.15);
                campoOfferta.setText(placeholder);
                campoOfferta.setForeground(Color.GRAY);

                // Quando entri nel campo, se c'è il placeholder, lo svuoti
                campoOfferta.addFocusListener(new FocusAdapter() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        if (campoOfferta.getText().equals(placeholder)) {
                            campoOfferta.setText("");
                            campoOfferta.setForeground(Color.BLACK);
                        }
                    }

                    @Override
                    public void focusLost(FocusEvent e) {
                        if (campoOfferta.getText().isEmpty()) {
                            campoOfferta.setText(placeholder);
                            campoOfferta.setForeground(Color.GRAY);
                        }
                    }
                });

                partenzaPanel.add(prezzoLabel2);
                partenzaPanel.add(campoOfferta);

                contenuto.add(partenzaPanel);
                contenuto.add(Box.createRigidArea(new Dimension(0, 20)));

                JSeparator separatore = new JSeparator(SwingConstants.HORIZONTAL);
                separatore.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1)); // Larghezza piena, altezza 1px
                separatore.setForeground(Color.LIGHT_GRAY); // Colore del tratto

                contenuto.add(separatore);
                contenuto.add(Box.createRigidArea(new Dimension(0, 10))); // Spazio dopo il divisore



                JPanel spiegazionePanel = new JPanel(new GridLayout(0, 1, 10, 10));

                JLabel speigazioneLabel = new JLabel("<html>é consentito fare un'offerta con una variazione massima del 15% rispetto al prezzo di partenza, al fine di mantenere una coerenza e limitare variazione troppo brusche di prezzo</html>");
                speigazioneLabel.setFont(new Font("SansSerif", Font.ITALIC, 16));
                speigazioneLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
                spiegazionePanel.add(speigazioneLabel);

                contenuto.add(spiegazionePanel);
                contenuto.add(Box.createRigidArea(new Dimension(0, 10)));

// Bottone invia
                JButton inviaButton = new JButton("Invia Offerta");
                inviaButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                inviaButton.setFont(new Font("SansSerif", Font.BOLD, 16));

                // Colore verde per lo sfondo e bianco per il testo
                inviaButton.setBackground(new Color(46, 204, 113)); // Verde brillante
                inviaButton.setForeground(Color.WHITE);

                inviaButton.setOpaque(true);
                inviaButton.setBorderPainted(false);
                inviaButton.setEnabled(false);

                // Listener per controllare i valori
                campoOfferta.getDocument().addDocumentListener(new DocumentListener() {
                    private void checkInput() {
                        try {
                            double valore = Double.parseDouble(campoOfferta.getText().replace(",", "."));
                            inviaButton.setEnabled(valore >= immobile.getPrezzo() * 0.85 && valore <= immobile.getPrezzo() * 1.15);
                        } catch (NumberFormatException ex) {
                            inviaButton.setEnabled(false); // non numero = disabilita
                        }
                    }

                    public void insertUpdate(DocumentEvent e) { checkInput(); }
                    public void removeUpdate(DocumentEvent e) { checkInput(); }
                    public void changedUpdate(DocumentEvent e) { checkInput(); }
                });

// Azione al click
                inviaButton.addActionListener(h -> {
                    //TODO credo che questi ormai siano controlli ridonanti, decidi tu se lasciarli
                    String testo = campoOfferta.getText().trim();
                    if (!testo.isEmpty()) {
                        try {
                            double prezzo = Double.parseDouble(testo);
                            if (prezzo <= 0) {
                                JOptionPane.showMessageDialog(offerteDialog, "Inserisci un valore numerico valido!", "Errore", JOptionPane.ERROR_MESSAGE);
                            }else {
                                Immobile immTemp = new Immobile(immobile.getId());
                                Offerta offerta = new Offerta(immTemp,prezzo);

                                boolean risOfferta=control.inviaOfferta(offerta,token);
                                if(risOfferta){
                                    JOptionPane.showMessageDialog(offerteDialog, "Offerta inviata con successo: €" + prezzo, "Conferma", JOptionPane.INFORMATION_MESSAGE);
                                    offerteDialog.dispose();
                                }else{
                                    JOptionPane.showMessageDialog(offerteDialog, "Invio offerta non riuscito", "Errore", JOptionPane.ERROR_MESSAGE);

                                }
                            }
                            // chiudi il dialog se vuoi
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(offerteDialog, "Inserisci un valore numerico valido!", "Errore", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(offerteDialog, "Il campo non può essere vuoto.", "Attenzione", JOptionPane.WARNING_MESSAGE);
                    }
                });

                contenuto.add(inviaButton);

                contenuto.setFocusable(true);
                contenuto.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        contenuto.requestFocusInWindow();
                    }
                });


// Imposta il pannello come contenuto del dialog
                offerteDialog.setContentPane(contenuto);
                offerteDialog.setLocationRelativeTo(frame); // centra rispetto al frame
                offerteDialog.setVisible(true);

            }else{
                frame.setContentPane(new LogIn(frame,control,token));
                frame.revalidate();
                frame.repaint();
            }
        });

        buttonVisita.addActionListener(e -> {
            if (!"".equals(token)) {
                JDialog visitaDialog = new JDialog(frame, "Visita", true);
                visitaDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                visitaDialog.setSize(700, 550);
                visitaDialog.setResizable(false);
                visitaDialog.setLayout(new BorderLayout());

                Locale locale = new Locale("it", "IT");
                JPanel dayPanel = new JPanel();
                dayPanel.setLayout(new BoxLayout(dayPanel, BoxLayout.X_AXIS));
                dayPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
                ButtonGroup dayGroup = new ButtonGroup();
                Map<JToggleButton, LocalDate> bottoniDataMap = new HashMap<>();

                // Chiamata API meteo
                //TODO metti il sole, la pioggia e le nuvolette pls
                String[] temperatureMax = new String[GIORNI_METEO];
                String[] temperatureMin = new String[GIORNI_METEO];
                try {
                    String endpoint = "https://api.open-meteo.com/v1/forecast?latitude=" + immobile.getLanIm() +
                            "&longitude=" + immobile.getLongIm() + "&daily=temperature_2m_max,temperature_2m_min&timezone=auto&forecast_days=" + GIORNI_METEO;

                    HttpURLConnection conn = (HttpURLConnection) new URL(endpoint).openConnection();
                    conn.setRequestMethod("GET");

                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder responseStr = new StringBuilder();
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        responseStr.append(inputLine);
                    }
                    in.close();

                    JSONObject json = new JSONObject(responseStr.toString());
                    JSONArray tempsMax = json.getJSONObject("daily").getJSONArray("temperature_2m_max");
                    JSONArray tempsMin = json.getJSONObject("daily").getJSONArray("temperature_2m_min");

                    for (int i = 0; i < GIORNI_METEO; i++) {
                        temperatureMax[i] = tempsMax.getDouble(i) + "°C";
                        temperatureMin[i] = tempsMin.getDouble(i) + "°C";
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    for (int i = 0; i < GIORNI_METEO; i++) {
                        temperatureMax[i] = "?°C";
                        temperatureMin[i] = "?°C";
                    }
                }

                // Bottoni con meteo associato
                for (int i = 0; i < GIORNI_METEO; i++) {
                    LocalDate day = LocalDate.now().plusDays(i);
                    String giorno = String.valueOf(day.getDayOfMonth());
                    String mese = day.getMonth().getDisplayName(java.time.format.TextStyle.FULL, locale);

                    JToggleButton dayButton = new JToggleButton("<html><center><b>" + giorno + "</b><br>" + mese + "</center></html>");
                    dayButton.setFont(new Font("Arial", Font.PLAIN, 24));
                    dayGroup.add(dayButton);
                    bottoniDataMap.put(dayButton, day);

                    JLabel meteoLabel = new JLabel(
                            "<html><center><span style='font-size:14px;'>Min: " + temperatureMin[i] + "<br>Max: " + temperatureMax[i] + "</span></center></html>"
                    );
                    meteoLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                    dayButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                    meteoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

                    JPanel giornoPanel = new JPanel();
                    giornoPanel.setLayout(new BoxLayout(giornoPanel, BoxLayout.Y_AXIS));
                    giornoPanel.setOpaque(false);
                    giornoPanel.add(dayButton);
                    giornoPanel.add(Box.createVerticalStrut(5));
                    giornoPanel.add(meteoLabel);

                    dayPanel.add(giornoPanel);
                    dayPanel.add(Box.createHorizontalStrut(10));
                }

                JScrollPane scrollPane = new JScrollPane(dayPanel, ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                scrollPane.setBorder(null);

                // Selezione orario
                String[] orari = {"08:00", "10:00", "12:00", "14:00", "16:00", "18:00"};
                JComboBox<String> oraComboBox = new JComboBox<>(orari);
                oraComboBox.setFont(new Font("Arial", Font.PLAIN, 28));

                // Pannello inferiore
                JPanel bottomPanel = new JPanel(new BorderLayout());
                bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 30, 30));
                bottomPanel.add(oraComboBox, BorderLayout.WEST);

                JButton inviaButton = new JButton("Invia");
                inviaButton.setFont(new Font("Arial", Font.BOLD, 28));
                bottomPanel.add(inviaButton, BorderLayout.EAST);

                inviaButton.addActionListener(h -> {
                    ButtonModel selectedModel = dayGroup.getSelection();
                    LocalDate dataSelezionata = null;

                    for (Map.Entry<JToggleButton, LocalDate> entry : bottoniDataMap.entrySet()) {
                        if (entry.getKey().getModel() == selectedModel) {
                            dataSelezionata = entry.getValue();
                            break;
                        }
                    }

                    if (dataSelezionata != null) {
                        String selected = (String) oraComboBox.getSelectedItem();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                        assert selected != null;
                        LocalTime orario = LocalTime.parse(selected, formatter);

                        Date dataVisita = java.sql.Date.valueOf(dataSelezionata);
                        Visita visita = new Visita(immobile, orario, dataVisita);

                        if (control.inviaVisita(visita,token)) {
                            JOptionPane.showMessageDialog(visitaDialog, "Richiesta inviata!");
                            visitaDialog.dispose();
                        } else {
                            JOptionPane.showMessageDialog(visitaDialog, "Errore durante l'invio della richiesta.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(visitaDialog, "Seleziona una data prima di inviare.");
                    }
                });


                visitaDialog.add(scrollPane, BorderLayout.CENTER);
                visitaDialog.add(bottomPanel, BorderLayout.SOUTH);
                visitaDialog.setLocationRelativeTo(null);
                visitaDialog.setVisible(true);

            } else {
                frame.setContentPane(new LogIn(frame, control, token));
                frame.revalidate();
                frame.repaint();
            }
        });

        buttonBack.addActionListener(e ->{
            //TODO
        });

        JPanel pannelloContenuto = new JPanel();

        buttonDestraImg.addActionListener( e ->{
            try {
                if (!immagini.isEmpty()) {
                    if (index+1 >= immagini.size()) index = -1;
                    // Decodifica la stringa Base64 in un array di byte
                    byte[] imageBytes = Base64.getDecoder().decode(immagini.get(++index));

                    // Converte l'array di byte in un'immagine
                    ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
                    Image image = ImageIO.read(bis);
                    bis.close();

                    Image scaledImage = image.getScaledInstance(600, 450, Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(scaledImage));

                    imageLabel.revalidate();
                    imageLabel.repaint();

                    panelImmagineImmobile.revalidate();
                    panelImmagineImmobile.repaint();
                } else {
                    JLabel errorLabel = new JLabel("Image not found");
                    errorLabel.setOpaque(true);
                    errorLabel.setBackground(Color.RED);
                    panelImmagineImmobile.add(errorLabel, BorderLayout.CENTER);
                    panelImmagineImmobile.revalidate();
                    panelImmagineImmobile.repaint();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        buttonSinistraImg.addActionListener( e ->{
            try {
                if (!immagini.isEmpty()) {
                    if (index-1 < 0) index = immagini.size();
                    // Decodifica la stringa Base64 in un array di byte
                    byte[] imageBytes = Base64.getDecoder().decode(immagini.get(--index));

                    // Converte l'array di byte in un'immagine
                    ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
                    Image image = ImageIO.read(bis);
                    bis.close();

                    Image scaledImage = image.getScaledInstance(600, 450, Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(scaledImage));

                    imageLabel.revalidate();
                    imageLabel.repaint();

                    panelImmagineImmobile.revalidate();
                    panelImmagineImmobile.repaint();
                } else {
                    JLabel errorLabel = new JLabel("Image not found");
                    errorLabel.setOpaque(true);
                    errorLabel.setBackground(Color.RED);
                    panelImmagineImmobile.add(errorLabel, BorderLayout.CENTER);
                    panelImmagineImmobile.revalidate();
                    panelImmagineImmobile.repaint();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Imposta dimensioni preferite ai pannelli chiave
        panelImmagineImmobile.setPreferredSize(new Dimension(600, 450));
        panelImmagineImmobile.setMaximumSize(new Dimension(Integer.MAX_VALUE, 500));

        panelChart.setPreferredSize(new Dimension(600, 350));
        panelChart.setMaximumSize(new Dimension(Integer.MAX_VALUE, 350));

// Crea il contenuto verticale scrollabile
        pannelloContenuto.setLayout(new BoxLayout(pannelloContenuto, BoxLayout.Y_AXIS));
        pannelloContenuto.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

// Agenzia immobiliare e Tipo
        JPanel agenziaPanel = new JPanel(new GridLayout(0, 1, 10, 10));

        JLabel agenziaLabel = new JLabel(immobile.getTipoImmobile() + " in " + immobile.getTipoVendita() + " da: " + immobile.getAgenziaImmobiliare().getNomeAgenzia());
        agenziaLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        agenziaLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        agenziaPanel.add(agenziaLabel);
        pannelloContenuto.add(agenziaPanel);

        pannelloContenuto.add(Box.createRigidArea(new Dimension(0, 10)));
// Immagine immobile
        pannelloContenuto.add(panelImmagineImmobile);
        pannelloContenuto.add(Box.createRigidArea(new Dimension(0, 30)));

// Indirizzo
        JPanel indirizzoPanel = new JPanel(new GridLayout(0, 1, 10, 10));

        JLabel indirizzoLabel1 = new JLabel(immobile.getIndirizzo().toString());
        indirizzoLabel1.setFont(new Font("SansSerif", Font.BOLD, 24));
        indirizzoLabel1.setAlignmentX(Component.LEFT_ALIGNMENT);

        indirizzoPanel.add(indirizzoLabel1);
        pannelloContenuto.add(indirizzoPanel);

        pannelloContenuto.add(Box.createRigidArea(new Dimension(0, 25)));

// Descrizione
        JPanel descrizionePanel = new JPanel(new GridLayout(0, 1, 10, 10));

        JLabel descrizioneLabel = new JLabel("Descrizione:");
        descrizioneLabel.setFont(new Font("SansSerif", Font.BOLD, 20)); // Grassetto e più grande
        descrizioneLabel.setAlignmentX(Component.LEFT_ALIGNMENT); // Allineamento a sinistra (in BoxLayout)
        descrizionePanel.add(descrizioneLabel);
        pannelloContenuto.add(descrizionePanel);

        pannelloContenuto.add(Box.createRigidArea(new Dimension(0, 15)));

        JTextArea areaDescrizione = new JTextArea(10, 30);
        areaDescrizione.setText(immobile.getDescrizione());
        areaDescrizione.setFont(new Font("SansSerif", Font.PLAIN, 16));
        areaDescrizione.setEditable(false);
        areaDescrizione.setOpaque(true);


        areaDescrizione.setLineWrap(true);
        areaDescrizione.setWrapStyleWord(true);
        areaDescrizione.setFocusable(false);
        JScrollPane descrizioneScroll = new JScrollPane(areaDescrizione);

        //permette di scrollare la pagina anche sopra la descrizione
        descrizioneScroll.addMouseWheelListener(e -> {
            JScrollBar bar = descrizioneScroll.getVerticalScrollBar();

            boolean scrollAtTop = bar.getValue() == bar.getMinimum();
            boolean scrollAtBottom = bar.getValue() + bar.getVisibleAmount() == bar.getMaximum();

            if ((e.getWheelRotation() < 0 && scrollAtTop) || (e.getWheelRotation() > 0 && scrollAtBottom)) {
                pannelloContenuto.dispatchEvent(e);
            }
        });


        descrizioneScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 800));
        pannelloContenuto.add(descrizioneScroll);

        pannelloContenuto.add(Box.createRigidArea(new Dimension(0, 30)));

// Parametri
        JPanel caratteristichePanel = new JPanel(new GridLayout(0, 1, 10, 10));

        JLabel caratteristiche = new JLabel("Caratteristiche:");
        caratteristiche.setFont(new Font("SansSerif", Font.BOLD, 20));
        caratteristiche.setAlignmentX(Component.LEFT_ALIGNMENT);
        caratteristichePanel.add(caratteristiche);
        pannelloContenuto.add(caratteristichePanel);

        pannelloContenuto.add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel paramPanel = new JPanel(new GridLayout(0, 3, 10, 10));

        JLabel numStanze = new JLabel(immobile.getNumStanze() + " locali");
        numStanze.setFont(new Font("SansSerif", Font.PLAIN, 18));
        paramPanel.add(numStanze);

        JLabel numBagni = new JLabel(immobile.getNumBagni() + " bagni");
        numBagni.setFont(new Font("SansSerif", Font.PLAIN, 18));
        paramPanel.add(numBagni);

        JLabel piano = new JLabel("Piano " + (immobile.getPiano()<=0 ? "Terra" : immobile.getPiano()));
        piano.setFont(new Font("SansSerif", Font.PLAIN, 18));
        paramPanel.add(piano);

        JLabel supeficie = new JLabel(immobile.getSuperficie() + " m²");
        supeficie.setFont(new Font("SansSerif", Font.PLAIN, 18));
        paramPanel.add(supeficie);

        JLabel classeEnergetica = new JLabel("Classe energetica: " + immobile.getClasseEnergetica());
        classeEnergetica.setFont(new Font("SansSerif", Font.PLAIN, 18));
        paramPanel.add(classeEnergetica);

        //TODO Mettere le icone vcino a questi dettagli
        if(immobile.getClimatizzazione()){
            JLabel climatizzazione = new JLabel("(Icona per froza) " + "Climatizzatore");
            climatizzazione.setFont(new Font("SansSerif", Font.PLAIN, 18));
            paramPanel.add(climatizzazione);
        }

        if(immobile.isBalcone()){
            JLabel balcone = new JLabel("(Icona per froza) " + "Balcone");
            balcone.setFont(new Font("SansSerif", Font.PLAIN, 18));
            paramPanel.add(balcone);
        }

        if(immobile.getAscensore()){
            JLabel ascensore = new JLabel("(Icona per froza) " + "Ascensore");
            ascensore.setFont(new Font("SansSerif", Font.PLAIN, 18));
            paramPanel.add(ascensore);
        }

        if(immobile.isPortineria()){
            JLabel portineria = new JLabel("(Icona per froza) " + "Portineria");
            portineria.setFont(new Font("SansSerif", Font.PLAIN, 18));
            paramPanel.add(portineria);
        }

        if(immobile.getCantina()){
            JLabel cantina = new JLabel("(Icona per froza) " + "Cantina");
            cantina.setFont(new Font("SansSerif", Font.PLAIN, 18));
            paramPanel.add(cantina);
        }


        paramPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
        pannelloContenuto.add(paramPanel);

        pannelloContenuto.add(Box.createRigidArea(new Dimension(0, 30)));

// Grafico
        pannelloContenuto.add(panelChart);
        pannelloContenuto.add(Box.createRigidArea(new Dimension(0, 15)));

        //TODO Mettere la mappa (magari non qua ma in basso a sinistra nello spazio vuoto
// Mappa
        JPanel mappaPanel = new JPanel(new BorderLayout());
        mappaPanel.setPreferredSize(new Dimension(600, 150));
        mappaPanel.add(new JLabel("Qui verrà la mappa"), BorderLayout.CENTER);
        pannelloContenuto.add(mappaPanel);

// ScrollPane principale
        JScrollPane scrollP = new JScrollPane(pannelloContenuto);
        scrollP.getVerticalScrollBar().setUnitIncrement(16); // scorrimento più fluido
        scrollPaneDettagli.add(scrollP, BorderLayout.CENTER);

    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Evaluation license - Alessio Nesi
        panelTopBar = new JPanel();
        menuBarOpzioni = new JMenuBar();
        menuOpzioni = new JMenu();
        buttonBack = new JButton();
        scrollPaneDettagli = new JPanel();
        labelPrezzo = new JLabel();
        buttonOfferta = new JButton();
        buttonVisita = new JButton();
        labelPrezzoAlto = new JLabel();
        labelPrezzoUltimo = new JLabel();
        panelChart = new JPanel();
        panelImmagineImmobile = new JPanel();
        buttonDestraImg = new JButton();
        buttonSinistraImg = new JButton();

        //======== this ========
        setBorder (new javax. swing. border. CompoundBorder( new javax .swing .border .TitledBorder (new javax. swing. border. EmptyBorder(
        0, 0, 0, 0) , "JF\u006frmDes\u0069gner \u0045valua\u0074ion", javax. swing. border. TitledBorder. CENTER, javax. swing. border. TitledBorder
        . BOTTOM, new java .awt .Font ("D\u0069alog" ,java .awt .Font .BOLD ,12 ), java. awt. Color.
        red) , getBorder( )) );  addPropertyChangeListener (new java. beans. PropertyChangeListener( ){ @Override public void propertyChange (java .
        beans .PropertyChangeEvent e) {if ("\u0062order" .equals (e .getPropertyName () )) throw new RuntimeException( ); }} );
        setLayout(new MigLayout(
            "fill,hidemode 3",
            // columns
            "[188,fill]" +
            "[174,center]" +
            "[174,center]" +
            "[174,center]" +
            "[174,center]",
            // rows
            "rel[452]rel" +
            "[452]" +
            "[452]" +
            "[452]" +
            "[452,grow]" +
            "[452]0" +
            "[106,grow]"));

        //======== panelTopBar ========
        {
            panelTopBar.setBorder(new LineBorder(Color.black, 2, true));
            panelTopBar.setBackground(UIManager.getColor("Actions.Green"));
            panelTopBar.setLayout(new BorderLayout());

            //======== menuBarOpzioni ========
            {
                menuBarOpzioni.setBorderPainted(false);
                menuBarOpzioni.setBackground(new Color(0x4e5052));
                menuBarOpzioni.setFont(new Font("Segoe UI", Font.PLAIN, 26));

                //======== menuOpzioni ========
                {
                    menuOpzioni.setText("Opzioni");
                    menuOpzioni.setIcon(UIManager.getIcon("Table.descendingSortIcon"));
                    menuOpzioni.setFont(new Font("Segoe UI", Font.PLAIN, 26));
                }
                menuBarOpzioni.add(menuOpzioni);
            }
            panelTopBar.add(menuBarOpzioni, BorderLayout.LINE_END);

            //---- buttonBack ----
            buttonBack.setText("Back");
            buttonBack.setFont(new Font("Segoe UI", Font.PLAIN, 24));
            buttonBack.setIcon(UIManager.getIcon("Tree.openIcon"));
            buttonBack.setBackground(UIManager.getColor("Actions.Green"));
            buttonBack.setBorder(new EtchedBorder(EtchedBorder.RAISED));
            buttonBack.setBorderPainted(false);
            panelTopBar.add(buttonBack, BorderLayout.LINE_START);
        }
        add(panelTopBar, "north");

        //======== scrollPaneDettagli ========
        {
            scrollPaneDettagli.setLayout(new BorderLayout());
        }
        add(scrollPaneDettagli, "cell 0 0 4 6,grow");

        //---- labelPrezzo ----
        labelPrezzo.setText("Prezzob");
        labelPrezzo.setBorder(new TitledBorder(null, "Prezzo", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION,
            new Font("Segoe UI", Font.ITALIC, 18)));
        labelPrezzo.setHorizontalTextPosition(SwingConstants.CENTER);
        labelPrezzo.setHorizontalAlignment(SwingConstants.CENTER);
        add(labelPrezzo, "cell 4 0,grow");

        //---- buttonOfferta ----
        buttonOfferta.setText("Proponi prezzo");
        buttonOfferta.setFont(buttonOfferta.getFont().deriveFont(buttonOfferta.getFont().getStyle() & ~Font.BOLD, buttonOfferta.getFont().getSize() + 5f));
        add(buttonOfferta, "cell 4 1");

        //---- buttonVisita ----
        buttonVisita.setText("Richiedi Visita");
        buttonVisita.setFont(buttonVisita.getFont().deriveFont(buttonVisita.getFont().getSize() + 5f));
        add(buttonVisita, "cell 4 1");

        //---- labelPrezzoAlto ----
        labelPrezzoAlto.setText("prezzoAlto");
        labelPrezzoAlto.setBorder(new TitledBorder(null, "Offerta pi\u00f9 alta", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION,
            new Font("Segoe UI", Font.ITALIC, 18)));
        labelPrezzoAlto.setHorizontalTextPosition(SwingConstants.CENTER);
        labelPrezzoAlto.setHorizontalAlignment(SwingConstants.CENTER);
        add(labelPrezzoAlto, "cell 4 2,grow");

        //---- labelPrezzoUltimo ----
        labelPrezzoUltimo.setText("prezzoUltimo");
        labelPrezzoUltimo.setBorder(new TitledBorder(null, "Ultima Offerta fatta", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION,
            new Font("Segoe UI", Font.ITALIC, 18)));
        labelPrezzoUltimo.setHorizontalTextPosition(SwingConstants.CENTER);
        labelPrezzoUltimo.setHorizontalAlignment(SwingConstants.CENTER);
        add(labelPrezzoUltimo, "cell 4 3,grow");

        //======== panelChart ========
        {
            panelChart.setBorder (new javax. swing. border. CompoundBorder( new javax .swing .border .TitledBorder (new javax. swing. border.
            EmptyBorder( 0, 0, 0, 0) , "JF\u006frmDes\u0069gner \u0045valua\u0074ion", javax. swing. border. TitledBorder. CENTER, javax. swing
            . border. TitledBorder. BOTTOM, new java .awt .Font ("D\u0069alog" ,java .awt .Font .BOLD ,12 ),
            java. awt. Color. red) ,panelChart. getBorder( )) ); panelChart. addPropertyChangeListener (new java. beans. PropertyChangeListener( )
            { @Override public void propertyChange (java .beans .PropertyChangeEvent e) {if ("\u0062order" .equals (e .getPropertyName () ))
            throw new RuntimeException( ); }} );
            panelChart.setLayout(new BorderLayout());
        }

        //======== panelImmagineImmobile ========
        {
            panelImmagineImmobile.setBorder(new EtchedBorder(EtchedBorder.RAISED, UIManager.getColor("Actions.Green"), null));
            panelImmagineImmobile.setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder(new javax.swing.border
            .EmptyBorder(0,0,0,0), "JF\u006frmDesi\u0067ner Ev\u0061luatio\u006e",javax.swing.border.TitledBorder.CENTER,javax
            .swing.border.TitledBorder.BOTTOM,new java.awt.Font("Dialo\u0067",java.awt.Font.BOLD,
            12),java.awt.Color.red),panelImmagineImmobile. getBorder()));panelImmagineImmobile. addPropertyChangeListener(new java.beans
            .PropertyChangeListener(){@Override public void propertyChange(java.beans.PropertyChangeEvent e){if("borde\u0072".equals(e.
            getPropertyName()))throw new RuntimeException();}});
            panelImmagineImmobile.setLayout(new BorderLayout(1, 1));

            //---- buttonDestraImg ----
            buttonDestraImg.setText(">");
            buttonDestraImg.setFont(new Font("Segoe UI", Font.BOLD, 24));
            buttonDestraImg.setBackground(new Color(0x00ffffff, true));
            panelImmagineImmobile.add(buttonDestraImg, BorderLayout.EAST);

            //---- buttonSinistraImg ----
            buttonSinistraImg.setText("<");
            buttonSinistraImg.setFont(new Font("Segoe UI", Font.BOLD, 24));
            buttonSinistraImg.setBackground(new Color(0x00ffffff, true));
            panelImmagineImmobile.add(buttonSinistraImg, BorderLayout.WEST);
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY //GEN-BEGIN:variables @formatter:off
    // Generated using JFormDesigner Evaluation license - Alessio Nesi
    private JPanel panelTopBar;
    private JMenuBar menuBarOpzioni;
    private JMenu menuOpzioni;
    private JButton buttonBack;
    private JPanel scrollPaneDettagli;
    private JLabel labelPrezzo;
    private JButton buttonOfferta;
    private JButton buttonVisita;
    private JLabel labelPrezzoAlto;
    private JLabel labelPrezzoUltimo;
    private JPanel panelChart;
    private JPanel panelImmagineImmobile;
    private JButton buttonDestraImg;
    private JButton buttonSinistraImg;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
