package ar.edu.itba.pod.tpe2.models;

import java.util.Comparator;

public class InfractionCountComparator implements Comparator<InfractionCount> {
    @Override
    public int compare(InfractionCount o1, InfractionCount o2) {
        int ticketCountCmp = Integer.compare(o2.getTicketsAmount(), o1.getTicketsAmount());
        if (ticketCountCmp != 0) {
            return ticketCountCmp;
        }

        return o1.getInfractionName().compareTo(o2.getInfractionName());
    }
}
