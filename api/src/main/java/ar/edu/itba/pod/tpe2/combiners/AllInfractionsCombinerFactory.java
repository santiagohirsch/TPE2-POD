package ar.edu.itba.pod.tpe2.combiners;

import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

@SuppressWarnings("deprecation")
public class AllInfractionsCombinerFactory<K> implements CombinerFactory<K, Integer, Integer> {

    @Override
    public Combiner<Integer, Integer> newCombiner(K key) {
        return new AllInfractionsCombiner();
    }

    private static class AllInfractionsCombiner extends Combiner<Integer, Integer> {
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