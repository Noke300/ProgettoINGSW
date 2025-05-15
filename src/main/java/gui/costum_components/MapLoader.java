package gui.costum_components;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.VirtualEarthTileFactoryInfo;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.viewer.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class MapLoader {
    private static final String GEOAPIFY_API_KEY = "8f5bca3082a24dadb72c4f3546cacd4c";
    private static final String NOMINATIM_URL = "https://nominatim.openstreetmap.org/search";

    public MapLoader() { /* TODO document why this constructor is empty */ }

    public static void createMapPanel(GeoPosition geoPosition, @Nullable List<JCheckBox> checkBoxesList, JPanel MapFrame) {
        // Barra di ricerca combinata
        IconClass icon = new IconClass();
        // Interactive Map with Markers
        JXMapViewer mapViewer = new JXMapViewer();

        TileFactoryInfo info =  new VirtualEarthTileFactoryInfo(VirtualEarthTileFactoryInfo.MAP);
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        mapViewer.setTileFactory(tileFactory);

        // Initial Position
        mapViewer.setCenterPosition(new GeoPosition(geoPosition.getLatitude(), geoPosition.getLongitude())); // Indirizzi da address
        mapViewer.setZoom(5);

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

            if (rotation > 0 && zoom <= 12) {
                // Zoom in
                mapViewer.setZoom(zoom + 1);
            } else if (zoom > 0) {
                // Zoom out
                mapViewer.setZoom(zoom - 1);
            }
        });
        // Add a map to the center of the frame
        MapFrame.add(mapViewer, BorderLayout.CENTER);
        MapFrame.revalidate();
        MapFrame.repaint();

        // Add waypoint click listener
        waypoints.clear();
        waypoints.add(new CustomWaypoint(geoPosition, "Indirizzo",icon.getMapImmobiliIcon()));
        waypointPainter.setWaypoints(waypoints);
        mapViewer.setCenterPosition(geoPosition);
        mapViewer.repaint();
        mapViewer.setZoom(6);

        //inserimento Filtro non toccare so che è sbagliato 0>1
        for(JCheckBox box : checkBoxesList) {

            if(box.getText().equals("Scuole") && box.isSelected()) {
                // Fetch places using Geoapify Places API for schools
                List<GeoPosition> places = fetchPlaces(geoPosition, "education.school");
                for (GeoPosition place : places) {
                    waypoints.add(new CustomWaypoint(place, "Scuola",icon.getMapImmobiliIcon()));
                }
                waypointPainter.setWaypoints(waypoints);
                mapViewer.repaint();
            }else if(box.getText().equals("Trasporti") && box.isSelected()) {
                List<GeoPosition> places = fetchPlaces(geoPosition, "public_transport");
                for (GeoPosition place : places) {
                    waypoints.add(new CustomWaypoint(place,"Transport",icon.getMapImmobiliIcon()));
                }
                waypointPainter.setWaypoints(waypoints);
                mapViewer.repaint();
            }else if(box.getText().equals("Parchi") && box.isSelected()) {
                List<GeoPosition> places = fetchPlaces(geoPosition, "leisure.park");
                for (GeoPosition place : places) {
                    waypoints.add(new CustomWaypoint(place, "Parco",icon.getMapImmobiliIcon()));
                }
                waypointPainter.setWaypoints(waypoints);
                mapViewer.repaint();

            }
        }

        final int HITBOX_RADIUS = 25;

        // Creiamo il pannello laterale per le informazioni
        final JPanel detailsPanel = new JPanel(new BorderLayout());
        detailsPanel.setPreferredSize(new Dimension(400, 600));  // dimensioni personalizzabili
        final JTextArea detailsTextArea = new JTextArea();
        detailsTextArea.setEditable(false);
        detailsTextArea.setLineWrap(true);
        detailsTextArea.setWrapStyleWord(true);
        detailsTextArea.setFont(new Font("Arial", Font.BOLD, 24));
        detailsPanel.add(new JScrollPane(detailsTextArea), BorderLayout.CENTER);

// Inizialmente il pannello non è visibile (oppure lo lasciamo vuoto)
        detailsPanel.setVisible(false);

// Aggiungiamo il pannello al frame (al lato destro)
        MapFrame.add(detailsPanel, BorderLayout.WEST);


