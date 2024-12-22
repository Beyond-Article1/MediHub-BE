package mediHub_be.case_sharing.entity;

import jakarta.persistence.*;
import lombok.*;
import mediHub_be.common.aggregate.entity.BaseFullEntity;
import mediHub_be.part.entity.Part;
import mediHub_be.user.entity.User;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "template")
@ToString
public class Template extends BaseFullEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long templateSeq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "part_seq", nullable = false)
    private Part part;

    private String templateTitle;

    @Column(columnDefinition = "LONGTEXT")
    private String templateContent;

    @Enumerated(EnumType.STRING)
    @Column(name="template_open_scope")
    private OpenScope openScope;

    @Builder
    public Template(User user, Part part, String templateTitle, String templateContent, OpenScope openScope) {
        this.user = user;
        this.part = part;
        this.templateTitle = templateTitle;
        this.templateContent = templateContent;
        this.openScope = openScope;
    }
    public void updateTemplate(String title, String content, OpenScope openScope) {
        this.templateTitle = title;
        this.templateContent = content;
        this.openScope = openScope;
    }

    public void markAsDeleted() {
        this.deletedAt = LocalDateTime.now();
    }


}
