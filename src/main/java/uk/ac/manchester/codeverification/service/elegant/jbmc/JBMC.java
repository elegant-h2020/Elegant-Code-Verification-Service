package uk.ac.manchester.codeverification.service.elegant.jbmc;

import uk.ac.manchester.codeverification.service.elegant.input.Klass;

import java.io.IOException;

public interface JBMC {

    void    setUpJBMCEnvironment()              throws IOException, InterruptedException;
    void    verifyCode(Klass mainClass)         throws IOException, InterruptedException;
    String  getVerificationResult()             throws IOException;
    String  getEnvironmentVariable(String var);

    int     getExitCode();
    String  getOutput();

}
