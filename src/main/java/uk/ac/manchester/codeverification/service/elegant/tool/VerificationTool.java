package uk.ac.manchester.codeverification.service.elegant.tool;

import jakarta.json.JsonStructure;
import uk.ac.manchester.codeverification.service.elegant.input.Request;

import java.io.IOException;

public interface VerificationTool {

    void setUpToolEnvironment() throws IOException, InterruptedException;
    void verifyCode(Request code) throws IOException, InterruptedException;
    JsonStructure readOutput();
    String getEnvironmentVariable(String var);
    String getName();
    int getExitCode();
    String getOutput();
    int waitFor() throws InterruptedException;
}
