package ar.edu.itba.pod.tpe2.client.utils.populators;

import com.hazelcast.core.IMap;

import java.util.Map;

public class CHIInfractionsPopulator extends Populator<String, String> {

    public CHIInfractionsPopulator(String csvPath, IMap<String, String> hazelcastIMap) {
        super(csvPath, hazelcastIMap);
    }

    @Override
    protected Map.Entry<String, String> consumeLine(String[] line) {
        //violation_code;violation_description -> string;string (violation_code is alphanumerical)
        return Map.entry(line[0], line[1]);
    }
}
