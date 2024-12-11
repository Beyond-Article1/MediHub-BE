package mediHub_be.case_sharing.repository;

import mediHub_be.case_sharing.entity.CaseSharingGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CaseSharingGroupRepository extends JpaRepository<CaseSharingGroup, Long> {

}
