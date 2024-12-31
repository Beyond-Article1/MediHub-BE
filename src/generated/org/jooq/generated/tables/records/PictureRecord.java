/*
 * This file is generated by jOOQ.
 */
package org.jooq.generated.tables.records;


import java.time.LocalDateTime;

import org.jooq.Record1;
import org.jooq.generated.tables.JPicture;
import org.jooq.generated.tables.pojos.Picture;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class PictureRecord extends UpdatableRecordImpl<PictureRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>medihub.picture.picture_seq</code>.
     */
    public PictureRecord setPictureSeq(Long value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>medihub.picture.picture_seq</code>.
     */
    public Long getPictureSeq() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>medihub.picture.created_at</code>.
     */
    public PictureRecord setCreatedAt(LocalDateTime value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>medihub.picture.created_at</code>.
     */
    public LocalDateTime getCreatedAt() {
        return (LocalDateTime) get(1);
    }

    /**
     * Setter for <code>medihub.picture.deleted_at</code>.
     */
    public PictureRecord setDeletedAt(LocalDateTime value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>medihub.picture.deleted_at</code>.
     */
    public LocalDateTime getDeletedAt() {
        return (LocalDateTime) get(2);
    }

    /**
     * Setter for <code>medihub.picture.picture_changed_name</code>.
     */
    public PictureRecord setPictureChangedName(String value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>medihub.picture.picture_changed_name</code>.
     */
    public String getPictureChangedName() {
        return (String) get(3);
    }

    /**
     * Setter for <code>medihub.picture.picture_is_deleted</code>.
     */
    public PictureRecord setPictureIsDeleted(Boolean value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>medihub.picture.picture_is_deleted</code>.
     */
    public Boolean getPictureIsDeleted() {
        return (Boolean) get(4);
    }

    /**
     * Setter for <code>medihub.picture.picture_name</code>.
     */
    public PictureRecord setPictureName(String value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>medihub.picture.picture_name</code>.
     */
    public String getPictureName() {
        return (String) get(5);
    }

    /**
     * Setter for <code>medihub.picture.picture_type</code>.
     */
    public PictureRecord setPictureType(String value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for <code>medihub.picture.picture_type</code>.
     */
    public String getPictureType() {
        return (String) get(6);
    }

    /**
     * Setter for <code>medihub.picture.picture_url</code>.
     */
    public PictureRecord setPictureUrl(String value) {
        set(7, value);
        return this;
    }

    /**
     * Getter for <code>medihub.picture.picture_url</code>.
     */
    public String getPictureUrl() {
        return (String) get(7);
    }

    /**
     * Setter for <code>medihub.picture.flag_seq</code>.
     */
    public PictureRecord setFlagSeq(Long value) {
        set(8, value);
        return this;
    }

    /**
     * Getter for <code>medihub.picture.flag_seq</code>.
     */
    public Long getFlagSeq() {
        return (Long) get(8);
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
     * Create a detached PictureRecord
     */
    public PictureRecord() {
        super(JPicture.PICTURE);
    }

    /**
     * Create a detached, initialised PictureRecord
     */
    public PictureRecord(Long pictureSeq, LocalDateTime createdAt, LocalDateTime deletedAt, String pictureChangedName, Boolean pictureIsDeleted, String pictureName, String pictureType, String pictureUrl, Long flagSeq) {
        super(JPicture.PICTURE);

        setPictureSeq(pictureSeq);
        setCreatedAt(createdAt);
        setDeletedAt(deletedAt);
        setPictureChangedName(pictureChangedName);
        setPictureIsDeleted(pictureIsDeleted);
        setPictureName(pictureName);
        setPictureType(pictureType);
        setPictureUrl(pictureUrl);
        setFlagSeq(flagSeq);
        resetChangedOnNotNull();
    }

    /**
     * Create a detached, initialised PictureRecord
     */
    public PictureRecord(Picture value) {
        super(JPicture.PICTURE);

        if (value != null) {
            setPictureSeq(value.getPictureSeq());
            setCreatedAt(value.getCreatedAt());
            setDeletedAt(value.getDeletedAt());
            setPictureChangedName(value.getPictureChangedName());
            setPictureIsDeleted(value.getPictureIsDeleted());
            setPictureName(value.getPictureName());
            setPictureType(value.getPictureType());
            setPictureUrl(value.getPictureUrl());
            setFlagSeq(value.getFlagSeq());
            resetChangedOnNotNull();
        }
    }
}
