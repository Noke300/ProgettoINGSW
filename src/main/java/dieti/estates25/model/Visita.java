package dieti.estates25.model;

import dieti.estates25.model.cliente.Cliente;
import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "Visita")
public class Visita {

    @Id
    @ManyToOne
    @JoinColumn(name = "IdImmobile")
    private Immobile immobile;

    @Id
    @ManyToOne
    @JoinColumn(name = "NomeCliente")
    private Cliente cliente;

    @Column(name = "OraVisita", nullable = false)
    private java.time.LocalTime oraVisita;

    @Column(name = "DataVisita", nullable = false)
    private Date dataVisita;

    // Getters and Setters

    public Immobile getImmobile() { return this.immobile; }
    public void setImmobile(Immobile immobile) { this.immobile = immobile; }
    public Cliente getCliente() { return this.cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public java.time.LocalTime getOraVisita() { return this.oraVisita; }
    public void setOraVisita(java.time.LocalTime oraVisita) { this.oraVisita = oraVisita; }
    public Date getDataVisita() { return this.dataVisita; }
    public void setDataVisita(Date dataVisita) { this.dataVisita = dataVisita; }
}