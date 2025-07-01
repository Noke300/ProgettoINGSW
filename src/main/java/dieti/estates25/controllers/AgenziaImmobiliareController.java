package dieti.estates25.controllers;

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

    @PutMapping("/agenziaImmobiliare/{name}")
    public void updateAgenziaImmobiliare(@PathVariable String name, @RequestBody AgenziaImmobiliare agenzia) {
        agenziaImmobiliareServiceDB.updateAgenziaImmobiliare(name, agenzia);
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
}
