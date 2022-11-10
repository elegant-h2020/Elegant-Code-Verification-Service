package uk.ac.manchester.codeverification.service.elegant.jbmc;

import jakarta.json.JsonStructure;
import uk.ac.manchester.codeverification.service.elegant.input.Code;

import java.io.IOException;

public interface JBMC {

    void setUpJBMCEnvironment() throws IOException, InterruptedException;
    void verifyCode(Code code) throws IOException, InterruptedException;
    JsonStructure readOutput();
    String getEnvironmentVariable(String var);

    int getExitCode();
    String getOutput();
    int waitFor() throws InterruptedException;
}
