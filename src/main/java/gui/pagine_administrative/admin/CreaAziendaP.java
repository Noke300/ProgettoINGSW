/*
 * Created by JFormDesigner on Fri Jul 18 11:13:58 CEST 2025
 */

package gui.pagine_administrative.admin;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.swing.*;
import javax.swing.border.*;
import com.formdev.flatlaf.extras.*;
import controller.Controller;
import gui.costum_components.BottoniOpzioni;
import gui.pagine_administrative.LogInAmministrazione;
import gui.pagine_administrative.agenteImmobliiare.InserimentoImmobile;
import model.AgenziaImmobiliare;
import net.miginfocom.swing.*;

/**
 * @author marco
 */
public class CreaAziendaP extends JPanel {
    public CreaAziendaP(JFrame frame , Controller controller, BottoniOpzioni bottoniOpzioni) {
        initComponents();
        bottoniOpzioni.setUpOpzioniMenu(frame,controller,"",menuBarOpzioni,menuOpzioni);


        Map<String, List<String>> regioneMap = InserimentoImmobile.regioniProvinceLabelMap();
        String[] regioni = regioneMap.keySet().toArray(new String[0]);
        for (String s : regioni){
            comboBoxRegione.addItem(s);
        }

    buttonRegistrati.addActionListener(e -> {
        if(textFildAdmin.getText().isEmpty() || textFildEmailAzienda.getText().isEmpty() || textFildNome.getText().isEmpty()){
            JOptionPane.showMessageDialog(frame, "Compila tutti i campi correttamente.");
        }else{
            String password = capitalizeFirstLetter(textFildNome.getText());
            AgenziaImmobiliare agenziaImmobiliare = new AgenziaImmobiliare(textFildNome.getText(),textFildEmailAzienda.getText(),textFildAdmin.getText(),password, comboBoxRegione.getSelectedItem().toString());

            if(controller.registrazioneAgenzia(agenziaImmobiliare)){
                JOptionPane.showMessageDialog(frame, "Registrazione avvenuta con successo.\nEmail: "+textFildAdmin.getText()+"\nPassword : "+password);
                frame.setContentPane(new LogInAmministrazione(frame,controller,""));
                frame.revalidate();
                frame.repaint();
            }else{
                JOptionPane.showMessageDialog(frame, "Registrazione fallita.");
            }


        }
    });

    }


    private static String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) return str;

        char firstChar = str.charAt(0);
        if (Character.isLowerCase(firstChar)) {
            firstChar = Character.toUpperCase(firstChar);
        }
        String nome = firstChar + str.substring(1);

        Random random = new Random();
        int numeroCasuale = random.nextInt(100) + 1;
        nome = nome.replaceAll(" ","");

        return nome + numeroCasuale + "!";
    }
    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Educational license - Marco Scotto di Uccio Scotto di Uccio (Marco Scotto Di Uccio)
        labelNome = new JLabel();
        textFildNome = new JTextField();
        iconNome = new JLabel();
        labelEmailAzienda = new JLabel();
        textFildEmailAzienda = new JTextField();
        iconCognome = new JLabel();
        labelAdmin = new JLabel();
        textFildAdmin = new JTextField();
        buttonCalender = new JButton();
        labelPassword = new JLabel();
        comboBoxRegione = new JComboBox();
        iconEmail = new JLabel();
        iconPass = new JLabel();
        buttonRegistrati = new JButton();
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
            "[292,grow,fill]" +
            "[72,fill]ind",
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
        labelNome.setText("Nome Azienda :");
        labelNome.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        add(labelNome, "cell 0 0,alignx right,growx 0");

        //---- textFildNome ----
        textFildNome.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        add(textFildNome, "cell 1 0 3 1,aligny center,growy 0");
        add(iconNome, "cell 4 0");

        //---- labelEmailAzienda ----
        labelEmailAzienda.setText("Email Azienda :");
        labelEmailAzienda.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        add(labelEmailAzienda, "cell 0 1,alignx right,growx 0");

        //---- textFildEmailAzienda ----
        textFildEmailAzienda.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        add(textFildEmailAzienda, "cell 1 1 3 1,aligny center,growy 0");
        add(iconCognome, "cell 4 1");

        //---- labelAdmin ----
        labelAdmin.setText("Email Admin :");
        labelAdmin.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        add(labelAdmin, "cell 0 2,alignx right,growx 0");

        //---- textFildAdmin ----
        textFildAdmin.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        add(textFildAdmin, "cell 1 2 3 1,aligny center,growy 0");

        //---- buttonCalender ----
        buttonCalender.setIcon(UIManager.getIcon("OptionPane.questionIcon"));
        buttonCalender.setBackground(new Color(0x00ffffff, true));
        buttonCalender.setBorder(null);
        add(buttonCalender, "cell 4 2,alignx left,growx 0");

        //---- labelPassword ----
        labelPassword.setText("Regione :");
        labelPassword.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        add(labelPassword, "cell 0 3,alignx right,growx 0");

        //---- comboBoxRegione ----
        comboBoxRegione.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        add(comboBoxRegione, "cell 1 3 3 1,aligny center,growy 0");
        add(iconEmail, "cell 4 3");
        add(iconPass, "cell 4 6");

        //---- buttonRegistrati ----
        buttonRegistrati.setText("Invia Richiesta");
        buttonRegistrati.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        add(buttonRegistrati, "cell 3 7 2 1,aligny center,grow 100 0");

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
    private JLabel labelNome;
    private JTextField textFildNome;
    private JLabel iconNome;
    private JLabel labelEmailAzienda;
    private JTextField textFildEmailAzienda;
    private JLabel iconCognome;
    private JLabel labelAdmin;
    private JTextField textFildAdmin;
    private JButton buttonCalender;
    private JLabel labelPassword;
    private JComboBox comboBoxRegione;
    private JLabel iconEmail;
    private JLabel iconPass;
    private JButton buttonRegistrati;
    private JPanel panelMenuBarOpzioniColore;
    private JMenuBar menuBarOpzioni;
    private JMenu menuOpzioni;
    private JLabel LogoOpzioni;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
