package dieti.estates25.controllers;

import dieti.estates25.model.Offerta;
import dieti.estates25.model.Visita;
import dieti.estates25.model.cliente.Cliente;
import dieti.estates25.service.ClienteServiceDB;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth-cliente")
public class ClienteController {
    private final ClienteServiceDB clienteService;

    public ClienteController(ClienteServiceDB clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping("/clienti")
    public List<Cliente> getAllClienti() {
        return clienteService.getAllClients();
    }

    @GetMapping("/clienti/{mail}")
    public Cliente getCliente(@PathVariable String mail) {
        return clienteService.getClienteByMail(mail);
    }

    @PostMapping("/register")
    public ResponseEntity<String> addCliente(@RequestBody Cliente cliente) {
        return clienteService.addCliente(cliente);
    }

    @PutMapping("/clienti")
    public void updateCliente(@RequestBody Cliente cliente) {
        clienteService.updateCliente(cliente);
    }

    @DeleteMapping("/clienti/{mail}")
    public void deleteCliente(@PathVariable String mail) {
        clienteService.deleteCliente(mail);
    }

    @GetMapping("/csrf-token")
    public CsrfToken getCsrfToken(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute("_csrf");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Cliente cliente) {
        return clienteService.verify(cliente);
    }

    @PostMapping("/visita")
    public ResponseEntity<String> newVisita(@RequestBody Visita visita) {
        return clienteService.prenotaVisita(visita);
    }

    @GetMapping("/specificheCliente")
    public Cliente getSpecifiche(){
        return clienteService.getSpecifiche();
    }

    @GetMapping("/getOfferte")
    public List<Offerta> getOfferte() {
        return clienteService.getOfferte();
    }

    @GetMapping("/getVisite")
    public List<Visita> getVisite() {
        return clienteService.getVisite();
    }

    @PostMapping("/controOfferta")
    public ResponseEntity<String> esitoOfferta(@RequestBody Offerta offerta) {
        return clienteService.esitoOfferta(offerta);
    }

    @PostMapping("/controVisita")
    public ResponseEntity<String> esitoVisista(@RequestBody Visita visita) {
        return clienteService.eistoVisita(visita);
    }

}

