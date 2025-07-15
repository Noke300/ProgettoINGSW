package controller;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import model.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.util.*;

//TODO dobbiamo gestire meglio le Exception di questa classe
public class ControllerAdministration extends Controller{


    protected static final String IMMOBILI_URL          = "http://localhost:8081/auth-agente/immobili";
    protected static final String VISITE_IMM_AGENTE     = "http://localhost:8081/auth-agente/visite";
    protected static final String ELIMINA_IMM           = "http://localhost:8081/auth-agente/elimina-immobile";
    protected static final String AGGIUNGI_IMMOBILE     = "http://localhost:8081/auth-agente/aggiungi-immobile";
    protected static final String GET_AGGENTEIMM        = "http://localhost:8081/auth-agente/specificheAgenteImmobiliare";
    protected static final String GET_SPECIFICHE_IMM    = "http://localhost:8081/auth-agente/specificheAgenteImmobiliare"; // (duplicato: puoi consolidare)
    protected static final String GET_IMMOBILE          = "http://localhost:8081/auth-agente/getImmobile";


    public boolean insertImmobili(Immobile immobile, List<String> immagini, List<String> fileNames, String token) {

        Map<String, Object> jsonMap = new HashMap<>();

        jsonMap.put("immobile", immobile);
        jsonMap.put("immagini", immagini);
        jsonMap.put("fileNames", fileNames);

        String requestBody = gson.toJson(jsonMap);
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
    public List<ImmobileEImmagini> fetchImmobili(String token) {

        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI(IMMOBILI_URL))
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
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<ImmobileEImmagini>>() {}.getType();
            return gson.fromJson(response.body(), listType);
        } else {
            throw new RuntimeException("Errore nella richiesta: " + response.statusCode() + " - " + response.body());
        }
    }

    public List<Visita> getVisite(String token) {
        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI(VISITE_IMM_AGENTE))
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException("URI non valido: " + e.getMessage());
        }

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
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


    public boolean eliminaImmobile(String immobile, String token) {
        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI(ELIMINA_IMM))
                    .header("Authorization", "Bearer " + token)
                    .header("Content-Type", "application/json")
                    .method("DELETE", HttpRequest.BodyPublishers.ofString(immobile))
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Errore nella URI: " + e.getMessage());
        }

        try {
          response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Errore durante l’eliminazione: " + e.getMessage());
        }
    }

    public AgenteImmobiliare fetchAgenteImmobiliare(String token) {
        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI(GET_AGGENTEIMM))
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException("URI non valido", e);
        }

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Errore nella comunicazione HTTP", e);
        }

        if (response.statusCode() == 200) {
            Gson gson = new Gson();
            return gson.fromJson(response.body(), AgenteImmobiliare.class);
        } else {
            throw new RuntimeException("Errore HTTP: " + response.statusCode() + " - " + response.body());
        }
    }

    public AgenteImmobiliare getSpecifiche(String token) {

        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI(GET_SPECIFICHE_IMM))
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
            Gson gson = new Gson();
            Type listType = new TypeToken<AgenteImmobiliare>() {}.getType();
            return gson.fromJson(response.body(), listType);
        } else {
            throw new RuntimeException("Errore nella richiesta: " + response.statusCode() + " - " + response.body());
        }
    }



    public Immobile fetchImmobile(String token, String idImmobile) {

        // Aggiungi il parametro idImmobile alla URL
        String endpoint = GET_IMMOBILE + "?idImmobile=" + URLEncoder.encode(idImmobile, StandardCharsets.UTF_8);

        try {
            URI uri = new URI(endpoint);
            request = HttpRequest.newBuilder()
                    .uri(uri)
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (response.statusCode() == 200) {
            Gson gson = new Gson();
            Type singleType = new TypeToken<Immobile>() {}.getType();
            return gson.fromJson(response.body(), singleType);
        } else {
            throw new RuntimeException("Errore nella richiesta: " + response.statusCode() + " - " + response.body());
        }
    }

}

