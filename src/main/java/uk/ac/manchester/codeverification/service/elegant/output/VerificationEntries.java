package uk.ac.manchester.codeverification.service.elegant.output;

import uk.ac.manchester.codeverification.service.elegant.jbmc.JBMC;

import java.util.HashMap;
import java.util.Map;

/**
 * A class to store the registered verification entries.
 */
public class VerificationEntries {

    // the id is utilized as unique index.
    int id;
    Map<Integer, JBMC> entries;

    public VerificationEntries() {
        this.id = 0;
        this.entries = new HashMap<>();
    }

    /**
     * Register a new entry and return the entry id.
     */
    public int registerEntry(JBMC j) {
        id++;
        entries.put(id, j);
        return id;
    }

    public JBMC getEntry(int id) {
        return entries.get(id);
    }
}
