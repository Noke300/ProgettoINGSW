package model;

import java.util.Date;

public class Visita {

    private Immobile immobile;


    private Cliente cliente;


    private java.time.LocalTime oraVisita;


    private Date dataVisita;


    private Boolean conferma;

    // Getters and Setters

    public Immobile getImmobile() { return this.immobile; }
    public void setImmobile(Immobile immobile) { this.immobile = immobile; }
    public Cliente getCliente() { return this.cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public java.time.LocalTime getOraVisita() { return this.oraVisita; }
    public void setOraVisita(java.time.LocalTime oraVisita) { this.oraVisita = oraVisita; }
    public Date getDataVisita() { return this.dataVisita; }
    public void setDataVisita(Date dataVisita) { this.dataVisita = dataVisita; }
    public boolean getConferma() { return this.conferma; }
}