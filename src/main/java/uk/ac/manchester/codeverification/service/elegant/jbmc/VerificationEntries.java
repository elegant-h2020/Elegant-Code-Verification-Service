package uk.ac.manchester.codeverification.service.elegant.jbmc;

import java.util.ArrayList;

/**
 * A class to store the registered verification entries.
 */
public class VerificationEntries {

    // the id is utilized as unique index.
    int id;
    private ArrayList<Entry> entries;

    public VerificationEntries() {
        this.id = 0;
        this.entries = new ArrayList<>();
    }

    /**
     * Register a new entry and return the entry id.
     */
    public int registerEntry(Entry e) {
        id++;
        entries.add(e);
        return id;
    }

    public Entry getEntry(int id) {
        return entries.get(id);
    }

    public ArrayList<Entry> getEntries() {
        return entries;
    }
}
