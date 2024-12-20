package mediHub_be.cp.repository;

import mediHub_be.cp.dto.ResponseCpVersionDTO;
import mediHub_be.cp.entity.Cp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CpRepository extends JpaRepository<Cp, Long> {
    @Query("SELECT new mediHub_be.cp.entity.Cp(" +
            "c.cpSeq, " +
            "c.userSeq, " +
            "c.cpName, " +
            "c.cpDescription, " +
            "c.cpViewCount) " +
            "FROM Cp AS c " +
            "JOIN CpVersion AS cv ON cv.cpSeq = c.cpSeq " +
            "WHERE cv.cpVersionSeq = :cpVersionSeq")
    Optional<Cp> findByCpVersionSeq(@Param("cpVersionSeq") long cpVersionSeq);

    @Query("SELECT new mediHub_be.cp.dto.ResponseCpVersionDTO( " +
            "cv.cpVersionSeq, " +
            "cv.cpVersion) " +
            "FROM CpVersion AS cv " +
            "WHERE cv.cpSeq = (SELECT cv2.cpSeq " +
            "                   FROM CpVersion AS cv2 " +
            "                   WHERE cv2.cpVersionSeq = :cpVersionSeq)")
    List<ResponseCpVersionDTO> findJoinCpVersionOnCpVersionSeq(@Param("cpVersionSeq") long cpVersionSeq);

}
