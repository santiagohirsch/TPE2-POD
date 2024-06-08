package ar.edu.itba.pod.tpe2.models;

import com.hazelcast.nio.serialization.DataSerializable;

import java.io.Serializable;
import java.time.LocalDate;

public abstract class Ticket<K, E> implements DataSerializable {
    private String plate;
    private E issueDate;
    private K infractionCode;
    private Double fineAmount;
    private String countyName;
    private String issuingAgency;

    public Ticket() {
        //Hazelcast
    }

    public Ticket(String plate, E issueDate, K infractionCode, Double fineAmount, String countyName, String issuingAgency) {
        this.plate = plate;
        this.issueDate = issueDate;
        this.infractionCode = infractionCode;
        this.fineAmount = fineAmount;
        this.countyName = countyName;
        this.issuingAgency = issuingAgency;
    }

    public String getPlate() {
        return plate;
    }

    public E getIssueDate() {
        return issueDate;
    }

    public K getInfractionCode() {
        return infractionCode;
    }

    public Double getFineAmount() {
        return fineAmount;
    }

    public String getCountyName() {
        return countyName;
    }

    public String getIssuingAgency() {
        return issuingAgency;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public void setIssueDate(E issueDate) {
        this.issueDate = issueDate;
    }

    public void setInfractionCode(K infractionCode) {
        this.infractionCode = infractionCode;
    }

    public void setFineAmount(Double fineAmount) {
        this.fineAmount = fineAmount;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public void setIssuingAgency(String issuingAgency) {
        this.issuingAgency = issuingAgency;
    }
}
