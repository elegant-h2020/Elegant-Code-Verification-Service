package uk.ac.manchester.elegant.verification.service.task.request;

import jakarta.json.*;
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

    private static JsonObject getJsonObjectFromFile(InputStream fileInputStream) {
        // read the json file
        JsonReader reader = Json.createReader(new InputStreamReader(fileInputStream));
        JsonObject jsonObject = reader.read().asJsonObject();

        reader.close();
        return jsonObject;
    }

    /**
     * De-serialize the json file to either a {@link JBMCRequest} or a {@link ESBMCRequest} Java object in a custom manner.
     */
    public static Request receiveRequest(InputStream requestInputStream) {
        JsonObject jsonObject = getJsonObjectFromFile(requestInputStream);

        final String tool = jsonObject.getString("tool");

        if (tool.equals("JBMC")) {
            final String className = jsonObject.getString("className");
            final boolean isMethod = jsonObject.getBoolean("isMethod");
            final String methodName = jsonObject.getString("methodName");

            return new JBMCRequest(className, isMethod, methodName);

        } else if (tool.equals("ESBMC")) {
            final String fileName = jsonObject.getString("fileName");

            return new ESBMCRequest(fileName);

        } else {
            return null;
        }
    }
}
