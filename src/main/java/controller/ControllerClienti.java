package controller;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import model.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ControllerClienti extends Controller {

    protected static final String GET_SPECIFICHE = "http://localhost:8081/auth-cliente/specificheCliente";

    public Cliente getSpecifiche(String token) {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request;
        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI(GET_SPECIFICHE))
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Codice risposta: " + response.statusCode());

        if (response.statusCode() == 200) {
            Gson gson = new Gson();
            Type listType = new TypeToken<Cliente>() {}.getType();
            return gson.fromJson(response.body(), listType);
        } else {
            throw new RuntimeException("Errore nella richiesta: " + response.statusCode() + " - " + response.body());
        }
    }

    public List<Visita> getVisite(String token) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request;

        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI(VISITE_IMM_CLIENTE))
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException("URI non valido: " + e.getMessage());
        }

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                List<Visita> visite = new ArrayList<>();
                JsonArray jsonArray = JsonParser.parseString(response.body()).getAsJsonArray();

                for (JsonElement elem : jsonArray) {
                    JsonObject obj = elem.getAsJsonObject();

                    // Recupera i singoli campi dal JSON
                    Date dataVisita = gson.fromJson(obj.get("dataVisita"), Date.class);
                    LocalTime oraVisita = LocalTime.parse(obj.get("oraVisita").getAsString());
                    Boolean conferma = obj.get("conferma").getAsBoolean();

                    // Gestione Immobile e Cliente (se sono oggetti complessi)
                    Immobile immobile = gson.fromJson(obj.get("immobile"), Immobile.class);
                    Cliente cliente = gson.fromJson(obj.get("cliente"), Cliente.class);

                    // Costruisci visita
                    Visita visita = new Visita(immobile, cliente, oraVisita, dataVisita, conferma);
                    visite.add(visita);
                }

                return visite;
            } else {
                throw new RuntimeException("Errore HTTP: " + response.statusCode() + " - " + response.body());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Errore durante richiesta visite: " + e.getMessage());
        }
    }

    public List<Offerta> getOfferte(String token) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request;

        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI(OFFERTE_CLIENTE)) // Sostituisci con l'URL effettivo
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException("URI non valido: " + e.getMessage());
        }

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                List<Offerta> offerte = new ArrayList<>();
                JsonArray jsonArray = JsonParser.parseString(response.body()).getAsJsonArray();

                for (JsonElement elem : jsonArray) {
                    JsonObject obj = elem.getAsJsonObject();

                    // Parsing dei campi
                    Cliente cliente = gson.fromJson(obj.get("cliente"), Cliente.class);
                    LocalDateTime dataOfferta = LocalDateTime.parse(obj.get("dataOfferta").getAsString());
                    Double prezzoOfferta = obj.get("prezzoOfferta").getAsDouble();
                    Immobile immobile = gson.fromJson(obj.get("immobile"), Immobile.class);
                    Boolean contrOfferta = obj.has("contrOfferta") ? obj.get("contrOfferta").getAsBoolean() : null;
                    Boolean esitoOfferta = obj.has("esitoOfferta") ? obj.get("esitoOfferta").getAsBoolean() : null;

                    // Costruzione oggetto Offerta
                    Offerta offerta = new Offerta(cliente, dataOfferta, prezzoOfferta, immobile, contrOfferta, esitoOfferta);
                    offerte.add(offerta);
                }

                return offerte;
            } else {
                throw new RuntimeException("Errore HTTP: " + response.statusCode() + " - " + response.body());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Errore durante la richiesta delle offerte: " + e.getMessage());
        }
    }



}