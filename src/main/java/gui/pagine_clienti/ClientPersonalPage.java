/*
 * Created by JFormDesigner on Wed Jul 02 01:30:41 CEST 2025
 */

package gui.pagine_clienti;

import java.awt.*;
import java.text.SimpleDateFormat;
import javax.swing.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import javax.swing.border.*;
import com.formdev.flatlaf.extras.*;
import controller.ControllerClienti;
import gui.costum_components.BottoniOpzioni;
import gui.costum_components.IconClass;
import model.Cliente;
import model.Offerta;
import model.Visita;
import net.miginfocom.swing.*;

/**
 * @author marco
 */
public class ClientPersonalPage extends JPanel {

    private List<Offerta> listaOfferte;
    private List<Visita> listaVisita;
    BottoniOpzioni bottoniOpzioni = new BottoniOpzioni();

    public ClientPersonalPage(JFrame frame, ControllerClienti controllerClienti, String token) {
        initComponents();
        Cliente cliente = controllerClienti.getSpecifiche(token);

        labelClient.setIcon(IconClass.getUtenteIcon());
        bottoniOpzioni.setUpOpzioniMenu(frame, controllerClienti, token, menuBarOpzioni, menuOpzioni);

        // 🔄 Bottone Offerte
        buttonOfferte.addActionListener(e -> {
            listaOfferte = controllerClienti.getOfferte(token);
            Map<LocalDateTime, JLabel> mappa = getOfferteMap(listaOfferte);
            aggiornaContenutoPanel(mappa, panelInterno, controllerClienti,token);
            contenutoPanel.revalidate();
            contenutoPanel.repaint();
        });

        // 🔄 Bottone Log
        buttonLog.addActionListener(e -> {
            aggiornaContenutoPanel(BottoniOpzioni.getMapLog(), panelInterno, controllerClienti);
            contenutoPanel.revalidate();
            contenutoPanel.repaint();
        });

        // 🔄 Bottone Visite
        listaVisita = controllerClienti.getVisite(token);
        buttonVisite.addActionListener(e -> {
            aggiornaContenutoPanel(getVisiteMap(listaVisita), panelInterno, controllerClienti);
            contenutoPanel.revalidate();
            contenutoPanel.repaint();
        });

        labelEmail.setText(cliente.getEmailcliente());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        labelDataN.setText(formatter.format(cliente.getDataN()));
        labelNomeC.setText(cliente.getNome() + " " + cliente.getCognome());

        separatorO.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        separatorO.setForeground(Color.LIGHT_GRAY);

        separatorV.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        separatorV.setForeground(Color.LIGHT_GRAY);
    }

    @Override
    public void addNotify() {
        super.addNotify();
        MenuSelectionManager.defaultManager().clearSelectedPath();
    }

