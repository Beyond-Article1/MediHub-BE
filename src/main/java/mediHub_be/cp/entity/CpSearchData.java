package mediHub_be.cp.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "cp_search_data")
@Getter
public class CpSearchData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long cpSearchDataSeq;               // cp 검색 데이터 번호

    @Column
    private long cpVersionSeq;                  // cp 버전 번호

    @Column
    private long cpSearchCategoryDataSeq;       // cp 카테고리 데이터 번호
}
