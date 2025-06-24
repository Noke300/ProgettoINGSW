package controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.*;
import org.jxmapviewer.viewer.GeoPosition;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class Controller {

    protected static final String LOGIN_URL_AGENTE = "http://localhost:8081/auth-agente/login";
    protected static final String LOGIN_URL_CLIENTE = "http://localhost:8081/auth-cliente/login";
    protected static final String IMMOBILI_URL = "http://localhost:8081/auth-agente/immobili";
    protected static final String RICERCA_URL = "http://localhost:8081/cerca-immobili";
    protected static final String DATE_URL = "http://localhost:8081/auth-agente/date";
    protected static final String OFFERTA_URL = "http://localhost:8081/auth-cliente/offerta";
    protected static final String OFFERTA_LIST = "http://localhost:8081/visualizza-offerte";

    protected static final String ELIMINA_IMM = "http://localhost:8081/auth-agente/elimina-immobile";

    protected static final Gson gson = new Gson();  //oggetto che serve a fare i Json

    public List<Immobile> ricerca(GeoPosition geoPos, Map<String, Object> filtri) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = null;
        HttpResponse<String> response;

        System.out.println(filtri);

        Map<String, Map<String, Object>> jsonMap = new HashMap<>();

        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("latitudine", geoPos.getLatitude());
        bodyMap.put("longitudine", geoPos.getLongitude());

        jsonMap.put("indirizzo", bodyMap);
        jsonMap.put("filtri", filtri);

        String requestBody = gson.toJson(jsonMap);
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
            return "";
        }
    }

    ///TODO Questo sara usato per gli immobili servira una con la sepicifica di un agente immobiliare
    public List<Offerta> getOffertePerImmobile(Immobile idImmobile) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request;

        Map<String, String> bodyMap = new HashMap<>();
        bodyMap.put("idImmobile", idImmobile.getId());


        String requestBody = gson.toJson(bodyMap);

        try {
            URI uri = new URI(OFFERTA_LIST); // endpoint esempio
            request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException("URI non valido: " + e.getMessage());
        }

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                Type listType = new TypeToken<List<Offerta>>() {}.getType();
                return gson.fromJson(response.body(), listType);
            } else {
                throw new RuntimeException("Errore HTTP: " + response.statusCode() + " - " + response.body());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Errore durante richiesta offerte: " + e.getMessage());
        }
    }

    public boolean inviaOfferta(Offerta offerta,String token) {
        HttpClient client = HttpClient.newHttpClient();
        String requestBody = gson.toJson(offerta);
        System.out.println("\n"+requestBody);

        HttpRequest request;
        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI(OFFERTA_URL)) // aggiorna se diverso
                    .header("Authorization", "Bearer " + token)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException("URI non valido: " + e.getMessage());
        }

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Errore durante invio offerta: " + e.getMessage());
        }
    }

}
