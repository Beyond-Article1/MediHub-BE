package mediHub_be.cp.repository;

import mediHub_be.cp.dto.ResponseCpDTO;
import mediHub_be.cp.entity.CpVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CpVersionRepository extends JpaRepository<CpVersion, Long> {

    @Query("SELECT new map(" +
            "cp.cpName AS cpName, " +
            "cp.cpDescription AS cpDescription, " +
            "cp.cpViewCount AS cpViewCount, " +
            "cv.cpVersion AS cpVersion, " +
            "cv.cpVersionDescription AS cpVersionDescription," +
            "cv.createdAt AS createdAt, " +
            "u.userName AS userName, " +
            "u.userId AS userId, " +
            "p.partName AS partName)" +
            "FROM CpVersion AS cv " +
            "JOIN Cp AS cp ON cv.cpSeq = cp.cpSeq " +
            "JOIN CpSearchData AS csd ON cv.cpVersionSeq = csd.cpVersionSeq " +
            "JOIN CpSearchCategoryData AS cscd ON csd.cpSearchCategoryDataSeq = cscd.cpSearchCategoryDataSeq " +
            "JOIN User AS u ON cv.userSeq = u.userSeq " +
            "JOIN Part AS p ON u.part.partSeq = p.partSeq " +
            "WHERE cscd.cpSearchCategorySeq IN (:cpSearchCategorySeqArray) " +
            "AND cscd.cpSearchCategoryDataSeq IN (:cpSearchCategoryDataArray)")
    List<Map<String, Object>> findByCategorySeqAndCategoryData(
            @Param("cpSearchCategorySeqArray") List<Long> cpSearchCategorySeqArray,
            @Param("cpSearchCategoryDataArray") List<Long> cpSearchCategoryDataArray);

    @Query("SELECT new map(" +
            "cp.cpName AS cpName, " +
            "cp.cpDescription AS cpDescription, " +
            "cp.cpViewCount AS cpViewCount, " +
            "cv.cpVersion AS cpVersion, " +
            "cv.cpVersionDescription AS cpVersionDescription, " +
            "cv.createdAt AS createdAt, " +
            "u.userName AS userName, " +
            "u.userId AS userId, " +
            "p.partName AS partName)" +
            "FROM CpVersion AS cv " +
            "JOIN Cp AS cp ON cv.cpSeq = cp.cpSeq " +
            "JOIN CpSearchData AS csd ON cv.cpVersionSeq = csd.cpVersionSeq " +
            "JOIN CpSearchCategoryData AS cscd ON csd.cpSearchCategoryDataSeq = cscd.cpSearchCategoryDataSeq " +
            "JOIN User AS u ON cv.userSeq = u.userSeq " +
            "JOIN Part AS p ON u.part.partSeq = p.partSeq " +
            "WHERE cp.cpName LIKE CONCAT('%', :cpName, '%')")
    List<Map<String, Object>> findByCpNameContainingIgnoreCase(@Param("cpName") String cpName);

    @Query("SELECT new mediHub_be.cp.dto.ResponseCpDTO(" +
            "cp.cpName, " +
            "cp.cpDescription, " +
            "cp.cpViewCount, " +
            "cv.cpVersion, " +
            "cv.cpVersionDescription, " +
            "cv.createdAt, " +
            "u.userName, " +
            "u.userId, " +
            "p.partName)" +
            "FROM CpVersion AS cv " +
            "JOIN Cp AS cp ON cv.cpSeq = cp.cpSeq " +
            "JOIN CpSearchData AS csd ON cv.cpVersionSeq = csd.cpVersionSeq " +
            "JOIN CpSearchCategoryData AS cscd ON csd.cpSearchCategoryDataSeq = cscd.cpSearchCategoryDataSeq " +
            "JOIN User AS u ON cv.userSeq = u.userSeq " +
            "JOIN Part AS p ON u.part.partSeq = p.partSeq " +
            "WHERE cv.cpVersion = :cpVersionSeq")
    ResponseCpDTO findByCpVersionSeq(@Param("cpVersionSeq") long cpVersionSeq);
}

