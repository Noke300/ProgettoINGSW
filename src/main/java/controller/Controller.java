package controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.Immobile;
import org.jxmapviewer.viewer.GeoPosition;

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

public class Controller {

    protected static final String LOGIN_URL_AGENTE = "http://localhost:8081/auth-agente/login";
    protected static final String LOGIN_URL_CLIENTE = "http://localhost:8081/auth-cliente/login";
    protected static final String IMMOBILI_URL = "http://localhost:8081/auth-agente/immobili";
    protected static final String RICERCA_URL = "http://localhost:8081/cerca-immobili";
    protected static final String DATE_URL = "http://localhost:8081/auth-agente/date";
    protected static final Gson gson = new Gson();  //oggetto che serve a fare i Json

    public List<Immobile> ricerca(GeoPosition geoPos) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = null;
        HttpResponse<String> response;

        Map<String, Double> bodyMap = new HashMap<>();
        bodyMap.put("latitudine", geoPos.getLatitude());
        bodyMap.put("longitudine", geoPos.getLongitude());

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

      //  client.close();

        if (response.statusCode() == 200) {
            Type listType = new TypeToken<ArrayList<Immobile>>() {}.getType();
            return gson.fromJson(response.body(), listType);
        } else {
            throw new RuntimeException("Errore nella richiesta: " + response.statusCode() + " - " + response.body());
        }
    }

    public static String logInClient(String emailCliente, String password) throws IOException, InterruptedException, URISyntaxException {
        HttpClient client = HttpClient.newHttpClient();

        Map<String, String> bodyMap = new HashMap<>();
        bodyMap.put("emailCliente", emailCliente);
        bodyMap.put("passwordCliente", password);

        String requestBody = gson.toJson(bodyMap);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(LOGIN_URL_CLIENTE))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return response.body().replace("\"", ""); // Rimuove doppi apici
        } else {
            System.out.println("Login fallito. Codice risposta: " + response.statusCode());
            return null;
        }
    }

    public String loginAgente(String email, String password) {
        HttpClient client = HttpClient.newHttpClient();

        Map<String, String> bodyMap = new HashMap<>();
        bodyMap.put("emailAgente", email);
        bodyMap.put("passwordAgente", password);

        String requestBody = gson.toJson(bodyMap);

        HttpRequest request;
        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI(LOGIN_URL_AGENTE))
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
        // client.close();

        if (response.statusCode() == 200) {
            return response.body().replace("\"", ""); // rimuove doppi apici
        } else {
            System.out.println("Login fallito. Codice risposta: " + response.statusCode());
            return null;
        }
    }


}
