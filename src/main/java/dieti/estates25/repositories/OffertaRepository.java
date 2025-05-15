package dieti.estates25.repositories;

import dieti.estates25.model.Offerta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OffertaRepository extends JpaRepository<Offerta, Long> {

}
