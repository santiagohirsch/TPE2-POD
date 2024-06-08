package ar.edu.itba.pod.tpe2.mappers;

import ar.edu.itba.pod.tpe2.models.Pair;
import ar.edu.itba.pod.tpe2.models.Ticket;
import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;

import java.time.LocalDateTime;

@SuppressWarnings("deprecation")
public class TopPlateMapper<K, T extends Ticket<K>> implements Mapper<Integer, T, Pair<String, String>, Integer> {

    private final LocalDateTime from;
    private final LocalDateTime to;

    public TopPlateMapper(LocalDateTime from, LocalDateTime to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public void map(Integer integer, T ticket, Context<Pair<String, String>, Integer> context) {
        LocalDateTime issueDate = ticket.getIssueDate();
        if (from.isAfter(issueDate) || to.isBefore(issueDate)) {
            return;
        }

        String county = ticket.getCountyName();
        String plate = ticket.getPlate();

        context.emit(new Pair<>(county, plate), 1);
    }
}
