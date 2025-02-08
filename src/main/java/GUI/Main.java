package GUI;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.VirtualEarthTileFactoryInfo;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.viewer.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {

    private double latitudine = 40.8518;
    private double longitudine = 14.2681;
    private static final String GEOAPIFY_API_KEY = "df7445014a484278a655999c41bc28c5"; // Sostituisci con la tua chiave API

    public void setLatitudine(double latitudine) {
        this.latitudine = latitudine;
    }

    public void setLongitudine(double longitudine) {
        this.longitudine = longitudine;
    }

    private static final String NOMINATIM_URL = "https://nominatim.openstreetmap.org/search";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::createAndShowGUI);
    }

    static void createAndShowGUI() {
        Main M = new Main();

        JFrame frame = new JFrame("Google Places Autocomplete with Interactive Map");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        // Barra di ricerca combinata
        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.setEditable(true);
        JTextField textField = (JTextField) comboBox.getEditor().getEditorComponent();
        textField.setColumns(60);
        JButton searchButton = new JButton("Cerca");
        JButton FilterButton = new JButton("Filtra");

        FilterButton.addActionListener(e -> {
            String input = textField.getText();
            if (!input.isEmpty()) {
                List<String> suggestions = fetchAutocompleteSuggestions(input);
                comboBox.removeAllItems();
                for (String suggestion : suggestions) {
                    comboBox.addItem(suggestion);
                }
                comboBox.showPopup();
            }
        });

        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("Indirizzo:"));
        searchPanel.add(comboBox);
        searchPanel.add(searchButton);
        searchPanel.add(FilterButton);
        frame.add(searchPanel, BorderLayout.NORTH);

        // Interactive Map with Markers
        JXMapViewer mapViewer = new JXMapViewer();

        JComboBox<String> comboBoxMap = new JComboBox<>();

        comboBoxMap.setVisible(true);
        comboBoxMap.setEditable(true);
        comboBoxMap.addItem("Default");
        comboBoxMap.addItem("MAP");
        comboBoxMap.addItem("HYBRID");
        comboBoxMap.addItem("SATELLITE");

        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        mapViewer.setTileFactory(tileFactory);

        comboBoxMap.addActionListener(e -> {
            int index = comboBoxMap.getSelectedIndex();
            TileFactoryInfo info2 = null;
            if (index == 0) {
                info2 = new OSMTileFactoryInfo();
            } else if (index == 1) {
                info2 = new VirtualEarthTileFactoryInfo(VirtualEarthTileFactoryInfo.MAP);

            } else if (index == 2) {

                info2 = new VirtualEarthTileFactoryInfo(VirtualEarthTileFactoryInfo.HYBRID);

            } else if (index == 3) {
                info2 = new VirtualEarthTileFactoryInfo(VirtualEarthTileFactoryInfo.SATELLITE);

            }

            DefaultTileFactory tileFactory2 = new DefaultTileFactory(info2);
            mapViewer.setTileFactory(tileFactory2);
        });

        // Initial Position
        mapViewer.setCenterPosition(new GeoPosition(M.latitudine, M.longitudine)); // Napoli, Italia
        mapViewer.setZoom(5);

        searchPanel.add(comboBoxMap);

        class CustomWaypoint extends DefaultWaypoint {
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

        class CustomWaypointRenderer implements WaypointRenderer<CustomWaypoint> {
            @Override
            public void paintWaypoint(Graphics2D g, JXMapViewer map, CustomWaypoint waypoint) {
                g.setColor(waypoint.getColor());
                Point2D point = map.getTileFactory().geoToPixel(waypoint.getPosition(), map.getZoom());
                g.fillOval((int) point.getX() - 5, (int) point.getY() - 5, 10, 10);

            }
        }


        // List of markers
        Set<CustomWaypoint> waypoints = new HashSet<>();
        WaypointPainter<CustomWaypoint> waypointPainter = new WaypointPainter<>();
        waypointPainter.setRenderer(new CustomWaypointRenderer());
        waypointPainter.setWaypoints(waypoints);
        mapViewer.setOverlayPainter(waypointPainter);

        // Enable panning with the mouse
        PanMouseInputListener panListener = new PanMouseInputListener(mapViewer);
        mapViewer.addMouseListener(panListener);
        mapViewer.addMouseMotionListener(panListener);

        // Enable zoom with the mouse wheel
        mapViewer.addMouseWheelListener(e -> {
            int rotation = e.getWheelRotation();
            int zoom = mapViewer.getZoom();

            if (rotation > 0 && zoom <= 13) {
                // Zoom in
                mapViewer.setZoom(zoom + 1);
            } else if (zoom > 0) {
                // Zoom out
                mapViewer.setZoom(zoom - 1);
            }
        });

        // Add map to the center of the frame
        frame.add(mapViewer, BorderLayout.CENTER);

        // Center the map on mouse click
        // Center the map on mouse click
        mapViewer.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                Point2D worldPos = e.getPoint();
                GeoPosition geo = mapViewer.convertPointToGeoPosition(worldPos);
                mapViewer.setCenterPosition(geo);
            }
        });

        // Add waypoint click listener


        searchButton.addActionListener(e -> {
            String address = textField.getText();
            if (!address.isEmpty()) {
                GeoPosition geoPosition = getCoordinatesFromAddress(address);
                if (geoPosition != null) {
                    waypoints.clear();
                    waypoints.add(new CustomWaypoint(geoPosition, Color.BLUE,"Cento"));
                    waypointPainter.setWaypoints(waypoints);
                    mapViewer.setCenterPosition(geoPosition);
                    mapViewer.repaint();
                    mapViewer.setZoom(6);

                    // Fetch places using Geoapify Places API for schools
                    List<GeoPosition> places = fetchPlaces(geoPosition.getLatitude(), geoPosition.getLongitude(), "education.school");
                    for (GeoPosition place : places) {
                        waypoints.add(new CustomWaypoint(place, Color.RED,"Scuola"));
                    }
                    waypointPainter.setWaypoints(waypoints);
                    mapViewer.repaint();
                } else {
                    JOptionPane.showMessageDialog(frame, "Indirizzo non Valido.");
                }
            }
        });

        mapViewer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point2D mousePos = e.getPoint();
                for (CustomWaypoint waypoint : waypoints) {
                    Point2D point = mapViewer.getTileFactory().geoToPixel(waypoint.getPosition(), mapViewer.getZoom());
                    if (point.distance(mousePos) < 5) {
                        EventQueue.invokeLater(() -> JOptionPane.showMessageDialog(mapViewer, waypoint.getInfo()));
                        break;
                    }
                }
            }
        });

        frame.setVisible(true);
    }
    // La funzione fetchAutocompleteSuggestions rimane invariata
    private static List<String> fetchAutocompleteSuggestions(String input) {
        List<String> results = new ArrayList<>();
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder()
                .url("https://api.geoapify.com/v1/geocode/autocomplete?text=" + input + "&limit=10&apiKey=" + GEOAPIFY_API_KEY)
                .method("GET", null)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.body() != null) {
                String jsonData = response.body().string();
                JSONParser parser = new JSONParser();
                JSONObject jsonObject = (JSONObject) parser.parse(jsonData);
                JSONArray features = (JSONArray) jsonObject.get("features");
                for (int i = 0; i < features.size(); i++) {
                    JSONObject feature = (JSONObject) features.get(i);

                    JSONObject properties = (JSONObject) feature.get("properties");
                    results.add((String) properties.get("formatted"));
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return results;
    }

    private static GeoPosition getCoordinatesFromAddress(String address) {

        OkHttpClient client = new OkHttpClient();
        String url = String.format("%s?q=%s&format=json&limit=1", NOMINATIM_URL, address.replace(" ", "+"));

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.body() != null) {
                String jsonData = response.body().string();
                JSONParser parser = new JSONParser();
                JSONArray jsonArray = (JSONArray) parser.parse(jsonData);

                if (!jsonArray.isEmpty()) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(0);
                    double lat = Double.parseDouble((String) jsonObject.get("lat"));
                    double lon = Double.parseDouble((String) jsonObject.get("lon"));
                    String stato = jsonObject.get("display_name").toString();
                    if (stato.contains("Italia") || stato.contains("Italy") || stato.contains("Ita")) {
                        return new GeoPosition(lat, lon);
                    }
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    //scan dei posti
    private static List<GeoPosition> fetchPlaces(double latitudine, double longitudine, String category) {
        List<GeoPosition> places = new ArrayList<>();
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        String url = String.format("https://api.geoapify.com/v2/places?categories=%s&filter=circle:%f,%f,5000&bias=proximity:%f,%f&limit=30&apiKey=%s",
                category, longitudine, latitudine, longitudine, latitudine, GEOAPIFY_API_KEY);

        Request request = new Request.Builder()
                .url(url)
                .method("GET", null)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.body() != null) {
                String jsonData = response.body().string();
                JSONParser parser = new JSONParser();
                JSONObject jsonObject = (JSONObject) parser.parse(jsonData);
                JSONArray features = (JSONArray) jsonObject.get("features");

                for (int i = 0; i < features.size(); i++) {
                    JSONObject feature = (JSONObject) features.get(i);
                    JSONObject geometry = (JSONObject) feature.get("geometry");
                    JSONArray coordinates = (JSONArray) geometry.get("coordinates");
                    double lon = (double) coordinates.get(0);
                    double lat = (double) coordinates.get(1);
                    places.add(new GeoPosition(lat, lon));
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return places;
    }

}
