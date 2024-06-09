package ar.edu.itba.pod.tpe2.combiners;

import ar.edu.itba.pod.tpe2.models.Pair;
import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;


@SuppressWarnings("deprecation")
public class InfractionGroupsCombinerFactory<K> implements CombinerFactory<K, Double, Double> {

    @Override
    public Combiner<Double, Double> newCombiner(K k) {
        return new InfractionGroupsCombiner();
    }

    private static class InfractionGroupsCombiner extends Combiner<Double, Double> {
        private double sum;
        private int count;

        @Override
        public void reset() {
            sum = 0.0;
            count = 0;
        }

        @Override
        public void combine(Double aDouble) {
            sum += aDouble;
            count++;
        }

        @Override
        public Double finalizeChunk() {
            return count == 0 ? 0 : sum/count;
        }
    }
}
