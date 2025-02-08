package model;

import java.util.ArrayList;

public class AgenziaImmobiliare implements Amministrazione {
    private String nomeAgenzia;
    private String emailAgenzia;
    private String emailAdmin;
    private String passwordAdmin;

    private ArrayList<Utente> agentiImmobiliari = new ArrayList<>();
    private ArrayList<Utente> adminSupport = new ArrayList<>();
    private ArrayList<Immobile> listaImmobili = new ArrayList<>();

    public AgenziaImmobiliare(String nomeAgenzia, String emailAgenzia, String emailAdmin, String passwordAdmin) {
        this.nomeAgenzia = nomeAgenzia;
        this.emailAgenzia = emailAgenzia;
        this.emailAdmin = emailAdmin;
        this.passwordAdmin = passwordAdmin;
    }

    public ArrayList<Utente> getAgentiImmobiliari() {
        return agentiImmobiliari;
    }

    public void setAgentiImmobiliari(ArrayList<Utente> agentiImmobiliari) {
        this.agentiImmobiliari = agentiImmobiliari;
    }

    public ArrayList<Utente> getAdminSupport() {
        return adminSupport;
    }

    public void setAdminSupport(ArrayList<Utente> adminSupport) {
        this.adminSupport = adminSupport;
    }

    public String getNomeAgenzia() {
        return nomeAgenzia;
    }

    public void setNomeAgenzia(String nomeAgenzia) {
        this.nomeAgenzia = nomeAgenzia;
    }

    public String getEmailAgenzia() {
        return emailAgenzia;
    }

    public void setEmailAgenzia(String emailAgenzia) {
        this.emailAgenzia = emailAgenzia;
    }

    public String getEmailAdmin() {
        return emailAdmin;
    }

    public void setEmailAdmin(String emailAdmin) {
        this.emailAdmin = emailAdmin;
    }

    public String getPasswordAdmin() {
        return passwordAdmin;
    }

    public void setPasswordAdmin(String passwordAdmin) {
        this.passwordAdmin = passwordAdmin;
    }

    public ArrayList<Immobile> getListaImmobili() {
        return listaImmobili;
    }

    public void setListaImmobili(ArrayList<Immobile> listaImmobili) {
        this.listaImmobili = listaImmobili;
    }

    @Override
    public void visualizzaDipendenti() {
        // TODO document why this method is empty
    }

    @Override
    public void assumiDipendente() {
        // TODO document why this method is empty
    }

    @Override
    public void licenziaDipendente() {
        // TODO document why this method is empty
    }

    @Override
    public void visualizzaImmobili() {
        // TODO document why this method is empty
    }

    @Override
    public void assegnaImmobile() {
        // TODO document why this method is empty
    }
}
