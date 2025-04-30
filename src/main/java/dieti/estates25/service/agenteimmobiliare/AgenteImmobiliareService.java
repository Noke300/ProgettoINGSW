package dieti.estates25.service.agenteimmobiliare;

import dieti.estates25.model.agenteimmobiliare.AgenteImmobiliare;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AgenteImmobiliareService {
    List<AgenteImmobiliare> getAllAgents();

    AgenteImmobiliare getAgentByMail(String mail);

    ResponseEntity<String> addAgent(AgenteImmobiliare agent);

    void updateAgent(String mail, AgenteImmobiliare agent);

    void deleteAgent(String mail);
}
