package uk.ac.manchester.codeverification.service.elegant.jbmc;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

/**
 * A class to store the registered verification entries.
 */
public class VerificationEntries {

    // the id is utilized as unique index.
    private AtomicLong uid;
    private ArrayList<Entry> entries;

    public VerificationEntries() {
        this.uid = new AtomicLong(-1);
        this.entries = new ArrayList<>();
    }

    /**
     * Register a new entry and return the entry id.
     */
    public long registerEntry(Entry e) {
        final long id = uid.incrementAndGet();
        e.setId(id);
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
