package uk.ac.manchester.elegant.verification.service.task;

import jakarta.json.JsonStructure;
import uk.ac.manchester.elegant.verification.service.api.ElegantCodeVerificationService;
import uk.ac.manchester.elegant.verification.service.task.request.Request;
import uk.ac.manchester.elegant.verification.service.task.result.ESBMCVerificationResult;
import uk.ac.manchester.elegant.verification.service.task.result.JBMCVerificationResult;
import uk.ac.manchester.elegant.verification.service.task.result.VerificationResult;
import uk.ac.manchester.elegant.verification.service.tool.linux.ESBMC;
import uk.ac.manchester.elegant.verification.service.tool.linux.JBMC;
import uk.ac.manchester.elegant.verification.service.tool.VerificationTool;

import java.io.IOException;

public class VerificationTask implements Runnable {

    public enum Status {
        INITIAL, ONGOING, COMPLETED
    }

    private long taskId;
    private VerificationTool verificationToolInstance;
    private Request request;
    private VerificationResult result;
    private Status status;

    public VerificationTask(long id, String tool, Request request) {
        this.taskId = id;
        this.request = request;
        this.result = null;
        this.status = Status.INITIAL;

        deployTool(tool);
        registerTask(id);
    }

    private VerificationTasks verificationTasks() {
        return ElegantCodeVerificationService.getVerificationTasks();
    }

    private void deployTool(String tool) {
        String OS = System.getProperty("os.name").toLowerCase();

        if (OS.startsWith("linux")) {
            if (tool.equals("JBMC")) {
                verificationToolInstance = new JBMC();
            } else if (tool.equals("ESBMC")) {
                verificationToolInstance = new ESBMC();
            } else {
                throw new UnsupportedOperationException("Code verification tool " + tool + " is currently not supported.");
            }
        } else {
            throw new UnsupportedOperationException("Code verification Service is currently not supported for " + OS + ".");
        }
    }

    /**
     * Initial registration of a {@link VerificationTask} into the {@link VerificationTasks}.
     */
    private void registerTask(long id) {
        this.result = null;
        verificationTasks().registerTask(id, this);
    }

    /**
     * Stores the output and exit code into the {@link VerificationTask} entry
     * as either a new {@link JBMCVerificationResult}, or a new {@link ESBMCVerificationResult} object.
     */
    private void storeResult() throws InterruptedException {
        String tool = verificationToolInstance.getName();

        if (tool.equals("JBMC")) {
            final JsonStructure output = (JsonStructure) verificationToolInstance.readOutput();
            final int exitCode = verificationToolInstance.waitFor();
            verificationTasks().updateTaskResult(taskId, new JBMCVerificationResult(output, exitCode));

        } else if (tool.equals("ESBMC")) {
            final String output = (String) verificationToolInstance.readOutput();
            final int exitCode = verificationToolInstance.waitFor();
            verificationTasks().updateTaskResult(taskId, new ESBMCVerificationResult(output, exitCode));
        }

    }

    @Override
    public void run() {
        try {
            // do the verification
            verificationToolInstance.verifyCode(request);

            // store the result
            storeResult();

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public long getTaskId() {
        return taskId;
    }

    public String getVerificationTool() {
        return verificationToolInstance.getName();
    }

    public Request getRequest() {
        return request;
    }

    public VerificationResult getResult() {
        return result;
    }

    public Status getStatus() {
        return status;
    }

    public void setTaskId(long id) {
        this.taskId = id;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setResult(VerificationResult result) {
        this.result = result;
    }

}
