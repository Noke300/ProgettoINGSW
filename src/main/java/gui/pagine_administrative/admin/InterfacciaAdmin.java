/*
 * Created by JFormDesigner on Mon Jul 14 14:23:45 CEST 2025
 */

package gui.pagine_administrative.admin;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;

import controller.ControllerAdmin;
import gui.costum_components.AggiungiDipendentiPannel;
import gui.costum_components.BottoniOpzioni;
import gui.costum_components.IconClass;
import gui.costum_components.ImmobilePanelSingolo;
import model.AdminSupport;
import model.AgenteImmobiliare;
import model.AgenziaImmobiliare;
import model.ImmobileEImmagini;
import net.miginfocom.swing.*;

/**
 * @author marco
 */
public class InterfacciaAdmin extends JPanel {
    protected AgenziaImmobiliare admin;
    private BottoniOpzioni bottoniOpzioni = new BottoniOpzioni();
    JComboBox<String> tipoCombo = new JComboBox<>(new String[]{"Admin Support", "Agente Immobiliare"});

    public InterfacciaAdmin(JFrame frame, ControllerAdmin controller, String token) {
        initComponents();
        bottoniOpzioni.setUpOpzioniMenu(frame,controller,token,menuBarOpzioni,menuOpzioni);
        this.admin = controller.getAgenziaImmobiliare(token);

        String[] colonne = {"Nome", "Cognome", "Email", "Tipo"};
        DefaultTableModel model = new DefaultTableModel(colonne, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // blocca l'editing su tutte le celle
            }
        };


        for (AdminSupport d : admin.getAdminSupport()) {
            model.addRow(new Object[]{d.getNomeSupport(), d.getCognomeSupport(), d.getEmailSupport(), "Admin Support"});
        }
        for (AgenteImmobiliare d : admin.getAgentiImmobiliari()){
            model.addRow(new Object[]{d.getNomeAgente(), d.getCognomeAgente(), d.getEmailAgente(), "Agente Immobiliari"});
        }
        panelListaImmobile.setLayout(new MigLayout("wrap 1, align center")); // One component per row
        JPanel[] selectedPanel = new JPanel[1];

        Dimension dim  = new Dimension(820,289);
        for (ImmobileEImmagini immobileEImmagini : admin.getListaImmobili()) {
            JPanel panel = ImmobilePanelSingolo.createImmobileSingoloPanel(frame,immobileEImmagini,controller,token,selectedPanel);
            panel.setName(immobileEImmagini.getImmobile().getId());
            panel.setMaximumSize(dim);
            panel.setPreferredSize(dim);
            panelListaImmobile.add(panel);
        }

