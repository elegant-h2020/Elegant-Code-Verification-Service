/*
 * This file is part of the ELEGANT Code Verification Service.
 * URL: https://github.com/elegant-h2020/Elegant-Code-Verification-Service
 *
 * Copyright (c) 2022-2023, APT Group, Department of Computer Science,
 * The University of Manchester. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.ac.manchester.elegant.verification.service.task.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.ws.rs.WebApplicationException;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import uk.ac.manchester.elegant.verification.service.api.ElegantCodeVerificationService;
import uk.ac.manchester.elegant.verification.service.task.VerificationTask;
import uk.ac.manchester.elegant.verification.service.tool.linux.ESBMC;
import uk.ac.manchester.elegant.verification.service.tool.linux.JBMC;

import java.io.*;
import java.nio.file.Files;

public class FileHandler {

    final static String UPLOAD_PATH = System.getProperty("user.home") + "/Elegant/Elegant-Code-Verification-Service/uploaded/";

    /**
     * Receives a file and stores it in $SERVICE_HOME/uploaded
     * @param fileInputStream
     * @param fileMetaData
     */
    public static boolean receiveFile(InputStream fileInputStream, FormDataContentDisposition fileMetaData, Request request) {
        try {
            int read = 0;
            byte[] bytes = new byte[1024];
            String relativeClassPath = null;
            File file = null;
            if (request instanceof JBMCRequest) {
                relativeClassPath = ((JBMCRequest) request).getRelativeClassPath();
                if (relativeClassPath != null) {
                    relativeClassPath = UPLOAD_PATH + relativeClassPath;
                } else {
                    relativeClassPath = UPLOAD_PATH;
                }
                resolveDirectory(relativeClassPath);
                file = new File((relativeClassPath + File.separator + fileMetaData.getFileName()));
            } else {
                file = new File(UPLOAD_PATH + fileMetaData.getFileName());
            }

            OutputStream out = Files.newOutputStream(file.toPath());
            while ((read = fileInputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();

            return true;
        } catch (IOException e) {
            //throw new WebApplicationException("Error while uploading file. Please try again !!");
            return false;
        }
    }

    private static void resolveDirectory(String filePathName) {
        File idDirectory = new File(filePathName);
        if (!idDirectory.exists()) {
            idDirectory.mkdirs();
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
            final boolean isJarFile = jsonObject.getBoolean("isJarFile");
            final String jarName = jsonObject.getString("jarName");
            final boolean isMethod = jsonObject.getBoolean("isMethod");
            final String methodName = jsonObject.getString("methodName");

            return new JBMCRequest(className, isJarFile, jarName, isMethod, methodName);

        } else if (tool.equals("ESBMC")) {
            final String fileName = jsonObject.getString("fileName");
            final boolean isFunction = jsonObject.getBoolean("isFunction");
            final String functionName = jsonObject.getString("functionName");

            return new ESBMCRequest(fileName, isFunction, functionName);

        } else {
            return null;
        }
    }

    public static File writeObjectToJsonFile(VerificationTask verificationTask, int id) {
        String serviceUId = ElegantCodeVerificationService.getServiceUId();
        // create a new File object
        File file = null;
        if (verificationTask.getVerificationTool() instanceof ESBMC) {
            file = new File(verificationTask.getVerificationTool().getEnvironmentVariable("OUTPUT") + File.separator + id + File.separator + "output.log");
        } else if (verificationTask.getVerificationTool() instanceof JBMC) {
            String filename = "/service/files/" + serviceUId + "_" + id + ".json";
            file = new File(filename);
            try {
                // write the object to the file
                ObjectMapper mapper = new ObjectMapper();
                mapper.writeValue(file, verificationTask);
            } catch (IOException ignored) {

            }
        } else {
            return null;
        }

        return file;
    }
}
