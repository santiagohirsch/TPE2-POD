package ar.edu.itba.pod.tpe2.models;

public class CountyPlateCount {
    private final String county;
    private final String plate;
    private final Integer infractionAmount;

    public CountyPlateCount(String county, String plate, Integer infractionAmount) {
        this.county = county;
        this.plate = plate;
        this.infractionAmount = infractionAmount;
    }

    public String getCounty() {
        return county;
    }

    public String getPlate() {
        return plate;
    }

    public Integer getInfractionAmount() {
        return infractionAmount;
    }
}
