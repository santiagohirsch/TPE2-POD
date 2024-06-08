package ar.edu.itba.pod.tpe2.models.comparators;

import ar.edu.itba.pod.tpe2.models.AgencyPercentage;

import java.util.Comparator;

public class AgencyPercentageComparator implements Comparator<AgencyPercentage> {
    @Override
    public int compare(AgencyPercentage o1, AgencyPercentage o2) {
        int amountComparison = o2.getPercentage().compareTo(o1.getPercentage());
        if (amountComparison != 0) {
            return amountComparison;
        }
        return o1.getAgencyName().compareTo(o2.getAgencyName());
    }
}
