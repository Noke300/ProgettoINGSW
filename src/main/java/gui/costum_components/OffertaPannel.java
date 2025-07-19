package gui.costum_components;

import controller.Controller;
import controller.ControllerAgente;
import gui.pagine_clienti.LogIn;
import model.Immobile;
import model.Offerta;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.util.List;

public class OffertaPannel {

    public static void setUpPannel(String token, JFrame frame, Immobile immobile, Controller controller) {
        if(!"".equals(token)) {
            JDialog offerteDialog = new JDialog(frame, "Proponi prezzo", true);
            offerteDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            offerteDialog.setSize(500, 450);
            offerteDialog.setResizable(false);

// Pannello contenitore
            JPanel contenuto = new JPanel();
            contenuto.setLayout(new BoxLayout(contenuto, BoxLayout.Y_AXIS));
            contenuto.setBorder(BorderFactory.createEmptyBorder(10, 50, 30, 50));

// Etichetta

            JPanel etichettaPanel = new JPanel(new GridLayout(0, 1, 10, 10));

            JLabel etichettaLabel = new JLabel("<html>Proponi un prezzo per la tua offerta che sia entro il limite consentito</html>");
            etichettaLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
            etichettaLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            etichettaPanel.add(etichettaLabel);

            JPanel partenzaPanel = new JPanel(new GridLayout(2, 2, 5, 5));

            JLabel partenzaLabel = new JLabel("Prezzo di partenza");
            partenzaLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
            partenzaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel prezzoLabel = new JLabel(immobile.getPrezzo() + " €");
            prezzoLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
            prezzoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            partenzaPanel.add(partenzaLabel);
            partenzaPanel.add(prezzoLabel);

            contenuto.add(etichettaPanel);
            contenuto.add(Box.createRigidArea(new Dimension(0, 10)));

// Campo input
            JLabel prezzoLabel2 = new JLabel("La tua proposta");
            prezzoLabel2.setFont(new Font("SansSerif", Font.BOLD, 16));
            prezzoLabel2.setAlignmentX(Component.LEFT_ALIGNMENT);

            JTextField campoOfferta = new JTextField();
            campoOfferta.setMaximumSize(new Dimension(20, 5));
            campoOfferta.setFont(new Font("SansSerif", Font.PLAIN, 16));
            // Placeholder
            String placeholder = "es: " + (int) (immobile.getPrezzo() * 1.15);
            campoOfferta.setText(placeholder);
            campoOfferta.setForeground(Color.GRAY);

            // Quando entri nel campo, se c'è il placeholder, lo svuoti
            campoOfferta.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    if (campoOfferta.getText().equals(placeholder)) {
                        campoOfferta.setText("");
                        campoOfferta.setForeground(Color.BLACK);
                    }
                }

                @Override
                public void focusLost(FocusEvent e) {
                    if (campoOfferta.getText().isEmpty()) {
                        campoOfferta.setText(placeholder);
                        campoOfferta.setForeground(Color.GRAY);
                    }
                }
            });

            partenzaPanel.add(prezzoLabel2);
            partenzaPanel.add(campoOfferta);

            contenuto.add(partenzaPanel);
            contenuto.add(Box.createRigidArea(new Dimension(0, 20)));

            JSeparator separatore = new JSeparator(SwingConstants.HORIZONTAL);
            separatore.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1)); // Larghezza piena, altezza 1px
            separatore.setForeground(Color.LIGHT_GRAY); // Colore del tratto

            contenuto.add(separatore);
            contenuto.add(Box.createRigidArea(new Dimension(0, 10))); // Spazio dopo il divisore



            JPanel spiegazionePanel = new JPanel(new GridLayout(0, 1, 10, 10));

            JLabel speigazioneLabel = new JLabel("<html>é consentito fare un'offerta con una variazione massima del 15% rispetto al prezzo di partenza, al fine di mantenere una coerenza e limitare variazione troppo brusche di prezzo</html>");
            speigazioneLabel.setFont(new Font("SansSerif", Font.ITALIC, 16));
            speigazioneLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            spiegazionePanel.add(speigazioneLabel);

            contenuto.add(spiegazionePanel);
            contenuto.add(Box.createRigidArea(new Dimension(0, 10)));

// Bottone invia
            JButton inviaButton = new JButton("Invia Offerta");
            inviaButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            inviaButton.setFont(new Font("SansSerif", Font.BOLD, 16));

            // Colore verde per lo sfondo e bianco per il testo
            inviaButton.setBackground(new Color(46, 204, 113)); // Verde brillante
            inviaButton.setForeground(Color.WHITE);

            inviaButton.setOpaque(true);
            inviaButton.setBorderPainted(false);
            inviaButton.setEnabled(false);

            // Listener per controllare i valori
            campoOfferta.getDocument().addDocumentListener(new DocumentListener() {
                private void checkInput() {
                    try {
                        double valore = Double.parseDouble(campoOfferta.getText().replace(",", "."));
                        inviaButton.setEnabled(valore >= immobile.getPrezzo() * 0.85 && valore <= immobile.getPrezzo() * 1.15);
                    } catch (NumberFormatException ex) {
                        inviaButton.setEnabled(false); // non numero = disabilita
                    }
                }

                public void insertUpdate(DocumentEvent e) { checkInput(); }
                public void removeUpdate(DocumentEvent e) { checkInput(); }
                public void changedUpdate(DocumentEvent e) { checkInput(); }
            });

