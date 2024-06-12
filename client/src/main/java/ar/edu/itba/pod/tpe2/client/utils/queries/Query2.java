package ar.edu.itba.pod.tpe2.client.utils.queries;

import ar.edu.itba.pod.tpe2.collators.PopularInfractionsCollator;
import ar.edu.itba.pod.tpe2.combiners.PopularInfractionsCombinerFactory;
import ar.edu.itba.pod.tpe2.mappers.PopularInfractionsMapper;
import ar.edu.itba.pod.tpe2.models.InfractionCount;
import ar.edu.itba.pod.tpe2.models.Ticket;
import ar.edu.itba.pod.tpe2.reducers.PopularInfractionsReducerFactory;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.JobCompletableFuture;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("deprecation")
public class Query2 <K, T extends Ticket<K>> implements Runnable {
    private static final String OUTPUT_HEADER = "County;InfractionTop1;InfractionTop2;InfractionTop3\n";
    private static final String OUTPUT_NAME = "/query2.csv";

    private final String jobName;
    private final HazelcastInstance hazelcastInstance;
    private final IMap<K, String> infractions;
    private final IMap<Integer, T> tickets;
    private final String outputPath;

    public Query2(String jobName, HazelcastInstance hazelcastInstance, IMap<K, String> infractions, IMap<Integer, T> tickets, String outputPath) {
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
        JobCompletableFuture<Map<String, List<InfractionCount>>> future = jobTracker.newJob(source)
                .mapper(new PopularInfractionsMapper<>())
                .combiner(new PopularInfractionsCombinerFactory<>())
                .reducer(new PopularInfractionsReducerFactory<>())
                .submit(new PopularInfractionsCollator<>(infractions));

        Map<String, List<InfractionCount>> result;

        try {
            result = future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        writeResultToCSV(result);

    }

    private void writeResultToCSV(Map<String, List<InfractionCount>> result) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath + OUTPUT_NAME))) {
            writer.write(OUTPUT_HEADER);

            for (Map.Entry<String, List<InfractionCount>> entry : result.entrySet()) {
                StringBuilder sb = new StringBuilder().append(entry.getKey()).append(";");
                StringJoiner sj = new StringJoiner(";");
                for (InfractionCount infractionCount : entry.getValue()) {
                    if (infractionCount.getInfractionName().isEmpty()) {
                        sj.add("-");
                    } else {
                        sj.add(infractionCount.getInfractionName());
                    }
                }
                sb.append(sj).append("\n");
                writer.write(sb.toString());
            }
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
