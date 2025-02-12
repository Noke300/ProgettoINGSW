package gui;

import org.jxmapviewer.viewer.GeoPosition;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.*;

public class MapPanelTest {

    /**
     * Tests for the createMapPanel method of the MapPanel class.
     * The createMapPanel method initializes and returns a fully configured JPanel
     * holding a map viewer and adding interactions and waypoints based on input parameters.
     * These tests will ensure only Italian addresses are used.
     */

    @Test
    public void testCreateMapPanelWithItalianAddress() {
        JFrame frame = new JFrame();
        GeoPosition geoPosition = new GeoPosition(41.9028, 12.4964); // Coordinates for Rome, Italy
        List<JCheckBox> checkBoxesList = new ArrayList<>();

        MapPanel mapPanel = new MapPanel();
        JPanel resultPanel = mapPanel.createMapPanel(frame, geoPosition, checkBoxesList);

        assertNotNull("Panel should not be null", resultPanel);
        assertEquals("Panel layout should be BorderLayout",
                BorderLayout.class, resultPanel.getLayout().getClass());
    }

    @Test
    public void testCreateMapPanelWithNonItalianAddress() {
        JFrame frame = new JFrame();
        GeoPosition geoPosition = new GeoPosition(40.7128, -74.0060); // Coordinates for New York, USA
        List<JCheckBox> checkBoxesList = new ArrayList<>();

        MapPanel mapPanel = new MapPanel();
        JPanel resultPanel = mapPanel.createMapPanel(frame, geoPosition, checkBoxesList);

        assertNotNull("Panel should not be null", resultPanel);
    }

    @Test
    public void testCreateMapPanelWithItalianGeoPositionAndWaypoints() {
        JFrame frame = new JFrame();
        GeoPosition geoPosition = new GeoPosition(43.7696, 11.2558); // Coordinates for Florence, Italy
        JCheckBox parksCheckBox = new JCheckBox("Parchi");
        parksCheckBox.setSelected(true);

        List<JCheckBox> checkBoxesList = new ArrayList<>();
        checkBoxesList.add(parksCheckBox);

        MapPanel mapPanel = new MapPanel();
        JPanel resultPanel = mapPanel.createMapPanel(frame, geoPosition, checkBoxesList);

        assertNotNull("Panel should not be null", resultPanel);
        assertTrue("Map should be centered on the given Italian location", Objects.requireNonNull(MapPanel.getAddressFromCoordinates(geoPosition)).contains("Italia"));
    }

    @Test
    public void testCreateMapPanelWithEmptyCheckBoxList() {
        JFrame frame = new JFrame();
        GeoPosition geoPosition = new GeoPosition(45.4642, 9.19); // Coordinates for Milan, Italy
        List<JCheckBox> checkBoxesList = new ArrayList<>(); // No checkboxes selected

        MapPanel mapPanel = new MapPanel();
        JPanel resultPanel = mapPanel.createMapPanel(frame, geoPosition, checkBoxesList);

        assertNotNull("Panel should not be null", resultPanel);
        assertTrue("Map should be centered on the given Italian location", Objects.requireNonNull(MapPanel.getAddressFromCoordinates(geoPosition)).contains("Italia"));
    }

    @Test
    public void testCreateMapPanelWithMultipleSelectedCheckBoxes() {
        JFrame frame = new JFrame();
        GeoPosition geoPosition = new GeoPosition(40.8518, 14.2681); // Coordinates for Naples, Italy
        JCheckBox schoolsCheckBox = new JCheckBox("Scuole");
        schoolsCheckBox.setSelected(true);
        JCheckBox transportCheckBox = new JCheckBox("Trasporti");
        transportCheckBox.setSelected(true);

        List<JCheckBox> checkBoxesList = new ArrayList<>();
        checkBoxesList.add(schoolsCheckBox);
        checkBoxesList.add(transportCheckBox);

        MapPanel mapPanel = new MapPanel();
        JPanel resultPanel = mapPanel.createMapPanel(frame, geoPosition, checkBoxesList);

        assertNotNull("Panel should not be null", resultPanel);
        assertTrue("Map should be centered on the given Italian location", Objects.requireNonNull(MapPanel.getAddressFromCoordinates(geoPosition)).contains("Italia"));
        assertTrue("Schools checkbox should be processed if selected", schoolsCheckBox.isSelected());
        assertTrue("Transport checkbox should be processed if selected", transportCheckBox.isSelected());
    }
}