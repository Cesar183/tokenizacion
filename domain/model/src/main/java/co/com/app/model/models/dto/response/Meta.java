package co.com.app.model.models.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Meta {
    private String _messageId;
    private String _requestDateTime;
    private String _applicationId;
    private Pagination paginationResponse;
}
