package controller;

import model.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Controller {

    public Controller() {
        setAdminSupport(null);
        setAgenziaImmobiliare(null);
        setAgenteImmobiliare(null);
        setCliente(null);
        setCasa(null);
        setAppartamento(null);
        setIndirizzo(null);
    }

    private AdminSupport adminSupport;
    private AgenziaImmobiliare agenziaImmobiliare;
    private AgenteImmobiliare agenteImmobiliare;
    private Cliente cliente;
    private Casa casa;
    private Appartamento appartamento;
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

    public Casa getCasa() {
        return casa;
    }

    public void setCasa(Casa casa) {
        this.casa = casa;
    }

    public Appartamento getAppartamento() {
        return appartamento;
    }

    public void setAppartamento(Appartamento appartamento) {
        this.appartamento = appartamento;
    }

    public Indirizzo getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(Indirizzo indirizzo) {
        this.indirizzo = indirizzo;
    }

    public String logInClient(String emailCliente , String password) throws IOException, InterruptedException, URISyntaxException {
        HttpClient client = HttpClient.newHttpClient();

        String requestBody = String.format("""
        {
            "emailCliente": "%s",
            "passwordCliente": "%s"
        }
        """, emailCliente, password);


        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8080/auth-cliente/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
      System.out.println(response.statusCode()+"  "+response.body());
        if(response.statusCode()!=200){
          System.out.println("Errore log in");
        return "";
      }
        return response.body();
    }

}
