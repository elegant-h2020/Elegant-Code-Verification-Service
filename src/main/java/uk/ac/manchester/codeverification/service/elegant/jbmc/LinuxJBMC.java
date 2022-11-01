package uk.ac.manchester.codeverification.service.elegant.jbmc;

import java.io.*;
import java.util.Map;

public class LinuxJBMC extends JBMC {

    ProcessBuilder      jbmcProcessBuilder;
    Process             jbmcProcess;
    Map<String, String> environment;

    static String cmdPrefix = "sh -c ";

    /**
     * Initialize environment and the jbmc process.
     */
    public LinuxJBMC() throws IOException, InterruptedException {
        super();
        this.homeDirectory = new File(System.getProperty("user.home"));
        this.workDirectory = new File(homeDirectory.getPath() + "/Elegant/CBMC");
        setUpJBMCEnvironment();
    }

    @Override
    public void setUpJBMCEnvironment() {

        environment = jbmcProcessBuilder.environment();

        environment.put("PATH_TO_JBMC", "/Elegant/CBMC");
        environment.put("WORKDIR", System.getProperty("user.home") + environment.get("PATH_TO_JBMC"));
        environment.put("JBMC_BIN", environment.get("WORKDIR") + "/jbmc/src/jbmc/jbmc");
        environment.put("JAVA_MODEL", environment.get("WORKDIR") + "/jbmc/lib/java-models-library/target/core-models.jar");
        environment.put("CLASSPATH", environment.get("JAVA_MODEL") + ":./test-codes");
    }

    @Override
    public String getProcessOutput() throws IOException {
        StringBuilder sb = new StringBuilder();

        String line;
        //Read the input stream connected to the normal output of the subprocess.
        try (BufferedReader processOutputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));) {
            while ((line = processOutputReader.readLine()) != null) {
                sb.append(line + System.lineSeparator());
            }
        }
        sb.append(" <endOfInputStream> ");

        //Read the input stream connected to the error output of the subprocess.
        try (BufferedReader processOutputReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));) {
            while ((line = processOutputReader.readLine()) != null) {
                sb.append(line + System.lineSeparator());
            }
        }
        sb.append(" <endOfErrorStream> ");

        return sb.substring(0, sb.length() - 1);
    }

    @Override
    public String getEnvironmentVariable(String key) {
        return environment.get(key);
    }
}
