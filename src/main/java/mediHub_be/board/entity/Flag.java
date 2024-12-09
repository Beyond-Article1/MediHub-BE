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

    private String flagType; // flag 구별용

    private Long flagEntitySeq; // 해당 entity 식별자 seq
}
