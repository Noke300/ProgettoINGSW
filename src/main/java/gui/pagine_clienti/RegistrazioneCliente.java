package gui.pagine_clienti;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.border.*;
import com.formdev.flatlaf.extras.*;
import com.toedter.calendar.*;
import controller.Controller;
import gui.costum_components.BottoniOpzioni;
import gui.pagine_comuni.Ricerca;
import gui.costum_components.IconClass;
import model.Cliente;
import net.miginfocom.swing.*;

public class RegistrazioneCliente extends JPanel {
    BottoniOpzioni bottoniOpzioni = new BottoniOpzioni();

    public RegistrazioneCliente(JFrame frame,Controller control) {
       initComponents();
       iconSetUp();
       hellperBoxs();

       bottoniOpzioni.setUpOpzioniMenu(frame,control,"",menuBarOpzioni,menuOpzioni);

        buttonCalender.addActionListener(e -> {
            final JDialog calendarDialog = new JDialog(frame, "Calendario", true);
            calendarDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            calendarDialog.setSize(700, 550);
            calendarDialog.setLocationRelativeTo(null); // Centra la finestra

            calendarDialog.setContentPane(calendarSelector);

            // Aggiungiamo il listener con doppio click sul pannello dei giorni
            JPanel dayPanel = calendarSelector.getDayChooser().getDayPanel();
            for (Component comp : dayPanel.getComponents()) {
                comp.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (e.getClickCount() == 2) {
                            java.util.Date selectedDate = calendarSelector.getDate();
                            String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(selectedDate);
                            textFildData.setText(formattedDate);
                            calendarDialog.dispose();
                        }
                    }
                });
            }

