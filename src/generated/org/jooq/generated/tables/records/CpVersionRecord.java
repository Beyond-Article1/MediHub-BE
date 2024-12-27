/*
 * This file is generated by jOOQ.
 */
package org.jooq.generated.tables.records;


import java.time.LocalDateTime;

import org.jooq.Record1;
import org.jooq.generated.tables.CpVersion;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class CpVersionRecord extends UpdatableRecordImpl<CpVersionRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>medihub.cp_version.cp_version_seq</code>.
     */
    public CpVersionRecord setCpVersionSeq(Long value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>medihub.cp_version.cp_version_seq</code>.
     */
    public Long getCpVersionSeq() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>medihub.cp_version.created_at</code>.
     */
    public CpVersionRecord setCreatedAt(LocalDateTime value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>medihub.cp_version.created_at</code>.
     */
    public LocalDateTime getCreatedAt() {
        return (LocalDateTime) get(1);
    }

    /**
     * Setter for <code>medihub.cp_version.cp_seq</code>.
     */
    public CpVersionRecord setCpSeq(Long value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>medihub.cp_version.cp_seq</code>.
     */
    public Long getCpSeq() {
        return (Long) get(2);
    }

    /**
     * Setter for <code>medihub.cp_version.cp_url</code>.
     */
    public CpVersionRecord setCpUrl(String value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>medihub.cp_version.cp_url</code>.
     */
    public String getCpUrl() {
        return (String) get(3);
    }

    /**
     * Setter for <code>medihub.cp_version.cp_version</code>.
     */
    public CpVersionRecord setCpVersion(String value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>medihub.cp_version.cp_version</code>.
     */
    public String getCpVersion() {
        return (String) get(4);
    }

    /**
     * Setter for <code>medihub.cp_version.cp_version_description</code>.
     */
    public CpVersionRecord setCpVersionDescription(String value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>medihub.cp_version.cp_version_description</code>.
     */
    public String getCpVersionDescription() {
        return (String) get(5);
    }

    /**
     * Setter for <code>medihub.cp_version.user_seq</code>.
     */
    public CpVersionRecord setUserSeq(Long value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for <code>medihub.cp_version.user_seq</code>.
     */
    public Long getUserSeq() {
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
     * Create a detached CpVersionRecord
     */
    public CpVersionRecord() {
        super(CpVersion.CP_VERSION);
    }

    /**
     * Create a detached, initialised CpVersionRecord
     */
    public CpVersionRecord(Long cpVersionSeq, LocalDateTime createdAt, Long cpSeq, String cpUrl, String cpVersion, String cpVersionDescription, Long userSeq) {
        super(CpVersion.CP_VERSION);

        setCpVersionSeq(cpVersionSeq);
        setCreatedAt(createdAt);
        setCpSeq(cpSeq);
        setCpUrl(cpUrl);
        setCpVersion(cpVersion);
        setCpVersionDescription(cpVersionDescription);
        setUserSeq(userSeq);
        resetChangedOnNotNull();
    }

    /**
     * Create a detached, initialised CpVersionRecord
     */
    public CpVersionRecord(org.jooq.generated.tables.pojos.CpVersion value) {
        super(CpVersion.CP_VERSION);

        if (value != null) {
            setCpVersionSeq(value.getCpVersionSeq());
            setCreatedAt(value.getCreatedAt());
            setCpSeq(value.getCpSeq());
            setCpUrl(value.getCpUrl());
            setCpVersion(value.getCpVersion());
            setCpVersionDescription(value.getCpVersionDescription());
            setUserSeq(value.getUserSeq());
            resetChangedOnNotNull();
        }
    }
}
