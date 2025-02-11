package gui;

import com.formdev.flatlaf.FlatDarculaLaf;
import javax.swing.*;
import java.awt.*;

public class Home {

    public JPanel createHomePanel(JFrame frame) {
        JPanel homePanel = new JPanel(new BorderLayout());

        JLabel titolo = new JLabel("Benvenuto nella Home");
        titolo.setHorizontalAlignment(SwingConstants.CENTER);
        homePanel.add(titolo, BorderLayout.CENTER);

        JButton accessoButton = new JButton("Accedi alla Mappa");
        accessoButton.addActionListener(e -> {
            frame.setContentPane(new Ricerca().createRicercaPanel(frame));  // Passa alla mappa
            frame.revalidate();
            frame.repaint();
        });

        homePanel.add(accessoButton, BorderLayout.SOUTH);
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
