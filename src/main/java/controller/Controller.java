package controller;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import model.*;
import org.jetbrains.annotations.NotNull;
import org.jxmapviewer.viewer.GeoPosition;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class Controller {

    HttpClient client = HttpClient.newHttpClient();
    HttpResponse<String> response;
    HttpRequest request;
    private String nameLogIn;

    //TODO separare i vari path nei rispettivi controller
    protected static final String LOGIN_URL_AGENTE = "http://localhost:8081/auth-agente/login";
    protected static final String LOGIN_URL_ADMINSUP = "http://localhost:8081/auth-adminSupport/login";
    protected static final String LOGIN_URL_ADMIN = "http://localhost:8081/auth-admin/login";
    protected static final String LOGIN_URL_CLIENTE = "http://localhost:8081/auth-cliente/login";

    protected static final String VISITE_IMM_CLIENTE = "http://localhost:8081/auth-cliente/getVisite";
    protected static final String IMMOBILI_URL = "http://localhost:8081/auth-agente/immobili";
    protected static final String RICERCA_URL = "http://localhost:8081/cerca-immobili";
    protected static final String VISITE_IMM_AGENTE = "http://localhost:8081/auth-agente/visite";
    protected static final String PRENOTA_VISITE = "http://localhost:8081/auth-cliente/visita";
    protected static final String OFFERTA_URL = "http://localhost:8081/offerta";
    protected static final String OFFERTA_LIST = "http://localhost:8081/visualizza-offerte";
    protected static final String ELIMINA_IMM = "http://localhost:8081/auth-agente/elimina-immobile";
    protected static final String REGISTRAZIONE_URL_CLIENTE = "http://localhost:8081/auth-cliente/register";
    protected static final String AGGIUNGI_IMMOBILE = "http://localhost:8081/auth-agente/aggiungi-immobile";
    protected static final String GET_AGGENTEIMM = "http://localhost:8081/auth-agente/specificheAgenteImmobiliare";
    protected static final String OFFERTE_CLIENTE = "http://localhost:8081/auth-cliente/getOfferte";
    protected static final String WHO_URL = "http://localhost:8081/whoAmI";


    protected static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalTime.class, (JsonSerializer<LocalTime>) (src, typeOfSrc, context) ->
                    new JsonPrimitive(src.format(DateTimeFormatter.ofPattern("HH:mm")))
            )
            .setDateFormat("yyyy-MM-dd")
            .create();//oggetto che serve a fare

    public List<ImmobileEImmagini> ricerca(GeoPosition geoPos, Map<String, Object> filtri) {

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
        if (response.statusCode() == 200) {
            Type listType = new TypeToken<ArrayList<ImmobileEImmagini>>() {
            }.getType();
            return gson.fromJson(response.body(), listType);
        } else {
            throw new RuntimeException("Errore nella richiesta: " + response.statusCode() + " - " + response.body());
        }
    }

    public String logInClient(String emailCliente, String password) {

        Map<String, String> bodyMap = new HashMap<>();
        bodyMap.put("emailCliente", emailCliente);
        bodyMap.put("passwordCliente", password);

        Gson gson = new Gson(); // Assicurati che sia configurato se serve formattazione data o custom type adapter
        String requestBody = gson.toJson(bodyMap);

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(LOGIN_URL_CLIENTE))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                setNameLogIn(getRawSpecifiche(response.body().replace("\"", "")));
                return response.body().replace("\"", ""); // rimuove doppi apici
            } else {
                return "";
            }

        } catch (URISyntaxException e) {
            System.err.println("URI non valido: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Errore di comunicazione con il server: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Buona pratica
            System.err.println("La richiesta è stata interrotta: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Errore imprevisto: " + e.getMessage());
        }

        return "";
    }
    public CompletableFuture<String> loginParallel(String email, String password) {
        List<String> urls = List.of(LOGIN_URL_ADMIN, LOGIN_URL_ADMINSUP, LOGIN_URL_AGENTE);

        List<CompletableFuture<String>> futures = urls.stream()
                .map(url -> CompletableFuture.supplyAsync(() -> attemptLogin(url, email, password)))
                .toList();

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(voidd -> {
                    for (CompletableFuture<String> future : futures) {
                        try {
                            String token = future.get();
                            if (token != null && !token.isBlank()) {
                                return token.trim(); // primo token valido
                            }
                        } catch (Exception e) {
                            System.err.println("Errore durante lettura del future: " + e.getMessage());
                        }
                    }
                    return ""; // nessun token trovato
                });
    }

    private String attemptLogin(String url, String email, String password) {
        Map<String, String> credentials = getPayloadPerTipo(url, email, password);
        String requestBody = gson.toJson(credentials);

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200 && response.body() != null && !response.body().isBlank()) {
                String token = response.body().replace("\"", "").trim();
                System.out.println(" Login riuscito su " + url + ", token: " + token);
                return token;
            } else {
                System.out.println(" Login fallito su " + url + ". Stato: " + response.statusCode());
            }

        } catch (Exception e) {
            System.err.println("Errore nel login su " + url + ": " + e.getMessage());
        }

        return "";
    }


    private static Map<String, String> getPayloadPerTipo(String url, String email, String password) {
        if (url.contains("agente")) {
            return Map.of("emailAgente", email, "passwordAgente", password);
        } else if (url.contains("adminSupport")) {
            return Map.of("emailSupport", email, "passwordSupport", password);
        } else if (url.contains("admin")) {
            return Map.of("emailAdmin", email, "passwordAdmin", password);
        }
        // Fallback generico se necessario
        return Map.of("email", email, "password", password);
    }


    public boolean registrazioneCliente(Cliente cliente) {
        String requestBody = gson.toJson(cliente);

        try {
            this.request = HttpRequest.newBuilder()
                    .uri(new URI(REGISTRAZIONE_URL_CLIENTE))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Errore URI: " + e.getMessage());
        }

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200 || response.statusCode() == 201) {
                return true;
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Errore nella richiesta: " + e.getMessage());
        }
        return false;
    }



    ///TODO Questo sara usato per gli immobili servira una con la sepicifica di un agente immobiliare
    public List<Offerta> getOffertePerImmobile(Immobile idImmobile) {

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
           response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {

                List<Offerta> offerte = new ArrayList<>();
                JsonArray jsonArray = JsonParser.parseString(response.body()).getAsJsonArray();

                for (JsonElement elem : jsonArray) {
                    JsonObject obj = elem.getAsJsonObject();

                    // Recupera i singoli campi dal JSON
                    Double prezzoOfferta = gson.fromJson(obj.get("prezzoOfferta"), Double.class);
                    LocalDateTime dataOfferta = LocalDateTime.parse(obj.get("dataOfferta").getAsString());
                    Boolean esitoOfferta = obj.get("esitoOfferta").getAsBoolean();
                    Boolean contrOfferta = obj.get("contrOfferta").getAsBoolean();

                    // Gestione Immobile e Cliente (se sono oggetti complessi)
                    Immobile immobile = gson.fromJson(obj.get("immobile"), Immobile.class);
                    Cliente cliente = gson.fromJson(obj.get("cliente"), Cliente.class);

                    // Costruisci offerta
                    Offerta offerta = new Offerta(cliente, dataOfferta, prezzoOfferta, immobile, contrOfferta, esitoOfferta);
                    offerte.add(offerta);
                }
                return offerte;

            } else {
                throw new RuntimeException("Errore HTTP: " + response.statusCode() + " - " + response.body());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Errore durante richiesta offerte: " + e.getMessage());
        }
    }

    public boolean inviaOfferta(Offerta offerta,String token) {

        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("immobile", offerta.getImmobile());
        bodyMap.put("prezzoOfferta", offerta.getPrezzoOfferta());

        String requestBody = gson.toJson(bodyMap);

        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI(OFFERTA_URL)) // aggiorna se diverso
                    .header("Authorization", "Bearer " + token)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException("URI non valido: " + e.getMessage());
        }

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 201;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Errore durante invio offerta: " + e.getMessage());
        }
    }

    //TODO da spostare in controller clienti

    public boolean inviaVisita(Visita visita, String token) {

        String requestBody = gson.toJson(visita);

        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI(PRENOTA_VISITE)) // aggiorna con l'URL corretto
                    .header("Authorization", "Bearer " + token)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
        } catch (URISyntaxException e) {
            throw new RuntimeException("URI non valido: " + e.getMessage());
        }

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.statusCode() == 201;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException("Errore durante invio visita: " + e.getMessage());
        }
    }

    public String getRawSpecifiche(String token) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(WHO_URL))
                    .header("Authorization", "Bearer " + token.trim())
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Risposta da /whoAmI: [" + response.body() + "]");

            if (response.statusCode() == 200 && response.body() != null && !response.body().isBlank()) {
                String ruolo = response.body().trim(); // Stringa pura, es: "[ADMIN]"
                setNameLogIn(ruolo);
                return ruolo;
            } else {
                throw new RuntimeException("Errore nella richiesta: " + response.statusCode() + " - " + response.body());
            }

        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException("Errore durante il recupero delle specifiche: " + e.getMessage(), e);
        }
    }


    public String getNameLogIn() {
        return nameLogIn;
    }

    public void setNameLogIn(String nameLogIn) {
        this.nameLogIn = nameLogIn;
    }
}
