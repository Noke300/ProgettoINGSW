package gui;

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
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Ricerca {

    private static final String GEOAPIFY_API_KEY = "df7445014a484278a655999c41bc28c5";
    private static final String NOMINATIM_URL = "https://nominatim.openstreetmap.org/search";
    private JPanel mainPanelRicerca;
    private JButton backButtonHome;
    private JTextField searchField;
    private JButton searchButton;
    private JCheckBox schoolsCheckBox;
    private JCheckBox hospitalsCheckBox;
    private JCheckBox barsCheckBox;
    private JPopupMenu popupMenu;

    public Ricerca() {
        initializePopupMenu(); // Inizializza il menu
        addPopupToSearchField(); // Collega il menu al searchField
    }



    public JPanel createRicercaPanel(JFrame frame) {

        List<JCheckBox> checkBoxesList = new ArrayList<>();
        checkBoxesList.add(schoolsCheckBox);
        checkBoxesList.add(hospitalsCheckBox);
        checkBoxesList.add(barsCheckBox);


        // --- 3. Popup per i suggerimenti ---
        JPopupMenu suggestionPopup = new JPopupMenu();

        // --- 4. Auto-completamento ---
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            private static final int TIMER_DELAY_MS = 700;

            private Timer initializeTimer() {
                Timer timer2;
                timer2 = new Timer(TIMER_DELAY_MS, e -> handleSuggestionInput());
                timer2.setRepeats(false);
                return timer2;
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

                });
            }

            private void hideSuggestions() {
                suggestionPopup.setVisible(false);

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
                JOptionPane.showMessageDialog(mainPanelRicerca, "Indirizzo non valido.");
            }
        });

        // --- 6. Pulsante "Torna alla Home" ---

        FontIcon homeIcon = FontIcon.of(FontAwesome.HOME, 24); // 24px di dimensione
        backButtonHome.setIcon(homeIcon);


        backButtonHome.addActionListener(e -> {
            // Supponiamo che la classe Home abbia il metodo createHomePanel
            frame.setContentPane(new Home().createHomePanel(frame));
            frame.revalidate();
            frame.repaint();
        });

        return mainPanelRicerca;
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
                    return new GeoPosition(lat, lon);
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
        // Creazione della finestra per testare l'interfaccia
        JFrame frame = new JFrame("Ricerca");
        Ricerca ricerca = new Ricerca();
        frame.setContentPane(ricerca.createRicercaPanel(frame));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }


    private void initializePopupMenu() {
        // Crea un nuovo JPopupMenu
        popupMenu = new JPopupMenu();

        // Aggiungi un'opzione per pulire il campo di testo
        JMenuItem clearFieldItem = new JMenuItem("Clear Field");
        clearFieldItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchField.setText(""); // Pulisce il campo quando selezionato
            }
        });

        // Aggiungi un'opzione per avviare la ricerca
        JMenuItem searchItem = new JMenuItem("Search");
        searchItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Search triggered for: " + searchField.getText());
            }
        });

        // Aggiungi le opzioni al menu
        popupMenu.add(clearFieldItem);
        popupMenu.add(searchItem);
    }

    private void addPopupToSearchField() {
        // Aggiungi un MouseListener al campo di testo per mostrare il popup
        searchField.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                showPopupMenu(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                showPopupMenu(e);
            }

            private void showPopupMenu(MouseEvent e) {
                if (e.isPopupTrigger()) { // Controlla se è un click destro
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }


}
