package co.com.app.model.modeltokenizacion;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ActionHistory {
    private Integer pkIdVisaRequest;
    private String fpan;
    private String levelOfTrust;
    private Boolean isConfirmed;
    private Timestamp dateRequest;
    private String stateToken;
    private String documentNumber;
    private String documentType;
    private String virtualCardId;
    private String declineReason;
    private String cardLogo;
    private String issuerCardRefId;
    private String walletProviderId;
    private String captureMethod;
    private String tokenRequestorId;
    private String tokenRequestorName;
    private String tokenType;

    private String bin;
    private String binType;
    private String descriptionBin;

    private String pkIdToken;
    private String dateUpdate;
    private String historyStateToken;

    private String nameDevice;
    private String typeDevice;
    private String seid;

    private String description;
    private String visaTokenRequestorId;
    private String mastercardTokenRequestorId;
    private String oldFpan;

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        ActionHistory that = (ActionHistory) o;
        return Objects.equals(pkIdVisaRequest, that.pkIdVisaRequest) &&
                Objects.equals(fpan, that.fpan) &&
                Objects.equals(levelOfTrust, that.levelOfTrust) &&
                Objects.equals(isConfirmed, that.isConfirmed) &&
                Objects.equals(dateRequest, that.dateRequest) &&
                Objects.equals(stateToken, that.stateToken) &&
                Objects.equals(documentNumber, that.documentNumber) &&
                Objects.equals(documentType, that.documentType) &&
                Objects.equals(virtualCardId, that.virtualCardId) &&
                Objects.equals(declineReason, that.declineReason) &&
                Objects.equals(cardLogo, that.cardLogo) &&
                Objects.equals(issuerCardRefId, that.issuerCardRefId) &&
                Objects.equals(walletProviderId, that.walletProviderId) &&
                Objects.equals(captureMethod, that.captureMethod) &&
                Objects.equals(bin, that.bin) &&
                Objects.equals(binType, that.binType) &&
                Objects.equals(descriptionBin, that.descriptionBin) &&
                Objects.equals(pkIdToken, that.pkIdToken) &&
                Objects.equals(dateUpdate, that.dateUpdate) &&
                Objects.equals(historyStateToken, that.historyStateToken) &&
                Objects.equals(nameDevice, that.nameDevice) &&
                Objects.equals(typeDevice, that.typeDevice) &&
                Objects.equals(seid, that.seid) &&
                Objects.equals(description, that.description) &&
                Objects.equals(visaTokenRequestorId, that.visaTokenRequestorId) &&
                Objects.equals(mastercardTokenRequestorId, that.mastercardTokenRequestorId) &&
                Objects.equals(oldFpan, that.oldFpan);
    }

    @Override
    public int hashCode(){
        return Objects.hash(pkIdVisaRequest, fpan, levelOfTrust, dateRequest, stateToken, documentNumber, documentType, virtualCardId, declineReason, cardLogo, issuerCardRefId, walletProviderId, captureMethod, bin, binType, descriptionBin, pkIdToken, dateUpdate, historyStateToken, nameDevice, typeDevice, seid, description, visaTokenRequestorId, mastercardTokenRequestorId, oldFpan);
    }
}
