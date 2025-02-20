package co.com.app.model.models.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Data {
    private Customer customer;
    private ActionHistoryRequest actionHistoryRequest;
    private Pagination pagination;
}