            calendarDialog.setVisible(true);
        });



        buttonRegistrati.addActionListener(e -> {
            char[] tempPass = passwordField1.getPassword();
            Date date = Date.valueOf(textFildData.getText());

            Cliente newC = new Cliente(textFildNome.getText(),textFildCognome.getText(),textFildEmail.getText(),
                    new String(tempPass), date);
            if(control.registrazioneCliente(newC)){
                String tokenT = null;
                tokenT = control.logInClient(newC.getEmailcliente(),newC.getPasswordcliente());
                frame.setContentPane(new Ricerca(frame,control,tokenT));
                Arrays.fill(tempPass,'0');
                frame.revalidate();
                frame.repaint();
            }else{
                Arrays.fill(tempPass,'0');
                System.out.println("Errore");
            }

        });

    }
    private void iconSetUp(){
        buttonCalender.setIcon(IconClass.getCalendarioIcon());
        iconEmail.setIcon(IconClass.getEmailIcon());
        iconCognome.setIcon(IconClass.getNomeCliente());
        iconNome.setIcon(IconClass.getNomeCliente());
        iconPass.setIcon(IconClass.getPasswordIcon());
    }
    
    private void hellperBoxs(){
        JWindow tooltipEmail = new JWindow();
        JLabel labelTooltipEmail = new JLabel("Inserisci un'email valida (es: nome@email.com)");
        labelTooltipEmail.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        labelTooltipEmail.setOpaque(true);
        labelTooltipEmail.setBackground(Color.LIGHT_GRAY);
        labelTooltipEmail.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        tooltipEmail.getContentPane().add(labelTooltipEmail);
        tooltipEmail.pack();

        textFildEmail.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                try {
                    Point p = textFildEmail.getLocationOnScreen();
                    tooltipEmail.setLocation(p.x, p.y + textFildEmail.getHeight() + 4);
                    tooltipEmail.setVisible(true);
                } catch (IllegalComponentStateException ex) {
                    System.err.println("Tooltip email non visibile: " + ex.getMessage());
                }
            }
            public void focusLost(FocusEvent e) {
                tooltipEmail.setVisible(false);
            }
        });

        JWindow tooltipPassword = new JWindow();
        JLabel labelTooltipPassword = new JLabel("Min 8 caratteri, 1 maiuscola, 1 numero, 1 simbolo");
        labelTooltipPassword.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        labelTooltipPassword.setOpaque(true);
        labelTooltipPassword.setBackground(Color.LIGHT_GRAY);
        labelTooltipPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        tooltipPassword.getContentPane().add(labelTooltipPassword);
        tooltipPassword.pack();

        passwordField1.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                try {
                    Point p = passwordField1.getLocationOnScreen();
                    tooltipPassword.setLocation(p.x, p.y + passwordField1.getHeight() + 4);
                    tooltipPassword.setVisible(true);
                } catch (IllegalComponentStateException ex) {
                    System.err.println("Tooltip password non visibile: " + ex.getMessage());
                }
            }
            public void focusLost(FocusEvent e) {
                tooltipPassword.setVisible(false);
            }
        });
        JWindow tooltipData = new JWindow();
        JLabel labelTooltipData = new JLabel("Formato richiesto: dd/MM/yyyy (es: 31/12/2025)");
        labelTooltipData.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        labelTooltipData.setOpaque(true);
        labelTooltipData.setBackground(Color.LIGHT_GRAY);
        labelTooltipData.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        tooltipData.getContentPane().add(labelTooltipData);
        tooltipData.pack();

        textFildData.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                try {
                    Point p = textFildData.getLocationOnScreen();
                    tooltipData.setLocation(p.x, p.y + textFildData.getHeight() + 4);
                    tooltipData.setVisible(true);
                } catch (IllegalComponentStateException ex) {
                    System.err.println("Tooltip data non visibile: " + ex.getMessage());
                }
            }
            public void focusLost(FocusEvent e) {
                tooltipData.setVisible(false);
            }
        });

    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Educational license - Marco Scotto di Uccio Scotto di Uccio (Marco Scotto Di Uccio)
        labelNome = new JLabel();
        textFildNome = new JTextField();
        iconNome = new JLabel();
        labelCognome = new JLabel();
        textFildCognome = new JTextField();
        iconCognome = new JLabel();
        labelData = new JLabel();
        textFildData = new JTextField();
        buttonCalender = new JButton();
        labelEmail = new JLabel();
        textFildEmail = new JTextField();
        iconEmail = new JLabel();
        labelPassword = new JLabel();
        passwordField1 = new JPasswordField();
        iconPass = new JLabel();
        buttonRegistrati = new JButton();
        panelMenuBarOpzioniColore = new JPanel();
        menuBarOpzioni = new JMenuBar();
        menuOpzioni = new JMenu();
        LogoOpzioni = new JLabel();
        labelTitolo = new JLabel();
        calendarSelector = new JCalendar();

        //======== this ========
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
        add(iconNome, "cell 4 0");

        //---- labelCognome ----
        labelCognome.setText("Cognome :");
        labelCognome.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        add(labelCognome, "cell 0 1,alignx right,growx 0");

        //---- textFildCognome ----
        textFildCognome.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        add(textFildCognome, "cell 1 1 3 1,aligny center,growy 0");
        add(iconCognome, "cell 4 1");

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
        add(buttonCalender, "cell 3 2,alignx left,growx 0");

        //---- labelEmail ----
        labelEmail.setText("Email :");
        labelEmail.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        add(labelEmail, "cell 0 3,alignx right,growx 0");

        //---- textFildEmail ----
        textFildEmail.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        add(textFildEmail, "cell 1 3 3 1,aligny center,growy 0");
        add(iconEmail, "cell 4 3");

        //---- labelPassword ----
        labelPassword.setText("Password :");
        labelPassword.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        add(labelPassword, "cell 0 6,alignx right,growx 0");

        //---- passwordField1 ----
        passwordField1.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        add(passwordField1, "cell 1 6 3 1,aligny center,growy 0");
        add(iconPass, "cell 4 6");

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
    // Generated using JFormDesigner Educational license - Marco Scotto di Uccio Scotto di Uccio (Marco Scotto Di Uccio)
    private JLabel labelNome;
    private JTextField textFildNome;
    private JLabel iconNome;
    private JLabel labelCognome;
    private JTextField textFildCognome;
    private JLabel iconCognome;
    private JLabel labelData;
    private JTextField textFildData;
    private JButton buttonCalender;
    private JLabel labelEmail;
    private JTextField textFildEmail;
    private JLabel iconEmail;
    private JLabel labelPassword;
    private JPasswordField passwordField1;
    private JLabel iconPass;
    private JButton buttonRegistrati;
    private JPanel panelMenuBarOpzioniColore;
    private JMenuBar menuBarOpzioni;
    private JMenu menuOpzioni;
    private JLabel LogoOpzioni;
    private JLabel labelTitolo;
    private JCalendar calendarSelector;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
