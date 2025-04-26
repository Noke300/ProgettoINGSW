/*
 * Created by JFormDesigner on Wed Apr 16 10:24:24 CEST 2025
 */

package gui;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import net.miginfocom.swing.*;

/**
 * @author marco
 */
public class LogIn extends JPanel {
    public LogIn(JFrame frame) {
        initComponents(frame);

         backButtonHome.addActionListener(e-> {

             frame.setContentPane(new Home().createHomePanel(frame));
             frame.revalidate();
             frame.repaint();
        });

         ricercaButton.addActionListener(e-> {

             frame.setContentPane(new Ricerca().createRicercaPanel(frame));
             frame.revalidate();
             frame.repaint();
         });
    }

    @Override
    public void addNotify() {
        super.addNotify();
        // Assicurati che tutti i menu popup siano nascosti quando il pannello viene aggiunto
        MenuSelectionManager.defaultManager().clearSelectedPath();
    }

    private void menuBar1MouseClicked(MouseEvent e) {
        menuOpzioni.setVisible(false);
        menuOpzioni.setVisible(false);
        menuBar1.setEnabled(false);
        menuOpzioni.setEnabled(false);

    }

    private void initComponents(JFrame frame) {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Evaluation license - Marco GuIdo Scotto di Uccio
        menuBar1 = new JMenuBar();
        menuOpzioni = new JMenu();
        logInButton = new JButton();
        ricercaButton = new JButton();
        backButtonHome = new JButton();
        labelEmail = new JLabel();
        textFildEmail = new JTextField();
        labelPassword = new JLabel();
        passwordField1 = new JPasswordField();
        buttonRegistrati = new JButton();
        buttonLogIn = new JButton();
        buttonGoogle = new JButton();

        //======== this ========
        setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder(new javax.swing.border.
        EmptyBorder(0,0,0,0), "JF\u006frmDesi\u0067ner Ev\u0061luatio\u006e",javax.swing.border.TitledBorder.CENTER,javax.swing
        .border.TitledBorder.BOTTOM,new java.awt.Font("Dialo\u0067",java.awt.Font.BOLD,12),
        java.awt.Color.red), getBorder())); addPropertyChangeListener(new java.beans.PropertyChangeListener()
        {@Override public void propertyChange(java.beans.PropertyChangeEvent e){if("borde\u0072".equals(e.getPropertyName()))
        throw new RuntimeException();}});
        setLayout(new MigLayout(
            "hidemode 3,align center center",
            // columns
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
            "[fill]" +
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
            "[]" +
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
        add(menuBar1, "cell 8 0,alignx center,growx 0");

        //---- labelEmail ----
        labelEmail.setText("Email :");
        add(labelEmail, "cell 1 3");
        add(textFildEmail, "cell 2 3 5 1,aligny center,grow 100 0");

        //---- labelPassword ----
        labelPassword.setText("Password :");
        add(labelPassword, "cell 1 4");
        add(passwordField1, "cell 2 4 5 1,aligny center,grow 100 0");

        //---- buttonRegistrati ----
        buttonRegistrati.setText("Registrati");
        add(buttonRegistrati, "cell 2 6");

        //---- buttonLogIn ----
        buttonLogIn.setText("Log In");
        add(buttonLogIn, "cell 3 6");

        //---- buttonGoogle ----
        buttonGoogle.setText("Google");
        add(buttonGoogle, "cell 2 9");
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    // Generated using JFormDesigner Evaluation license - Marco GuIdo Scotto di Uccio
    private JMenuBar menuBar1;
    private JMenu menuOpzioni;
    private JButton logInButton;
    private JButton ricercaButton;
    private JButton backButtonHome;
    private JLabel labelEmail;
    private JTextField textFildEmail;
    private JLabel labelPassword;
    private JPasswordField passwordField1;
    private JButton buttonRegistrati;
    private JButton buttonLogIn;
    private JButton buttonGoogle;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
