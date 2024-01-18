/*
 * This file is part of the ELEGANT Code Verification Service.
 * URL: https://github.com/elegant-h2020/Elegant-Code-Verification-Service
 *
 * Copyright (c) 2022-2023, APT Group, Department of Computer Science,
 * The University of Manchester. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
    private void storeResult(long taskId) throws InterruptedException {
        String tool = verificationToolInstance.getName();

        if (tool.equals("JBMC")) {
            final JsonStructure output = (JsonStructure) verificationToolInstance.readOutput();
            final int exitCode = verificationToolInstance.waitFor();
            verificationTasks().updateTaskResult(taskId, new JBMCVerificationResult(output, exitCode));
        }
        // ESBMC writes the outcome of verification in a file, which is stored in: environment.get("OUTPUT") + File.separator + taskId + File.separator + "output.log"

    }

    @Override
    public void run() {
        try {
            // do the verification
            if (verificationToolInstance instanceof ESBMC) {
                verificationToolInstance.verifyCode(taskId, request);
            } else if (verificationToolInstance instanceof JBMC) {
                verificationToolInstance.verifyCode(taskId, request);
                storeResult(taskId);
            }

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public long getTaskId() {
        return taskId;
    }

    public VerificationTool getVerificationTool() {
        return verificationToolInstance;
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
