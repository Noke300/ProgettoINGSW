package dieti.estates25.database;

import java.io.IOException;

public interface StorageManagerInterface {

	String uploadFile(String fileName, String base64Data, int idCartella) throws IOException;
	
	String downloadImageAsBase64(String fileName) throws IOException;

	void deleteFolder (String folderName);
}
