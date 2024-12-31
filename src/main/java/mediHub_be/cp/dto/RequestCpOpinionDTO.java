package mediHub_be.cp.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RequestCpOpinionDTO {

    private String cpOpinionContent;
    private List<String> keywordList;
}
