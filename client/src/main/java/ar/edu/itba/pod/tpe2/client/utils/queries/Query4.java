package ar.edu.itba.pod.tpe2.client.utils.queries;

import ar.edu.itba.pod.tpe2.collators.TopPlateCollator;
import ar.edu.itba.pod.tpe2.combiners.TopPlateCombinerFactory;
import ar.edu.itba.pod.tpe2.mappers.TopPlateMapper;
import ar.edu.itba.pod.tpe2.models.CountyPlateCount;
import ar.edu.itba.pod.tpe2.models.Ticket;
import ar.edu.itba.pod.tpe2.reducers.TopPlateReducerFactory;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.JobCompletableFuture;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("deprecation")
public class Query4<K, T extends Ticket<K>> implements Runnable {
    private static final String OUTPUT_HEADER = "County;Plate;Tickets\n";
    private static final String OUTPUT_NAME = "query4.csv";

    private final String jobName;
    private final HazelcastInstance hazelcastInstance;
    private final IMap<Integer, T> tickets;
    private final String outputPath;
    private final LocalDateTime from;
    private final LocalDateTime to;

    public Query4(String jobName, HazelcastInstance hazelcastInstance, IMap<Integer, T> tickets, String outputPath, LocalDateTime from, LocalDateTime to) {
        this.jobName = jobName;
        this.hazelcastInstance = hazelcastInstance;
        this.tickets = tickets;
        this.outputPath = outputPath;
        this.from = from;
        this.to = to;
    }

    @Override
    public void run() {
        JobTracker jobTracker = hazelcastInstance.getJobTracker(jobName);
        KeyValueSource<Integer, T> source = KeyValueSource.fromMap(tickets);
        JobCompletableFuture<List<CountyPlateCount>> future = jobTracker.newJob(source)
                .mapper(new TopPlateMapper<>(from, to))
                .combiner(new TopPlateCombinerFactory())
                .reducer(new TopPlateReducerFactory())
                .submit(new TopPlateCollator());

        List<CountyPlateCount> result;

        try {
            result = future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        writeResultToCSV(result);
    }

    private void writeResultToCSV(List<CountyPlateCount> result) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath + OUTPUT_NAME))) {
            writer.write(OUTPUT_HEADER);

            for (CountyPlateCount countyPlateCount : result) {
                StringBuilder sb = new StringBuilder()
                        .append(countyPlateCount.getCounty()).append(";")
                        .append(countyPlateCount.getPlate()).append(";")
                        .append(countyPlateCount.getInfractionAmount()).append("\n");
                writer.write(sb.toString());
            }
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
