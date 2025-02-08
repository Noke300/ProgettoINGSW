package model;

public abstract class Immobile {
    private double prezzo;
    private String tipoVendita;
    private int superficie;
    private int numBagni;
    private String classeEnergetica;
    private int numStanze;
    private boolean ascensore;
    private boolean cantina;
    private boolean climatizzazione;
    private boolean venduto;

    private final AgenziaImmobiliare agenziaImmobiliare;
    private AgenteImmobiliare agenteImmobiliare;
    private final Indirizzo indirizzo;

    public Immobile(double prezzo, String tipoVendita, int superficie, String classeEnergetica, AgenziaImmobiliare agenziaImmobiliare, AgenteImmobiliare agenteImmobiliare, Indirizzo indirizzo) {
        this.prezzo = prezzo;
        this.tipoVendita = tipoVendita;
        this.superficie = superficie;
        this.classeEnergetica = classeEnergetica;
        this.agenziaImmobiliare = agenziaImmobiliare;
        this.agenteImmobiliare = agenteImmobiliare;
        this.indirizzo = indirizzo;

        this.numBagni = 1;
        this.numStanze = 1;
        this.ascensore = false;
        this.cantina = false;
        this.climatizzazione = false;
        this.venduto = false;
    }

    public AgenziaImmobiliare getAgenziaImmobiliare() {
        return agenziaImmobiliare;
    }

    public AgenteImmobiliare getAgenteImmobiliare() {
        return agenteImmobiliare;
    }

    public void setAgenteImmobiliare(AgenteImmobiliare agenteImmobiliare) {
        this.agenteImmobiliare = agenteImmobiliare;
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

}
