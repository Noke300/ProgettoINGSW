package dieti.estates25.repositories;

import dieti.estates25.model.Immobile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImmobileRepository extends JpaRepository<Immobile, Long> {

    List<Immobile> findAllByAgente_EmailAgente(String agenteEmailAgente);

}
