package dieti.estates25.model;

import dieti.estates25.model.agenteimmobiliare.AgenteImmobiliare;
import jakarta.persistence.*;
    @Entity
    @Table(name = "indirizzo")
    public class Indirizzo {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "idindirizzo")
        private Long idIndirizzo;

        @Column(name = "via", nullable = false)
        private String via;

        @Column(name = "citta", nullable = false)
        private String citta;

        @Column(name = "Latitudine", nullable = false)
        private Double latitudine;

        @Column(name = "Longitudine", nullable = false)
        private Double longitudine;

        @Column(name = "provincia", nullable = false)
        private String provincia;

        @Column(name = "numerocivico", nullable = false)
        private int numeroCivico;

        @Column(name = "cap", nullable = false)
        private int cap;
        @Column(name = "regione", nullable = false)
        private String regione;

        public Long getIdIndirizzo() {return idIndirizzo; }

        public Double getLatitudine() {return latitudine; }

        public void setLatitudine(Double latitudine) {this.latitudine = latitudine; }

        public Double getLongitudine() {return longitudine; }

        public void setLongitudine(Double longitudine) {this.longitudine = longitudine; }

        public int getNumeroCivico() {return numeroCivico; }

        public void setNumeroCivico(int numeroCivico) {this.numeroCivico = numeroCivico; }

        public String getCitta() {return citta; }

        public void setCitta(String citta) {this.citta = citta; }

        public int getCap() {return cap; }

        public void setCap(int cap) {this.cap = cap; }

        public String getProvincia() {return provincia; }

        public void setProvincia(String provincia) {this.provincia = provincia; }

        public String getRegione() {return regione; }

        public void setRegione(String regione) {this.regione = regione; }

        public String getVia() {return via; }

        public void setVia(String via) {this.via = via; }
    }
