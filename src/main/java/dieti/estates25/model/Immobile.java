package dieti.estates25.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Immobile")
public class Immobile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdImmobile")
    private Long idImmobile;

    @ManyToOne
    @JoinColumn(name = "IdIndirizzo")
    private Indirizzo indirizzo;

    @ManyToOne
    @JoinColumn(name = "NomeAgente")
    private AgenteImmobiliare agente;

    @Column(name = "NomeAgenzia", nullable = false)
    private String nomeAgenzia;

    @Column(name = "Prezzo", nullable = false)
    private Double prezzo;

    @Column(name = "TipoVendita", nullable = false)
    private String tipoVendita;

    @Column(name = "Superficie", nullable = false)
    private int superficie;

    @Column(name = "TipoImmobile", nullable = false)
    private String tipoImmobile;

    @Column(name = "NumBagni", nullable = false)
    private int numBagni;

    @Column(name = "ClasseEnergetica", nullable = false)
    private String classeEnergetica;

    @Column(name = "NumStanze", nullable = false)
    private int numStanze;

    @Column(name = "Piano")
    private Boolean piano;

    @Column(name = "Ascensore")
    private Boolean ascensore;

    @Column(name = "Balcone")
    private Boolean balcone;

    @Column(name = "Cantina")
    private Boolean cantina;

    @Column(name = "Portineria")
    private Boolean portineria;

    @Column(name = "Climatizzatione")
    private Boolean climatizzazione;

    @Column(name = "Venduto")
    private Boolean venduto;

    // Getters and Setters

    public Long getIdImmobile() { return this.idImmobile; }
    public void setIdImmobile(Long idImmobile) { this.idImmobile = idImmobile; }
    public Indirizzo getIndirizzo() { return this.indirizzo; }
    public void setIndirizzo(Indirizzo indirizzo) { this.indirizzo = indirizzo; }
    public AgenteImmobiliare getAgente() { return this.agente; }
    public void setAgente(AgenteImmobiliare agente) { this.agente = agente; }
    public String getNomeAgenzia() { return this.nomeAgenzia; }
    public void setNomeAgenzia(String nomeAgenzia) { this.nomeAgenzia = nomeAgenzia; }
    public Double getPrezzo() { return this.prezzo; }
    public void setPrezzo(Double prezzo) { this.prezzo = prezzo; }
    public String getTipoVendita() { return this.tipoVendita; }
    public void setTipoVendita(String tipoVendita) { this.tipoVendita = tipoVendita; }
    public int getSuperficie() { return this.superficie; }
    public void setSuperficie(int superficie) { this.superficie = superficie; }
    public String getTipoImmobile() { return this.tipoImmobile; }
    public void setTipoImmobile(String tipoImmobile) { this.tipoImmobile = tipoImmobile; }
    public int getNumBagni() { return this.numBagni; }
    public void setNumBagni(int numBagni) { this.numBagni = numBagni; }
    public String getClasseEnergetica() { return this.classeEnergetica; }
    public void setClasseEnergetica(String classeEnergetica) { this.classeEnergetica = classeEnergetica; }
    public int getNumStanze() { return this.numStanze; }
    public void setNumStanze(int numStanze) { this.numStanze = numStanze; }
    public Boolean getPiano() { return this.piano; }
    public void setPiano(Boolean piano) { this.piano = piano; }
    public Boolean getAscensore() { return this.ascensore; }
    public void setAscensore(Boolean ascensore) { this.ascensore = ascensore; }
    public Boolean getBalcone() { return this.balcone; }
    public void setBalcone(Boolean balcone) { this.balcone = balcone; }
    public Boolean getCantina() { return this.cantina; }
    public void setCantina(Boolean cantina) { this.cantina = cantina; }
    public Boolean getPortineria() { return this.portineria; }
    public void setPortineria(Boolean portineria) { this.portineria = portineria; }
    public Boolean getClimatizzazione() { return this.climatizzazione; }
    public void setClimatizzazione(Boolean climatizzazione) { this.climatizzazione = climatizzazione; }
    public Boolean getVenduto() { return this.venduto; }
    public void setVenduto(Boolean venduto) { this.venduto = venduto; }
}