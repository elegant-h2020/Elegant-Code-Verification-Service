package uk.ac.manchester.elegant.verification.service.task.request;

public class ESBMCRequest implements Request{

    private String fileName;

    public ESBMCRequest(String fileName) {
        this.fileName = fileName;
    }

    public static ESBMCRequest asESBMCRequest(Request request) {
        return (ESBMCRequest) request;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "ESBMCRequest{"           +
                ", fileName = \""   + fileName    + "\"" +
                "}";
    }
}
