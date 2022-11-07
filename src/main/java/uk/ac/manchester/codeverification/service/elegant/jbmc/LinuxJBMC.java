package uk.ac.manchester.codeverification.service.elegant.jbmc;

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
    public void verifyCode(Klass mainClass) throws IOException, InterruptedException {
        final String program = mainClass.getClassname();
        jbmcProcessBuilder.command(environment.get("JBMC_BIN"), "--json-ui", "--classpath", environment.get("CLASSPATH"), program, "--unwind", "5");
        this.jbmcProcess = jbmcProcessBuilder.start();
    }

    @Override
    public String getVerificationResult() throws IOException {
        /*
        // store the output as a Json array
        InputStream inputStream = this.jbmcProcess.getInputStream();
        InputStreamReader reader = new InputStreamReader(inputStream);
        return JsonParser.parseReader(reader).getAsJsonArray();
        */

        StringBuilder sb = new StringBuilder();

        String line;
        //Read the input stream connected to the normal output of the subprocess.
        try (BufferedReader processOutputReader = new BufferedReader(new InputStreamReader(this.jbmcProcess.getInputStream()));) {
            while ((line = processOutputReader.readLine()) != null) {
                sb.append(line + System.lineSeparator());
            }
        }

        //Read the input stream connected to the error output of the subprocess.
        try (BufferedReader processOutputReader = new BufferedReader(new InputStreamReader(jbmcProcess.getErrorStream()));) {
            while ((line = processOutputReader.readLine()) != null) {
                sb.append(line + System.lineSeparator());
            }
        }

        return sb.substring(0, sb.length() - 1);
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

    /*public void iterateOverJsonObjects() {
        // prints all the json objects in the json array
        ListIterator l = this.output.deepCopy().asList().listIterator();
        while (l.hasNext()) {
            JsonObject j = (JsonObject)l.next();
            System.out.println("Iteration: " + j);
        }
    }*/

    @Override
    public int waitFor() throws InterruptedException {
        return this.jbmcProcess.waitFor();
    }
}
