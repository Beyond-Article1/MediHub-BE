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
public class Prefer implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long preferSeq;
    private LocalDateTime createdAt;
    private Long flagSeq;
    private Long userSeq;

    public Prefer() {}

    public Prefer(Prefer value) {
        this.preferSeq = value.preferSeq;
        this.createdAt = value.createdAt;
        this.flagSeq = value.flagSeq;
        this.userSeq = value.userSeq;
    }

    public Prefer(
        Long preferSeq,
        LocalDateTime createdAt,
        Long flagSeq,
        Long userSeq
    ) {
        this.preferSeq = preferSeq;
        this.createdAt = createdAt;
        this.flagSeq = flagSeq;
        this.userSeq = userSeq;
    }

    /**
     * Getter for <code>medihub.prefer.prefer_seq</code>.
     */
    public Long getPreferSeq() {
        return this.preferSeq;
    }

    /**
     * Setter for <code>medihub.prefer.prefer_seq</code>.
     */
    public Prefer setPreferSeq(Long preferSeq) {
        this.preferSeq = preferSeq;
        return this;
    }

    /**
     * Getter for <code>medihub.prefer.created_at</code>.
     */
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    /**
     * Setter for <code>medihub.prefer.created_at</code>.
     */
    public Prefer setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    /**
     * Getter for <code>medihub.prefer.flag_seq</code>.
     */
    public Long getFlagSeq() {
        return this.flagSeq;
    }

    /**
     * Setter for <code>medihub.prefer.flag_seq</code>.
     */
    public Prefer setFlagSeq(Long flagSeq) {
        this.flagSeq = flagSeq;
        return this;
    }

    /**
     * Getter for <code>medihub.prefer.user_seq</code>.
     */
    public Long getUserSeq() {
        return this.userSeq;
    }

    /**
     * Setter for <code>medihub.prefer.user_seq</code>.
     */
    public Prefer setUserSeq(Long userSeq) {
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
        final Prefer other = (Prefer) obj;
        if (this.preferSeq == null) {
            if (other.preferSeq != null)
                return false;
        }
        else if (!this.preferSeq.equals(other.preferSeq))
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
        result = prime * result + ((this.preferSeq == null) ? 0 : this.preferSeq.hashCode());
        result = prime * result + ((this.createdAt == null) ? 0 : this.createdAt.hashCode());
        result = prime * result + ((this.flagSeq == null) ? 0 : this.flagSeq.hashCode());
        result = prime * result + ((this.userSeq == null) ? 0 : this.userSeq.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Prefer (");

        sb.append(preferSeq);
        sb.append(", ").append(createdAt);
        sb.append(", ").append(flagSeq);
        sb.append(", ").append(userSeq);

        sb.append(")");
        return sb.toString();
    }
}
