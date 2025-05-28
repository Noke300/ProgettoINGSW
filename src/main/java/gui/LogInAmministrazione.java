/*
 * Created by JFormDesigner on Wed Apr 16 10:24:24 CEST 2025
 */

package gui;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;

import com.formdev.flatlaf.extras.*;

import controller.Controller;
import controller.ControllerAdministration;
import controller.ControllerClienti;
import gui.costum_components.BottoniOpzioni;
import model.Immobile;
import net.miginfocom.swing.*;

/**
 * @author marco
 */
public class LogInAmministrazione extends JPanel {
    private String token;
    public LogInAmministrazione(JFrame frame, Controller control, String token) {
        initComponents(frame);

         backButtonHome.addActionListener(e-> {
             frame.setContentPane(new Home().createHomePanel(frame));
             frame.revalidate();
             frame.repaint();
        });

         ricercaButton.addActionListener(e-> {

             frame.setContentPane(new Ricerca().createRicercaPanel(frame,control,token));
             frame.revalidate();
             frame.repaint();

         });
         
         buttonLogIn.addActionListener(e-> {
             String token1;
             try {
                 token1 = control.loginAgente(textFildEmail.getText(), passwordField1.getText());

                 if (token1.isEmpty()) {
                     JOptionPane.showMessageDialog(
                             frame,
                             "Login fallito. Riprova!",
                             "Errore",
                             JOptionPane.ERROR_MESSAGE
                     );

                 }else {
                     this.token = token1;
                     ControllerAdministration controlAdmin = new ControllerAdministration();
                     List<Immobile> immobili = controlAdmin.fetchImmobili(token1);
                     BottoniOpzioni bottoniOpzioni = new BottoniOpzioni();
                     frame.setContentPane(new InterfacciaAgenteImmobiliare(frame,bottoniOpzioni,controlAdmin,this.token,immobili));
                     frame.revalidate();
                     frame.repaint();
                 }
             } catch (RuntimeException ex) {
                 throw new RuntimeException(ex);
             }
         });
    }

    @Override
    public void addNotify() {
        super.addNotify();
        // Assicurati che tutti i menu popup siano nascosti quando il pannello viene aggiunto
        MenuSelectionManager.defaultManager().clearSelectedPath();
    }

    private void initComponents(JFrame frame) {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Evaluation license - Marco
        labelTitolo = new JLabel();
        labelEmail = new JLabel();
        textFildEmail = new JTextField();
        labelPassword = new JLabel();
        passwordField1 = new JPasswordField();
        buttonRegistrati = new JButton();
        buttonLogIn = new JButton();
        buttonGoogle = new JButton();
        panelMenuBarOpzioniColore = new JPanel();
        menuBarOpzioni = new JMenuBar();
        menuOpzioni = new JMenu();
        logInButton = new JButton();
        ricercaButton = new JButton();
        buttonAccountAmministrazione = new JButton();
        backButtonHome = new JButton();
        LogoOpzioni = new JLabel();

        //======== this ========
        setBorder ( new javax . swing. border .CompoundBorder ( new javax . swing. border .TitledBorder ( new javax . swing. border .
        EmptyBorder ( 0, 0 ,0 , 0) ,  "JF\u006frmDesi\u0067ner Ev\u0061luatio\u006e" , javax. swing .border . TitledBorder. CENTER ,javax . swing
        . border .TitledBorder . BOTTOM, new java. awt .Font ( "Dialo\u0067", java .awt . Font. BOLD ,12 ) ,
        java . awt. Color .red ) , getBorder () ) );  addPropertyChangeListener( new java. beans .PropertyChangeListener ( )
        { @Override public void propertyChange (java . beans. PropertyChangeEvent e) { if( "borde\u0072" .equals ( e. getPropertyName () ) )
        throw new RuntimeException( ) ;} } );
        setLayout(new MigLayout(
            "fill,hidemode 3,align center center",
            // columns
            "[292,grow,fill]" +
            "[292,grow,fill]" +
            "[292,grow,fill]" +
            "[292,grow,fill]",
            // rows
            "[186,grow,fill]" +
            "[28,fill]" +
            "[69,fill]para" +
            "[186,grow,fill]0" +
            "[186,grow,top]0" +
            "[21,grow,fill]" +
            "[]"));

        //---- labelTitolo ----
        labelTitolo.setText("DietiEstates25 ");
        labelTitolo.setFont(new Font("Felix Titling", Font.BOLD | Font.ITALIC, 58));
        labelTitolo.setForeground(UIManager.getColor("Actions.Green"));
        labelTitolo.setHorizontalAlignment(SwingConstants.CENTER);
        add(labelTitolo, "cell 0 0 4 1,alignx center,growx 0");

        //---- labelEmail ----
        labelEmail.setText("Email :");
        labelEmail.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        add(labelEmail, "cell 0 1,alignx right,growx 0");

        //---- textFildEmail ----
        textFildEmail.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        add(textFildEmail, "cell 1 1 2 1,aligny center,growy 0");

        //---- labelPassword ----
        labelPassword.setText("Password :");
        labelPassword.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        add(labelPassword, "cell 0 2,alignx right,growx 0");

        //---- passwordField1 ----
        passwordField1.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        add(passwordField1, "cell 1 2 2 1,aligny center,growy 0");

        //---- buttonRegistrati ----
        buttonRegistrati.setText("Registrati");
        buttonRegistrati.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        add(buttonRegistrati, "cell 1 3,align center top,grow 0 0");

        //---- buttonLogIn ----
        buttonLogIn.setText("Log In");
        buttonLogIn.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        add(buttonLogIn, "cell 2 3,align center top,grow 0 0");

        //---- buttonGoogle ----
        buttonGoogle.setText("Crea un accaunt Aggenzia");
        buttonGoogle.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        add(buttonGoogle, "cell 1 4 2 1,align center top,grow 0 0");

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
        }
        add(panelMenuBarOpzioniColore, "north");
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    // Generated using JFormDesigner Evaluation license - Marco
    private JLabel labelTitolo;
    private JLabel labelEmail;
    private JTextField textFildEmail;
    private JLabel labelPassword;
    private JPasswordField passwordField1;
    private JButton buttonRegistrati;
    private JButton buttonLogIn;
    private JButton buttonGoogle;
    private JPanel panelMenuBarOpzioniColore;
    private JMenuBar menuBarOpzioni;
    private JMenu menuOpzioni;
    private JButton logInButton;
    private JButton ricercaButton;
    private JButton buttonAccountAmministrazione;
    private JButton backButtonHome;
    private JLabel LogoOpzioni;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
