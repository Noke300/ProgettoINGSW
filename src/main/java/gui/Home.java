package gui;

import com.formdev.flatlaf.FlatDarculaLaf;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Home {

    public JPanel createHomePanel(JFrame frame) {
        // Creazione del pannello principale con GridBagLayout
        JPanel homePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Margini intorno ai componenti
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // Aggiunta del titolo (label)
        JLabel titolo = new JLabel("Benvenuto nella Home");
        titolo.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 0;  // Prima riga
        gbc.weightx = 1;  // Espande orizzontalmente
        gbc.weighty = 0.2;  // Poca altezza occupata
        homePanel.add(titolo, gbc);

        // Aggiunta del logo (immagine)
        ImageIcon logoIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/your_aww_logo27.png"))); // Carica l'immagine dalla cartella resources

        JLabel logoLabel = new JLabel(logoIcon);

        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 1;  // Seconda riga (tra titolo e bottone)
        gbc.weighty = 0.2;  // Spazio per il logo
        homePanel.add(logoLabel, gbc);

        // Aggiunta del pulsante (bottone)
        JButton accessoButton = new JButton("Accedi alla Ricerca");
        accessoButton.addActionListener(e -> {
            frame.setContentPane(new Ricerca().createRicercaPanel(frame)); // Passa alla mappa
            frame.revalidate();
            frame.repaint();
        });
        gbc.gridy = 2;  // Terza riga
        gbc.weightx = 0;  // Non espande orizzontalmente
        gbc.weighty = 0.6;  // Spinge più verso il basso
        homePanel.add(accessoButton, gbc);

        return homePanel;
    }

    public static void main(String[] args) {
        FlatDarculaLaf.setup();
        JFrame frame = new JFrame("Home");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(1400, 800);
        frame.setLocationRelativeTo(null);
        frame.setContentPane(new Home().createHomePanel(frame));  // Schermata iniziale
        frame.setVisible(true);
    }
}