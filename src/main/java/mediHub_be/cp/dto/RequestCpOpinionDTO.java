package mediHub_be.cp.dto;

import lombok.Data;

import java.util.List;

@Data
public class RequestCpOpinionDTO {

    private String cpOpinionContent;
    private List<String> keywordList;
}
