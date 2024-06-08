package ar.edu.itba.pod.tpe2.client.utils.populators.ticketFactories;

import ar.edu.itba.pod.tpe2.models.NYCTicket_;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

public class NYCTicketFactory implements Function<String[], NYCTicket_> {
    @Override
    public NYCTicket_ apply(String[] line) {
        String plate = line[0];
        LocalDate issueDate = LocalDate.parse(line[1], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        int infractionCode = Integer.parseInt(line[2]);
        double fineAmount = Double.parseDouble(line[3]);
        String countyName = line[4];
        String issuingAgency = line[5];
        return new NYCTicket_(plate, issueDate, infractionCode, fineAmount, countyName, issuingAgency);
    }
}
