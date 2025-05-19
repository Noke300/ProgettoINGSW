/*
 * Created by JFormDesigner on Sat Apr 19 05:32:39 CEST 2025
 */

package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import net.miginfocom.swing.*;

/**
 * @author marco
 */
public class AccountAmministrazione extends JPanel {
    public AccountAmministrazione() {
        initComponents();
    }

    private void menuBar1MouseClicked(MouseEvent e) {
        // TODO add your code here
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Evaluation license - Marco
        menuBar1 = new JMenuBar();
        menuOpzioni = new JMenu();
        logInButton = new JButton();
        ricercaButton = new JButton();
        backButtonHome = new JButton();
        labelEmail = new JLabel();
        textFildEmail = new JTextField();
        labelPassword = new JLabel();
        passwordField1 = new JPasswordField();
        buttonLogIn = new JButton();
        buttonRegistrati = new JButton();

        //======== this ========
        setBorder (new javax. swing. border. CompoundBorder( new javax .swing .border .TitledBorder (new javax. swing. border. EmptyBorder
        ( 0, 0, 0, 0) , "JF\u006frmDes\u0069gner \u0045valua\u0074ion", javax. swing. border. TitledBorder. CENTER, javax. swing. border
        . TitledBorder. BOTTOM, new java .awt .Font ("D\u0069alog" ,java .awt .Font .BOLD ,12 ), java. awt
        . Color. red) , getBorder( )) );  addPropertyChangeListener (new java. beans. PropertyChangeListener( ){ @Override public void
        propertyChange (java .beans .PropertyChangeEvent e) {if ("\u0062order" .equals (e .getPropertyName () )) throw new RuntimeException( )
        ; }} );
        setLayout(new MigLayout(
            "hidemode 3,align center center",
            // columns
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]",
            // rows
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]"));

        //======== menuBar1 ========
        {
            menuBar1.setBorderPainted(false);
            menuBar1.setBackground(new Color(0x4e5052));
            menuBar1.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    menuBar1MouseClicked(e);
                }
            });

            //======== menuOpzioni ========
            {
                menuOpzioni.setText("Opzioni");
                menuOpzioni.setIcon(UIManager.getIcon("Table.descendingSortIcon"));

                //---- logInButton ----
                logInButton.setText("Log In");
                menuOpzioni.add(logInButton);

                //---- ricercaButton ----
                ricercaButton.setText("Ricerca");
                ricercaButton.setIcon(UIManager.getIcon("PasswordField.revealIcon"));
                menuOpzioni.add(ricercaButton);

                //---- backButtonHome ----
                backButtonHome.setText("Home");
                backButtonHome.setIcon(UIManager.getIcon("FileChooser.homeFolderIcon"));
                menuOpzioni.add(backButtonHome);
            }
            menuBar1.add(menuOpzioni);
        }
        add(menuBar1, "cell 3 0,alignx center,growx 0");

        //---- labelEmail ----
        labelEmail.setText("Email :");
        add(labelEmail, "cell 0 1");
        add(textFildEmail, "cell 1 1 3 1");

        //---- labelPassword ----
        labelPassword.setText("Password :");
        add(labelPassword, "cell 0 2");
        add(passwordField1, "cell 1 2 3 1");

        //---- buttonLogIn ----
        buttonLogIn.setText("Log In");
        add(buttonLogIn, "cell 2 3");

        //---- buttonRegistrati ----
        buttonRegistrati.setText("Inizia con Noi");
        add(buttonRegistrati, "cell 2 4");
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    // Generated using JFormDesigner Evaluation license - Marco
    private JMenuBar menuBar1;
    private JMenu menuOpzioni;
    private JButton logInButton;
    private JButton ricercaButton;
    private JButton backButtonHome;
    private JLabel labelEmail;
    private JTextField textFildEmail;
    private JLabel labelPassword;
    private JPasswordField passwordField1;
    private JButton buttonLogIn;
    private JButton buttonRegistrati;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