// Azione al click
            inviaButton.addActionListener(h -> {
                String testo = campoOfferta.getText().trim();
                if (!testo.isEmpty()) {
                    if (controller.getNameLogIn().equals("[CLIENT]")) {
                        try {
                            double prezzo = Double.parseDouble(testo);
                            if (prezzo <= 0) {
                                JOptionPane.showMessageDialog(offerteDialog, "Inserisci un valore numerico valido!", "Errore", JOptionPane.ERROR_MESSAGE);
                            } else {
                                Immobile immTemp = new Immobile(immobile.getId());
                                Offerta offerta = new Offerta(immTemp, prezzo);

                                boolean risOfferta = controller.inviaOfferta(offerta, token);
                                if (risOfferta) {
                                    BottoniOpzioni.getMapLog().put(LocalDateTime.now(), "Offerta Richiesta : " + offerta.getPrezzoOfferta() + " \n" + "Per " + immobile.getIndirizzo().toString());
                                    JOptionPane.showMessageDialog(offerteDialog, "Offerta inviata con successo: €" + prezzo, "Conferma", JOptionPane.INFORMATION_MESSAGE);
                                    offerteDialog.dispose();
                                } else {
                                    JOptionPane.showMessageDialog(offerteDialog, "Invio offerta non riuscito", "Errore", JOptionPane.ERROR_MESSAGE);

                                }
                            }
                            // chiudi il dialog se vuoi
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(offerteDialog, "Inserisci un valore numerico valido!", "Errore", JOptionPane.ERROR_MESSAGE);
                        }
                    }else {
                        JOptionPane.showMessageDialog(offerteDialog, "Bisogna essere registrato con un account cliente per fare un offerta ", "Attenzione", JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(offerteDialog, "Il campo non può essere vuoto.", "Attenzione", JOptionPane.WARNING_MESSAGE);
                }
            });

            contenuto.add(inviaButton);

            contenuto.setFocusable(true);
            contenuto.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    contenuto.requestFocusInWindow();
                }
            });


