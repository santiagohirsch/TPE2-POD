package ar.edu.itba.pod.tpe2.models.comparators;

import ar.edu.itba.pod.tpe2.models.CountyPlateCount;

import java.util.Comparator;

public class CountyPlateComparator implements Comparator<CountyPlateCount> {
    @Override
    public int compare(CountyPlateCount c1, CountyPlateCount c2) {
        return c1.getCounty().compareTo(c2.getCounty());
    }
}
