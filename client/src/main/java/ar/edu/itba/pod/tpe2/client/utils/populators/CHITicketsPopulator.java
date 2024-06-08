package ar.edu.itba.pod.tpe2.client.utils.populators;

import ar.edu.itba.pod.tpe2.models.CHITicket;
import ar.edu.itba.pod.tpe2.models.City;
import ar.edu.itba.pod.tpe2.models.Ticket;
import com.hazelcast.core.IMap;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;

public class CHITicketsPopulator extends Populator<String, Integer, LocalDateTime> {
    private static int ticketID = 1;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public CHITicketsPopulator(String csvPath, IMap<Integer, Ticket<String, Integer, LocalDateTime>> hazelcastIMap, City city) {
        super(csvPath, hazelcastIMap, city);
    }

    //    @Override
//    protected Map.Entry<Integer, CHITicket> consumeLine(String[] line) {
//        LocalDateTime issueDate = LocalDateTime.parse(line[0], formatter);
//        // TODO: ask about uuid length in dataset
//        // UUID licensePlateNumber = UUID.fromString(line[1]);
//        UUID licensePlateNumber = UUID.randomUUID();
//        String violationCode = line[2];
//        String unitDescription = line[3];
//        int fineLevel1Amount = Integer.parseInt(line[4]);
//        String communityAreaName = line[5];
//        CHITicket ticket = new CHITicket(issueDate, licensePlateNumber, violationCode, unitDescription, fineLevel1Amount, communityAreaName);
//        return Map.entry(ticketID++, ticket);
//    }
}
