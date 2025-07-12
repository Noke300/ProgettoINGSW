package gui.costum_components;

import controller.Controller;
import controller.ControllerAdministration;
import controller.ControllerClienti;
import gui.*;
import gui.pagine_administrative.InterfacciaAgenteImmobiliare;
import gui.pagine_administrative.LogInAmministrazione;
import gui.pagine_clienti.ClientPersonalPage;
import gui.pagine_clienti.LogIn;
import model.ImmobileEImmagini;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;


public class BottoniOpzioni extends JPanel {

    private final JButton logInButton = new JButton();
    private final JButton buttonAccountClienti = new JButton();
    private final JButton backButtonHome = new JButton();
    private final JButton buttonAccountAmministrazione = new JButton();

    private String token = "";

    public void setUpOpzioniMenu(JFrame frame, Controller control, String token, JMenuBar menuBarOpzioni, JMenu menuOpzioni) {
        this.token = token;
        setBottoniSetUpJForm();

        // BACK HOME
        backButtonHome.addActionListener(e -> {
            menuOpzioni.setVisible(false);
            menuBarOpzioni.setVisible(false);
            frame.setContentPane(new Home().createHomePanel(frame));
            frame.revalidate();
            frame.repaint();
        });

        // LOGIN / LOGOUT
        logInButton.addActionListener(e -> {
            menuOpzioni.setVisible(false);
            menuBarOpzioni.setVisible(false);
            if (this.token == null || this.token.trim().isEmpty()) {
                frame.setContentPane(new LogIn(frame, control, ""));
            } else {
                frame.setContentPane(new Ricerca(frame, new Controller(), this.token));
            }
            frame.revalidate();
            frame.repaint();
        });

        // ACCOUNT AMMINISTRAZIONE
        buttonAccountAmministrazione.addActionListener(e -> {
            menuOpzioni.setVisible(false);
            menuBarOpzioni.setVisible(false);
            if (this.token == null || this.token.trim().isEmpty()) {
                frame.setContentPane(new LogInAmministrazione(frame, control, ""));
            } else {
                ControllerAdministration controlAdmin = new ControllerAdministration();
                List<ImmobileEImmagini> immobileEImmagini = controlAdmin.fetchImmobili(this.token);
                frame.setContentPane(new InterfacciaAgenteImmobiliare(frame, this, controlAdmin, this.token, immobileEImmagini));
            }
            frame.revalidate();
            frame.repaint();
        });

        // ACCOUNT CLIENTI
        buttonAccountClienti.addActionListener(e -> {
            menuOpzioni.setVisible(false);
            menuBarOpzioni.setVisible(false);
            ControllerClienti controlClienti = new ControllerClienti();
            if (this.token == null || this.token.trim().isEmpty()) {
                frame.setContentPane(new LogIn(frame, control, ""));
            } else {
                frame.setContentPane(new ClientPersonalPage(frame, controlClienti, this.token));
            }
            frame.revalidate();
            frame.repaint();
        });

        for (JButton btn : Arrays.asList(logInButton, buttonAccountAmministrazione, backButtonHome, buttonAccountClienti)) {
            btn.addMouseListener(IconClass.cursorButton());
            menuOpzioni.add(btn);
        }
    }

    private void setBottoniSetUpJForm() {
        backButtonHome.setText("Home");
        backButtonHome.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        backButtonHome.setIcon(IconClass.getHomeButtonIcon());

        buttonAccountAmministrazione.setText("Account Amministrazione");
        buttonAccountAmministrazione.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        buttonAccountAmministrazione.setIcon(IconClass.getAccountAministazioneIcon());

        buttonAccountClienti.setText("Account Clienti");
        buttonAccountClienti.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        buttonAccountClienti.setIcon(IconClass.getNomeCliente());

        setUpLogInOutButton();
    }

    private void setUpLogInOutButton() {
        logInButton.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        if (this.token == null || this.token.trim().isEmpty()) {
            logInButton.setText("Login");
            logInButton.setIcon(IconClass.getLogInIcon());
        } else {
            logInButton.setText("Logout");
            logInButton.setIcon(IconClass.getLogOutIcon());
        }
    }
}
