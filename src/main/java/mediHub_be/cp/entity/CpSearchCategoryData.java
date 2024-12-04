package mediHub_be.cp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import mediHub_be.user.entity.User;

@Entity
@Table(name = "cp_search_category_data")
@Getter
public class CpSearchCategoryData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cpSearchCategoryDataSeq;           // cp 검색카테고리 데이터 번호

    @Column
    private long userSeq;                           // 등록자

    @Column
    private long cpSearchCategorySeq;               // cp 검색카테고리 번호

    @Column
    private String cpSearchCategoryDataName;        // cp 검색카테고리 데이터명
}