package ar.edu.itba.pod.tpe2.models;

public class InfractionCount {
    private final String infractionName;
    private final Integer ticketsAmount;

    public InfractionCount(String infractionName, Integer ticketsAmount) {
        this.infractionName = infractionName;
        this.ticketsAmount = ticketsAmount;
    }

    public String getInfractionName() {
        return infractionName;
    }

    public Integer getTicketsAmount() {
        return ticketsAmount;
    }
}
