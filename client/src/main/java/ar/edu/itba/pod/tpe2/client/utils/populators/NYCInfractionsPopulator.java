package ar.edu.itba.pod.tpe2.client.utils.populators;

import ar.edu.itba.pod.tpe2.models.City;
import ar.edu.itba.pod.tpe2.models.Ticket;
import com.hazelcast.core.IMap;

import java.util.Map;

public class NYCInfractionsPopulator extends Populator<Integer, String, Integer> {

    public NYCInfractionsPopulator(String csvPath, IMap<Integer, Ticket<Integer, String, Integer>> hazelcastIMap, City city) {
        super(csvPath, hazelcastIMap, city);
    }

    @Override
    protected Map.Entry<Integer, String> consumeLine(String[] line) {
        //code;definition -> int;string
        return Map.entry(Integer.parseInt(line[0]), line[1]);
    }
}
