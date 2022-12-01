package uk.ac.manchester.codeverification.service.elegant.output;

import uk.ac.manchester.codeverification.service.elegant.input.Request;

/**
 * An entry in the service is composed by:
 *  a unique id: {@code id},
 *  a request for code verification {@code request},
 *  the result of the code verification request {@code result}.
 */
public class Entry {

    // The unique id of a code verification entry.
    private long id;
    // An object that represents a request for code verification
    private Request request;
    // An object that represents the result of a code verification request
    private VerificationResult result;

    public Entry(Request request, VerificationResult result) {
        this.id = -99;
        this.request = request;
        this.result = result;
    }

    public VerificationResult getResult() {
        return result;
    }

    public long getId() {
        return id;
    }

    public Request getRequest() {
        return request;
    }

    public void setResult(VerificationResult result) {
        this.result = result;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

}
