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

public class ControllerClienti extends Controller {

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

}