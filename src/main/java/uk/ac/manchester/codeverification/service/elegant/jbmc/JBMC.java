package uk.ac.manchester.codeverification.service.elegant.jbmc;

import uk.ac.manchester.codeverification.service.elegant.input.Klass;

import java.io.IOException;

public interface JBMC {

    public abstract void    setUpJBMCEnvironment() throws IOException, InterruptedException;
    public abstract void    verifyCode(Klass mainClass) throws IOException, InterruptedException;
    public abstract String  getVerificationResult() throws IOException;
    public abstract String  getEnvironmentVariable(String var);

}
