package ar.edu.itba.pod.tpe2.collators;

import ar.edu.itba.pod.tpe2.models.InfractionCount;
import ar.edu.itba.pod.tpe2.models.comparators.InfractionCountComparator;
import ar.edu.itba.pod.tpe2.models.Pair;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Collator;

import java.util.*;

@SuppressWarnings("deprecation")
public class PopularInfractionsCollator<K> implements Collator<Map.Entry<Pair<String, K>, Integer>, Map<String, List<InfractionCount>>> {

    private final IMap<K, String> infractions;

    public PopularInfractionsCollator(IMap<K, String> infractions) {
        this.infractions = infractions;
    }

    @Override
    public Map<String, List<InfractionCount>> collate(Iterable<Map.Entry<Pair<String, K>, Integer>> iterable) {
        Map<String, List<InfractionCount>> topInfractionsPerCounty = new HashMap<>();

        iterable.forEach(entry -> {
            String county = entry.getKey().left();
            String infractionName = infractions.get(entry.getKey().right());
            InfractionCount infractionCount = new InfractionCount(infractionName, entry.getValue());
            if (topInfractionsPerCounty.containsKey(county)) {
                topInfractionsPerCounty.get(county).add(infractionCount);
            } else {
                List<InfractionCount> toAdd = new ArrayList<>();
                toAdd.add(infractionCount);
                topInfractionsPerCounty.put(county, toAdd);
            }
        });

        Map<String, List<InfractionCount>> sortedTopInfractionsPerCounty = new TreeMap<>(topInfractionsPerCounty);

        sortedTopInfractionsPerCounty.forEach((county, infractionCounts) -> {
            infractionCounts.sort(new InfractionCountComparator());

            if (infractionCounts.size() > 3) {
                infractionCounts = infractionCounts.subList(0, 3);
            }

            while (infractionCounts.size() < 3) {
                infractionCounts.add(new InfractionCount("", -1));
            }

            sortedTopInfractionsPerCounty.put(county, infractionCounts);
        });

        return sortedTopInfractionsPerCounty;
    }

}
