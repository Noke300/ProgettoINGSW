package model;

import org.jetbrains.annotations.Nullable;

import java.util.Date;

public class Offerta {



    private Immobile immobile;

    private String nominativo;

    @Nullable
    private Cliente cliente;

    private Double prezzoOfferta;

    private Date dataOfferta;

    @Nullable
    private Boolean esitoOfferta;

    private Boolean contrOfferta;

    public Offerta(Immobile immobile,Double prezzoOfferta) {
        this.immobile = immobile;
        this.prezzoOfferta = prezzoOfferta;
    }

    public Offerta(@Nullable Cliente cliente, Date dataOfferta, Double prezzoOfferta, String nominativo, Immobile immobile, Boolean contrOfferta) {
        this.esitoOfferta = null;
        this.cliente = cliente;
        this.dataOfferta = dataOfferta;
        this.prezzoOfferta = prezzoOfferta;
        this.nominativo = cliente.getCognome();
        this.immobile = immobile;
        this.contrOfferta = contrOfferta;
    }

    public Immobile getImmobile() {
        return immobile;
    }

    public void setImmobile(Immobile immobile) {
        this.immobile = immobile;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Double getPrezzoOfferta() {
        return prezzoOfferta;
    }

    public void setPrezzoOfferta(Double prezzoOfferta) {
        this.prezzoOfferta = prezzoOfferta;
    }

    public Date getDataOfferta() {
        return dataOfferta;
    }

    public void setDataOfferta(Date dataOfferta) {
        this.dataOfferta = dataOfferta;
    }

    public Boolean getEsitoOfferta() {
        return esitoOfferta;
    }

    public void setEsitoOfferta(Boolean esitoOfferta) {
        this.esitoOfferta = esitoOfferta;
    }

    public String getNominativo() {
        return nominativo;
    }

    public void setNominativo(String nominativo) {
        this.nominativo = nominativo;
    }

    public Boolean getContrOfferta() {
        return contrOfferta;
    }

    public void setContrOfferta(Boolean contrOfferta) {
        this.contrOfferta = contrOfferta;
    }
}
