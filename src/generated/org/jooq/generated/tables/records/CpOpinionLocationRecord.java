/*
 * This file is generated by jOOQ.
 */
package org.jooq.generated.tables.records;


import java.time.LocalDateTime;

import org.jooq.Record1;
import org.jooq.generated.tables.JCpOpinionLocation;
import org.jooq.generated.tables.pojos.CpOpinionLocation;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class CpOpinionLocationRecord extends UpdatableRecordImpl<CpOpinionLocationRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for
     * <code>medihub.cp_opinion_location.cp_opinion_location_seq</code>.
     */
    public CpOpinionLocationRecord setCpOpinionLocationSeq(Long value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for
     * <code>medihub.cp_opinion_location.cp_opinion_location_seq</code>.
     */
    public Long getCpOpinionLocationSeq() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>medihub.cp_opinion_location.created_at</code>.
     */
    public CpOpinionLocationRecord setCreatedAt(LocalDateTime value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>medihub.cp_opinion_location.created_at</code>.
     */
    public LocalDateTime getCreatedAt() {
        return (LocalDateTime) get(1);
    }

    /**
     * Setter for <code>medihub.cp_opinion_location.deleted_at</code>.
     */
    public CpOpinionLocationRecord setDeletedAt(LocalDateTime value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>medihub.cp_opinion_location.deleted_at</code>.
     */
    public LocalDateTime getDeletedAt() {
        return (LocalDateTime) get(2);
    }

    /**
     * Setter for
     * <code>medihub.cp_opinion_location.cp_opinion_location_page_num</code>.
     */
    public CpOpinionLocationRecord setCpOpinionLocationPageNum(Long value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for
     * <code>medihub.cp_opinion_location.cp_opinion_location_page_num</code>.
     */
    public Long getCpOpinionLocationPageNum() {
        return (Long) get(3);
    }

    /**
     * Setter for
     * <code>medihub.cp_opinion_location.cp_opinion_location_x</code>.
     */
    public CpOpinionLocationRecord setCpOpinionLocationX(Double value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for
     * <code>medihub.cp_opinion_location.cp_opinion_location_x</code>.
     */
    public Double getCpOpinionLocationX() {
        return (Double) get(4);
    }

    /**
     * Setter for
     * <code>medihub.cp_opinion_location.cp_opinion_location_y</code>.
     */
    public CpOpinionLocationRecord setCpOpinionLocationY(Double value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for
     * <code>medihub.cp_opinion_location.cp_opinion_location_y</code>.
     */
    public Double getCpOpinionLocationY() {
        return (Double) get(5);
    }

    /**
     * Setter for <code>medihub.cp_opinion_location.cp_version_seq</code>.
     */
    public CpOpinionLocationRecord setCpVersionSeq(Long value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for <code>medihub.cp_opinion_location.cp_version_seq</code>.
     */
    public Long getCpVersionSeq() {
        return (Long) get(6);
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
     * Create a detached CpOpinionLocationRecord
     */
    public CpOpinionLocationRecord() {
        super(JCpOpinionLocation.CP_OPINION_LOCATION);
    }

    /**
     * Create a detached, initialised CpOpinionLocationRecord
     */
    public CpOpinionLocationRecord(Long cpOpinionLocationSeq, LocalDateTime createdAt, LocalDateTime deletedAt, Long cpOpinionLocationPageNum, Double cpOpinionLocationX, Double cpOpinionLocationY, Long cpVersionSeq) {
        super(JCpOpinionLocation.CP_OPINION_LOCATION);

        setCpOpinionLocationSeq(cpOpinionLocationSeq);
        setCreatedAt(createdAt);
        setDeletedAt(deletedAt);
        setCpOpinionLocationPageNum(cpOpinionLocationPageNum);
        setCpOpinionLocationX(cpOpinionLocationX);
        setCpOpinionLocationY(cpOpinionLocationY);
        setCpVersionSeq(cpVersionSeq);
        resetChangedOnNotNull();
    }

    /**
     * Create a detached, initialised CpOpinionLocationRecord
     */
    public CpOpinionLocationRecord(CpOpinionLocation value) {
        super(JCpOpinionLocation.CP_OPINION_LOCATION);

        if (value != null) {
            setCpOpinionLocationSeq(value.getCpOpinionLocationSeq());
            setCreatedAt(value.getCreatedAt());
            setDeletedAt(value.getDeletedAt());
            setCpOpinionLocationPageNum(value.getCpOpinionLocationPageNum());
            setCpOpinionLocationX(value.getCpOpinionLocationX());
            setCpOpinionLocationY(value.getCpOpinionLocationY());
            setCpVersionSeq(value.getCpVersionSeq());
            resetChangedOnNotNull();
        }
    }
}