package dieti.estates25.repositories;

import dieti.estates25.model.ImmagineImmobile;
import dieti.estates25.model.Immobile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImmagineImmobileRepository extends JpaRepository<ImmagineImmobile, String> {

    List<ImmagineImmobile> findAllByImmobile(Immobile immobile);
}
