package ar.edu.itba.pod.tpe2.collators;

import ar.edu.itba.pod.tpe2.models.AgencyPercentage;
import ar.edu.itba.pod.tpe2.models.comparators.AgencyPercentageComparator;
import com.hazelcast.mapreduce.Collator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@SuppressWarnings("deprecation")
public class TopAgenciesCollator implements Collator<Map.Entry<String, Double>, List<AgencyPercentage>> {

    private final int n;

    public TopAgenciesCollator(int n) {
        this.n = n;
    }

    @Override
    public List<AgencyPercentage> collate(Iterable<Map.Entry<String, Double>> iterable) {
        List<AgencyPercentage> agencyPercentages = new ArrayList<>();

        AtomicReference<Double> total = new AtomicReference<>(0.0);
        iterable.forEach(entry -> {
            total.updateAndGet(v -> v + entry.getValue());
        });

        List<AgencyPercentage> finalAgencyPercentages = agencyPercentages;
        iterable.forEach(entry -> {
            String agencyName = entry.getKey();
            Double agencyIncome = entry.getValue();
            AgencyPercentage agencyPercentage = new AgencyPercentage(agencyName, (agencyIncome/ total.get()) * 100);
            finalAgencyPercentages.add(agencyPercentage);
        });

        agencyPercentages = agencyPercentages.parallelStream().sorted(new AgencyPercentageComparator()).collect(Collectors.toList());
        return agencyPercentages.subList(0, Math.min(n, agencyPercentages.size()));
    }
}
