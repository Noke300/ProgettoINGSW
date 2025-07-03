package dieti.estates25.database;

import java.io.IOException;

public class GoogleCloudStorageService {
	
	private StorageManagerInterface storageManager;
	/**
     * 
     * Costruttore
     */
    public GoogleCloudStorageService(StorageManagerInterface storageManager) {
        this.storageManager = storageManager;
    }

    

    public String uploadHouseImage(String fileName, String base64Data, String idCartella) throws IOException {
        return storageManager.uploadFile(fileName, base64Data, idCartella);
    }
    
    
   
    public String downloadHouseImage(String fileName) throws IOException {
        return storageManager.downloadImageAsBase64(fileName);
    }

    public void delete(String folderName) {
        storageManager.deleteFolder(folderName);
    }
}

