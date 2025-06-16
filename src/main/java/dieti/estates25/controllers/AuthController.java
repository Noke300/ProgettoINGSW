package dieti.estates25.controllers;

import dieti.estates25.config.TokenBlacklist;
import dieti.estates25.model.CoordinateFiltri;
import dieti.estates25.model.Filtri;
import dieti.estates25.model.Immobile;
import dieti.estates25.model.Offerta;
import dieti.estates25.repositories.ImmobileRepository;
import dieti.estates25.repositories.OffertaRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class AuthController {

    @Autowired
    private TokenBlacklist tokenBlacklist;

    @Autowired
    private ImmobileRepository immobileRepository;

    @Autowired
    private OffertaRepository offertaRepository;

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

    @PostMapping("/visualizza-offerte")
    public List<Offerta> visualizzaOfferte(@RequestBody Immobile immobile) {
        if (immobile.getIdImmobile() == null) return new ArrayList<>();
        //TODO maagri meglio lanciare un eccezione o dare un messaggio d'errore
        return offertaRepository.findAllByImmobileIdImmobileEquals(immobile.getIdImmobile());
    }

    @PostMapping("/cerca-immobili")
    public List<Immobile> findImmobiliVicini(@RequestBody CoordinateFiltri couple) {
        double centroLat = couple.getIndirizzo().getLatitudine();
        double centroLon = couple.getIndirizzo().getLongitudine();
        double raggioKm = 3;
        double delta1 = kmToDegreesLatitude(raggioKm);
        double delta2 = kmToDegreesLongitude(raggioKm, delta1);

        List<Immobile> candidati = immobileRepository.findWithinBounds(
                centroLat - delta1, centroLat + delta1,
                centroLon - delta2, centroLon + delta2
        );

        candidati = candidati.stream()
                .filter(i -> calcolaDistanza(
                        i.getIndirizzo().getLatitudine(), i.getIndirizzo().getLongitudine(),
                        centroLat, centroLon) <= raggioKm)
                .toList();

        System.out.println(candidati);

        if(couple.getFiltri() != null) return filtraImmobili(candidati, couple.getFiltri());
        return candidati;
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

    // Conversione km -> gradi latitudine
    private double kmToDegreesLatitude(double km) {
        return km / 111.32;
    }

    // Conversione km -> gradi longitudine (serve la latitudine in gradi)
    private double kmToDegreesLongitude(double km, double latitudeDegrees) {
        double latitudeRadians = Math.toRadians(latitudeDegrees);
        double kmPerDegree = 111.32 * Math.cos(latitudeRadians);
        return km / kmPerDegree;
    }

    private List<Immobile> filtraImmobili(List<Immobile> candidati, Filtri filtri){
        if (candidati.isEmpty()) return candidati;
        if (filtri.getAscensore()) candidati = candidati.stream().filter(Immobile::getAscensore)
                .toList();
        if (filtri.getBalcone()) candidati = candidati.stream().filter(Immobile::getBalcone)
                .toList();
        if (filtri.getCantina()) candidati = candidati.stream().filter(Immobile::getCantina)
                .toList();
        if (filtri.getPortineria()) candidati = candidati.stream().filter(Immobile::getPortineria)
                .toList();
        if (filtri.getClimatizzazione()) candidati = candidati.stream().filter(Immobile::getClimatizzazione)
                .toList();
        if (filtri.getClasseEnergetica() != null) candidati = candidati.stream().filter(i -> i.getClasseEnergetica().equals(filtri.getClasseEnergetica()))
                .toList();
        if (filtri.getNumBagni() != 0) candidati = candidati.stream().filter(i -> i.getNumBagni() >= filtri.getNumBagni())
                .toList();
        if (filtri.getNumStanze() != 0) candidati = candidati.stream().filter(i -> i.getNumStanze() >= filtri.getNumStanze())
                .toList();
        if (filtri.getPiano() != 0) candidati = candidati.stream().filter(i -> i.getPiano() >= filtri.getPiano())
                .toList();
        if (filtri.getPrezzoMin() != 0 && filtri.getPrezzoMax() != 0) {
            candidati = candidati.stream().filter(i -> i.getPrezzo() >= filtri.getPrezzoMin() && i.getPrezzo() <= filtri.getPrezzoMax())
                    .toList();
        } else if (filtri.getPrezzoMin() != 0 && filtri.getPrezzoMax() == 0) {
            candidati = candidati.stream().filter(i -> i.getPrezzo() >= filtri.getPrezzoMin())
                    .toList();
        } else if (filtri.getPrezzoMin() == 0 && filtri.getPrezzoMax() != 0){
            candidati = candidati.stream().filter(i -> i.getPrezzo() <= filtri.getPrezzoMax())
                    .toList();
        }
        if (filtri.getSuperficie() != 0) candidati = candidati.stream().filter(i -> i.getSuperficie() >= filtri.getSuperficie())
                .toList();
        if (filtri.getTipoImmobile() != null) candidati = candidati.stream().filter(i -> i.getTipoImmobile().equals(filtri.getTipoImmobile()))
                .toList();
        if (filtri.getTipoVendita() != null) candidati = candidati.stream().filter(i -> i.getTipoVendita().equals(filtri.getTipoVendita()))
                .toList();
        return candidati;
    }


}
