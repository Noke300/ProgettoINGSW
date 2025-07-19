package gui.costum_components;

import controller.Controller;
import gui.pagine_comuni.PaginaImmobileDettagliata;
import model.Immobile;
import model.ImmobileEImmagini;
import net.miginfocom.swing.MigLayout;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayInputStream;
import java.util.Base64;

public class ImmobilePanelSingolo {

    private static final Color borderColor = new Color(24, 122, 26);
    private static final Font borderFont = new Font("Segoe UI", Font.BOLD, 18);
    private static final Font labelFont = new Font("Segoe UI", Font.PLAIN, 16);


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

    public static JPanel createImmobileSingoloPanel(JFrame frame, ImmobileEImmagini immobileEImmagini, Controller control, String token, JPanel[] selectedPanelRef) {
        JPanel panel = new JPanel();
        Immobile immobile = immobileEImmagini.getImmobile();
        panel.setBorder(BorderStyler.createStyledBorder(immobile.getTipoVendita(), borderColor, 1, borderFont));
        panel.setPreferredSize(new Dimension(780, 195));
        panel.setLayout(new MigLayout("hidemode 3", "[325][250][325]", "[95][95][95][95][95]"));

        try {
            if (immobileEImmagini.getImmagini() != null && !immobileEImmagini.getImmagini().isEmpty()) {
                byte[] imageBytes = Base64.getDecoder().decode(immobileEImmagini.getImmagini().get(0));
                ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
                Image image = ImageIO.read(bis);
                bis.close();

                Image scaledImage = image.getScaledInstance(320, 160, Image.SCALE_SMOOTH);
                JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
                imageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                panel.add(imageLabel, "cell 0 0 2 2");
            } else {
                JLabel errorLabel = new JLabel("Image not found");
                errorLabel.setFont(labelFont);
                errorLabel.setBackground(Color.RED);
                errorLabel.setOpaque(true);
                panel.add(errorLabel, "cell 0 0 2 2");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JLabel indirizzoLabel = new JLabel("<html>" + immobile.getIndirizzo().toString() + "</html>");
        indirizzoLabel.setFont(labelFont);
        panel.add(indirizzoLabel, "cell 2 0 2 1");

        JLabel stanzeLabel = new JLabel("Numero Stanze : " + immobile.getNumStanze());
        stanzeLabel.setFont(labelFont);
        panel.add(stanzeLabel, "cell 2 1");

        JLabel bagniLabel = new JLabel("Numero Bagni : " + immobile.getNumBagni());
        bagniLabel.setFont(labelFont);
        panel.add(bagniLabel, "cell 3 1");

        JLabel prezzoLabel = new JLabel("Prezzo : " + immobile.getPrezzo());
        prezzoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        panel.add(prezzoLabel, "cell 0 3");

        JLabel superficieLabel = new JLabel("Metri Quadri : " + immobile.getSuperficie());
        superficieLabel.setFont(labelFont);
        panel.add(superficieLabel, "cell 2 2");

        JLabel classeLabel = new JLabel("Classe Energetica : " + immobile.getClasseEnergetica());
        classeLabel.setFont(labelFont);
        panel.add(classeLabel, "cell 3 2");

        // 🆕 Nuove etichette nella riga successiva
        JLabel scuoleLabel = new JLabel("Scuole");
        scuoleLabel.setFont(labelFont);
        scuoleLabel.setIcon(IconClass.getScuolaIcon());
        if (immobile.isParchi()) {
            panel.add(scuoleLabel, "cell 2 3");
        }

        JLabel trasportiLabel = new JLabel("Trasporti ");
        trasportiLabel.setFont(labelFont);
        trasportiLabel.setIcon(IconClass.getTransportIcon());
        if(immobile.isTrasportoPubblico()) {
            panel.add(trasportiLabel, "cell 3 3");
        }

        JLabel parchiLabel = new JLabel("Parchi ");
        parchiLabel.setFont(labelFont);
        parchiLabel.setIcon(IconClass.getParchiIcon());
        if (immobile.isParchi()) {
            panel.add(parchiLabel, "cell 4 3");
        }

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    selectPanel(panel, selectedPanelRef);
                } else if (e.getClickCount() == 2) {
                    navigateToDetailsPage(frame, immobileEImmagini, control, token);
                }
            }
        });

        return panel;
    }

    private static void navigateToDetailsPage(JFrame frame, ImmobileEImmagini immobileEImmagini, Controller control, String token) {
            if (frame != null) {
                Container content = frame.getContentPane();
                PaginaImmobileDettagliata detailsPage = new PaginaImmobileDettagliata(frame, immobileEImmagini, control, token,content);
                frame.setContentPane(detailsPage);
                frame.revalidate();
                frame.repaint();
            } else {
                System.err.println("Parent frame not found for navigation.");
            }
        }

        private static void selectPanel(JPanel panel, JPanel[] selectedPanelRef) {
            JPanel selectedPanel = selectedPanelRef[0];

            if (selectedPanel != null) {
                String title = extractBorderTitle(selectedPanel.getBorder());
                selectedPanel.setBorder(BorderStyler.createStyledBorder(title, borderColor, 1, borderFont));
            }

            if (panel != selectedPanel) {
                selectedPanelRef[0] = panel;
                String title = extractBorderTitle(panel.getBorder());
                panel.setBorder(BorderStyler.createSelectedBorder(title, borderColor, borderFont));
            } else {
                selectedPanelRef[0] = null;
            }

            panel.repaint();
        }

        private static String extractBorderTitle(Border border) {
            if (border instanceof TitledBorder) {
                return ((TitledBorder) border).getTitle();
            } else if (border instanceof CompoundBorder) {
                Border inside = ((CompoundBorder) border).getInsideBorder();
                if (inside instanceof TitledBorder) {
                    return ((TitledBorder) inside).getTitle();
                }
            }
            return "Senza titolo";
        }
    }
