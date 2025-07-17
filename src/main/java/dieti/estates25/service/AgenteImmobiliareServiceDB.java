package dieti.estates25.service;

import dieti.estates25.database.GoogleCloudStorageManager;
import dieti.estates25.database.GoogleCloudStorageService;
import dieti.estates25.model.*;
import dieti.estates25.model.agenteimmobiliare.AgenteImmobiliare;
import dieti.estates25.model.agenteimmobiliare.AgenteImmobiliarePrincipal;
import dieti.estates25.repositories.*;
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
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class AgenteImmobiliareServiceDB implements UserDetailsService {

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

    public List<AgenteImmobiliare> getAllAgents() {
        return agentRepository.findAll();
    }

    public AgenteImmobiliare getAgentByMail(String mail) {
        Optional<AgenteImmobiliare> agent = this.agentRepository.findById(mail);
        return  agent.orElse(null);
    }

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

    public void updateAgent(AgenteImmobiliare agent) {
        agentRepository.save(agent);
    }

    public ResponseEntity<String> deleteAgent(String mail) {
        if(agentRepository.existsById(mail)) {
            agentRepository.deleteById(mail);
            return ResponseEntity.ok("Agente Immobiliare licenziato correttamente");
        }
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("L'agente immobiliare che vuoi lincenziare non esiste");
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

        Immobile immobile = immobileRepository.findImmobileByIdImmobile(idImmobile);

        if (immobile == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Immobile non trovato");
        }

        if (immobileRepository.countByIndirizzo(immobile.getIndirizzo()) <= 1){
            String idIndirizzo = immobile.getIndirizzo().getIdIndirizzo();
            immobileRepository.delete(immobile);
            indirizzoRepository.deleteById(idIndirizzo);
        }else{
            immobileRepository.delete(immobile);
        }
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

    public ResponseEntity<String> confermaVisista(Visita visita) {
        visita.setId(new VisitaId(visita.getCliente().getEmailCliente(), visita.getImmobile().getIdImmobile()));
        if (!visitaRepository.existsById(visita.getId())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Non puoi confermare una visita che non esiste");
        }
        if (!visita.getImmobile().getAgente().getEmailAgente().equals(getEmailAutenticato())) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Non puoi confermare una visita per un immobile non tuo");
        }
        if (checkData(visita.getDataVisita(), visita.getOraVisita())){
            if (visita.getConferma()) {
                visitaRepository.save(visita);
                return ResponseEntity.ok("Visita confermata");
            }else{
                visitaRepository.delete(visita);
                return ResponseEntity.ok("Visita rifiutata");
            }
        }
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Non puoi accettare una visita in questa data/ora");
    }

    public boolean checkData(Date dataVisita, LocalTime oraVisita) {
        if (dataVisita == null || oraVisita == null) {return false;}

        LocalDate today = LocalDate.now();
        LocalDate visitaDate = dataVisita.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        if (visitaDate.isBefore(today) || visitaDate.isAfter(today.plusDays(14))) {return false;}

        // Fascia oraria ammessa
        LocalTime startTime = LocalTime.of(8, 0);
        LocalTime endTime = LocalTime.of(18, 0);

        // Se non è nell'intervallo base
        if (oraVisita.isBefore(startTime) || oraVisita.isAfter(endTime)) {return false;}

        // Caso speciale: se la data è oggi, l'orario deve essere nel futuro
        if (visitaDate.isEqual(today)) {
            LocalTime now = LocalTime.now();
            return !oraVisita.isBefore(now);
        }

        return true;
    }

    public Immobile getImmobile(String idImmobile) {
        return immobileRepository.findImmobileByIdImmobile(idImmobile);
    }
}
