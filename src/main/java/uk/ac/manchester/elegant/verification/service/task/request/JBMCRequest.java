package uk.ac.manchester.elegant.verification.service.task.request;

/**
 * This class represents a code verification request with the JBMC tool.
 *
 */
public class JBMCRequest implements Request{

    private String className;
    private boolean isMethod;
    // Fully qualified name of a method.
    private String methodName;

    public JBMCRequest(String className, boolean isMethod, String methodName) {
        this.className = className;
        this.isMethod = isMethod;
        this.methodName = methodName;
    }

    public static JBMCRequest asJBMCRequest(Request request) {
        return (JBMCRequest) request;
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
        return "JBMCRequest{" +
                ", className = \""    + className     + "\"" +
                ", isMethod = "       + isMethod      +
                ", methodName = \""   + methodName    + "\"" +
                "}";
    }
}
