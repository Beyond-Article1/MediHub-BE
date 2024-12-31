package mediHub_be.board.entity;

import jakarta.persistence.*;
import lombok.*;
import mediHub_be.common.aggregate.entity.CreateTimeEntity;
import mediHub_be.user.entity.User;

@Entity
@Table(name = "prefer")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prefer extends CreateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long preferSeq; // 좋아요 번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", nullable = false)
    private User user;      // 직원

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flag_seq", nullable = false)
    private Flag flag;      // 식별 번호

    // 생성일은 CreateTimeEntity에서 상속
}