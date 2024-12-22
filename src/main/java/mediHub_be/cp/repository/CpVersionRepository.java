package mediHub_be.cp.repository;

import mediHub_be.cp.dto.ResponseCpDTO;
import mediHub_be.cp.entity.CpVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CpVersionRepository extends JpaRepository<CpVersion, Long> {

    @Query("SELECT new map(" +
            "cv.cpVersionSeq AS cpVersionSeq," +
            "cp.cpName AS cpName, " +
            "cp.cpDescription AS cpDescription, " +
            "cp.cpViewCount AS cpViewCount, " +
            "cv.cpVersion AS cpVersion, " +
            "cv.cpVersionDescription AS cpVersionDescription," +
            "cv.createdAt AS createdAt, " +
            "cv.cpUrl, " +
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
            "cv.cpVersionSeq AS cpVersionSeq, " +
            "cp.cpName AS cpName, " +
            "cp.cpDescription AS cpDescription, " +
            "cp.cpViewCount AS cpViewCount, " +
            "cv.cpVersion AS cpVersion, " +
            "cv.cpVersionDescription AS cpVersionDescription, " +
            "cv.createdAt AS createdAt, " +
            "cv.cpUrl AS cpUrl, " +
            "u.userName AS userName, " +
            "u.userId AS userId, " +
            "p.partName AS partName) " +
            "FROM CpVersion AS cv " +
            "JOIN Cp AS cp ON cv.cpSeq = cp.cpSeq " +
            "JOIN User AS u ON cv.userSeq = u.userSeq " +
            "JOIN Part AS p ON u.part.partSeq = p.partSeq " +
            "WHERE cv.createdAt = (" +
            "SELECT MAX(cv2.createdAt) " +
            "FROM CpVersion AS cv2 " +
            "WHERE cv2.cpSeq = cv.cpSeq) " +
            "AND cp.cpName LIKE CONCAT('%', :cpName, '%') " +
            "ORDER BY cv.createdAt DESC")
    List<Map<String, Object>> findByCpNameContainingIgnoreCase(@Param("cpName") String cpName);

    @Query("SELECT new mediHub_be.cp.dto.ResponseCpDTO(" +
            "cv.cpVersionSeq ," +
            "cp.cpName, " +
            "cp.cpDescription, " +
            "cp.cpViewCount, " +
            "cv.cpVersion, " +
            "cv.cpVersionDescription, " +
            "cv.createdAt, " +
            "cv.cpUrl, " +
            "u.userName, " +
            "u.userId, " +
            "p.partName)" +
            "FROM CpVersion AS cv " +
            "JOIN Cp AS cp ON cv.cpSeq = cp.cpSeq " +
            "JOIN User AS u ON cv.userSeq = u.userSeq " +
            "JOIN Part AS p ON u.part.partSeq = p.partSeq " +
            "WHERE cv.cpVersionSeq = :cpVersionSeq")
    Optional<ResponseCpDTO> findByCpVersionSeq(@Param("cpVersionSeq") long cpVersionSeq);

    @Query("SELECT new mediHub_be.cp.dto.ResponseCpDTO(" +
            "cv.cpVersionSeq, " +
            "cp.cpName, " +
            "cp.cpDescription, " +
            "cp.cpViewCount, " +
            "cv.cpVersion, " +
            "cv.cpVersionDescription, " +
            "cv.createdAt, " +
            "cv.cpUrl, " +
            "u.userName, " +
            "u.userId, " +
            "p.partName) " +
            "FROM CpVersion cv " +
            "JOIN Cp cp ON cv.cpSeq = cp.cpSeq " +
            "LEFT JOIN User u ON cv.userSeq = u.userSeq " +
            "LEFT JOIN Part p ON u.part.partSeq = p.partSeq " +
            "WHERE cv.createdAt = (" +
            "SELECT MAX(cv2.createdAt) " +
            "FROM CpVersion cv2 " +
            "WHERE cv2.cpSeq = cv.cpSeq) " +
            "ORDER BY cv.createdAt DESC")
    List<ResponseCpDTO> findLatestCp();
}

