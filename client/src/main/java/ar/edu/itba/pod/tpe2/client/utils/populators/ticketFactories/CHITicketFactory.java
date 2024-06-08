package ar.edu.itba.pod.tpe2.client.utils.populators.ticketFactories;

import ar.edu.itba.pod.tpe2.models.CHITicket_;
import com.hazelcast.core.IMap;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

public class CHITicketFactory implements Function<String[], CHITicket_> {
    @Override
    public CHITicket_ apply(String[] line) {
        LocalDateTime issueDate = LocalDateTime.parse(line[0], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String licensePlateNumber = line[1];
        String violationCode = line[2];
        String unitDescription = line[3];
        int fineLevel1Amount = Integer.parseInt(line[4]);
        String communityAreaName = line[5];
        return new CHITicket_(licensePlateNumber, issueDate, violationCode, fineLevel1Amount, communityAreaName, unitDescription);
    }
}
