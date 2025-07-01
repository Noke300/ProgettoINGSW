package dieti.estates25.repositories;

import dieti.estates25.model.agenteimmobiliare.AgenteImmobiliare;
import dieti.estates25.model.agenziaimmobiliare.AgenziaImmobiliare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgenteImmobiliareRepository extends JpaRepository<AgenteImmobiliare, String> {

    AgenteImmobiliare findByEmailAgente(String email);

    List<AgenteImmobiliare> findByNomeAgenzia(AgenziaImmobiliare agenziaImmobiliare);

}
