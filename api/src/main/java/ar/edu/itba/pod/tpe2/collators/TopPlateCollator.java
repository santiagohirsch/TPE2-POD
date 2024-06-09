package ar.edu.itba.pod.tpe2.collators;

import ar.edu.itba.pod.tpe2.models.CountyPlateCount;
import ar.edu.itba.pod.tpe2.models.InfractionCount;
import ar.edu.itba.pod.tpe2.models.Pair;

import ar.edu.itba.pod.tpe2.models.comparators.CountyPlateComparator;
import com.hazelcast.mapreduce.Collator;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("deprecation")
public class TopPlateCollator implements Collator<Map.Entry<Pair<String, String>, Integer>, List<CountyPlateCount>> {

    @Override
    public List<CountyPlateCount> collate(Iterable<Map.Entry<Pair<String, String>, Integer>> iterable) {
        List<CountyPlateCount> topPlates = new ArrayList<>();
        Map<String, CountyPlateCount> plateCountPerCounty = new HashMap<>();
        iterable.forEach(entry -> {
            String county = entry.getKey().left();
            String plate = entry.getKey().right();
            Integer amount = entry.getValue();
            if (plateCountPerCounty.containsKey(county)) {
                if (plateCountPerCounty.get(county).getInfractionAmount() < amount) {
                    plateCountPerCounty.put(county, new CountyPlateCount(county, plate, amount));
                }
            } else {
                plateCountPerCounty.put(county, new CountyPlateCount(county, plate, amount));
            }

        });

        List<CountyPlateCount> finalTopPlates = topPlates;
        plateCountPerCounty.forEach((key, value) -> finalTopPlates.add(value));

        topPlates = topPlates.parallelStream().sorted(new CountyPlateComparator()).collect(Collectors.toList());

        return topPlates;
    }

}