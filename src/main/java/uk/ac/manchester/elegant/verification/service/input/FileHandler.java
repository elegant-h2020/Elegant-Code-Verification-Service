package uk.ac.manchester.elegant.verification.service.input;

import jakarta.ws.rs.WebApplicationException;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import java.io.*;
import java.nio.file.Files;

public class FileHandler {

    final static String UPLOAD_PATH = System.getProperty("user.home") + "/Elegant/Elegant-Code-Verification-Service/uploaded/";

    /**
     * Receives a file and stores it in $SERVICE_HOME/uploaded
     * @param fileInputStream
     * @param fileMetaData
     */
    public static void receiveFile(InputStream fileInputStream, FormDataContentDisposition fileMetaData) {
        try {
            int read = 0;
            byte[] bytes = new byte[1024];

            OutputStream out = Files.newOutputStream(new File(UPLOAD_PATH + fileMetaData.getFileName()).toPath());
            while ((read = fileInputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();
        } catch (IOException e) {
            throw new WebApplicationException("Error while uploading file. Please try again !!");
        }
    }
}
