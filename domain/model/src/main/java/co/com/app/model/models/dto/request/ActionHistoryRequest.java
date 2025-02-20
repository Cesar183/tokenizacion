package co.com.app.model.models.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActionHistoryRequest {
    private List<SearchAction> searchAction;
    private SearchDate searchDate;
    private SearchTokenization searchTokenization;
}
