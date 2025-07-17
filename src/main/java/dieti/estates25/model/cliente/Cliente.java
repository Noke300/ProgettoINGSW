package dieti.estates25.model.cliente;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "cliente")
public class Cliente {

    @Id
    @Column(name = "emailcliente")
    private String emailCliente;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "cognome", nullable = false)
    private String cognome;

    @Column(name = "passwordcliente", nullable = false)
    private String passwordCliente;

    @Column(name = "datan", nullable = false)
    private Date dataN;

    public Cliente(String emailCliente, String nome, String cognome, String passwordCliente, Date dataN) {
        this.emailCliente = emailCliente;
        this.nome = nome;
        this.cognome = cognome;
        this.passwordCliente = passwordCliente;
        this.dataN = dataN;
    }

    public Cliente() {}

    public String getEmailCliente() { return this.emailCliente; }
    public void setEmailCliente(String emailCliente) { this.emailCliente = emailCliente; }
    public String getNome() { return this.nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCognome() { return this.cognome; }
    public void setCognome(String cognome) { this.cognome = cognome; }
    public String getPasswordCliente() { return this.passwordCliente; }
    public void setPasswordCliente(String passwordCliente) { this.passwordCliente = passwordCliente; }
    public Date getDataN() { return this.dataN; }
    public void setDataN(Date dataN) { this.dataN = dataN; }
}