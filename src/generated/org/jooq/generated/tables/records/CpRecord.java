/*
 * This file is generated by jOOQ.
 */
package org.jooq.generated.tables.records;


import org.jooq.Record1;
import org.jooq.generated.tables.JCp;
import org.jooq.generated.tables.pojos.Cp;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class CpRecord extends UpdatableRecordImpl<CpRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>medihub.cp.cp_seq</code>.
     */
    public CpRecord setCpSeq(Long value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>medihub.cp.cp_seq</code>.
     */
    public Long getCpSeq() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>medihub.cp.cp_description</code>.
     */
    public CpRecord setCpDescription(String value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>medihub.cp.cp_description</code>.
     */
    public String getCpDescription() {
        return (String) get(1);
    }

    /**
     * Setter for <code>medihub.cp.cp_name</code>.
     */
    public CpRecord setCpName(String value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>medihub.cp.cp_name</code>.
     */
    public String getCpName() {
        return (String) get(2);
    }

    /**
     * Setter for <code>medihub.cp.cp_view_count</code>.
     */
    public CpRecord setCpViewCount(Long value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>medihub.cp.cp_view_count</code>.
     */
    public Long getCpViewCount() {
        return (Long) get(3);
    }

    /**
     * Setter for <code>medihub.cp.user_seq</code>.
     */
    public CpRecord setUserSeq(Long value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>medihub.cp.user_seq</code>.
     */
    public Long getUserSeq() {
        return (Long) get(4);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached CpRecord
     */
    public CpRecord() {
        super(JCp.CP);
    }

    /**
     * Create a detached, initialised CpRecord
     */
    public CpRecord(Long cpSeq, String cpDescription, String cpName, Long cpViewCount, Long userSeq) {
        super(JCp.CP);

        setCpSeq(cpSeq);
        setCpDescription(cpDescription);
        setCpName(cpName);
        setCpViewCount(cpViewCount);
        setUserSeq(userSeq);
        resetChangedOnNotNull();
    }

    /**
     * Create a detached, initialised CpRecord
     */
    public CpRecord(Cp value) {
        super(JCp.CP);

        if (value != null) {
            setCpSeq(value.getCpSeq());
            setCpDescription(value.getCpDescription());
            setCpName(value.getCpName());
            setCpViewCount(value.getCpViewCount());
            setUserSeq(value.getUserSeq());
            resetChangedOnNotNull();
        }
    }
}
