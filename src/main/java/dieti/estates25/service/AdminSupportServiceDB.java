package dieti.estates25.service;

import dieti.estates25.database.GoogleCloudStorageManager;
import dieti.estates25.database.GoogleCloudStorageService;
import dieti.estates25.model.ImmagineImmobile;
import dieti.estates25.model.Immobile;
import dieti.estates25.model.ImmobileEImmagini;
import dieti.estates25.model.adminsupport.AdminSupport;
import dieti.estates25.model.adminsupport.AdminSupportPrincipal;
import dieti.estates25.model.agenteimmobiliare.AgenteImmobiliare;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AdminSupportServiceDB implements UserDetailsService {

    private final JWTService jwtService;
    private final AdminSupportRepository adminSupportRepository;
    private final AgenteImmobiliareRepository agenteImmobiliareRepository;
    private final ImmobileRepository immobileRepository;
    private final ImmagineImmobileRepository immagineImmobileRepository;
    private final AgenteImmobiliareServiceDB agentService;
    private final AuthenticationManager authManager;

    public AdminSupportServiceDB(JWTService jwtService,
                                 AdminSupportRepository adminSupportRepository,
                                 AgenteImmobiliareRepository agenteImmobiliareRepository,
                                 ImmobileRepository immobileRepository,
                                 ImmagineImmobileRepository immagineImmobileRepository,
                                 AgenteImmobiliareServiceDB agentService,
                                 @Lazy @Qualifier("adminSupportAuthenticationManager") AuthenticationManager authManager) {
        this.jwtService = jwtService;
        this.adminSupportRepository = adminSupportRepository;
        this.agenteImmobiliareRepository = agenteImmobiliareRepository;
        this.immobileRepository = immobileRepository;
        this.immagineImmobileRepository = immagineImmobileRepository;
        this.agentService = agentService;
        this.authManager = authManager;
    }

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public List<AdminSupport> getAllAdminSupport() {
        return adminSupportRepository.findAll();
    }

    public AdminSupport getAdminSupportByMail(String mail) {
        Optional<AdminSupport> adminSupport = this.adminSupportRepository.findById(mail);
        return  adminSupport.orElse(null);
    }

    public ResponseEntity<String> addAdminSupport(AdminSupport adminSupport) {
        if (adminSupportRepository.findByEmailSupport(adminSupport.getEmailSupport()) != null) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("è già presente un account con questa mail");
        }
        adminSupport.setPasswordSupport(encoder.encode(adminSupport.getPasswordSupport()));
        adminSupportRepository.save(adminSupport);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Admin Support creato correttamente");
    }

    public void updateAdminSupport(AdminSupport adminSupport) {
        adminSupportRepository.save(adminSupport);
    }

    public ResponseEntity<String> deleteAdminSupport(String mail) {
        if(adminSupportRepository.existsById(mail)) {
            adminSupportRepository.deleteById(mail);
            return ResponseEntity.ok().body("Admin Support eliminato");
        }
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Admin Support non trovato");
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AdminSupport adminSupport = adminSupportRepository.findByEmailSupport(username);

        if (adminSupport == null) {
            throw new UsernameNotFoundException("Admin Support non trovato");
        }

        return new AdminSupportPrincipal(adminSupport);
    }

    public ResponseEntity<String> verify(AdminSupport adminSupport) {
        Authentication authentication =
                authManager.authenticate(new UsernamePasswordAuthenticationToken(adminSupport.getEmailSupport(), adminSupport.getPasswordSupport()));

        if(authentication.isAuthenticated()){
            return ResponseEntity.ok(jwtService.generateToken(adminSupport.getEmailSupport(), "adminSupport"));
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

    public ResponseEntity<String> addAgent(AgenteImmobiliare agent) {
        agent.setNomeAgenzia(getAdminSupportByMail(getEmailAutenticato()).getNomeAgenzia());
        return agentService.addAgent(agent);
    }

    public ResponseEntity<String> deleteAgenteImmobiliare(AgenteImmobiliare agent) {
        AgenteImmobiliare temp = agenteImmobiliareRepository.findByEmailAgente(agent.getEmailAgente());
        if (!temp.getNomeAgenzia().getNomeAgenzia().equals(getAdminSupportByMail(getEmailAutenticato()).getNomeAgenzia().getNomeAgenzia())) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Non puoi licenziare dipendenti che non sono della tua agenzia");
        }
        return agentService.deleteAgent(agent.getEmailAgente());
    }

    public List<ImmobileEImmagini> getImmobili() {
        List<Immobile> immobili = immobileRepository.findAllByAgente_NomeAgenzia(getAdminSupportByMail(getEmailAutenticato()).getNomeAgenzia());
        List<ImmobileEImmagini> candidati = getImmagini(immobili);
        candidati = candidati.stream().filter(immobileEImmagini -> !immobileEImmagini.getImmobile().getVenduto()).toList();

        for (ImmobileEImmagini i : candidati) {
            i.getImmobile().getAgente().setPasswordAgente(null);
            i.getImmobile().getAgente().setNomeAgenzia(null);
        }
        return candidati;
    }

    private List<ImmobileEImmagini> getImmagini(List<Immobile> candidati) {
        GoogleCloudStorageService storageService;
        GoogleCloudStorageManager storageManager;

        List<ImmobileEImmagini> immobileEImmagini = new ArrayList<>();

        try {
            storageManager = new GoogleCloudStorageManager();
        } catch (IOException e) {
            return new ArrayList<>();
        }
        storageService = new GoogleCloudStorageService(storageManager);

        for(Immobile i : candidati){
            List<ImmagineImmobile> imm = immagineImmobileRepository.findAllByImmobile(i);
            List<String> immagini = new ArrayList<>();

            for(ImmagineImmobile j : imm){
                try {
                    immagini.add(storageService.downloadHouseImage(j.getUrlImmagine()));
                } catch (IOException e) {
                    return new ArrayList<>();
                }
            }

            immobileEImmagini.add(new ImmobileEImmagini(i, immagini));
        }

        return immobileEImmagini;
    }

    public AdminSupport getSpecifiche() {
        return adminSupportRepository.findByEmailSupport(getEmailAutenticato());
    }

    public List<AgenteImmobiliare> getAgenti() {
        AdminSupport support = adminSupportRepository.findByEmailSupport(getEmailAutenticato());
        List<AgenteImmobiliare> agenti = agenteImmobiliareRepository.findByNomeAgenzia(support.getNomeAgenzia());
        for (AgenteImmobiliare a : agenti){
            a.setPasswordAgente(null);
        }
        return agenti;
    }
}
