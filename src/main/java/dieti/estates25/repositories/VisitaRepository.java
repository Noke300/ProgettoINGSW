package dieti.estates25.repositories;

import dieti.estates25.model.Immobile;
import dieti.estates25.model.Visita;
import dieti.estates25.model.VisitaId;
import dieti.estates25.model.cliente.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VisitaRepository extends JpaRepository<Visita, VisitaId> {

    List<Visita> findByImmobileIn(List<Immobile> immobiles);

    boolean existsVisitaByClienteAndImmobile(Cliente cliente, Immobile immobile);
}
