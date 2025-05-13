package dieti.estates25.model;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "agenziaimmobiliare")
public class AgenziaImmobiliare {

    @Id
    @Column(name = "nomeagenzia")
    private String nomeAgenzia;

    @Column(name = "emailagenzia", nullable = false, unique = true)
    private String emailAgenzia;

    @Column(name = "emailadmin", nullable = false, unique = true)
    private String emailAdmin;

    @Column(name = "passwordadmin", nullable = false)
    private String passwordAdmin;

    @Column(name = "regione", nullable = false)
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