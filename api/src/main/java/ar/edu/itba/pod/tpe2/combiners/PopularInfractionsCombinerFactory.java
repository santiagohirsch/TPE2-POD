package ar.edu.itba.pod.tpe2.combiners;

import ar.edu.itba.pod.tpe2.models.Pair;
import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

@SuppressWarnings("deprecation")
public class PopularInfractionsCombinerFactory<K> implements CombinerFactory<Pair<String, K>, Integer, Integer> {

    @Override
    public Combiner<Integer, Integer> newCombiner(Pair<String, K> stringKPair) {
        return new PopularInfractionsCombiner();
    }

    private static class PopularInfractionsCombiner extends Combiner<Integer, Integer> {
        private int sum;

        @Override
        public void reset() {
            sum = 0;
        }

        @Override
        public void combine(Integer integer) {
            sum += integer;
        }

        @Override
        public Integer finalizeChunk() {
            return sum;
        }
    }
}
