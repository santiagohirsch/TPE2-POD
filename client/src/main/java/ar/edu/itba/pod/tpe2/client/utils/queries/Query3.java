package ar.edu.itba.pod.tpe2.client.utils.queries;

import ar.edu.itba.pod.tpe2.collators.TopAgenciesCollator;
import ar.edu.itba.pod.tpe2.combiners.TopAgenciesCombinerFactory;
import ar.edu.itba.pod.tpe2.mappers.TopAgenciesMapper;
import ar.edu.itba.pod.tpe2.models.AgencyPercentage;
import ar.edu.itba.pod.tpe2.models.InfractionCount;
import ar.edu.itba.pod.tpe2.models.Ticket;
import ar.edu.itba.pod.tpe2.reducers.TopAgenciesReducerFactory;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.JobCompletableFuture;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("deprecation")
public class Query3 <K, T extends Ticket<K>> implements Runnable {
    private static final String OUTPUT_HEADER = "Issuing Agency;Percentage\n";
    private static final String OUTPUT_NAME = "query3.csv";

    private final String jobName;
    private final HazelcastInstance hazelcastInstance;
    private final IMap<Integer, T> tickets;
    private final String outputPath;
    private final int n;

    public Query3(String jobName, HazelcastInstance hazelcastInstance, IMap<Integer, T> tickets, String outputPath, int n) {
        this.jobName = jobName;
        this.hazelcastInstance = hazelcastInstance;
        this.tickets = tickets;
        this.outputPath = outputPath;
        this.n = n;
    }

    @Override
    public void run() {
        JobTracker jobTracker = hazelcastInstance.getJobTracker(jobName);
        KeyValueSource<Integer, T> source = KeyValueSource.fromMap(tickets);
        JobCompletableFuture<List<AgencyPercentage>> future = jobTracker.newJob(source)
                .mapper(new TopAgenciesMapper<>())
                .combiner(new TopAgenciesCombinerFactory())
                .reducer(new TopAgenciesReducerFactory())
                .submit(new TopAgenciesCollator(n));

        List<AgencyPercentage> result;

        try {
            result = future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        writeResultToCSV(result);
    }

    private void writeResultToCSV(List<AgencyPercentage> result) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath + OUTPUT_NAME))) {
            writer.write(OUTPUT_HEADER);

            for (AgencyPercentage agencyPercentage : result) {
                StringBuilder sb = new StringBuilder()
                        .append(agencyPercentage.getAgencyName()).append(";")
                        .append(String.format("%.2f", agencyPercentage.getPercentage())).append("%").append("\n");
                writer.write(sb.toString());
            }
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
