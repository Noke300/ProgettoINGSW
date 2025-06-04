package dieti.estates25.repositories;

import dieti.estates25.model.Immobile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ImmobileRepository extends JpaRepository<Immobile, Long> {

    List<Immobile> findAllByAgente_EmailAgente(String agenteEmailAgente);

    @Query("SELECT i FROM Immobile i join Indirizzo d on i.indirizzo.idIndirizzo = d.idIndirizzo WHERE d.latitudine BETWEEN :minLat AND :maxLat AND d.longitudine BETWEEN :minLon AND :maxLon")
    List<Immobile> findWithinBounds(double minLat, double maxLat, double minLon, double maxLon);


}
