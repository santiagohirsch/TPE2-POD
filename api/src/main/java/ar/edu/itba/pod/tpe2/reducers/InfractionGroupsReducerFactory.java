package ar.edu.itba.pod.tpe2.reducers;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

@SuppressWarnings("deprecation")
public class InfractionGroupsReducerFactory<K> implements ReducerFactory<K, Double, Double> {

    @Override
    public Reducer<Double, Double> newReducer(K k) {
        return new InfractionGroupsReducer();
    }

    private static class InfractionGroupsReducer extends Reducer<Double, Double> {
        private double sum;
        private int count;

        @Override
        public void beginReduce() {
            sum = 0.0;
            count = 0;
        }

        @Override
        public void reduce(Double aDouble) {
            if (aDouble != 0.0) {
                sum += aDouble;
                count++;
            }
        }

        @Override
        public Double finalizeReduce() {
            return count == 0 ? 0 : sum/count;
        }
    }
}
