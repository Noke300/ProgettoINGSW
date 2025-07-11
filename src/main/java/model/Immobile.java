package model;

import java.util.List;

public class Immobile {
    private String idImmobile;
    private String tipoImmobile;
    private String descrizione;
    private double prezzo;
    private String tipoVendita;
    private int superficie;
    private int piano;
    private int numBagni;
    private String classeEnergetica;
    private int numStanze;
    private boolean balcone;
    private boolean portineria;
    private boolean ascensore;
    private boolean cantina;
    private boolean climatizzazione;
    private boolean venduto;

    private boolean scuole;
    private boolean parchi;
    private boolean trasportoPubblico;

    private final AgenziaImmobiliare nomeAgenzia;
    private AgenteImmobiliare agente;
    private final Indirizzo indirizzo;

    public Immobile(AgenziaImmobiliare agenziaImmobiliare, Indirizzo indirizzo,AgenteImmobiliare agenteImmobiliare,
                    boolean transportoPubblico,boolean parchi, boolean scuole, boolean venduto,
                    boolean climatizzazione, boolean ascensore, boolean cantina,
                    boolean portineria, boolean balcone,
                    int numBagni, int piano, int superficie, int numStanze,double prezzo,
                    String classeEnergetica,String tipoVendita, String descrizione,String tipoImmobile) {

        this.nomeAgenzia = agenziaImmobiliare;
        this.indirizzo = indirizzo;
        this.agente = agenteImmobiliare;
        this.trasportoPubblico = transportoPubblico;
        this.parchi = parchi;
        this.scuole = scuole;
        this.venduto = venduto;
        this.climatizzazione = climatizzazione;
        this.ascensore = ascensore;
        this.cantina = cantina;
        this.portineria = portineria;
        this.numStanze = numStanze;
        this.balcone = balcone;
        this.classeEnergetica = classeEnergetica;
        this.numBagni = numBagni;
        this.piano = piano;
        this.superficie = superficie;
        this.tipoVendita = tipoVendita;
        this.prezzo = prezzo;
        this.descrizione = descrizione;
        this.tipoImmobile = tipoImmobile;
    }

    public Immobile(String id){
        this.idImmobile = id;

        this.nomeAgenzia = null;
        this.agente = null;
        this.indirizzo = null;
    }

    public AgenziaImmobiliare getAgenziaImmobiliare() {
        return nomeAgenzia;
    }

    public AgenteImmobiliare getAgenteImmobiliare() {
        return agente;
    }

    public void setAgenteImmobiliare(AgenteImmobiliare agenteImmobiliare) {
        this.agente = agenteImmobiliare;
    }

    public Indirizzo getIndirizzo() {
        return indirizzo;
    }

    public double getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }

    public String getTipoVendita() {
        return tipoVendita;
    }

    public void setTipoVendita(String tipoVendita) {
        this.tipoVendita = tipoVendita;
    }

    public int getSuperficie() {
        return superficie;
    }

    public void setSuperficie(int superficie) {
        this.superficie = superficie;
    }

    public int getNumBagni() {
        return numBagni;
    }

    public void setNumBagni(int numBagni) {
        this.numBagni = numBagni;
    }

    public String getClasseEnergetica() {
        return classeEnergetica;
    }

    public void setClasseEnergetica(String classeEnergetica) {
        this.classeEnergetica = classeEnergetica;
    }

    public int getNumStanze() {
        return numStanze;
    }

    public void setNumStanze(int numStanze) {
        this.numStanze = numStanze;
    }

    public boolean getAscensore() {
        return ascensore;
    }

    public void setAscensore(boolean ascensore) {
        this.ascensore = ascensore;
    }

    public boolean getCantina() {
        return cantina;
    }

    public void setCantina(boolean cantina) {
        this.cantina = cantina;
    }

    public boolean getClimatizzazione() {
        return climatizzazione;
    }

    public void setClimatizzazione(boolean climatizzazione) {
        this.climatizzazione = climatizzazione;
    }

    public boolean getVenduto() {
        return venduto;
    }

    public void setVenduto(boolean venduto) {
        this.venduto = venduto;
    }

    public boolean isAscensore() {
        return ascensore;
    }

    public boolean isCantina() {
        return cantina;
    }

    public boolean isClimatizzazione() {
        return climatizzazione;
    }

    public boolean isVenduto() {
        return venduto;
    }

    public String getId() {return idImmobile;}
    public void setId(String id) {
        this.idImmobile = id;
    }
    public Double getLongIm(){
        return this.indirizzo.getLongitudine();
    }
    public Double getLanIm(){
        return this.indirizzo.getLatitudine();
    }

    public String getIdImmobile() {
        return idImmobile;
    }

    public void setIdImmobile(String idImmobile) {
        this.idImmobile = idImmobile;
    }

    public String getTipoImmobile() {
        return tipoImmobile;
    }

    public void setTipoImmobile(String tipoImmobile) {
        this.tipoImmobile = tipoImmobile;
    }

    public int getPiano() {
        return piano;
    }

    public void setPiano(int piano) {
        this.piano = piano;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public boolean isBalcone() {
        return balcone;
    }

    public void setBalcone(boolean balcone) {
        this.balcone = balcone;
    }

    public boolean isScuole() {
        return scuole;
    }

    public void setScuole(boolean scuole) {
        this.scuole = scuole;
    }

    public boolean isParchi() {
        return parchi;
    }

    public void setParchi(boolean parchi) {
        this.parchi = parchi;
    }

    public boolean isTransportoPubblico() {
        return trasportoPubblico;
    }

    public void setTransportoPubblico(boolean transportoPubblico) {
        this.trasportoPubblico = transportoPubblico;
    }

    public boolean isPortineria() {
        return portineria;
    }

    public void setPortineria(boolean portineria) {
        this.portineria = portineria;
    }
}
