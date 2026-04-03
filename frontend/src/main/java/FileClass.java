import com.fasterxml.jackson.databind.ser.Serializers;
//import org.apache.commons.io.FileUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Base64;
import java.lang.Object;

public class FileClass {

    public static String encodeFileToBase64(File file) {
        try {
            byte[] fileContent = Files.readAllBytes(file.toPath());
            return Base64.getEncoder().encodeToString(fileContent);
        } catch (IOException e) {
            throw new IllegalStateException("Datei konnte nicht gelesen werden " + file, e);
        }
    }

    public static InputStream decodeBase64toFile(String fileString)  {
        byte[] result = Base64.getDecoder().decode(fileString);
        InputStream inputStream = new ByteArrayInputStream(result);
        return inputStream;
    }

    public static byte[] decodeBase64toFileByteArray(String fileString) {
        byte[] result = Base64.getDecoder().decode(fileString);
        return result;
    }


    public static boolean validateFileImage(File file){
        String[] allowedExtentions = {"jpg", "jpeg", "png"};
        String devidedFileName[] = file.getName().split("\\.");
        String fileExtention = devidedFileName[devidedFileName.length-1];
        for(int i = 0; i < allowedExtentions.length; i++){
            if(fileExtention.toLowerCase().equals(allowedExtentions[i])){
                return true;
            }
        }
        return false;
    }
}
