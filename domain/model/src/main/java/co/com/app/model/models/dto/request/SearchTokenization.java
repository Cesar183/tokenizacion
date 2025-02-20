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
public class SearchTokenization {
    private String state;
    private SearchCard searchCard;
    private List<SearchBusiness> searchBusiness;
    private List<SearchDevice> searchDevice;
}
