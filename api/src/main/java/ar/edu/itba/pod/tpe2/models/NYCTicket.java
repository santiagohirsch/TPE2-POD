package ar.edu.itba.pod.tpe2.models;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class NYCTicket extends Ticket<Integer, LocalDate> {
    public NYCTicket() {
        //Hazelcast
    }

    public NYCTicket(String plate, LocalDate issueDate, Integer infractionCode, Double fineAmount, String countyName, String issuingAgency) {
        super(plate, issueDate, infractionCode, fineAmount, countyName, issuingAgency);
    }

    @Override
    public void writeData(ObjectDataOutput objectDataOutput) throws IOException {
        objectDataOutput.writeUTF(getPlate());
        objectDataOutput.writeLong(getIssueDate().atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli());
        objectDataOutput.writeInt(getInfractionCode());
        objectDataOutput.writeDouble(getFineAmount());
        objectDataOutput.writeUTF(getCountyName());
        objectDataOutput.writeUTF(getIssuingAgency());
    }

    @Override
    public void readData(ObjectDataInput objectDataInput) throws IOException {
        setPlate(objectDataInput.readUTF());
        setIssueDate(LocalDate.from(LocalDateTime.ofInstant(Instant.ofEpochMilli(objectDataInput.readLong()), ZoneOffset.UTC)));
        setInfractionCode(objectDataInput.readInt());
        setFineAmount(objectDataInput.readDouble());
        setCountyName(objectDataInput.readUTF());
        setIssuingAgency(objectDataInput.readUTF());
    }
}
