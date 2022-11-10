package uk.ac.manchester.codeverification.service.elegant.api;

import jakarta.json.JsonStructure;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import uk.ac.manchester.codeverification.service.elegant.input.Code;
import uk.ac.manchester.codeverification.service.elegant.jbmc.JBMC;
import uk.ac.manchester.codeverification.service.elegant.jbmc.LinuxJBMC;
import uk.ac.manchester.codeverification.service.elegant.jbmc.VerificationEntries;
import uk.ac.manchester.codeverification.service.elegant.jbmc.Entry;

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
    public Response submit(Code code) throws IOException, InterruptedException {
        isInitialized();

        // verify
        newJBMCInstance();
        jbmc.verifyCode(code);

        // store the result as a new Entry
        JsonStructure output = jbmc.readOutput();
        int exitCode = jbmc.waitFor();
        long entryId = verificationEntries.registerEntry(new Entry(code, output, exitCode));

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
        Entry e = verificationEntries.getEntry(id);

        if (e != null) {
            return Response
                    .status(Response.Status.OK)
                    .entity(e.getOutput())
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
    public Response removeEntry(@QueryParam("entryId") long entryId) throws IOException, InterruptedException {
        isInitialized();
        Entry deleted = verificationEntries.removeEntry(entryId);
        Response.Status responseStatus = (deleted != null) ? Response.Status.FOUND : Response.Status.NOT_FOUND;
        String responseMsg = (deleted != null) ? "Code verification entry (#" + entryId + ") has been deleted.\n" : "Invalid Entry.\n";
        return Response
                .status(responseStatus)
                .type(MediaType.TEXT_PLAIN_TYPE)
                .entity(responseMsg)
                .build();
    }

    /**
     * List all known verification entries.
     */
    @GET
    @Path("getEntries")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEntries() throws IOException, InterruptedException {
        isInitialized();
        return Response
                .status(Response.Status.ACCEPTED)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(verificationEntries.listEntries())
                .build();
    }
}