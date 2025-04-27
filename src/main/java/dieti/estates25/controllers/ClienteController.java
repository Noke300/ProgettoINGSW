package dieti.estates25.controllers;

import dieti.estates25.model.Cliente;
import dieti.estates25.service.cliente.ClienteServiceDB;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    //possiamo iniettare ClienteServiceMock per andare a lavorare con i dati d'esempio presenti
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
    public void addCliente(@RequestBody Cliente cliente) {
        clienteService.addCliente(cliente);
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
    public String login(@RequestBody Cliente cliente) {
        return clienteService.verify(cliente);
    }

}

