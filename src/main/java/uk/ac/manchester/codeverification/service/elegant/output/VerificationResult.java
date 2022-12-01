package uk.ac.manchester.codeverification.service.elegant.output;

import jakarta.json.JsonStructure;

/**
 * A class that wraps the attributes of the verification output.
 */
public class VerificationResult {

    // The code verification tool output in JSON format (if supported).
    private JsonStructure JsonOutput;
    // The exit code of the code verification tool.
    private int exitCode;

    public VerificationResult(JsonStructure jsonOutput, int exitCode) {
        this.JsonOutput = jsonOutput;
        this.exitCode = exitCode;
    }

    public JsonStructure getJsonOutput() {
        return JsonOutput;
    }

    public int getExitCode() {
        return exitCode;
    }

    public void setOutput(JsonStructure output) {
        this.JsonOutput = output;
    }

    public void setExitCode(int exitCode) {
        this.exitCode = exitCode;
    }
}