        JTable tabella = new JTable(model);
        tabella.getTableHeader().setReorderingAllowed(false);
        tabella.setFont(IconClass.getFontImmobiliC());
        tabella.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!tabella.getBounds().contains(e.getPoint())) {
                    tabella.clearSelection();
                    buttonElimina.setEnabled(false);
                }
            }
        });


        tabella.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                int selectedRow = tabella.getSelectedRow();
                buttonElimina.setEnabled(selectedRow != -1);
            }
        });

        buttonElimina.addActionListener(e -> {
            int selectedRow = tabella.getSelectedRow();
            String tipo = (String) tabella.getValueAt(selectedRow, 3); // Colonna 'Tipo'
            String email = (String) tabella.getValueAt(selectedRow, 2); // Colonna 'Email'
            boolean success = false;

            if (tipo.equals("Admin Support")) {
                for (AdminSupport d : admin.getAdminSupport()) {
                    if (d.getEmailSupport().equals(email)) {
                        success = controller.eliminaAdminSupport(d, token);
                        break;
                    }
                }
            } else if (tipo.equals("Agente Immobiliari")) {
                for (AgenteImmobiliare d : admin.getAgentiImmobiliari()) {
                    if (d.getEmailAgente().equals(email)) {
                        success = controller.eliminaAgenteImmobiliare(d, token);
                        break;
                    }
                }
            }

            if (success) {
                model.removeRow(selectedRow);
                JOptionPane.showMessageDialog(this, "Dipendente eliminato con successo.");
            } else {
                JOptionPane.showMessageDialog(this, "Errore durante l’eliminazione.");
            }
        });

        buttonAggiungiDipendenti.addActionListener(e -> AggiungiDipendentiPannel.creaPanelAdmin(frame,controller,admin,token,model,tipoCombo));


        panelListaDipendenti.setViewportView(tabella);
        panelListaDipendenti.revalidate();
        panelListaDipendenti.repaint();


    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Educational license - Marco Scotto di Uccio Scotto di Uccio (Marco Scotto Di Uccio)
        panelOpzioni = new JPanel();
        labelNomeAzzienda = new JLabel();
        menuBarOpzioni = new JMenuBar();
        menuOpzioni = new JMenu();
        panelListaDipendenti = new JScrollPane();
        panelListaImmobileScroll = new JScrollPane();
        panelListaImmobile = new JPanel();
        panelBottomBar = new JPanel();
        buttonRiorganizzaImmobile = new JButton();
        buttonAggiungiDipendenti = new JButton();
        buttonElimina = new JButton();

        //======== this ========
        setBorder(new BevelBorder(BevelBorder.LOWERED));
        setLayout(new MigLayout(
            "fill,insets 0,hidemode 3,align center center",
            // columns
            "0[139,grow,fill]0" +
            "[117,fill]0" +
            "[96,fill]0" +
            "[96,fill]0" +
            "[93,fill]0" +
            "[fill]0" +
            "[112,fill]0" +
            "[0,fill]0" +
            "[71,fill]0" +
            "[0,center]0" +
            "[24,grow,fill]0" +
            "[9,fill]0" +
            "[14,grow,fill]",
            // rows
            "[48,grow,fill]" +
            "[48,grow,fill]" +
            "[48,grow,center]" +
            "[grow,center]" +
            "[grow,fill]0" +
            "[grow,fill]0" +
            "[grow,fill]0" +
            "[grow,fill]0"));

        //======== panelOpzioni ========
        {
            panelOpzioni.setBackground(UIManager.getColor("Actions.Green"));
            panelOpzioni.setBorder(new BevelBorder(BevelBorder.RAISED, null, Color.black, null, null));
            panelOpzioni.setLayout(new MigLayout(
                "fill,insets 0,hidemode 3,gap 5 5",
                // columns
                "[0,fill]0" +
                "[fill]" +
                "[fill]0" +
                "[138,fill]" +
                "[fill]",
                // rows
                "[67,grow,fill]0"));

            //---- labelNomeAzzienda ----
            labelNomeAzzienda.setText("Nome");
            labelNomeAzzienda.setFont(new Font("Segoe UI", Font.PLAIN, 20));
            panelOpzioni.add(labelNomeAzzienda, "cell 0 0,align left center,grow 0 0");

            //======== menuBarOpzioni ========
            {
                menuBarOpzioni.setBorderPainted(false);
                menuBarOpzioni.setBackground(new Color(0x4e5052));
                menuBarOpzioni.setFont(new Font("Segoe UI", Font.PLAIN, 16));

                //======== menuOpzioni ========
                {
                    menuOpzioni.setText("Opzioni");
                    menuOpzioni.setIcon(UIManager.getIcon("Table.descendingSortIcon"));
                    menuOpzioni.setFont(new Font("Segoe UI", Font.PLAIN, 22));
                }
                menuBarOpzioni.add(menuOpzioni);
            }
            panelOpzioni.add(menuBarOpzioni, "cell 4 0,alignx trailing,grow 0 100");
        }
        add(panelOpzioni, "north");

        //======== panelListaDipendenti ========
        {
            panelListaDipendenti.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            panelListaDipendenti.setViewportBorder(null);
        }
        add(panelListaDipendenti, "cell 3 0 10 8,grow");

        //======== panelListaImmobileScroll ========
        {
            panelListaImmobileScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            panelListaImmobileScroll.setViewportBorder(null);

            //======== panelListaImmobile ========
            {
                panelListaImmobile.setBorder(new LineBorder(UIManager.getColor("Actions.Green"), 2, true));
                panelListaImmobile.setLayout(new GridLayout(2, 0));
            }
            panelListaImmobileScroll.setViewportView(panelListaImmobile);
        }
        add(panelListaImmobileScroll, "cell 0 0 3 8");

        //======== panelBottomBar ========
        {
            panelBottomBar.setBorder(new EtchedBorder());
            panelBottomBar.setBackground(UIManager.getColor("Actions.Green"));
            panelBottomBar.setLayout(new MigLayout(
                "insets 0,hidemode 3,gap 5 5",
                // columns
                "[88,grow 1,fill]unrel" +
                "[123,grow 1,fill]0" +
                "[grow 1,fill]rel" +
                "[grow 1,fill]",
                // rows
                "[33,fill]" +
                "[]"));

            //---- buttonRiorganizzaImmobile ----
            buttonRiorganizzaImmobile.setIcon(UIManager.getIcon("FileChooser.homeFolderIcon"));
            buttonRiorganizzaImmobile.setBackground(UIManager.getColor("Actions.Green"));
            buttonRiorganizzaImmobile.setText("Ricognizione immobili");
            buttonRiorganizzaImmobile.setBorderPainted(false);
            panelBottomBar.add(buttonRiorganizzaImmobile, "cell 0 0 1 2");

            //---- buttonAggiungiDipendenti ----
            buttonAggiungiDipendenti.setText("Aggiungi Dipendente");
            buttonAggiungiDipendenti.setBorderPainted(false);
            buttonAggiungiDipendenti.setBackground(UIManager.getColor("Actions.Green"));
            panelBottomBar.add(buttonAggiungiDipendenti, "cell 2 0 1 2,growy");

            //---- buttonElimina ----
            buttonElimina.setText("Rimuovi Dipendete ");
            buttonElimina.setBorder(new EmptyBorder(5, 5, 5, 5));
            buttonElimina.setBackground(UIManager.getColor("Actions.Green"));
            buttonElimina.setEnabled(false);
            panelBottomBar.add(buttonElimina, "cell 3 0 1 2,grow");
        }
        add(panelBottomBar, "south,growy");
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    // Generated using JFormDesigner Educational license - Marco Scotto di Uccio Scotto di Uccio (Marco Scotto Di Uccio)
    private JPanel panelOpzioni;
    private JLabel labelNomeAzzienda;
    private JMenuBar menuBarOpzioni;
    private JMenu menuOpzioni;
    private JScrollPane panelListaDipendenti;
    private JScrollPane panelListaImmobileScroll;
    private JPanel panelListaImmobile;
    private JPanel panelBottomBar;
    private JButton buttonRiorganizzaImmobile;
    private JButton buttonAggiungiDipendenti;
    private JButton buttonElimina;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
