package uk.ac.manchester.codeverification.service.elegant.jbmc;

import jakarta.json.*;
import uk.ac.manchester.codeverification.service.elegant.input.Klass;

import java.io.*;
import java.util.Map;

public class LinuxJBMC implements JBMC {

    ProcessBuilder      jbmcProcessBuilder;
    Map<String, String> environment;

    Process             jbmcProcess;
    int                 exitCode;
    String              output;

     // TODO: run with sh for compatibility.
     //static String cmdPrefix = "sh -c ";

    /**
     * Initialize environment and the jbmc process.
     */
    public LinuxJBMC() {
        super();
        this.jbmcProcessBuilder = new ProcessBuilder();
        setUpJBMCEnvironment();
        jbmcProcessBuilder.directory(new File(environment.get("WORKDIR")));
    }

    @Override
    public void setUpJBMCEnvironment() {

        environment = jbmcProcessBuilder.environment();

        environment.put("PATH_TO_JBMC", "/Elegant/CBMC");
        environment.put("WORKDIR", System.getProperty("user.home") + environment.get("PATH_TO_JBMC"));
        environment.put("JBMC_BIN", environment.get("WORKDIR") + "/jbmc/src/jbmc/jbmc");
        environment.put("JAVA_MODEL", environment.get("WORKDIR") + "/jbmc/lib/java-models-library/target/core-models.jar");
        environment.put("SERVICE_DIR", System.getProperty("user.home") + "/Elegant/Elegant-Code-Verification-Service");
        //String testCasesPath = System.getProperty("user.home") + "/Elegant/Elegant-Code-Verification-Service/test-cases";
        environment.put("TEST_CASES", environment.get("SERVICE_DIR") + "/test-cases");
        environment.put("CLASSPATH", environment.get("JAVA_MODEL") + ":" + environment.get("TEST_CASES"));
        environment.put("OUTPUT", environment.get("SERVICE_DIR") + "/output");
    }

    /**
     * Starts a Linux JBMC process to verify a Java program.
     * @param mainClass is the main class of the program to be verified.
     */
    @Override
    public void verifyCode(Klass mainClass) throws IOException {
        final String program = mainClass.getClassname();
        jbmcProcessBuilder.command(environment.get("JBMC_BIN"), "--json-ui", "--classpath", environment.get("CLASSPATH"), program, "--unwind", "5");
        this.jbmcProcess = jbmcProcessBuilder.start();
    }

    @Override
    public JsonStructure readOutput() {
        InputStream inputStream = this.jbmcProcess.getInputStream();
        JsonReader jsonReader = Json.createReader(new InputStreamReader(inputStream));

        return jsonReader.read();
    }

    @Override
    public String getEnvironmentVariable(String key) {
        return environment.get(key);
    }

    @Override
    public int getExitCode() {
        return exitCode;
    }

    @Override
    public String getOutput() {
        return output;
    }

    @Override
    public int waitFor() throws InterruptedException {
        return this.jbmcProcess.waitFor();
    }
}
