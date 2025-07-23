package gui.costum_components;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.io.IOException;
import java.util.*;

import static gui.pagine_administrative.agenteImmobliiare.InserimentoImmobile.regioniProvinceLabelMap;

public class AutoCompleteAddress {
    private static final String GEOAPIFY_API_KEY = "df7445014a484278a655999c41bc28c5";


    public void setupSearchAutocomplete(JTextField searchField) {
        JPopupMenu suggestionPopup = new JPopupMenu();
        suggestionPopup.setFocusable(false);

        final DocumentListener[] listenerHolder = new DocumentListener[1];

        listenerHolder[0] = new DocumentListener() {
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
                            // Rimuove temporaneamente il listener
                            searchField.getDocument().removeDocumentListener(listenerHolder[0]);

                            searchField.setText(suggestion);
                            suggestionPopup.setVisible(false);

                            // Ripristina il focus e riattiva il listener
                            SwingUtilities.invokeLater(() -> {
                                searchField.requestFocusInWindow();
                                searchField.getDocument().addDocumentListener(listenerHolder[0]);
                            });
                        });
                        suggestionPopup.add(item);
                    }

                    suggestionPopup.setPopupSize(
                            searchField.getWidth(),
                            searchField.getHeight() * suggestions.size() + 50 / 5 + 10
                    );
                    suggestionPopup.show(searchField, 0, searchField.getHeight());
                });
            }
        };

        // Registra il listener iniziale
        searchField.getDocument().addDocumentListener(listenerHolder[0]);
    }


    public void setupSearchAutocomplete(JTextField searchField,
                                        JTextField viaField,
                                        JTextField capField,
                                        JTextField civicoField,
                                        JTextField cittaField,
                                        JComboBox<String> regioneCombo,
                                        JComboBox<String> provinciaCombo) {

        JPopupMenu suggestionPopup = new JPopupMenu();
        suggestionPopup.setFocusable(false);

        final DocumentListener[] listenerHolder = new DocumentListener[1];

        listenerHolder[0] = new DocumentListener() {
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
                            // Disattiva temporaneamente il listener
                            searchField.getDocument().removeDocumentListener(listenerHolder[0]);

                            searchField.setText(suggestion);
                            suggestionPopup.setVisible(false);

                            // Popola altri campi e riattiva il listener
                            SwingUtilities.invokeLater(() -> {
                                popolaCampiIndirizzo(suggestion, viaField, capField, civicoField, cittaField, regioneCombo, provinciaCombo);
                                searchField.requestFocusInWindow();
                                searchField.getDocument().addDocumentListener(listenerHolder[0]);
                            });
                        });
                        suggestionPopup.add(item);
                    }

                    suggestionPopup.setPopupSize(searchField.getWidth(), searchField.getHeight() * suggestions.size() + 50 / 5 + 10);
                    suggestionPopup.show(searchField, 0, searchField.getHeight());
                });
            }
        };

        // Registra il listener iniziale
        searchField.getDocument().addDocumentListener(listenerHolder[0]);
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

    private void popolaCampiIndirizzo(
            String fullAddress,
            JTextField viaField,
            JTextField capField,
            JTextField civicoField,
            JTextField cittaField,
            JComboBox<String> regioneCombo,
            JComboBox<String> provinciaCombo
    ) {
        OkHttpClient client = new OkHttpClient();
        String url = String.format(
                "https://api.geoapify.com/v1/geocode/search?text=%s&limit=1&lang=it&filter=countrycode:it&apiKey=%s",
                fullAddress, GEOAPIFY_API_KEY
        );

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

                if (!features.isEmpty()) {
                    JSONObject feature = (JSONObject) features.get(0);
                    JSONObject properties = (JSONObject) feature.get("properties");

                    String street = safeGet(properties, "street");
                    String housenumber = safeGet(properties, "housenumber");
                    String postcode = safeGet(properties, "postcode");
                    String city = safeGet(properties, "city");
                    String region = safeGet(properties, "state");
                    String provinceSigla = safeGet(properties, "county"); // es: MI, NA...

                    // Popola i JTextField
                    viaField.setText(street);
                    civicoField.setText(housenumber);
                    capField.setText(postcode);
                    cittaField.setText(city);

                    // Imposta Regione se presente
                    if (region != null && !region.isEmpty()) {
                        for (int i = 0; i < regioneCombo.getItemCount(); i++) {
                            String regioneItem = regioneCombo.getItemAt(i);
                            if (regioneItem.equalsIgnoreCase(region)) {
                                regioneCombo.setSelectedIndex(i);
                                break;
                            }
                        }
                    }

                    // Aggiorna ProvinciaCombo in base alla regione selezionata
                    Map<String, List<String>> regioneMap = regioniProvinceLabelMap();
                    List<String> provinceList = regioneMap.getOrDefault((String) regioneCombo.getSelectedItem(), new ArrayList<>());
                    provinciaCombo.removeAllItems();
                    provinceList.stream()
                            .sorted(Comparator.comparing(s -> s.substring(s.indexOf("-") + 2)))
                            .forEach(provinciaCombo::addItem);

                    // Imposta Provincia se sigla disponibile
                    if (provinceSigla != null && !provinceSigla.isEmpty()) {
                        String siglaLower = provinceSigla.toLowerCase();
                        for (int i = 0; i < provinciaCombo.getItemCount(); i++) {
                            String provinciaItem = provinciaCombo.getItemAt(i).toLowerCase();

                            if (provinciaItem.startsWith(siglaLower + " ") || provinciaItem.endsWith(" " + siglaLower)) {
                                provinciaCombo.setSelectedIndex(i);
                                break;
                            }
                            if (provinciaItem.contains(siglaLower)) {
                                provinciaCombo.setSelectedIndex(i);
                                break;
                            }
                        }
                    }

                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }


    // Metodo di supporto per evitare i null
    private String safeGet(JSONObject obj, String key) {
        Object value = obj.get(key);
        return value != null ? value.toString() : "";
    }


}
