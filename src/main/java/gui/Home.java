package gui;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.extras.*;
import controller.ControllerClienti;
import gui.pagine_comuni.Ricerca;
import net.miginfocom.swing.*;

public class Home extends JPanel {

    String token ="";
    public Home() {
        initComponents();
    }
    public JPanel createHomePanel(JFrame frame) {
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        accessoButton.setText("Entra");
        accessoButton.setPreferredSize(new Dimension(120, 30));
        accessoButton.addActionListener(e -> {

            frame.setContentPane(new Ricerca(frame,new ControllerClienti(),token));
            frame.revalidate();
            frame.repaint();
            frame.setExtendedState(Frame.MAXIMIZED_BOTH);

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

        frame.setMinimumSize(new Dimension(600, 500));
        frame.setLocationRelativeTo(null);
        frame.setContentPane(new Home().createHomePanel(frame));// Schermata iniziale
        frame.setVisible(true);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY //GEN-BEGIN:initComponents @formatter:off
        // Generated using JFormDesigner Educational license - Marco Scotto di Uccio Scotto di Uccio (Marco Scotto Di Uccio)
        panelMenuBarOpzioniColore = new JPanel();
        titolo = new JLabel();
        accessoButton = new JButton();

        //======== this ========
        setPreferredSize(new Dimension(500, 600));
        setLayout(new MigLayout(
            "insets 0,hidemode 3,align center center,gap 10 5",
            // columns
            "[14,grow 1,fill]0",
            // rows
            "[64]0" +
            "[283,grow]0" +
            "[69,grow,shrink 0]para"));

        //======== panelMenuBarOpzioniColore ========
        {
            panelMenuBarOpzioniColore.setForeground(Color.green);
            panelMenuBarOpzioniColore.setBackground(UIManager.getColor("Actions.Green"));
            panelMenuBarOpzioniColore.setBorder(new MatteBorder(1, 1, 1, 1, Color.black));
            panelMenuBarOpzioniColore.setLayout(new BorderLayout());
        }
        add(panelMenuBarOpzioniColore, "cell 0 0,growy");

        //---- titolo ----
        titolo.setText("Dieti Estates 25 ");
        titolo.setFont(new Font("Felix Titling", Font.BOLD | Font.ITALIC, 69));
        titolo.setForeground(UIManager.getColor("Actions.Green"));
        titolo.setHorizontalAlignment(SwingConstants.CENTER);
        titolo.setIcon(new FlatSVGIcon("LogoBackGraund.svg"));
        titolo.setHorizontalTextPosition(SwingConstants.CENTER);
        titolo.setVerticalTextPosition(SwingConstants.BOTTOM);
        add(titolo, "cell 0 1,growy");

        //---- accessoButton ----
        accessoButton.setText("Button");
        accessoButton.setPreferredSize(null);
        add(accessoButton, "cell 0 2,aligny top,grow 100 0");
        // JFormDesigner - End of component initialization //GEN-END:initComponents @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY //GEN-BEGIN:variables @formatter:off
    // Generated using JFormDesigner Educational license - Marco Scotto di Uccio Scotto di Uccio (Marco Scotto Di Uccio)
    private JPanel panelMenuBarOpzioniColore;
    private JLabel titolo;
    private JButton accessoButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}