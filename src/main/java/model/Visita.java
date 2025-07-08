package model;

import java.time.LocalTime;
import java.util.Date;

public class Visita {

    private Immobile immobile;
    private Cliente cliente;
    private java.time.LocalTime oraVisita;
    private Date dataVisita;
    private Boolean conferma;

    public Visita(Immobile immobile, Cliente cliente, LocalTime oraVisita, Date dataVisita, Boolean conferma) {
        this.immobile = immobile;
        this.cliente = cliente;
        this.oraVisita = oraVisita;
        this.dataVisita = dataVisita;
        this.conferma = conferma;
    }

    public Visita(Immobile immobile, LocalTime oraVisita, Date dataVisita) {
        this.immobile = immobile;

        this.oraVisita = oraVisita;
        this.dataVisita = dataVisita;
        this.conferma = false;
    }

    public Immobile getImmobile() { return this.immobile; }
    public void setImmobile(Immobile immobile) { this.immobile = immobile; }
    public Cliente getCliente() { return this.cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public java.time.LocalTime getOraVisita() { return this.oraVisita; }
    public void setOraVisita(java.time.LocalTime oraVisita) { this.oraVisita = oraVisita; }
    public Date getDataVisita() { return this.dataVisita; }
    public void setDataVisita(Date dataVisita) { this.dataVisita = dataVisita; }

    public void setConferma(Boolean conferma) {this.conferma = conferma;}
    public boolean getConferma() { return this.conferma; }
}