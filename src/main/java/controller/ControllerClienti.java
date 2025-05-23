package controller;

import com.google.gson.Gson;
import model.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

public class ControllerClienti {

    private static final String LOGIN_URL = "http://localhost:8081/auth-cliente/login";
    private static final Gson gson = new Gson();

    public ControllerClienti() {
        setAdminSupport(null);
        setAgenziaImmobiliare(null);
        setAgenteImmobiliare(null);
        setCliente(null);
        setIndirizzo(null);
    }

    private AdminSupport adminSupport;
    private AgenziaImmobiliare agenziaImmobiliare;
    private AgenteImmobiliare agenteImmobiliare;
    private Cliente cliente;
    //da modificare
    private Indirizzo indirizzo;


    public AdminSupport getAdminSupport() {
        return adminSupport;
    }

    public void setAdminSupport(AdminSupport adminSupport) {
        this.adminSupport = adminSupport;
    }

    public AgenziaImmobiliare getAgenziaImmobiliare() {
        return agenziaImmobiliare;
    }

    public void setAgenziaImmobiliare(AgenziaImmobiliare agenziaImmobiliare) {
        this.agenziaImmobiliare = agenziaImmobiliare;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public AgenteImmobiliare getAgenteImmobiliare() {
        return agenteImmobiliare;
    }

    public void setAgenteImmobiliare(AgenteImmobiliare agenteImmobiliare) {
        this.agenteImmobiliare = agenteImmobiliare;
    }
    public Indirizzo getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(Indirizzo indirizzo) {
        this.indirizzo = indirizzo;
    }

    public static String logInClient(String emailCliente, String password) throws IOException, InterruptedException, URISyntaxException {
        HttpClient client = HttpClient.newHttpClient();

        Map<String, String> bodyMap = new HashMap<>();
        bodyMap.put("emailCliente", emailCliente);
        bodyMap.put("passwordCliente", password);

        String requestBody = gson.toJson(bodyMap);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(LOGIN_URL))
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

    public Immobile[] getImmos() {
        return null;
    }
}
