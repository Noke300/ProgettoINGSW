package dieti.estates25.repositories;

import dieti.estates25.model.adminsupport.AdminSupport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminSupportRepository extends JpaRepository<AdminSupport, String> {
    AdminSupport findByEmailSupport(String email);
}
