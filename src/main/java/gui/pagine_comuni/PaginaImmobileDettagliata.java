/*
 * Created by JFormDesigner on Sat Apr 26 03:06:22 CEST 2025
 */

package gui.pagine_comuni;


import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;

import controller.*;
import gui.costum_components.BottoniOpzioni;
import gui.costum_components.IconClass;
import gui.costum_components.VisitaPanel;
import gui.costum_components.maps.MapLoader;
import gui.costum_components.OffertaPannel;
import gui.pagine_administrative.admin.InterfacciaAdmin;
import gui.pagine_administrative.adminSup.InterfacciaAdminSup;
import gui.pagine_administrative.agenteImmobliiare.InterfacciaAgenteImmobiliare;
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
    BottoniOpzioni bottoniOpzioni = new BottoniOpzioni();
    JLabel imageLabel = new JLabel();
    int index = 0;

    //TODO distanzia i bottoni di aggiungi offerta e richiedi visita nella GUI pls

    public PaginaImmobileDettagliata(JFrame frame, ImmobileEImmagini immobileEImmagini, Controller control, String token,Container content) {
        initComponents();

        Immobile immobile = immobileEImmagini.getImmobile();
        List<String> immagini = immobileEImmagini.getImmagini();

        labelPrezzo.setText(immobile.getPrezzo() +" €");
        labelPrezzo.setFont(new Font("Segoe UI", Font.BOLD,28));


        BottoniOpzioni.getMapLog().put(LocalDateTime.now(),"Visualizzato "+immobile.getTipoImmobile()+" in "+ immobile.getTipoVendita()+" : "+ immobile.getIndirizzo().toString());

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
        chartPanel.setBackground(new Color(0, 0, 0, 0));
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
            VisitaPanel.createPanel(token, frame, immobile, control);
        });

        buttonBack.setIcon(IconClass.getBackButton());
        buttonBack.addActionListener(e ->{
          frame.setContentPane(content);
          frame.revalidate();
          frame.repaint();
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
        numStanze.setIcon(IconClass.getNumeroStanzeIcon());
        paramPanel.add(numStanze);

        JLabel numBagni = new JLabel(immobile.getNumBagni() + " bagni");
        numBagni.setFont(IconClass.getFontImmobiliC());
        numBagni.setIcon(IconClass.getNumeroBagniIcon());
        paramPanel.add(numBagni);

        JLabel piano = new JLabel("Piano " + (immobile.getPiano()<=0 ? "Terra" : immobile.getPiano()));
        piano.setFont(IconClass.getFontImmobiliC());
        piano.setIcon(IconClass.getPianiIcon());
        paramPanel.add(piano);

        JLabel supeficie = new JLabel(immobile.getSuperficie() + " m²");
        supeficie.setFont(IconClass.getFontImmobiliC());
        supeficie.setIcon(IconClass.getHomeButtonIcon());
        paramPanel.add(supeficie);

        JLabel classeEnergetica = new JLabel("Classe energetica: " + immobile.getClasseEnergetica());
        classeEnergetica.setFont(IconClass.getFontImmobiliC());
        classeEnergetica.setIcon(IconClass.getClasseEnergeticaIcon());
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
        // Generated using JFormDesigner Evaluation license - Alessio Nesi
        panelTopBar = new JPanel();
        menuBarOpzioni = new JMenuBar();
        menuOpzioni = new JMenu();
        buttonBack = new JButton();
        scrollPaneDettagli = new JPanel();
        labelPrezzo = new JLabel();
        buttonOfferta = new JButton();
        hSpacer1 = new JPanel(null);
        buttonVisita = new JButton();
        labelPrezzoAlto = new JLabel();
        labelPrezzoUltimo = new JLabel();
        panelChart = new JPanel();
        panelImmagineImmobile = new JPanel();
        buttonDestraImg = new JButton();
        buttonSinistraImg = new JButton();

        //======== this ========
        setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder(new javax.swing.
        border.EmptyBorder(0,0,0,0), "JF\u006frmD\u0065sig\u006eer \u0045val\u0075ati\u006fn",javax.swing.border.TitledBorder.CENTER
        ,javax.swing.border.TitledBorder.BOTTOM,new java.awt.Font("Dia\u006cog",java.awt.Font
        .BOLD,12),java.awt.Color.red), getBorder())); addPropertyChangeListener(
        new java.beans.PropertyChangeListener(){@Override public void propertyChange(java.beans.PropertyChangeEvent e){if("\u0062ord\u0065r"
        .equals(e.getPropertyName()))throw new RuntimeException();}});
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
        add(hSpacer1, "cell 4 1");

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
            panelChart.setBorder (new javax. swing. border. CompoundBorder( new javax .swing .border .TitledBorder (new javax. swing
            . border. EmptyBorder( 0, 0, 0, 0) , "JF\u006frmDes\u0069gner \u0045valua\u0074ion", javax. swing. border. TitledBorder
            . CENTER, javax. swing. border. TitledBorder. BOTTOM, new java .awt .Font ("D\u0069alog" ,java .
            awt .Font .BOLD ,12 ), java. awt. Color. red) ,panelChart. getBorder( )) )
            ; panelChart. addPropertyChangeListener (new java. beans. PropertyChangeListener( ){ @Override public void propertyChange (java .beans .PropertyChangeEvent e
            ) {if ("\u0062order" .equals (e .getPropertyName () )) throw new RuntimeException( ); }} )
            ;
            panelChart.setLayout(new BorderLayout());
        }

        //======== panelImmagineImmobile ========
        {
            panelImmagineImmobile.setBorder(new EtchedBorder(EtchedBorder.RAISED, UIManager.getColor("Actions.Green"), null));
            panelImmagineImmobile.setBorder (new javax. swing. border. CompoundBorder( new javax .swing .border .TitledBorder (new javax. swing. border
            . EmptyBorder( 0, 0, 0, 0) , "JF\u006frm\u0044es\u0069gn\u0065r \u0045va\u006cua\u0074io\u006e", javax. swing. border. TitledBorder. CENTER, javax
            . swing. border. TitledBorder. BOTTOM, new java .awt .Font ("D\u0069al\u006fg" ,java .awt .Font .BOLD ,
            12 ), java. awt. Color. red) ,panelImmagineImmobile. getBorder( )) ); panelImmagineImmobile. addPropertyChangeListener (new java. beans
            . PropertyChangeListener( ){ @Override public void propertyChange (java .beans .PropertyChangeEvent e) {if ("\u0062or\u0064er" .equals (e .
            getPropertyName () )) throw new RuntimeException( ); }} );
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
    // Generated using JFormDesigner Evaluation license - Alessio Nesi
    private JPanel panelTopBar;
    private JMenuBar menuBarOpzioni;
    private JMenu menuOpzioni;
    private JButton buttonBack;
    private JPanel scrollPaneDettagli;
    private JLabel labelPrezzo;
    private JButton buttonOfferta;
    private JPanel hSpacer1;
    private JButton buttonVisita;
    private JLabel labelPrezzoAlto;
    private JLabel labelPrezzoUltimo;
    private JPanel panelChart;
    private JPanel panelImmagineImmobile;
    private JButton buttonDestraImg;
    private JButton buttonSinistraImg;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
