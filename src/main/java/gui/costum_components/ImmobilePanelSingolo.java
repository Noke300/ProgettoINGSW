package gui.costum_components;

import controller.Controller;
import gui.PaginaImmobileDettagliata;
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
import java.net.URL;
import java.util.Base64;

public class ImmobilePanelSingolo {
    private static final Color borderColor = new Color(24, 122, 26);
        private static final Font borderFont = new Font("Segoe UI", Font.BOLD, 18);

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
            panel.setPreferredSize(new Dimension(780, 170));
            panel.setLayout(new MigLayout("hidemode 3", "[325][250][325]", "[95][95][95][95]"));

            try {
                if (immobileEImmagini.getImmagini() != null && !immobileEImmagini.getImmagini().isEmpty()) {

                    // Decodifica la stringa Base64 in un array di byte
                    byte[] imageBytes = Base64.getDecoder().decode(immobileEImmagini.getImmagini().get(0));

                    // Converte l'array di byte in un'immagine
                    ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
                    Image image = ImageIO.read(bis);
                    bis.close();

                    Image scaledImage = image.getScaledInstance(330, 180, Image.SCALE_SMOOTH);
                    JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
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

            panel.add(new JLabel(("<html>" + immobile.getIndirizzo().toString() + "</html>")), "cell 2 0 2 1");
            panel.add(new JLabel("Numero Stanze : " + immobile.getNumStanze()), "cell 2 1");
            panel.add(new JLabel("Numero Bagni : " + immobile.getNumBagni()), "cell 3 1");
            panel.add(new JLabel("Prezzo : " + immobile.getPrezzo()), "cell 0 2 2 2");
            panel.add(new JLabel("Metri Quadri : " + immobile.getSuperficie()), "cell 2 2");
            panel.add(new JLabel("Classe Energetica : " + immobile.getClasseEnergetica()), "cell 3 2");

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
                PaginaImmobileDettagliata detailsPage = new PaginaImmobileDettagliata(frame, immobileEImmagini, control, token);
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
