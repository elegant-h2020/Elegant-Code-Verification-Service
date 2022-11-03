package uk.ac.manchester.codeverification.service.elegant.api;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import uk.ac.manchester.codeverification.service.elegant.input.Klass;
import uk.ac.manchester.codeverification.service.elegant.jbmc.JBMC;
import uk.ac.manchester.codeverification.service.elegant.jbmc.LinuxJBMC;
import uk.ac.manchester.codeverification.service.elegant.output.VerificationEntries;

import java.io.*;

@Path("/verification")
public class ElegantCodeVerificationService {

    private static final String OS;
    private static JBMC jbmc;
    private static VerificationEntries verificationEntries;
    private static boolean isInitialized = false;

    static {
        OS = System.getProperty("os.name").toLowerCase();
    }

    private void initService() {
        verificationEntries = new VerificationEntries();
        isInitialized = true;
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
     * Ensures that any API call can be safely utilized even prior to the explicit initialization of the service.
     */
    private void isInitialized() throws IOException, InterruptedException {
        if (!isInitialized) {
            serviceStart();
        }
    }

    private void newJBMCInstance() {
        if (OS.startsWith("linux")) {
            jbmc = new LinuxJBMC();
        } else {
            throw new UnsupportedOperationException("Code verification Service is currently not supported for " + OS + ".");
        }
    }

    /**
     * Register a new entry for verification.
     */
    @POST
    @Path("newEntry")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String submit(Klass klass) throws IOException, InterruptedException {
        isInitialized();
        newJBMCInstance();
        jbmc.verifyCode(klass);
        int id = verificationEntries.registerEntry(jbmc);
        return "New code verification request has been registered (#" + id + ")\n" + jbmc.getOutput() + "\n";
    }

    /**
     * Get the verification outcome of an entry.
     */
    @GET
    @Path("getEntry")
    @Produces("text/plain")
    public String getEntry(@QueryParam("entryId") String entryId) throws IOException, InterruptedException {
        isInitialized();
        int id = Integer.parseInt(entryId);
        jbmc = verificationEntries.getEntry(id);

        if (jbmc != null) {
            return "Code verification result of entry #" + id + " : \n" +
                    "(exit code:" + jbmc.getExitCode() + ")\n" +
                    jbmc.getOutput() + "\n";
        } else {
            return "Invalid entry id.\n";
        }
    }

    /**
     * Remove an entry.
     */
    @DELETE
    @Path("removeEntry")
    @Produces("text/plain")
    public String removeEntry(@QueryParam("entryId") String entryId) throws IOException, InterruptedException {
        isInitialized();
        return "removeEntry = " + entryId;
    }

    /**
     * List all known verification entries.
     */
    @GET
    @Path("getEntities")
    @Produces("text/plain")
    public String getEntities() throws IOException, InterruptedException {
        isInitialized();
        return "getEntities";
    }
}