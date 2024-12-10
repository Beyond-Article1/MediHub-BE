package mediHub_be.journal.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mediHub_be.common.aggregate.entity.UpdateTimeEntity;
import mediHub_be.user.entity.User;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JournalSearch extends UpdateTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "journal_search_seq", nullable = false)
    private Long journalSearchSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "journal_seq")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Journal journal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    public JournalSearch(Journal journal, User user) {
        this.journal = journal;
        this.user = user;
    }
}
