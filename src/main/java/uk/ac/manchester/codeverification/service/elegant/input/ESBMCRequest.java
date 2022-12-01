package uk.ac.manchester.codeverification.service.elegant.input;

public class ESBMCRequest implements Request{

    private String tool;
    private String fileName;

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

    public void setTool(String tool) {
        this.tool = tool;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "Request{"           +
                "tool = \""         + tool        + "\"" +
                ", fileName = \""   + fileName    + "\"" +
                "}";
    }
}
