package model;

import java.util.ArrayList;

public class AgenziaImmobiliare {
    private String nomeAgenzia;
    private String emailAgenzia;
    private String emailAdmin;
    private String passwordAdmin;
    private String regione;

    private ArrayList<AgenteImmobiliare> agentiImmobiliari = new ArrayList<>();
    private ArrayList<AdminSupport> adminSupport = new ArrayList<>();
    private ArrayList<Immobile> listaImmobili = new ArrayList<>();

    public AgenziaImmobiliare(String nomeAgenzia, String emailAgenzia, String emailAdmin, String passwordAdmin,String regione) {
        this.nomeAgenzia = nomeAgenzia;
        this.emailAgenzia = emailAgenzia;
        this.emailAdmin = emailAdmin;
        this.passwordAdmin = passwordAdmin;
        this.regione = regione;
    }

    public ArrayList<AgenteImmobiliare> getAgentiImmobiliari() {
        return agentiImmobiliari;
    }

    public void setAgentiImmobiliari(ArrayList<AgenteImmobiliare> agentiImmobiliari) {
        this.agentiImmobiliari = agentiImmobiliari;
    }

    public ArrayList<AdminSupport> getAdminSupport() {
        return adminSupport;
    }

    public void setAdminSupport(ArrayList<AdminSupport> adminSupport) {
        this.adminSupport = adminSupport;
    }

    public ArrayList<Immobile> getListaImmobili() {
        return listaImmobili;
    }

    public void setListaImmobili(ArrayList<Immobile> listaImmobili) {
        this.listaImmobili = listaImmobili;
    }

    public String getNomeAgenzia() {
        return nomeAgenzia;
    }

}
