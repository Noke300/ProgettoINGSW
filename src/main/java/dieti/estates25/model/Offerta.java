package dieti.estates25.model;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "Offerta")
public class Offerta {

    @Id
    @ManyToOne
    @JoinColumn(name = "IdImmobile")
    private Immobile immobile;

    @Id
    @ManyToOne
    @JoinColumn(name = "NomeCliente")
    private Cliente cliente;

    @Column(name = "PrezzoOfferta", nullable = false)
    private Double prezzoOfferta;

    @Column(name = "DataOfferta", nullable = false)
    private Date dataOfferta;

    @Column(name = "EsitoOfferta")
    private Boolean esitoOfferta;

    // Getters and Setters

    public Immobile getImmobile() { return this.immobile; }
    public void setImmobile(Immobile immobile) { this.immobile = immobile; }
    public Cliente getCliente() { return this.cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public Double getPrezzoOfferta() { return this.prezzoOfferta; }
    public void setPrezzoOfferta(Double prezzoOfferta) { this.prezzoOfferta = prezzoOfferta; }
    public Date getDataOfferta() { return this.dataOfferta; }
    public void setDataOfferta(Date dataOfferta) { this.dataOfferta = dataOfferta; }
    public Boolean getEsitoOfferta() { return this.esitoOfferta; }
    public void setEsitoOfferta(Boolean esitoOfferta) { this.esitoOfferta = esitoOfferta; }
}