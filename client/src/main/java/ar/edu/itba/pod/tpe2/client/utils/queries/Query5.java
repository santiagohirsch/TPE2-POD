package ar.edu.itba.pod.tpe2.client.utils.queries;

import ar.edu.itba.pod.tpe2.collators.InfractionGroupsCollator;
import ar.edu.itba.pod.tpe2.combiners.InfractionGroupsCombinerFactory;
import ar.edu.itba.pod.tpe2.mappers.InfractionGroupsMapper;
import ar.edu.itba.pod.tpe2.models.InfractionCount;
import ar.edu.itba.pod.tpe2.models.Pair;
import ar.edu.itba.pod.tpe2.models.Ticket;
import ar.edu.itba.pod.tpe2.reducers.InfractionGroupsReducerFactory;
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
public class Query5<K, T extends Ticket<K>> implements Runnable {
    private static final String OUTPUT_HEADER = "Group;Infraction A;Infraction B\n";
    private static final String OUTPUT_NAME = "/query5.csv";

    private final String jobName;
    private final HazelcastInstance hazelcastInstance;
    private final IMap<K, String> infractions;
    private final IMap<Integer, T> tickets;
    private final String outputPath;

    public Query5(String jobName, HazelcastInstance hazelcastInstance, IMap<K, String> infractions, IMap<Integer, T> tickets, String outputPath) {
        this.jobName = jobName;
        this.hazelcastInstance = hazelcastInstance;
        this.infractions = infractions;
        this.tickets = tickets;
        this.outputPath = outputPath;
    }


    @Override
    public void run() {
        JobTracker jobTracker = hazelcastInstance.getJobTracker(jobName);
        KeyValueSource<Integer, T> source = KeyValueSource.fromMap(tickets);
        JobCompletableFuture<Map<Integer, List<Pair<String, String>>>> future = jobTracker.newJob(source)
                .mapper(new InfractionGroupsMapper<>())
                .combiner(new InfractionGroupsCombinerFactory<>())
                .reducer(new InfractionGroupsReducerFactory<>())
                .submit(new InfractionGroupsCollator<>(infractions));

        Map<Integer, List<Pair<String, String>>> result;
        try {
            result = future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        writeResultToCSV(result);
    }

    private void writeResultToCSV(Map<Integer, List<Pair<String, String>>> result) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath + OUTPUT_NAME))) {
            writer.write(OUTPUT_HEADER);

            result.forEach((group, value) -> value.forEach(pair -> {
                StringBuilder sb = new StringBuilder()
                        .append(group).append(";")
                        .append(pair.left()).append(";")
                        .append(pair.right()).append("\n");
                try {
                    writer.write(sb.toString());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }));
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
