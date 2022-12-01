package uk.ac.manchester.codeverification.service.elegant.api;

import jakarta.json.JsonStructure;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import uk.ac.manchester.codeverification.service.elegant.input.ESBMCRequest;
import uk.ac.manchester.codeverification.service.elegant.input.JBMCRequest;
import uk.ac.manchester.codeverification.service.elegant.input.Request;
import uk.ac.manchester.codeverification.service.elegant.output.Entry;
import uk.ac.manchester.codeverification.service.elegant.output.VerificationEntries;
import uk.ac.manchester.codeverification.service.elegant.output.VerificationResult;
import uk.ac.manchester.codeverification.service.elegant.tool.*;

import java.io.*;

@Path("/verification")
public class ElegantCodeVerificationService {

    private static final String OS;
    private static VerificationTool verificationTool;
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

    private void newToolInstance(String tool) {
        if (OS.startsWith("linux")) {
            if (tool.equals("JBMC")) {
                verificationTool = new LinuxJBMC();
            } else if (tool.equals("ESBMC")) {
                verificationTool = new LinuxESBMC();
            } else {
                throw new UnsupportedOperationException("Code verification tool " + tool + " is currently not supported.");
            }
        } else {
            throw new UnsupportedOperationException("Code verification Service is currently not supported for " + OS + ".");
        }
    }

    @POST
    @Path("newJBMCEntry")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response submit(JBMCRequest request) throws IOException, InterruptedException {
        isInitialized();
        newToolInstance("JBMC");
        request.setTool("JBMC");
        return verifyAndStore(request);
    }

    @POST
    @Path("newESBMCEntry")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response submit(ESBMCRequest request) throws IOException, InterruptedException {
        isInitialized();
        newToolInstance("ESBMC");
        request.setTool("ESBMC");
        return verifyAndStore(request);
    }

    public Response verifyAndStore(Request request) throws IOException, InterruptedException {
        // verify
        verificationTool.verifyCode(request);

        // store the output and exit code as a new VerificationResult
        JsonStructure output = verificationTool.readOutput();
        int exitCode = verificationTool.waitFor();
        VerificationResult result = new VerificationResult(output, exitCode);

        // store the result as a new Entry
        long entryId = verificationEntries.registerEntry(new Entry(request, result));

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
                    .entity(e)
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