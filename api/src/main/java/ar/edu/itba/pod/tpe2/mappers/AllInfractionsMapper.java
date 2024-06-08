package ar.edu.itba.pod.tpe2.mappers;

import ar.edu.itba.pod.tpe2.models.Ticket;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

@SuppressWarnings("deprecation")
public class AllInfractionsMapper<K, T extends Ticket<K>> implements Mapper<Integer, T, K, Integer>, HazelcastInstanceAware {
    private transient IMap<K, String> infractionsMap;
    private static final String INFRACTIONS_MAP_NAME = "infractions-map";


    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        infractionsMap = hazelcastInstance.getMap(INFRACTIONS_MAP_NAME);
    }

    @Override
    public void map(Integer integer, T ticket, Context<K, Integer> context) {
        K infractionCode = ticket.getInfractionCode();

        if (!infractionsMap.containsKey(infractionCode)) {
            return;
        }

        context.emit(infractionCode, 1);
    }
}
