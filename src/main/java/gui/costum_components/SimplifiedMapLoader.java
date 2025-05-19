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

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

public class SimplifiedMapLoader {

    // Get the waypoint icon from IconClass
    private static final IconClass ICON_CLASS = new IconClass();

    public static void createClickableMap(
            @NotNull JPanel mapContainerPanel,
            @NotNull GeoPosition initialCenter,
            int initialZoom,
            @Nullable Consumer<AddressSelectionEvent> onAddressSelected) {

        // Create the JXMapViewer
        JXMapViewer mapViewer = new JXMapViewer();

        // Create and set the tile factory
        TileFactoryInfo info = new VirtualEarthTileFactoryInfo(VirtualEarthTileFactoryInfo.MAP);
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        mapViewer.setTileFactory(tileFactory);

        // Set the initial position and zoom level
        mapViewer.setZoom(initialZoom);
        mapViewer.setAddressLocation(initialCenter);

        // Set up pan and zoom listeners
        PanMouseInputListener panListener = new PanMouseInputListener(mapViewer);
        mapViewer.addMouseListener(panListener);
        mapViewer.addMouseMotionListener(panListener);
        mapViewer.addMouseWheelListener(e -> {
            int rotation = e.getWheelRotation();
            int zoom = mapViewer.getZoom();

            if (rotation > 0 && zoom <= 10) {
                // Zoom in
                mapViewer.setZoom(zoom + 1);
            } else if (zoom > 0) {
                // Zoom out
                mapViewer.setZoom(zoom - 1);
            }
        });

        // Create shared objects for waypoint handling
        final Set<CustomWaypoint> waypoints = new HashSet<>();
        final CustomWaypointRenderer waypointRenderer = new CustomWaypointRenderer();
        final WaypointPainter<CustomWaypoint> waypointPainter = new WaypointPainter<>();

        // Configure the waypoint painter
        waypointPainter.setRenderer(waypointRenderer);
        waypointPainter.setWaypoints(waypoints);

        // Set the overlay painter
        mapViewer.setOverlayPainter(waypointPainter);

        System.out.println("Map initialized with waypoint painter");

        // Add a double-click listener for waypoint creation
        mapViewer.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
                    Point clickPoint = e.getPoint();
                    final GeoPosition clickedGeoPosition = mapViewer.convertPointToGeoPosition(clickPoint);

                    // Clear existing waypoints
                    waypoints.clear();

                    // Create a new waypoint
                    CustomWaypoint waypoint = new CustomWaypoint(
                            clickedGeoPosition,
                            "Indirizzo", // Placeholder info
                            ICON_CLASS.getCursoreMapSelect() // Get the icon from your IconClass
                    );

                    // Add the waypoint to the set
                    waypoints.add(waypoint);

                    // CRITICAL: Update the waypoint painter with the modified set
                    waypointPainter.setWaypoints(waypoints);

                    // Force repaint to show the waypoint immediately
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
                                if(!(address==null) && !address.toLowerCase().contains("italia")){
                                    address=null;
                                }
                                System.out.println("Address lookup result: " + (address != null ? address : "Indirizzo non valido o fuori confine Italiano"));
                            } catch (InterruptedException ex) {
                                Thread.currentThread().interrupt();
                                System.err.println("Address lookup interrupted: " + ex.getMessage());
                            } catch (ExecutionException ex) {
                                System.err.println("Error during address lookup: " + ex.getCause().getMessage());
                                ex.getCause().printStackTrace();
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
        String userAgent = "DieteEstate/1.0 (marco.scotto2002@gmail.com)"; // Update with your real contact info
        String url = String.format(Locale.US,
                "https://nominatim.openstreetmap.org/reverse?format=json&lat=%f&lon=%f&addressdetails=1&accept-language=it",
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
                    System.err.println("Error from Nominatim API: " + jsonObject.get("error"));
                    return null;
                }
                return (String) jsonObject.get("display_name");
            } else {
                System.err.println("Nominatim HTTP Error: " + response.code() + " " + response.message());
                if (response.body() != null) {
                    System.err.println("Response body: " + response.body().string());
                }
            }
        } catch (IOException | ParseException e) {
            System.err.println("Exception during reverse geocoding: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}