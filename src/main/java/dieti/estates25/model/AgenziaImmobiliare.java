package dieti.estates25.model;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "AgenziaImmobiliare")
public class AgenziaImmobiliare {

    @Id
    @Column(name = "NomeAgenzia")
    private String nomeAgenzia;

    @Column(name = "EmailAgenzia", nullable = false, unique = true)
    private String emailAgenzia;

    @Column(name = "EmailAdmin", nullable = false, unique = true)
    private String emailAdmin;

    @Column(name = "PasswordAdmin", nullable = false)
    private String passwordAdmin;

    @Column(name = "Regione", nullable = false)
    private String regione;

    // Getters and Setters

    public String getNomeAgenzia() { return this.nomeAgenzia; }
    public void setNomeAgenzia(String nomeAgenzia) { this.nomeAgenzia = nomeAgenzia; }
    public String getEmailAgenzia() { return this.emailAgenzia; }
    public void setEmailAgenzia(String emailAgenzia) { this.emailAgenzia = emailAgenzia; }
    public String getEmailAdmin() { return this.emailAdmin; }
    public void setEmailAdmin(String emailAdmin) { this.emailAdmin = emailAdmin; }
    public String getPasswordAdmin() { return this.passwordAdmin; }
    public void setPasswordAdmin(String passwordAdmin) { this.passwordAdmin = passwordAdmin; }
    public String getRegione() { return this.regione; }
    public void setRegione(String regione) { this.regione = regione; }
}