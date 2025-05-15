package gui.costum_components;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.WaypointRenderer;
// import your CustomWaypoint class

import java.awt.Color; // For fallback
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import org.kordamp.ikonli.swing.FontIcon;

public class CustomWaypointRenderer implements WaypointRenderer<CustomWaypoint> {

    @Override
    public void paintWaypoint(Graphics2D g, JXMapViewer mapViewer, CustomWaypoint waypoint) {
        Point2D point = mapViewer.getTileFactory().geoToPixel(waypoint.getPosition(), mapViewer.getZoom());
        FontIcon icon = waypoint.getFontIcon();

        if (icon != null) {
            int iconWidth = icon.getIconWidth();
            int iconHeight = icon.getIconHeight();

            // Calculate top-left position to center the icon on the point
            int x = (int) point.getX() - iconWidth / 2;
            int y = (int) point.getY() - iconHeight / 2;

            // If your icon is a pin and you want the bottom-center to be at the point:
            // int y = (int) point.getY() - iconHeight;
            // You might also need to adjust x slightly if the pin's tip isn't centered in the icon's width

            // The JXMapViewer is a Component, so we can pass it to paintIcon
            icon.paintIcon(mapViewer, g, x, y);

        } else {
            // Fallback: Draw shapes if no FontIcon is available (your old code)
            int size = 10;
            // Assuming CustomWaypoint might have a getColor() for fallback, or use a default
            // For simplicity, let's use a default color if waypoint.getColor() isn't available
            Color fallbackColor = Color.GRAY; // Or get from waypoint if it has a color field

            // If your CustomWaypoint has a .getColor() method for the old system:
            // if (waypoint.getColor() != null) fallbackColor = waypoint.getColor();

            g.setColor(fallbackColor);

            if ("Indirizzo".equals(waypoint.getInfo())) { // Use .equals for string comparison
                g.fillOval((int) point.getX() - size / 2, (int) point.getY() - size / 2, size, size);
                g.setColor(Color.BLACK);
                g.drawOval((int) point.getX() - size / 2, (int) point.getY() - size / 2, size, size);
            } else {
                g.fillRect((int) point.getX() - size / 2, (int) point.getY() - size / 2, size, size);
                g.setColor(Color.BLACK);
                g.drawRect((int) point.getX() - size / 2, (int) point.getY() - size / 2, size, size);
            }
        }
    }
}