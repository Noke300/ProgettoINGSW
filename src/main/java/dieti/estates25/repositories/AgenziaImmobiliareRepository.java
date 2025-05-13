package dieti.estates25.repositories;

import dieti.estates25.model.AgenziaImmobiliare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgenziaImmobiliareRepository extends JpaRepository<AgenziaImmobiliare, String> {
}

