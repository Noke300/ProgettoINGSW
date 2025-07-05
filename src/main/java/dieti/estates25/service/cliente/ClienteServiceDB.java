package dieti.estates25.service.cliente;

import dieti.estates25.model.Offerta;
import dieti.estates25.model.Visita;
import dieti.estates25.model.VisitaId;
import dieti.estates25.model.cliente.Cliente;
import dieti.estates25.model.cliente.ClientePrincipal;
import dieti.estates25.repositories.ImmobileRepository;
import dieti.estates25.repositories.OffertaRepository;
import dieti.estates25.repositories.VisitaRepository;
import dieti.estates25.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import dieti.estates25.repositories.ClienteRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteServiceDB implements ClienteService, UserDetailsService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private VisitaRepository visitaRepository;

    @Autowired
    private OffertaRepository offertaRepository;

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
    public ResponseEntity<String> addCliente(Cliente cliente) {
        if (clienteRepository.findByEmailCliente(cliente.getEmailCliente()) != null) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("è già presente un account con questa mail");
        }

        cliente.setPasswordCliente(encoder.encode(cliente.getPasswordCliente()));
        clienteRepository.save(cliente);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Utente creato correttamente");
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

    public ResponseEntity<String> verify(Cliente cliente) {
        Authentication authentication =
                authManager.authenticate(new UsernamePasswordAuthenticationToken(cliente.getEmailCliente(), cliente.getPasswordCliente()));

        if(authentication.isAuthenticated()){
            return ResponseEntity.ok(jwtService.generateToken(cliente.getEmailCliente(), "cliente"));
        }

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Unauthorized");
    }

    public String getEmailAutenticato() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userDetails.getUsername();
        }
        return null;
    }

    public ResponseEntity<String> prenotaVisita(Visita visita) {
        if (visita.getImmobile() == null){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Nessun immobile selezionato");
        }
        if (visita.getDataVisita().before(Date.from(Instant.now()))){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Data non valida");
        }
        String email = getEmailAutenticato();
        if (visitaRepository.existsVisitaByClienteAndImmobile(clienteRepository.findByEmailCliente(email), visita.getImmobile())){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Non puoi prenotare più di una visita alla volta per lo stesso immobile");
        }
        visita.setCliente(clienteRepository.findByEmailCliente(email));
        visita.setConferma(false);
        visita.setId(new VisitaId(email, visita.getImmobile().getIdImmobile()));
        visitaRepository.save(visita);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Visita prenotata correttamente");
    }

    public Cliente getSpecifiche() {
        return clienteRepository.findByEmailCliente(getEmailAutenticato());
    }

    public List<Offerta> getOfferte() {
        List<Offerta> offerte = offertaRepository.findAllByCliente(clienteRepository.findByEmailCliente(getEmailAutenticato()));
        for( Offerta o : offerte){
            o.getImmobile().getAgente().setPasswordAgente(null);
            o.getImmobile().getNomeAgenzia().setPasswordAdmin(null);
        }
        return offerte;
    }

    public List<Visita> getVisite() {
        List<Visita> visite = visitaRepository.findAllByCliente(clienteRepository.findByEmailCliente(getEmailAutenticato()));
        for( Visita v : visite){
            v.getImmobile().getAgente().setPasswordAgente(null);
            v.getImmobile().getNomeAgenzia().setPasswordAdmin(null);
        }
        return visite;
    }
}

