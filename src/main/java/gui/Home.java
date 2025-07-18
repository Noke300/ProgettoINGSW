package gui;

import java.awt.*;

import javax.swing.*;

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
        // Generated using JFormDesigner Evaluation license - Alessio Nesi
        titolo = new JLabel();
        labelHome = new JLabel();
        accessoButton = new JButton();

        //======== this ========
        setPreferredSize(new Dimension(500, 600));
        setBorder ( new javax . swing. border .CompoundBorder ( new javax . swing. border .TitledBorder ( new javax .
        swing. border .EmptyBorder ( 0, 0 ,0 , 0) ,  "JFor\u006dDesi\u0067ner \u0045valu\u0061tion" , javax. swing .border
        . TitledBorder. CENTER ,javax . swing. border .TitledBorder . BOTTOM, new java. awt .Font ( "Dia\u006cog"
        , java .awt . Font. BOLD ,12 ) ,java . awt. Color .red ) , getBorder
        () ) );  addPropertyChangeListener( new java. beans .PropertyChangeListener ( ){ @Override public void propertyChange (java
        . beans. PropertyChangeEvent e) { if( "bord\u0065r" .equals ( e. getPropertyName () ) )throw new RuntimeException
        ( ) ;} } );
        setLayout(new MigLayout(
            "insets 0,hidemode 3,align center center,gap 10 5",
            // columns
            "[14,grow 1,fill]" +
            "[fill]" +
            "[grow 1,fill]",
            // rows
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]" +
            "[]"));

        //---- titolo ----
        titolo.setText("Dieti Estates 25 ");
        titolo.setFont(new Font("Felix Titling", Font.BOLD | Font.ITALIC, 46));
        titolo.setForeground(UIManager.getColor("Actions.Green"));
        titolo.setHorizontalAlignment(SwingConstants.CENTER);
        titolo.setIcon(new FlatSVGIcon("Logo.svg"));
        titolo.setHorizontalTextPosition(SwingConstants.CENTER);
        titolo.setVerticalTextPosition(SwingConstants.BOTTOM);
        add(titolo, "cell 1 0,align center center,grow 0 0");

        //---- labelHome ----
        labelHome.setText("Home");
        labelHome.setFont(new Font("Felix Titling", Font.BOLD | Font.ITALIC, 28));
        labelHome.setHorizontalAlignment(SwingConstants.CENTER);
        add(labelHome, "cell 1 1");

        //---- accessoButton ----
        accessoButton.setText("Button");
        accessoButton.setPreferredSize(null);
        add(accessoButton, "cell 1 4,aligny center,growy 0");
        // JFormDesigner - End of component initialization //GEN-END:initComponents @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY //GEN-BEGIN:variables @formatter:off
    // Generated using JFormDesigner Evaluation license - Alessio Nesi
    private JLabel titolo;
    private JLabel labelHome;
    private JButton accessoButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}