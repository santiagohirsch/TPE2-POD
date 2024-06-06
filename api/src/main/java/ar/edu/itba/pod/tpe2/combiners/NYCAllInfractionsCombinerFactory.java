package ar.edu.itba.pod.tpe2.combiners;

import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

@SuppressWarnings("deprecation")
public class NYCAllInfractionsCombinerFactory implements CombinerFactory<Integer, Integer, Integer> {

    @Override
    public Combiner<Integer, Integer> newCombiner(Integer integer) {
        return new NYCAllInfractionsCombiner();
    }

    private static class NYCAllInfractionsCombiner extends Combiner<Integer, Integer> {
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
