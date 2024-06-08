package ar.edu.itba.pod.tpe2.client.utils.populators.keyParsers;

import java.util.function.Function;

public class NYCInfractionParser implements Function<String, Integer> {

    @Override
    public Integer apply(String key) {
        return Integer.parseInt(key);
    }
}
