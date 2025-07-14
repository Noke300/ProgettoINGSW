package model;

public class AdminSupport {
    private final AgenziaImmobiliare nomeAgenzia;

    private String nomeSupport;
    private String cognomeSupport;
    private String emailSupport;
    private String passwordSupport;

    public AdminSupport(String nome, String cognome, String email, String password, AgenziaImmobiliare agenziaImmobiliare) {
        this.nomeSupport = nome;
        this.cognomeSupport = cognome;
        this.emailSupport = email;
        this.passwordSupport = password;
        this.nomeAgenzia = agenziaImmobiliare;
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

    public String getPasswordSupport() {
        return passwordSupport;
    }
}
