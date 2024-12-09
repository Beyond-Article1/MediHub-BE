package mediHub_be.cp.repository;

import mediHub_be.cp.dto.ResponseCpSearchCategoryDataDTO;
import mediHub_be.cp.entity.CpSearchCategoryData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CpSearchCategoryDataRepository extends JpaRepository<CpSearchCategoryData, Long> {

    @Query("SELECT new mediHub_be.cp.dto.ResponseCpSearchCategoryDataDTO( " +
            "cscd.cpSearchCategoryDataSeq, " +
            "cscd.cpSearchCategorySeq, " +
            "csc.cpSearchCategoryName, " +
            "cscd.cpSearchCategoryDataName, " +
            "cscd.userSeq, " +
            "u.userName, " +
            "u.userId, " +
            "cscd.createdAt, " +
            "cscd.updatedAt, " +
            "cscd.deletedAt) " +
            "FROM CpSearchCategoryData AS cscd " +
            "LEFT JOIN CpSearchCategory AS csc ON cscd.cpSearchCategorySeq = csc.cpSearchCategorySeq " +
            "JOIN User AS u ON cscd.userSeq = u.userSeq " +
            "WHERE csc.cpSearchCategorySeq = :cpSearchCategorySeq")
    List<ResponseCpSearchCategoryDataDTO> findByCpSearchCategorySeq(@Param("cpSearchCategorySeq") long cpSearchCategorySeq);

    @Query("SELECT new mediHub_be.cp.dto.ResponseCpSearchCategoryDataDTO( " +
            "cscd.cpSearchCategoryDataSeq, " +
            "cscd.cpSearchCategorySeq, " +
            "csc.cpSearchCategoryName, " +
            "cscd.cpSearchCategoryDataName, " +
            "cscd.userSeq, " +
            "u.userName, " +
            "u.userId, " +
            "cscd.createdAt, " +
            "cscd.updatedAt, " +
            "cscd.deletedAt) " +
            "FROM CpSearchCategoryData AS cscd " +
            "LEFT JOIN CpSearchCategory AS csc ON cscd.cpSearchCategorySeq = csc.cpSearchCategorySeq " +
            "JOIN User AS u ON cscd.userSeq = u.userSeq " +
            "WHERE csc.cpSearchCategorySeq = :cpSearchCategorySeq " +
            "AND cscd.cpSearchCategoryDataSeq = :cpSearchCategoryDataSeq")
    ResponseCpSearchCategoryDataDTO findByCpSearchCategorySeqAndDataSeq(@Param("cpSearchCategorySeq") long cpSearchCategorySeq,
                                                                        @Param("cpSearchCategoryDataSeq") long cpSearchCategoryDataSeq);

}
