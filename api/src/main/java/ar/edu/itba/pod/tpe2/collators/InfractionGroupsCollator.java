package ar.edu.itba.pod.tpe2.collators;

import ar.edu.itba.pod.tpe2.models.Pair;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Collator;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("deprecation")
public class InfractionGroupsCollator<K> implements Collator<Map.Entry<K, Double>, Map<Integer, List<Pair<String, String>>>> {
    private final IMap<K, String> infractions;

    public InfractionGroupsCollator(IMap<K, String> infractions) {
        this.infractions = infractions;
    }

    @Override
    public Map<Integer, List<Pair<String, String>>> collate(Iterable<Map.Entry<K, Double>> iterable) {
        Map<Integer, List<String>> allInfractionsPerGroup = new HashMap<>();
        Map<Integer, List<Pair<String, String>>> allPairsPerGroup = new TreeMap<>(Comparator.reverseOrder());
        iterable.forEach(entry -> {
            Double avgFineAmount = entry.getValue();
            int group = Double.valueOf(Math.floor(avgFineAmount/100)).intValue() * 100;
            if (group != 0) {
                String infractionName = infractions.get(entry.getKey());
                List<String> infractionNames = allInfractionsPerGroup.getOrDefault(group, new ArrayList<>());
                infractionNames.add(infractionName);
                allInfractionsPerGroup.put(group, infractionNames);
            }
        });



        allInfractionsPerGroup.forEach((key, infractionNames) -> {
            List<Pair<String, String>> finalInfractionPairs = new ArrayList<>();
            infractionNames = infractionNames.parallelStream().sorted().collect(Collectors.toList());
            for (int i = 0; i < infractionNames.size(); i++) {
                for (int t = i + 1; t < infractionNames.size(); t++) {
                    Pair<String, String> newPair = new Pair<>(infractionNames.get(i), infractionNames.get(t));
                    finalInfractionPairs.add(newPair);
                }
            }

            allPairsPerGroup.put(key, finalInfractionPairs);

        });

        return allPairsPerGroup;
    }
}
