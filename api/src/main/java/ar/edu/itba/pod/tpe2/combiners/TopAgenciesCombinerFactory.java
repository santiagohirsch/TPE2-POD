package ar.edu.itba.pod.tpe2.combiners;

import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;

@SuppressWarnings("deprecation")
public class TopAgenciesCombinerFactory implements CombinerFactory<String, Double, Double> {

    @Override
    public Combiner<Double, Double> newCombiner(String key) { return new TopAgenciesCombiner(); }

    private static class TopAgenciesCombiner extends Combiner<Double, Double> {
        private double sum;

        @Override
        public void reset() {
            sum = 0.0;
        }

        @Override
        public void combine(Double aDouble) {
            sum += aDouble;
        }

        @Override
        public Double finalizeChunk() {
            return sum;
        }
    }

}
