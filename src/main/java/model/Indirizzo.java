package model;

public class Indirizzo {
    private String via;
    private String citta;
    private String provincia;
    private int numeroCivico;
    private int cap;
    private String regione;
    private Double latitudine;
    private Double longitudine;

    public Indirizzo(String via, String citta, String provincia, int numeroCivico, int cap,
                     String regione, Double latitudine, Double longitudine) {
        this.via = via;
        this.citta = citta;
        this.provincia = provincia;
        this.numeroCivico = numeroCivico;
        this.cap = cap;
        this.regione = regione;
        this.latitudine = latitudine;
        this.longitudine = longitudine;
    }

    public String getVia() {
        return via;
    }

    public void setVia(String via) {
        this.via = via;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getCitta() {
        return citta;
    }

    public void setCitta(String citta) {
        this.citta = citta;
    }

    public int getCap() {
        return cap;
    }

    public void setCap(int cap) {
        this.cap = cap;
    }

    public int getNumeroCivico() {
        return numeroCivico;
    }

    public void setNumeroCivico(int numeroCivico) {
        this.numeroCivico = numeroCivico;
    }

    public String getRegione() {
        return regione;
    }

    public void setRegione(String regione) {
        this.regione = regione;
    }

    public Double getLatitudine() {
        return latitudine;
    }

    public void setLatitudine(Double latitudine) {
        this.latitudine = latitudine;
    }

    public Double getLongitudine() {
        return longitudine;
    }

    public void setLongitudine(Double longitudine) {
        this.longitudine = longitudine;
    }

    @Override
    public String toString() {
        return "Indirizzo : "+ via +" "+ citta + " " + provincia + " "+ numeroCivico + " " + cap + " " + regione + " ";
    }
}
