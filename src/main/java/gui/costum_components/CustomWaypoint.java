package gui.costum_components;

import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;
import org.kordamp.ikonli.swing.FontIcon; // Import FontIcon
// No need for java.awt.Color here if the FontIcon itself defines its color

public class CustomWaypoint extends DefaultWaypoint {
    private String info;
    private FontIcon fontIcon; // Store the FontIcon directly

    // Constructor for FontAwesome icons from Ikonli
    public CustomWaypoint(GeoPosition coord, String info, FontIcon fontIcon) {
        super(coord);
        this.info = info;
        this.fontIcon = fontIcon;
    }

    // Getters
    public String getInfo() {
        return info;
    }

    public FontIcon getFontIcon() {
        return fontIcon;
    }
}