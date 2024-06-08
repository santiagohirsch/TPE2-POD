package ar.edu.itba.pod.tpe2.reducers;

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

@SuppressWarnings("deprecation")
public class AllInfractionsReducerFactory<K> implements ReducerFactory<K, Integer, Integer> {

    @Override
    public Reducer<Integer, Integer> newReducer(K key) {
        return new AllInfractionsReducer();
    }

    private static class AllInfractionsReducer extends Reducer<Integer, Integer> {
        private int sum;

        @Override
        public void beginReduce() {
            sum = 0;
        }

        @Override
        public void reduce(Integer integer) {
            sum += integer;
        }

        @Override
        public Integer finalizeReduce() {
            return sum;
        }
    }
}