// Listener per cambiare il cursore quando il mouse passa sopra un marker
        mapViewer.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                Point mousePos = e.getPoint();
                Rectangle viewport = mapViewer.getViewportBounds();
                boolean hovering = false;
                for (CustomWaypoint waypoint : waypoints) {
                    Point2D markerPoint = calcoloPosizione(mapViewer,waypoint,viewport);
                    if (mousePos.distance(markerPoint) < HITBOX_RADIUS-10) {
                        hovering = true;
                        break;
                    }
                }
                mapViewer.setCursor(hovering ? Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
                        : Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        });

// Se il click avviene vicino a un marker, viene mostrato il popup con le info.
// Se non è su un marker, la mappa viene centrata sul punto cliccato.
        mapViewer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point clickedPoint = e.getPoint();
                Rectangle viewport = mapViewer.getViewportBounds();
                boolean markerFound = false;

                // Scorri tutti i waypoint per vedere se il click è vicino aduno di essi
                for (CustomWaypoint waypoint : waypoints) {

                    Point2D markerPoint = calcoloPosizione(mapViewer,waypoint,viewport);
                    if (clickedPoint.distance(markerPoint) < HITBOX_RADIUS+7) {
                        markerFound = true;
                        // Aggiorna il pannello con le informazioni del waypoint
                        detailsTextArea.setText("Informazioni: " + waypoint.getInfo() + "\n" +
                                "Indirizzo: " + getAddressFromCoordinates(waypoint.getPosition()));
                        detailsPanel.setVisible(true);  // Rendi visibile il pannello
                        break;
                    }
                }

                if (!markerFound) {
                    // Se il click non è su un marker, nascondi o svuota il pannello
                    detailsTextArea.setText("");
                    detailsPanel.setVisible(false);
                }
            }
        });
    }

    public static Point2D calcoloPosizione(JXMapViewer mapViewer,Waypoint waypoint, Rectangle viewport){
        GeoPosition wpGeo = waypoint.getPosition();
        // Ottieni le coordinate assolute del waypoint sulla mappa
        Point2D wpPixelAbs = mapViewer.getTileFactory().geoToPixel(wpGeo, mapViewer.getZoom());
        // Calcola la posizione relativa rispetto al viewport
        double markerX = wpPixelAbs.getX() - viewport.x;
        double markerY = wpPixelAbs.getY() - viewport.y;
        return new Point2D.Double(markerX, markerY);
    }


    public static String getAddressFromCoordinates(GeoPosition g) {
        OkHttpClient client = new OkHttpClient();
        // Costruisco URL per la richiesta di reverse geocoding a Nominatim
        String url = String.format(Locale.US,"https://nominatim.openstreetmap.org/reverse?format=json&lat=%f&lon=%f&addressdetails=1", g.getLatitude(), g.getLongitude());

        Request request = new Request.Builder()
                .url(url)
                .header("User-Agent", "Mozilla/5.0 (Java) ReverseGeocoder")  // opzionale, ma raccomandato
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.body() != null) {
                String jsonData = response.body().string();
                JSONParser parser = new JSONParser();
                JSONObject jsonObject = (JSONObject) parser.parse(jsonData);
                // "display_name" contiene l'indirizzo formattato
                return (String) jsonObject.get("display_name");
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static GeoPosition getCoordinatesFromAddress(String address) {
        OkHttpClient client = new OkHttpClient();
        String url = String.format("%s?q=%s&format=json&limit=1&accept-language=it", NOMINATIM_URL, address.replace(" ", "+"));
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Accept-Language", "it")
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

                    // Check if coordinates are within the desired area
                    if (isWithinTargetArea(lat, lon)) {
                        return new GeoPosition(lat, lon);
                    }
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
    private static boolean isWithinTargetArea(double lat, double lon) {
        // Define your target area boundaries here
        // For example, if targeting Italy:
        double minLat = 36.0; // Southern boundary of Italy
        double maxLat = 47.5; // Northern boundary of Italy
        double minLon = 6.5;  // Western boundary of Italy
        double maxLon = 18.5; // Eastern boundary of Italy

        return lat >= minLat && lat <= maxLat && lon >= minLon && lon <= maxLon;
    }

    //scan dei posti
    private static List<GeoPosition> fetchPlaces(GeoPosition g, String category) {
        List<GeoPosition> places = new ArrayList<>();
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        String url = String.format(Locale.US,"https://api.geoapify.com/v2/places?categories=%s&filter=circle:%f,%f,5000&bias=proximity:%f,%f&limit=15&apiKey=%s",
                category, g.getLongitude(), g.getLatitude(), g.getLongitude(), g.getLatitude(), GEOAPIFY_API_KEY);

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

                for (Object o : features) {
                    JSONObject feature = (JSONObject) o;
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
