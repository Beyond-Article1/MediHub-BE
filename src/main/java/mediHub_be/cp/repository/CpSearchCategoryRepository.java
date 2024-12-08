package mediHub_be.cp.repository;

import mediHub_be.cp.dto.ResponseCpSearchCategoryDTO;
import mediHub_be.cp.entity.CpSearchCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CpSearchCategoryRepository extends JpaRepository<CpSearchCategory, Long> {

    @Query("SELECT new mediHub_be.cp.dto.ResponseCpSearchCategoryDTO( " +
            "csc.cpSearchCategorySeq, " +
            "csc.cpSearchCategoryName, " +
            "csc.createdAt, " +
            "csc.updatedAt, " +
            "csc.deletedAt, " +
            "u.userSeq, " +
            "u.userName, " +
            "u.userId) " +
            "FROM CpSearchCategory AS csc " +
            "LEFT JOIN User AS u ON csc.userSeq = u.userSeq")
    List<ResponseCpSearchCategoryDTO> findJoinUserOnUserSeq();

    @Query("SELECT new mediHub_be.cp.dto.ResponseCpSearchCategoryDTO( " +
            "csc.cpSearchCategorySeq, " +
            "csc.cpSearchCategoryName, " +
            "csc.createdAt, " +
            "csc.updatedAt, " +
            "csc.deletedAt, " +
            "u.userSeq, " +
            "u.userName, " +
            "u.userId) " +
            "FROM CpSearchCategory AS csc " +
            "LEFT JOIN User AS u ON csc.userSeq = u.userSeq " +
            "WHERE csc.cpSearchCategorySeq = :cpSearchCategorySeq")
    ResponseCpSearchCategoryDTO findByCpSearchCategorySeq(@Param("cpSearchCategorySeq") long cpSearchCategorySeq);
}
