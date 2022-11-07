package uk.ac.manchester.codeverification.service.elegant.jbmc;

import uk.ac.manchester.codeverification.service.elegant.input.Klass;

public class Entry {
    private Klass   klass;
    private String  output;
    private int     exitCode;

    public Entry(Klass klass, String output, int exitCode) {
        this.klass = klass;
        this.output = output;
        this.exitCode = exitCode;
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
