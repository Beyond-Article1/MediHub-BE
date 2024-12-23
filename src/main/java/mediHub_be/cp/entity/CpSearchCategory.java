package mediHub_be.cp.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mediHub_be.common.aggregate.entity.BaseFullEntity;

@Entity
@Table(name = "cp_search_category")
@Getter
@NoArgsConstructor
public class CpSearchCategory extends BaseFullEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cpSearchCategorySeq;           // cp 검색카테고리 번호

    @Column
    private long userSeq;                       // 등록자

    @Column
    private String cpSearchCategoryName;        // cp 검색카테고리명

    @Builder
    public CpSearchCategory(long userSeq, String cpSearchCategoryName) {
        this.userSeq = userSeq;
        this.cpSearchCategoryName = cpSearchCategoryName;
    }

    public void updateUserSeq(long userSeq) {
        this.userSeq = userSeq;
    }

    public void updateCpSearchCategoryName(String cpSearchCategoryName) {
        this.cpSearchCategoryName = cpSearchCategoryName;
    }
}
