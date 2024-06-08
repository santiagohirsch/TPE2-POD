package ar.edu.itba.pod.tpe2.mappers;

import ar.edu.itba.pod.tpe2.models.Ticket;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

@SuppressWarnings("deprecation")
public class TopAgenciesMapper <K, E, T extends Ticket<K, E>> implements Mapper<Integer, T, String, Double> {

    @Override
    public void map(Integer integer, T ticket, Context<String, Double> context) {
        String agency = ticket.getIssuingAgency();
        Double fineAmount = ticket.getFineAmount();

        context.emit(agency, fineAmount);
    }
}
