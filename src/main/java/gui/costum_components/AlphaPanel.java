package gui.costum_components;

import javax.swing.*;
import java.awt.*;

// --- Custom Panel supporting Alpha Transparency ---
public class AlphaPanel extends JPanel {
    private float alpha = 1.0f; // Default to fully opaque

    public AlphaPanel() {
        super();
        setOpaque(false); // Crucial: Make the panel non-opaque so transparency works
    }

    public AlphaPanel(LayoutManager layout) {
        super(layout);
        setOpaque(false); // Crucial: Make the panel non-opaque so transparency works
    }

    public void setAlpha(float alpha) {
        if (alpha < 0.0f) alpha = 0.0f;
        if (alpha > 1.0f) alpha = 1.0f;
        this.alpha = alpha;
        // No need to repaint here, the AnimationLayer will handle repaint cycles
    }

    public float getAlpha() {
        return alpha;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Paints background if opaque (which it isn't)

        Graphics2D g2d = (Graphics2D) g.create();
        // Apply the alpha composite
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

        // Draw the background *with alpha* if one is set
        if (getBackground() != null) {
            g2d.setColor(getBackground());
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }

        // Now let the superclass paint borders etc. (still under alpha)
        // super.paintComponent(g2d); // Re-painting background etc might not be desired here
        // Let paintChildren handle content

        g2d.dispose(); // Dispose of the copy
    }

    @Override
    protected void paintChildren(Graphics g) {
        // Paint children *with* the alpha applied from paintComponent's setup
        // We need to re-apply the composite to the graphics context used for children
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        super.paintChildren(g2d);
        g2d.dispose();
    }
}


