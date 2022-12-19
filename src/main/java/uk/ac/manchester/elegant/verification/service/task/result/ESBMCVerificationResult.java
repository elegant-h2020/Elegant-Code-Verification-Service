package uk.ac.manchester.elegant.verification.service.task.result;

import uk.ac.manchester.elegant.verification.service.task.result.VerificationResult;

public class ESBMCVerificationResult extends VerificationResult {
    // The code verification tool output
    private String output;

    public ESBMCVerificationResult(String stringOutput, int exitCode) {
        super(exitCode);
        this.output = stringOutput;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String stringOutput) {
        this.output = stringOutput;
    }
}
