package dieti.estates25.controllers;

import dieti.estates25.model.Immobile;
import dieti.estates25.model.ImmobileEImmagini;
import dieti.estates25.model.Offerta;
import dieti.estates25.model.Visita;
import dieti.estates25.model.agenteimmobiliare.AgenteImmobiliare;
import dieti.estates25.service.AgenteImmobiliareServiceDB;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth-agente")
public class AgenteImmobiliareController {

    private final AgenteImmobiliareServiceDB agentService;

    public AgenteImmobiliareController(
            AgenteImmobiliareServiceDB agentService) {
        this.agentService = agentService;
    }

    @GetMapping("/agenti")
    public List<AgenteImmobiliare> getAllAgents() {
        return agentService.getAllAgents();
    }

    @GetMapping("/agenti/{mail}")
    public AgenteImmobiliare getAgent(@PathVariable String mail) {
        return agentService.getAgentByMail(mail);
    }

    @PutMapping("/agenti")
    public void updateAgent(@RequestBody AgenteImmobiliare agent) {
        agentService.updateAgent(agent);
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
    public List<ImmobileEImmagini> getAllImmobili() {
        return agentService.getAllImmobili();
    }

    @GetMapping("/visite")
    public List<Visita> getVisite() {
        return agentService.getVisite();
    }

    @DeleteMapping("elimina-immobile")
    public ResponseEntity<String> eliminaImmobile(@RequestBody String idImmobile) {
        return agentService.eliminaImmobile(idImmobile);
    }

    @PostMapping("aggiungi-immobile")
    public ResponseEntity<String> aggiungiImmobile(@RequestBody ImmobileEImmagini request) {
        return agentService.aggiungiImmobile(request);
    }

    @PostMapping("confermaVisita")
    public ResponseEntity<String> confermaVisita(@RequestBody Visita visita) {
        return agentService.confermaVisista(visita);
    }

    @PostMapping("accettaOfferta")
    public ResponseEntity<String> accettaOfferta(@RequestBody Offerta offerta) {
        return agentService.accettaOfferta(offerta);
    }

    @GetMapping("/specificheAgenteImmobiliare")
    public AgenteImmobiliare getSpecifiche(){
        return agentService.getSpecifiche();
    }

    @GetMapping("/getImmobile")
    public Immobile getImmobile(@RequestParam String idImmobile) {
        return agentService.getImmobile(idImmobile);
    }

    @PostMapping("controOfferta")
    public ResponseEntity<String> controOfferta(@RequestBody Offerta offerta) {
        return agentService.controOfferta(offerta);
    }
}
