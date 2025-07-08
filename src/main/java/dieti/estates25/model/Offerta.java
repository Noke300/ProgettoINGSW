package dieti.estates25.model;

import dieti.estates25.model.cliente.Cliente;
import jakarta.persistence.*;

import java.time.LocalDateTime;
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
    private LocalDateTime dataOfferta;

    @Column(name = "esitoofferta")
    private Boolean esitoOfferta;

    @Column(name = "controfferta")
    private Boolean contrOfferta;

    // Getters and Setters

    public String getIdOfferta() { return idOfferta; }
    public void setIdOfferta(String idOfferta) { this.idOfferta = idOfferta; }
    public Immobile getImmobile() { return this.immobile; }
    public void setImmobile(Immobile immobile) { this.immobile = immobile; }
    public Cliente getCliente() { return this.cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public Double getPrezzoOfferta() { return this.prezzoOfferta; }
    public void setPrezzoOfferta(Double prezzoOfferta) { this.prezzoOfferta = prezzoOfferta; }
    public LocalDateTime getDataOfferta() { return this.dataOfferta; }
    public void setDataOfferta(LocalDateTime dataOfferta) { this.dataOfferta = dataOfferta; }
    public Boolean getEsitoOfferta() { return this.esitoOfferta; }
    public void setEsitoOfferta(Boolean esitoOfferta) { this.esitoOfferta = esitoOfferta; }
    public Boolean getContrOfferta() {
        return contrOfferta;
    }
    public void setContrOfferta(Boolean contrOfferta) {
        this.contrOfferta = contrOfferta;
    }

    @Override
    public String toString() {
        return idOfferta + " " + immobile + " " + cliente + " " + prezzoOfferta + " " + dataOfferta;
    }
}