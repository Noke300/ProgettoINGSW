package controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import java.util.List;
import java.util.Map;
import java.util.HashMap;

//TODO dobbiamo gestire meglio le Exception di questa classe
public class ControllerAdministration extends Controller{


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

