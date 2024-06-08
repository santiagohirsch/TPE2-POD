package ar.edu.itba.pod.tpe2.client.utils.populators;

import ar.edu.itba.pod.tpe2.models.*;
import com.hazelcast.core.IMap;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.function.Function;

public class TicketsPopulator<V> extends Populator<Integer, V> {
    private int ticketID = 0;
    private final Function<String[], V> ticketFactory;

    public TicketsPopulator(String csvPath, IMap<Integer, V> hazelcastIMap, Function<String[], V> ticketFactory) {
        super(csvPath, hazelcastIMap);
        this.ticketFactory = ticketFactory;
    }

    @Override
    protected IMap.Entry<Integer, V> consumeLine(String[] line) {
        V ticket = ticketFactory.apply(line);
        return Map.entry(ticketID++, ticket);
    }
}
