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
import java.time.format.TextStyle;
import java.util.*;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;

import controller.Controller;
import controller.ControllerClienti;
import controller.NotificationClient;
import gui.costum_components.BottoniOpzioni;
import gui.costum_components.IconClass;
import gui.costum_components.maps.MapLoader;
import gui.costum_components.OffertaPannel;
import gui.pagine_administrative.LogInAmministrazione;
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
import org.jxmapviewer.viewer.GeoPosition;

/**
 * @author marco
 */
public class PaginaImmobileDettagliata extends JPanel {
    private static final int GIORNI_METEO = 14;
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

        //Bottoni
        bottoniOpzioni.setUpOpzioniMenu(frame,control,token,menuBarOpzioni,menuOpzioni);

        buttonOfferta.addActionListener(e -> OffertaPannel.setUpPannel(token,frame,immobile,control));

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
                    String mese = day.getMonth().getDisplayName(TextStyle.FULL, locale);

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

                        if (control.getNameLogIn().equals("[CLIENT]")) {
                            ControllerClienti controllerClienti = new ControllerClienti();
                            if (controllerClienti.inviaVisita(visita, token)) {
                                JOptionPane.showMessageDialog(visitaDialog, "Richiesta inviata!");
                                visitaDialog.dispose();
                            } else {
                                JOptionPane.showMessageDialog(visitaDialog, "Errore durante l'invio della richiesta.");
                            }
                        }else{
                            JOptionPane.showMessageDialog(visitaDialog, "Per prenotare una visita bisogna essere un Cliente ");

                        }
                    } else{
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
        areaDescrizione.setFont(IconClass.getFontImmobiliC());
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
        numStanze.setFont(IconClass.getFontImmobiliC());
        paramPanel.add(numStanze);

        JLabel numBagni = new JLabel(immobile.getNumBagni() + " bagni");
        numBagni.setFont(IconClass.getFontImmobiliC());
        paramPanel.add(numBagni);

        JLabel piano = new JLabel("Piano " + (immobile.getPiano()<=0 ? "Terra" : immobile.getPiano()));
        piano.setFont(IconClass.getFontImmobiliC());
        paramPanel.add(piano);

        JLabel supeficie = new JLabel(immobile.getSuperficie() + " m²");
        supeficie.setFont(IconClass.getFontImmobiliC());
        paramPanel.add(supeficie);

        JLabel classeEnergetica = new JLabel("Classe energetica: " + immobile.getClasseEnergetica());
        classeEnergetica.setFont(IconClass.getFontImmobiliC());
        paramPanel.add(classeEnergetica);


        if(immobile.getClimatizzazione()){
            JLabel climatizzazione = new JLabel("Climatizzatore");
            climatizzazione.setFont(IconClass.getFontImmobiliC());
            climatizzazione.setIcon(IconClass.getClimatizzazioneIcon());
            paramPanel.add(climatizzazione);
        }

        if(immobile.isBalcone()){
            JLabel balcone = new JLabel("Balcone");
            balcone.setFont(IconClass.getFontImmobiliC());
            balcone.setIcon(IconClass.getBalconeIcon());
            paramPanel.add(balcone);
        }

        if(immobile.getAscensore()){
            JLabel ascensore = new JLabel("Ascensore");
            ascensore.setFont(IconClass.getFontImmobiliC());
            ascensore.setIcon(IconClass.getAscensoreIcon());
            paramPanel.add(ascensore);
        }

        if(immobile.isPortineria()){
            JLabel portineria = new JLabel("Portineria");
            portineria.setFont(IconClass.getFontImmobiliC());
            portineria.setIcon(IconClass.getPortineriaIcon());
            paramPanel.add(portineria);
        }

        if(immobile.getCantina()){
            JLabel cantina = new JLabel("Cantina");
            cantina.setFont(IconClass.getFontImmobiliC());
            cantina.setIcon(IconClass.getCantinaIcon());
            paramPanel.add(cantina);
        }


        paramPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));
        pannelloContenuto.add(paramPanel);

        pannelloContenuto.add(Box.createRigidArea(new Dimension(0, 30)));

// Grafico
        pannelloContenuto.add(panelChart);
        pannelloContenuto.add(Box.createRigidArea(new Dimension(0, 15)));

// Mappa
        JPanel mappaPanel = new JPanel(new BorderLayout());
        mappaPanel.setPreferredSize(new Dimension(600, 150));
        GeoPosition geoP = new GeoPosition(immobile.getLanIm(),immobile.getLongIm());
        //TODO MapLoder da covert in metodo statico
        new MapLoader(geoP, mappaPanel, List.of(immobile));
        pannelloContenuto.add(mappaPanel);

// ScrollPane principale
        JScrollPane scrollP = new JScrollPane(pannelloContenuto);
        scrollP.getVerticalScrollBar().setUnitIncrement(16); // scorrimento più fluido
        scrollPaneDettagli.add(scrollP, BorderLayout.CENTER);



        if(!token.isEmpty() && control.getNameLogIn().equals("[AGENTE_IMMOBILIARE]")){
            Ricerca.notifiche.addListener(messaggio -> SwingUtilities.invokeLater(() -> {
                NotificationClient.notificheWindow(messaggio);
            }));
        }


    }

    private void destraImg(ActionEvent e) {
        // TODO add your code here
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Educational license - Marco Scotto di Uccio Scotto di Uccio (Marco Scotto Di Uccio)
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
            panelChart.setLayout(new BorderLayout());
        }

        //======== panelImmagineImmobile ========
        {
            panelImmagineImmobile.setBorder(new EtchedBorder(EtchedBorder.RAISED, UIManager.getColor("Actions.Green"), null));
            panelImmagineImmobile.setLayout(new BorderLayout(1, 1));

            //---- buttonDestraImg ----
            buttonDestraImg.setText(">");
            buttonDestraImg.setFont(new Font("Segoe UI", Font.BOLD, 24));
            buttonDestraImg.setBackground(new Color(0x00ffffff, true));
            buttonDestraImg.addActionListener(e -> destraImg(e));
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
    // Generated using JFormDesigner Educational license - Marco Scotto di Uccio Scotto di Uccio (Marco Scotto Di Uccio)
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
