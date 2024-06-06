package ar.edu.itba.pod.tpe2.client.utils.queries;

import ar.edu.itba.pod.tpe2.collators.NYCAllInfractionsCollator;
import ar.edu.itba.pod.tpe2.combiners.NYCAllInfractionsCombinerFactory;
import ar.edu.itba.pod.tpe2.mappers.NYCAllInfractionsMapper;
import ar.edu.itba.pod.tpe2.models.InfractionCount;
import ar.edu.itba.pod.tpe2.models.NYCTicket;
import ar.edu.itba.pod.tpe2.reducers.NYCAllInfractionsReducerFactory;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.JobCompletableFuture;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("deprecation")
public class Query1 implements Runnable {
    private static final String OUTPUT_HEADER = "Infraction;Tickets\n";
    private static final String OUTPUT_NAME = "query1.csv";

    private final String jobName;
    private final HazelcastInstance hazelcastInstance;
    private final IMap<Integer, String> infractions;
    private final IMap<Integer, NYCTicket> tickets;
    private final String outputPath;

    public Query1(String jobName, HazelcastInstance hazelcastInstance, IMap<Integer, String> infractions, IMap<Integer, NYCTicket> tickets, String outputPath) {
        this.jobName = jobName;
        this.hazelcastInstance = hazelcastInstance;
        this.infractions = infractions;
        this.tickets = tickets;
        this.outputPath = outputPath;
    }

    @Override
    public void run() {
        JobTracker jobTracker = hazelcastInstance.getJobTracker(jobName);
        KeyValueSource<Integer, NYCTicket> source = KeyValueSource.fromMap(tickets);
        JobCompletableFuture<List<InfractionCount>> future = jobTracker.newJob(source)
                .mapper(new NYCAllInfractionsMapper())
                .combiner(new NYCAllInfractionsCombinerFactory())
                .reducer(new NYCAllInfractionsReducerFactory())
                .submit(new NYCAllInfractionsCollator(infractions));

        List<InfractionCount> result;

        try {
            result = future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        writeResultToCSV(result);
    }

    private void writeResultToCSV(List<InfractionCount> result) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath + OUTPUT_NAME))) {
            writer.write(OUTPUT_HEADER);

            for (InfractionCount infractionCount : result) {
                StringBuilder sb = new StringBuilder()
                        .append(infractionCount.getInfractionName()).append(";")
                        .append(infractionCount.getTicketsAmount()).append("\n");
                writer.write(sb.toString());
            }
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
