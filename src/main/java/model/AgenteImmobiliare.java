package model;

import java.util.ArrayList;

public class AgenteImmobiliare{

    private String nomeAgente;
    private String cognomeAgente;
    private String emailAgente;
    private String passwordAgente;
    private String regione;
    private ArrayList<Immobile> listaImmobili = new ArrayList<>();
    private final AgenziaImmobiliare agenziaImmobiliare;

    public AgenteImmobiliare(AgenziaImmobiliare agenziaImmobiliare, ArrayList<Immobile> listaImmobili, String regione, String passwordAgente, String emailAgente, String cognomeAgente, String nomeAgente) {
        this.agenziaImmobiliare = agenziaImmobiliare;
        this.listaImmobili = listaImmobili;
        this.regione = regione;
        this.passwordAgente = passwordAgente;
        this.emailAgente = emailAgente;
        this.cognomeAgente = cognomeAgente;
        this.nomeAgente = nomeAgente;
    }
    public AgenteImmobiliare(String emailAgente, String passwordAgente) {
        this.emailAgente = emailAgente;
        this.passwordAgente = passwordAgente;
        this.agenziaImmobiliare = null;
    }

    public String getRegione() {
        return regione;
    }

    public void setRegione(String regione) {
        this.regione = regione;
    }

    public ArrayList<Immobile> getListaImmobili() {
        return listaImmobili;
    }

    public void setListaImmobili(ArrayList<Immobile> listaImmobili) {
        this.listaImmobili = listaImmobili;
    }

    public AgenziaImmobiliare getAgenziaImmobiliare() {
        return agenziaImmobiliare;
    }

}
