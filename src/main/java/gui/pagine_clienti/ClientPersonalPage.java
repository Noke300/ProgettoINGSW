/*
 * Created by JFormDesigner on Wed Jul 02 01:30:41 CEST 2025
 */

package gui.pagine_clienti;

import java.awt.*;
import java.text.SimpleDateFormat;
import javax.swing.*;
import javax.swing.border.*;
import com.formdev.flatlaf.extras.*;
import controller.ControllerClienti;
import model.Cliente;
import net.miginfocom.swing.*;

/**
 * @author marco
 */
public class ClientPersonalPage extends JPanel {

    public ClientPersonalPage(JFrame frame, ControllerClienti controllerClienti,String token) {
        initComponents();
        Cliente cliente = controllerClienti.getSpecifiche(token);

        buttonOfferte.addActionListener(e ->{
            final JDialog offertePanel = new JDialog(frame, "Offerte", true);
            offertePanel.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            offertePanel.setSize(700, 550); // Imposta una dimensione per il dialogo della mappa
            offertePanel.setContentPane(offertePanel);

        });

        labelEmail.setText(cliente.getEmailcliente());

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        labelDataN.setText(formatter.format(cliente.getDataN()));
        labelNomeC.setText(cliente.getNome() + " " + cliente.getCognome());

        //TODO non funzionano non so perché
        separatorO.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1)); // Larghezza piena, altezza 1px
        separatorO.setForeground(Color.LIGHT_GRAY); // Colore del tratto

        separatorV.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1)); // Larghezza piena, altezza 1px
        separatorV.setForeground(Color.LIGHT_GRAY); // Colore del tratto

        datiLabel.setText("<html> Situato nel cuore di una tranquilla zona residenziale, questo splendido appartamento rappresenta la perfetta fusione tra eleganza classica e comfort moderno. L’immobile, recentemente ristrutturato con materiali di pregio, si sviluppa su una superficie di circa 120 metri quadrati distribuiti in modo armonioso e funzionale. All’ingresso si viene accolti da un ampio soggiorno luminoso, impreziosito da pavimenti in parquet naturale, soffitti alti e grandi finestre che garantiscono una meravigliosa esposizione solare per gran parte della giornata.\n" +
                "\n" +
                "La cucina abitabile, separata ma comunicante con la zona living tramite una raffinata porta scorrevole in vetro satinato, è dotata di elettrodomestici di ultima generazione, piano a induzione e finiture in acciaio inox. La zona notte comprende tre camere da letto, di cui una matrimoniale con bagno privato dotato di box doccia in cristallo e dettagli in marmo travertino. Un secondo bagno, anch’esso finemente arredato, serve le altre due camere. Entrambi i bagni sono provvisti di riscaldamento a pavimento e specchi retroilluminati.\n" +
                "\n" +
                "Completano la proprietà una cabina armadio, una lavanderia interna e un balcone che si affaccia su un curato cortile condominiale con area verde. L’appartamento è inoltre dotato di impianto di climatizzazione canalizzato, tapparelle elettriche, sistema di allarme e connessione in fibra ottica. Possibilità di acquisto di un box auto doppio nelle immediate vicinanze. Ideale per famiglie, coppie o professionisti che cercano un’abitazione di prestigio in una delle zone più ambite della città, a pochi minuti da scuole, parchi, mezzi pubblici e negozi.</html>");

    }

    @Override
    public void addNotify() {
        super.addNotify();
        // Assicurati che tutti i menu popup siano nascosti quando il pannello viene aggiunto
        MenuSelectionManager.defaultManager().clearSelectedPath();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Evaluation license - Alessio Nesi
        labelEmail = new JLabel();
        labelNomeC = new JLabel();
        separatorO = new JSeparator();
        labelDataN = new JLabel();
        separatorV = new JSeparator();
        buttonLog = new JButton();
        datiLabel = new JLabel();
        contenutoPanel = new JScrollPane();
        buttonOfferte = new JButton();
        buttonVisite = new JButton();
        panelMenuBarOpzioniColore = new JPanel();
        menuBarOpzioni = new JMenuBar();
        menuOpzioni = new JMenu();
        LogoOpzioni = new JLabel();
        labelTitolo = new JLabel();
        LogoOpzioni2 = new JLabel();

        //======== this ========
        setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder
        (0,0,0,0), "JFor\u006dDesi\u0067ner \u0045valu\u0061tion",javax.swing.border.TitledBorder.CENTER,javax.swing.border
        .TitledBorder.BOTTOM,new java.awt.Font("Dia\u006cog",java.awt.Font.BOLD,12),java.awt
        .Color.red), getBorder())); addPropertyChangeListener(new java.beans.PropertyChangeListener(){@Override public void
        propertyChange(java.beans.PropertyChangeEvent e){if("bord\u0065r".equals(e.getPropertyName()))throw new RuntimeException()
        ;}});
        setLayout(new MigLayout(
            "fill,insets 0,hidemode 3,align center center,gap 10 5",
            // columns
            "0[118,fill]0" +
            "[74,fill]0" +
            "[fill]0" +
            "[111,grow,center]0" +
            "[225,center]0" +
            "[62,shrink 0,fill]0",
            // rows
            "[73,top]0" +
            "[73,center]0" +
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
        labelNomeC.setIcon(UIManager.getIcon("FileChooser.listViewIcon"));
        labelNomeC.setHorizontalAlignment(SwingConstants.CENTER);
        labelNomeC.setHorizontalTextPosition(SwingConstants.CENTER);
        labelNomeC.setVerticalTextPosition(SwingConstants.BOTTOM);
        labelNomeC.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        add(labelNomeC, "cell 0 1 2 2,growy");
        add(separatorO, "cell 2 0 1 10");

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

        //---- datiLabel ----
        datiLabel.setText("Dati");
        datiLabel.setVerticalAlignment(SwingConstants.TOP);
        add(datiLabel, "cell 3 4 2 6");
        add(contenutoPanel, "cell 3 4 2 6");

        //---- buttonOfferte ----
        buttonOfferte.setText("Offerte");
        buttonOfferte.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        add(buttonOfferte, "cell 0 5,alignx center,growx 0");

        //---- buttonVisite ----
        buttonVisite.setText("Visite");
        buttonVisite.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        add(buttonVisite, "cell 1 5,alignx center,growx 0");

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

        //---- LogoOpzioni2 ----
        LogoOpzioni2.setIcon(new FlatSVGIcon("Logo.svg"));
        LogoOpzioni2.setIconTextGap(2);
        add(LogoOpzioni2, "cell 0 9 2 1,align center center,grow 0 0");
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    // Generated using JFormDesigner Evaluation license - Alessio Nesi
    private JLabel labelEmail;
    private JLabel labelNomeC;
    private JSeparator separatorO;
    private JLabel labelDataN;
    private JSeparator separatorV;
    private JButton buttonLog;
    private JLabel datiLabel;
    private JScrollPane contenutoPanel;
    private JButton buttonOfferte;
    private JButton buttonVisite;
    private JPanel panelMenuBarOpzioniColore;
    private JMenuBar menuBarOpzioni;
    private JMenu menuOpzioni;
    private JLabel LogoOpzioni;
    private JLabel labelTitolo;
    private JLabel LogoOpzioni2;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
