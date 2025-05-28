package gui.costum_components;

import controller.Controller;
import gui.InterfacciaAgenteImmobiliare;
import gui.InterfacciaListaImmobili;
import gui.PaginaImmobileDettagliata;
import model.Immobile;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

public class ImmobilePanelSingolo {

    private final Color borderColor = new Color(24, 122, 26);
    private final Font borderFont = new Font("Segoe UI", Font.BOLD, 18);

    protected static class BorderStyler {
        public static TitledBorder createStyledBorder(String title, Color lineColor, int thickness, Font font) {
            TitledBorder titledBorder = new TitledBorder(title);
            titledBorder.setBorder(BorderFactory.createLineBorder(lineColor, thickness));
            titledBorder.setTitleColor(new Color(0, 0, 0));
            titledBorder.setTitleFont(font);
            return titledBorder;
        }
        public static Border createSelectedBorder(String title, Color lineColor, Font font) {
            TitledBorder innerBorder = createStyledBorder(title, lineColor, 1, font);
            return BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(lineColor, 2, true),
                    innerBorder
            );
        }

    }

    public JPanel createImmobileSingoloPanel(JFrame frame,Immobile immobile, Controller control, String token, JPanel selectedPanel) {
        JPanel panel = new JPanel();
        panel.setBorder(BorderStyler.createStyledBorder(immobile.getTipoVendita(), borderColor, 1, borderFont));
        panel.setPreferredSize(new Dimension(780, 170));
        panel.setLayout(new MigLayout(
                "hidemode 3",
                "[325][250][325]",
                "[95][95][95][95]"));

        // Panel with the image as a background
        try {
            URL imageUrl = getClass().getResource("/CasaProva2.jpeg");
            if (imageUrl != null) {
                ImageIcon icon = new ImageIcon(imageUrl);
                JLabel imageLabel = new JLabel(icon);
                imageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                panel.add(imageLabel, "cell 0 0 2 2");
            } else {
                JLabel errorLabel = new JLabel("Image not found");
                errorLabel.setBackground(Color.RED);
                errorLabel.setOpaque(true);
                panel.add(errorLabel, "cell 0 0 2 2");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Font font = new Font("Segoe UI", Font.PLAIN, 20);
        // Adding other components to the main pane
        JLabel labelIndirizzo = new JLabel("<html>" + immobile.getIndirizzo().toString() + "</html>");
        labelIndirizzo.setFont(font);
        panel.add(labelIndirizzo, "cell 2 0 2 1");


        JLabel labelNStanze = new JLabel("Numero Stanze : "+immobile.getNumStanze());
        labelNStanze.setFont(font);
        panel.add(labelNStanze, "cell 2 1");

        JLabel labelNBagni = new JLabel("Numero Bagni : "+immobile.getNumBagni());
        labelNBagni.setFont(font);
        panel.add(labelNBagni, "cell 3 1");

        JLabel labelPrezzo = new JLabel("Prezzo : "+immobile.getPrezzo());
        labelPrezzo.setFont(font);
        panel.add(labelPrezzo, "cell 0 2 2 2,alignx left,growx 0");

        JLabel labelMetriQuadri = new JLabel("Merti Quadri : "+immobile.getSuperficie());
        labelMetriQuadri.setFont(font);
        panel.add(labelMetriQuadri, "cell 2 2");

        JLabel labelClasseEnergetica = new JLabel("Classe Energetica : "+immobile.getClasseEnergetica());
        labelClasseEnergetica.setFont(font);
        panel.add(labelClasseEnergetica, "cell 3 2");

        // MouseAdaptor doppio click
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    // Single click - select/deselect the panel
                    selectPanel(panel,selectedPanel);
                } else if (e.getClickCount() == 2) {
                    // Double-click - navigate to the details page
                    navigateToDetailsPage(frame,panel,control,token);
                }
            }
        });


        return panel;
    }

    private void navigateToDetailsPage(JFrame frame,JPanel panel, Controller control, String token) {
        // Get the property name/ID from the panel border
        // Create and navigate to the details page
        if (frame != null) {
            // Navigate to the details page, passing the `title` to the new JPanel or JFrame
            PaginaImmobileDettagliata detailsPage = new PaginaImmobileDettagliata(frame,control,token);
            frame.setContentPane(detailsPage);
            frame.revalidate();
            frame.repaint();
        } else {
            System.err.println("Parent frame not found for navigation.");
        }
    }

    private void selectPanel(JPanel panel,JPanel selectedPanel) {
        // Deselect a previous panel if exists
        if (selectedPanel != null) {
            // Get the title from the current border
            String title;
            Border currentBorder = selectedPanel.getBorder();

            if (currentBorder instanceof CompoundBorder) {
                // It's currently selected - extract title from an inner border
                TitledBorder innerBorder = (TitledBorder)((CompoundBorder)currentBorder).getInsideBorder();
                title = innerBorder.getTitle();
            } else {
                // It's a simple TitledBorder
                title = ((TitledBorder)currentBorder).getTitle();
            }

            // Restore standard styled border with the same title
            selectedPanel.setBorder(BorderStyler.createStyledBorder(title, borderColor, 1, borderFont));
        }

        // Select a new panel
        if (panel != selectedPanel) {
            selectedPanel = panel;

            // Get the title from the current border
            String title = ((TitledBorder)panel.getBorder()).getTitle();

            // Apply the selected border with consistent styling
            panel.setBorder(BorderStyler.createSelectedBorder(title, borderColor, borderFont));
            //buttonElimina.setEnabled(true);
        } else {
            // Clicking the same panel deselects it
            selectedPanel = null;
            //buttonElimina.setEnabled(false);
        }
        panel.repaint();
    }


}
