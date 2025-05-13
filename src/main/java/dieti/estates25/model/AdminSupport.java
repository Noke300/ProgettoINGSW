package dieti.estates25.model;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "adminsupport")
public class AdminSupport {

    @Id
    @Column(name = "emailsupport")
    private String emailSupport;

    @Column(name = "NomeSupport", nullable = false)
    private String nomeSupport;

    @Column(name = "CognomeSupport", nullable = false)
    private String cognomeSupport;

    @Column(name = "PasswordSupport", nullable = false, unique = true)
    private String passwordSupport;

    //@ManyToOne
    @JoinColumn(name = "NomeAgenzia")
    private String nomeAgenzia;

    // Getters and Setters

    public String getEmailSupport() { return this.emailSupport; }
    public void setEmailSupport(String emailSupport) { this.emailSupport = emailSupport; }
    public String getNomeSupport() { return this.nomeSupport; }
    public void setNomeSupport(String nomeSupport) { this.nomeSupport = nomeSupport; }
    public String getCognomeSupport() { return this.cognomeSupport; }
    public void setCognomeSupport(String cognomeSupport) { this.cognomeSupport = cognomeSupport; }
    public String getPasswordSupport() { return this.passwordSupport; }
    public void setPasswordSupport(String passwordSupport) { this.passwordSupport = passwordSupport; }
    public String getNomeAgenzia() { return this.nomeAgenzia; }
    public void setNomeAgenzia(String nomeAgenzia) { this.nomeAgenzia = nomeAgenzia; }
}