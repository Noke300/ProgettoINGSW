package dieti.estates25.repositories;

import dieti.estates25.model.Offerta;
import dieti.estates25.model.cliente.Cliente;
import org.springframework.boot.autoconfigure.pulsar.PulsarProperties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OffertaRepository extends JpaRepository<Offerta, Long> {

    List<Offerta> findAllByImmobileIdImmobileEquals(String idImmobile);

    List<Offerta> findAllByCliente(Cliente cliente);

}
