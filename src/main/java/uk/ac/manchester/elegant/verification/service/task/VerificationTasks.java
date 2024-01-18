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

import uk.ac.manchester.elegant.verification.service.task.result.VerificationResult;
import uk.ac.manchester.elegant.verification.service.tool.linux.ESBMC;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A class to store the registered verification {@link VerificationTask}s.
 */
public class VerificationTasks {

    // the id is utilized as unique index.
    private Map<Long, VerificationTask> tasks;

    public VerificationTasks() {
        this.tasks = new HashMap<>();
    }

    /**
     * Register a new {@link VerificationTask} and return its id.
     */
    synchronized public void registerTask(long id, VerificationTask newTask) {
        newTask.setTaskId(id);
        tasks.put(id, newTask);
    }

    synchronized public void updateTaskResult(long id, VerificationResult result) {
        VerificationTask updated = tasks.get(id);
        updated.setResult(result);
        tasks.put(id, updated);
    }

    public VerificationTask getTask(long id) {
        return tasks.get(id);
    }

    public ArrayList<VerificationTask> listEntries() {
        return new ArrayList<>(tasks.values());
    }

    public VerificationTask removeEntry(long id) {
        if (tasks.get(id).getVerificationTool() instanceof ESBMC) {
            File file = new File(getTask(id).getVerificationTool().getEnvironmentVariable("OUTPUT") + File.separator + id + File.separator + "output.log");
            File parentFile = file.getParentFile();
            if (file.exists()) {
                file.delete();
            }
            if (parentFile.exists()) {
                parentFile.delete();
            }
        }
        return tasks.remove(id);
    }
}
