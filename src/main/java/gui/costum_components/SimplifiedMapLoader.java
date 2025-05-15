package gui.costum_components;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.VirtualEarthTileFactoryInfo;
import org.jxmapviewer.input.PanMouseInputListener;
import org.jxmapviewer.input.ZoomMouseWheelListenerCursor;
import org.jxmapviewer.viewer.*;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

public class SimplifiedMapLoader {

    private static final FontIcon WAYPOINT_FONT_ICON = new IconClass().getMapImmobiliIcon();

    public static void createClickableMap(
            @NotNull JPanel mapContainerPanel,
            @NotNull GeoPosition initialCenter,
            int initialZoom,
            @Nullable Consumer<AddressSelectionEvent> onAddressSelected) {

        JXMapViewer mapViewer = new JXMapViewer();

        TileFactoryInfo info = new VirtualEarthTileFactoryInfo(VirtualEarthTileFactoryInfo.MAP);
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        mapViewer.setTileFactory(tileFactory);

        mapViewer.setZoom(initialZoom);
        mapViewer.setAddressLocation(initialCenter);

        // Set up pan and zoom listeners
        PanMouseInputListener panListener = new PanMouseInputListener(mapViewer);
        mapViewer.addMouseListener(panListener);
        mapViewer.addMouseMotionListener(panListener);
        mapViewer.addMouseWheelListener(new ZoomMouseWheelListenerCursor(mapViewer));

        // Initialize the set of waypoints and the custom renderer
        final Set<CustomWaypoint> waypoints = new HashSet<>();
        CustomWaypointRenderer waypointRenderer = new CustomWaypointRenderer();

        // Configure the waypoint painter with the custom renderer
        WaypointPainter<CustomWaypoint> waypointPainter = new WaypointPainter<>();
        waypointPainter.setRenderer(waypointRenderer);
        waypointPainter.setWaypoints(waypoints);

        // Add the waypoint painter to the map
        mapViewer.setOverlayPainter(waypointPainter);

        // Add double-click listener for waypoint creation
        mapViewer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
                    Point clickPoint = e.getPoint();
                    final GeoPosition clickedGeoPosition = mapViewer.convertPointToGeoPosition(clickPoint);

                    // Clear existing waypoints for visual feedback
                    waypoints.clear();

                    // Add a new waypoint immediately for visual feedback
                    CustomWaypoint waypoint = new CustomWaypoint(
                            clickedGeoPosition,
                            "Indirizzo", // This will be used by the renderer
                            WAYPOINT_FONT_ICON // Use the FontIcon directly
                    );
                    waypoints.add(waypoint);

                    // Repaint to show the waypoint immediately
                    mapViewer.repaint();

                    // Start the address lookup in the background
                    new SwingWorker<String, Void>() {
                        @Override
                        protected String doInBackground() throws Exception {
                            return getAddressFromCoordinates(clickedGeoPosition);
                        }

                        @Override
                        protected void done() {
                            String address = null;
                            try {
                                address = get();

                                // The waypoint is already added, but we might update its info
                                // if needed based on the address result

                                if (address == null) {
                                    System.out.println("Indirizzo non trovato per le coordinate, ma il waypoint è stato aggiunto.");
                                }
                            } catch (InterruptedException ex) {
                                Thread.currentThread().interrupt();
                                System.err.println("Recupero indirizzo interrotto: " + ex.getMessage());
                            } catch (ExecutionException ex) {
                                System.err.println("Errore durante l'esecuzione del recupero indirizzo: " + ex.getCause().getMessage());
                                ex.getCause().printStackTrace();
                            } catch (Exception ex) {
                                System.err.println("Errore imprevisto nel worker del recupero indirizzo: " + ex.getMessage());
                                ex.printStackTrace();
                            } finally {
                                // Ensure the callback is called with whatever address we found
                                if (onAddressSelected != null) {
                                    onAddressSelected.accept(new AddressSelectionEvent(address, clickedGeoPosition));
                                }
                            }
                        }
                    }.execute();
                }
            }
        });

        // Set up the map in the container panel
        mapContainerPanel.setLayout(new BorderLayout());
        mapContainerPanel.removeAll();
        mapContainerPanel.add(mapViewer, BorderLayout.CENTER);
        mapContainerPanel.revalidate();
        mapContainerPanel.repaint();
    }

    public static class AddressSelectionEvent {
        private final String address;
        private final GeoPosition geoPosition;

        public AddressSelectionEvent(String address, GeoPosition geoPosition) {
            this.address = address;
            this.geoPosition = geoPosition;
        }

        public String getAddress() { return address; }
        public GeoPosition getGeoPosition() { return geoPosition; }
    }

    @Nullable
    public static String getAddressFromCoordinates(@NotNull GeoPosition g) {
        OkHttpClient client = new OkHttpClient();
        // Assicurati che User-Agent sia conforme alle policy di Nominatim
        String userAgent = "MySwingApp/1.0 (myemail@example.com)"; // Sostituisci con un contatto reale se possibile
        String url = String.format(Locale.US, "https://nominatim.openstreetmap.org/reverse?format=json&lat=%f&lon=%f&addressdetails=1&accept-language=it",
                g.getLatitude(), g.getLongitude());
        Request request = new Request.Builder()
                .url(url)
                .header("User-Agent", userAgent)
                .get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String jsonData = response.body().string();
                JSONParser parser = new JSONParser();
                JSONObject jsonObject = (JSONObject) parser.parse(jsonData);
                if (jsonObject.containsKey("error")) {
                    System.err.println("Errore da Nominatim API: " + jsonObject.get("error"));
                    return null;
                }
                return (String) jsonObject.get("display_name");
            } else {
                System.err.println("Errore Nominatim HTTP: " + response.code() + " " + response.message());
                if(response.body() != null) {
                    System.err.println("Corpo della risposta: " + response.body().string());
                }
            }
        } catch (IOException | ParseException e) {
            System.err.println("Eccezione durante il reverse geocoding: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}