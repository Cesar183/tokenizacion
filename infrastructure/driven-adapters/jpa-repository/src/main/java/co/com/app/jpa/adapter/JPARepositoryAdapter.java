package co.com.app.jpa.adapter;

import co.com.app.jpa.entities.ActionHistoryEntity;
import co.com.app.jpa.helper.AdapterOperations;
import co.com.app.jpa.repository.JPARepository;
import co.com.app.model.models.dto.request.Pagination;
import co.com.app.model.modeltokenizacion.ActionHistory;
import co.com.app.model.modeltokenizacion.gateways.ActionHistoryRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

import static co.com.app.model.models.utils.StringUtils.toInt;

@Repository
public class JPARepositoryAdapter extends AdapterOperations<ActionHistory, ActionHistoryEntity, Integer, JPARepository> implements ActionHistoryRepository
{

    public JPARepositoryAdapter(JPARepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.mapBuilder(d, ActionHistory.ActionHistoryBuilder.class).build());
    }

    @Override
    public List<ActionHistory> getActionHistory(String identificationNumber, String identificactionType, Timestamp dateFrom, Timestamp dateTo, String stateToken, String levelOfTrut, String franchise, String tokenizationState, String typeCustomerCard, String binCards, String endCards, String businessName, String devicesType, String devicesSeid, Pagination pagination) {
        int pageSize = toInt(pagination.getPageSize(), "pagination.pageSize");
        int pageNumber = toInt(pagination.getPageNumber(), "pagination.pageNumber");
        Pageable pageable = PageRequest.of(pageNumber-1, pageSize);

        return super.toList(repository.getActionHistory(identificationNumber, identificactionType, dateFrom, dateTo,
                stateToken, levelOfTrut, franchise, tokenizationState, typeCustomerCard, binCards, endCards,
                businessName, devicesType, devicesSeid, pageable));
    }

    @Override
    public Integer totalActionHistory(String identificationNumber, String identificationType, Timestamp dateFrom, Timestamp dateTo, String stateToken, String levelOfTrust, String franchise, String tokenizationState, String typeCustomerCard, String binCards, String endCards, String businessName, String devicesType, String devicesSeid) {
        return repository.totalActionHistory(identificationNumber, identificationType, dateFrom, dateTo, stateToken,
                levelOfTrust, franchise, tokenizationState, typeCustomerCard, binCards, endCards, businessName,
                devicesType, devicesSeid);
    }
}
