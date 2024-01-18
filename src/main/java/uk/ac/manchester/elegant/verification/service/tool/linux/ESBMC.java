package uk.ac.manchester.elegant.verification.service.tool.linux;

import uk.ac.manchester.elegant.verification.service.task.request.ESBMCRequest;
import uk.ac.manchester.elegant.verification.service.task.request.Request;
import uk.ac.manchester.elegant.verification.service.tool.VerificationTool;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class ESBMC implements VerificationTool {

    ProcessBuilder      esbmcProcessBuilder;
    Map<String, String> environment;

    Process             esbmcProcess;
    int                 exitCode;
    String              output;

    String              toolName;

    public ESBMC() {
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

        environment.put("PATH_TO_ESBMC", "/Elegant/ESBMC-Linux");
        environment.put("WORKDIR", System.getProperty("user.home") + environment.get("PATH_TO_ESBMC"));
        environment.put("ESBMC_BIN", environment.get("WORKDIR") + "/bin/esbmc");
        environment.put("SERVICE_DIR", System.getProperty("user.home") + "/Elegant/Elegant-Code-Verification-Service");
        environment.put("EXAMPLE_CODES", environment.get("SERVICE_DIR") + "/examples/codes/c");
        environment.put("UPLOADED_FILES", environment.get("SERVICE_DIR") + "/uploaded");
        environment.put("OUTPUT", environment.get("SERVICE_DIR") + "/output/esbmc");
    }

    /**
     * A not configurable ESBMC configuration.
     */
    public void addOptions(ArrayList<String> args) {
        // utilize the FuSeBMC options
        args.add("--no-div-by-zero-check");
        args.add("--force-malloc-success");
        args.add("--state-hashing");
        args.add("--no-align-check");
        args.add("--k-step");
        args.add("5");
        args.add("--floatbv");
        args.add("--context-bound");
        args.add("2");
        args.add("--show-cex");
        args.add("--quiet");
        args.add("--unlimited-k-steps");
        args.add("--no-pointer-check");
        args.add("--no-bounds-check");
        args.add("--no-slice");
        args.add("--interval-analysis");
        args.add("--incremental-bmc");
        args.add("--unlimited-k-steps");
        args.add("--k-induction");
        args.add("--falsification");
        args.add("--timeout");
        args.add("878s");
        args.add("--memlimit");
        args.add("10g");
    }

    /**
     * Set ESBMC run configuration as follows:
     * ./esbmc example.cpp --cppstd 14 --function execute --incremental-bmc --compact-trace
     */
    public String[] commandArgs(ESBMCRequest code) {
        ArrayList<String> args = new ArrayList<>();
        args.add(environment.get("ESBMC_BIN"));
        final String file = environment.get("UPLOADED_FILES") + "/" + code.getFileName();
        args.add(file);
        args.add("--cppstd");
        args.add("14");
        if (code.isFunction()) {
            final String method = code.getFunctionName();
            args.add("--function");
            args.add(method);
        }
//        args.add("--incremental-bmc");
//        args.add("--compact-trace");
        args.add("--no-pointer-check");
        args.add("--unlimited-k-steps");
        args.add("--timeout");
        args.add("60s");
        String[] strArray = new String[args.size()];
        return args.toArray(strArray);
    }

    /**
     * Starts a Linux ESBMC process to verify a C/C++ program. Writes the output in a file stored in: environment.get("OUTPUT") + File.separator + taskId + File.separator + "output.log".
     * @param code is the file of the program to be verified.
     */
    @Override
    public void verifyCode(long taskId, Request code) throws IOException {
        File idDirectory = new File(environment.get("OUTPUT") + File.separator + taskId);
        if (!idDirectory.exists()) {
            idDirectory.mkdirs();
        }
        File outputFile = new File( environment.get("OUTPUT") + File.separator + taskId + File.separator + "output.log");
        esbmcProcessBuilder.redirectOutput(outputFile);
        esbmcProcessBuilder.command(commandArgs(ESBMCRequest.asESBMCRequest(code)));
        this.esbmcProcess = esbmcProcessBuilder.start();
    }

    /**
     * ESBMC does not export output in JSON format.
     * @return null.
     */
    @Override
    public Object readOutput() {
        return null;
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
