package dieti.estates25.controllers;

import dieti.estates25.model.Offerta;
import dieti.estates25.model.cliente.Cliente;
import dieti.estates25.service.cliente.ClienteServiceDB;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth-cliente")
public class ClienteController {

    @Autowired
    @Qualifier("clientAuthenticationManager")
    private AuthenticationManager authenticationManager;

    @Autowired
    private ClienteServiceDB clienteService;

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

    @PutMapping("/clienti/{mail}")
    public void updateCliente(@PathVariable String mail, @RequestBody Cliente cliente) {
        clienteService.updateCliente(mail, cliente);
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

    @PostMapping("/offerta")
    public ResponseEntity<String> newOfferta(@RequestBody Offerta offerta) {
        return clienteService.newOfferta(offerta);
    }

}

