package gui.costumComponents;

import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;

import java.awt.*;

public class CustomWaypoint extends DefaultWaypoint {
    private final Color color;
    private final String info;

    public CustomWaypoint(GeoPosition coord, Color color, String info) {
        super(coord);
        this.color = color;
        this.info = info;
    }

    public Color getColor() {
        return color;
    }

    public String getInfo() {
        return info;
    }
}