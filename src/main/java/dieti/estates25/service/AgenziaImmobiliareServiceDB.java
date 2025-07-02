package dieti.estates25.service;

import dieti.estates25.model.Immobile;
import dieti.estates25.model.adminsupport.AdminSupport;
import dieti.estates25.model.agenteimmobiliare.AgenteImmobiliare;
import dieti.estates25.model.agenziaimmobiliare.AgenziaImmobiliare;
import dieti.estates25.model.agenziaimmobiliare.AgenziaImmobiliarePrincipal;
import dieti.estates25.repositories.AdminSupportRepository;
import dieti.estates25.repositories.AgenteImmobiliareRepository;
import dieti.estates25.repositories.AgenziaImmobiliareRepository;
import dieti.estates25.repositories.ImmobileRepository;
import dieti.estates25.service.adminsupport.AdminSupportServiceDB;
import dieti.estates25.service.agenteimmobiliare.AgenteImmobiliareServiceDB;
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
public class AgenziaImmobiliareServiceDB implements UserDetailsService {

    @Autowired
    private JWTService jwtService;

    @Autowired
    private AgenziaImmobiliareRepository agenziaImmobiliareRepository;

    @Autowired
    private AgenteImmobiliareRepository agenteImmobiliareRepository;

    @Autowired
    private AdminSupportRepository adminSupportRepository;

    @Autowired
    private ImmobileRepository immobileRepository;

    @Autowired
    private AgenteImmobiliareServiceDB agentService;

    @Autowired
    private AdminSupportServiceDB adminSupportService;

    @Autowired
    @Lazy
    @Qualifier("agenziaImmobiliareAuthenticationManager")
    private AuthenticationManager authManager;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public List<AgenziaImmobiliare> getAllAgenzieImmobiliari() {
        return agenziaImmobiliareRepository.findAll();
    }

    public AgenziaImmobiliare getAgenziaByName(String name) {
        Optional<AgenziaImmobiliare> agenzia = this.agenziaImmobiliareRepository.findById(name);
        return agenzia.orElse(null);
    }

