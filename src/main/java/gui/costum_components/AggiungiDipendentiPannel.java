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
        dialog.setMinimumSize(new Dimension(700, 550));
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

        dialog.add(new JLabel("Tipo dipendente:"), gbc); gbc.gridy++;
        dialog.add(tipoCombo, gbc); gbc.gridy++;
        dialog.add(new JLabel("Nome:"), gbc); gbc.gridy++;
        dialog.add(nomeField, gbc); gbc.gridy++;
        dialog.add(new JLabel("Cognome:"), gbc); gbc.gridy++;
        dialog.add(cognomeField, gbc); gbc.gridy++;
        dialog.add(new JLabel("Email:"), gbc); gbc.gridy++;
        dialog.add(emailField, gbc); gbc.gridy++;
        dialog.add(new JLabel("Password:"), gbc); gbc.gridy++;
        dialog.add(passwordField, gbc); gbc.gridy++;

        gbc.gridwidth = 2;
        dialog.add(aggiungiButton, gbc);

        aggiungiButton.addActionListener(ev -> {
            String tipo = (String) tipoCombo.getSelectedItem();
            String nome = nomeField.getText().trim();
            String cognome = cognomeField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (nome.isEmpty() || cognome.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Completa tutti i campi.");
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
        dialog.setMinimumSize(new Dimension(700, 550));
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

        dialog.add(new JLabel("Tipo dipendente:"), gbc); gbc.gridy++;
        dialog.add(tipoCombo, gbc); gbc.gridy++;
        dialog.add(new JLabel("Nome:"), gbc); gbc.gridy++;
        dialog.add(nomeField, gbc); gbc.gridy++;
        dialog.add(new JLabel("Cognome:"), gbc); gbc.gridy++;
        dialog.add(cognomeField, gbc); gbc.gridy++;
        dialog.add(new JLabel("Email:"), gbc); gbc.gridy++;
        dialog.add(emailField, gbc); gbc.gridy++;
        dialog.add(new JLabel("Password:"), gbc); gbc.gridy++;
        dialog.add(passwordField, gbc); gbc.gridy++;

        gbc.gridwidth = 2;
        dialog.add(aggiungiButton, gbc);

        aggiungiButton.addActionListener(ev -> {
            String nome = nomeField.getText().trim();
            String cognome = cognomeField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (nome.isEmpty() || cognome.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Completa tutti i campi.");
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


}
