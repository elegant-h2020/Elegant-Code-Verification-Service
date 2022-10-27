package uk.ac.manchester.codeverification.service.elegant.jbmc;

import java.io.File;
import java.io.IOException;

public abstract class JBMC {

    Process process;
    File    homeDirectory;
    File    workDirectory;

    public abstract void    setUpJBMCEnvironment()  throws IOException, InterruptedException;
    public abstract String  getProcessOutput()      throws IOException;
    public abstract File    getHomeDirectory();
    public abstract String  getEnvironmentVariables();

}
