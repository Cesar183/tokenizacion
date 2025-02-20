package co.com.app.jpa.repository;

import co.com.app.jpa.entities.ActionHistoryEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.sql.Timestamp;
import java.util.List;

public interface JPARepository extends CrudRepository<ActionHistoryEntity, Integer>, QueryByExampleExecutor<ActionHistoryEntity> {
    @Query(value = "SELECT row_number() OVER () AS id, " +
            "    tr.pk_id_visa_request, tr.fpan, tr.level_of_trust, tr.is_confirmed, " +
            "    tr.date_request, tr.state_token, tr.document_number, tr.document_type, " +
            "    tr.virtual_card_id, tr.declined_reason, tr.card_logo, " +
            "    tr.issuer_card_ref_id, tr.wallet_provider_id, tr.capture_method, " +
            "    tb.bin, tb.bin_type, tb.description_bin, " +
            "    td.name_device, td.type_device, td.seid, " +
            "    tht.pk_id_token, tht.state_token AS history_state_token, " +
            "    tht.date_update, twp.description, " +
            "    twp.visa_token_requestor_id, twp.mastercard_token_requestor_id, " +
            "    tuc.old_fpan, tr.token_requestor_id, tr.token_requestor_name, tr.token_type " +
            "FROM schadigi.tbl_token_request tr " +
            "LEFT JOIN schadigi.tbl_bin tb ON (tb.pk_logo_class = tr.card_logo) " +
            "LEFT JOIN schadigi.tbl_devices td ON (td.x_correlation_id = tr.x_correlation_id) " +
            "LEFT JOIN schadigi.tbl_historic_token tht ON (tht.virtual_card_id = tr.virtual_card_id) " +
            "LEFT JOIN schadigi.tbl_wallet_provider twp ON (twp.pk_wallet_provider_id = tr.wallet_provider_id) " +
            "LEFT JOIN schadigi.tbl_update_cards tuc ON (tuc.new_fpan = tr.fpan) " +
            "WHERE tr.document_number = :documentNumber " +
            "  AND tr.document_type = :documentType " +
            "  AND tr.date_request BETWEEN :dateFrom AND :dateTo " +
            "  AND (tr.state_token = ANY(CAST(:stateToken AS text[])) OR COALESCE(:stateToken,'') = '') " +
            "  AND ( " +
            "      (COALESCE(:levelOfTrust, '') = '' AND NOT (tr.level_of_trust = 'SUCCESSFUL' AND tr.is_confirmed = true)) " +
            "      OR ( " +
            "          ('SUCCESSFUL' = ANY(CAST(:levelOfTrust AS text[])) AND ( " +
            "              (tr.is_confirmed = false AND tr.level_of_trust = ANY(CAST('{SUCCESSFUL,REQUIRE_ADDITIONAL_AUTHENTICATION}' AS text[]))) " +
            "          )) " +
            "          OR " +
            "          ('APPROVED' = ANY(CAST(:levelOfTrust AS text[])) AND ( " +
            "              (tr.is_confirmed = true AND tr.level_of_trust = ANY(CAST('{APPROVED,REQUIRE_ADDITIONAL_AUTHENTICATION}' AS text[]))) " +
            "              OR " +
            "              (tr.is_confirmed = false AND tr.level_of_trust = 'APPROVED') " +
            "          )) " +
            "          OR " +
            "          ('DECLINED' = ANY(CAST(:levelOfTrust AS text[])) AND tr.level_of_trust = ANY(CAST('{DECLINED,DENIED}' AS text[]))) " +
            "      ) " +
            "  ) " +
            "  AND (:franchise IS NULL OR :franchise = LEFT(tb.bin, 1)) " +
            "  AND (:tokenizationState IS NULL OR :tokenizationState = tht.state_token) " +
            "  AND (tb.bin_type = ANY(CAST(:typeCustomerCard AS text[])) OR COALESCE(:typeCustomerCard,'') = '') " +
            "  AND (LEFT(tr.fpan, 6) = ANY(CAST(:binCards AS text[])) OR COALESCE(:binCards,'') = '') " +
            "  AND (RIGHT(tr.fpan, 4) = ANY(CAST(:endCards AS text[])) OR COALESCE(:endCards,'') = '') " +
            "  AND (td.type_device = ANY(CAST(:devicesType AS text[])) OR COALESCE(:devicesType,'') = '') " +
            "  AND (td.seid = ANY(CAST(:devicesSeid AS text[])) OR COALESCE(:devicesSeid,'') = '') " +
            "  AND (twp.pk_wallet_provider_id = ANY(CAST(:businessName AS text[])) OR COALESCE(:businessName,'') = '') " +
            "ORDER BY tr.date_request DESC;"
            , nativeQuery = true)
    List<ActionHistoryEntity> getActionHistory(
            @Param("documentNumber") String documentNumber,
            @Param("documentType") String documentType,
            @Param("dateFrom") Timestamp dateFrom,
            @Param("dateTo") Timestamp dateTo,
            @Param("stateToken") String stateToken,
            @Param("levelOfTrust") String levelOfTrust,
            @Param("franchise") String franchise,
            @Param("tokenizationState") String tokenizationState,
            @Param("typeCustomerCard") String typeCustomerCard,
            @Param("binCards") String binCards,
            @Param("endCards") String endCards,
            @Param("businessName") String businessName,
            @Param("devicesType") String devicesType,
            @Param("devicesSeid") String devicesSeid,
            Pageable pageable
    );

