package uk.ac.manchester.codeverification.service.elegant.jbmc;

import java.io.*;

public class LinuxJBMC extends JBMC {

    String[] environmentVariables;

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
    public void setUpJBMCEnvironment() throws IOException, InterruptedException {

        environmentVariables = new String[] {
                "WORKDIR="      + workDirectory.getAbsolutePath(),
                "JBMC_BIN="     + workDirectory.getAbsolutePath() + "/jbmc/src/jbmc/jbmc",
                "JAVA_MODEL="   + workDirectory.getAbsolutePath() + "/jbmc/lib/java-models-library/target/core-models.jar"};

        //process = Runtime.getRuntime().exec(String.format("sh -c ls %s", homeDirectory));
        String jbmcCommand = "$JBMC_BIN --classpath $JAVA_MODEL:./test-codes my.petty.examples.Simple --unwind 5";
        process = Runtime.getRuntime().exec(cmdPrefix + jbmcCommand, environmentVariables, workDirectory);
        process.waitFor();
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
    public File getHomeDirectory() {
        return homeDirectory;
    }

    @Override
    public String getEnvironmentVariables() {
        StringBuilder sb = new StringBuilder();
        for (String str : environmentVariables)
            sb.append(str).append(", ");
        return sb.substring(0, sb.length() - 1);
    }
}
