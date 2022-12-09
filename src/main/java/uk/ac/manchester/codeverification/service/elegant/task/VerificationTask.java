package uk.ac.manchester.codeverification.service.elegant.task;

import jakarta.json.JsonStructure;
import uk.ac.manchester.codeverification.service.elegant.api.ElegantCodeVerificationService;
import uk.ac.manchester.codeverification.service.elegant.input.Request;
import uk.ac.manchester.codeverification.service.elegant.tool.LinuxESBMC;
import uk.ac.manchester.codeverification.service.elegant.tool.LinuxJBMC;
import uk.ac.manchester.codeverification.service.elegant.tool.VerificationTool;

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
                verificationToolInstance = new LinuxJBMC();
            } else if (tool.equals("ESBMC")) {
                verificationToolInstance = new LinuxESBMC();
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

    @Override
    public void run() {
        try {
            // do the verification
            verificationToolInstance.verifyCode(request);
            // store the output and exit code as a new VerificationResult
            final JsonStructure output = verificationToolInstance.readOutput();
            final int exitCode = verificationToolInstance.waitFor();
            // update the registered task
            verificationTasks().updateTaskResult(taskId, new VerificationResult(output, exitCode));
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
