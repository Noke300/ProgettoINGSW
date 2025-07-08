package dieti.estates25.controllers;

import dieti.estates25.model.Immobile;
import dieti.estates25.model.adminsupport.AdminSupport;
import dieti.estates25.model.agenteimmobiliare.AgenteImmobiliare;
import dieti.estates25.model.agenziaimmobiliare.AgenziaImmobiliare;
import dieti.estates25.service.AgenziaImmobiliareServiceDB;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth-admin")
public class AgenziaImmobiliareController {

    @Autowired
    @Qualifier("agenziaImmobiliareAuthenticationManager")
    private AuthenticationManager authenticationManager;

    @Autowired
    private AgenziaImmobiliareServiceDB agenziaImmobiliareServiceDB;

    @GetMapping("/getAgenzie")
    public List<AgenziaImmobiliare> getAllAgenzie() {
        return agenziaImmobiliareServiceDB.getAllAgenzieImmobiliari();
    }

    @GetMapping("/getAgenzia/{name}")
    public AgenziaImmobiliare getAgenziaImmobiliare(@PathVariable String name) {
        return agenziaImmobiliareServiceDB.getAgenziaByName(name);
    }

    @PostMapping("/register")
    public ResponseEntity<String> agenziaImmobiliare(@RequestBody AgenziaImmobiliare agenzia) {
        return agenziaImmobiliareServiceDB.addAgenzia(agenzia);
    }

    @PutMapping("/agenziaImmobiliare")
    public void updateAgenziaImmobiliare(@RequestBody AgenziaImmobiliare agenzia) {
        agenziaImmobiliareServiceDB.updateAgenziaImmobiliare(agenzia);
    }

    @DeleteMapping("/agenziaiImmobiliare/{name}")
    public void deleteAgenziaImmobiliare(@PathVariable String name) {
        agenziaImmobiliareServiceDB.deleteAgenziaImmobiliare(name);
    }

    @GetMapping("/csrf-token")
    public CsrfToken getCsrfToken(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute("_csrf");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AgenziaImmobiliare agenzia) {
        return agenziaImmobiliareServiceDB.verify(agenzia);
    }

    @GetMapping("getAgentiImmobiliari")
    public List<AgenteImmobiliare> getAgenti(){
        return agenziaImmobiliareServiceDB.getAgenti();
    }

    @GetMapping("getAdminSupport")
    public List<AdminSupport> getAdminSupport(){
        return agenziaImmobiliareServiceDB.getAdminSupport();
    }

    @PostMapping("/registraAgenteImmobiliare")
    public ResponseEntity<String> addAgente(@RequestBody AgenteImmobiliare agent) {
        return agenziaImmobiliareServiceDB.addAgent(agent);
    }

    @PostMapping("/registraAdminSupport")
    public ResponseEntity<String> addAdminSupport(@RequestBody AdminSupport adminSupport) {
        return agenziaImmobiliareServiceDB.addAdminSupport(adminSupport);
    }

    @DeleteMapping("/licenziaAdminSupport")
    public ResponseEntity<String> deleteAdminSupport(@RequestBody AdminSupport adminSupport) {
        return agenziaImmobiliareServiceDB.deleteAdminSupport(adminSupport);
    }

    @DeleteMapping("/licenziaAgenteImmobiliare")
    public ResponseEntity<List<Immobile>> deleteAgente(@RequestBody AgenteImmobiliare agent) {
        return agenziaImmobiliareServiceDB.deleteAgenteImmobiliare(agent);
    }

    @GetMapping("/visualizzaImmobili")
    //TODO ImmobiliEImmagini
    public List<Immobile> visualizzaImmobili(){
        return agenziaImmobiliareServiceDB.getImmobili();
    }

    @PostMapping("/aggiornaCredenziali")
    public void aggiornaCredenziali(@RequestBody AgenziaImmobiliare agenzia) {
        agenziaImmobiliareServiceDB.modificaCredenziali(agenzia);
    }

    @GetMapping("/specificheAgenziaImmobiliare")
    public AgenziaImmobiliare getSpecifiche(){
        return agenziaImmobiliareServiceDB.getSpecifiche();
    }
}
