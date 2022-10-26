package uk.ac.manchester.codeverification.service.elegant.api;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Path("/hello-world")
public class ElegantCodeVerificationService {
    static Process process;
    static boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
    static String homeDirectory = System.getProperty("user.home");

    @GET
    @Produces("text/plain")
    public String hello() throws IOException, InterruptedException {

        if (isWindows) {
            process = Runtime.getRuntime().exec(String.format("cmd.exe /c dir %s", homeDirectory));
        } else {
            process = Runtime.getRuntime().exec(String.format("sh -c ls %s", homeDirectory));
        }

        int exitCode = process.waitFor();
        assert exitCode == 0;


        return "Hello, World!, Exit code = " + exitCode;
    }

    @GET
    @Path("hello")
    @Produces("text/plain")
    public String hello2() throws IOException, InterruptedException {
        process = Runtime.getRuntime().exec(String.format("ls", homeDirectory));
        return "Hello2!, Exit code = " + process.waitFor();
    }
}