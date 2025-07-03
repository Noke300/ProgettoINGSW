package dieti.estates25.model;

import jakarta.persistence.*;

@Entity
@Table(name = "immagineimmobile")
public class ImmagineImmobile {
    @Id
    @Column(name = "urlimmagine")
    private String urlImmagine;

    @ManyToOne
    @JoinColumn(name = "idimmobile")
    private Immobile immobile;

    public ImmagineImmobile(String urlImmagine, Immobile immobile) {
        this.urlImmagine = urlImmagine;
        this.immobile = immobile;
    }

    public void setUrlImmagine(String urlImmagine) { this.urlImmagine = urlImmagine; }

    public String getUrlImmagine() { return urlImmagine; }

    public Immobile getImmobile() { return immobile; }
}
