package ar.edu.itba.pod.tpe2.client.utils.populators;

import ar.edu.itba.pod.tpe2.models.NYCTicket;
import com.hazelcast.core.IMap;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class NYCTicketsPopulator extends Populator<Integer, NYCTicket> {
    private static int ticketID = 1;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public NYCTicketsPopulator(String csvPath, IMap<Integer, NYCTicket> hazelcastIMap) {
        super(csvPath, hazelcastIMap);
    }

    @Override
    protected Map.Entry<Integer, NYCTicket> consumeLine(String[] line) {
        String plate = line[0];
        LocalDate issueDate = LocalDate.parse(line[1], formatter);
        int infractionCode = Integer.parseInt(line[2]);
        double fineAmount = Double.parseDouble(line[3]);
        String countyName = line[4];
        String issuingAgency = line[5];
        NYCTicket ticket = new NYCTicket(plate, issueDate, infractionCode, fineAmount, countyName, issuingAgency);
        return Map.entry(ticketID++, ticket);
    }
}
