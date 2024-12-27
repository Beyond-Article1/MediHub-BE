/*
 * This file is generated by jOOQ.
 */
package org.jooq.generated.tables.records;


import java.time.LocalDateTime;

import org.jooq.Record1;
import org.jooq.generated.tables.Part;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class PartRecord extends UpdatableRecordImpl<PartRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>medihub.part.part_seq</code>.
     */
    public PartRecord setPartSeq(Long value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>medihub.part.part_seq</code>.
     */
    public Long getPartSeq() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>medihub.part.created_at</code>.
     */
    public PartRecord setCreatedAt(LocalDateTime value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>medihub.part.created_at</code>.
     */
    public LocalDateTime getCreatedAt() {
        return (LocalDateTime) get(1);
    }

    /**
     * Setter for <code>medihub.part.deleted_at</code>.
     */
    public PartRecord setDeletedAt(LocalDateTime value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>medihub.part.deleted_at</code>.
     */
    public LocalDateTime getDeletedAt() {
        return (LocalDateTime) get(2);
    }

    /**
     * Setter for <code>medihub.part.updated_at</code>.
     */
    public PartRecord setUpdatedAt(LocalDateTime value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>medihub.part.updated_at</code>.
     */
    public LocalDateTime getUpdatedAt() {
        return (LocalDateTime) get(3);
    }

    /**
     * Setter for <code>medihub.part.part_name</code>.
     */
    public PartRecord setPartName(String value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>medihub.part.part_name</code>.
     */
    public String getPartName() {
        return (String) get(4);
    }

    /**
     * Setter for <code>medihub.part.dept_seq</code>.
     */
    public PartRecord setDeptSeq(Long value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>medihub.part.dept_seq</code>.
     */
    public Long getDeptSeq() {
        return (Long) get(5);
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
     * Create a detached PartRecord
     */
    public PartRecord() {
        super(Part.PART);
    }

    /**
     * Create a detached, initialised PartRecord
     */
    public PartRecord(Long partSeq, LocalDateTime createdAt, LocalDateTime deletedAt, LocalDateTime updatedAt, String partName, Long deptSeq) {
        super(Part.PART);

        setPartSeq(partSeq);
        setCreatedAt(createdAt);
        setDeletedAt(deletedAt);
        setUpdatedAt(updatedAt);
        setPartName(partName);
        setDeptSeq(deptSeq);
        resetChangedOnNotNull();
    }

    /**
     * Create a detached, initialised PartRecord
     */
    public PartRecord(org.jooq.generated.tables.pojos.Part value) {
        super(Part.PART);

        if (value != null) {
            setPartSeq(value.getPartSeq());
            setCreatedAt(value.getCreatedAt());
            setDeletedAt(value.getDeletedAt());
            setUpdatedAt(value.getUpdatedAt());
            setPartName(value.getPartName());
            setDeptSeq(value.getDeptSeq());
            resetChangedOnNotNull();
        }
    }
}
