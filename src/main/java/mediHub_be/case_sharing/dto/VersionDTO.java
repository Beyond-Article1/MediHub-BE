package mediHub_be.case_sharing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VersionDTO {
    private Long versionSeq;
    private Integer versionNum;
    private boolean versionIsLatest;
}
