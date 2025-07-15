package dieti.estates25.controllers;

import dieti.estates25.model.Immobile;
import dieti.estates25.model.ImmobileEImmagini;
import dieti.estates25.model.adminsupport.AdminSupport;
import dieti.estates25.model.agenteimmobiliare.AgenteImmobiliare;
import dieti.estates25.service.adminsupport.AdminSupportServiceDB;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth-adminSupport")
public class AdminSupportController {

    @Autowired
    @Qualifier("adminSupportAuthenticationManager")
    private AuthenticationManager authenticationManager;

    @Autowired
    private AdminSupportServiceDB adminSupportService;

    @GetMapping("/adminSupport")
    public List<AdminSupport> getAllAdminSupport() {
        return adminSupportService.getAllAdminSupport();
    }

    @GetMapping("/adminSupport/{mail}")
    public AdminSupport getAdminSupport(@PathVariable String mail) {
        return adminSupportService.getAdminSupportByMail(mail);
    }

    @PutMapping("/adminSupport/{mail}")
    public void updateAdminSupport(@PathVariable String mail, @RequestBody AdminSupport agent) {
        adminSupportService.updateAdminSupport(mail, agent);
    }

    @DeleteMapping("/adminSupport/{mail}")
    public void deleteAdminSupport(@PathVariable String mail) {
        adminSupportService.deleteAdminSupport(mail);
    }

    @GetMapping("/csrf-token")
    public CsrfToken getCsrfToken(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute("_csrf");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AdminSupport adminSupport) {
        return adminSupportService.verify(adminSupport);
    }

    @PostMapping("/registraAgenteImmobiliare")
    public ResponseEntity<String> addAgente(@RequestBody AgenteImmobiliare agent) {
        return adminSupportService.addAgent(agent);
    }

    @DeleteMapping("/licenziaAgenteImmobiliare")
    public ResponseEntity<String> deleteAgente(@RequestBody AgenteImmobiliare agent) {
        return adminSupportService.deleteAgenteImmobiliare(agent);
    }

    @GetMapping("/visualizzaImmobili")
    public List<ImmobileEImmagini> visualizzaImmobili(){
        return adminSupportService.getImmobili();
    }

    @GetMapping("/specificheCliente")
    public AdminSupport getSpecifiche(){
        return adminSupportService.getSpecifiche();
    }

    @GetMapping("/getAgentiImmobiliari")
    public List<AgenteImmobiliare> getAgenti() {
        return adminSupportService.getAgenti();
    }
}
