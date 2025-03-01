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
public class CpSearchCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long cpSearchCategorySeq;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
    private LocalDateTime updatedAt;
    private String cpSearchCategoryName;
    private Long userSeq;

    public CpSearchCategory() {}

    public CpSearchCategory(CpSearchCategory value) {
        this.cpSearchCategorySeq = value.cpSearchCategorySeq;
        this.createdAt = value.createdAt;
        this.deletedAt = value.deletedAt;
        this.updatedAt = value.updatedAt;
        this.cpSearchCategoryName = value.cpSearchCategoryName;
        this.userSeq = value.userSeq;
    }

    public CpSearchCategory(
        Long cpSearchCategorySeq,
        LocalDateTime createdAt,
        LocalDateTime deletedAt,
        LocalDateTime updatedAt,
        String cpSearchCategoryName,
        Long userSeq
    ) {
        this.cpSearchCategorySeq = cpSearchCategorySeq;
        this.createdAt = createdAt;
        this.deletedAt = deletedAt;
        this.updatedAt = updatedAt;
        this.cpSearchCategoryName = cpSearchCategoryName;
        this.userSeq = userSeq;
    }

    /**
     * Getter for
     * <code>medihub.cp_search_category.cp_search_category_seq</code>.
     */
    public Long getCpSearchCategorySeq() {
        return this.cpSearchCategorySeq;
    }

    /**
     * Setter for
     * <code>medihub.cp_search_category.cp_search_category_seq</code>.
     */
    public CpSearchCategory setCpSearchCategorySeq(Long cpSearchCategorySeq) {
        this.cpSearchCategorySeq = cpSearchCategorySeq;
        return this;
    }

    /**
     * Getter for <code>medihub.cp_search_category.created_at</code>.
     */
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    /**
     * Setter for <code>medihub.cp_search_category.created_at</code>.
     */
    public CpSearchCategory setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    /**
     * Getter for <code>medihub.cp_search_category.deleted_at</code>.
     */
    public LocalDateTime getDeletedAt() {
        return this.deletedAt;
    }

    /**
     * Setter for <code>medihub.cp_search_category.deleted_at</code>.
     */
    public CpSearchCategory setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    /**
     * Getter for <code>medihub.cp_search_category.updated_at</code>.
     */
    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    /**
     * Setter for <code>medihub.cp_search_category.updated_at</code>.
     */
    public CpSearchCategory setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    /**
     * Getter for
     * <code>medihub.cp_search_category.cp_search_category_name</code>.
     */
    public String getCpSearchCategoryName() {
        return this.cpSearchCategoryName;
    }

    /**
     * Setter for
     * <code>medihub.cp_search_category.cp_search_category_name</code>.
     */
    public CpSearchCategory setCpSearchCategoryName(String cpSearchCategoryName) {
        this.cpSearchCategoryName = cpSearchCategoryName;
        return this;
    }

    /**
     * Getter for <code>medihub.cp_search_category.user_seq</code>.
     */
    public Long getUserSeq() {
        return this.userSeq;
    }

    /**
     * Setter for <code>medihub.cp_search_category.user_seq</code>.
     */
    public CpSearchCategory setUserSeq(Long userSeq) {
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
        final CpSearchCategory other = (CpSearchCategory) obj;
        if (this.cpSearchCategorySeq == null) {
            if (other.cpSearchCategorySeq != null)
                return false;
        }
        else if (!this.cpSearchCategorySeq.equals(other.cpSearchCategorySeq))
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
        if (this.cpSearchCategoryName == null) {
            if (other.cpSearchCategoryName != null)
                return false;
        }
        else if (!this.cpSearchCategoryName.equals(other.cpSearchCategoryName))
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
        result = prime * result + ((this.cpSearchCategorySeq == null) ? 0 : this.cpSearchCategorySeq.hashCode());
        result = prime * result + ((this.createdAt == null) ? 0 : this.createdAt.hashCode());
        result = prime * result + ((this.deletedAt == null) ? 0 : this.deletedAt.hashCode());
        result = prime * result + ((this.updatedAt == null) ? 0 : this.updatedAt.hashCode());
        result = prime * result + ((this.cpSearchCategoryName == null) ? 0 : this.cpSearchCategoryName.hashCode());
        result = prime * result + ((this.userSeq == null) ? 0 : this.userSeq.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("CpSearchCategory (");

        sb.append(cpSearchCategorySeq);
        sb.append(", ").append(createdAt);
        sb.append(", ").append(deletedAt);
        sb.append(", ").append(updatedAt);
        sb.append(", ").append(cpSearchCategoryName);
        sb.append(", ").append(userSeq);

        sb.append(")");
        return sb.toString();
    }
}
