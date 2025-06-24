package dieti.estates25;

import dieti.estates25.database.ClientHandler;
import io.github.cdimascio.dotenv.Dotenv;
import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

@SpringBootApplication
public class BackEndIngswApplication {

    public static void main(String[] args) throws IOException {
        // Carica il file .env
        Dotenv dotenv = Dotenv.load();
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

        SpringApplication.run(BackEndIngswApplication.class, args);

        /*JSONObject request = new JSONObject();
        request.put("fileName", "pippo.jpg");
        byte[] fileBytes = Files.readAllBytes(Paths.get("C:/Users/simon/Desktop/Foto/Stupide/Arthur.png"));
        request.put("fileData", Base64.getEncoder().encodeToString(fileBytes));
        request.put("id", 43);

        ClientHandler clientHandler = new ClientHandler();

        String url = clientHandler.handleUploadRequest(request).getString("fileUrl");

        JSONObject request2 = new JSONObject();
        request2.put("fileName", url);

        System.out.println(clientHandler.handleDownloadRequest(request2).getString("fileData"));

        JSONObject request3 = new JSONObject();

        request3.put("folderName", "immobili/cartella42");

        clientHandler.handleDeleteRequest(request3);
        */
    }
    
}
