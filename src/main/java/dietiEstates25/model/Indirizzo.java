package dietiEstates25.model;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "Indirizzo")
public class Indirizzo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdIndirizzo")
    private Long idIndirizzo;

    @Column(name = "Cap", nullable = false)
    private int cap;

    @Column(name = "Provincia", nullable = false)
    private String provincia;

    @Column(name = "Citta", nullable = false)
    private String citta;

    @Column(name = "Via", nullable = false)
    private String via;

    @Column(name = "NumeroCivico", nullable = false)
    private String numeroCivico;

    @Column(name = "Regione", nullable = false)
    private String regione;

    // Getters and Setters

    public Long getIdIndirizzo() { return this.idIndirizzo; }
    public void setIdIndirizzo(Long idIndirizzo) { this.idIndirizzo = idIndirizzo; }
    public int getCap() { return this.cap; }
    public void setCap(int cap) { this.cap = cap; }
    public String getProvincia() { return this.provincia; }
    public void setProvincia(String provincia) { this.provincia = provincia; }
    public String getCitta() { return this.citta; }
    public void setCitta(String citta) { this.citta = citta; }
    public String getVia() { return this.via; }
    public void setVia(String via) { this.via = via; }
    public String getNumeroCivico() { return this.numeroCivico; }
    public void setNumeroCivico(String numeroCivico) { this.numeroCivico = numeroCivico; }
    public String getRegione() { return this.regione; }
    public void setRegione(String regione) { this.regione = regione; }
}