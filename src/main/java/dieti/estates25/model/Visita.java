package dieti.estates25.model;

import dieti.estates25.model.cliente.Cliente;
import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "visita")
public class Visita {

    @EmbeddedId
    private VisitaId id;

    @ManyToOne
    @MapsId("immobileId")
    @JoinColumn(name = "idimmobile")
    private Immobile immobile;

    @ManyToOne
    @MapsId("clienteId")
    @JoinColumn(name = "emailcliente")
    private Cliente cliente;

    @Column(name = "oravisita", nullable = false)
    private java.time.LocalTime oraVisita;

    @Column(name = "datavisita", nullable = false)
    private Date dataVisita;

    @Column(name = "conferma")
    private Boolean conferma;

    // Getters and Setters


    public VisitaId getId() {
        return id;
    }
    public void setId(VisitaId id) {this.id = id;}
    public void setConferma(Boolean conferma) {this.conferma = conferma;}
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