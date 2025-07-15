package model;

import java.util.ArrayList;

public class AgenteImmobiliare{

    private String nomeAgente;
    private String cognomeAgente;
    private String emailAgente;
    private String passwordAgente;
    private String regione;
    private ArrayList<Immobile> listaImmobili = new ArrayList<>();
    private final AgenziaImmobiliare nomeAgenzia;

    public AgenteImmobiliare(AgenziaImmobiliare agenziaImmobiliare, ArrayList<Immobile> listaImmobili, String regione, String passwordAgente, String emailAgente, String cognomeAgente, String nomeAgente) {
        this.nomeAgenzia = agenziaImmobiliare;
        this.listaImmobili = listaImmobili;
        this.regione = regione;
        this.passwordAgente = passwordAgente;
        this.emailAgente = emailAgente;
        this.cognomeAgente = cognomeAgente;
        this.nomeAgente = nomeAgente;
    }
    public AgenteImmobiliare(AgenziaImmobiliare agenziaImmobiliare, String regione, String passwordAgente, String emailAgente, String cognomeAgente, String nomeAgente) {
        this.nomeAgenzia = agenziaImmobiliare;
        this.regione = regione;
        this.passwordAgente = passwordAgente;
        this.emailAgente = emailAgente;
        this.cognomeAgente = cognomeAgente;
        this.nomeAgente = nomeAgente;
    }
    public AgenteImmobiliare(String emailAgente, String passwordAgente) {
        this.emailAgente = emailAgente;
        this.passwordAgente = passwordAgente;
        this.nomeAgenzia = null;
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
        return nomeAgenzia;
    }

    public String getNomeAgente() {
        return nomeAgente;
    }

    public void setNomeAgente(String nomeAgente) {
        this.nomeAgente = nomeAgente;
    }

    public String getCognomeAgente() {
        return cognomeAgente;
    }

    public void setCognomeAgente(String cognomeAgente) {
        this.cognomeAgente = cognomeAgente;
    }

    public String getEmailAgente() {
        return emailAgente;
    }

    public void setEmailAgente(String emailAgente) {
        this.emailAgente = emailAgente;
    }

    public String getPasswordAgente() {
        return passwordAgente;
    }

    public void setPasswordAgente(String passwordAgente) {
        this.passwordAgente = passwordAgente;
    }
}
