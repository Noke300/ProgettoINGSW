package dieti.estates25.repositories;

import dieti.estates25.model.adminsupport.AdminSupport;
import dieti.estates25.model.agenziaimmobiliare.AgenziaImmobiliare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminSupportRepository extends JpaRepository<AdminSupport, String> {
    AdminSupport findByEmailSupport(String email);

    List<AdminSupport> findByNomeAgenzia(AgenziaImmobiliare agenziaImmobiliareByNomeAgenzia);
}
