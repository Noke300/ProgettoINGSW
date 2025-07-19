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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
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
        offerte = offerte.stream().filter(offerta -> !offerta.getContrOfferta()).toList();
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
            offerta.setEsitoOfferta(null);
            offerta = offertaRepository.save(offerta);
            offerta.setImmobile(immobileRepository.findImmobileByIdImmobile(offerta.getImmobile().getIdImmobile()));

            notificaService.inviaNotifica(offerta.getImmobile().getAgente().getEmailAgente(), "Hanno fatto una nuova offerta");
        }else if(getAuthorities().equals("[AGENTE_IMMOBILIARE]")){
            offerta.setContrOfferta(false);
            offerta.setDataOfferta(LocalDateTime.now());
            offerta.setEsitoOfferta(null);
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

        Predicate<Immobile> filtroComplessivo = immobile -> !immobile.getVenduto();

        filtroComplessivo = filtroComplessivo
                .and(filtriBooleani(filtri))
                .and(filtriValori(filtri))
                .and(filtroPrezzo(filtri))
                .and(filtriStringa(filtri));

        return candidati.stream()
                .filter(filtroComplessivo)
                .toList();
    }

    private Predicate<Immobile> filtriBooleani(Filtri f) {
        Predicate<Immobile> p = immobile -> true;

        if (f.getAscensore()) p = p.and(Immobile::getAscensore);
        if (f.getBalcone()) p = p.and(Immobile::getBalcone);
        if (f.getCantina()) p = p.and(Immobile::getCantina);
        if (f.getPortineria()) p = p.and(Immobile::getPortineria);
        if (f.getClimatizzazione()) p = p.and(Immobile::getClimatizzazione);

        return p;
    }

    private Predicate<Immobile> filtriValori(Filtri f) {
        Predicate<Immobile> p = immobile -> true;

        if (checkFiltri(f.getClasseEnergetica(), f.getNumStanze(), f.getNumBagni(), f.getPiano())) {
            if (f.getClasseEnergetica() != null) {
                p = p.and(i -> i.getClasseEnergetica().equals(f.getClasseEnergetica()));
            }
            if (f.getNumBagni() != -1) {
                p = p.and(i -> i.getNumBagni() >= f.getNumBagni());
            }
            if (f.getNumStanze() != 0) {
                p = p.and(i -> i.getNumStanze() >= f.getNumStanze());
            }
            if (f.getPiano() != -2) {
                p = p.and(i -> i.getPiano() >= f.getPiano());
            }
        }

        return p;
    }

    private Predicate<Immobile> filtroPrezzo(Filtri f) {
        double min = f.getPrezzoMin();
        double max = f.getPrezzoMax();

        if (!checkPrezzi(min, max)) return immobile -> false;

        if (min == 0 && max == 0) return immobile -> true;
        if (min != 0 && max != 0) return i -> i.getPrezzo() >= min && i.getPrezzo() <= max;
        if (min != 0) return i -> i.getPrezzo() >= min;
        return i -> i.getPrezzo() <= max;
    }

    private Predicate<Immobile> filtriStringa(Filtri f) {
        Predicate<Immobile> p = immobile -> true;

        if (f.getTipoImmobile() != null) {
            p = p.and(i -> i.getTipoImmobile().equals(f.getTipoImmobile()));
        }
        if (f.getTipoVendita() != null) {
            p = p.and(i -> i.getTipoVendita().equals(f.getTipoVendita()));
        }

        return p;
    }

    public boolean checkFiltri(String classeEnergetica, int numStanze, int numBagni, int piano) {
        if (classeEnergetica != null) {
            List<String> classiValide = Arrays.asList("A+", "A", "B", "C", "D", "E", "F", "G");
            if (!classiValide.contains(classeEnergetica)) {
                return false;
            }
        }
        if (numStanze < 0 || numStanze > 10) return false;
        if (numBagni < -1 || numBagni > 10) return false;
        if (numStanze > 0 && numBagni >= numStanze) return false;
        return piano >= -2 && piano <= 10;
    }

    public boolean checkPrezzi(double prezzoMin, double prezzoMax) {
        if (prezzoMin < 0 || prezzoMin > 800000) return false;
        if (prezzoMax < 0 || prezzoMax > 1000000) return false;
        if (prezzoMax != 0){
            return (prezzoMin <= prezzoMax);
        }
        return true;
    }

    private List<ImmobileEImmagini> getImmagini(List<Immobile> candidati) {

        List<ImmobileEImmagini> immobileEImmagini = new ArrayList<>();
        GoogleCloudStorageManager storageManager;

        try {
            storageManager = new GoogleCloudStorageManager();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        GoogleCloudStorageService storageService = new GoogleCloudStorageService(storageManager);

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
