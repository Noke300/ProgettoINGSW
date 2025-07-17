package dieti.estates25.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;

@Embeddable
public class VisitaId implements Serializable {

    private String clienteId;
    private String immobileId;

    public VisitaId() {}

    public VisitaId(String clienteId, String immobileId) {
        this.clienteId = clienteId;
        this.immobileId = immobileId;
    }

    // getter e setter

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VisitaId that)) return false;
        return Objects.equals(clienteId, that.clienteId) &&
                Objects.equals(immobileId, that.immobileId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clienteId, immobileId);
    }
}

