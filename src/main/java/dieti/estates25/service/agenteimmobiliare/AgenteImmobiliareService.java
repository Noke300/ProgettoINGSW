package dieti.estates25.service.agenteimmobiliare;

import dieti.estates25.model.agenteimmobiliare.AgenteImmobiliare;

import java.util.List;

public interface AgenteImmobiliareService {
    List<AgenteImmobiliare> getAllAgents();

    AgenteImmobiliare getAgentByMail(String mail);

    void addAgent(AgenteImmobiliare agent);

    void updateAgent(String mail, AgenteImmobiliare agent);

    void deleteAgent(String mail);
}
