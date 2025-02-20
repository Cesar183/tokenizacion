package co.com.app.model.modeltokenizacion.gateways;

import co.com.app.model.models.dto.request.Pagination;
import co.com.app.model.modeltokenizacion.ActionHistory;

import java.sql.Timestamp;
import java.util.List;

public interface ActionHistoryRepository {
    List<ActionHistory> getActionHistory(String identificationNumber, String identificactionType, Timestamp dateFrom,
                                          Timestamp dateTo, String stateToken, String levelOfTrut, String franchise,
                                          String tokenizationState, String typeCustomerCard, String binCards, String endCards,
                                          String businessName,String devicesType, String devicesSeid, Pagination pagination);

    Integer totalActionHistory(String identificationNumber, String identificationType, Timestamp dateFrom,
                               Timestamp dateTo, String stateToken, String levelOfTrust, String franchise,
                               String tokenizationState, String typeCustomerCard, String binCards, String endCards,
                               String businessName, String devicesType, String devicesSeid);
}
