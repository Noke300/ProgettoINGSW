package dieti.estates25.model.agenteimmobiliare;

import dieti.estates25.model.AgenziaImmobiliare;
import jakarta.persistence.*;

@Entity
@Table(name = "agenteimmobiliare")
public class AgenteImmobiliare {

    @Id
    @Column(name = "emailagente")
    private String emailAgente;

    @Column(name = "nomeagente", nullable = false)
    private String nomeAgente;

    @Column(name = "cognomeagente", nullable = false)
    private String cognomeAgente;

    @Column(name = "passwordagente", nullable = false, unique = true)
    private String passwordAgente;

    @ManyToOne
    @JoinColumn(name = "nomeagenzia")
    private AgenziaImmobiliare nomeAgenzia;

    // Getters and Setters

    public String getEmailAgente() { return this.emailAgente; }
    public void setEmailAgente(String emailAgente) { this.emailAgente = emailAgente; }
    public String getNomeAgente() { return this.nomeAgente; }
    public void setNomeAgente(String nomeAgente) { this.nomeAgente = nomeAgente; }
    public String getCognomeAgente() { return this.cognomeAgente; }
    public void setCognomeAgente(String cognomeAgente) { this.cognomeAgente = cognomeAgente; }
    public String getPasswordAgente() { return this.passwordAgente; }
    public void setPasswordAgente(String passwordAgente) { this.passwordAgente = passwordAgente; }
    public AgenziaImmobiliare getNomeAgenzia() { return this.nomeAgenzia; }
    public void setNomeAgenzia(AgenziaImmobiliare nomeAgenzia) { this.nomeAgenzia = nomeAgenzia; }
}