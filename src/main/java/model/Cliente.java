package model;


import java.util.Date;

public class Cliente {

    private String nome;
    private String cognome;
    private String emailCliente;
    private String passwordCliente;
    private Date dataN;

    public Cliente(String nome, String cognome, String email, String password, Date dataN) {
        this.nome = nome;
        this.cognome = cognome;
        this.emailCliente = email;
        this.passwordCliente = password;
        this.dataN = dataN;
    }

    public Date getDataN() {
        return dataN;
    }

    public void setDataN(Date tDataN) {
        this.dataN = tDataN;
    }

    public String getEmailcliente() {
        return emailCliente;
    }

    public String getPasswordcliente() {
        return passwordCliente;
    }

    public String getCognome() {
        return cognome;
    }
    @Override
    public String toString() {
        return getCognome() + " " +getEmailcliente() + " " + getPasswordcliente();
    }
}
