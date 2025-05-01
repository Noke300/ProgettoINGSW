package gui.costum_components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// --- Panel that Handles Animation ---
public class AnimationLayer extends JPanel {



    private static final int ANIMATION_DURATION_MS = 500; // Fade duration
    private static final int ANIMATION_STEP_DELAY = 15;  // Timer frequency

    private AlphaPanel currentPanel; // Changed type
    private AlphaPanel nextPanel;    // Changed type
    private Timer animationTimer;
    private long animationStartTime;

    public AnimationLayer() {
        setLayout(null);
        setOpaque(true);
        setBackground(Color.DARK_GRAY); // Background visible during fade
    }

    // Simplified switchToPanel for fading
    public void switchToPanel(AlphaPanel newPanelToShow) {
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop(); // Stop previous animation if any
            // Instantly finish the previous transition if interrupted
            if (currentPanel != null) {
                remove(currentPanel);
            }
            if (nextPanel != null) {
                nextPanel.setAlpha(1.0f);
                nextPanel.setBounds(0, 0, getWidth(), getHeight());
                currentPanel = nextPanel; // The interrupted 'next' becomes 'current'
                nextPanel = null;
            }
        }

        this.nextPanel = newPanelToShow;
        // Assuming the only component is the current one
        if (getComponentCount() > 0) {
            this.currentPanel = (AlphaPanel) getComponent(0);
        } else {
            this.currentPanel = null; // Should not happen after first panel shown
        }

        if (currentPanel == nextPanel) return; // No change needed

        // Prepare next panel
        nextPanel.setAlpha(0.0f); // Start fully transparent
        nextPanel.setBounds(0, 0, getWidth(), getHeight()); // Position at the same spot
        add(nextPanel);

        // Z-Order: new panel should be on top (lower index)
        if (currentPanel != null) {
            setComponentZOrder(nextPanel, 0);
            setComponentZOrder(currentPanel, 1);
        }

        animationStartTime = System.currentTimeMillis();

        animationTimer = new Timer(ANIMATION_STEP_DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long now = System.currentTimeMillis();
                long elapsedTime = now - animationStartTime;
                double progress = (double) elapsedTime / ANIMATION_DURATION_MS;

                if (progress >= 1.0) {
                    progress = 1.0;
                    ((Timer) e.getSource()).stop();
                    finishAnimation();
                } else {
                    animateStep(progress);
                }
                // Repaint the AnimationLayer itself to redraw children with new alpha
                repaint();
            }
        });
        animationTimer.start();
    }

    private void animateStep(double progress) {
        float fadeInAlpha = (float) progress;
        float fadeOutAlpha = (float) (1.0 - progress);

        if (nextPanel != null) {
            nextPanel.setAlpha(fadeInAlpha);
        }
        if (currentPanel != null) {
            currentPanel.setAlpha(fadeOutAlpha);
        }
        // No setBounds needed here as panels stay in place
    }

    private void finishAnimation() {
        // Ensure final state
        if (nextPanel != null) {
            nextPanel.setAlpha(1.0f);
            nextPanel.setBounds(0, 0, getWidth(), getHeight()); // Ensure final position/size
        }
        if (currentPanel != null) {
            remove(currentPanel); // Remove the faded-out panel
            currentPanel.setAlpha(1.0f); // Reset alpha for potential reuse
        }

        // Update references
        currentPanel = nextPanel; // The next panel is now the current one
        nextPanel = null;
        animationTimer = null;

        revalidate(); // May not be strictly necessary but good practice
        repaint();
    }

}
