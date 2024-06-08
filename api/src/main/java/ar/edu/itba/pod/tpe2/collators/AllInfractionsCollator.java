package ar.edu.itba.pod.tpe2.collators;

import ar.edu.itba.pod.tpe2.models.InfractionCount;
import ar.edu.itba.pod.tpe2.models.comparators.InfractionCountComparator;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Collator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("deprecation")
public class AllInfractionsCollator<K> implements Collator<Map.Entry<K, Integer>, List<InfractionCount>> {
    private final IMap<K, String> infractions;

    public AllInfractionsCollator(IMap<K, String> infractions) {
        this.infractions = infractions;
    }

    @Override
    public List<InfractionCount> collate(Iterable<Map.Entry<K, Integer>> iterable) {
        List<InfractionCount> allInfractions = new ArrayList<>();

        List<InfractionCount> finalAllInfractions = allInfractions;
        iterable.forEach(entry -> {
            String infractionName = infractions.get(entry.getKey());
            Integer ticketsCount = entry.getValue();
            InfractionCount toAdd = new InfractionCount(infractionName, ticketsCount);
            finalAllInfractions.add(toAdd);
        });

        allInfractions = allInfractions.parallelStream().sorted(new InfractionCountComparator()).collect(Collectors.toList());
        return allInfractions;
    }

}
