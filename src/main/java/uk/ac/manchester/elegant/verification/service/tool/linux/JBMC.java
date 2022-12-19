package uk.ac.manchester.elegant.verification.service.tool.linux;

import jakarta.json.*;
import uk.ac.manchester.elegant.verification.service.task.request.JBMCRequest;
import uk.ac.manchester.elegant.verification.service.task.request.Request;
import uk.ac.manchester.elegant.verification.service.tool.VerificationTool;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;

public class JBMC implements VerificationTool {

    ProcessBuilder      jbmcProcessBuilder;
    Map<String, String> environment;

    Process             jbmcProcess;
    int                 exitCode;
    String              output;

    String              toolName;

     // TODO: run with sh for compatibility.
     //static String cmdPrefix = "sh -c ";

    /**
     * Initialize environment and the jbmc process.
     */
    public JBMC() {
        super();
        this.toolName = "JBMC";
        this.jbmcProcessBuilder = new ProcessBuilder();
        setUpToolEnvironment();
        jbmcProcessBuilder.directory(new File(environment.get("WORKDIR")));
    }

    /**
     * If CBMC has been built with CMake:
     *  JBMC_BIN = WORKDIR/build/bin/jbmc
     * If CBMC has been built with Make:
     *  JBMC_BIN = WORKDIR/jbmc/src/jbmc/jbmc
     */
    @Override
    public void setUpToolEnvironment() {

        environment = jbmcProcessBuilder.environment();

        environment.put("PATH_TO_JBMC", "/Elegant/CBMC");
        environment.put("WORKDIR", System.getProperty("user.home") + environment.get("PATH_TO_JBMC"));
        environment.put("JBMC_BIN", environment.get("WORKDIR") + "/build/bin/jbmc");
        environment.put("JAVA_MODEL", environment.get("WORKDIR") + "/jbmc/lib/java-models-library/target/core-models.jar");
        environment.put("SERVICE_DIR", System.getProperty("user.home") + "/Elegant/Elegant-Code-Verification-Service");
        //String testCasesPath = System.getProperty("user.home") + "/Elegant/Elegant-Code-Verification-Service/test-cases";
        environment.put("EXAMPLE_CODES", environment.get("SERVICE_DIR") + "/examples/codes/java");
        environment.put("UPLOADED_FILES", environment.get("SERVICE_DIR") + "/uploaded");
        environment.put("CLASSPATH", environment.get("JAVA_MODEL") + ":" + environment.get("EXAMPLE_CODES") + ":" + environment.get("UPLOADED_FILES"));
        environment.put("OUTPUT", environment.get("SERVICE_DIR") + "/output");
    }

    public String[] commandArgs(JBMCRequest code) {
        ArrayList<String> args = new ArrayList<>();
        args.add(environment.get("JBMC_BIN"));
        args.add("--json-ui");
        args.add("--classpath");
        args.add(environment.get("CLASSPATH"));
        if (code.isMethod()) {
            final String method = code.getMethodName();
            args.add("--function");
            args.add(method);
        }
        final String klass = code.getClassName();
        args.add(klass);
        args.add("--unwind");
        args.add("5");
        String[] strArray = new String[args.size()];
        return args.toArray(strArray);
    }

    /**
     * Starts a Linux JBMC process to verify a Java program.
     * @param code is the main class of the program to be verified.
     */
    @Override
    public void verifyCode(Request code) throws IOException {
        jbmcProcessBuilder.command(commandArgs(JBMCRequest.asJBMCRequest(code)));
        this.jbmcProcess = jbmcProcessBuilder.start();
    }

    /**
     * JBMC exports output in JSON format.
     * @return a {@link JsonStructure} object.
     */
    @Override
    public Object readOutput() {
        InputStream inputStream = this.jbmcProcess.getInputStream();
        JsonReader jsonReader = Json.createReader(new InputStreamReader(inputStream));

        return jsonReader.read();
    }

    @Override
    public String getEnvironmentVariable(String key) {
        return environment.get(key);
    }

    @Override
    public String getName() {
        return toolName;
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