    public ResponseEntity<String> addAgenzia(AgenziaImmobiliare agenzia) {
        if (agenziaImmobiliareRepository.findAgenziaImmobiliareByNomeAgenzia(agenzia.getNomeAgenzia()) != null) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("è già presente un agenzia con questo nome");
        }
        agenzia.setPasswordAdmin(encoder.encode(agenzia.getPasswordAdmin()));
        agenziaImmobiliareRepository.save(agenzia);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Agenzia creata correttamente");
    }

    public void updateAgenziaImmobiliare(AgenziaImmobiliare agenzia) {
        agenziaImmobiliareRepository.save(agenzia);
    }

    //TODO testare le cascade
    public void deleteAgenziaImmobiliare(String name) {
        agenziaImmobiliareRepository.deleteById(name);
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AgenziaImmobiliare agenzia = agenziaImmobiliareRepository.findAgenziaImmobiliareByNomeAgenzia(username);

        if (agenzia == null) {
            throw new UsernameNotFoundException("Agenzia Immobiliare non trovata");
        }

        return new AgenziaImmobiliarePrincipal(agenzia);
    }

    public ResponseEntity<String> verify(AgenziaImmobiliare agenzia) {
        AgenziaImmobiliare temp = agenziaImmobiliareRepository.findAgenziaImmobiliaresByEmailAdmin(agenzia.getEmailAdmin());
        if (temp == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Non è stato trovato nessun account con questa mail");
        }
        agenzia.setNomeAgenzia(temp.getNomeAgenzia());
        Authentication authentication =
                authManager.authenticate(new UsernamePasswordAuthenticationToken(agenzia.getNomeAgenzia(), agenzia.getPasswordAdmin()));

        if(authentication.isAuthenticated()){
            return ResponseEntity.ok(jwtService.generateToken(agenzia.getNomeAgenzia(), "admin"));
        }
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Unauthorized");
    }

    public String getNomeAgenzia() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userDetails.getUsername();
        }
        return null;
    }

    public List<AgenteImmobiliare> getAgenti() {
        List<AgenteImmobiliare> agenti = agenteImmobiliareRepository.findByNomeAgenzia(agenziaImmobiliareRepository.findAgenziaImmobiliareByNomeAgenzia(getNomeAgenzia()));
        for (AgenteImmobiliare a : agenti){
            a.setPasswordAgente(null);
        }
        return agenti;
    }

    public List<AdminSupport> getAdminSupport() {
        List<AdminSupport> adminSupports = adminSupportRepository.findByNomeAgenzia(agenziaImmobiliareRepository.findAgenziaImmobiliareByNomeAgenzia(getNomeAgenzia()));
        for (AdminSupport a : adminSupports){
            a.setPasswordSupport(null);
        }
        return adminSupports;
    }

    public ResponseEntity<String> addAgent(AgenteImmobiliare agent) {
        agent.setNomeAgenzia(agenziaImmobiliareRepository.findAgenziaImmobiliareByNomeAgenzia(getNomeAgenzia()));
        return agentService.addAgent(agent);
    }

    public ResponseEntity<String> addAdminSupport(AdminSupport adminSupport) {
        adminSupport.setNomeAgenzia(agenziaImmobiliareRepository.findAgenziaImmobiliareByNomeAgenzia(getNomeAgenzia()));
        return adminSupportService.addAdminSupport(adminSupport);
    }

    public ResponseEntity<String> deleteAdminSupport(AdminSupport adminSupport) {
        AdminSupport temp = adminSupportRepository.findByEmailSupport(adminSupport.getEmailSupport());
        if (!temp.getNomeAgenzia().getNomeAgenzia().equals(getNomeAgenzia())){
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Non sei autorizzato a licenziare un dipendente di un'altra agenzia.");
        }
        return adminSupportService.deleteAdminSupport(adminSupport.getEmailSupport());
    }

    public ResponseEntity<List<Immobile>> deleteAgenteImmobiliare(AgenteImmobiliare agent) {
        AgenteImmobiliare temp = agenteImmobiliareRepository.findByEmailAgente(agent.getEmailAgente());
        if (!temp.getNomeAgenzia().getNomeAgenzia().equals(getNomeAgenzia())){
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(null);
        }
        return agentService.deleteAgent(agent.getEmailAgente());
    }

    public List<Immobile> getImmobili() {
        List<Immobile> immobili =  immobileRepository.findAllByAgente_NomeAgenzia(agenziaImmobiliareRepository.findAgenziaImmobiliareByNomeAgenzia(getNomeAgenzia()));
        for (Immobile i : immobili) {
            i.getAgente().setPasswordAgente(null);
            i.getAgente().setNomeAgenzia(null);
        }
        return immobili;
    }

    public void modificaCredenziali(AgenziaImmobiliare agenzia) {
        AgenziaImmobiliare oldAgenzia = agenziaImmobiliareRepository.findAgenziaImmobiliareByNomeAgenzia(getNomeAgenzia());
        agenzia.setNomeAgenzia(oldAgenzia.getNomeAgenzia());
        agenzia.setEmailAdmin(oldAgenzia.getEmailAdmin());
        if (agenzia.getEmailAgenzia() == null){
            agenzia.setEmailAgenzia(oldAgenzia.getEmailAgenzia());
        }
        if (agenzia.getPasswordAdmin() == null){
            agenzia.setPasswordAdmin(oldAgenzia.getPasswordAdmin());
        } else{
            agenzia.setPasswordAdmin(encoder.encode(agenzia.getPasswordAdmin()));
        }
        if (agenzia.getRegione() == null){
            agenzia.setRegione(oldAgenzia.getRegione());
        }
        updateAgenziaImmobiliare(agenzia);
    }
}
