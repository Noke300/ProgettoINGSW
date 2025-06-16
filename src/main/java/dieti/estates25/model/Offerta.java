package dieti.estates25.model;

import dieti.estates25.model.cliente.Cliente;
import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "offerta")
public class Offerta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idofferta")
    private String idOfferta;

    @ManyToOne
    @JoinColumn(name = "idimmobile")
    private Immobile immobile;

    @ManyToOne
    @JoinColumn(name = "emailcliente")
    private Cliente cliente;

    @Column(name = "prezzoofferta", nullable = false)
    private Double prezzoOfferta;

    @Column(name = "dataofferta", nullable = false)
    private Date dataOfferta;

    @Column(name = "esitoofferta")
    private Boolean esitoOfferta;

    // Getters and Setters

    public String getIdOfferta() { return idOfferta; }
    public void setIdOfferta(String idOfferta) { this.idOfferta = idOfferta; }
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

    @Override
    public String toString() {
        return idOfferta + " " + immobile + " " + cliente + " " + prezzoOfferta + " " + dataOfferta;
    }
}