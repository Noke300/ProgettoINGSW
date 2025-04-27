package dieti.estates25.service.cliente;

import dieti.estates25.model.Cliente;
import dieti.estates25.model.ClientePrincipal;
import dieti.estates25.service.JWTService;
import dieti.estates25.service.cliente.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import dieti.estates25.repositories.ClienteRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteServiceDB implements ClienteService, UserDetailsService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private JWTService jwtService;

    @Autowired
    @Lazy
    @Qualifier("clientAuthenticationManager")
    private AuthenticationManager authManager;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);   //numero di round che permettono una grande sicurezza senza troppo tempo per il calcolo

    @Override
    public List<Cliente> getAllClients() {
        return clienteRepository.findAll();
    }

    @Override
    public Cliente getClienteByMail(String mail) {
        Optional<Cliente> cliente = this.clienteRepository.findById(mail);
        return cliente.orElse(null);
    }

    @Override
    public void addCliente(Cliente cliente) {
        cliente.setPasswordCliente(encoder.encode(cliente.getPasswordCliente()));
        clienteRepository.save(cliente);
    }

    @Override
    public void updateCliente(String mail, Cliente cliente) {
        clienteRepository.save(cliente);
    }

    @Override
    public void deleteCliente(String mail) {
        clienteRepository.deleteById(mail);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Cliente cliente = clienteRepository.findByEmailCliente(username);

        if (cliente == null) {
            throw new UsernameNotFoundException("Cliente non trovato");
        }

        return new ClientePrincipal(cliente);
    }

    public String verify(Cliente cliente) {
        Authentication authentication =
                authManager.authenticate(new UsernamePasswordAuthenticationToken(cliente.getEmailCliente(), cliente.getPasswordCliente()));

        if(authentication.isAuthenticated())
            return jwtService.generateToken(cliente.getEmailCliente(), "cliente");

        return "Fail";
    }
}

