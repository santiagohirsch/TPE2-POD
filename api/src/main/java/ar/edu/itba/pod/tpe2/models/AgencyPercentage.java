package ar.edu.itba.pod.tpe2.models;

public class AgencyPercentage {
    private final String agencyName;
    private final Double percentage;

    public AgencyPercentage(String agencyName, Double amountCollected) {
        this.agencyName = agencyName;
        this.percentage = amountCollected;
    }

    public String getAgencyName() {
        return agencyName;
    }

    public Double getPercentage() {
        return percentage;
    }
}
