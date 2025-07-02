package dieti.estates25.service.adminsupport;

import dieti.estates25.model.adminsupport.AdminSupport;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AdminSupportService {

    List<AdminSupport> getAllAdminSupport();

    AdminSupport getAdminSupportByMail(String mail);

    ResponseEntity<String> addAdminSupport(AdminSupport adminSupport);

    void updateAdminSupport(String mail, AdminSupport adminSupport);

    ResponseEntity<String> deleteAdminSupport(String mail);
}
