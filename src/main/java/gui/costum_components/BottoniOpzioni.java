package gui.costum_components;

import controller.*;
import gui.*;
import gui.pagine_comuni.Ricerca;
import gui.pagine_administrative.admin.InterfacciaAdmin;
import gui.pagine_administrative.adminSup.InterfacciaAdminSup;
import gui.pagine_administrative.agenteImmobliiare.InterfacciaAgenteImmobiliare;
import gui.pagine_administrative.LogInAmministrazione;
import gui.pagine_clienti.ClientPersonalPage;
import gui.pagine_clienti.LogIn;
import model.ImmobileEImmagini;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BottoniOpzioni extends JPanel {

    private final JButton logInButton = new JButton();
    private final JButton buttonAccountClienti = new JButton();
    private final JButton buttonRicerca = new JButton();
    private final JButton backButtonHome = new JButton();
    private final JButton buttonAccountAmministrazione = new JButton();

    private static Map<LocalDateTime,String> mapLog = new HashMap<>();

    private String token = "";
    private String ruolo = "";

    public void setUpOpzioniMenu(JFrame frame, Controller control, String token, JMenuBar menuBarOpzioni, JMenu menuOpzioni) {
        this.token = token;
        setBottoniSetUpJForm();
        if(this.token != null && !token.isEmpty()) {
            ruolo = control.getRawSpecifiche(this.token);
        }
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
                control.eseguiLogout(token);
                frame.setContentPane(new Ricerca(frame, new Controller(), ""));
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
                Controller controlAdmin;

                switch (ruolo) {
                    case "[AGENTE_IMMOBILIARE]":
                        controlAdmin = new ControllerAgente();
                        controlAdmin.setNameLogIn(control.getNameLogIn());

                        List<ImmobileEImmagini> immobiliEImmagini = ((ControllerAgente) controlAdmin).fetchImmobili(token);
                        BottoniOpzioni bottoniOpzioni = new BottoniOpzioni();
                        frame.setContentPane(new InterfacciaAgenteImmobiliare(
                                frame, bottoniOpzioni, (ControllerAgente) controlAdmin, this.token, immobiliEImmagini));
                        break;

                    case "[ADMIN]":
                        controlAdmin = new ControllerAdmin();
                        controlAdmin.setNameLogIn(control.getNameLogIn());
                        frame.setContentPane(new InterfacciaAdmin(frame,new ControllerAdmin(), this.token));
                        break;

                    case "[ADMIN_SUPPORT]":
                        controlAdmin = new ControllerAdminSup();
                        controlAdmin.setNameLogIn(control.getNameLogIn());
                        frame.setContentPane(new InterfacciaAdminSup(frame,new ControllerAdminSup(), this.token));
                        break;

                    default:
                        JOptionPane.showMessageDialog(
                                frame,
                                "Bisogna effettuare il log in con un account administrative ",
                                "Errore",
                                JOptionPane.ERROR_MESSAGE
                        );
                        menuOpzioni.setVisible(true);
                        menuBarOpzioni.setVisible(true);
                        break;
                }
            }
            frame.revalidate();
            frame.repaint();
        });

        // ACCOUNT CLIENTI
        buttonAccountClienti.addMouseListener(IconClass.cursorButton());
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

        //RICERCA

        buttonRicerca.addActionListener(e -> {
            menuOpzioni.setVisible(false);
            menuBarOpzioni.setVisible(false);
            frame.setContentPane(new Ricerca(frame, new Controller(), this.token));
            frame.revalidate();
            frame.repaint();
        });




        for (JButton btn : Arrays.asList(logInButton, buttonAccountAmministrazione,buttonRicerca)) {
            btn.addMouseListener(IconClass.cursorButton());
            menuOpzioni.add(btn);
        }
        if((!(token == null) && !token.isEmpty()) && control.getNameLogIn().equals("[CLIENT]")){
            menuOpzioni.add(buttonAccountClienti);
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

        buttonRicerca.setText("Ricerca");
        buttonRicerca.setIcon(IconClass.getSearcherIcon());
        buttonRicerca.setFont(new Font("Segoe UI", Font.PLAIN, 16));

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

    public static Map<LocalDateTime, String> getMapLog() {
        return mapLog;
    }
}
