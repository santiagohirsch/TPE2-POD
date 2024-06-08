package ar.edu.itba.pod.tpe2.client.utils.populators.keyParsers;

import java.util.function.Function;

public class CHIInfractionParser implements Function<String, String> {

    @Override
    public String apply(String key) {
        return key;
    }
}
