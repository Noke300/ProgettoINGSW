/*
 * Created by JFormDesigner on Tue Apr 22 11:59:05 CEST 2025
 */

package gui.pagine_administrative;

import java.awt.*;
import java.awt.event.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;

import com.google.gson.JsonArray;
import com.toedter.calendar.*;

import controller.ControllerAdministration;

import gui.costum_components.*;
import model.Cliente;
import model.Immobile;
import model.ImmobileEImmagini;
import model.Visita;
import net.miginfocom.swing.*;
import org.jxmapviewer.viewer.GeoPosition;


/**
 * @author marco
 */
public class InterfacciaAgenteImmobiliare extends JPanel {
    MapLoader mapLoader;
    private static final IconClass iconSet = new IconClass();
    private GeoPosition geoPos = new GeoPosition(45.464170, 11.885890);
    List<JCheckBox> checkBoxesList = new ArrayList<>();
    private JPanel[] selectedPanel = new JPanel[1]; // inizialmente vuoto
    private ImmobilePanelSingolo immobilePanelSingolo = new ImmobilePanelSingolo();
    //--Border per gli immobili selected
    private List<ColoredDate> coloredDates = new ArrayList<>();


    @Override
    public void addNotify() {
        super.addNotify();
        // Assicurati che tutti i menu popup siano nascosti quando il pannello viene aggiunto
        MenuSelectionManager.defaultManager().clearSelectedPath();
    }

