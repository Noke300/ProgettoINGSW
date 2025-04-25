package dieti.estates25.repositories;

import dieti.estates25.model.AgenteImmobiliare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgenteImmobiliareRepository extends JpaRepository<AgenteImmobiliare, String> {

}
