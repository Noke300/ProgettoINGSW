/*
 * Created by JFormDesigner on Sat Apr 26 03:06:22 CEST 2025
 */

package gui;

import java.awt.*;
import java.net.URL;
import javax.swing.*;
import javax.swing.border.*;

import controller.Controller;
import controller.ControllerClienti;
import gui.costum_components.BottoniOpzioni;
import model.Immobile;
import net.miginfocom.swing.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * @author marco
 */
public class PaginaImmobileDettagliata extends JPanel {
    BottoniOpzioni bottoniOpzioni = new BottoniOpzioni();
    public PaginaImmobileDettagliata(JFrame frame, Immobile immobile, Controller control, String token) {
        initComponents();

        //TODO da rendere come funzione ausilare che carica i dati nella chiamata (Thread)
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(200, "Marco", "January");
        dataset.addValue(100, "Alessio", "January");
        dataset.addValue(500, "Simone", "January");


        JFreeChart chart = ChartFactory.createLineChart(
                "Offerte Immobile",
                "Valore",
                "Tempo",
                dataset);

        ChartPanel chartPanel = new ChartPanel(chart);
        panelChart.add(chartPanel, BorderLayout.CENTER);

        try {
            URL imageUrl = getClass().getResource("/CasaProva3.jpeg");
            if (imageUrl != null) {
                ImageIcon icon = new ImageIcon(imageUrl);
                JLabel imageLabel = new JLabel(icon);
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
        
        //TODO migrare alla classe ausiliare per le opzioni
         bottoniOpzioni.setUpOpzioniMenu(frame,control,token,menuBarOpzioni,menuOpzioni);
        //Bottoni
        buttonOfferta.addActionListener(e -> {
            if(token !="") {
                final JDialog offerteDialog = new JDialog(frame, "Offerta", true);
                offerteDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                offerteDialog.setSize(700, 550);
                offerteDialog.setResizable(false);
            }else{
               frame.setContentPane(new LogIn(frame,control,token));
            }
        });

        buttonVisita.addActionListener(e -> {
            if(token !="") {
                final JDialog visitaDialog = new JDialog(frame, "Visita", true);
                visitaDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                visitaDialog.setSize(700, 550);
                visitaDialog.setResizable(false);
            }else{
                frame.setContentPane(new LogIn(frame,control,token));
            }
        });

        //Pannello Dettagli
        JPanel pannelloContenuto = new JPanel();
        pannelloContenuto.setLayout(new BoxLayout(pannelloContenuto, BoxLayout.Y_AXIS));

// Descrizione
        pannelloContenuto.add(new JLabel("Descrizione:"));
        JTextArea areaDescrizione = new JTextArea(5, 30);
        areaDescrizione.setLineWrap(true);
        areaDescrizione.setWrapStyleWord(true);
        pannelloContenuto.add(new JScrollPane(areaDescrizione));

        JPanel paramPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        for (int i = 1; i <= 6; i++) {
            paramPanel.add(new JLabel("Parametro " + i));
            paramPanel.add(new JTextField(10));
        }
        pannelloContenuto.add(paramPanel);

        pannelloContenuto.add(panelChart);

        pannelloContenuto.add(new JLabel("Indirizzo: Via Esempio 123, Napoli"));

        JPanel mappaPanel = new JPanel(new BorderLayout());
        mappaPanel.add(new JLabel("Qui verrà la mappa"), BorderLayout.CENTER);
        pannelloContenuto.add(mappaPanel);

        scrollPaneDettagli = new JScrollPane(pannelloContenuto);

    }


    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Evaluation license - Marco Scotto
        panelTopBar = new JPanel();
        menuBarOpzioni = new JMenuBar();
        menuOpzioni = new JMenu();
        buttonBack = new JButton();
        panelImmagineImmobile = new JPanel();
        buttonDestraImg = new JButton();
        buttonSinistraImg = new JButton();
        buttonOfferta = new JButton();
        buttonVisita = new JButton();
        scrollPaneDettagli = new JScrollPane();
        panelChart = new JPanel();

        //======== this ========
        setBorder ( new javax . swing. border .CompoundBorder ( new javax . swing. border .TitledBorder ( new javax . swing
        . border .EmptyBorder ( 0, 0 ,0 , 0) ,  "JF\u006frmDes\u0069gner \u0045valua\u0074ion" , javax. swing .border . TitledBorder
        . CENTER ,javax . swing. border .TitledBorder . BOTTOM, new java. awt .Font ( "D\u0069alog", java .
        awt . Font. BOLD ,12 ) ,java . awt. Color .red ) , getBorder () ) )
        ;  addPropertyChangeListener( new java. beans .PropertyChangeListener ( ){ @Override public void propertyChange (java . beans. PropertyChangeEvent e
        ) { if( "\u0062order" .equals ( e. getPropertyName () ) )throw new RuntimeException( ) ;} } )
        ;
        setLayout(new MigLayout(
            "fill,hidemode 3",
            // columns
            "[188,fill]" +
            "[174,center]" +
            "[174,center]" +
            "[174,center]" +
            "[174,center]",
            // rows
            "rel[452]" +
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

        //======== panelImmagineImmobile ========
        {
            panelImmagineImmobile.setBorder(new EtchedBorder(EtchedBorder.RAISED, UIManager.getColor("Actions.Green"), null));
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
        add(panelImmagineImmobile, "cell 0 0 3 2,grow");

        //---- buttonOfferta ----
        buttonOfferta.setText("Aggiungi Offerta");
        add(buttonOfferta, "cell 3 0 2 1");

        //---- buttonVisita ----
        buttonVisita.setText("Richiedi Visita");
        add(buttonVisita, "cell 3 1 2 1");
        add(scrollPaneDettagli, "cell 0 2 3 3,grow");

        //======== panelChart ========
        {
            panelChart.setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(
            0,0,0,0), "JF\u006frmD\u0065sig\u006eer \u0045val\u0075ati\u006fn",javax.swing.border.TitledBorder.CENTER,javax.swing.border.TitledBorder
            .BOTTOM,new java.awt.Font("Dia\u006cog",java.awt.Font.BOLD,12),java.awt.Color.
            red),panelChart. getBorder()));panelChart. addPropertyChangeListener(new java.beans.PropertyChangeListener(){@Override public void propertyChange(java.
            beans.PropertyChangeEvent e){if("\u0062ord\u0065r".equals(e.getPropertyName()))throw new RuntimeException();}});
            panelChart.setLayout(new BorderLayout());
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY //GEN-BEGIN:variables @formatter:off
    // Generated using JFormDesigner Evaluation license - Marco Scotto
    private JPanel panelTopBar;
    private JMenuBar menuBarOpzioni;
    private JMenu menuOpzioni;
    private JButton buttonBack;
    private JPanel panelImmagineImmobile;
    private JButton buttonDestraImg;
    private JButton buttonSinistraImg;
    private JButton buttonOfferta;
    private JButton buttonVisita;
    private JScrollPane scrollPaneDettagli;
    private JPanel panelChart;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
