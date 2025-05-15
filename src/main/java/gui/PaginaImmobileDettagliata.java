/*
 * Created by JFormDesigner on Sat Apr 26 03:06:22 CEST 2025
 */

package gui;

import java.awt.*;
import java.net.URL;
import javax.swing.*;
import javax.swing.border.*;

import controller.Controller;
import net.miginfocom.swing.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 * @author marco
 */
public class PaginaImmobileDettagliata extends JPanel {
    public PaginaImmobileDettagliata(JFrame frame, Controller control,String token) {
        initComponents();

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(200, "Sales", "January");
        dataset.addValue(150, "Sales", "February");
        dataset.addValue(180, "Sales", "March");
        dataset.addValue(260, "Sales", "April");
        dataset.addValue(300, "Sales", "May");

        JFreeChart chart = ChartFactory.createBarChart(
                "Offerte Immobile",
                "Month",
                "Offerte",
                dataset);

        ChartPanel chartPanel = new ChartPanel(chart);

        panelChart.add(chartPanel, BorderLayout.CENTER);
        try {
            URL imageUrl = getClass().getResource("/CasaProva3.jpeg");
            if (imageUrl != null) {
                ImageIcon icon = new ImageIcon(imageUrl);
                JLabel imageLabel = new JLabel(icon);
                imageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                panelImmagineImmobile.add(imageLabel, BorderLayout.CENTER);
            } else {
                JLabel errorLabel = new JLabel("Image not found");
                errorLabel.setBackground(Color.RED);
                errorLabel.setOpaque(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
        backButtonHome.addActionListener(e -> {
            // Supponiamo che la classe Home abbia il metodo createHomePanel
            menuOpzioni.setVisible(false);
            menuBarOpzioni.setVisible(false);

            frame.setContentPane(new Home().createHomePanel(frame));
            frame.revalidate();
            frame.repaint();
        });

        logInButton.addActionListener(e -> {
            menuOpzioni.setVisible(false);
            menuBarOpzioni.setVisible(false);

            frame.setContentPane(new LogIn(frame, control, token));
            frame.revalidate();
            frame.repaint();
        });

        ricercaButton.addActionListener(e-> {

            frame.setContentPane(new Ricerca().createRicercaPanel(frame,control,token));
            frame.revalidate();
            frame.repaint();
        });
    }


    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Evaluation license - Marco
        panelTopBar = new JPanel();
        menuBarOpzioni = new JMenuBar();
        menuOpzioni = new JMenu();
        logInButton = new JButton();
        ricercaButton = new JButton();
        backButtonHome = new JButton();
        button1 = new JButton();
        panelImmagineImmobile = new JPanel();
        button3 = new JButton();
        button4 = new JButton();
        panelChart = new JPanel();

        //======== this ========
        setBorder ( new javax . swing. border .CompoundBorder ( new javax . swing. border .TitledBorder ( new javax . swing. border .
        EmptyBorder ( 0, 0 ,0 , 0) ,  "JFor\u006dDesi\u0067ner \u0045valu\u0061tion" , javax. swing .border . TitledBorder. CENTER ,javax . swing
        . border .TitledBorder . BOTTOM, new java. awt .Font ( "Dia\u006cog", java .awt . Font. BOLD ,12 ) ,
        java . awt. Color .red ) , getBorder () ) );  addPropertyChangeListener( new java. beans .PropertyChangeListener ( )
        { @Override public void propertyChange (java . beans. PropertyChangeEvent e) { if( "bord\u0065r" .equals ( e. getPropertyName () ) )
        throw new RuntimeException( ) ;} } );
        setLayout(new MigLayout(
            "fill,hidemode 3",
            // columns
            "[188,fill]" +
            "[174,center]" +
            "[174,center]" +
            "[174,center]" +
            "[174,center]",
            // rows
            "[452]" +
            "[452]" +
            "[452]" +
            "[452,grow]" +
            "[452]0" +
            "[106,grow]"));

        //======== panelTopBar ========
        {
            panelTopBar.setBorder(new LineBorder(Color.black, 2, true));
            panelTopBar.setBackground(UIManager.getColor("Actions.Green"));
            panelTopBar.setLayout(new BorderLayout());

            //======== menuBarOpzioni ========
            {
                menuBarOpzioni.setBorderPainted(false);
                menuBarOpzioni.setBackground(new Color(0x4e5052));
                menuBarOpzioni.setFont(new Font("Segoe UI", Font.PLAIN, 26));

                //======== menuOpzioni ========
                {
                    menuOpzioni.setText("Opzioni");
                    menuOpzioni.setIcon(UIManager.getIcon("Table.descendingSortIcon"));
                    menuOpzioni.setFont(new Font("Segoe UI", Font.PLAIN, 26));

                    //---- logInButton ----
                    logInButton.setText("Log In");
                    logInButton.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                    menuOpzioni.add(logInButton);

                    //---- ricercaButton ----
                    ricercaButton.setText("Ricerca");
                    ricercaButton.setIcon(UIManager.getIcon("PasswordField.revealIcon"));
                    ricercaButton.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                    menuOpzioni.add(ricercaButton);

                    //---- backButtonHome ----
                    backButtonHome.setText("Home");
                    backButtonHome.setIcon(UIManager.getIcon("FileChooser.homeFolderIcon"));
                    backButtonHome.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                    menuOpzioni.add(backButtonHome);
                }
                menuBarOpzioni.add(menuOpzioni);
            }
            panelTopBar.add(menuBarOpzioni, BorderLayout.LINE_END);

            //---- button1 ----
            button1.setText("Back");
            button1.setFont(new Font("Segoe UI", Font.PLAIN, 24));
            button1.setIcon(UIManager.getIcon("Tree.openIcon"));
            button1.setBackground(UIManager.getColor("Actions.Green"));
            button1.setBorder(new EtchedBorder(EtchedBorder.RAISED));
            button1.setBorderPainted(false);
            panelTopBar.add(button1, BorderLayout.LINE_START);
        }
        add(panelTopBar, "north");

        //======== panelImmagineImmobile ========
        {
            panelImmagineImmobile.setBorder(new EtchedBorder(EtchedBorder.RAISED, UIManager.getColor("Actions.Green"), null));
            panelImmagineImmobile.setLayout(new BorderLayout(1, 1));

            //---- button3 ----
            button3.setText(">");
            button3.setFont(new Font("Segoe UI", Font.BOLD, 24));
            button3.setBackground(new Color(0x00ffffff, true));
            panelImmagineImmobile.add(button3, BorderLayout.EAST);

            //---- button4 ----
            button4.setText("<");
            button4.setFont(new Font("Segoe UI", Font.BOLD, 24));
            button4.setBackground(new Color(0x00ffffff, true));
            panelImmagineImmobile.add(button4, BorderLayout.WEST);
        }
        add(panelImmagineImmobile, "cell 0 0 3 2,grow");

        //======== panelChart ========
        {
            panelChart.setLayout(new BorderLayout());
        }
        add(panelChart, "cell 3 2 2 3,grow");
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    // Generated using JFormDesigner Evaluation license - Marco
    private JPanel panelTopBar;
    private JMenuBar menuBarOpzioni;
    private JMenu menuOpzioni;
    private JButton logInButton;
    private JButton ricercaButton;
    private JButton backButtonHome;
    private JButton button1;
    private JPanel panelImmagineImmobile;
    private JButton button3;
    private JButton button4;
    private JPanel panelChart;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