    public InterfacciaAgenteImmobiliare(JFrame frame, BottoniOpzioni bottoniOpzioni,ControllerAdministration control, String token,List<ImmobileEImmagini> immobiliEImmagini) {
        initComponents();
        bottoniOpzioni.setUpOpzioniMenu(frame,control,token,menuBarOpzioni,menuOpzioni);

        List<Immobile> immobili = new ArrayList<>();
        for (ImmobileEImmagini i : immobiliEImmagini){
            immobili.add(i.getImmobile());
        }
        //TODO notifiche non interaggibile temporanio
        LogInAmministrazione.notifiche.addListener(messaggio -> {
            System.out.println("Ricevuto: " + messaggio);

            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Notifica: " + messaggio));
        });

        mapLoader = new MapLoader(geoPos, panelMap, immobili);
        // In the initComponents method, replace the GridLayout with MigLayout
        panelListaImmobile.setLayout(new MigLayout("wrap 1, align center")); // One component per row

        Dimension dim  = new Dimension(820,289);
        for (ImmobileEImmagini immobileEImmagini : immobiliEImmagini) {
            JPanel panel = immobilePanelSingolo.createImmobileSingoloPanel(frame,immobileEImmagini,control,token,selectedPanel);
            panel.setName(immobileEImmagini.getImmobile().getId());
            panel.setMaximumSize(dim);
            panel.setPreferredSize(dim);
            panelListaImmobile.add(panel);


            panel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (selectedPanel[0] == null) {
                       buttonElimina.setEnabled(false);
                    }else{
                        buttonElimina.setEnabled(true);
                    }
                }
            });
        }

        buttonElimina.addActionListener(e -> {
            if (selectedPanel != null) {
                control.eliminaImmobile(selectedPanel[0].getName(),token);
                panelListaImmobile.remove(selectedPanel[0]);
                selectedPanel = null;
                buttonElimina.setEnabled(false);
                panelListaImmobile.revalidate();
                panelListaImmobile.repaint();
            }
        });
        //--Calendario
        // Then in your initialization or constructor method
        IconClass iconSet = new IconClass();
        buttonCalendario.setIcon(iconSet.getCalendarioIcon());
        buttonCalendario.setText(LocalDate.now().toString());

        // Calendario
        JCalendar calendar = new JCalendar();
        Map<String, List<Visita>> visitePerGiorno = new HashMap<>();

        buttonCalendario.addActionListener(e -> {


            calendar.setLocale(Locale.ITALY);
            JDayChooser dayChooser = calendar.getDayChooser();

            // Manager colori
            DateColorManager dateColorManager = new DateColorManager(dayChooser);

            // Pulizia dati prima di caricare
            visitePerGiorno.clear();
            ArrayList<ColoredDate> coloredDates = new ArrayList<>();

            List<Visita> date = control.getVisite(token);
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            for (Visita visita : date) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(visita.getDataVisita());
                cal.add(Calendar.DAY_OF_MONTH, 1);
                Date nuovaData = cal.getTime();
                visita.setDataVisita(nuovaData);

                String giornoChiave = format.format(nuovaData);
                visitePerGiorno.putIfAbsent(giornoChiave, new ArrayList<>());
                visitePerGiorno.get(giornoChiave).add(visita);

                if (!coloredDates.contains(new ColoredDate(nuovaData,visita.getConferma()))) {
                    coloredDates.add(new ColoredDate(nuovaData,visita.getConferma()));
                }


                dateColorManager.setColoredDates(coloredDates);
                dateColorManager.updateDayColors(visita.getConferma());

                calendar.addPropertyChangeListener(evt -> {
                    if ("calendar".equals(evt.getPropertyName())) {
                        dateColorManager.updateDayColors(visita.getConferma());
                    }
                });
            }



            JPanel controlPanel = new JPanel(new BorderLayout());

            JButton addDateButton = new JButton("Info Giorno");
            addDateButton.addActionListener(h -> mostraDettagliVisite(calendar.getDate(),format,frame,visitePerGiorno));
            dayChooser.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        mostraDettagliVisite(calendar.getDate(),format,frame,visitePerGiorno);
                    }
                }
            });


            controlPanel.add(addDateButton, BorderLayout.SOUTH);

            final JDialog calendarPopup = new JDialog(frame, "Calendario", true);
            calendarPopup.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            calendarPopup.setSize(700, 450);
            calendarPopup.setResizable(false);
            calendarPopup.add(calendar, BorderLayout.CENTER);
            calendarPopup.add(controlPanel, BorderLayout.SOUTH);
            calendarPopup.setLocationRelativeTo(frame);

            // Pulizia finale alla chiusura
            calendarPopup.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    visitePerGiorno.clear();
                    coloredDates.clear();
                }
            });

            calendarPopup.setVisible(true);
        });

        buttonAggiungImmobile.setIcon(iconSet.getPlusImmobile());

        buttonAggiungImmobile.addActionListener(h ->{
            frame.setContentPane(new InserimentoImmobile(frame,control,token));
            frame.revalidate();
            frame.repaint();
        });


    }

    public JPanel getSelectedPanel() {
        return selectedPanel[0];
    }



    private void mostraDettagliVisite(Date selectedDate,DateFormat format,JFrame frame, Map<String, List<Visita>> visitePerGiorno) {
        String giornoChiave = format.format(selectedDate);
        List<Visita> visiteDelGiorno = visitePerGiorno.get(giornoChiave);

        if (visiteDelGiorno != null && !visiteDelGiorno.isEmpty()) {
            JPanel containerPanel = new JPanel();
            containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.Y_AXIS));

            for (Visita visita : visiteDelGiorno) {
                Immobile imm = visita.getImmobile();
                Cliente cl = visita.getCliente();

                JPanel visitaPanel = new JPanel();
                visitaPanel.setLayout(new BoxLayout(visitaPanel, BoxLayout.Y_AXIS));
                visitaPanel.setBorder(BorderFactory.createTitledBorder("🕒 Visita alle " + visita.getOraVisita()));

                JTextArea info = new JTextArea(
                        "🏠 Tipo immobile: " + imm.getTipoImmobile() + "\n" +
                                "💸 Prezzo: €" + imm.getPrezzo() + "\n" +
                                "📍 Indirizzo: " + imm.getIndirizzo().toString() + "\n" +
                                "👤 Cliente: " + cl.getNome() + " " + cl.getCognome() + "\n" +
                                "✅ Confermata: " + (visita.getConferma() ? "Sì" : "No")
                );
                info.setFont(new Font("SansSerif", Font.PLAIN, 16));
                info.setEditable(false);
                visitaPanel.add(info);

                JPanel buttonPanel = new JPanel();
                if (!visita.getConferma()) {
                    JButton accetta = new JButton("✅ Accetta");
                    JButton rifiuta = new JButton("❌ Rifiuta");

                    accetta.addActionListener(ev -> {
                        visita.setConferma(true);
                        JOptionPane.showMessageDialog(frame, "Visita confermata.");
                    });
                    rifiuta.addActionListener(ev -> {
                        JOptionPane.showMessageDialog(frame, "Visita rifiutata.");
                    });

                    buttonPanel.add(accetta);
                    buttonPanel.add(rifiuta);
                } else {
                    JButton annulla = new JButton("🔄 Annulla");

                    annulla.addActionListener(ev -> {
                        visita.setConferma(false);
                        JOptionPane.showMessageDialog(frame, "Visita annullata.");
                    });

                    buttonPanel.add(annulla);
                }

                visitaPanel.add(Box.createRigidArea(new Dimension(0, 10)));
                visitaPanel.add(buttonPanel);
                containerPanel.add(visitaPanel);
            }

            JScrollPane scrollPane = new JScrollPane(containerPanel);
            scrollPane.setPreferredSize(new Dimension(600, 400));

            JOptionPane.showMessageDialog(
                    frame,
                    scrollPane,
                    "Visite del " + giornoChiave,
                    JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            JOptionPane.showMessageDialog(
                    frame,
                    "Nessuna visita per questo giorno.",
                    "Informazioni",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }


    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY //GEN-BEGIN:initComponents @formatter:off
        // Generated using JFormDesigner Evaluation license - Marco Scotto
        panelOpzioni = new JPanel();
        labelNomeAzzienda = new JLabel();
        labelNotifiche = new JLabel();
        buttonCalendario = new JButton();
        menuBarOpzioni = new JMenuBar();
        menuOpzioni = new JMenu();
        panelListaImmobileScroll = new JScrollPane();
        panelListaImmobile = new JPanel();
        panelMap = new JPanel();
        panelBottomBar = new JPanel();
        buttonAggiungImmobile = new JButton();
        buttonAggiungiOfferta = new JButton();
        buttonElimina = new JButton();

        //======== this ========
        setBorder(new BevelBorder(BevelBorder.LOWERED));
        setBorder(new CompoundBorder(new TitledBorder(new EmptyBorder(0,0,0,0), "JF\u006frmD\u0065sig\u006eer \u0045val\u0075ati\u006fn",TitledBorder.CENTER,TitledBorder.BOTTOM,new Font("Dia\u006cog",Font.BOLD,
        12),Color.red), getBorder())); addPropertyChangeListener(new PropertyChangeListener(){@Override public void propertyChange(PropertyChangeEvent e){if("\u0062ord\u0065r".equals(e.
        getPropertyName()))throw new RuntimeException();}});
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

            //---- labelNotifiche ----
            labelNotifiche.setText("Notifiche");
            labelNotifiche.setIcon(UIManager.getIcon("FileView.directoryIcon"));
            labelNotifiche.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            panelOpzioni.add(labelNotifiche, "cell 1 0,align center center,grow 0 0");

            //---- buttonCalendario ----
            buttonCalendario.setIcon(UIManager.getIcon("FileView.fileIcon"));
            buttonCalendario.setHorizontalTextPosition(SwingConstants.CENTER);
            buttonCalendario.setVerticalTextPosition(SwingConstants.BOTTOM);
            buttonCalendario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            buttonCalendario.setBackground(new Color(0x00ffffff, true));
            buttonCalendario.setBorder(null);
            panelOpzioni.add(buttonCalendario, "cell 2 0,grow");

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
        add(panelMap, "cell 3 0 10 5,grow");

        //======== panelBottomBar ========
        {
            panelBottomBar.setBorder(new EtchedBorder());
            panelBottomBar.setBackground(UIManager.getColor("Actions.Green"));
            panelBottomBar.setLayout(new MigLayout(
                "insets 0,hidemode 3,gap 5 5",
                // columns
                "[88,grow 1,fill]" +
                "[grow 1,fill]" +
                "[grow 1,fill]" +
                "[grow 1,fill]",
                // rows
                "[33,fill]" +
                "[]"));

            //---- buttonAggiungImmobile ----
            buttonAggiungImmobile.setIcon(UIManager.getIcon("FileChooser.homeFolderIcon"));
            buttonAggiungImmobile.setBackground(UIManager.getColor("Actions.Green"));
            buttonAggiungImmobile.setText("Aggiungi Immobile");
            panelBottomBar.add(buttonAggiungImmobile, "cell 0 0 1 2,align left center,grow 0 0");

            //---- buttonAggiungiOfferta ----
            buttonAggiungiOfferta.setText("Aggiungi Offerta");
            buttonAggiungiOfferta.setBorderPainted(false);
            buttonAggiungiOfferta.setBackground(UIManager.getColor("Actions.Green"));
            panelBottomBar.add(buttonAggiungiOfferta, "cell 2 0 1 2,aligny center,growy 0");

            //---- buttonElimina ----
            buttonElimina.setText("Elimina");
            buttonElimina.setBorder(new EmptyBorder(5, 5, 5, 5));
            buttonElimina.setBackground(UIManager.getColor("Actions.Green"));
            buttonElimina.setEnabled(false);
            panelBottomBar.add(buttonElimina, "cell 3 0 1 2,grow");
        }
        add(panelBottomBar, "south");
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    // Generated using JFormDesigner Evaluation license - Marco Scotto
    private JPanel panelOpzioni;
    private JLabel labelNomeAzzienda;
    private JLabel labelNotifiche;
    private JButton buttonCalendario;
    private JMenuBar menuBarOpzioni;
    private JMenu menuOpzioni;
    private JScrollPane panelListaImmobileScroll;
    private JPanel panelListaImmobile;
    private JPanel panelMap;
    private JPanel panelBottomBar;
    private JButton buttonAggiungImmobile;
    private JButton buttonAggiungiOfferta;
    private JButton buttonElimina;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
