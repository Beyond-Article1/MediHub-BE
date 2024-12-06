package mediHub_be.dept.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DeptDTO {
    private Long deptSeq;
    private String deptName;
}
