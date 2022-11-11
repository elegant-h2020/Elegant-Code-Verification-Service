package uk.ac.manchester.codeverification.service.elegant.input;

/**
 * This class holds the metadata of the input code.
 */
public class Code {

    private String className;
    private boolean isMethod;
    // Fully qualified name of a method.
    private String methodName;

    public Code() {
    }

    public Code(String name, boolean isMethod, String methodName) {
        this.className = name;
        this.isMethod = isMethod;
        this.methodName = methodName;
    }

    public String getClassName() {
        return className;
    }

    public boolean isMethod() {
        return isMethod;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setClassName(String classname) {
        this.className = classname;
    }

    public void setIsMethod(boolean isMethod) {
        this.isMethod = isMethod;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    @Override
    public String toString() {
        return "Code{" +
                "classname=\""      + className     + "\"" +
                ", isMethod="       + isMethod      +
                ", methodName=\""   + methodName    + "\"" +
                "}";
    }
}
