package co.com.app.model.models.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Data {
    private List<ActionHistoryResponse> actionHistoryResponse;
}
