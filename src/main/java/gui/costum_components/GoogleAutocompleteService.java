package gui.costum_components;

import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.model.AutocompletePrediction;
import com.google.maps.model.ComponentFilter;
import com.google.maps.errors.ApiException;
import com.google.maps.model.PlaceAutocompleteType;
import com.google.maps.model.ComponentFilter;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GoogleAutocompleteService {

    private static final String GOOGLE_API_KEY = "AIzaSyCxXT8MqPi70nzJKKDEFXc0zyKAHimXeq0"; // SOSTITUISCI CON LA TUA CHIAVE API
    private GeoApiContext geoApiContext;

    public GoogleAutocompleteService() {
        this.geoApiContext = new GeoApiContext.Builder()
                .apiKey(GOOGLE_API_KEY)
                .build();
    }

    public List<String> fetchAutocompleteSuggestions(String input) {
        List<String> results = new ArrayList<>();
        Set<String> uniqueLocations = new HashSet<>(); // Usa un Set per rimuovere duplicati

        if (input == null || input.trim().isEmpty()) {
            return results; // Ritorna lista vuota se l'input è vuoto
        }

        // Per il billing corretto, è consigliato usare session tokens.
        // Crea un nuovo token all'inizio di una sessione di autocompletamento dell'utente
        // e passalo a tutte le successive richieste di autocompletamento per quella sessione.
        // AutocompleteSessionToken sessionToken = new AutocompleteSessionToken();

        try {
            AutocompletePrediction[] predictions = PlacesApi.queryAutocomplete(geoApiContext, input)
                    .language("it") // Specifica la lingua italiana
                   // Opzionale: per restringere ha risultati geocodificabili (indirizzi, città, ecc.)
                    // Simile a 'geocode/autocomplete' di Geoapify.
                    // Altri tipi: ADDRESS, ESTABLISHMENT, (regions), (cities)
                    // .sessionToken(sessionToken) // Passa il session token se lo usi
                    .await();
            // Esegue la chiamata in modo sincrono

            if (predictions != null) {
                for (AutocompletePrediction prediction : predictions) {
                    // 'prediction.description' è il testo formattato suggerito, simile a 'formatted' di Geoapify
                    String formatted = prediction.description;

                    // Normalizza il nome rimuovendo "Italia" o "Italy" dalla fine
                    // Nota: Google Places API di solito include già il paese nel formato corretto per la lingua specificata.
                    // Questa normalizzazione potrebbe non essere strettamente necessaria o potrebbe necessitare di aggiustamenti
                    // a seconda di come Google formatta i risultati per l'Italia.
                    String normalized = formatted.replaceAll(",?\\s*(Italia|Italy)\\s*$", "").trim();

                    if (uniqueLocations.add(normalized)) {
                        results.add(formatted); // Mantiene il formato originale
                    }

                    // Google Places API non ha un parametro 'limit' diretto come Geoapify.
                    // Solitamente restituisce fino a 5 risultati. Se ne servono di più e Google ne fornisce di più
                    // (a volte con session token), puoi troncare la lista 'results' qui se necessario.
                    // if (results.size() >= 10) break; // Esempio se vuoi limitare a 10
                }
            }
        } catch (ApiException | InterruptedException | IOException e) {
            // Logga l'eccezione in modo più robusto in un'applicazione reale
            e.printStackTrace();
        }

        return results;
    }

    // È buona pratica chiudere il GeoApiContext quando l'applicazione termina
    // per rilasciare le risorse.
    public void shutdown() {
        if (this.geoApiContext != null) {
            this.geoApiContext.shutdown();
        }
    }
}