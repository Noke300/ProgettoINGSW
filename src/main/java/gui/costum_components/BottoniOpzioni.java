package gui.costum_components;

import controller.Controller;
import controller.ControllerAdministration;
import controller.ControllerClienti;
import gui.*;
import gui.pagine_administrative.InterfacciaAgenteImmobiliare;
import gui.pagine_administrative.LogInAmministrazione;
import gui.pagine_clienti.ClientPersonalPage;
import gui.pagine_clienti.LogIn;
import model.Immobile;
import model.ImmobileEImmagini;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;


public class BottoniOpzioni extends JPanel {

private static final JButton logInButton = new JButton();
private static final JButton buttonAccountClienti = new JButton();
private static final JButton backButtonHome = new JButton();
private static final JButton buttonAccountAmministrazione = new JButton();
private String token;

    public void setUpOpzioniMenu(JFrame frame, Controller control, String token, JMenuBar menuBarOpzioni, JMenu menuOpzioni) {
        this.token = token;
        setBottoniSetUpJForm();

        backButtonHome.addActionListener(e -> {
            // Supponiamo che la classe Home abbia il metodo createHomePanel
            menuOpzioni.setVisible(false);
            menuBarOpzioni.setVisible(false);
            frame.setContentPane(new Home().createHomePanel(frame));
            frame.revalidate();
            frame.repaint();
        });

        //TODO Cambio comportamento bottone log in in caso di log out //Alessio non legge
        logInButton.addActionListener(e -> {
            if (this.token.isEmpty()) {
                menuOpzioni.setVisible(false);
                menuBarOpzioni.setVisible(false);
                frame.setContentPane(new LogIn(frame, control,this.token ));
            }else {
                this.token = "";
                frame.setContentPane(new Ricerca(frame,new Controller(),this.token));
            }
            frame.revalidate();
            frame.repaint();
        });

        buttonAccountAmministrazione.addActionListener(e -> {
            if(token.isEmpty()) {
                menuOpzioni.setVisible(false);
                menuBarOpzioni.setVisible(false);
                frame.setContentPane(new LogInAmministrazione(frame, control,this.token ));
                frame.revalidate();
            }else {
                menuOpzioni.setVisible(false);
                menuBarOpzioni.setVisible(false);
                ControllerAdministration controlAdmin = new ControllerAdministration();
                List<ImmobileEImmagini> immobileEImmagini = controlAdmin.fetchImmobili(token);
                frame.setContentPane(new InterfacciaAgenteImmobiliare(frame,this,controlAdmin, this.token,immobileEImmagini ));
                frame.revalidate();
                frame.repaint();
            }
        });

        buttonAccountClienti.addActionListener(e ->{
            ControllerClienti controlClienti = new ControllerClienti();
            if(token.isEmpty()) {
                menuOpzioni.setVisible(false);
                menuBarOpzioni.setVisible(false);
                frame.setContentPane(new LogIn(frame, control,this.token ));
                frame.revalidate();
                frame.repaint();
            }else{
                menuOpzioni.setVisible(false);
                menuBarOpzioni.setVisible(false);
                frame.setContentPane(new ClientPersonalPage(frame, controlClienti,token));
                frame.revalidate();
                frame.repaint();
            }

        });

        for (JButton jButton : Arrays.asList(logInButton, buttonAccountAmministrazione, backButtonHome, buttonAccountClienti)) {
            jButton.addMouseListener(IconClass.cursorButton());
        }

        menuOpzioni.add(logInButton);
        menuOpzioni.add(buttonAccountClienti);
        menuOpzioni.add(buttonAccountAmministrazione);
        menuOpzioni.add(backButtonHome);

    }

    private void setBottoniSetUpJForm(){

        backButtonHome.setText("Home");
        backButtonHome.setVerticalAlignment(SwingConstants.TOP);
        backButtonHome.setVerticalTextPosition(SwingConstants.TOP);
        backButtonHome.setFont(new Font("Segoe UI", Font.PLAIN, 16));



        buttonAccountAmministrazione.setText("Account Amministrazione");
        buttonAccountAmministrazione.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        buttonAccountClienti.setText("Account Clienti");
        buttonAccountClienti.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        buttonAccountClienti.setIcon(IconClass.getNomeCliente());

        setUpLogInOutButton(token);
        logInButton.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        backButtonHome.setIcon(IconClass.getHomeButtonIcon());
        buttonAccountAmministrazione.setIcon(IconClass.getAccountAministazioneIcon());
    }
    private boolean setUpLogInOutButton(String token){
        if (token.isEmpty()){
            logInButton.setText("Login");
            logInButton.setIcon(IconClass.getLogInIcon());
            return false;
        }else{
            logInButton.setText("Logout");
            logInButton.setIcon(IconClass.getLogOutIcon());
            return true;
        }
    }

}
