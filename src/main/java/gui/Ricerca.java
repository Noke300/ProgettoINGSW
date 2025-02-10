package gui;

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
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Ricerca {

    private static final String GEOAPIFY_API_KEY = "df7445014a484278a655999c41bc28c5"; // Sostituisci con la tua chiave API

    public JPanel createMainPanel(JFrame frame) {

        JPanel mainPanel = new JPanel(new BorderLayout());

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(frame.getWidth(), frame.getHeight());

        // ComboBox per la ricerca (editabile)
        JComboBox<CheckableItem> comboBoxRicerca = new JComboBox<>();
        comboBoxRicerca.setEditable(true);
        JTextField textField = (JTextField) comboBoxRicerca.getEditor().getEditorComponent();
        textField.setColumns(60);
        JButton searchButton = new JButton("Cerca");

        // Impostiamo il modello per la combo box ed esso deve essere final
        final DefaultComboBoxModel<CheckableItem> model = new DefaultComboBoxModel<>();
        comboBoxRicerca.setModel(model);

        // ComboBox per i filtri con elementi predefiniti e renderer personalizzato
        List<CheckableItem> items = new ArrayList<>();
        items.add(new CheckableItem("Scuole"));
        items.add(new CheckableItem("Ospedali"));
        items.add(new CheckableItem("Bar"));
        JComboBox<CheckableItem> filterButton = new JComboBox<>(items.toArray(new CheckableItem[0]));
        filterButton.setRenderer(new CheckBoxRenderer());

        // Pannello contenente i controlli
        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("Indirizzo:"));
        searchPanel.add(comboBoxRicerca);
        searchPanel.add(searchButton);
        searchPanel.add(filterButton);
        mainPanel.add(searchPanel, BorderLayout.NORTH);

        filterButton.addActionListener(e -> {
            CheckableItem item = (CheckableItem) filterButton.getSelectedItem();
            if (item != null) {
                item.setSelected(!item.isSelected()); // Cambia lo stato
                filterButton.repaint(); // Aggiorna la UI
            }
        });

        final Timer debounceTimer = new Timer(300, null);
        debounceTimer.setRepeats(false);

        textField.getDocument().addDocumentListener(new DocumentListener() {
            // Metodo per avviare/riavviare il timer
            private void scheduleUpdate() {
                // Ferma eventuali timer in esecuzione e poi riavvialo
                debounceTimer.restart();
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
        });

// Definiamo cosa succede quando il timer "scatta"
        debounceTimer.addActionListener(e -> {
            String inputText = textField.getText();
            if (inputText.trim().isEmpty()) {
                model.removeAllElements();
                comboBoxRicerca.hidePopup();
                return;
            }

            // Lanciamo lo SwingWorker per aggiornare i suggerimenti
            SwingWorker<List<String>, Void> worker = new SwingWorker<List<String>, Void>() {
                @Override
                protected List<String> doInBackground() throws Exception {
                    return fetchAutocompleteSuggestions(inputText);
                }

                @Override
                protected void done() {
                    try {
                        List<String> suggestions = get();
                        model.removeAllElements();
                        for (String suggestion : suggestions) {
                            model.addElement(new CheckableItem(suggestion));
                        }
                        // Imposta l'editor con il testo corrente per evitare auto-selezioni indesiderate
                        comboBoxRicerca.getEditor().setItem(inputText);

                        if (comboBoxRicerca.isShowing()) { // Verifica che il componente sia visibile
                            if (model.getSize() > 0) {
                                comboBoxRicerca.showPopup();
                            } else {
                                comboBoxRicerca.hidePopup();
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

            };
            worker.execute();

        });

        // Gestione degli eventi di selezione: cliccando su un elemento si inverte lo stato della checkbox
       comboBoxRicerca.addActionListener(e -> {
            Object selected = comboBoxRicerca.getSelectedItem();
            if (selected instanceof CheckableItem) {
                CheckableItem item = (CheckableItem) selected;
                item.setSelected(!item.isSelected()); // Inverte lo stato
                comboBoxRicerca.repaint(); // Aggiorna la UI
            }
        });

        // Azione del pulsante di ricerca: passa ad un'altra schermata (es. la mappa)
        searchButton.addActionListener(e -> {
            // Nota: assicurati che la classe Main e il metodo createMainPanel(frame, String) siano definiti
            frame.setContentPane(new Main().createMainPanel(frame, textField.getText()));
            frame.revalidate();
            frame.repaint();
        });

        return mainPanel;
    }

    private static List<String> fetchAutocompleteSuggestions(String input) {
        List<String> results = new ArrayList<>();
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Request request = new Request.Builder()
                .url("https://api.geoapify.com/v1/geocode/autocomplete?text="
                        + input + "&limit=10&apiKey=" + GEOAPIFY_API_KEY)
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

    // Classe per rappresentare gli elementi con checkbox
    static class CheckableItem {
        private String text;
        private boolean selected;

        public CheckableItem(String text) {
            this.text = text;
            this.selected = false;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    // Renderer personalizzato per visualizzare JCheckBox dentro la JComboBox
    static class CheckBoxRenderer extends JCheckBox implements ListCellRenderer<CheckableItem> {
        @Override
        public Component getListCellRendererComponent(JList<? extends CheckableItem> list, CheckableItem value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            setText(value.toString());
            setSelected(value.isSelected());
            setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
            return this;
        }
    }
}
