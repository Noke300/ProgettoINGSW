package dieti.estates25.model;

import dieti.estates25.model.agenteimmobiliare.AgenteImmobiliare;
import jakarta.persistence.*;


@Entity
@Table(name = "immobile")
public class Immobile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idimmobile")
    private String idImmobile;

    @ManyToOne
    @JoinColumn(name = "idindirizzo")
    private Indirizzo indirizzo;

    @ManyToOne
    @JoinColumn(name = "nomeagente")
    private AgenteImmobiliare agente;

    @Column(name = "nomeagenzia", nullable = false)
    private String nomeAgenzia;

    @Column(name = "prezzo", nullable = false)
    private Double prezzo;

    @Column(name = "tipovendita", nullable = false)
    private String tipoVendita;

    @Column(name = "superficie", nullable = false)
    private int superficie;

    @Column(name = "tipoimmobile", nullable = false)
    private String tipoImmobile;

    @Column(name = "numbagni", nullable = false)
    private int numBagni;

    @Column(name = "classeenergetica", nullable = false)
    private String classeEnergetica;

    @Column(name = "numstanze", nullable = false)
    private int numStanze;

    @Column(name = "piano")
    private int piano;

    @Column(name = "ascensore")
    private Boolean ascensore;

    @Column(name = "balcone")
    private Boolean balcone;

    @Column(name = "cantina")
    private Boolean cantina;

    @Column(name = "portineria")
    private Boolean portineria;

    @Column(name = "climatizzatione")
    private Boolean climatizzazione;

    @Column(name = "venduto")
    private Boolean venduto;

    // Getters and Setters

    public String getIdImmobile() { return this.idImmobile; }
    public void setIdImmobile(String idImmobile) { this.idImmobile = idImmobile; }
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
    public int getPiano() { return this.piano; }
    public void setPiano(int piano) { this.piano = piano; }
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

    public Indirizzo getIndirizzo() {
        return indirizzo;
    }
}