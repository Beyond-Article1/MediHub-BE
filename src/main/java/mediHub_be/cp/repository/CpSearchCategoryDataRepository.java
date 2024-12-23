package mediHub_be.cp.repository;

import mediHub_be.cp.dto.ResponseCpSearchCategoryDataDTO;
import mediHub_be.cp.dto.ResponseSimpleCpSearchCategoryDataDTO;
import mediHub_be.cp.entity.CpSearchCategoryData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

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
            "WHERE csc.cpSearchCategorySeq = :cpSearchCategorySeq " +
            "AND cscd.deletedAt IS NULL")
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
            "WHERE cscd.cpSearchCategoryDataSeq = :cpSearchCategoryDataSeq")
    Optional<ResponseCpSearchCategoryDataDTO> findByCpSearchCategoryDataSeq(@Param("cpSearchCategoryDataSeq") long cpSearchCategoryDataSeq);

    boolean existsByCpSearchCategoryDataName(String cpSearchCategoryDataName);

    boolean existsByCpSearchCategorySeq(long cpSearchCategorySeq);

    @Query("SELECT new mediHub_be.cp.dto.ResponseSimpleCpSearchCategoryDataDTO( " +
            "cscd.cpSearchCategoryDataSeq, " +
            "cscd.cpSearchCategoryDataName) " +
            "FROM CpSearchCategoryData AS cscd " +
            "WHERE cscd.cpSearchCategorySeq = :cpSearchCategorySeq")
    List<ResponseSimpleCpSearchCategoryDataDTO> findByCpSearchCategoryDataSeqAndDeletedAtIsNull(@Param("cpSearchCategorySeq") long cpSearchCategorySeq);
}
