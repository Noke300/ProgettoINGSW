package model;

public class AdminSupport extends Utente implements Amministrazione{
    private final AgenziaImmobiliare agenziaImmobiliare;

    public AdminSupport(String nome, String cognome, String email, String password, AgenziaImmobiliare agenziaImmobiliare) {
        super(nome, cognome, email, password);
        this.agenziaImmobiliare = agenziaImmobiliare;
    }

    public AgenziaImmobiliare getAgenziaImmobiliare() {
        return agenziaImmobiliare;
    }

    @Override
    public void visualizzaDipendenti() {
        // TODO document why this method is empty
    }

    @Override
    public void assumiDipendente() {
        // TODO document why this method is empty
    }

    @Override
    public void licenziaDipendente() {
        // TODO document why this method is empty
    }

    @Override
    public void visualizzaImmobili() {
        // TODO document why this method is empty
    }

    @Override
    public void assegnaImmobile() {
        // TODO document why this method is empty
    }
}
