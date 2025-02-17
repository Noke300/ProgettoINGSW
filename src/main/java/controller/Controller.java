package controller;

import model.*;

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
}
