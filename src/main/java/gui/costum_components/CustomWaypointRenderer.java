package gui.costum_components;


import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.WaypointRenderer;

import java.awt.*;
import java.awt.geom.Point2D;

public class CustomWaypointRenderer implements WaypointRenderer<CustomWaypoint> {
    @Override
    public void paintWaypoint(Graphics2D g, JXMapViewer map, CustomWaypoint waypoint) {
        int size = 10;
        g.setColor(waypoint.getColor());
        Point2D point = map.getTileFactory().geoToPixel(waypoint.getPosition(), map.getZoom());

        if (waypoint.getInfo().equals("Indirizzo")) {

            g.fillOval((int) point.getX() - size / 2, (int) point.getY() - size / 2, size, size);
            g.setColor(Color.BLACK);  // Bordo nero
            g.drawOval((int) point.getX() - 3, (int) point.getY() - 3, size, size);

        } else {

            g.fillRect((int) point.getX() - size / 2, (int) point.getY() - size / 2, size, size);
            g.setColor(Color.BLACK);
            g.drawRect((int) point.getX() - size / 2, (int) point.getY() - size / 2, size, size);

        }

    }
}