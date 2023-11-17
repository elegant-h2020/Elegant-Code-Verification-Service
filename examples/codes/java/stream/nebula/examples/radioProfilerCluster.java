package stream.nebula.examples;

import stream.nebula.exceptions.RESTException;
import stream.nebula.operators.Aggregation;
import stream.nebula.operators.sinks.FileSink;
import stream.nebula.operators.window.TimeMeasure;
import stream.nebula.operators.window.TumblingWindow;
import stream.nebula.runtime.NebulaStreamRuntime;
import stream.nebula.runtime.Query;
import stream.nebula.udf.FlatMapFunction;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import static stream.nebula.expression.Expressions.attribute;
import static stream.nebula.operators.window.EventTime.eventTime;


/**
 * Example demonstrating how to implement arbitrary window logic with a flatMap UDF that retains state.
 */
public class radioProfilerCluster {

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

    // This UDF tracks a configurable sliding window and computes the median and mean age of the input.
    static class SlidingWindow implements FlatMapFunction<Input, Output> {

        // These fields hold the immutable properties of this particular window instance, i.e., the size and the slide.
        private final int size;
        private final int slide;

        // These fields hold the mutable state of the window, i.e., the elements contained in the window and a
        // variable to track the size of the current window slide.
        LinkedList<Input> window = new LinkedList<>();
        private int index = 0;

        public SlidingWindow(int size, int slide) {
            this.size = size;
            this.slide = slide;
        }

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
    }

    public static void main(String[] args) throws RESTException, IOException {
        // Create a NebulaStream runtime and connect it to the NebulaStream coordinator.
        NebulaStreamRuntime nebulaStreamRuntime = NebulaStreamRuntime.getRuntime();
        nebulaStreamRuntime.getConfig().setHost("localhost").setPort("8081");

        // Create a streaming query. The `ages` source contains two attributes `timestamp` and `age`.
        //Query query = nebulaStreamRuntime.readFromSource("ages");

        // Project the input attributes for the UDF.
        // This operation is not strictly necessary in this example, because the input stream only contains the two
        // attributes.
        //query.project(attribute("timestamp"), attribute("age"));


        Aggregation aggregation1 = Aggregation.average("datr").as("SFaverage");
        Aggregation aggregation2 = Aggregation.min("gwmac").as("gwmacmin");
        Aggregation aggregation3 = Aggregation.average("rssi").as("rssiaverage");
        Aggregation aggregation4 = Aggregation.min("chan").as("chanmin");

        // Create a streaming query. The `ages` source contains two attributes `timestamp` and `age`.
        Query query = nebulaStreamRuntime.readFromSource("lora_stream");
        // Create a streaming query
//        query.project(attribute("datr"), attribute("gwmac"),
//                attribute("rssi"), attribute("chan"));
        //int, float, int, float, int
        query.project(attribute("devaddr"), attribute("datr"), attribute("agent_time"),
                attribute("gwmac"), attribute("rssi"), attribute("chan"))
                .window(TumblingWindow.of(eventTime("agent_time"), TimeMeasure.hours(1)))
                .byKey("devaddr")
                .apply(aggregation1, aggregation2, aggregation3, aggregation4);


        // Execute the UDF on the stream.
        query.flatMap(new SlidingWindow(6, 2));

        // Finish the query with a sink
        query.sink(new FileSink("/demo/cluster-results-1.csv", "CSV_FORMAT", true));

        // Submit the query to the coordinator.
        nebulaStreamRuntime.executeQuery(query, "BottomUp");
    }

}
