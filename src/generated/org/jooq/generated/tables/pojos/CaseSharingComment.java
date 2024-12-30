/*
 * This file is generated by jOOQ.
 */
package org.jooq.generated.tables.pojos;


import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class CaseSharingComment implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long caseSharingCommentSeq;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
    private LocalDateTime updatedAt;
    private String caseSharingBlockId;
    private String caseSharingCommentContent;
    private Long caseSharingSeq;
    private Long userSeq;

    public CaseSharingComment() {}

    public CaseSharingComment(CaseSharingComment value) {
        this.caseSharingCommentSeq = value.caseSharingCommentSeq;
        this.createdAt = value.createdAt;
        this.deletedAt = value.deletedAt;
        this.updatedAt = value.updatedAt;
        this.caseSharingBlockId = value.caseSharingBlockId;
        this.caseSharingCommentContent = value.caseSharingCommentContent;
        this.caseSharingSeq = value.caseSharingSeq;
        this.userSeq = value.userSeq;
    }

    public CaseSharingComment(
        Long caseSharingCommentSeq,
        LocalDateTime createdAt,
        LocalDateTime deletedAt,
        LocalDateTime updatedAt,
        String caseSharingBlockId,
        String caseSharingCommentContent,
        Long caseSharingSeq,
        Long userSeq
    ) {
        this.caseSharingCommentSeq = caseSharingCommentSeq;
        this.createdAt = createdAt;
        this.deletedAt = deletedAt;
        this.updatedAt = updatedAt;
        this.caseSharingBlockId = caseSharingBlockId;
        this.caseSharingCommentContent = caseSharingCommentContent;
        this.caseSharingSeq = caseSharingSeq;
        this.userSeq = userSeq;
    }

    /**
     * Getter for
     * <code>medihub.case_sharing_comment.case_sharing_comment_seq</code>.
     */
    public Long getCaseSharingCommentSeq() {
        return this.caseSharingCommentSeq;
    }

    /**
     * Setter for
     * <code>medihub.case_sharing_comment.case_sharing_comment_seq</code>.
     */
    public CaseSharingComment setCaseSharingCommentSeq(Long caseSharingCommentSeq) {
        this.caseSharingCommentSeq = caseSharingCommentSeq;
        return this;
    }

    /**
     * Getter for <code>medihub.case_sharing_comment.created_at</code>.
     */
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    /**
     * Setter for <code>medihub.case_sharing_comment.created_at</code>.
     */
    public CaseSharingComment setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    /**
     * Getter for <code>medihub.case_sharing_comment.deleted_at</code>.
     */
    public LocalDateTime getDeletedAt() {
        return this.deletedAt;
    }

    /**
     * Setter for <code>medihub.case_sharing_comment.deleted_at</code>.
     */
    public CaseSharingComment setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    /**
     * Getter for <code>medihub.case_sharing_comment.updated_at</code>.
     */
    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    /**
     * Setter for <code>medihub.case_sharing_comment.updated_at</code>.
     */
    public CaseSharingComment setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    /**
     * Getter for
     * <code>medihub.case_sharing_comment.case_sharing_block_id</code>.
     */
    public String getCaseSharingBlockId() {
        return this.caseSharingBlockId;
    }

    /**
     * Setter for
     * <code>medihub.case_sharing_comment.case_sharing_block_id</code>.
     */
    public CaseSharingComment setCaseSharingBlockId(String caseSharingBlockId) {
        this.caseSharingBlockId = caseSharingBlockId;
        return this;
    }

    /**
     * Getter for
     * <code>medihub.case_sharing_comment.case_sharing_comment_content</code>.
     */
    public String getCaseSharingCommentContent() {
        return this.caseSharingCommentContent;
    }

    /**
     * Setter for
     * <code>medihub.case_sharing_comment.case_sharing_comment_content</code>.
     */
    public CaseSharingComment setCaseSharingCommentContent(String caseSharingCommentContent) {
        this.caseSharingCommentContent = caseSharingCommentContent;
        return this;
    }

    /**
     * Getter for <code>medihub.case_sharing_comment.case_sharing_seq</code>.
     */
    public Long getCaseSharingSeq() {
        return this.caseSharingSeq;
    }

    /**
     * Setter for <code>medihub.case_sharing_comment.case_sharing_seq</code>.
     */
    public CaseSharingComment setCaseSharingSeq(Long caseSharingSeq) {
        this.caseSharingSeq = caseSharingSeq;
        return this;
    }

    /**
     * Getter for <code>medihub.case_sharing_comment.user_seq</code>.
     */
    public Long getUserSeq() {
        return this.userSeq;
    }

    /**
     * Setter for <code>medihub.case_sharing_comment.user_seq</code>.
     */
    public CaseSharingComment setUserSeq(Long userSeq) {
        this.userSeq = userSeq;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final CaseSharingComment other = (CaseSharingComment) obj;
        if (this.caseSharingCommentSeq == null) {
            if (other.caseSharingCommentSeq != null)
                return false;
        }
        else if (!this.caseSharingCommentSeq.equals(other.caseSharingCommentSeq))
            return false;
        if (this.createdAt == null) {
            if (other.createdAt != null)
                return false;
        }
        else if (!this.createdAt.equals(other.createdAt))
            return false;
        if (this.deletedAt == null) {
            if (other.deletedAt != null)
                return false;
        }
        else if (!this.deletedAt.equals(other.deletedAt))
            return false;
        if (this.updatedAt == null) {
            if (other.updatedAt != null)
                return false;
        }
        else if (!this.updatedAt.equals(other.updatedAt))
            return false;
        if (this.caseSharingBlockId == null) {
            if (other.caseSharingBlockId != null)
                return false;
        }
        else if (!this.caseSharingBlockId.equals(other.caseSharingBlockId))
            return false;
        if (this.caseSharingCommentContent == null) {
            if (other.caseSharingCommentContent != null)
                return false;
        }
        else if (!this.caseSharingCommentContent.equals(other.caseSharingCommentContent))
            return false;
        if (this.caseSharingSeq == null) {
            if (other.caseSharingSeq != null)
                return false;
        }
        else if (!this.caseSharingSeq.equals(other.caseSharingSeq))
            return false;
        if (this.userSeq == null) {
            if (other.userSeq != null)
                return false;
        }
        else if (!this.userSeq.equals(other.userSeq))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.caseSharingCommentSeq == null) ? 0 : this.caseSharingCommentSeq.hashCode());
        result = prime * result + ((this.createdAt == null) ? 0 : this.createdAt.hashCode());
        result = prime * result + ((this.deletedAt == null) ? 0 : this.deletedAt.hashCode());
        result = prime * result + ((this.updatedAt == null) ? 0 : this.updatedAt.hashCode());
        result = prime * result + ((this.caseSharingBlockId == null) ? 0 : this.caseSharingBlockId.hashCode());
        result = prime * result + ((this.caseSharingCommentContent == null) ? 0 : this.caseSharingCommentContent.hashCode());
        result = prime * result + ((this.caseSharingSeq == null) ? 0 : this.caseSharingSeq.hashCode());
        result = prime * result + ((this.userSeq == null) ? 0 : this.userSeq.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("CaseSharingComment (");

        sb.append(caseSharingCommentSeq);
        sb.append(", ").append(createdAt);
        sb.append(", ").append(deletedAt);
        sb.append(", ").append(updatedAt);
        sb.append(", ").append(caseSharingBlockId);
        sb.append(", ").append(caseSharingCommentContent);
        sb.append(", ").append(caseSharingSeq);
        sb.append(", ").append(userSeq);

        sb.append(")");
        return sb.toString();
    }
}