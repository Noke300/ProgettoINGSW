package dieti.estates25.service;

import dieti.estates25.model.Immobile;
import dieti.estates25.model.Offerta;
import dieti.estates25.model.Visita;
import dieti.estates25.model.VisitaId;
import dieti.estates25.model.cliente.Cliente;
import dieti.estates25.model.cliente.ClientePrincipal;
import dieti.estates25.repositories.*;
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

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteServiceDB implements UserDetailsService {

    private final ClienteRepository clienteRepository;
    private final VisitaRepository visitaRepository;
    private final OffertaRepository offertaRepository;
    private final ImmobileRepository immobileRepository;
    private final JWTService jwtService;
    private final NotificaService notificaService;
    private final AuthenticationManager authManager;

    public ClienteServiceDB(ClienteRepository clienteRepository,
                            VisitaRepository visitaRepository,
                            OffertaRepository offertaRepository,
                            ImmobileRepository immobileRepository,
                            JWTService jwtService,
                            NotificaService notificaService,
                            @Lazy @Qualifier("clientAuthenticationManager") AuthenticationManager authManager) {
        this.clienteRepository = clienteRepository;
        this.visitaRepository = visitaRepository;
        this.offertaRepository = offertaRepository;
        this.immobileRepository = immobileRepository;
        this.jwtService = jwtService;
        this.notificaService = notificaService;
        this.authManager = authManager;
    }

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);   //numero di round che permettono una grande sicurezza senza troppo tempo per il calcolo

    public List<Cliente> getAllClients() {
        return clienteRepository.findAll();
    }

    public Cliente getClienteByMail(String mail) {
        Optional<Cliente> cliente = this.clienteRepository.findById(mail);
        return cliente.orElse(null);
    }

    public ResponseEntity<String> addCliente(Cliente cliente) {
        if (!checkDatiCliente(cliente.getEmailCliente(), cliente.getPasswordCliente(), cliente.getDataN())){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("I dati che stai inserendo non sono validi");
        }

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

    public boolean checkDatiCliente(String emailCliente, String passwordCliente, Date dataN) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        String regex2 = "^(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$";
        if (emailCliente == null || !emailCliente.matches(regex)) return false;
        if (passwordCliente == null || !passwordCliente.matches(regex2)) return false;
        if (dataN == null) return false;
        Calendar oggi = Calendar.getInstance();
        oggi.add(Calendar.YEAR, -18); // oggi meno 18 anni
        return dataN.before(oggi.getTime());
    }

    public void updateCliente(Cliente cliente) {
        clienteRepository.save(cliente);
    }

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
        visita.setImmobile(immobileRepository.findImmobileByIdImmobile(visita.getImmobile().getIdImmobile()));
        visita.setConferma(false);
        visita.setId(new VisitaId(email, visita.getImmobile().getIdImmobile()));
        visitaRepository.save(visita);
        notificaService.inviaNotifica(visita.getImmobile().getAgente().getEmailAgente(), "Hanno richiesto una nuova visita");
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

    public ResponseEntity<String> esitoOfferta(Offerta offerta) {
        if (offerta == null || !offerta.getContrOfferta() || !offertaRepository.existsById(offerta.getIdOfferta())){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Offerta inesistente o non contro Offerta");
        }
        Offerta oldOfferta = offertaRepository.findById(offerta.getIdOfferta()).orElse(null);
        assert oldOfferta != null;
        if(offerta.getEsitoOfferta() == null){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Esito incerto");
        }
        if(Boolean.FALSE.equals(offerta.getEsitoOfferta())){
            oldOfferta.setEsitoOfferta(false);
            offertaRepository.save(oldOfferta);
            return ResponseEntity.ok("Offerta Rifiutata");
        }
        oldOfferta.setEsitoOfferta(true);
        offertaRepository.save(oldOfferta);

        Immobile immobile = immobileRepository.findImmobileByIdImmobile(oldOfferta.getImmobile().getIdImmobile());
        immobile.setVenduto(true);
        immobileRepository.save(immobile);
        visitaRepository.deleteAll(visitaRepository.findByImmobile(immobile));
        notificaService.inviaNotifica(immobile.getAgente().getEmailAgente(), "Hanno accettato una contro Offerta");
        return ResponseEntity.ok("Offerta accettata");
    }
}

