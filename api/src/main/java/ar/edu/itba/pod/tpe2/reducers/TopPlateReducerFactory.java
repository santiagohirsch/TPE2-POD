package ar.edu.itba.pod.tpe2.reducers;

import ar.edu.itba.pod.tpe2.models.Pair;
import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;

@SuppressWarnings("deprecation")
public class TopPlateReducerFactory implements ReducerFactory<Pair<String, String>, Integer, Integer> {

    @Override
    public Reducer<Integer, Integer> newReducer(Pair<String, String> stringStringPair) {
        return new TopPlateReducer();
    }

    private static class TopPlateReducer extends Reducer<Integer, Integer> {
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
