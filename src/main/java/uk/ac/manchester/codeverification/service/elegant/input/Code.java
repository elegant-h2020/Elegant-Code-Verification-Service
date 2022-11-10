package uk.ac.manchester.codeverification.service.elegant.input;

/**
 * This class holds the metadata of the input code.
 */
public class Code {

    private String className;

    public Code() {
    }

    public Code(String name) {
        this.className = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String classname) {
        this.className = classname;
    }

    @Override
    public String toString() {
        return "Code{classname:" + className + "}";
    }
}
