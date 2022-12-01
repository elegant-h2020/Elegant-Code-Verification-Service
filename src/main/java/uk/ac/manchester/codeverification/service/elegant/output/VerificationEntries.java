package uk.ac.manchester.codeverification.service.elegant.output;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * A class to store the registered verification {@link Entry}s.
 */
public class VerificationEntries {

    // the id is utilized as unique index.
    private final AtomicLong uid;
    private Map<Long, Entry> entries;

    public VerificationEntries() {
        this.uid = new AtomicLong(-1);
        this.entries = new HashMap<>();
    }

    /**
     * Register a new entry and return the entry id.
     */
    public long registerEntry(Entry e) {
        final long id = uid.incrementAndGet();
        e.setId(id);
        entries.put(id, e);
        return id;
    }

    public Entry getEntry(long id) {
        return entries.get(id);
    }

    public ArrayList<Entry> listEntries() {
        return new ArrayList<>(entries.values());
    }

    public Entry removeEntry(long id) {
        Long key = id;
        return entries.remove(id);
    }
}
