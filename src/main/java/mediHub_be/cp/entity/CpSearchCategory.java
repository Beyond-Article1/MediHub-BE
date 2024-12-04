package mediHub_be.cp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import mediHub_be.common.aggregate.entity.BaseFullEntity;
import mediHub_be.user.entity.User;

@Entity
@Table(name = "cp_search_category")
@Getter
public class CpSearchCategory extends BaseFullEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cpSearchCategorySeq;           // cp 검색카테고리 번호

    @Column
    private long userSeq;                       // 등록자

    @Column
    private String cpSearchCategoryName;        // cp 검색카테고리명
}