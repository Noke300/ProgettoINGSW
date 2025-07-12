/*
 * Created by JFormDesigner on Wed Apr 16 10:24:24 CEST 2025
 */

package gui.pagine_administrative;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;

import com.formdev.flatlaf.extras.*;

import controller.Controller;
import controller.ControllerAdministration;
import controller.NotificationClient;
import gui.Ricerca;
import gui.costum_components.BottoniOpzioni;
import model.Immobile;
import model.ImmobileEImmagini;
import net.miginfocom.swing.*;

/**
 * @author marco
 */
public class LogInAmministrazione extends JPanel {
    private String token;
    BottoniOpzioni bottoniOpzioni = new BottoniOpzioni();

    public LogInAmministrazione(JFrame frame, Controller control, String token) {
        initComponents();
        bottoniOpzioni.setUpOpzioniMenu(frame,control,token,menuBarOpzioni,menuOpzioni);

        textFildEmail.addActionListener(e -> passwordField1.requestFocusInWindow());

        passwordField1.addActionListener(e -> buttonLogIn.doClick());

        buttonLogIn.addActionListener(e-> {
             String token1;
             char[] pass = passwordField1.getPassword();
             try {
                 token1 = control.loginAgente(textFildEmail.getText(), new String(pass));
                 if (token1.isEmpty()) {
                     JOptionPane.showMessageDialog(
                             frame,
                             "Login fallito. Riprova!",
                             "Errore",
                             JOptionPane.ERROR_MESSAGE
                     );
                    Arrays.fill(pass, ' ');
                 }else {
                     this.token = token1;

                     Ricerca.notifiche = new NotificationClient(textFildEmail.getText());
                     Ricerca.notifiche.start();

                     ControllerAdministration controlAdmin = new ControllerAdministration();
                     List<ImmobileEImmagini> immobiliEImmagini = controlAdmin.fetchImmobili(token1);
                     BottoniOpzioni bottoniOpzioni = new BottoniOpzioni();
                     frame.setContentPane(new InterfacciaAgenteImmobiliare(frame,bottoniOpzioni,controlAdmin,this.token,immobiliEImmagini));
                     Arrays.fill(pass, ' ');
                     frame.revalidate();
                     frame.repaint();
                 }
             } catch (RuntimeException ex) {
                 throw new RuntimeException(ex);
             }
         });

        buttonCreaAggenzia.addActionListener(e -> {
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
        // Generated using JFormDesigner Educational license - Marco Scotto di Uccio Scotto di Uccio (Marco Scotto Di Uccio)
        labelTitolo = new JLabel();
        labelEmail = new JLabel();
        textFildEmail = new JTextField();
        labelPassword = new JLabel();
        passwordField1 = new JPasswordField();
        buttonCreaAggenzia = new JButton();
        buttonLogIn = new JButton();
        panelMenuBarOpzioniColore = new JPanel();
        menuBarOpzioni = new JMenuBar();
        menuOpzioni = new JMenu();
        LogoOpzioni = new JLabel();

        //======== this ========
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

        //---- buttonCreaAggenzia ----
        buttonCreaAggenzia.setText("Crea un Aggenzia");
        buttonCreaAggenzia.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        add(buttonCreaAggenzia, "cell 1 3,align center top,grow 0 0");

        //---- buttonLogIn ----
        buttonLogIn.setText("Log In");
        buttonLogIn.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        add(buttonLogIn, "cell 1 4,align center top,grow 0 0");

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
    // Generated using JFormDesigner Educational license - Marco Scotto di Uccio Scotto di Uccio (Marco Scotto Di Uccio)
    private JLabel labelTitolo;
    private JLabel labelEmail;
    private JTextField textFildEmail;
    private JLabel labelPassword;
    private JPasswordField passwordField1;
    private JButton buttonCreaAggenzia;
    private JButton buttonLogIn;
    private JPanel panelMenuBarOpzioniColore;
    private JMenuBar menuBarOpzioni;
    private JMenu menuOpzioni;
    private JLabel LogoOpzioni;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
