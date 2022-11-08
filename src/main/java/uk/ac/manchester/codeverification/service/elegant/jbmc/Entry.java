package uk.ac.manchester.codeverification.service.elegant.jbmc;

import jakarta.json.JsonStructure;
import uk.ac.manchester.codeverification.service.elegant.input.Klass;

public class Entry {

    // The unique id of a code verification request.
    private long id;
    // The Java class of the code that is verified.
    private Klass klass;
    // The code verification tool output (in JSON format).
    private JsonStructure output;
    // The exit code of the code verification tool.
    private int exitCode;

    public Entry(Klass klass, JsonStructure output, int exitCode) {
        this.id = -99;
        this.klass = klass;
        this.output = output;
        this.exitCode = exitCode;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setKlass(Klass klass) {
        this.klass = klass;
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

    public Klass getKlass() {
        return klass;
    }

    public JsonStructure getOutput() {
        return output;
    }

    public int getExitCode() {
        return exitCode;
    }

}
