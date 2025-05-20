package dieti.estates25.controllers;

import dieti.estates25.config.TokenBlacklist;
import dieti.estates25.model.Immobile;
import dieti.estates25.repositories.ImmobileRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class AuthController {

    @Autowired
    private TokenBlacklist tokenBlacklist;

    @Autowired
    private ImmobileRepository immobileRepository;

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            tokenBlacklist.blacklist(token);
            return ResponseEntity.ok("Logout effettuato con successo");
        }

        return ResponseEntity
                .badRequest()
                .body("Token non presente o non valido");
    }

    @PostMapping("/cerca-immobili")
    public List<Immobile> findImmobiliVicini(@RequestBody Immobile immobile) {
        double centroLat = immobile.getLatitudine().doubleValue();
        double centroLon = immobile.getLongitudine().doubleValue();
        double raggioKm = 10;
        double delta = raggioKm / 111.0; // approx conversion km to degrees

        List<Immobile> candidati = immobileRepository.findWithinBounds(
                centroLat - delta, centroLat + delta,
                centroLon - delta, centroLon + delta
        );

        return candidati.stream()
                .filter(i -> calcolaDistanza(
                        i.getLatitudine().doubleValue(), i.getLongitudine().doubleValue(),
                        centroLat, centroLon) <= raggioKm)
                .collect(Collectors.toList());
    }


    private static double calcolaDistanza(double lat1, double lon1, double lat2, double lon2) {
        double raggioTerra = 6371; // km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return raggioTerra * c;
    }

}
