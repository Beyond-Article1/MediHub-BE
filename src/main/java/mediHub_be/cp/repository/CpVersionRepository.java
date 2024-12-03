package mediHub_be.cp.controller;

import mediHub_be.cp.entity.CpVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CpVersionRepository extends JpaRepository<CpVersion, Long> {

    @Query("SELECT cv FROM CpVersion cv " +
            "JOIN CpSearchData csd ON cv.cpVersionSeq = csd.cpVersionSeq " +
            "JOIN CpSearchCategoryData cscd ON csd.cpSearchCategoryDataSeq = cscd.cpSearchCategoryDataSeq " +
            "WHERE cscd.cpSearchCategorySeq IN (:categorySeqs) " +
            "AND cscd.cpSearchCategoryDataName IN (:categoryDataNames)")
    List<CpVersion> findByCategorySeqAndCategoryDataNames(
            @Param("categorySeqs") List<Long> categorySeqs,
            @Param("categoryDataNames") List<String> categoryDataNames);
}
