package dieti.estates25.controllers;

import dieti.estates25.model.Immobile;
import dieti.estates25.model.Visita;
import dieti.estates25.model.agenteimmobiliare.AgenteImmobiliare;
import dieti.estates25.service.agenteimmobiliare.AgenteImmobiliareServiceDB;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth-agente")
public class AgenteImmobiliareController {

    @Autowired
    @Qualifier("agentAuthenticationManager")
    private AuthenticationManager authenticationManager;

    @Autowired
    private AgenteImmobiliareServiceDB agentService;

    @GetMapping("/agenti")
    public List<AgenteImmobiliare> getAllAgents() {
        return agentService.getAllAgents();
    }

    @GetMapping("/agenti/{mail}")
    public AgenteImmobiliare getAgent(@PathVariable String mail) {
        return agentService.getAgentByMail(mail);
    }

    @PostMapping("/register")
    public ResponseEntity<String> addAgent(@RequestBody AgenteImmobiliare agent) {
        return agentService.addAgent(agent);
    }

    @PutMapping("/agenti/{mail}")
    public void updateAgent(@PathVariable String mail, @RequestBody AgenteImmobiliare agent) {
        agentService.updateAgent(mail, agent);
    }

    @DeleteMapping("/agenti/{mail}")
    public void deleteAgent(@PathVariable String mail) {
        agentService.deleteAgent(mail);
    }

    @GetMapping("/csrf-token")
    public CsrfToken getCsrfToken(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute("_csrf");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AgenteImmobiliare agent) {
        return agentService.verify(agent);
    }

    @GetMapping("/immobili")
    public List<Immobile> getAllImmobili() {
        return agentService.getAllImmobili();
    }

    @GetMapping("/date")
    public List<Visita> getVisite() {
        return agentService.getVisite();
    }

    @DeleteMapping("elimina-immobile")
    public ResponseEntity<String> eliminaImmobile(@RequestBody String idImmobile) {
        return agentService.eliminaImmobile(idImmobile);
    }
}
