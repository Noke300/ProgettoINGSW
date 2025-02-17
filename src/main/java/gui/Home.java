package gui;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;

public class Home {

    private JPanel homePanel;
    private JLabel titolo;
    private JButton accessoButton;

    public JPanel createHomePanel(JFrame frame) {
        // Creazione del pannello principale con GridBagLayout
        // Aggiunta del titolo (label)
         titolo.setText("Benvenuto nella Home");
         // Aggiunta del pulsante (bottone)
        accessoButton.setText("Mappa");
        accessoButton.addActionListener(e -> {
            frame.setContentPane(new Ricerca().createRicercaPanel(frame)); // Passa alla mappa
            frame.revalidate();
            frame.repaint();
        });

        return homePanel;
    }

    public static void main(String[] args) {
        FlatLightLaf.setup();
        JFrame frame = new JFrame("Home");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(1400, 800);
        frame.setLocationRelativeTo(null);
        frame.setContentPane(new Home().createHomePanel(frame));  // Schermata iniziale
        frame.setVisible(true);
    }
}