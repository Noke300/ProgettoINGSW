package dieti.estates25.repositories;

import dieti.estates25.model.cliente.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, String> {

    Cliente findByEmailCliente(String email);

}
