package uk.ac.manchester.elegant.verification.service.tool;

import jakarta.json.JsonStructure;
import uk.ac.manchester.elegant.verification.service.input.ESBMCRequest;
import uk.ac.manchester.elegant.verification.service.input.Request;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;

public class LinuxESBMC implements VerificationTool{

    ProcessBuilder      esbmcProcessBuilder;
    Map<String, String> environment;

    Process             esbmcProcess;
    int                 exitCode;
    String              output;

    String              toolName;

    public LinuxESBMC() {
        super();
        this.toolName = "ESBMC";
        this.esbmcProcessBuilder = new ProcessBuilder();
        setUpToolEnvironment();
        esbmcProcessBuilder.directory(new File(environment.get("WORKDIR")));
        // send the ESBMC output to nowhere...
        esbmcProcessBuilder.redirectOutput(new File("/dev/null"));
    }

    @Override
    public void setUpToolEnvironment() {
        environment = esbmcProcessBuilder.environment();

        environment.put("PATH_TO_ESBMC", "/Elegant/ESBMC_Project");
        environment.put("WORKDIR", System.getProperty("user.home") + environment.get("PATH_TO_ESBMC"));
        environment.put("ESBMC_BIN", environment.get("WORKDIR") + "/release/bin/esbmc");
        //environment.put("JAVA_MODEL", environment.get("WORKDIR") + "/jbmc/lib/java-models-library/target/core-models.jar");
        environment.put("SERVICE_DIR", System.getProperty("user.home") + "/Elegant/Elegant-Code-Verification-Service");
        //String testCasesPath = System.getProperty("user.home") + "/Elegant/Elegant-Code-Verification-Service/test-cases";
        environment.put("TEST_CASES", environment.get("SERVICE_DIR") + "/test-cases/esbmc");
        environment.put("UPLOADED_FILES", environment.get("SERVICE_DIR") + "/uploaded");
        //environment.put("CLASSPATH", environment.get("JAVA_MODEL") + ":" + environment.get("TEST_CASES"));
        environment.put("OUTPUT", environment.get("SERVICE_DIR") + "/output/esbmc");
    }

    public String[] commandArgs(ESBMCRequest code) {
        ArrayList<String> args = new ArrayList<>();
        args.add(environment.get("ESBMC_BIN"));
        final String file = environment.get("UPLOADED_FILES") + "/" + code.getFileName();
        args.add(file);
        String[] strArray = new String[args.size()];
        // analyze technique arguments
        String technique = code.getTechnique().toLowerCase();
        if (technique.equals("memoryleakcheck")) {
            args.add("--memory-leak-check");
        } else if (technique.equals("contextbound")) {
            args.add("--context-bound");
            args.add(String.valueOf(code.getIntArg()));
        } else if (technique.equals("unwind")) {
            args.add("--unwind");
            args.add(String.valueOf(code.getIntArg()));
        }
        // TODO: add all techiniques
        return args.toArray(strArray);
    }

    /**
     * Starts a Linux ESBMC process to verify a C/C++ program.
     * @param code is the file of the program to be verified.
     */
    @Override
    public void verifyCode(Request code) throws IOException {
        esbmcProcessBuilder.command(commandArgs(ESBMCRequest.asESBMCRequest(code)));
        this.esbmcProcess = esbmcProcessBuilder.start();
    }

    /**
     * ESBMC does not export output in JSON format.
     * @return an empty JsonStructure object.
     */
    @Override
    public JsonStructure readOutput() {
        return JsonStructure.EMPTY_JSON_OBJECT.asJsonObject();
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
        return this.esbmcProcess.waitFor();
    }
}
