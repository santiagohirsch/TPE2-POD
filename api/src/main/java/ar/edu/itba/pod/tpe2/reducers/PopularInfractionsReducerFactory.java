package ar.edu.itba.pod.tpe2.reducers;

import ar.edu.itba.pod.tpe2.models.Pair;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

@SuppressWarnings("deprecation")
public class PopularInfractionsReducerFactory<K> implements ReducerFactory<Pair<String, K>, Integer, Integer> {

    @Override
    public Reducer<Integer, Integer> newReducer(Pair<String, K> stringKPair) {
        return new PopularInfractionsReducer();
    }

    private static class PopularInfractionsReducer extends Reducer<Integer, Integer> {
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