// Imposta il pannello come contenuto del dialog
            offerteDialog.setContentPane(contenuto);
            offerteDialog.setLocationRelativeTo(frame); // centra rispetto al frame
            offerteDialog.setVisible(true);

        }else{
            frame.setContentPane(new LogIn(frame,controller,token));
            frame.revalidate();
            frame.repaint();
        }
}

    public static void mostraOfferteInSospeso(JFrame frame, List<Offerta> listaOfferte, ControllerAgente controller, String token, JPanel panelListaImmobile, JPanel jPanel) {

        List<Offerta> offerteInSospeso = listaOfferte.stream()
                .filter(o -> o.getEsitoOfferta() == null)
                .toList();

        if (offerteInSospeso.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Non ci sono offerte in sospeso.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog(frame, "Offerte In Sospeso", true);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setSize(600, 500);
        dialog.setLayout(new BorderLayout());

        JPanel pannelloOfferte = new JPanel();
        pannelloOfferte.setLayout(new BoxLayout(pannelloOfferte, BoxLayout.Y_AXIS));
        pannelloOfferte.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (Offerta offerta : offerteInSospeso) {
            JPanel card = new JPanel();
            card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));

            card.add(new JLabel("📌 Cliente: " + (offerta.getCliente() != null ? offerta.getCliente() : "")));
            card.add(new JLabel("🏡 Immobile: " + offerta.getImmobile().getIndirizzo()));
            card.add(new JLabel("💰 Prezzo offerto: €" + offerta.getPrezzoOfferta()));
            card.add(new JLabel("📅 Data: " + offerta.getDataOfferta()));

            JPanel bottoniPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

            JButton rifiutaBtn = new JButton("Rifiuta");
            JButton contoBtn = new JButton("Contro Offerta");
            JButton accettaBtn = new JButton("Accetta");

            rifiutaBtn.addActionListener(e -> {
                int scelta = JOptionPane.showOptionDialog(pannelloOfferte, "Sei sicuro di voler rifiutare questa proposta?", "Conferma rifiuto",                          // Titolo della finestra
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        null,
                        null
                );
                if (scelta == JOptionPane.YES_NO_OPTION){
                    offerta.setEsitoOfferta(false);
                    if(controller.esito(offerta, token)){
                        card.setVisible(false); // Rimuove la card visivamente
                        dialog.dispose();
                    }else{
                        JOptionPane.showMessageDialog(pannelloOfferte, "C'è stato un errore durante l'invio della risposta");
                    }
                }
            });

            contoBtn.addActionListener(e -> {
                // Potresti aprire un altro dialog simile al tuo "proponi prezzo"
                apriDialogControOfferta(frame, offerta, controller, token);
                card.setVisible(false); // Rimuove la card visivamente
                dialog.dispose();
            });

            accettaBtn.addActionListener(e -> {
                int scelta = JOptionPane.showOptionDialog(pannelloOfferte, "Sei sicuro di voler accettare questa proposta?", "Conferma proposta",                          // Titolo della finestra
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        null,
                        null
                );
                if (scelta == JOptionPane.YES_NO_OPTION){
                    offerta.setEsitoOfferta(true);
                    if(controller.esito(offerta, token)){
                        card.setVisible(false);
                        panelListaImmobile.remove(jPanel);
                        panelListaImmobile.repaint();
                        panelListaImmobile.revalidate();
                        dialog.dispose();
                    }else{
                        JOptionPane.showMessageDialog(pannelloOfferte, "C'è stato un errore durante l'invio della risposta");
                    }
                }
            });

            bottoniPanel.add(rifiutaBtn);
            bottoniPanel.add(contoBtn);
            bottoniPanel.add(accettaBtn);

            card.add(bottoniPanel);
            card.add(Box.createRigidArea(new Dimension(0, 10)));

            pannelloOfferte.add(card);
        }

        JScrollPane scrollPane = new JScrollPane(pannelloOfferte);
        dialog.add(scrollPane, BorderLayout.CENTER);

        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    public static void apriDialogControOfferta(JFrame frame, Offerta offerta, ControllerAgente controller, String token) {
        JDialog dialog = new JDialog(frame, "Contro Offerta", true);
        dialog.setSize(500, 400);
        dialog.setResizable(false);
        dialog.setLayout(new BorderLayout());

        JPanel contenuto = new JPanel();
        contenuto.setLayout(new BoxLayout(contenuto, BoxLayout.Y_AXIS));
        contenuto.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel descrizione = new JLabel("<html>Fai una contro-offerta per l'immobile: <b>" +
                offerta.getImmobile().getIndirizzo() + "</b><br>Prezzo attuale: €" + offerta.getPrezzoOfferta() + "</html>");
        descrizione.setFont(new Font("SansSerif", Font.PLAIN, 14));
        contenuto.add(descrizione);
        contenuto.add(Box.createRigidArea(new Dimension(0, 20)));

        JTextField campoPrezzo = new JTextField();
        campoPrezzo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        campoPrezzo.setFont(new Font("SansSerif", Font.PLAIN, 16));
        contenuto.add(new JLabel("Nuova proposta (€):"));
        contenuto.add(campoPrezzo);

        JButton inviaBtn = new JButton("Invia Contro Offerta");
        inviaBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        inviaBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        inviaBtn.setBackground(new Color(52, 152, 219)); // blu
        inviaBtn.setForeground(Color.WHITE);
        inviaBtn.setEnabled(false);
        contenuto.add(Box.createRigidArea(new Dimension(0, 20)));
        contenuto.add(inviaBtn);

        campoPrezzo.getDocument().addDocumentListener(new DocumentListener() {
            private void checkInput() {
                try {
                    double valore = Double.parseDouble(campoPrezzo.getText().replace(",", "."));
                    inviaBtn.setEnabled(valore > 0);
                } catch (NumberFormatException e) {
                    inviaBtn.setEnabled(false);
                }
            }
            public void insertUpdate(DocumentEvent e) { checkInput(); }
            public void removeUpdate(DocumentEvent e) { checkInput(); }
            public void changedUpdate(DocumentEvent e) { checkInput(); }
        });

        inviaBtn.addActionListener(a -> {
            try {
                Offerta nuovaOfferta = new Offerta(offerta);
                double nuovoPrezzo = Double.parseDouble(campoPrezzo.getText().replace(",", "."));
                Immobile imm = new Immobile(offerta.getImmobile().getId());
                nuovaOfferta.setPrezzoOfferta(nuovoPrezzo);
                nuovaOfferta.setContrOfferta(true);

                boolean risultato = controller.contrOfferta(nuovaOfferta, token);
                if (risultato) {
                    JOptionPane.showMessageDialog(dialog, "Contro-offerta inviata: €" + nuovoPrezzo);
                    BottoniOpzioni.getMapLog().put(LocalDateTime.now(),
                            "Contro-Offerta: €" + nuovoPrezzo + " per " + imm.getIndirizzo());
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Invio non riuscito", "Errore", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(dialog, "Prezzo non valido!", "Errore", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(contenuto, BorderLayout.CENTER);
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }



}
