package model;


import java.util.Date; //sql

public class Cliente extends Utente {
    private Date dataN;

    public Cliente(String nome, String cognome, String email, String password, Date dataN) {
        super(nome, cognome, email, password);
        this.dataN = dataN;
    }

    public Date getDataN() {
        return dataN;
    }

    public void setDataN(Date tDataN) {
        this.dataN = tDataN;
    }
}
