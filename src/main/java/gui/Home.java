package gui;

import java.awt.*;

import javax.swing.*;

import com.formdev.flatlaf.FlatLightLaf;
import controller.Controller;
import net.miginfocom.swing.*;

public class Home extends JPanel {

    String token ="";

    public Home() {
        initComponents();
    }
    public JPanel createHomePanel(JFrame frame) {
        titolo.setText("Benvenuto nella Home");

        accessoButton.setText("Entra");
        accessoButton.addActionListener(e -> {

            frame.setContentPane(new Ricerca().createRicercaPanel(frame,new Controller(),token));
            frame.revalidate();
            frame.repaint();

        });
        return this; // restituisci il pannello corrente
    }
    @Override
    public void addNotify() {
        super.addNotify();
        // Assicurati che tutti i menu popup siano nascosti quando il pannello viene aggiunto
        MenuSelectionManager.defaultManager().clearSelectedPath();
    }


    public static void main(String[] args) {
        FlatLightLaf.setup();

        JFrame frame = new JFrame("Home");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setMinimumSize(new Dimension(1000, 800));
        frame.setLocationRelativeTo(null);
        frame.setContentPane(new Home().createHomePanel(frame));// Schermata iniziale
        frame.setVisible(true);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Evaluation license - Marco GuIdo Scotto di Uccio
        titolo = new JLabel();
        accessoButton = new JButton();

        //======== this ========
        setPreferredSize(new Dimension(500, 600));
        setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder
                (0,0,0,0), "JFor\u006dDesi\u0067ner \u0045valu\u0061tion",javax.swing.border.TitledBorder.CENTER,javax.swing.border
                .TitledBorder.BOTTOM,new java.awt.Font("Dia\u006cog",java.awt.Font.BOLD,12),java.awt
                .Color.red), getBorder())); addPropertyChangeListener(e-> {if("bord\u0065r".equals(e.getPropertyName()))throw new RuntimeException()
                ;});
        setLayout(new MigLayout(
                "insets 0,hidemode 3,align center center,gap 10 5",
                // columns
                "[grow 1,fill]",
                // rows
                "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]"));

        //---- titolo ----
        titolo.setText("Label");
        titolo.setMaximumSize(null);
        titolo.setMinimumSize(null);
        titolo.setPreferredSize(null);
        add(titolo, "cell 0 0,align center top,grow 0 0");

        //---- accessoButton ----
        accessoButton.setText("Button");
        add(accessoButton, "cell 0 8,aligny center,growy 0");
        // JFormDesigner - End of component initialization //GEN-END:initComponents @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    // Generated using JFormDesigner Evaluation license - Marco GuIdo Scotto di Uccio
    private JLabel titolo;
    private JButton accessoButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}