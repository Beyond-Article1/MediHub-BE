/*
 * This file is generated by jOOQ.
 */
package org.jooq.generated.tables.records;


import java.time.LocalDateTime;

import org.jooq.Record1;
import org.jooq.generated.tables.JCpOpinion;
import org.jooq.generated.tables.pojos.CpOpinion;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class CpOpinionRecord extends UpdatableRecordImpl<CpOpinionRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>medihub.cp_opinion.cp_opinion_seq</code>.
     */
    public CpOpinionRecord setCpOpinionSeq(Long value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>medihub.cp_opinion.cp_opinion_seq</code>.
     */
    public Long getCpOpinionSeq() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>medihub.cp_opinion.created_at</code>.
     */
    public CpOpinionRecord setCreatedAt(LocalDateTime value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>medihub.cp_opinion.created_at</code>.
     */
    public LocalDateTime getCreatedAt() {
        return (LocalDateTime) get(1);
    }

    /**
     * Setter for <code>medihub.cp_opinion.deleted_at</code>.
     */
    public CpOpinionRecord setDeletedAt(LocalDateTime value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>medihub.cp_opinion.deleted_at</code>.
     */
    public LocalDateTime getDeletedAt() {
        return (LocalDateTime) get(2);
    }

    /**
     * Setter for <code>medihub.cp_opinion.updated_at</code>.
     */
    public CpOpinionRecord setUpdatedAt(LocalDateTime value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>medihub.cp_opinion.updated_at</code>.
     */
    public LocalDateTime getUpdatedAt() {
        return (LocalDateTime) get(3);
    }

    /**
     * Setter for <code>medihub.cp_opinion.cp_opinion_content</code>.
     */
    public CpOpinionRecord setCpOpinionContent(String value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>medihub.cp_opinion.cp_opinion_content</code>.
     */
    public String getCpOpinionContent() {
        return (String) get(4);
    }

    /**
     * Setter for <code>medihub.cp_opinion.cp_opinion_location_seq</code>.
     */
    public CpOpinionRecord setCpOpinionLocationSeq(Long value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>medihub.cp_opinion.cp_opinion_location_seq</code>.
     */
    public Long getCpOpinionLocationSeq() {
        return (Long) get(5);
    }

    /**
     * Setter for <code>medihub.cp_opinion.cp_opinion_view_count</code>.
     */
    public CpOpinionRecord setCpOpinionViewCount(Long value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for <code>medihub.cp_opinion.cp_opinion_view_count</code>.
     */
    public Long getCpOpinionViewCount() {
        return (Long) get(6);
    }

    /**
     * Setter for <code>medihub.cp_opinion.user_seq</code>.
     */
    public CpOpinionRecord setUserSeq(Long value) {
        set(7, value);
        return this;
    }

    /**
     * Getter for <code>medihub.cp_opinion.user_seq</code>.
     */
    public Long getUserSeq() {
        return (Long) get(7);
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
     * Create a detached CpOpinionRecord
     */
    public CpOpinionRecord() {
        super(JCpOpinion.CP_OPINION);
    }

    /**
     * Create a detached, initialised CpOpinionRecord
     */
    public CpOpinionRecord(Long cpOpinionSeq, LocalDateTime createdAt, LocalDateTime deletedAt, LocalDateTime updatedAt, String cpOpinionContent, Long cpOpinionLocationSeq, Long cpOpinionViewCount, Long userSeq) {
        super(JCpOpinion.CP_OPINION);

        setCpOpinionSeq(cpOpinionSeq);
        setCreatedAt(createdAt);
        setDeletedAt(deletedAt);
        setUpdatedAt(updatedAt);
        setCpOpinionContent(cpOpinionContent);
        setCpOpinionLocationSeq(cpOpinionLocationSeq);
        setCpOpinionViewCount(cpOpinionViewCount);
        setUserSeq(userSeq);
        resetChangedOnNotNull();
    }

    /**
     * Create a detached, initialised CpOpinionRecord
     */
    public CpOpinionRecord(CpOpinion value) {
        super(JCpOpinion.CP_OPINION);

        if (value != null) {
            setCpOpinionSeq(value.getCpOpinionSeq());
            setCreatedAt(value.getCreatedAt());
            setDeletedAt(value.getDeletedAt());
            setUpdatedAt(value.getUpdatedAt());
            setCpOpinionContent(value.getCpOpinionContent());
            setCpOpinionLocationSeq(value.getCpOpinionLocationSeq());
            setCpOpinionViewCount(value.getCpOpinionViewCount());
            setUserSeq(value.getUserSeq());
            resetChangedOnNotNull();
        }
    }
}
