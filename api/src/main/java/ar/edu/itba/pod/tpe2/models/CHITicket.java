package ar.edu.itba.pod.tpe2.models;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class CHITicket extends Ticket<String, Integer, LocalDateTime> {
    public CHITicket() {
        //Hazelcast
    }

    public CHITicket(String plate, LocalDateTime issueDate, String infractionCode, Integer fineAmount, String countyName, String issuingAgency) {
        super(plate, issueDate, infractionCode, fineAmount, countyName, issuingAgency);
    }

    @Override
    public void writeData(ObjectDataOutput objectDataOutput) throws IOException {
        objectDataOutput.writeLong(getIssueDate().toInstant(ZoneOffset.UTC).toEpochMilli());
        objectDataOutput.writeUTF(getPlate());
        objectDataOutput.writeUTF(getInfractionCode());
        objectDataOutput.writeUTF(getIssuingAgency());
        objectDataOutput.writeInt(getFineAmount());
        objectDataOutput.writeUTF(getCountyName());
    }

    @Override
    public void readData(ObjectDataInput objectDataInput) throws IOException {
        setIssueDate(LocalDateTime.ofInstant(Instant.ofEpochMilli(objectDataInput.readLong()), ZoneOffset.UTC));
        setPlate(objectDataInput.readUTF());
        setInfractionCode(objectDataInput.readUTF());
        setIssuingAgency(objectDataInput.readUTF());
        setFineAmount(objectDataInput.readInt());
        setCountyName(objectDataInput.readUTF());
    }
}
