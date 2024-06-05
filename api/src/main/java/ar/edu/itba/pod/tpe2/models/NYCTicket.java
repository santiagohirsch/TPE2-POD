package ar.edu.itba.pod.tpe2.models;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

public class NYCTicket implements DataSerializable {
//    Plate: Patente (Cadena de caracteres)
//    Issue Date: Fecha de la multa (Formato YYYY-MM-DD)
//    InfractionCode: Código de la infracción (Entero)
//    Fine Amount: Monto (Número)
//    County Name: Barrio (Cadena de caracteres)
//    Issuing Agency: Agencia Recaudadora (Cadena de caracteres)
    private String plate;
    private LocalDate issueDate;
    private int infractionCode;
    private double fineAmount;
    private String countyName;
    private String issuingAgency;

    public NYCTicket(){
        //Hazelcast
    }

    public NYCTicket(String plate, LocalDate issueDate, int infractionCode, double fineAmount, String countyName, String issuingAgency) {
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

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public int getInfractionCode() {
        return infractionCode;
    }

    public double getFineAmount() {
        return fineAmount;
    }

    public String getCountyName() {
        return countyName;
    }

    public String getIssuingAgency() {
        return issuingAgency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NYCTicket nycTicket = (NYCTicket) o;
        return infractionCode == nycTicket.infractionCode &&
                Double.compare(nycTicket.fineAmount, fineAmount) == 0 &&
                Objects.equals(plate, nycTicket.plate) &&
                Objects.equals(issueDate, nycTicket.issueDate) &&
                Objects.equals(countyName, nycTicket.countyName) &&
                Objects.equals(issuingAgency, nycTicket.issuingAgency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(plate, issueDate, infractionCode, fineAmount, countyName, issuingAgency);
    }

    @Override
    public String toString() {
        return "NYCTicket{" +
                "plate='" + plate + '\'' +
                ", issueDate=" + issueDate +
                ", infractionCode=" + infractionCode +
                ", fineAmount=" + fineAmount +
                ", countyName='" + countyName + '\'' +
                ", issuingAgency='" + issuingAgency + '\'' +
                '}';
    }

    @Override
    public void writeData(ObjectDataOutput objectDataOutput) throws IOException {
        objectDataOutput.writeUTF(plate);
        objectDataOutput.writeLong(issueDate.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli());
        objectDataOutput.writeInt(infractionCode);
        objectDataOutput.writeDouble(fineAmount);
        objectDataOutput.writeUTF(countyName);
        objectDataOutput.writeUTF(issuingAgency);
    }

    @Override
    public void readData(ObjectDataInput objectDataInput) throws IOException {
        plate = objectDataInput.readUTF();
        issueDate = LocalDate.from(LocalDateTime.ofInstant(Instant.ofEpochMilli(objectDataInput.readLong()), ZoneOffset.UTC));
        infractionCode = objectDataInput.readInt();
        fineAmount = objectDataInput.readDouble();
        countyName = objectDataInput.readUTF();
        issuingAgency = objectDataInput.readUTF();
    }
}
