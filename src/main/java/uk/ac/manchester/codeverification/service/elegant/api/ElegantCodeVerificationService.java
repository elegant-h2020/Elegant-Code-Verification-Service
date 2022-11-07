package uk.ac.manchester.codeverification.service.elegant.api;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
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
    public Response serviceStart() throws IOException, InterruptedException {

        initService();

        return Response
                .status(Response.Status.OK)
                .type(MediaType.TEXT_PLAIN_TYPE)
                .entity("Code Verification Service : START!\n")
                .build();
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
    public Response submit(Klass klass) throws IOException, InterruptedException {
        isInitialized();
        newJBMCInstance();
        jbmc.verifyCode(klass);
        int entryId = verificationEntries.registerEntry(jbmc);
        return Response
                .status(Response.Status.ACCEPTED)
                .type(MediaType.TEXT_PLAIN_TYPE)
                .entity("New code verification request has been registered (#" + entryId + ")\n")
                .build();
    }

    /**
     * Get the verification outcome of an entry.
     */
    @GET
    @Path("getEntry")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEntry(@QueryParam("entryId") String entryId) throws IOException, InterruptedException {
        isInitialized();
        int id = Integer.parseInt(entryId);
        jbmc = verificationEntries.getEntry(id);

        if (jbmc != null) {
            return Response
                    .status(Response.Status.OK)
                    .entity(jbmc.getOutput())
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } else {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .type(MediaType.TEXT_PLAIN)
                    .entity("Invalid Entry!!")
                    .build();
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