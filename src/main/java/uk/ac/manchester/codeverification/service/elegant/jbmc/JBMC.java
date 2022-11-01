package uk.ac.manchester.codeverification.service.elegant.jbmc;

import java.io.IOException;

public interface JBMC {

    public abstract void    setUpJBMCEnvironment() throws IOException, InterruptedException;
    public abstract void    verifyCode(String mainClass) throws IOException, InterruptedException;
    public abstract String  getVerificationResult() throws IOException;
    public abstract String  getEnvironmentVariable(String var);

}
