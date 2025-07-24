/*
 * Created by JFormDesigner on Tue Apr 22 11:59:05 CEST 2025
 */

package gui.pagine_administrative.agenteImmobliiare;

import java.awt.*;
import java.awt.event.*;

import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;

import controller.ControllerAgente;

import controller.NotificationClient;
import gui.pagine_comuni.Ricerca;
import gui.costum_components.*;
import gui.costum_components.calender.CalenderAgenteImmobiliare;
import gui.costum_components.maps.MapLoader;
import model.AgenteImmobiliare;
import model.Immobile;
import model.ImmobileEImmagini;
import model.Offerta;
import net.miginfocom.swing.*;
import org.jxmapviewer.viewer.GeoPosition;


/**
 * @author marco
 */
public class InterfacciaAgenteImmobiliare extends JPanel {
    MapLoader mapLoader;
    private JPanel[] selectedPanel = new JPanel[1];// inizialmente vuoto
    GeoPosition geoPos;
    //--Border per gli immobili selected

    @Override
    public void addNotify() {
        super.addNotify();
        // Assicurati che tutti i menu popup siano nascosti quando il pannello viene aggiunto
        MenuSelectionManager.defaultManager().clearSelectedPath();
    }

    public InterfacciaAgenteImmobiliare(JFrame frame, BottoniOpzioni bottoniOpzioni, ControllerAgente control, String token, List<ImmobileEImmagini> immobiliEImmagini) {
        initComponents();
        bottoniOpzioni.setUpOpzioniMenu(frame,control,token,menuBarOpzioni,menuOpzioni);

        AgenteImmobiliare agente = control.getSpecifiche(token);
        labelNomeAzzienda.setText(agente.getNomeAgente() + " " + agente.getCognomeAgente() + ", agente immobiliare presso: " + agente.getAgenziaImmobiliare().getNomeAgenzia());

        List<Immobile> immobili = new ArrayList<>();

        for (ImmobileEImmagini i : immobiliEImmagini){
            immobili.add(i.getImmobile());
        }

        Ricerca.notifiche.addListener(messaggio -> SwingUtilities.invokeLater(() -> NotificationClient.notificheWindow(messaggio)));

        if(!immobili.isEmpty()){
            geoPos = MapLoader.getCoordinatesFromAddress(immobili.getFirst().getGeoPosition());
        }else{
            geoPos = new GeoPosition(42.5, 12.5);
        }
        mapLoader = new MapLoader(geoPos, panelMap, immobili);
        // In the initComponents method, replace the GridLayout with MigLayout
        panelListaImmobile.setLayout(new MigLayout("wrap 1, align center")); // One component per row

        Dimension dim  = new Dimension(820,289);
        for (ImmobileEImmagini immobileEImmagini : immobiliEImmagini) {
            JPanel panel = ImmobilePanelSingolo.createImmobileSingoloPanel(frame,immobileEImmagini,control,token,selectedPanel);
            panel.setName(immobileEImmagini.getImmobile().getId());
            panel.setMaximumSize(dim);
            panel.setPreferredSize(dim);
            panelListaImmobile.add(panel);


            panel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (selectedPanel[0] == null) {
                       buttonElimina.setEnabled(false);
                       buttonAggiungiOfferta.setEnabled(false);
                       buttonOfferteVisualizza.setEnabled(false);
                    }else{
                        buttonAggiungiOfferta.setEnabled(true);
                        buttonElimina.setEnabled(true);
                        buttonOfferteVisualizza.setEnabled(true);
                    }
                }
            });
        }

        //Button
        buttonAggiungiOfferta.addActionListener(e -> {
            Immobile impTem = control.fetchImmobile(token,selectedPanel[0].getName());
            OffertaPannel.setUpPannel(token,frame,impTem,control);
        });

        buttonOfferteVisualizza.addActionListener(e -> {
            List<Offerta> offertas;
            Immobile immobile = new Immobile(selectedPanel[0].getName());
            offertas = control.getOffertePerImmobile(immobile);
            OffertaPannel.mostraOfferteInSospeso(frame,offertas,control,token,panelListaImmobile,selectedPanel[0]);

            buttonElimina.setEnabled(false);
            buttonOfferteVisualizza.setEnabled(false);
            buttonAggiungiOfferta.setEnabled(false);

        });


        buttonElimina.addActionListener(e -> {
            if (selectedPanel != null) {
                int conferma = JOptionPane.showConfirmDialog(
                        null,
                        "Sei sicuro di voler eliminare l'immobile?",
                        "Conferma eliminazione",
                        JOptionPane.YES_NO_OPTION
                );

                    if (conferma == JOptionPane.YES_OPTION) {
                    control.eliminaImmobile(selectedPanel[0].getName(), token);

                    panelListaImmobile.remove(selectedPanel[0]);
                    selectedPanel[0] = null;

                    buttonElimina.setEnabled(false);
                    buttonAggiungiOfferta.setEnabled(false);
                    buttonOfferteVisualizza.setEnabled(false);

                    panelListaImmobile.revalidate();
                    panelListaImmobile.repaint();
                }
            }
        });

        //--Calendario
        CalenderAgenteImmobiliare.caricaCalendario(buttonCalendario,control,token,frame);

        buttonAggiungImmobile.setIcon(IconClass.getPlusImmobile());

        buttonAggiungImmobile.addActionListener(h ->{
            frame.setContentPane(new InserimentoImmobile(frame,control,token));
            frame.revalidate();
            frame.repaint();
        });


    }

    public JPanel getSelectedPanel() {
        return selectedPanel[0];
    }


    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY //GEN-BEGIN:initComponents @formatter:off
        // Generated using JFormDesigner Educational license - Marco Scotto di Uccio Scotto di Uccio (Marco Scotto Di Uccio)
        panelOpzioni = new JPanel();
        labelNomeAzzienda = new JLabel();
        buttonCalendario = new JButton();
        menuBarOpzioni = new JMenuBar();
        menuOpzioni = new JMenu();
        panelListaImmobileScroll = new JScrollPane();
        panelListaImmobile = new JPanel();
        panelMap = new JPanel();
        panelBottomBar = new JPanel();
        buttonAggiungImmobile = new JButton();
        buttonAggiungiOfferta = new JButton();
        buttonOfferteVisualizza = new JButton();
        buttonElimina = new JButton();

        //======== this ========
        setBorder(new BevelBorder(BevelBorder.LOWERED));
        setLayout(new MigLayout(
            "fill,insets 0,hidemode 3,align center center",
            // columns
            "0[139,grow,fill]0" +
            "[117,fill]0" +
            "[96,fill]0" +
            "[96,fill]0" +
            "[93,fill]0" +
            "[fill]0" +
            "[112,fill]0" +
            "[0,fill]0" +
            "[71,fill]0" +
            "[0,center]0" +
            "[24,grow,fill]0" +
            "[9,fill]0" +
            "[14,grow,fill]",
            // rows
            "[48,grow,fill]" +
            "[48,grow,fill]" +
            "[48,grow,center]" +
            "[grow,center]" +
            "[grow,fill]" +
            "[grow,fill]" +
            "[grow,fill]" +
            "[grow,fill]"));

        //======== panelOpzioni ========
        {
            panelOpzioni.setBackground(UIManager.getColor("Actions.Green"));
            panelOpzioni.setBorder(new BevelBorder(BevelBorder.RAISED, null, Color.black, null, null));
            panelOpzioni.setLayout(new MigLayout(
                "fill,insets 0,hidemode 3,gap 5 5",
                // columns
                "[0,fill]0" +
                "[fill]" +
                "[fill]0" +
                "[138,fill]" +
                "[fill]",
                // rows
                "[67,grow,fill]0"));

            //---- labelNomeAzzienda ----
            labelNomeAzzienda.setText("Nome");
            labelNomeAzzienda.setFont(new Font("Segoe UI", Font.PLAIN, 20));
            panelOpzioni.add(labelNomeAzzienda, "cell 0 0,align left center,grow 0 0");

            //---- buttonCalendario ----
            buttonCalendario.setIcon(UIManager.getIcon("FileView.fileIcon"));
            buttonCalendario.setHorizontalTextPosition(SwingConstants.CENTER);
            buttonCalendario.setVerticalTextPosition(SwingConstants.BOTTOM);
            buttonCalendario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            buttonCalendario.setBackground(new Color(0x00ffffff, true));
            buttonCalendario.setBorder(null);
            panelOpzioni.add(buttonCalendario, "cell 1 0,grow");

            //======== menuBarOpzioni ========
            {
                menuBarOpzioni.setBorderPainted(false);
                menuBarOpzioni.setBackground(new Color(0x4e5052));
                menuBarOpzioni.setFont(new Font("Segoe UI", Font.PLAIN, 16));

                //======== menuOpzioni ========
                {
                    menuOpzioni.setText("Opzioni");
                    menuOpzioni.setIcon(UIManager.getIcon("Table.descendingSortIcon"));
                    menuOpzioni.setFont(new Font("Segoe UI", Font.PLAIN, 22));
                }
                menuBarOpzioni.add(menuOpzioni);
            }
            panelOpzioni.add(menuBarOpzioni, "cell 4 0,alignx trailing,grow 0 100");
        }
        add(panelOpzioni, "north");

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
        add(panelListaImmobileScroll, "cell 0 0 3 8,grow");

        //======== panelMap ========
        {
            panelMap.setBorder(new LineBorder(UIManager.getColor("Actions.Green"), 3, true));
            panelMap.setLayout(new BorderLayout());
        }
        add(panelMap, "cell 3 0 10 8,grow");

        //======== panelBottomBar ========
        {
            panelBottomBar.setBorder(new EtchedBorder());
            panelBottomBar.setBackground(UIManager.getColor("Actions.Green"));
            panelBottomBar.setLayout(new MigLayout(
                "insets 0,hidemode 3,gap 5 5",
                // columns
                "[88,grow 1,fill]unrel" +
                "[123,grow 1,fill]rel" +
                "[grow 1,fill]rel" +
                "[grow 1,fill]",
                // rows
                "[33,fill]" +
                "[]"));

            //---- buttonAggiungImmobile ----
            buttonAggiungImmobile.setIcon(UIManager.getIcon("FileChooser.homeFolderIcon"));
            buttonAggiungImmobile.setBackground(UIManager.getColor("Actions.Green"));
            buttonAggiungImmobile.setText("Aggiungi Immobile");
            buttonAggiungImmobile.setBorderPainted(false);
            panelBottomBar.add(buttonAggiungImmobile, "cell 0 0 1 2");

            //---- buttonAggiungiOfferta ----
            buttonAggiungiOfferta.setText("Aggiungi Offerta");
            buttonAggiungiOfferta.setBorderPainted(false);
            buttonAggiungiOfferta.setBackground(UIManager.getColor("Actions.Green"));
            buttonAggiungiOfferta.setEnabled(false);
            panelBottomBar.add(buttonAggiungiOfferta, "cell 1 0 1 2,growy");

            //---- buttonOfferteVisualizza ----
            buttonOfferteVisualizza.setText("Visualizza Offerte");
            buttonOfferteVisualizza.setBorderPainted(false);
            buttonOfferteVisualizza.setBackground(UIManager.getColor("Actions.Green"));
            buttonOfferteVisualizza.setEnabled(false);
            panelBottomBar.add(buttonOfferteVisualizza, "cell 2 0 1 2");

            //---- buttonElimina ----
            buttonElimina.setText("Elimina");
            buttonElimina.setBorder(new EmptyBorder(5, 5, 5, 5));
            buttonElimina.setBackground(UIManager.getColor("Actions.Green"));
            buttonElimina.setEnabled(false);
            panelBottomBar.add(buttonElimina, "cell 3 0 1 2,grow");
        }
        add(panelBottomBar, "south,growy");
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    // Generated using JFormDesigner Educational license - Marco Scotto di Uccio Scotto di Uccio (Marco Scotto Di Uccio)
    private JPanel panelOpzioni;
    private JLabel labelNomeAzzienda;
    private JButton buttonCalendario;
    private JMenuBar menuBarOpzioni;
    private JMenu menuOpzioni;
    private JScrollPane panelListaImmobileScroll;
    private JPanel panelListaImmobile;
    private JPanel panelMap;
    private JPanel panelBottomBar;
    private JButton buttonAggiungImmobile;
    private JButton buttonAggiungiOfferta;
    private JButton buttonOfferteVisualizza;
    private JButton buttonElimina;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
