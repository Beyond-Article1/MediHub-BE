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
public class CpOpinion implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long cpOpinionSeq;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
    private LocalDateTime updatedAt;
    private String cpOpinionContent;
    private Long cpOpinionLocationSeq;
    private Long cpOpinionViewCount;
    private Long userSeq;

    public CpOpinion() {}

    public CpOpinion(CpOpinion value) {
        this.cpOpinionSeq = value.cpOpinionSeq;
        this.createdAt = value.createdAt;
        this.deletedAt = value.deletedAt;
        this.updatedAt = value.updatedAt;
        this.cpOpinionContent = value.cpOpinionContent;
        this.cpOpinionLocationSeq = value.cpOpinionLocationSeq;
        this.cpOpinionViewCount = value.cpOpinionViewCount;
        this.userSeq = value.userSeq;
    }

    public CpOpinion(
        Long cpOpinionSeq,
        LocalDateTime createdAt,
        LocalDateTime deletedAt,
        LocalDateTime updatedAt,
        String cpOpinionContent,
        Long cpOpinionLocationSeq,
        Long cpOpinionViewCount,
        Long userSeq
    ) {
        this.cpOpinionSeq = cpOpinionSeq;
        this.createdAt = createdAt;
        this.deletedAt = deletedAt;
        this.updatedAt = updatedAt;
        this.cpOpinionContent = cpOpinionContent;
        this.cpOpinionLocationSeq = cpOpinionLocationSeq;
        this.cpOpinionViewCount = cpOpinionViewCount;
        this.userSeq = userSeq;
    }

    /**
     * Getter for <code>medihub.cp_opinion.cp_opinion_seq</code>.
     */
    public Long getCpOpinionSeq() {
        return this.cpOpinionSeq;
    }

    /**
     * Setter for <code>medihub.cp_opinion.cp_opinion_seq</code>.
     */
    public CpOpinion setCpOpinionSeq(Long cpOpinionSeq) {
        this.cpOpinionSeq = cpOpinionSeq;
        return this;
    }

    /**
     * Getter for <code>medihub.cp_opinion.created_at</code>.
     */
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    /**
     * Setter for <code>medihub.cp_opinion.created_at</code>.
     */
    public CpOpinion setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    /**
     * Getter for <code>medihub.cp_opinion.deleted_at</code>.
     */
    public LocalDateTime getDeletedAt() {
        return this.deletedAt;
    }

    /**
     * Setter for <code>medihub.cp_opinion.deleted_at</code>.
     */
    public CpOpinion setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    /**
     * Getter for <code>medihub.cp_opinion.updated_at</code>.
     */
    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    /**
     * Setter for <code>medihub.cp_opinion.updated_at</code>.
     */
    public CpOpinion setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    /**
     * Getter for <code>medihub.cp_opinion.cp_opinion_content</code>.
     */
    public String getCpOpinionContent() {
        return this.cpOpinionContent;
    }

    /**
     * Setter for <code>medihub.cp_opinion.cp_opinion_content</code>.
     */
    public CpOpinion setCpOpinionContent(String cpOpinionContent) {
        this.cpOpinionContent = cpOpinionContent;
        return this;
    }

    /**
     * Getter for <code>medihub.cp_opinion.cp_opinion_location_seq</code>.
     */
    public Long getCpOpinionLocationSeq() {
        return this.cpOpinionLocationSeq;
    }

    /**
     * Setter for <code>medihub.cp_opinion.cp_opinion_location_seq</code>.
     */
    public CpOpinion setCpOpinionLocationSeq(Long cpOpinionLocationSeq) {
        this.cpOpinionLocationSeq = cpOpinionLocationSeq;
        return this;
    }

    /**
     * Getter for <code>medihub.cp_opinion.cp_opinion_view_count</code>.
     */
    public Long getCpOpinionViewCount() {
        return this.cpOpinionViewCount;
    }

    /**
     * Setter for <code>medihub.cp_opinion.cp_opinion_view_count</code>.
     */
    public CpOpinion setCpOpinionViewCount(Long cpOpinionViewCount) {
        this.cpOpinionViewCount = cpOpinionViewCount;
        return this;
    }

    /**
     * Getter for <code>medihub.cp_opinion.user_seq</code>.
     */
    public Long getUserSeq() {
        return this.userSeq;
    }

    /**
     * Setter for <code>medihub.cp_opinion.user_seq</code>.
     */
    public CpOpinion setUserSeq(Long userSeq) {
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
        final CpOpinion other = (CpOpinion) obj;
        if (this.cpOpinionSeq == null) {
            if (other.cpOpinionSeq != null)
                return false;
        }
        else if (!this.cpOpinionSeq.equals(other.cpOpinionSeq))
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
        if (this.cpOpinionContent == null) {
            if (other.cpOpinionContent != null)
                return false;
        }
        else if (!this.cpOpinionContent.equals(other.cpOpinionContent))
            return false;
        if (this.cpOpinionLocationSeq == null) {
            if (other.cpOpinionLocationSeq != null)
                return false;
        }
        else if (!this.cpOpinionLocationSeq.equals(other.cpOpinionLocationSeq))
            return false;
        if (this.cpOpinionViewCount == null) {
            if (other.cpOpinionViewCount != null)
                return false;
        }
        else if (!this.cpOpinionViewCount.equals(other.cpOpinionViewCount))
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
        result = prime * result + ((this.cpOpinionSeq == null) ? 0 : this.cpOpinionSeq.hashCode());
        result = prime * result + ((this.createdAt == null) ? 0 : this.createdAt.hashCode());
        result = prime * result + ((this.deletedAt == null) ? 0 : this.deletedAt.hashCode());
        result = prime * result + ((this.updatedAt == null) ? 0 : this.updatedAt.hashCode());
        result = prime * result + ((this.cpOpinionContent == null) ? 0 : this.cpOpinionContent.hashCode());
        result = prime * result + ((this.cpOpinionLocationSeq == null) ? 0 : this.cpOpinionLocationSeq.hashCode());
        result = prime * result + ((this.cpOpinionViewCount == null) ? 0 : this.cpOpinionViewCount.hashCode());
        result = prime * result + ((this.userSeq == null) ? 0 : this.userSeq.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("CpOpinion (");

        sb.append(cpOpinionSeq);
        sb.append(", ").append(createdAt);
        sb.append(", ").append(deletedAt);
        sb.append(", ").append(updatedAt);
        sb.append(", ").append(cpOpinionContent);
        sb.append(", ").append(cpOpinionLocationSeq);
        sb.append(", ").append(cpOpinionViewCount);
        sb.append(", ").append(userSeq);

        sb.append(")");
        return sb.toString();
    }
}
