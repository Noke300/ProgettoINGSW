package dieti.estates25.model;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "AgenteImmobiliare")
public class AgenteImmobiliare {

    @Id
    @Column(name = "EmailAgente")
    private String emailAgente;

    @Column(name = "NomeAgente", nullable = false)
    private String nomeAgente;

    @Column(name = "CognomeAgente", nullable = false)
    private String cognomeAgente;

    @Column(name = "PasswordAgente", nullable = false, unique = true)
    private String passwordAgente;

    //@ManyToOne
    @JoinColumn(name = "NomeAgenzia")
    private String nomeAgenzia;

    // Getters and Setters

    public String getEmailAgente() { return this.emailAgente; }
    public void setEmailAgente(String emailAgente) { this.emailAgente = emailAgente; }
    public String getNomeAgente() { return this.nomeAgente; }
    public void setNomeAgente(String nomeAgente) { this.nomeAgente = nomeAgente; }
    public String getCognomeAgente() { return this.cognomeAgente; }
    public void setCognomeAgente(String cognomeAgente) { this.cognomeAgente = cognomeAgente; }
    public String getPasswordAgente() { return this.passwordAgente; }
    public void setPasswordAgente(String passwordAgente) { this.passwordAgente = passwordAgente; }
    public String getNomeAgenzia() { return this.nomeAgenzia; }
    public void setNomeAgenzia(String nomeAgenzia) { this.nomeAgenzia = nomeAgenzia; }
}