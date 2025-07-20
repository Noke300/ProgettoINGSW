package dieti.estates25.model;

import dieti.estates25.model.agenteimmobiliare.AgenteImmobiliare;
import dieti.estates25.model.agenziaimmobiliare.AgenziaImmobiliare;
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

    @ManyToOne
    @JoinColumn(name = "nomeagenzia", nullable = false)
    private AgenziaImmobiliare nomeAgenzia;

    @Column(name = "prezzo", nullable = false)
    private double prezzo;

    @Column(name = "tipovendita", nullable = false)
    private String tipoVendita;

    @Column(name = "descrizione", nullable = false)
    private String descrizione;

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
    private boolean ascensore;

    @Column(name = "balcone")
    private boolean balcone;

    @Column(name = "cantina")
    private boolean cantina;

    @Column(name = "portineria")
    private boolean portineria;

    @Column(name = "climatizzazione")
    private boolean climatizzazione;

    @Column(name = "venduto")
    private boolean venduto;

    @Column(name = "scuole")
    private boolean scuole;

    @Column(name = "parchi")
    private boolean parchi;

    @Column(name = "trasportopubblico")
    private boolean trasportoPubblico;

    // Getters and Setters

    public String getIdImmobile() { return this.idImmobile; }
    public void setIdImmobile(String idImmobile) { this.idImmobile = idImmobile; }
    public AgenteImmobiliare getAgente() { return this.agente; }
    public void setAgente(AgenteImmobiliare agente) { this.agente = agente; }
    public AgenziaImmobiliare getNomeAgenzia() { return this.nomeAgenzia; }
    public void setNomeAgenzia(AgenziaImmobiliare nomeAgenzia) { this.nomeAgenzia = nomeAgenzia; }
    public double getPrezzo() { return this.prezzo; }
    public void setPrezzo(double prezzo) { this.prezzo = prezzo; }
    public String getTipoVendita() { return this.tipoVendita; }
    public void setTipoVendita(String tipoVendita) { this.tipoVendita = tipoVendita; }
    public String getDescrizione() {return this.descrizione; }
    public void setDescrizione(String descrizione) {this.descrizione = descrizione; }
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
    public boolean getAscensore() { return this.ascensore; }
    public void setAscensore(boolean ascensore) { this.ascensore = ascensore; }
    public boolean getBalcone() { return this.balcone; }
    public void setBalcone(boolean balcone) { this.balcone = balcone; }
    public boolean getCantina() { return this.cantina; }
    public void setCantina(boolean cantina) { this.cantina = cantina; }
    public boolean getPortineria() { return this.portineria; }
    public void setPortineria(boolean portineria) { this.portineria = portineria; }
    public boolean getClimatizzazione() { return this.climatizzazione; }
    public void setClimatizzazione(boolean climatizzazione) { this.climatizzazione = climatizzazione; }
    public boolean getVenduto() { return this.venduto; }
    public void setVenduto(boolean venduto) { this.venduto = venduto; }
    public boolean getScuole() { return this.scuole; }
    public void setScuole(boolean scuole) { this.scuole = scuole; }
    public boolean getParchi() { return this.parchi; }

    public void setParchi(boolean parchi) { this.parchi = parchi; }

    public void setTrasportoPubblico(boolean trasportoPubblico) { this.trasportoPubblico = trasportoPubblico; }

    public boolean getTrasportoPubblico() { return trasportoPubblico;    }
    public Indirizzo getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(Indirizzo indirizzo) {
        this.indirizzo = indirizzo;
    }
}