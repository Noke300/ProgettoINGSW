package controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.AdminSupport;
import model.AgenteImmobiliare;
import model.AgenziaImmobiliare;
import model.ImmobileEImmagini;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ControllerAdmin extends Controller {

    protected static final String GET_AGENZIAIMM        = "http://localhost:8081/auth-admin/specificheAgenziaImmobiliare";
    protected static final String GET_AGENTIIMM         = "http://localhost:8081/auth-admin/getAgentiImmobiliari";
    protected static final String GET_ADMINSUPPORT      = "http://localhost:8081/auth-admin/getAdminSupport";
    protected static final String ELIMINA_ADMINSUP      = "http://localhost:8081/auth-admin/licenziaAdminSupport";
    protected static final String ELIMINA_AGENTE_IMM    = "http://localhost:8081/auth-admin/licenziaAgenteImmobiliare";
    protected static final String ADD_AGENTE_IMM        = "http://localhost:8081/auth-admin/registraAgenteImmobiliare";
    protected static final String ADD_ADMINSUP          = "http://localhost:8081/auth-admin/registraAdminSupport";
    protected static final String GET_IMMOBILI          = "http://localhost:8081/auth-admin/visualizzaImmobili";


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


    public List<ImmobileEImmagini> getListaImmobili(String token) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(GET_IMMOBILI))
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Type listType = new TypeToken<List<ImmobileEImmagini>>() {}.getType();
                return new Gson().fromJson(response.body(), listType);
            } else {
                System.err.println("Errore HTTP: " + response.statusCode());
                return Collections.emptyList();
            }
        } catch (Exception e) {
            throw new RuntimeException("Errore durante la visualizzazione immobili: " + e.getMessage());
        }
    }




    public AgenziaImmobiliare getAgenziaImmobiliare(String token) {

        AgenziaImmobiliare agenziaImmobiliare = fetchAgenziaImmobiliare(token);

        agenziaImmobiliare.setAgentiImmobiliari(new ArrayList<>());
        agenziaImmobiliare.setAdminSupport(new ArrayList<>());
        agenziaImmobiliare.setListaImmobili(new ArrayList<>());

        agenziaImmobiliare.setAdminSupport((ArrayList<AdminSupport>) fetchAdminSupport(token));
        agenziaImmobiliare.setAgentiImmobiliari((ArrayList<AgenteImmobiliare>) fetchAgentiImmobiliari(token));
        agenziaImmobiliare.setListaImmobili((ArrayList<ImmobileEImmagini>) getListaImmobili(token));

        return agenziaImmobiliare;
    }


}
