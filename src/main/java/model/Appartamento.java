package model;

public class Appartamento extends Immobile{
    private int piano;
    private boolean portineria;

    public Appartamento(double prezzo, String tipoVendita, int superficie, String classeEnergetica, AgenziaImmobiliare agenziaImmobiliare, AgenteImmobiliare agenteImmobiliare, Indirizzo indirizzo) {
        super(prezzo, tipoVendita, superficie, classeEnergetica, agenziaImmobiliare, agenteImmobiliare, indirizzo);
    }

    public int getPiano() {
        return piano;
    }

    public void setPiano(int piano) {
        this.piano = piano;
    }

    public boolean getPortineria() {
        return portineria;
    }

    public void setPortineria(boolean portineria) {
        this.portineria = portineria;
    }
}
