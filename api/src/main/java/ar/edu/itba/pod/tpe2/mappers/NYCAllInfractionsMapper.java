package ar.edu.itba.pod.tpe2.mappers;

import ar.edu.itba.pod.tpe2.models.NYCTicket;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

@SuppressWarnings("deprecation")
public class NYCAllInfractionsMapper implements Mapper<Integer, NYCTicket, Integer, Integer>, HazelcastInstanceAware {
    private transient IMap<Integer, String> infractionsMap;
    private static final String INFRACTIONS_MAP_NAME = "nyc-infractions";

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        infractionsMap = hazelcastInstance.getMap(INFRACTIONS_MAP_NAME);
    }

    @Override
    public void map(Integer integer, NYCTicket nycTicket, Context<Integer, Integer> context) {
        Integer infractionCode = nycTicket.getInfractionCode();
        if (!infractionsMap.containsKey(infractionCode)) {
            return;
        }
        context.emit(infractionCode, 1);
    }
}
