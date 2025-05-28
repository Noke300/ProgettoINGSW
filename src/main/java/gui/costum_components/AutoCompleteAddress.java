package gui.costum_components;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AutoCompleteAddress {
    private static final String GEOAPIFY_API_KEY = "df7445014a484278a655999c41bc28c5";

    public void setupSearchAutocomplete(JTextField searchField) {
        JPopupMenu suggestionPopup = new JPopupMenu();
        suggestionPopup.setFocusable(false);

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            private final Timer timer = new Timer(500, e -> fetchAndShowSuggestions());

            {
                timer.setRepeats(false);
            }

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

            private void fetchAndShowSuggestions() {
                SwingUtilities.invokeLater(() -> {
                    String input = searchField.getText().trim();
                    if (input.isEmpty()) {
                        suggestionPopup.setVisible(false);
                        return;
                    }

                    List<String> suggestions = fetchAutocompleteSuggestions(input);
                    if (suggestions.isEmpty()) {
                        suggestionPopup.setVisible(false);
                        return;
                    }

                    suggestionPopup.removeAll();
                    for (String suggestion : suggestions) {
                        JMenuItem item = new JMenuItem(suggestion);
                        item.addActionListener(ev -> {
                            searchField.setText(suggestion);
                            suggestionPopup.setVisible(false);
                        });
                        suggestionPopup.add(item);
                    }

                    suggestionPopup.setPopupSize(searchField.getWidth(), searchField.getHeight() * suggestions.size()+50 / 5 + 10);
                    suggestionPopup.show(searchField, 0, searchField.getHeight());
                });
            }
        });
    }

    protected List<String> fetchAutocompleteSuggestions(String input) {
        List<String> results = new ArrayList<>();
        Set<String> uniqueLocations = new HashSet<>(); // Usa un Set per rimuovere duplicati

        OkHttpClient client = new OkHttpClient();
        String url = String.format("https://api.geoapify.com/v1/geocode/autocomplete?text=%s&limit=7&lang=it&filter=countrycode:it&apiKey=%s",
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

}
