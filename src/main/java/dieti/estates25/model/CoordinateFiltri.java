package dieti.estates25.model;

public class CoordinateFiltri {

    private Indirizzo indirizzo;

    private Filtri filtri;

    public CoordinateFiltri(Indirizzo indirizzo, Filtri filtri) {
        this.indirizzo = indirizzo;
        this.filtri = filtri;
    }

    public Indirizzo getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(Indirizzo indirizzo) {
        this.indirizzo = indirizzo;
    }

    public Filtri getFiltri() {
        return filtri;
    }

    public void setFiltri(Filtri filtri) {
        this.filtri = filtri;
    }
}
