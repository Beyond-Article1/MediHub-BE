package mediHub_be.board.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
    private Long preferSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flag_seq", nullable = false)
    private Flag flag;
}