package uk.ac.manchester.elegant.verification.service.tool;

import uk.ac.manchester.elegant.verification.service.task.request.Request;

import java.io.IOException;

public interface VerificationTool {

    void setUpToolEnvironment() throws IOException, InterruptedException;
    void verifyCode(Request code) throws IOException, InterruptedException;
    Object readOutput();
    String getEnvironmentVariable(String var);
    String getName();
    int getExitCode();
    String getOutput();
    int waitFor() throws InterruptedException;
}
