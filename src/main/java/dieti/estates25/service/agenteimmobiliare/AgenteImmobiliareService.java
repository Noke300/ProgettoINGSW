package dieti.estates25.service.agenteimmobiliare;

import dieti.estates25.model.Immobile;
import dieti.estates25.model.agenteimmobiliare.AgenteImmobiliare;
import dieti.estates25.model.agenziaimmobiliare.AgenziaImmobiliare;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AgenteImmobiliareService {
    List<AgenteImmobiliare> getAllAgents();

    AgenteImmobiliare getAgentByMail(String mail);

    ResponseEntity<String> addAgent(AgenteImmobiliare agent);

    void updateAgent(String mail, AgenteImmobiliare agent);

    ResponseEntity<String> deleteAgent(String mail);
}
