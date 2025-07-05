package dieti.estates25;

import dieti.estates25.controllers.AgenteImmobiliareController;
import dieti.estates25.database.ClientHandler;
import dieti.estates25.model.Immobile;
import dieti.estates25.service.agenteimmobiliare.AgenteImmobiliareServiceDB;
import io.github.cdimascio.dotenv.Dotenv;
import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@SpringBootApplication
public class BackEndIngswApplication {

    public static void main(String[] args) throws IOException {
        // Carica il file .env
        Dotenv dotenv = Dotenv.load();
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

        SpringApplication.run(BackEndIngswApplication.class, args);


        /*
        JSONObject request = new JSONObject();

        Immobile immobile = new Immobile();

        AgenteImmobiliareController pippo2 = new AgenteImmobiliareController();

        byte[] fileBytes = Files.readAllBytes(Paths.get("C:/Users/Utente/Desktop/PROGETTO/cultura generale200.jpg"));
        byte[] fileBytes2 = Files.readAllBytes(Paths.get("C:/Users/Utente/Desktop/PROGETTO/unovaQuasiCompleta.jpg"));


        List<String> immagini = new ArrayList<>();
        immagini.add(Base64.getEncoder().encodeToString(fileBytes));
        immagini.add(Base64.getEncoder().encodeToString(fileBytes2));

        System.out.println(Base64.getEncoder().encodeToString(fileBytes));
        System.out.println(Base64.getEncoder().encodeToString(fileBytes2));

        List<String> fileNames= new ArrayList<>();
        fileNames.add("pippo1.jpeg");
        fileNames.add("pipppo2.png");

        request.put("fileNames", fileNames);
        request.put("immobile", immobile);
        request.put("immagini", immagini);

        //pippo2.aggiungiImmobile(request);

*/

    }
    
}
