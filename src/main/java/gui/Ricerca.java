package gui;

import com.formdev.flatlaf.FlatDarculaLaf;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jxmapviewer.viewer.GeoPosition;
import org.kordamp.ikonli.fontawesome.FontAwesome;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.metal.MetalButtonUI;
import javax.swing.plaf.metal.MetalCheckBoxIcon;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Ricerca {

    private static final String GEOAPIFY_API_KEY = "df7445014a484278a655999c41bc28c5";
    private static final String NOMINATIM_URL = "https://nominatim.openstreetmap.org/search";

    public JPanel createRicercaPanel(JFrame frame) {
        // Utilizziamo un pannello principale con BoxLayout verticale per centrare i componenti
        JPanel mainPanel = new JPanel();

        // Imposta uno sfondo o un bordo per evidenziare il pannello


        // --- 1. Barra di ricerca ---
        JTextField searchField = new JTextField(90);
        JButton searchButton = new JButton("Cerca");
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchPanel.add(new JLabel("Indirizzo:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        // Impostiamo l'allineamento orizzontale per il pannello

        // --- 2. Pannello dei filtri ---
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        filterPanel.add(new JLabel("Filtri:"));
        JCheckBox schoolsCheckBox = new JCheckBox("Scuole");
        JCheckBox hospitalsCheckBox = new JCheckBox("Trasporti");
        JCheckBox barsCheckBox = new JCheckBox("Parchi");

        List<JCheckBox> checkBoxesList = new ArrayList<>();
        checkBoxesList.add(schoolsCheckBox);
        checkBoxesList.add(hospitalsCheckBox);
        checkBoxesList.add(barsCheckBox);

        filterPanel.add(schoolsCheckBox);
        filterPanel.add(hospitalsCheckBox);
        filterPanel.add(barsCheckBox);

        // --- 3. Popup per i suggerimenti ---
        JPopupMenu suggestionPopup = new JPopupMenu();
        JLabel loadingLabel = new JLabel("Caricamento...");
        loadingLabel.setVisible(false);
        // Inseriamo l'etichetta di caricamento nel pannello della ricerca (puoi spostarla se preferisci)
        searchPanel.add(loadingLabel);

        // --- 4. Auto-completamento ---
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            private static final int TIMER_DELAY_MS = 300;

            private Timer initializeTimer() {
                Timer timer = new Timer(TIMER_DELAY_MS, e -> handleSuggestionInput());
                timer.setRepeats(false);
                return timer;
            }

            private final Timer timer = initializeTimer();

            @Override
            public void insertUpdate(DocumentEvent e) {
                timer.restart();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                timer.restart();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                timer.restart();
            }
            private void handleSuggestionInput() {
                loadingLabel.setVisible(true);
                SwingUtilities.invokeLater(() -> {
                    String inputText = searchField.getText().trim();
                    if (inputText.isEmpty()) {
                        hideSuggestions();
                        return;
                    }

                    List<String> suggestions = fetchAutocompleteSuggestions(inputText);
                    if (suggestions.isEmpty()) {
                        hideSuggestions();
                        return;
                    }

                    populateSuggestions(suggestions);
                    displaySuggestionPopup();
                    loadingLabel.setVisible(false);
                });
            }

            private void hideSuggestions() {
                suggestionPopup.setVisible(false);
                loadingLabel.setVisible(false);
            }

            private void populateSuggestions(List<String> suggestions) {
                suggestionPopup.removeAll();
                for (String suggestion : suggestions) {
                    JMenuItem item = new JMenuItem(suggestion);
                    item.addActionListener((ActionEvent ev) -> {
                        searchField.setText(suggestion);
                        suggestionPopup.setVisible(false);
                    });
                    suggestionPopup.add(item);
                }
            }

            private void displaySuggestionPopup() {
                try {
                    Rectangle2D caretCoords2D = searchField.modelToView2D(searchField.getCaretPosition());
                    if (caretCoords2D != null) {
                        Rectangle caretCoords = new Rectangle((int) caretCoords2D.getX(), (int) caretCoords2D.getY(),
                                (int) caretCoords2D.getWidth(), (int) caretCoords2D.getHeight());
                        Point popupLocation = new Point(caretCoords.x, caretCoords.y + caretCoords.height);
                        SwingUtilities.convertPointToScreen(popupLocation, searchField);
                        suggestionPopup.setLocation(popupLocation);
                        suggestionPopup.setInvoker(searchField);
                        suggestionPopup.setVisible(true);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        // --- 5. Azione del pulsante "Cerca" ---
        searchButton.addActionListener(e -> {
            GeoPosition geoPos = getCoordinatesFromAddress(searchField.getText());
            if (geoPos != null) {
                // Passa a un altro pannello, ad esempio una mappa (ipotesi: esiste MapPanel)
                frame.setContentPane(new MapPanel().createMapPanel(frame, geoPos, checkBoxesList));
                frame.revalidate();
                frame.repaint();
            } else {
                JOptionPane.showMessageDialog(mainPanel, "Indirizzo non valido.");
            }
        });

        // --- 6. Pulsante "Torna alla Home" ---

        FontIcon homeIcon = FontIcon.of(FontAwesome.HOME, 24); // 24px di dimensione

        JButton backButtonHome = new JButton();
        backButtonHome.setIcon(homeIcon);


        backButtonHome.addActionListener(e -> {
            // Supponiamo che la classe Home abbia il metodo createHomePanel
            frame.setContentPane(new Home().createHomePanel(frame));
            frame.revalidate();
            frame.repaint();
        });

        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

// Bottone "Ritorna alla Home" in alto a sinistra
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0; // Il bottone non si espande orizzontalmente
        gbc.weighty = 0; // Il bottone non si espande verticalmente
        gbc.anchor = GridBagConstraints.FIRST_LINE_START; // Posizionato in alto a sinistra
        gbc.insets = new Insets(10, 10, 0, 0); // Margine attorno al bottone
        gbc.fill = GridBagConstraints.NONE; // Nessuna espansione del bottone
        mainPanel.add(backButtonHome, gbc);

// Pannello di ricerca (searchPanel) in alto, si espande orizzontalmente e leggermente in verticale
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.8; // Si espande orizzontalmente
        gbc.weighty = 0.1; // Occupa il 30% dello spazio verticale totale
        gbc.fill = GridBagConstraints.BOTH; // Espandibile in entrambe le direzioni
        gbc.insets = new Insets(400, 10, 0, 10); // Margini intorno al pannello
        mainPanel.add(searchPanel, gbc);

// Pannello di filtro (filterPanel) al centro, si espande completamente
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1; // Si espande orizzontalmente
        gbc.weighty = 1; // Occupa il resto dello spazio verticale (70%)
        gbc.fill = GridBagConstraints.BOTH; // Espandibile in entrambe le direzioni
        gbc.insets = new Insets(1, 30, 0, 30); // Margini
        mainPanel.add(filterPanel, gbc);
        return mainPanel;
    }

    private static GeoPosition getCoordinatesFromAddress(String address) {
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
                    String displayName = jsonObject.get("display_name").toString();
                    if (displayName.contains("Italia") || displayName.contains("Italy") || displayName.contains("Ita")) {
                        return new GeoPosition(lat, lon);
                    }
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static List<String> fetchAutocompleteSuggestions(String input) {
        List<String> results = new ArrayList<>();
        Set<String> uniqueLocations = new HashSet<>(); // Usa un Set per rimuovere duplicati

        OkHttpClient client = new OkHttpClient();
        String url = String.format("https://api.geoapify.com/v1/geocode/autocomplete?text=%s&limit=10&lang=it&filter=countrycode:it&apiKey=%s",
                input, GEOAPIFY_API_KEY);

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.body() != null) {
                String jsonData = response.body().string();
                JSONParser parser = new JSONParser();
                JSONObject jsonObject = (JSONObject) parser.parse(jsonData);
                JSONArray features = (JSONArray) jsonObject.get("features");

                for (Object o : features) {
                    JSONObject feature = (JSONObject) o;
                    JSONObject properties = (JSONObject) feature.get("properties");
                    String formatted = (String) properties.get("formatted");

                    // Normalizza il nome rimuovendo "Italia" o "Italy" dalla fine
                    String normalized = formatted.replaceAll(",?\\s*(Italia|Italy)\\s*$", "").trim();

                    if (uniqueLocations.add(normalized)) {
                        results.add(formatted); // Mantiene il formato originale
                    }
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return results;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FlatDarculaLaf.setup();
            JFrame frame = new JFrame("Ricerca con AutoCompletamento");
            frame.setContentPane(new Ricerca().createRicercaPanel(frame));
            // Imposta una dimensione fissa o usa pack() in base alle tue esigenze
            frame.setSize(800, 600);
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
