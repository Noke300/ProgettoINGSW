package model;

import java.util.ArrayList;

public class AdminSupport {

    private AgenziaImmobiliare nomeAgenzia;
    private String nomeSupport;
    private String cognomeSupport;
    private String emailSupport;
    private String passwordSupport;
    private ArrayList<AgenteImmobiliare> agentiImmobiliari;
    private ArrayList<ImmobileEImmagini> immobiliE;


    public AdminSupport(String nome, String cognome, String email, String password, AgenziaImmobiliare agenziaImmobiliare) {
        this.nomeSupport = nome;
        this.cognomeSupport = cognome;
        this.emailSupport = email;
        this.passwordSupport = password;
        this.nomeAgenzia = agenziaImmobiliare;
        this.agentiImmobiliari = new ArrayList<>();
    }


    public String getNomeSupport() {
        return nomeSupport;
    }

    public String getCognomeSupport() {
        return cognomeSupport;
    }

    public String getEmailSupport() {
        return emailSupport;
    }

    public AgenziaImmobiliare getNomeAgenzia() {
        return nomeAgenzia;
    }

    public ArrayList<AgenteImmobiliare> getAgentiImmobiliari() {
        return agentiImmobiliari;
    }

    public void setAgentiImmobiliari(ArrayList<AgenteImmobiliare> agentiImmobiliari) {
        this.agentiImmobiliari = agentiImmobiliari;
    }

    public ArrayList<ImmobileEImmagini> getImmobiliE() {
        return immobiliE;
    }

    public void setImmobiliE(ArrayList<ImmobileEImmagini> immobiliE) {
        this.immobiliE = immobiliE;
    }
}
