package ar.edu.itba.pod.tpe2.client.utils.populators.ticketFactories;

import ar.edu.itba.pod.tpe2.models.NYCTicket;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

public class NYCTicketFactory implements Function<String[], NYCTicket> {
    @Override
    public NYCTicket apply(String[] line) {
        String plate = line[0];
        LocalDateTime issueDate = LocalDate.parse(line[1], DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay();
        int infractionCode = Integer.parseInt(line[2]);
        double fineAmount = Double.parseDouble(line[3]);
        String countyName = line[4];
        String issuingAgency = line[5];
        return new NYCTicket(plate, issueDate, infractionCode, fineAmount, countyName, issuingAgency);
    }
}
