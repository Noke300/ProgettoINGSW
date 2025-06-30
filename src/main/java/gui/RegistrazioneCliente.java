package gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import com.formdev.flatlaf.extras.*;
import com.toedter.calendar.*;
import net.miginfocom.swing.*;

public class RegistrazioneCliente extends JPanel {
    public RegistrazioneCliente() {

       initComponents();
       buttonCalender.addActionListener(e ->{

       });
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Evaluation license - Marco Scotto
        labelNome = new JLabel();
        textFildNome = new JTextField();
        labelCognome = new JLabel();
        textFildCognome = new JTextField();
        labelData = new JLabel();
        textFildData = new JTextField();
        buttonCalender = new JButton();
        labelEmail = new JLabel();
        textFildEmail = new JTextField();
        labelPassword = new JLabel();
        passwordField1 = new JPasswordField();
        buttonRegistrati = new JButton();
        panelMenuBarOpzioniColore = new JPanel();
        menuBarOpzioni = new JMenuBar();
        menuOpzioni = new JMenu();
        logInButton = new JButton();
        ricercaButton = new JButton();
        buttonAccountAmministrazione = new JButton();
        backButtonHome = new JButton();
        LogoOpzioni = new JLabel();
        labelTitolo = new JLabel();
        calendarSelector = new JCalendar();

        //======== this ========
        setBorder (new javax. swing. border. CompoundBorder( new javax .swing .border .TitledBorder (new javax. swing. border.
        EmptyBorder( 0, 0, 0, 0) , "JF\u006frmD\u0065sig\u006eer \u0045val\u0075ati\u006fn", javax. swing. border. TitledBorder. CENTER, javax. swing
        . border. TitledBorder. BOTTOM, new java .awt .Font ("Dia\u006cog" ,java .awt .Font .BOLD ,12 ),
        java. awt. Color. red) , getBorder( )) );  addPropertyChangeListener (new java. beans. PropertyChangeListener( )
        { @Override public void propertyChange (java .beans .PropertyChangeEvent e) {if ("\u0062ord\u0065r" .equals (e .getPropertyName () ))
        throw new RuntimeException( ); }} );
        setLayout(new MigLayout(
            "fill,hidemode 3,align center center",
            // columns
            "[292,grow,fill]" +
            "[292,grow,fill]" +
            "[292,grow,fill]" +
            "[292,grow,fill]" +
            "[292,grow,fill]",
            // rows
            "rel[186,grow,fill]0" +
            "[186,grow,fill]0" +
            "[186,grow,fill]0" +
            "[186,grow,fill]" +
            "[28,fill]0" +
            "[69,fill]0" +
            "[186,grow,fill]0" +
            "[186,grow,top]0" +
            "[21,grow,fill]0" +
            "[]"));

        //---- labelNome ----
        labelNome.setText("Nome :");
        labelNome.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        add(labelNome, "cell 0 0,alignx right,growx 0");

        //---- textFildNome ----
        textFildNome.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        add(textFildNome, "cell 1 0 3 1,aligny center,growy 0");

        //---- labelCognome ----
        labelCognome.setText("Cognome :");
        labelCognome.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        add(labelCognome, "cell 0 1,alignx right,growx 0");

        //---- textFildCognome ----
        textFildCognome.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        add(textFildCognome, "cell 1 1 3 1,aligny center,growy 0");

        //---- labelData ----
        labelData.setText("Data di Nascita");
        labelData.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        add(labelData, "cell 0 2,alignx right,growx 0");

        //---- textFildData ----
        textFildData.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        add(textFildData, "cell 1 2 2 1,aligny center,growy 0");

        //---- buttonCalender ----
        buttonCalender.setIcon(UIManager.getIcon("OptionPane.questionIcon"));
        buttonCalender.setBackground(new Color(0x00ffffff, true));
        buttonCalender.setBorder(null);
        add(buttonCalender, "cell 3 2");

        //---- labelEmail ----
        labelEmail.setText("Email :");
        labelEmail.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        add(labelEmail, "cell 0 3,alignx right,growx 0");

        //---- textFildEmail ----
        textFildEmail.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        add(textFildEmail, "cell 1 3 3 1,aligny center,growy 0");

        //---- labelPassword ----
        labelPassword.setText("Password :");
        labelPassword.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        add(labelPassword, "cell 0 6,alignx right,growx 0");

        //---- passwordField1 ----
        passwordField1.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        add(passwordField1, "cell 1 6 3 1,aligny center,growy 0");

        //---- buttonRegistrati ----
        buttonRegistrati.setText("Registrati");
        buttonRegistrati.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        add(buttonRegistrati, "cell 4 7,aligny center,grow 100 0");

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

                    //---- logInButton ----
                    logInButton.setText("Log In");
                    logInButton.setIcon(UIManager.getIcon("FileChooser.detailsViewIcon"));
                    logInButton.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                    menuOpzioni.add(logInButton);

                    //---- ricercaButton ----
                    ricercaButton.setText("Ricerca");
                    ricercaButton.setIcon(UIManager.getIcon("PasswordField.revealIcon"));
                    ricercaButton.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                    menuOpzioni.add(ricercaButton);

                    //---- buttonAccountAmministrazione ----
                    buttonAccountAmministrazione.setText("Account Amministrazione");
                    buttonAccountAmministrazione.setIcon(UIManager.getIcon("FileChooser.newFolderIcon"));
                    buttonAccountAmministrazione.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                    menuOpzioni.add(buttonAccountAmministrazione);

                    //---- backButtonHome ----
                    backButtonHome.setHorizontalTextPosition(SwingConstants.RIGHT);
                    backButtonHome.setText("Home");
                    backButtonHome.setVerticalAlignment(SwingConstants.TOP);
                    backButtonHome.setVerticalTextPosition(SwingConstants.TOP);
                    backButtonHome.setIcon(UIManager.getIcon("FileChooser.homeFolderIcon"));
                    backButtonHome.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                    menuOpzioni.add(backButtonHome);
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
            labelTitolo.setFont(new Font("Felix Titling", Font.BOLD | Font.ITALIC, 52));
            labelTitolo.setHorizontalAlignment(SwingConstants.CENTER);
            labelTitolo.setForeground(Color.white);
            panelMenuBarOpzioniColore.add(labelTitolo, BorderLayout.CENTER);
        }
        add(panelMenuBarOpzioniColore, "north");
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    // Generated using JFormDesigner Evaluation license - Marco Scotto
    private JLabel labelNome;
    private JTextField textFildNome;
    private JLabel labelCognome;
    private JTextField textFildCognome;
    private JLabel labelData;
    private JTextField textFildData;
    private JButton buttonCalender;
    private JLabel labelEmail;
    private JTextField textFildEmail;
    private JLabel labelPassword;
    private JPasswordField passwordField1;
    private JButton buttonRegistrati;
    private JPanel panelMenuBarOpzioniColore;
    private JMenuBar menuBarOpzioni;
    private JMenu menuOpzioni;
    private JButton logInButton;
    private JButton ricercaButton;
    private JButton buttonAccountAmministrazione;
    private JButton backButtonHome;
    private JLabel LogoOpzioni;
    private JLabel labelTitolo;
    private JCalendar calendarSelector;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
