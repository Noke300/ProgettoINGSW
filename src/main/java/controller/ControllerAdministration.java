package controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Immobile;

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

public class ControllerAdministration {

    private static final String LOGIN_URL = "http://localhost:8080/auth-agente/login";
    private static final String IMMOBILI_URL = "http://localhost:8080/auth-agente/immobili";
    private static final String RICERCA_URL = "http://localhost:8080/cerca-immobili";
    private static final Gson gson = new Gson();  //oggetto che serve a fare i Json

    public static String loginAgente(String email, String password) throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        Map<String, String> bodyMap = new HashMap<>();
        bodyMap.put("emailAgente", email);
        bodyMap.put("passwordAgente", password);

        String requestBody = gson.toJson(bodyMap);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(LOGIN_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return response.body().replace("\"", ""); // rimuove doppi apici
        } else {
            System.out.println("Login fallito. Codice risposta: " + response.statusCode());
            return null;
        }
    }

    public static ArrayList<Immobile> fetchImmobili(String token) throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(IMMOBILI_URL))
                .header("Authorization", "Bearer " + token)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("Codice risposta: " + response.statusCode());

        if (response.statusCode() == 200) {
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Immobile>>() {}.getType();
            ArrayList<Immobile> immobili = gson.fromJson(response.body(), listType);
            return immobili;
        } else {
            throw new RuntimeException("Errore nella richiesta: " + response.statusCode() + " - " + response.body());
        }
    }

    public static List<Immobile> ricerca(String token, BigDecimal latitudine, BigDecimal longitudine) {
        HttpClient client = HttpClient.newHttpClient();

        Map<String, Double> bodyMap = new HashMap<>();
        bodyMap.put("latitudine", latitudine.doubleValue());
        bodyMap.put("longitudine", longitudine.doubleValue());

        String requestBody = gson.toJson(bodyMap);

        HttpRequest request = null;
        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI(RICERCA_URL))
                    .header("Authorization", "Bearer " + token)
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
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Codice risposta: " + response.statusCode());

        if (response.statusCode() == 200) {
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Immobile>>() {}.getType();
            return gson.fromJson(response.body(), listType);
        } else {
            throw new RuntimeException("Errore nella richiesta: " + response.statusCode() + " - " + response.body());
        }
    }

    public static void main(String[] args) {
        String token = null;
        try {
            token = loginAgente("prova@gmail.com", "password");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (token != null) {
            System.out.println("Token ricevuto: " + token);
            ArrayList<Immobile> immobili = null;
            try {
                immobili = fetchImmobili(token);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            immobili.forEach(System.out::println);
        }
    }
}

