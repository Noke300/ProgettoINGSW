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
        // Generated using JFormDesigner Evaluation license - Simone Sommella
        titolo = new JLabel();
        accessoButton = new JButton();

        //======== this ========
        setPreferredSize(new Dimension(500, 600));
        setBorder (new javax. swing. border. CompoundBorder( new javax .swing .border .TitledBorder (new javax. swing.
        border. EmptyBorder( 0, 0, 0, 0) , "JF\u006frm\u0044es\u0069gn\u0065r \u0045va\u006cua\u0074io\u006e", javax. swing. border. TitledBorder. CENTER
        , javax. swing. border. TitledBorder. BOTTOM, new java .awt .Font ("D\u0069al\u006fg" ,java .awt .Font
        .BOLD ,12 ), java. awt. Color. red) , getBorder( )) );  addPropertyChangeListener (
        new java. beans. PropertyChangeListener( ){ @Override public void propertyChange (java .beans .PropertyChangeEvent e) {if ("\u0062or\u0064er"
        .equals (e .getPropertyName () )) throw new RuntimeException( ); }} );
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
    // Generated using JFormDesigner Evaluation license - Simone Sommella
    private JLabel titolo;
    private JButton accessoButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}