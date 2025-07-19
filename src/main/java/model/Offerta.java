package model;

import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.Date;

public class Offerta {

    private String idOfferta;

    private Immobile immobile;

    private String nominativo;

    @Nullable
    private Cliente cliente;

    private Double prezzoOfferta;

    private LocalDateTime dataOfferta;

    @Nullable
    private Boolean esitoOfferta;

    @Nullable
    private Boolean contrOfferta;

    public Offerta(Immobile immobile,Double prezzoOfferta) {
        this.immobile = immobile;
        this.prezzoOfferta = prezzoOfferta;
        this.esitoOfferta = null;
    }
    public Offerta(String id,boolean contrOfferta){
        this.idOfferta = id;
        this.contrOfferta = contrOfferta;
    }
    public Offerta(String id, @Nullable Cliente cliente, LocalDateTime dataOfferta, Double prezzoOfferta, Immobile immobile, Boolean contrOfferta, Boolean esitoOfferta) {
        this.idOfferta = id;
        this.cliente = cliente;
        this.dataOfferta = dataOfferta;
        this.prezzoOfferta = prezzoOfferta;
        this.immobile = immobile;
        this.contrOfferta = contrOfferta;
        this.esitoOfferta = esitoOfferta;
    }

    public Offerta(Offerta offerta){
        this.idOfferta = offerta.getIdOfferta();
        this.cliente = offerta.getCliente();
        this.dataOfferta = offerta.getDataOfferta();
        this.prezzoOfferta = offerta.getPrezzoOfferta();
        this.immobile = offerta.getImmobile();
        this.contrOfferta = offerta.getContrOfferta();
        this.esitoOfferta = offerta.getEsitoOfferta();
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

    public LocalDateTime getDataOfferta() {
        return dataOfferta;
    }

    public void setDataOfferta(LocalDateTime dataOfferta) {
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

    public String getIdOfferta() {
        return idOfferta;
    }

    public void setIdOfferta(String idOfferta) {
        this.idOfferta = idOfferta;
    }

    public void setContrOfferta(@Nullable Boolean contrOfferta) {
        this.contrOfferta = contrOfferta;
    }
}
