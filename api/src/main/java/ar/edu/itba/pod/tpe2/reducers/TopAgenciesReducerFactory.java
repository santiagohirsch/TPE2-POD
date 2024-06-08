package ar.edu.itba.pod.tpe2.reducers;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

@SuppressWarnings("deprecation")
public class TopAgenciesReducerFactory implements ReducerFactory<String, Double, Double> {

    @Override
    public Reducer<Double, Double> newReducer(String s) {
        return new TopAgenciesReducer();
    }

    private static class TopAgenciesReducer extends Reducer<Double, Double> {
        private double sum;

        @Override
        public void beginReduce() {
            sum = 0.0;
        }

        @Override
        public void reduce(Double aDouble) {
            sum += aDouble;
        }

        @Override
        public Double finalizeReduce() {
            return sum;
        }
    }
}
