package model;

import java.util.ArrayList;

public class AgenteImmobiliare extends Utente{

    private String regione;
    private ArrayList<Immobile> listaImmobili = new ArrayList<>();
    private final AgenziaImmobiliare agenziaImmobiliare;

    public AgenteImmobiliare(String nome, String cognome, String email, String password, AgenziaImmobiliare agenziaImmobiliare, String regione) {
        super(nome, cognome, email, password);
        this.regione = regione;
        this.agenziaImmobiliare = agenziaImmobiliare;
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
