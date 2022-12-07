package uk.ac.manchester.codeverification.service.elegant.input;

public class ESBMCRequest implements Request{

    private String tool;
    private String fileName;
    /**
     * The verification techniques:
     * Memory Leak Check    ->  --memory-leak-check
     * Context Bound        ->  --context-bound N
     * Unwind               ->  --unwind N
     * Falsification        ->  --falsification
     * Incremental BMC      ->  --incremental-bmc
     * K Induction          ->  --k-induction
     */
    private String technique;
    private int intArg;

    /*public ESBMCRequest(String tool, String fileName) {
        this.tool = tool;
        this.fileName = fileName;
    }*/

    public static ESBMCRequest asESBMCRequest(Request request) {
        return (ESBMCRequest) request;
    }

    public String getTool() {
        return tool;
    }

    public String getFileName() {
        return fileName;
    }

    public String getTechnique() {
        return technique;
    }

    public int getIntArg() {
        return intArg;
    }

    public void setTool(String tool) {
        this.tool = tool;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setTechnique(String technique) {
        this.technique = technique;
    }

    public void setIntArg(int intArg) {
        this.intArg = intArg;
    }

    @Override
    public String toString() {
        return "Request{"           +
                "tool = \""         + tool        + "\"" +
                ", fileName = \""   + fileName    + "\"" +
                "}";
    }
}
