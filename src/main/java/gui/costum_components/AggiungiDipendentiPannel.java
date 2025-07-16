package gui.costum_components;

import controller.ControllerAdmin;
import controller.ControllerAdminSup;
import model.AdminSupport;
import model.AgenteImmobiliare;
import model.AgenziaImmobiliare;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AggiungiDipendentiPannel {

    public static void creaPanelAdmin(JFrame frame, ControllerAdmin controller, AgenziaImmobiliare admin, String token, DefaultTableModel model, JComboBox<String> tipoCombo) {
        JDialog dialog = new JDialog(frame, "Aggiungi Dipendente", true);
        dialog.setMinimumSize(new Dimension(550, 450));
        dialog.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JTextField nomeField = new JTextField();
        JTextField cognomeField = new JTextField();
        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton aggiungiButton = new JButton("Aggiungi");

        aggiungiButton.setPreferredSize(new Dimension(120, 30));
        aggiungiButton.setBackground(new Color(0, 153, 0)); // Verde scuro
        aggiungiButton.setForeground(Color.WHITE); // Testo bianco
        aggiungiButton.setOpaque(true);
        aggiungiButton.setBorderPainted(false);

        dialog.add(new JLabel("Tipo dipendente:"), gbc); gbc.gridy++;
        dialog.add(tipoCombo, gbc); gbc.gridy++;
        dialog.add(new JLabel("Nome:"), gbc); gbc.gridy++;
        dialog.add(nomeField, gbc); gbc.gridy++;
        dialog.add(new JLabel("Cognome:"), gbc); gbc.gridy++;
        dialog.add(cognomeField, gbc); gbc.gridy++;

        dialog.add(new JLabel("Email:"), gbc); gbc.gridy++;
        dialog.add(emailField, gbc); gbc.gridy++;

// Etichetta con vincoli email
        dialog.add(new JLabel("<html><small>Formato valido: utente@dominio.com</small></html>"), gbc); gbc.gridy++;

        dialog.add(new JLabel("Password:"), gbc); gbc.gridy++;
        dialog.add(passwordField, gbc); gbc.gridy++;

// Etichetta con vincoli password
        dialog.add(new JLabel("<html><small>Min. 8 caratteri, 1 maiuscola, 1 numero, 1 speciale</small></html>"), gbc); gbc.gridy++;


        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        dialog.add(aggiungiButton, gbc);

        aggiungiButton.addActionListener(ev -> {
            String tipo = (String) tipoCombo.getSelectedItem();
            String nome = nomeField.getText().trim();
            String cognome = cognomeField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());

            if(!checkDati(nome, cognome, email, password)){
                JOptionPane.showMessageDialog(dialog, "Compila tutti i campi correttamente.");
                return;
            }

            boolean added = false;

            if (tipo.equals("Admin Support")) {
                AdminSupport nuovo = new AdminSupport(nome, cognome, email, password, admin);
                added = controller.aggiungiAdminSupport(nuovo, token);
                if (added) {
                    model.addRow(new Object[]{nome, cognome, email, "Admin Support"});
                }
            } else {
                AgenteImmobiliare nuovo = new AgenteImmobiliare(admin, admin.getRegione(), password, email, cognome, nome);
                added = controller.aggiungiAgenteImmobiliare(nuovo, token);
                if (added) {
                    model.addRow(new Object[]{nome, cognome, email, "Agente Immobiliare"});
                }
            }

            if (added) {
                JOptionPane.showMessageDialog(dialog, "Dipendente aggiunto con successo.");
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Errore durante l’aggiunta.");
            }
        });

        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    public static void creaPanelAdminSup(JFrame frame, ControllerAdminSup controller, AgenziaImmobiliare admin, String token, DefaultTableModel model, JComboBox<String> tipoCombo) {
        JDialog dialog = new JDialog(frame, "Aggiungi Dipendente", true);
        dialog.setMinimumSize(new Dimension(550, 450));
        dialog.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JTextField nomeField = new JTextField();
        JTextField cognomeField = new JTextField();
        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton aggiungiButton = new JButton("Aggiungi");

        aggiungiButton.setPreferredSize(new Dimension(120, 30));
        aggiungiButton.setBackground(new Color(0, 153, 0)); // Verde scuro
        aggiungiButton.setForeground(Color.WHITE); // Testo bianco
        aggiungiButton.setOpaque(true);
        aggiungiButton.setBorderPainted(false);

        dialog.add(new JLabel("Tipo dipendente:"), gbc); gbc.gridy++;
        dialog.add(tipoCombo, gbc); gbc.gridy++;
        dialog.add(new JLabel("Nome:"), gbc); gbc.gridy++;
        dialog.add(nomeField, gbc); gbc.gridy++;
        dialog.add(new JLabel("Cognome:"), gbc); gbc.gridy++;
        dialog.add(cognomeField, gbc); gbc.gridy++;
        dialog.add(new JLabel("Email:"), gbc); gbc.gridy++;
        dialog.add(emailField, gbc); gbc.gridy++;

// Etichetta con vincoli email
        dialog.add(new JLabel("<html><small>Formato valido: utente@dominio.com</small></html>"), gbc); gbc.gridy++;

        dialog.add(new JLabel("Password:"), gbc); gbc.gridy++;
        dialog.add(passwordField, gbc); gbc.gridy++;

// Etichetta con vincoli password
        dialog.add(new JLabel("<html><small>Min. 8 caratteri, 1 maiuscola, 1 numero, 1 speciale</small></html>"), gbc); gbc.gridy++;

        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        dialog.add(aggiungiButton, gbc);

        aggiungiButton.addActionListener(ev -> {
            String nome = nomeField.getText().trim();
            String cognome = cognomeField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());

            if(!checkDati(nome, cognome, email, password)){
                JOptionPane.showMessageDialog(dialog, "Compila tutti i campi correttamente.");
                return;
            }

            boolean added = false;
            AgenteImmobiliare nuovo = new AgenteImmobiliare(admin, "", password, email, cognome, nome);
            added = controller.aggiungiAgenteImmobiliare(nuovo, token);

            if (added) {
                model.addRow(new Object[]{nome, cognome, email, "Agente Immobiliare"});
            }

            if (added) {
                JOptionPane.showMessageDialog(dialog, "Dipendente aggiunto con successo.");
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Errore durante l’aggiunta.");
            }
        });

        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    private static boolean checkDati(String nome, String cognome, String email, String password) {
        if (nome == null || nome.isEmpty() ||
                cognome == null || cognome.isEmpty() ||
                email == null || email.isEmpty() ||
                password == null || password.isEmpty()) {
            return false;
        }

        // Regex per email valida
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        if (!email.matches(emailRegex)) {
            return false;
        }

        // Regex per password:
        // almeno 1 maiuscola, 1 numero, 1 carattere speciale, minimo 8 caratteri
        String passwordRegex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$";
        return password.matches(passwordRegex);
    }


}