    public static void aggiornaContenutoPanel(Map<LocalDateTime, JLabel> dati, JPanel contenutoPanel, ControllerClienti controllerClienti,String token) {
        contenutoPanel.removeAll();
        contenutoPanel.setLayout(new BoxLayout(contenutoPanel, BoxLayout.Y_AXIS));

        Map<LocalDateTime, JLabel> ordinata = new TreeMap<>(dati);

        for (Map.Entry<LocalDateTime, JLabel> entry : ordinata.entrySet()) {
            LocalDateTime data = entry.getKey();
            JLabel label = entry.getValue();

            JPanel rigaPanel = new JPanel();
            rigaPanel.setLayout(new BoxLayout(rigaPanel, BoxLayout.X_AXIS));
            rigaPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

            rigaPanel.add(label);

            // Controllo testuale dentro JLabel
            String testo = label.getText();
            if (testo.contains("Controfferta")) {
                JButton accetta = new JButton("Accetta");
                JButton rifiuta = new JButton("Rifiuta");

                accetta.addActionListener(e -> {
                    Offerta offerta = new Offerta(label.getName(),true);
                    controllerClienti.accettaOfferta(offerta,token); // Aggiungi chiamata se disponibile
                    JOptionPane.showMessageDialog(contenutoPanel, "✅ Offerta accettata il " + data.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                });

                rifiuta.addActionListener(e -> {
                    Offerta offerta = new Offerta(label.getName(),false);
                    controllerClienti.accettaOfferta(offerta,token); // Ag
                    JOptionPane.showMessageDialog(contenutoPanel, "❌ Offerta rifiutata il " + data.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                });

                rigaPanel.add(Box.createHorizontalStrut(10));
                rigaPanel.add(accetta);
                rigaPanel.add(Box.createHorizontalStrut(5));
                rigaPanel.add(rifiuta);
            }

            contenutoPanel.add(rigaPanel);
            contenutoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        contenutoPanel.revalidate();
        contenutoPanel.repaint();
    }

    public static void aggiornaContenutoPanel(Map<LocalDateTime, String> dati, JPanel contenutoPanel, ControllerClienti controllerClienti) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        contenutoPanel.removeAll();
        contenutoPanel.setLayout(new BoxLayout(contenutoPanel, BoxLayout.Y_AXIS));

        Map<LocalDateTime, String> ordinata = new TreeMap<>(dati);

        for (Map.Entry<LocalDateTime, String> entry : ordinata.entrySet()) {
            LocalDateTime data = entry.getKey();
            String testo = entry.getValue();

            JPanel rigaPanel = new JPanel();
            rigaPanel.setLayout(new BoxLayout(rigaPanel, BoxLayout.X_AXIS));
            rigaPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel riga = new JLabel("🕓 " + data.format(formatter) + " → " + testo);
            riga.setFont(new Font("SansSerif", Font.PLAIN, 20));
            rigaPanel.add(riga);

            contenutoPanel.add(rigaPanel);
            contenutoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        contenutoPanel.revalidate();
        contenutoPanel.repaint();
    }


    public static Map<LocalDateTime, JLabel> getOfferteMap(List<Offerta> listaOfferte) {
        Map<LocalDateTime, JLabel> dati = new HashMap<>();
        for (Offerta offerta : listaOfferte) {
            LocalDateTime data = offerta.getDataOfferta();
            String tipo;
            if (Boolean.TRUE.equals(offerta.getContrOfferta())) {
                tipo = "Controfferta";
            } else {
                Boolean esito = offerta.getEsitoOfferta();
                if (esito == null) {
                    tipo = "In Attesa";
                } else if (esito) {
                    tipo = "Accettata";
                } else {
                    tipo = "Rifiutata";
                }
            }
            String testo = "💰 Prezzo: " + offerta.getPrezzoOfferta() +
                    " | 📍 Immobile: " + offerta.getImmobile().getIndirizzo().toString() +
                    " | Tipo: " + tipo;
            JLabel label = new JLabel("🕓 " + data.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + " → " + testo);
            label.setName(offerta.getIdOfferta());
            label.setFont(new Font("SansSerif", Font.PLAIN, 20));

            dati.put(data, label);
        }
        return dati;
    }

    public static Map<LocalDateTime, String> getVisiteMap(List<Visita> listaVisite) {
        Map<LocalDateTime, String> dati = new HashMap<>();
        for (Visita visita : listaVisite) {
            Instant instant = visita.getDataVisita().toInstant();
            ZoneId zona = ZoneId.systemDefault();
            LocalDateTime dataOra = LocalDateTime.ofInstant(instant, zona)
                    .withHour(visita.getOraVisita().getHour())
                    .withMinute(visita.getOraVisita().getMinute());

            String stato = visita.getConferma() ? "Confermata" : "In Attesa";
            String testo = "🗓️ Data/Ora: " + dataOra.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) +
                    " | 📍 Immobile: " + visita.getImmobile().getIndirizzo().toString() +
                    " | Stato: " + stato;

            dati.put(dataOra, testo);
        }
        return dati;
    }


    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Educational license - Marco Scotto di Uccio Scotto di Uccio (Marco Scotto Di Uccio)
        labelEmail = new JLabel();
        labelNomeC = new JLabel();
        separatorO = new JSeparator();
        labelClient = new JLabel();
        labelDataN = new JLabel();
        separatorV = new JSeparator();
        buttonLog = new JButton();
        buttonOfferte = new JButton();
        buttonVisite = new JButton();
        contenutoPanel = new JScrollPane();
        panelInterno = new JPanel();
        panelMenuBarOpzioniColore = new JPanel();
        menuBarOpzioni = new JMenuBar();
        menuOpzioni = new JMenu();
        LogoOpzioni = new JLabel();
        labelTitolo = new JLabel();

        //======== this ========
        setLayout(new MigLayout(
            "fill,insets 0,hidemode 3,align center center,gap 10 5",
            // columns
            "0[107,fill]0" +
            "[74,fill]0" +
            "[fill]0" +
            "[111,grow,center]0" +
            "[225,center]0" +
            "[62,shrink 0,fill]0",
            // rows
            "rel[73,top]0" +
            "[64,center]" +
            "[73,center]0" +
            "[]0" +
            "[73,center]0" +
            "[73,center]0" +
            "[84,top]0" +
            "[90,center]0" +
            "[29]0" +
            "[77]0" +
            "[21,top]0"));

        //---- labelEmail ----
        labelEmail.setText("Email");
        labelEmail.setBorder(new TitledBorder(null, "Email", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION,
            new Font("Segoe UI", Font.PLAIN, 16)));
        labelEmail.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        add(labelEmail, "cell 3 0 2 2,aligny center,grow 100 0");

        //---- labelNomeC ----
        labelNomeC.setText("Nome");
        labelNomeC.setIcon(null);
        labelNomeC.setHorizontalAlignment(SwingConstants.CENTER);
        labelNomeC.setHorizontalTextPosition(SwingConstants.CENTER);
        labelNomeC.setVerticalTextPosition(SwingConstants.BOTTOM);
        labelNomeC.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        add(labelNomeC, "cell 0 2 2 1,growy");
        add(separatorO, "cell 2 0 1 10");

        //---- labelClient ----
        labelClient.setDisabledIcon(UIManager.getIcon("OptionPane.warningIcon"));
        labelClient.setHorizontalAlignment(SwingConstants.CENTER);
        labelClient.setHorizontalTextPosition(SwingConstants.CENTER);
        labelClient.setIcon(UIManager.getIcon("FileView.fileIcon"));
        labelClient.setVerticalAlignment(SwingConstants.BOTTOM);
        add(labelClient, "cell 0 0 2 2,grow");

        //---- labelDataN ----
        labelDataN.setText("00-00-0000");
        labelDataN.setBorder(new TitledBorder(null, "Data di Nascita", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION,
            new Font("Segoe UI", Font.PLAIN, 16)));
        labelDataN.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        add(labelDataN, "cell 3 2 2 1,grow");
        add(separatorV, "cell 0 3 6 1");

        //---- buttonLog ----
        buttonLog.setText("Riepilogo Attivita");
        buttonLog.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        buttonLog.setIcon(UIManager.getIcon("FileView.directoryIcon"));
        add(buttonLog, "cell 0 4 2 1,align center center,grow 0 0");

        //---- buttonOfferte ----
        buttonOfferte.setText("Offerte");
        buttonOfferte.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        add(buttonOfferte, "cell 0 5,alignx center,growx 0");

        //---- buttonVisite ----
        buttonVisite.setText("Visite");
        buttonVisite.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        add(buttonVisite, "cell 1 5,alignx center,growx 0");

        //======== contenutoPanel ========
        {

            //======== panelInterno ========
            {
                panelInterno.setLayout(new BoxLayout(panelInterno, BoxLayout.X_AXIS));
            }
            contenutoPanel.setViewportView(panelInterno);
        }
        add(contenutoPanel, "cell 3 4 2 4,grow");

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
            panelMenuBarOpzioniColore.add(menuBarOpzioni, BorderLayout.LINE_END);

            //---- LogoOpzioni ----
            LogoOpzioni.setIcon(new FlatSVGIcon("Logo.svg"));
            LogoOpzioni.setIconTextGap(2);
            panelMenuBarOpzioniColore.add(LogoOpzioni, BorderLayout.LINE_START);

            //---- labelTitolo ----
            labelTitolo.setText("DietiEstates25 ");
            labelTitolo.setFont(new Font("Felix Titling", Font.BOLD | Font.ITALIC, 50));
            labelTitolo.setForeground(UIManager.getColor("Button.background"));
            labelTitolo.setHorizontalAlignment(SwingConstants.CENTER);
            labelTitolo.setIcon(null);
            labelTitolo.setHorizontalTextPosition(SwingConstants.CENTER);
            labelTitolo.setVerticalTextPosition(SwingConstants.BOTTOM);
            panelMenuBarOpzioniColore.add(labelTitolo, BorderLayout.CENTER);
        }
        add(panelMenuBarOpzioniColore, "north,growx");
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }



    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    // Generated using JFormDesigner Educational license - Marco Scotto di Uccio Scotto di Uccio (Marco Scotto Di Uccio)
    private JLabel labelEmail;
    private JLabel labelNomeC;
    private JSeparator separatorO;
    private JLabel labelClient;
    private JLabel labelDataN;
    private JSeparator separatorV;
    private JButton buttonLog;
    private JButton buttonOfferte;
    private JButton buttonVisite;
    private JScrollPane contenutoPanel;
    private JPanel panelInterno;
    private JPanel panelMenuBarOpzioniColore;
    private JMenuBar menuBarOpzioni;
    private JMenu menuOpzioni;
    private JLabel LogoOpzioni;
    private JLabel labelTitolo;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
