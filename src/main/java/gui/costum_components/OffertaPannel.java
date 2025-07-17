package gui.costum_components;

import controller.Controller;
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
                    try {
                        double prezzo = Double.parseDouble(testo);
                        if (prezzo <= 0) {
                            JOptionPane.showMessageDialog(offerteDialog, "Inserisci un valore numerico valido!", "Errore", JOptionPane.ERROR_MESSAGE);
                        }else {
                            Immobile immTemp = new Immobile(immobile.getId());
                            Offerta offerta = new Offerta(immTemp,prezzo);

                            boolean risOfferta=controller.inviaOfferta(offerta,token);
                            if(risOfferta){
                                BottoniOpzioni.getMapLog().put(LocalDateTime.now(),"Offerta Richiesta : "+offerta.getPrezzoOfferta()+" \n"+"Per "+immobile.getIndirizzo().toString());
                                JOptionPane.showMessageDialog(offerteDialog, "Offerta inviata con successo: €" + prezzo, "Conferma", JOptionPane.INFORMATION_MESSAGE);
                                offerteDialog.dispose();
                            }else{
                                JOptionPane.showMessageDialog(offerteDialog, "Invio offerta non riuscito", "Errore", JOptionPane.ERROR_MESSAGE);

                            }
                        }
                        // chiudi il dialog se vuoi
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(offerteDialog, "Inserisci un valore numerico valido!", "Errore", JOptionPane.ERROR_MESSAGE);
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

}
