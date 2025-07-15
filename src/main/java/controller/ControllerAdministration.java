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

    protected static final String GET_SPECIFICHE = "http://localhost:8081/auth-agente/specificheAgenteImmobiliare";
    protected static final String GET_AGENZIAIMM = "http://localhost:8081/auth-admin/specificheAgenziaImmobiliare";
    protected static final String GET_IMMOBILE = "http://localhost:8081/auth-agente/getImmobile";
    protected static final String GET_AGENTIIMM = "http://localhost:8081/auth-admin/getAgentiImmobiliari";
    protected static final String GET_ADMINSUPPORT = "http://localhost:8081/auth-admin/getAdminSupport";
    protected static final String ELIMINA_ADMINSUP = "http://localhost:8081/auth-admin/licenziaAdminSupport";
    protected static final String ELIMINA_AGENTE_IMM = "http://localhost:8081/auth-admin/licenziaAgenteImmobiliare";
    protected static final String ADD_AGENTE_IMM = "http://localhost:8081/auth-admin/registraAgenteImmobiliare";
    protected static final String ADD_ADMINSUP = "http://localhost:8081/auth-admin/registraAdminSupport";


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

    public boolean eliminaAgenteImmobiliare(AgenteImmobiliare agenteJson, String token) {

        requestBody = gson.toJson(agenteJson);
        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI(ELIMINA_AGENTE_IMM))
                    .header("Authorization", "Bearer " + token)
                    .header("Content-Type", "application/json")
                    .method("DELETE", HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Errore nella URI agente: " + e.getMessage());
        }

        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200 || response.statusCode() == 204;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Errore durante l’eliminazione dell’agente: " + e.getMessage());
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

    public boolean eliminaAdminSupport(AdminSupport adminSupportJson, String token) {
        requestBody = gson.toJson(adminSupportJson);
        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI(ELIMINA_ADMINSUP))
                    .header("Authorization", "Bearer " + token)
                    .header("Content-Type", "application/json")
                    .method("DELETE", HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Errore nella URI admin: " + e.getMessage());
        }

        try {
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 200 || response.statusCode() == 204;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Errore durante l’eliminazione admin: " + e.getMessage());
        }
    }


    public AgenteImmobiliare getSpecifiche(String token) {

        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI(GET_SPECIFICHE))
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

    public AgenziaImmobiliare fetchAgenziaImmobiliare(String token) {
        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI(GET_AGENZIAIMM))
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

        System.out.println("Codice risposta: " + response.statusCode());

        if (response.statusCode() == 200) {
            Gson gson = new Gson();
            return gson.fromJson(response.body(), AgenziaImmobiliare.class);
        } else {
            throw new RuntimeException("Errore HTTP: " + response.statusCode() + " - " + response.body());
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


    public List<AgenteImmobiliare> fetchAgentiImmobiliari(String token) {

        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI(GET_AGENTIIMM)) // definisci GET_AGENTIIMM correttamente
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
            Type listType = new TypeToken<List<AgenteImmobiliare>>() {}.getType();
            return gson.fromJson(response.body(), listType);
        } else {
            throw new RuntimeException("Errore HTTP: " + response.statusCode() + " - " + response.body());
        }
    }

    public List<AdminSupport> fetchAdminSupport(String token) {

        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI(GET_ADMINSUPPORT)) // definisci GET_ADMINSUPPORT correttamente
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
            Type listType = new TypeToken<List<AdminSupport>>() {}.getType();
            return gson.fromJson(response.body(), listType);
        } else {
            throw new RuntimeException("Errore HTTP: " + response.statusCode() + " - " + response.body());
        }
    }

    public boolean aggiungiAdminSupport(AdminSupport adminJson, String token) {
         requestBody = gson.toJson(adminJson);
        try {
             request = HttpRequest.newBuilder()
                    .uri(new URI(ADD_ADMINSUP))
                    .header("Authorization", "Bearer " + token)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

           response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            String body = response.body();
            return response.statusCode() == 201 || (body != null && body.contains("Admin Support creato"));
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException("Errore durante la registrazione admin support: " + e.getMessage());
        }
    }

    public boolean aggiungiAgenteImmobiliare(AgenteImmobiliare agenteJson, String token) {
         requestBody = gson.toJson(agenteJson);

        try {
           request = HttpRequest.newBuilder()
                    .uri(new URI(ADD_AGENTE_IMM))
                    .header("Authorization", "Bearer " + token)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            String body = response.body();
            return response.statusCode() == 201 || (body != null && body.contains("Agente Immobiliare creato"));
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException("Errore durante la registrazione agente: " + e.getMessage());
        }
    }




    public AgenziaImmobiliare getAgenziaImmobiliare(String token) {

        AgenziaImmobiliare agenziaImmobiliare = fetchAgenziaImmobiliare(token);
        agenziaImmobiliare.setAdminSupport((ArrayList<AdminSupport>) fetchAdminSupport(token));
        agenziaImmobiliare.setAgentiImmobiliari((ArrayList<AgenteImmobiliare>) fetchAgentiImmobiliari(token));

        return agenziaImmobiliare;
    }
}

