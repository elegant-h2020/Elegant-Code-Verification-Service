package my.petty.examples;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

public class NesOperator {
    
    // This is the input type of the UDF.
    // The names of the fields `timestamp` and `age` must correspond to the schema of the input stream.
    //int, float, int, float, int
    static class Input {
        long agentTimeStart;
        long agentTimeStop;
        long devaddr;
        float datr;
        int gwmac;
        float rssi;
        int chan;
    }

    // This is he output type of the UDF.
    // The output stream will contain fields that correspond to the field names.
    static class Output {
        float datr;
        int gwmac;
        float rssi;
        int chan;
        int cluster;
    }


    // These fields hold the immutable properties of this particular window instance, i.e., the size and the slide.
    private final int size=0;
    private final int slide=0;

    // These fields hold the mutable state of the window, i.e., the elements contained in the window and a
    // variable to track the size of the current window slide.
    LinkedList<Input> window = new LinkedList<>();
    private int index = 0;

    // The flatMap method is called for every tuple.
    // It can access state retained inside the UDF across invocations.
    // If the current tuple does not trigger a new window, it returns an empty result.
    // Otherwise, if the current tuple triggers a new window, it aggregates the window contents and emits a
    // single output tuple.
    // FlatMap can also return a collection with multiple output tuples, e.g., it could emit every tuple in the
    // window and enrich it with additional data.
    public Collection<Output> flatMap(Input value) {
        // Print the input for debugging on the console of the worker.
        System.out.println("rssi = " + value.rssi + "; chan = " + value.chan);

        // Keep the last `size` objects in the window.
        if (window.size() == size) {
            window.removeFirst();
        }
        window.addLast(value);

        // Emit empty result if the current window slide is not completed or if the first window is not full.
        index += 1;
        if (index < slide || window.size() != size) {
            return Collections.emptyList();
        }

        // Reset the window slide, compute window aggregation, and emit result.
        index = 0;
        Output output = new Output();

        //List<Double> sortedAges = window.stream().map(i -> (double) i.rssi).sorted().collect(Collectors.toList());

        //AGGIUNGERE KMEANS

        output.rssi = window.getLast().rssi;
        output.datr = window.getLast().datr;
        output.chan = window.getLast().chan;
        output.gwmac = window.getLast().gwmac;
        output.cluster = 0;

        return Collections.singletonList(output);
    }

    public static void main(String[] args) {

    }

}
