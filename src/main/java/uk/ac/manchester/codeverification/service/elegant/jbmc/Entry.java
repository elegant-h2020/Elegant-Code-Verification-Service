package uk.ac.manchester.codeverification.service.elegant.jbmc;

import jakarta.json.JsonStructure;
import uk.ac.manchester.codeverification.service.elegant.input.Code;

public class Entry {

    // The unique id of a code verification request.
    private long id;
    // The Java class of the code that is verified.
    private Code code;
    // The code verification tool output (in JSON format).
    private JsonStructure output;
    // The exit code of the code verification tool.
    private int exitCode;

    public Entry(Code code, JsonStructure output, int exitCode) {
        this.id = -99;
        this.code = code;
        this.output = output;
        this.exitCode = exitCode;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCode(Code code) {
        this.code = code;
    }

    public void setOutput(JsonStructure output) {
        this.output = output;
    }

    public void setExitCode(int exitCode) {
        this.exitCode = exitCode;
    }

    public long getId() {
        return id;
    }

    public Code getCode() {
        return code;
    }

    public JsonStructure getOutput() {
        return output;
    }

    public int getExitCode() {
        return exitCode;
    }

}
