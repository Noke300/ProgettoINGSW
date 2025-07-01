package dieti.estates25.service.agenteimmobiliare;

import dieti.estates25.model.Immobile;
import dieti.estates25.model.Visita;
import dieti.estates25.model.agenteimmobiliare.AgenteImmobiliare;
import dieti.estates25.model.agenteimmobiliare.AgenteImmobiliarePrincipal;
import dieti.estates25.model.agenziaimmobiliare.AgenziaImmobiliare;
import dieti.estates25.repositories.AgenteImmobiliareRepository;
import dieti.estates25.repositories.ImmobileRepository;
import dieti.estates25.repositories.IndirizzoRepository;
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
import java.util.List;
import java.util.Optional;

@Service
public class AgenteImmobiliareServiceDB implements AgenteImmobiliareService, UserDetailsService {

    @Autowired
    private AgenteImmobiliareRepository agentRepository;

    @Autowired
    private ImmobileRepository immobileRepository;

    @Autowired
    private VisitaRepository visitaRepository;

    @Autowired
    private IndirizzoRepository indirizzoRepository;

    @Autowired
    private JWTService jwtService;

    @Autowired
    @Lazy
    @Qualifier("agentAuthenticationManager")
    private AuthenticationManager authManager;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
    @Override
    public List<AgenteImmobiliare> getAllAgents() {
        return agentRepository.findAll();
    }

    @Override
    public AgenteImmobiliare getAgentByMail(String mail) {
        Optional<AgenteImmobiliare> agent = this.agentRepository.findById(mail);
        return  agent.orElse(null);
    }

    @Override
    public ResponseEntity<String> addAgent(AgenteImmobiliare agent, AgenziaImmobiliare nomeAgenzia) {
        if (agentRepository.findByEmailAgente(agent.getEmailAgente()) != null) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("è già presente un account con questa mail");
        }
        agent.setPasswordAgente(encoder.encode(agent.getPasswordAgente()));
        agent.setNomeAgenzia(nomeAgenzia);
        agentRepository.save(agent);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Utente creato correttamente");
    }

    @Override
    public void updateAgent(String mail, AgenteImmobiliare agent) {
        agentRepository.save(agent);
    }

    @Override
    public void deleteAgent(String mail) {
        agentRepository.deleteById(mail);
    }

    @Override
    public List<Immobile> getAllImmobili() {
        return immobileRepository.findAllByAgente_EmailAgente(getEmailAutenticato());
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        AgenteImmobiliare agent = agentRepository.findByEmailAgente(username);

        if (agent == null) {
            throw new UsernameNotFoundException("Agente Immobiliare non trovato");
        }

        return new AgenteImmobiliarePrincipal(agent);
    }

    public ResponseEntity<String> verify(AgenteImmobiliare agent) {
        Authentication authentication =
                authManager.authenticate(new UsernamePasswordAuthenticationToken(agent.getEmailAgente(), agent.getPasswordAgente()));

        if(authentication.isAuthenticated()){
            return ResponseEntity.ok(jwtService.generateToken(agent.getEmailAgente(), "agente"));
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

    public List<Visita> getVisite() {
        List<Immobile> immobiles = immobileRepository.findAllByAgente_EmailAgente(getEmailAutenticato());
        return visitaRepository.findByImmobileIn(immobiles);
    }

    public ResponseEntity<String> eliminaImmobile(String idImmobile) {
        if (idImmobile == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Immobile non trovato");
        }

        Immobile immobile;

        immobile = immobileRepository.findImmobileByIdImmobile(idImmobile);

        if (immobile == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Immobile non trovato");
        }

        if (!immobile.getAgente().getEmailAgente().equals(getEmailAutenticato())) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Stai cercando di eliminare un immobile che non gestisci tu");
        }
        //TODO Da testare
        if (immobileRepository.countByIndirizzo(immobile.getIndirizzo()) <= 1){
            indirizzoRepository.deleteById(immobile.getIndirizzo().getIdIndirizzo());
        }
        immobileRepository.delete(immobile);
        //TODO Idealmente mandare delle notifiche ai clienti che hanno fatto un offerta/prenotato una visita all'immobile
        return ResponseEntity.ok("Immobile eliminato correttamente");
    }

    public ResponseEntity<String> aggiungiImmobile(Immobile immobile) {
        AgenteImmobiliare agent = getAgentByMail(getEmailAutenticato());
        immobile.setAgente(agent);
        immobile.setNomeAgenzia(agent.getNomeAgenzia());
        immobile.setVenduto(false);
        immobileRepository.save(immobile);
        return ResponseEntity.ok("Immobile aggiunto correttamente");
        //TODO Da testare
    }
}
