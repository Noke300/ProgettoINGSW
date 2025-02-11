package gui;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jxmapviewer.viewer.GeoPosition;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Ricerca {

    private static final String GEOAPIFY_API_KEY = "df7445014a484278a655999c41bc28c5";
    private static final String NOMINATIM_URL = "https://nominatim.openstreetmap.org/search";


    public JPanel createRicercaPanel(JFrame frame) {

        JPanel mainPanel = new JPanel(new BorderLayout());

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(frame.getWidth(), frame.getHeight());

        // Barra di ricerca con JTextField
        JTextField searchField = new JTextField(60);
        JButton searchButton = new JButton("Cerca");

        // Pannello per i filtri con JCheckBox
        JPanel filterPanel = new JPanel();
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

        // Pannello contenente i controlli
        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("Indirizzo:"));
        searchPanel.add(searchField,BorderLayout.NORTH);
        searchPanel.add(searchButton,BorderLayout.CENTER);
        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(filterPanel, BorderLayout.CENTER);

        // Creazione del popup per i suggerimenti
        JPopupMenu suggestionPopup = new JPopupMenu();

        // Aggiungere un indicatore di caricamento
        JLabel loadingLabel = new JLabel("Caricamento...");
        loadingLabel.setVisible(false);
        searchPanel.add(loadingLabel);

        // Aggiungi un DocumentListener al JTextField
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            private final Timer timer = new Timer(300, null);

            private void scheduleUpdate() {
                timer.restart();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                scheduleUpdate();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                scheduleUpdate();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                scheduleUpdate();
            }

            {
                timer.setRepeats(false);
                timer.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        updateSuggestions();
                    }
                });
            }

            private void updateSuggestions() {
                loadingLabel.setVisible(true);
                SwingUtilities.invokeLater(() -> {
                    String inputText = searchField.getText().trim();
                    if (inputText.isEmpty()) {
                        suggestionPopup.setVisible(false);
                        loadingLabel.setVisible(false);
                        return;
                    }

                    // Ottieni suggerimenti dall'API
                    List<String> suggestions = fetchAutocompleteSuggestions(inputText);

                    // Rimuovi vecchi elementi
                    suggestionPopup.removeAll();

                    if (suggestions.isEmpty()) {
                        suggestionPopup.setVisible(false);
                        loadingLabel.setVisible(false);
                        return;
                    }

                    // Aggiungi nuovi elementi
                    for (String suggestion : suggestions) {
                        JMenuItem item = new JMenuItem(suggestion);
                        item.addActionListener(ev -> {
                            searchField.setText(suggestion);
                            suggestionPopup.setVisible(false);
                        });
                        suggestionPopup.add(item);
                    }

                    // Mostra il popup sotto il JTextField
                    try {
                        Rectangle caretCoords = searchField.modelToView(searchField.getCaretPosition());
                        Point popupLocation = new Point(caretCoords.x, caretCoords.y + caretCoords.height);
                        SwingUtilities.convertPointToScreen(popupLocation, searchField);
                        suggestionPopup.setLocation(popupLocation);
                        suggestionPopup.setInvoker(searchField);
                        suggestionPopup.setVisible(true);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    loadingLabel.setVisible(false);
                });
            }
        });

        // Azione del pulsante di ricerca: passa a un'altra schermata
        searchButton.addActionListener(e -> {

            GeoPosition geoPos = getCoordinatesFromAddress(searchField.getText());
            if (geoPos != null) {
                frame.setContentPane(new MapPanel().createMapPanel(frame, geoPos,checkBoxesList));
                frame.revalidate();
                frame.repaint();
            }else{
                JOptionPane.showMessageDialog(mainPanel, "Indirizzo non Valido.");
            }
        });


        JButton backButtonHome = new JButton("Torna alla Home");
        backButtonHome.addActionListener(e -> {
            frame.setContentPane(new Home().createHomePanel(frame));
            frame.revalidate();
            frame.repaint();
        });
        mainPanel.add(backButtonHome, BorderLayout.SOUTH);

        return mainPanel;
    }

    private static GeoPosition getCoordinatesFromAddress(String address) {

        OkHttpClient client = new OkHttpClient();
        String url = String.format("%s?q=%s&format=json&limit=1&accept-language=it", NOMINATIM_URL, address.replace(" ", "+"));

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Accept-Language", "it")
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

    private static List<String> fetchAutocompleteSuggestions(String input) {
        List<String> results = new ArrayList<>();
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder()
                .url("https://api.geoapify.com/v1/geocode/autocomplete?text="
                        + input + "&limit=10&lang=it&apiKey=" + GEOAPIFY_API_KEY)
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
                    results.add((String) properties.get("formatted"));
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return results;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Ricerca con AutoCompletamento");
            frame.setContentPane(new Ricerca().createRicercaPanel(frame));
            frame.setSize(frame.getWidth(), frame.getHeight());
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}
