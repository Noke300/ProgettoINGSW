package dieti.estates25.service.agenteimmobiliare;

import dieti.estates25.database.GoogleCloudStorageManager;
import dieti.estates25.database.GoogleCloudStorageService;
import dieti.estates25.database.StorageManagerInterface;
import dieti.estates25.model.*;
import dieti.estates25.model.agenteimmobiliare.AgenteImmobiliare;
import dieti.estates25.model.agenteimmobiliare.AgenteImmobiliarePrincipal;
import dieti.estates25.model.agenziaimmobiliare.AgenziaImmobiliare;
import dieti.estates25.repositories.*;
import dieti.estates25.service.JWTService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
    private ImmagineImmobileRepository immagineImmobileRepository;

    @Autowired
    @Lazy
    @Qualifier("agentAuthenticationManager")
    private AuthenticationManager authManager;

    private GoogleCloudStorageManager storageManager;

    private GoogleCloudStorageService storageService;

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
    public ResponseEntity<String> addAgent(AgenteImmobiliare agent) {
        if (agentRepository.findByEmailAgente(agent.getEmailAgente()) != null) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("è già presente un account con questa mail");
        }
        agent.setPasswordAgente(encoder.encode(agent.getPasswordAgente()));
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
    public ResponseEntity<List<Immobile>> deleteAgent(String mail) {
        if(agentRepository.existsById(mail)) {
            List<Immobile> immobiliDaGestire = immobileRepository.findAllByAgente_EmailAgente(mail);
            agentRepository.deleteById(mail);
            return ResponseEntity.ok(immobiliDaGestire);
        }
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(null);
    }

    public List<ImmobileEImmagini> getAllImmobili() {
        List<Immobile> immobili =  immobileRepository.findAllByAgente_EmailAgente(getEmailAutenticato());
        return getImmagini(immobili);
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

    public ResponseEntity<String> aggiungiImmobile(ImmobileEImmagini request) {

        Immobile immobile = request.getImmobile();
        Indirizzo indirizzo = immobile.getIndirizzo();

        indirizzo = getOrCreateIndirizzo(indirizzo);
        immobile.setIndirizzo(indirizzo);

        AgenteImmobiliare agent = getAgentByMail(getEmailAutenticato());
        immobile.setAgente(agent);
        immobile.setNomeAgenzia(agent.getNomeAgenzia());
        immobile.setVenduto(false);
        immobile = immobileRepository.save(immobile);

        List<String> immagini = request.getImmagini();

        List<String> fileNames = request.getFileNames();

        String idCartella = immobile.getIdImmobile();

        //TODO gestire meglio le exception
        try {
            storageManager = new GoogleCloudStorageManager();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        storageService = new GoogleCloudStorageService(storageManager);

        for(int i=0; i<immagini.size(); i++)
        {
            try {
                String fileUrl = storageService.uploadHouseImage(fileNames.get(i), immagini.get(i), idCartella);
                immagineImmobileRepository.save(new ImmagineImmobile(fileUrl, immobile));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return ResponseEntity.ok("Immobile aggiunto correttamente");
        //TODO Da testare
    }

    private Indirizzo getOrCreateIndirizzo(Indirizzo nuovoIndirizzo) {
        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                .withIgnorePaths("latitudine", "longitudine")
                .withIgnoreCase()
                .withIgnoreNullValues();

        Example<Indirizzo> example = Example.of(nuovoIndirizzo, matcher);

        return indirizzoRepository.findOne(example)
                .orElseGet(() -> indirizzoRepository.save(nuovoIndirizzo));
    }

    public AgenteImmobiliare getSpecifiche() {
        return agentRepository.findByEmailAgente(getEmailAutenticato());
    }

    private List<ImmobileEImmagini> getImmagini(List<Immobile> candidati) {

        List<ImmobileEImmagini> immobileEImmagini = new ArrayList<>();

        try {
            storageManager = new GoogleCloudStorageManager();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        storageService = new GoogleCloudStorageService(storageManager);

        for(Immobile i : candidati){
            List<ImmagineImmobile> imm = immagineImmobileRepository.findAllByImmobile(i);
            List<String> immagini = new ArrayList<>();

            for(ImmagineImmobile j : imm){
                try {
                    immagini.add(storageService.downloadHouseImage(j.getUrlImmagine()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            immobileEImmagini.add(new ImmobileEImmagini(i, immagini));
        }

        return immobileEImmagini;
    }
}
