package ar.edu.itba.pod.tpe2.client.utils.populators;

import com.hazelcast.core.IMap;

import java.util.Map;

public class NYCInfractionsPopulator extends Populator<Integer, String> {

    public NYCInfractionsPopulator(String csvPath, IMap<Integer, String> hazelcastIMap) {
        super(csvPath, hazelcastIMap);
    }

    @Override
    protected Map.Entry<Integer, String> consumeLine(String[] line) {
        //code;definition -> int;string
        return Map.entry(Integer.parseInt(line[0]), line[1]);
    }
}
