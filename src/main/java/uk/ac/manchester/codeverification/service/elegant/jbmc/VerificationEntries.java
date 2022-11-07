package uk.ac.manchester.codeverification.service.elegant.jbmc;

import java.util.HashMap;
import java.util.Map;

/**
 * A class to store the registered verification entries.
 */
public class VerificationEntries {

    // the id is utilized as unique index.
    int id;
    Map<Integer, Entry> entries;

    public VerificationEntries() {
        this.id = 0;
        this.entries = new HashMap<>();
    }

    /**
     * Register a new entry and return the entry id.
     */
    public int registerEntry(Entry e) {
        id++;
        entries.put(id, e);
        return id;
    }

    public Entry getEntry(int id) {
        return entries.get(id);
    }

    public Map<Integer, Entry> getEntries() {
        return entries;
    }
}
