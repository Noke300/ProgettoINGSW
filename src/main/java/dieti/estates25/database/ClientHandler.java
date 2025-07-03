package dieti.estates25.database;

import java.io.*;
import java.util.logging.Logger;

import org.json.JSONObject;

public class ClientHandler extends Thread {
    private GoogleCloudStorageService storageService;
    private StorageManagerInterface storageManager;
    private static final Logger logger = Logger.getLogger(ClientHandler.class.getName());
    private static final String SUCCESS = "success";
    private static final String ERROR = "error";
    private static final String STATUS = "status";
    private static final String MESSAGE = "message";
    /**
     * 
     * Costruttore
     * @throws IOException
     */
    public ClientHandler() throws IOException {
        this.storageManager = new GoogleCloudStorageManager();
    }
   
   /*
   public JSONObject handleUploadRequest(JSONObject request) {
	    JSONObject response = new JSONObject();
	    try {
	        String fileName = request.getString("fileName");
	        String base64Data = request.getString("fileData");
	        int idCartella = request.getInt("id");
	        storageService = new GoogleCloudStorageService(storageManager);
	        //String fileUrl = storageService.uploadHouseImage(fileName, base64Data, idCartella);

	        response.put(STATUS, SUCCESS);
	       // response.put("fileUrl", fileUrl);
	    } catch (IOException e) {
	        response.put(STATUS, ERROR);
	        response.put(MESSAGE, "Errore durante l'upload: " + e.getMessage());
	    }
	    return response;
	}*/
   
   public JSONObject handleDownloadRequest(JSONObject request) {
       JSONObject response = new JSONObject();
       try {

           String fileName = request.getString("fileName");
           storageService = new GoogleCloudStorageService(storageManager);

           String base64Image = storageService.downloadHouseImage(fileName);
           response.put(STATUS, SUCCESS);
           response.put("fileData", base64Image);
       } catch (Exception e) {
           response.put(STATUS, ERROR);
           response.put(MESSAGE, "Errore durante il download: " + e.getMessage());
       }
       return response;
   }
   public void handleDeleteRequest (JSONObject request) {
       String folderName = request.getString("folderName");
        storageManager.deleteFolder(folderName);
   }
}

