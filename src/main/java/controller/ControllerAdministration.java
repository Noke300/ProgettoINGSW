package controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Immobile;
import model.Visita;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

//TODO dobbiamo gestire meglio le Exception di questa classe
public class ControllerAdministration {

    private static final String LOGIN_URL = "http://localhost:8080/auth-agente/login";
    private static final String IMMOBILI_URL = "http://localhost:8080/auth-agente/immobili";
    private static final String RICERCA_URL = "http://localhost:8080/cerca-immobili";
    private static final String DATE_URL = "http://localhost:8080/auth-agente/date";
    private static final Gson gson = new Gson();  //oggetto che serve a fare i Json

    public String loginAgente(String email, String password) {
        HttpClient client = HttpClient.newHttpClient();

        Map<String, String> bodyMap = new HashMap<>();
        bodyMap.put("emailAgente", email);
        bodyMap.put("passwordAgente", password);

        String requestBody = gson.toJson(bodyMap);

        HttpRequest request;
        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI(LOGIN_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RuntimeException();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        client.close();

        if (response.statusCode() == 200) {
            return response.body().replace("\"", ""); // rimuove doppi apici
        } else {
            System.out.println("Login fallito. Codice risposta: " + response.statusCode());
            return null;
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
        client.close();

        System.out.println("Codice risposta: " + response.statusCode());

        if (response.statusCode() == 200) {
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Immobile>>() {}.getType();
            return gson.fromJson(response.body(), listType);
        } else {
            throw new RuntimeException("Errore nella richiesta: " + response.statusCode() + " - " + response.body());
        }
    }

    public List<Immobile> ricerca(BigDecimal latitudine, BigDecimal longitudine) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = null;
        HttpResponse<String> response;

        Map<String, Double> bodyMap = new HashMap<>();
        bodyMap.put("latitudine", latitudine.doubleValue());
        bodyMap.put("longitudine", longitudine.doubleValue());

        String requestBody = gson.toJson(bodyMap);
        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI(RICERCA_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
        } catch (URISyntaxException e) {
            System.out.println("Errore nella richiesta: " + e.getMessage());
        }

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RuntimeException("Errore nella richiesta: " + e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException("Errore nella richiesta: " + e.getMessage());
        }

        client.close();

        if (response.statusCode() == 200) {
            Type listType = new TypeToken<ArrayList<Immobile>>() {}.getType();
            return gson.fromJson(response.body(), listType);
        } else {
            throw new RuntimeException("Errore nella richiesta: " + response.statusCode() + " - " + response.body());
        }
    }

    public ArrayList<Visita> recuperoData(String token) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = null;
        HttpResponse<String> response;

        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI(DATE_URL))
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (response.statusCode() == 200) {
            Type listType = new TypeToken<ArrayList<Visita>>() {}.getType();
            return gson.fromJson(response.body(), listType);
        } else {
            throw new RuntimeException("Errore nella richiesta: " + response.statusCode() + " - " + response.body());
        }

    }
}

