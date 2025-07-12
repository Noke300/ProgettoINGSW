package dieti.estates25.controllers;

import dieti.estates25.config.TokenBlacklist;
import dieti.estates25.database.GoogleCloudStorageManager;
import dieti.estates25.database.GoogleCloudStorageService;
import dieti.estates25.model.*;
import dieti.estates25.repositories.ClienteRepository;
import dieti.estates25.repositories.ImmagineImmobileRepository;
import dieti.estates25.repositories.ImmobileRepository;
import dieti.estates25.repositories.OffertaRepository;
import dieti.estates25.service.NotificaService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

@RestController
public class AuthController {

    @Autowired
    private TokenBlacklist tokenBlacklist;

    @Autowired
    private ImmobileRepository immobileRepository;

    @Autowired
    private OffertaRepository offertaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ImmagineImmobileRepository immagineImmobileRepository;

    @Autowired
    private NotificaService notificaService;

    private GoogleCloudStorageManager storageManager;

    private GoogleCloudStorageService storageService;

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
        List<Offerta> offerte = offertaRepository.findAllByImmobileIdImmobileEquals(immobile.getIdImmobile());
        for(Offerta o : offerte) {
            if (o.getCliente() != null)
                o.getCliente().setPasswordCliente(null);
            o.getImmobile().setAgente(null);
        }
        return offerte;
    }

    @PostMapping("/cerca-immobili")
    public List<ImmobileEImmagini> findImmobiliVicini(@RequestBody CoordinateFiltri couple) {
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

        if(couple.getFiltri() != null) candidati = filtraImmobili(candidati, couple.getFiltri());

        return(getImmagini(candidati));
    }

    @PostMapping("/offerta")
    public ResponseEntity<String> newOfferta(@RequestBody Offerta offerta) {
        if(getAuthorities() == null) return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        if(getAuthorities().equals("[CLIENT]")){
            offerta.setCliente(clienteRepository.findByEmailCliente(getEmailAutenticato()));
            offerta.setContrOfferta(false);
            offerta.setDataOfferta(LocalDateTime.now());
            offerta.setEsitoOfferta(false);
            offerta = offertaRepository.save(offerta);

            notificaService.inviaNotifica(offerta.getImmobile().getAgente().getEmailAgente(), "Hanno fatto una nuova offerta");
        }else if(getAuthorities().equals("[AGENTE_IMMOBILIARE]")){
            offerta.setContrOfferta(true);
            offerta.setDataOfferta(LocalDateTime.now());
            offerta.setEsitoOfferta(false);
            offertaRepository.save(offerta);
        }
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Offerta inserita correttamente");
    }

    @GetMapping("/whoAmI")
    public String getAutentication(){
        return getAuthorities();
        //[CLIENT], [AGENTE_IMMOBILIARE], [ADMIN_SUPPORT], [ADMIN], null.
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

    private List<Immobile> filtraImmobili(List<Immobile> candidati, Filtri filtri) {
        if (candidati.isEmpty()) {
            return candidati;
        }

        // Inizia con un predicato che accetta tutto
        Predicate<Immobile> filtroComplessivo = immobile -> true;

        // Aggiungi i filtri booleani solo se sono impostati a true
        if (filtri.getAscensore()) {
            filtroComplessivo = filtroComplessivo.and(Immobile::getAscensore);
        }
        if (filtri.getBalcone()) {
            filtroComplessivo = filtroComplessivo.and(Immobile::getBalcone);
        }
        if (filtri.getCantina()) {
            filtroComplessivo = filtroComplessivo.and(Immobile::getCantina);
        }
        if (filtri.getPortineria()) {
            filtroComplessivo = filtroComplessivo.and(Immobile::getPortineria);
        }
        if (filtri.getClimatizzazione()) {
            filtroComplessivo = filtroComplessivo.and(Immobile::getClimatizzazione);
        }

        // Aggiungi i filtri basati su valori specifici
        if (filtri.getClasseEnergetica() != null) {
            filtroComplessivo = filtroComplessivo.and(i -> i.getClasseEnergetica().equals(filtri.getClasseEnergetica()));
        }
        if (filtri.getNumBagni() != 0) {
            filtroComplessivo = filtroComplessivo.and(i -> i.getNumBagni() >= filtri.getNumBagni());
        }
        if (filtri.getNumStanze() != 0) {
            filtroComplessivo = filtroComplessivo.and(i -> i.getNumStanze() >= filtri.getNumStanze());
        }
        if (filtri.getPiano() != 0) {
            filtroComplessivo = filtroComplessivo.and(i -> i.getPiano() >= filtri.getPiano());
        }

        // Gestione del filtro prezzo
        if (filtri.getPrezzoMin() != 0 && filtri.getPrezzoMax() != 0) {
            filtroComplessivo = filtroComplessivo.and(i -> i.getPrezzo() >= filtri.getPrezzoMin() && i.getPrezzo() <= filtri.getPrezzoMax());
        } else if (filtri.getPrezzoMin() != 0) { // Max è 0
            filtroComplessivo = filtroComplessivo.and(i -> i.getPrezzo() >= filtri.getPrezzoMin());
        } else if (filtri.getPrezzoMax() != 0) { // Min è 0
            filtroComplessivo = filtroComplessivo.and(i -> i.getPrezzo() <= filtri.getPrezzoMax());
        }

        if (filtri.getSuperficie() != 0) {
            filtroComplessivo = filtroComplessivo.and(i -> i.getSuperficie() >= filtri.getSuperficie());
        }
        if (filtri.getTipoImmobile() != null) {
            filtroComplessivo = filtroComplessivo.and(i -> i.getTipoImmobile().equals(filtri.getTipoImmobile()));
        }
        if (filtri.getTipoVendita() != null) {
            filtroComplessivo = filtroComplessivo.and(i -> i.getTipoVendita().equals(filtri.getTipoVendita()));
        }

        return candidati.stream()
                .filter(filtroComplessivo)
                .toList();
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

    public String getAuthorities() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userDetails.getAuthorities().toString();
        }
        return null;
    }

    public String getEmailAutenticato() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userDetails.getUsername();
        }
        return null;
    }


}
