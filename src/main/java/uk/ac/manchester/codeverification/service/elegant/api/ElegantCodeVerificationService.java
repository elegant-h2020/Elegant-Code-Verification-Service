package uk.ac.manchester.codeverification.service.elegant.api;

import jakarta.ws.rs.*;
import uk.ac.manchester.codeverification.service.elegant.jbmc.JBMC;
import uk.ac.manchester.codeverification.service.elegant.jbmc.LinuxJBMC;

import java.io.*;

@Path("/verification")
public class ElegantCodeVerificationService {

    private static final String OS;
    private static JBMC jbmc;

    static {
        OS = System.getProperty("os.name").toLowerCase();
    }

    private void initService() {

        if (OS.startsWith("linux")) {
            jbmc = new LinuxJBMC();
        } else {
            throw new UnsupportedOperationException("Code verification Service is currently not supported for " + OS + ".");
        }
    }

    /**
     * Starting Point of the Code Verification Service.
     */
    @GET
    @Produces("text/plain")
    public String serviceStart() throws IOException, InterruptedException {

        initService();

        return "Code Verification Service : START!"                                 + "\n" +
                "PATH_TO_JBMC = "   + jbmc.getEnvironmentVariable("PATH_TO_JBMC")   + "\n" +
                "WORKDIR = "        + jbmc.getEnvironmentVariable("WORKDIR")        + "\n" +
                "JBMC_BIN = "       + jbmc.getEnvironmentVariable("JBMC_BIN")       + "\n" +
                "JAVA_MODEL = "     + jbmc.getEnvironmentVariable("JAVA_MODEL")     + "\n" +
                "CLASSPATH = "      + jbmc.getEnvironmentVariable("CLASSPATH")      + "\n";
    }

    /**
     * Register a new entry for verification.
     */
    @POST
    @Path("newEntry")
    @Produces("text/plain")
    public String submit() throws IOException, InterruptedException {
        jbmc.verifyCode("my.petty.examples.Simple");
        return "Process Output: "  + jbmc.getVerificationResult();
    }

    /**
     * Get the verification outcome of an entry.
     */
    @GET
    @Path("getEntry")
    @Produces("text/plain")
    public String getEntry(@QueryParam("entryId") String entryId) {
        return "getEntry = " + entryId;
    }

    /**
     * Remove an entry.
     */
    @DELETE
    @Path("removeEntry")
    @Produces("text/plain")
    public String removeEntry(@QueryParam("entryId") String entryId) {
        return "removeEntry = " + entryId;
    }

    /**
     * List all known verification entries.
     */
    @GET
    @Path("getEntities")
    @Produces("text/plain")
    public String getEntities() {
        return "getEntities";
    }
}