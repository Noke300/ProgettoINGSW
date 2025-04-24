package dietiEstates25.repositories;

import dietiEstates25.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, String> {

    Cliente findByEmailCliente(String email);

}
