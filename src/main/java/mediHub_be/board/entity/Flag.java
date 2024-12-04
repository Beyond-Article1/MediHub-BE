package mediHub_be.board.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "flag")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Flag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long flagSeq;
    private String flagBoardFlag; // 게시판 구별용
    private Long flagPostSeq; // 해당 게시판의 게시물 seq
}
