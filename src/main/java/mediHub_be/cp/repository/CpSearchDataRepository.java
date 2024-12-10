package mediHub_be.cp.repository;

import mediHub_be.cp.dto.ResponseCpSearchDataDTO;
import mediHub_be.cp.entity.CpSearchData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CpSearchDataRepository extends JpaRepository<CpSearchData, Long> {

    @Query("SELECT new mediHub_be.cp.dto.ResponseCpSearchDataDTO( " +
            "   csd.cpSearchDataSeq, " +
            "   csd.cpVersionSeq, " +
            "   c.cpName, " +
            "   cscd.cpSearchCategoryDataSeq, " +
            "   cscd.cpSearchCategoryDataName) " +
            "FROM CpSearchData AS csd " +
            "JOIN CpSearchCategoryData AS cscd ON csd.cpSearchCategoryDataSeq = cscd.cpSearchCategoryDataSeq " +
            "JOIN CpVersion AS cv ON csd.cpVersionSeq = cv.cpVersionSeq " +
            "JOIN Cp AS c ON cv.cpSeq = c.cpSeq " +
            "WHERE cv.cpVersionSeq = :cpVersionSeq")
    List<ResponseCpSearchDataDTO> findDtoJoinCpSearchCategoryDataOnCpSearchCategorySeqCpVersionOnCpVersionSeq(@Param("cpVersionSeq") long cpVersionSeq);
}
