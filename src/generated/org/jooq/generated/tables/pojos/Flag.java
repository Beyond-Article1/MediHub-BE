/*
 * This file is generated by jOOQ.
 */
package org.jooq.generated.tables.pojos;


import java.io.Serializable;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Flag implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long flagSeq;
    private Long flagEntitySeq;
    private String flagType;

    public Flag() {}

    public Flag(Flag value) {
        this.flagSeq = value.flagSeq;
        this.flagEntitySeq = value.flagEntitySeq;
        this.flagType = value.flagType;
    }

    public Flag(
        Long flagSeq,
        Long flagEntitySeq,
        String flagType
    ) {
        this.flagSeq = flagSeq;
        this.flagEntitySeq = flagEntitySeq;
        this.flagType = flagType;
    }

    /**
     * Getter for <code>medihub.flag.flag_seq</code>.
     */
    public Long getFlagSeq() {
        return this.flagSeq;
    }

    /**
     * Setter for <code>medihub.flag.flag_seq</code>.
     */
    public Flag setFlagSeq(Long flagSeq) {
        this.flagSeq = flagSeq;
        return this;
    }

    /**
     * Getter for <code>medihub.flag.flag_entity_seq</code>.
     */
    public Long getFlagEntitySeq() {
        return this.flagEntitySeq;
    }

    /**
     * Setter for <code>medihub.flag.flag_entity_seq</code>.
     */
    public Flag setFlagEntitySeq(Long flagEntitySeq) {
        this.flagEntitySeq = flagEntitySeq;
        return this;
    }

    /**
     * Getter for <code>medihub.flag.flag_type</code>.
     */
    public String getFlagType() {
        return this.flagType;
    }

    /**
     * Setter for <code>medihub.flag.flag_type</code>.
     */
    public Flag setFlagType(String flagType) {
        this.flagType = flagType;
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
        final Flag other = (Flag) obj;
        if (this.flagSeq == null) {
            if (other.flagSeq != null)
                return false;
        }
        else if (!this.flagSeq.equals(other.flagSeq))
            return false;
        if (this.flagEntitySeq == null) {
            if (other.flagEntitySeq != null)
                return false;
        }
        else if (!this.flagEntitySeq.equals(other.flagEntitySeq))
            return false;
        if (this.flagType == null) {
            if (other.flagType != null)
                return false;
        }
        else if (!this.flagType.equals(other.flagType))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.flagSeq == null) ? 0 : this.flagSeq.hashCode());
        result = prime * result + ((this.flagEntitySeq == null) ? 0 : this.flagEntitySeq.hashCode());
        result = prime * result + ((this.flagType == null) ? 0 : this.flagType.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Flag (");

        sb.append(flagSeq);
        sb.append(", ").append(flagEntitySeq);
        sb.append(", ").append(flagType);

        sb.append(")");
        return sb.toString();
    }
}
