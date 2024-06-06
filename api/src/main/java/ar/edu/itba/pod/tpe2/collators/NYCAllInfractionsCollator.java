package ar.edu.itba.pod.tpe2.collators;

import ar.edu.itba.pod.tpe2.models.InfractionCount;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Collator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@SuppressWarnings("deprecation")
public class NYCAllInfractionsCollator implements Collator<Map.Entry<Integer, Integer>, List<InfractionCount>> {
    private final IMap<Integer, String> infractions;

    public NYCAllInfractionsCollator(IMap<Integer, String> infractions) {
        this.infractions = infractions;
    }

    @Override
    public List<InfractionCount> collate(Iterable<Map.Entry<Integer, Integer>> iterable) {
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

    private static class InfractionCountComparator implements Comparator<InfractionCount> {
        @Override
        public int compare(InfractionCount o1, InfractionCount o2) {
            int ticketCountCmp = Integer.compare(o2.getTicketsAmount(), o1.getTicketsAmount());
            if (ticketCountCmp != 0) {
                return ticketCountCmp;
            }

            return o1.getInfractionName().compareTo(o2.getInfractionName());
        }
    }
}
