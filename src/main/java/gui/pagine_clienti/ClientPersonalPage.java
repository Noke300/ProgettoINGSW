/*
 * Created by JFormDesigner on Wed Jul 02 01:30:41 CEST 2025
 */

package gui.pagine_clienti;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import com.formdev.flatlaf.extras.*;
import controller.ControllerClienti;
import net.miginfocom.swing.*;

/**
 * @author marco
 */
public class ClientPersonalPage extends JPanel {

    public ClientPersonalPage(JFrame frame, ControllerClienti controllerClienti,String token) {
        initComponents();

    buttonOfferte.addActionListener(e ->{
        final JDialog offertePanel = new JDialog(frame, "Offerte", true);
        offertePanel.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        offertePanel.setSize(700, 550); // Imposta una dimensione per il dialogo della mappa
        offertePanel.setContentPane(offertePanel);
    
    });


    }

    @Override
    public void addNotify() {
        super.addNotify();
        // Assicurati che tutti i menu popup siano nascosti quando il pannello viene aggiunto
        MenuSelectionManager.defaultManager().clearSelectedPath();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Evaluation license - Marco Scotto
        labelEmail = new JLabel();
        labelNomeC = new JLabel();
        labelDataN = new JLabel();
        buttonLog = new JButton();
        buttonOfferte = new JButton();
        buttonVisite = new JButton();
        panelMenuBarOpzioniColore = new JPanel();
        menuBarOpzioni = new JMenuBar();
        menuOpzioni = new JMenu();
        LogoOpzioni = new JLabel();
        labelTitolo = new JLabel();
        LogoOpzioni2 = new JLabel();

        //======== this ========
        setBorder ( new javax . swing. border .CompoundBorder ( new javax . swing. border .TitledBorder ( new javax . swing
        . border .EmptyBorder ( 0, 0 ,0 , 0) ,  "JFor\u006dDesi\u0067ner \u0045valu\u0061tion" , javax. swing .border . TitledBorder
        . CENTER ,javax . swing. border .TitledBorder . BOTTOM, new java. awt .Font ( "Dia\u006cog", java .
        awt . Font. BOLD ,12 ) ,java . awt. Color .red ) , getBorder () ) )
        ;  addPropertyChangeListener( new java. beans .PropertyChangeListener ( ){ @Override public void propertyChange (java . beans. PropertyChangeEvent e
        ) { if( "bord\u0065r" .equals ( e. getPropertyName () ) )throw new RuntimeException( ) ;} } )
        ;
        setLayout(new MigLayout(
            "fill,insets 0,hidemode 3,align center center,gap 10 5",
            // columns
            "0[118,fill]0" +
            "[74,fill]0" +
            "[111,grow,center]0" +
            "[225,center]0" +
            "[62,shrink 0,fill]0",
            // rows
            "[73,top]0" +
            "[73,center]0" +
            "[73,center]0" +
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
        add(labelEmail, "cell 2 0 2 2,grow");

        //---- labelNomeC ----
        labelNomeC.setText("Nome");
        labelNomeC.setIcon(UIManager.getIcon("FileChooser.listViewIcon"));
        labelNomeC.setHorizontalAlignment(SwingConstants.CENTER);
        labelNomeC.setHorizontalTextPosition(SwingConstants.CENTER);
        labelNomeC.setVerticalTextPosition(SwingConstants.BOTTOM);
        labelNomeC.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        add(labelNomeC, "cell 0 1 2 2,growy");

        //---- labelDataN ----
        labelDataN.setText("00-00-0000");
        labelDataN.setBorder(new TitledBorder(null, "Data di Nascita", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION,
            new Font("Segoe UI", Font.PLAIN, 16)));
        labelDataN.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        add(labelDataN, "cell 2 2 2 1,grow");

        //---- buttonLog ----
        buttonLog.setText("Riepilogo Attivita");
        buttonLog.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        buttonLog.setIcon(UIManager.getIcon("FileView.directoryIcon"));
        add(buttonLog, "cell 0 3 2 1,align center center,grow 0 0");

        //---- buttonOfferte ----
        buttonOfferte.setText("Offerte");
        buttonOfferte.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        add(buttonOfferte, "cell 0 4,alignx center,growx 0");

        //---- buttonVisite ----
        buttonVisite.setText("Visite");
        buttonVisite.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        add(buttonVisite, "cell 1 4,alignx center,growx 0");

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
        add(LogoOpzioni2, "cell 0 8 2 1,align center center,grow 0 0");
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    // Generated using JFormDesigner Evaluation license - Marco Scotto
    private JLabel labelEmail;
    private JLabel labelNomeC;
    private JLabel labelDataN;
    private JButton buttonLog;
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
