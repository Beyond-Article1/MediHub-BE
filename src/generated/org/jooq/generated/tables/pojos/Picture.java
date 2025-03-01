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
public class Picture implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long pictureSeq;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
    private String pictureChangedName;
    private Boolean pictureIsDeleted;
    private String pictureName;
    private String pictureType;
    private String pictureUrl;
    private Long flagSeq;

    public Picture() {}

    public Picture(Picture value) {
        this.pictureSeq = value.pictureSeq;
        this.createdAt = value.createdAt;
        this.deletedAt = value.deletedAt;
        this.pictureChangedName = value.pictureChangedName;
        this.pictureIsDeleted = value.pictureIsDeleted;
        this.pictureName = value.pictureName;
        this.pictureType = value.pictureType;
        this.pictureUrl = value.pictureUrl;
        this.flagSeq = value.flagSeq;
    }

    public Picture(
        Long pictureSeq,
        LocalDateTime createdAt,
        LocalDateTime deletedAt,
        String pictureChangedName,
        Boolean pictureIsDeleted,
        String pictureName,
        String pictureType,
        String pictureUrl,
        Long flagSeq
    ) {
        this.pictureSeq = pictureSeq;
        this.createdAt = createdAt;
        this.deletedAt = deletedAt;
        this.pictureChangedName = pictureChangedName;
        this.pictureIsDeleted = pictureIsDeleted;
        this.pictureName = pictureName;
        this.pictureType = pictureType;
        this.pictureUrl = pictureUrl;
        this.flagSeq = flagSeq;
    }

    /**
     * Getter for <code>medihub.picture.picture_seq</code>.
     */
    public Long getPictureSeq() {
        return this.pictureSeq;
    }

    /**
     * Setter for <code>medihub.picture.picture_seq</code>.
     */
    public Picture setPictureSeq(Long pictureSeq) {
        this.pictureSeq = pictureSeq;
        return this;
    }

    /**
     * Getter for <code>medihub.picture.created_at</code>.
     */
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    /**
     * Setter for <code>medihub.picture.created_at</code>.
     */
    public Picture setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    /**
     * Getter for <code>medihub.picture.deleted_at</code>.
     */
    public LocalDateTime getDeletedAt() {
        return this.deletedAt;
    }

    /**
     * Setter for <code>medihub.picture.deleted_at</code>.
     */
    public Picture setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }

    /**
     * Getter for <code>medihub.picture.picture_changed_name</code>.
     */
    public String getPictureChangedName() {
        return this.pictureChangedName;
    }

    /**
     * Setter for <code>medihub.picture.picture_changed_name</code>.
     */
    public Picture setPictureChangedName(String pictureChangedName) {
        this.pictureChangedName = pictureChangedName;
        return this;
    }

    /**
     * Getter for <code>medihub.picture.picture_is_deleted</code>.
     */
    public Boolean getPictureIsDeleted() {
        return this.pictureIsDeleted;
    }

    /**
     * Setter for <code>medihub.picture.picture_is_deleted</code>.
     */
    public Picture setPictureIsDeleted(Boolean pictureIsDeleted) {
        this.pictureIsDeleted = pictureIsDeleted;
        return this;
    }

    /**
     * Getter for <code>medihub.picture.picture_name</code>.
     */
    public String getPictureName() {
        return this.pictureName;
    }

    /**
     * Setter for <code>medihub.picture.picture_name</code>.
     */
    public Picture setPictureName(String pictureName) {
        this.pictureName = pictureName;
        return this;
    }

    /**
     * Getter for <code>medihub.picture.picture_type</code>.
     */
    public String getPictureType() {
        return this.pictureType;
    }

    /**
     * Setter for <code>medihub.picture.picture_type</code>.
     */
    public Picture setPictureType(String pictureType) {
        this.pictureType = pictureType;
        return this;
    }

    /**
     * Getter for <code>medihub.picture.picture_url</code>.
     */
    public String getPictureUrl() {
        return this.pictureUrl;
    }

    /**
     * Setter for <code>medihub.picture.picture_url</code>.
     */
    public Picture setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
        return this;
    }

    /**
     * Getter for <code>medihub.picture.flag_seq</code>.
     */
    public Long getFlagSeq() {
        return this.flagSeq;
    }

    /**
     * Setter for <code>medihub.picture.flag_seq</code>.
     */
    public Picture setFlagSeq(Long flagSeq) {
        this.flagSeq = flagSeq;
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
        final Picture other = (Picture) obj;
        if (this.pictureSeq == null) {
            if (other.pictureSeq != null)
                return false;
        }
        else if (!this.pictureSeq.equals(other.pictureSeq))
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
        if (this.pictureChangedName == null) {
            if (other.pictureChangedName != null)
                return false;
        }
        else if (!this.pictureChangedName.equals(other.pictureChangedName))
            return false;
        if (this.pictureIsDeleted == null) {
            if (other.pictureIsDeleted != null)
                return false;
        }
        else if (!this.pictureIsDeleted.equals(other.pictureIsDeleted))
            return false;
        if (this.pictureName == null) {
            if (other.pictureName != null)
                return false;
        }
        else if (!this.pictureName.equals(other.pictureName))
            return false;
        if (this.pictureType == null) {
            if (other.pictureType != null)
                return false;
        }
        else if (!this.pictureType.equals(other.pictureType))
            return false;
        if (this.pictureUrl == null) {
            if (other.pictureUrl != null)
                return false;
        }
        else if (!this.pictureUrl.equals(other.pictureUrl))
            return false;
        if (this.flagSeq == null) {
            if (other.flagSeq != null)
                return false;
        }
        else if (!this.flagSeq.equals(other.flagSeq))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.pictureSeq == null) ? 0 : this.pictureSeq.hashCode());
        result = prime * result + ((this.createdAt == null) ? 0 : this.createdAt.hashCode());
        result = prime * result + ((this.deletedAt == null) ? 0 : this.deletedAt.hashCode());
        result = prime * result + ((this.pictureChangedName == null) ? 0 : this.pictureChangedName.hashCode());
        result = prime * result + ((this.pictureIsDeleted == null) ? 0 : this.pictureIsDeleted.hashCode());
        result = prime * result + ((this.pictureName == null) ? 0 : this.pictureName.hashCode());
        result = prime * result + ((this.pictureType == null) ? 0 : this.pictureType.hashCode());
        result = prime * result + ((this.pictureUrl == null) ? 0 : this.pictureUrl.hashCode());
        result = prime * result + ((this.flagSeq == null) ? 0 : this.flagSeq.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Picture (");

        sb.append(pictureSeq);
        sb.append(", ").append(createdAt);
        sb.append(", ").append(deletedAt);
        sb.append(", ").append(pictureChangedName);
        sb.append(", ").append(pictureIsDeleted);
        sb.append(", ").append(pictureName);
        sb.append(", ").append(pictureType);
        sb.append(", ").append(pictureUrl);
        sb.append(", ").append(flagSeq);

        sb.append(")");
        return sb.toString();
    }
}
