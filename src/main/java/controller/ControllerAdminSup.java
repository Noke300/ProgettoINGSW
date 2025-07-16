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

public class ControllerAdminSup extends Controller {

    protected static final String ELIMINA_AGENTE_IMM = "http://localhost:8081/auth-adminSupport/licenziaAgenteImmobiliare";
    protected static final String ADD_AGENTE_IMM = "http://localhost:8081/auth-adminSupport/registraAgenteImmobiliare";
    //todo ti odio cambia il nome pesso di merda
    protected static final String GET_ADMIN_SUP = "http://localhost:8081/auth-adminSupport/specificheCliente";
    protected static final String GET_IMMOBILI = "http://localhost:8081/auth-adminSupport/visualizzaImmobili";

    public AdminSupport fetchAdminSup(String token) {
        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI(GET_ADMIN_SUP))
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
            return gson.fromJson(response.body(), AdminSupport.class);
        } else {
            throw new RuntimeException("Errore HTTP: " + response.statusCode() + " - " + response.body());
        }
    }



    //TODO funzione backend
    /*
    public List<AgenteImmobiliare> fetchAgentiImmobiliari(String token) {

        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI()) // definisci GET_AGENTIIMM correttamente
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
    /
     */

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

    public AdminSupport getAdminSup(String token) {

        AdminSupport adminSup = fetchAdminSup(token);
        adminSup.setAgentiImmobiliari(new ArrayList<>());
        adminSup.setImmobiliE(new ArrayList<>());
       // adminSup.setAgentiImmobiliari((ArrayList<AgenteImmobiliare>) fetchAgentiImmobiliari(token));
        adminSup.setImmobiliE((ArrayList<ImmobileEImmagini>) getListaImmobili(token));

        return adminSup;
    }
}
