package ar.edu.itba.pod.tpe2.models;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.UUID;

public class CHITicket implements DataSerializable {

//    issue_date: Fecha y hora de la multa (Formato YYYY-MM-DD hh:mm:ss)
//    license_plate_number: Patente (UUID)
//    violation_code: Código de la infracción (Cadena de caracteres)
//    unit_description: Agencia Recaudadora (Cadena de caracteres)
//    fine_level1_amount: Monto (Entero)
//    community_area_name: Barrio (Cadena de caracteres)
    private LocalDateTime issueDate;
    private UUID licensePlateNumber;
    private String violationCode;
    private String unitDescription;
    private int fineLevel1Amount;
    private String communityAreaName;

    public CHITicket() {
        //Hazelcast
    }

    public CHITicket(LocalDateTime issueDate, UUID licensePlateNumber, String violationCode, String unitDescription, int fineLevel1Amount, String communityAreaName) {
        this.issueDate = issueDate;
        this.licensePlateNumber = licensePlateNumber;
        this.violationCode = violationCode;
        this.unitDescription = unitDescription;
        this.fineLevel1Amount = fineLevel1Amount;
        this.communityAreaName = communityAreaName;
    }

    public LocalDateTime getIssueDate() {
        return issueDate;
    }

    public UUID getLicensePlateNumber() {
        return licensePlateNumber;
    }

    public String getViolationCode() {
        return violationCode;
    }

    public String getUnitDescription() {
        return unitDescription;
    }

    public int getFineLevel1Amount() {
        return fineLevel1Amount;
    }

    public String getCommunityAreaName() {
        return communityAreaName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CHITicket chiTicket = (CHITicket) o;
        return fineLevel1Amount == chiTicket.fineLevel1Amount &&
                Objects.equals(issueDate, chiTicket.issueDate) &&
                Objects.equals(licensePlateNumber, chiTicket.licensePlateNumber) &&
                Objects.equals(violationCode, chiTicket.violationCode) &&
                Objects.equals(unitDescription, chiTicket.unitDescription) &&
                Objects.equals(communityAreaName, chiTicket.communityAreaName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(issueDate, licensePlateNumber, violationCode, unitDescription, fineLevel1Amount, communityAreaName);
    }

    @Override
    public String toString() {
        return "CHITicket{" +
                "issueDate=" + issueDate +
                ", licensePlateNumber=" + licensePlateNumber +
                ", violationCode='" + violationCode + '\'' +
                ", unitDescription='" + unitDescription + '\'' +
                ", fineLevel1Amount=" + fineLevel1Amount +
                ", communityAreaName='" + communityAreaName + '\'' +
                '}';
    }

    @Override
    public void writeData(ObjectDataOutput objectDataOutput) throws IOException {
        objectDataOutput.writeLong(issueDate.toInstant(ZoneOffset.UTC).toEpochMilli());
        objectDataOutput.writeUTF(licensePlateNumber.toString());
        objectDataOutput.writeUTF(violationCode);
        objectDataOutput.writeUTF(unitDescription);
        objectDataOutput.writeInt(fineLevel1Amount);
        objectDataOutput.writeUTF(communityAreaName);
    }

    @Override
    public void readData(ObjectDataInput objectDataInput) throws IOException {
        issueDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(objectDataInput.readLong()), ZoneOffset.UTC);
        licensePlateNumber = UUID.fromString(objectDataInput.readUTF());
        violationCode = objectDataInput.readUTF();
        unitDescription = objectDataInput.readUTF();
        fineLevel1Amount = objectDataInput.readInt();
        communityAreaName = objectDataInput.readUTF();
    }
}
