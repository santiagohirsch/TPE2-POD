package ar.edu.itba.pod.tpe2.client.utils.populators;

import com.hazelcast.core.IMap;

import java.util.Map;
import java.util.function.Function;

public class InfractionsPopulator<K> extends Populator<K, String> {

    private final Function<String, K> keyParser;

    public InfractionsPopulator(String csvPath, IMap<K, String> hazelcastIMap, Function<String, K> keyParser) {
        super(csvPath, hazelcastIMap);
        this.keyParser = keyParser;
    }

    @Override
    protected Map.Entry<K, String> consumeLine(String[] line) {
        K key = keyParser.apply(line[0]);
        String value = line[1];
        return Map.entry(key, value);
    }
}
