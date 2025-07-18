package model;

import java.util.ArrayList;

public class AgenziaImmobiliare {
    private String nomeAgenzia;
    private String emailAgenzia;
    private String emailAdmin;
    private String passwordAdmin;
    private String regione;

    private ArrayList<AgenteImmobiliare> agentiImmobiliari;
    private ArrayList<AdminSupport> adminSupport;
    private ArrayList<ImmobileEImmagini> listaImmobili ;

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

    public ArrayList<ImmobileEImmagini> getListaImmobili() {
        return listaImmobili;
    }

    public void setListaImmobili(ArrayList<ImmobileEImmagini> listaImmobili) {
        this.listaImmobili = listaImmobili;
    }

    public String getPasswordAdmin() {
        return passwordAdmin;
    }

    public void setPasswordAdmin(String passwordAdmin) {
        this.passwordAdmin = passwordAdmin;
    }

    public String getRegione() {
        return regione;
    }

    public String getNomeAgenzia() {
        return nomeAgenzia;
    }

}
