package uk.ac.manchester.codeverification.service.elegant.api;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import uk.ac.manchester.codeverification.service.elegant.jbmc.JBMC;
import uk.ac.manchester.codeverification.service.elegant.jbmc.LinuxJBMC;

import java.io.*;

@Path("/service")
public class ElegantCodeVerificationService {

    private static final String OS;
    private static JBMC jbmc;

    static {
        OS = System.getProperty("os.name").toLowerCase();
    }

    private void initService() throws IOException, InterruptedException {

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

        return "Code Verification Service : START!"                                     + "\n" +
               "Directory: "                + jbmc.getHomeDirectory().getAbsolutePath() + "\n" +
               "Environment Variables: "    + jbmc.getEnvironmentVariables()            + "\n" +
               "Process Output: "           + jbmc.getProcessOutput();
    }

    @GET
    @Path("submit")
    @Produces("text/plain")
    public String submit() {
        return "This will be a submit request!";//, Exit code = " + process.waitFor();
    }
}