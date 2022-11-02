package uk.ac.manchester.codeverification.service.elegant.input;

public class Klass {

    private String classname;

    public Klass() {
    }

    public Klass(String name) {
        this.classname = name;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    @Override
    public String toString() {
        return "Code{classname:" + classname + "}";
    }
}
