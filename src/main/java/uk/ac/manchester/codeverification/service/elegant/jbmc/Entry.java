package uk.ac.manchester.codeverification.service.elegant.jbmc;

import uk.ac.manchester.codeverification.service.elegant.input.Klass;

public class Entry {
    /**
     * The id of a code verification request.
     */
    private long id;
    private Klass   klass;
    private String  output;
    private int     exitCode;

    public Entry(Klass klass, String output, int exitCode) {
        this.id = -99;
        this.klass = klass;
        this.output = output;
        this.exitCode = exitCode;
    }

    public Entry(int id, Klass klass, String output, int exitCode) {
        this.id = id;
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

    public void setOutput(String output) {
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

    public String getOutput() {
        return output;
    }

    public int getExitCode() {
        return exitCode;
    }

}
