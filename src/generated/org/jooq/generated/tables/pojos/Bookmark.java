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
public class Bookmark implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long bookmarkSeq;
    private LocalDateTime createdAt;
    private Long flagSeq;
    private Long userSeq;

    public Bookmark() {}

    public Bookmark(Bookmark value) {
        this.bookmarkSeq = value.bookmarkSeq;
        this.createdAt = value.createdAt;
        this.flagSeq = value.flagSeq;
        this.userSeq = value.userSeq;
    }

    public Bookmark(
        Long bookmarkSeq,
        LocalDateTime createdAt,
        Long flagSeq,
        Long userSeq
    ) {
        this.bookmarkSeq = bookmarkSeq;
        this.createdAt = createdAt;
        this.flagSeq = flagSeq;
        this.userSeq = userSeq;
    }

    /**
     * Getter for <code>medihub.bookmark.bookmark_seq</code>.
     */
    public Long getBookmarkSeq() {
        return this.bookmarkSeq;
    }

    /**
     * Setter for <code>medihub.bookmark.bookmark_seq</code>.
     */
    public Bookmark setBookmarkSeq(Long bookmarkSeq) {
        this.bookmarkSeq = bookmarkSeq;
        return this;
    }

    /**
     * Getter for <code>medihub.bookmark.created_at</code>.
     */
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    /**
     * Setter for <code>medihub.bookmark.created_at</code>.
     */
    public Bookmark setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    /**
     * Getter for <code>medihub.bookmark.flag_seq</code>.
     */
    public Long getFlagSeq() {
        return this.flagSeq;
    }

    /**
     * Setter for <code>medihub.bookmark.flag_seq</code>.
     */
    public Bookmark setFlagSeq(Long flagSeq) {
        this.flagSeq = flagSeq;
        return this;
    }

    /**
     * Getter for <code>medihub.bookmark.user_seq</code>.
     */
    public Long getUserSeq() {
        return this.userSeq;
    }

    /**
     * Setter for <code>medihub.bookmark.user_seq</code>.
     */
    public Bookmark setUserSeq(Long userSeq) {
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
        final Bookmark other = (Bookmark) obj;
        if (this.bookmarkSeq == null) {
            if (other.bookmarkSeq != null)
                return false;
        }
        else if (!this.bookmarkSeq.equals(other.bookmarkSeq))
            return false;
        if (this.createdAt == null) {
            if (other.createdAt != null)
                return false;
        }
        else if (!this.createdAt.equals(other.createdAt))
            return false;
        if (this.flagSeq == null) {
            if (other.flagSeq != null)
                return false;
        }
        else if (!this.flagSeq.equals(other.flagSeq))
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
        result = prime * result + ((this.bookmarkSeq == null) ? 0 : this.bookmarkSeq.hashCode());
        result = prime * result + ((this.createdAt == null) ? 0 : this.createdAt.hashCode());
        result = prime * result + ((this.flagSeq == null) ? 0 : this.flagSeq.hashCode());
        result = prime * result + ((this.userSeq == null) ? 0 : this.userSeq.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Bookmark (");

        sb.append(bookmarkSeq);
        sb.append(", ").append(createdAt);
        sb.append(", ").append(flagSeq);
        sb.append(", ").append(userSeq);

        sb.append(")");
        return sb.toString();
    }
}