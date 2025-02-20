package co.com.app.jpa.entities;

import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.Entity;
import jakarta.persistence.SqlResultSetMapping;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity
@SqlResultSetMapping(
        name = "ActionHistoryMapping",
        classes = @ConstructorResult(
                targetClass = ActionHistoryEntity.class,
                columns = {
                        @ColumnResult(name = "id", type = Integer.class),
                        @ColumnResult(name = "pk_id_visa_request", type = Integer.class),
                        @ColumnResult(name = "fpan", type = String.class),
                        @ColumnResult(name = "level_of_trust", type = String.class),
                        @ColumnResult(name = "date_request", type = Timestamp.class),
                        @ColumnResult(name = "state_token", type = String.class),
                        @ColumnResult(name = "document_number", type = String.class),
                        @ColumnResult(name = "document_type", type = String.class),
                        @ColumnResult(name = "virtual_card_id", type = String.class),
                        @ColumnResult(name = "declined_reason", type = String.class),
                        @ColumnResult(name = "card_logo", type = String.class),
                        @ColumnResult(name = "issuer_card_ref_id", type = String.class),
                        @ColumnResult(name = "wallet_provider_id", type = String.class),
                        @ColumnResult(name = "capture_method", type = String.class),
                        @ColumnResult(name = "bin", type = String.class),
                        @ColumnResult(name = "bin_type", type = String.class),
                        @ColumnResult(name = "description_bin", type = String.class),
                        @ColumnResult(name = "pk_id_token", type = Integer.class),
                        @ColumnResult(name = "date_update", type = Timestamp.class),
                        @ColumnResult(name = "history_state_token", type = String.class),
                        @ColumnResult(name = "name_device", type = String.class),
                        @ColumnResult(name = "type_device", type = String.class),
                        @ColumnResult(name = "seid", type = String.class),
                        @ColumnResult(name = "description", type = String.class),
                        @ColumnResult(name = "visa_token_requestor_id", type = String.class),
                        @ColumnResult(name = "mastercard_token_requestor_id", type = String.class),
                        @ColumnResult(name = "old_fpan", type = String.class),
                        @ColumnResult(name = "token_requestor_id", type = String.class),
                        @ColumnResult(name = "token_requestor_name", type = String.class),
                        @ColumnResult(name = "is_confirmed", type = Boolean.class)
                }
        )
)
public class ActionHistoryEntity {
    @jakarta.persistence.Id
    @jakarta.persistence.Column(name = "id")
    private Integer id;
    @jakarta.persistence.Column(name = "pk_id_visa_request")
    private Integer pkIdVisaRequest;
    @jakarta.persistence.Column(name = "fpan")
    private String fpan;
    @jakarta.persistence.Column(name = "level_of_trust")
    private String levelOfTrust;
    @jakarta.persistence.Column(name = "date_request")
    private Timestamp dateRequest;
    @jakarta.persistence.Column(name = "state_token")
    private String stateToken;
    @jakarta.persistence.Column(name = "document_number")
    private String documentNumber;
    @jakarta.persistence.Column(name = "document_type")
    private String documentType;
    @jakarta.persistence.Column(name = "virtual_card_id")
    private String virtualCardId;
    @jakarta.persistence.Column(name = "declined_reason")
    private String declinedReason;
    @jakarta.persistence.Column(name = "card_logo")
    private String cardLogo;
    @jakarta.persistence.Column(name = "issuer_card_ref_id")
    private String issuerCardRefId;
    @jakarta.persistence.Column(name = "wallet_provider_id")
    private String walletProviderId;
    @jakarta.persistence.Column(name = "capture_method")
    private String captureMethod;
    @jakarta.persistence.Column(name = "bin")
    private String bin;
    @jakarta.persistence.Column(name = "bin_type")
    private String binType;
    @jakarta.persistence.Column(name = "description_bin")
    private String descriptionBin;
    @jakarta.persistence.Column(name = "pk_id_token")
    private Integer pkIdToken;
    @jakarta.persistence.Column(name = "date_update")
    private Timestamp dateUpdate;
    @jakarta.persistence.Column(name = "history_state_token")
    private String historyStateToken;
    @jakarta.persistence.Column(name = "name_device")
    private String nameDevice;
    @jakarta.persistence.Column(name = "type_device")
    private String typeDevice;
    @jakarta.persistence.Column(name = "seid")
    private String seid;
    @jakarta.persistence.Column(name = "description")
    private String description;
    @jakarta.persistence.Column(name = "visa_token_requestor_id")
    private String visaTokenRequestorId;
    @jakarta.persistence.Column(name = "mastercard_token_requestor_id")
    private String mastercardTokenRequestorId;
    @jakarta.persistence.Column(name = "old_fpan")
    private String oldFpan;
    @jakarta.persistence.Column(name = "is_confirmed")
    private Boolean isConfirmed;
}
