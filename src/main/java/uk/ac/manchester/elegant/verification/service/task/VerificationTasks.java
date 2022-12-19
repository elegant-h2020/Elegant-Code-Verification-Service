package uk.ac.manchester.elegant.verification.service.task;

import uk.ac.manchester.elegant.verification.service.task.result.VerificationResult;

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
        Long key = id;
        return tasks.remove(id);
    }
}
