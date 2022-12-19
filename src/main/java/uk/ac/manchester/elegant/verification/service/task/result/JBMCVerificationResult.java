package uk.ac.manchester.elegant.verification.service.task.result;

import jakarta.json.JsonStructure;
import uk.ac.manchester.elegant.verification.service.task.result.VerificationResult;

public class JBMCVerificationResult extends VerificationResult {
    // The code verification tool output in JSON format
    private JsonStructure output;

    public JBMCVerificationResult(JsonStructure jsonOutput, int exitCode) {
        super(exitCode);
        this.output = jsonOutput;
    }

    public JsonStructure getOutput() {
        return output;
    }

    public void setOutput(JsonStructure jsonOutput) {
        this.output = jsonOutput;
    }
}