    @Query(value = "SELECT count(1) " +
            "FROM schadigi.tbl_token_request tr " +
            "LEFT JOIN schadigi.tbl_bin tb ON (tb.pk_logo_class = tr.card_logo) " +
            "LEFT JOIN schadigi.tbl_devices td ON (td.x_correlation_id = tr.x_correlation_id) " +
            "LEFT JOIN schadigi.tbl_historic_token tht ON (tht.virtual_card_id = tr.virtual_card_id) " +
            "LEFT JOIN schadigi.tbl_wallet_provider twp ON (twp.pk_wallet_provider_id = tr.wallet_provider_id) " +
            "WHERE tr.document_number = :documentNumber  " +
            "  AND tr.document_type = :documentType " +
            "  AND tr.date_request BETWEEN :dateFrom AND :dateTo " +
            "  AND (tr.state_token = ANY(CAST(:stateToken AS text[])) OR COALESCE(:stateToken,'') = '')  " +
            "  AND ( " +
            "      (COALESCE(:levelOfTrust, '') = '' AND NOT (tr.level_of_trust = 'SUCCESSFUL' AND tr.is_confirmed = true)) " +
            "      OR ( " +
            "          ('SUCCESSFUL' = ANY(CAST(:levelOfTrust AS text[])) AND ( " +
            "              (tr.is_confirmed = false AND tr.level_of_trust = ANY(CAST('{SUCCESSFUL,REQUIRE_ADDITIONAL_AUTHENTICATION}' AS text[]))) " +
            "          )) " +
            "          OR " +
            "          ('APPROVED' = ANY(CAST(:levelOfTrust AS text[])) AND ( " +
            "              (tr.is_confirmed = true AND tr.level_of_trust = ANY(CAST('{APPROVED,REQUIRE_ADDITIONAL_AUTHENTICATION}' AS text[]))) " +
            "              OR " +
            "              (tr.is_confirmed = false AND tr.level_of_trust = 'APPROVED') " +
            "          )) " +
            "          OR " +
            "          ('DECLINED' = ANY(CAST(:levelOfTrust AS text[])) AND tr.level_of_trust = ANY(CAST('{DECLINED,DENIED}' AS text[]))) " +
            "      ) " +
            "  )  " +
            "  AND (:franchise IS NULL OR :franchise = LEFT(tb.bin, 1)) " +
            "  AND (:tokenizationState IS NULL OR :tokenizationState = tht.state_token) " +
            "  AND (tb.bin_type = ANY(CAST(:typeCustomerCard AS text[])) OR COALESCE(:typeCustomerCard,'') = '') " +
            "  AND (LEFT(tr.fpan, 6) = ANY(CAST(:binCards AS text[])) OR COALESCE(:binCards,'') = '') " +
            "  AND (RIGHT(tr.fpan, 4) = ANY(CAST(:endCards AS text[])) OR COALESCE(:endCards,'') = '') " +
            "  AND (td.type_device = ANY(CAST(:devicesType AS text[])) OR COALESCE(:devicesType,'') = '') " +
            "  AND (td.seid = ANY(CAST(:devicesSeid AS text[])) OR COALESCE(:devicesSeid,'') = '')     " +
            "  AND (twp.pk_wallet_provider_id = ANY(CAST(:businessName AS text[])) OR COALESCE(:businessName,'') = '') "
            , nativeQuery = true)
    Integer totalActionHistory(
            @Param("documentNumber") String documentNumber,
            @Param("documentType") String documentType,
            @Param("dateFrom" ) Timestamp dateFrom,
            @Param("dateTo" ) Timestamp dateTo,
            @Param("stateToken" ) String stateToken,
            @Param("levelOfTrust" ) String levelOfTrust,
            @Param("franchise") String franchise,
            @Param("tokenizationState") String tokenizationState,
            @Param("typeCustomerCard") String typeCustomerCard,
            @Param("binCards") String binCards,
            @Param("endCards") String endCards,
            @Param("businessName") String businessName,
            @Param("devicesType") String devicesType,
            @Param("devicesSeid") String devicesSeid
    );
}
