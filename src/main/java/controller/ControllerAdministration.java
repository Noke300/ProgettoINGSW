package controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.AgenteImmobiliare;
import model.Immobile;
import model.Visita;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TODO dobbiamo gestire meglio le Exception di questa classe
public class ControllerAdministration extends Controller{



    public boolean insertImmobili(Immobile immobile, List<String> immagini, List<String> fileNames, String token) {
        HttpClient client = HttpClient.newHttpClient();

        Map<String, Object> jsonMap = new HashMap<>();

        jsonMap.put("immobile", immobile);
        jsonMap.put("immagini", immagini);
        jsonMap.put("fileNames", fileNames);

        String requestBody = gson.toJson(jsonMap);
        System.out.println("\n"+requestBody);

        HttpRequest request;
        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI(AGGIUNGI_IMMOBILE)) // aggiorna se diverso
                    .header("Authorization", "Bearer " + token)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException("URI non valido: " + e.getMessage());
        }

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Errore durante inserimento immobile: " + e.getMessage());
        }
    }



    public List<Immobile> fetchImmobili(String token) {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request;
        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI(IMMOBILI_URL))
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
        //client.close();

        System.out.println("Codice risposta: " + response.statusCode());

        if (response.statusCode() == 200) {
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Immobile>>() {}.getType();
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
                    .uri(new URI(VISITE_IMM))
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException("URI non valido: " + e.getMessage());
        }

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                Type listType = new TypeToken<List<Visita>>() {}.getType();
                return gson.fromJson(response.body(), listType);
            } else {
                throw new RuntimeException("Errore HTTP: " + response.statusCode() + " - " + response.body());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Errore durante richiesta visite: " + e.getMessage());
        }
    }


    public boolean eliminaImmobile(String immobile, String token) {

        Immobile immTemp = new Immobile(immobile);
        HttpClient client = HttpClient.newHttpClient();
        String requestBody = gson.toJson(immTemp);

        HttpRequest request;
        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI(ELIMINA_IMM))
                    .header("Authorization", "Bearer " + token)
                    .header("Content-Type", "application/json")
                    .method("DELETE", HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Errore nella URI: " + e.getMessage());
        }

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Errore durante l’eliminazione: " + e.getMessage());
        }
    }

    public AgenteImmobiliare fetchAgenteImmobiliare(String token) {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request;
        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI(GET_AGGENTEIMM))
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException("URI non valido", e);
        }

        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Errore nella comunicazione HTTP", e);
        }

        System.out.println("Codice risposta: " + response.statusCode());

        if (response.statusCode() == 200) {
            Gson gson = new Gson();
            return gson.fromJson(response.body(), AgenteImmobiliare.class);
        } else {
            throw new RuntimeException("Errore HTTP: " + response.statusCode() + " - " + response.body());
        }
    }


}

