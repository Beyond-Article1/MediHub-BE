/*
 * This file is generated by jOOQ.
 */
package org.jooq.generated.tables.pojos;


import java.io.Serializable;
import java.time.LocalDateTime;

import org.jooq.generated.enums.NotifyNotiType;
import org.jooq.generated.enums.NotifyReadedAt;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Notify implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long notiSeq;
    private LocalDateTime createdAt;
    private String notiContent;
    private String notiSenderUserName;
    private String notiSenderUserPart;
    private NotifyNotiType notiType;
    private String notiUrl;
    private NotifyReadedAt readedAt;
    private Long flagSeq;
    private Long userSeq;

    public Notify() {}

    public Notify(Notify value) {
        this.notiSeq = value.notiSeq;
        this.createdAt = value.createdAt;
        this.notiContent = value.notiContent;
        this.notiSenderUserName = value.notiSenderUserName;
        this.notiSenderUserPart = value.notiSenderUserPart;
        this.notiType = value.notiType;
        this.notiUrl = value.notiUrl;
        this.readedAt = value.readedAt;
        this.flagSeq = value.flagSeq;
        this.userSeq = value.userSeq;
    }

    public Notify(
        Long notiSeq,
        LocalDateTime createdAt,
        String notiContent,
        String notiSenderUserName,
        String notiSenderUserPart,
        NotifyNotiType notiType,
        String notiUrl,
        NotifyReadedAt readedAt,
        Long flagSeq,
        Long userSeq
    ) {
        this.notiSeq = notiSeq;
        this.createdAt = createdAt;
        this.notiContent = notiContent;
        this.notiSenderUserName = notiSenderUserName;
        this.notiSenderUserPart = notiSenderUserPart;
        this.notiType = notiType;
        this.notiUrl = notiUrl;
        this.readedAt = readedAt;
        this.flagSeq = flagSeq;
        this.userSeq = userSeq;
    }

    /**
     * Getter for <code>medihub.notify.noti_seq</code>.
     */
    public Long getNotiSeq() {
        return this.notiSeq;
    }

    /**
     * Setter for <code>medihub.notify.noti_seq</code>.
     */
    public Notify setNotiSeq(Long notiSeq) {
        this.notiSeq = notiSeq;
        return this;
    }

    /**
     * Getter for <code>medihub.notify.created_at</code>.
     */
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    /**
     * Setter for <code>medihub.notify.created_at</code>.
     */
    public Notify setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    /**
     * Getter for <code>medihub.notify.noti_content</code>.
     */
    public String getNotiContent() {
        return this.notiContent;
    }

    /**
     * Setter for <code>medihub.notify.noti_content</code>.
     */
    public Notify setNotiContent(String notiContent) {
        this.notiContent = notiContent;
        return this;
    }

    /**
     * Getter for <code>medihub.notify.noti_sender_user_name</code>.
     */
    public String getNotiSenderUserName() {
        return this.notiSenderUserName;
    }

    /**
     * Setter for <code>medihub.notify.noti_sender_user_name</code>.
     */
    public Notify setNotiSenderUserName(String notiSenderUserName) {
        this.notiSenderUserName = notiSenderUserName;
        return this;
    }

    /**
     * Getter for <code>medihub.notify.noti_sender_user_part</code>.
     */
    public String getNotiSenderUserPart() {
        return this.notiSenderUserPart;
    }

    /**
     * Setter for <code>medihub.notify.noti_sender_user_part</code>.
     */
    public Notify setNotiSenderUserPart(String notiSenderUserPart) {
        this.notiSenderUserPart = notiSenderUserPart;
        return this;
    }

    /**
     * Getter for <code>medihub.notify.noti_type</code>.
     */
    public NotifyNotiType getNotiType() {
        return this.notiType;
    }

    /**
     * Setter for <code>medihub.notify.noti_type</code>.
     */
    public Notify setNotiType(NotifyNotiType notiType) {
        this.notiType = notiType;
        return this;
    }

    /**
     * Getter for <code>medihub.notify.noti_url</code>.
     */
    public String getNotiUrl() {
        return this.notiUrl;
    }

    /**
     * Setter for <code>medihub.notify.noti_url</code>.
     */
    public Notify setNotiUrl(String notiUrl) {
        this.notiUrl = notiUrl;
        return this;
    }

    /**
     * Getter for <code>medihub.notify.readed_at</code>.
     */
    public NotifyReadedAt getReadedAt() {
        return this.readedAt;
    }

    /**
     * Setter for <code>medihub.notify.readed_at</code>.
     */
    public Notify setReadedAt(NotifyReadedAt readedAt) {
        this.readedAt = readedAt;
        return this;
    }

    /**
     * Getter for <code>medihub.notify.flag_seq</code>.
     */
    public Long getFlagSeq() {
        return this.flagSeq;
    }

    /**
     * Setter for <code>medihub.notify.flag_seq</code>.
     */
    public Notify setFlagSeq(Long flagSeq) {
        this.flagSeq = flagSeq;
        return this;
    }

    /**
     * Getter for <code>medihub.notify.user_seq</code>.
     */
    public Long getUserSeq() {
        return this.userSeq;
    }

    /**
     * Setter for <code>medihub.notify.user_seq</code>.
     */
    public Notify setUserSeq(Long userSeq) {
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
        final Notify other = (Notify) obj;
        if (this.notiSeq == null) {
            if (other.notiSeq != null)
                return false;
        }
        else if (!this.notiSeq.equals(other.notiSeq))
            return false;
        if (this.createdAt == null) {
            if (other.createdAt != null)
                return false;
        }
        else if (!this.createdAt.equals(other.createdAt))
            return false;
        if (this.notiContent == null) {
            if (other.notiContent != null)
                return false;
        }
        else if (!this.notiContent.equals(other.notiContent))
            return false;
        if (this.notiSenderUserName == null) {
            if (other.notiSenderUserName != null)
                return false;
        }
        else if (!this.notiSenderUserName.equals(other.notiSenderUserName))
            return false;
        if (this.notiSenderUserPart == null) {
            if (other.notiSenderUserPart != null)
                return false;
        }
        else if (!this.notiSenderUserPart.equals(other.notiSenderUserPart))
            return false;
        if (this.notiType == null) {
            if (other.notiType != null)
                return false;
        }
        else if (!this.notiType.equals(other.notiType))
            return false;
        if (this.notiUrl == null) {
            if (other.notiUrl != null)
                return false;
        }
        else if (!this.notiUrl.equals(other.notiUrl))
            return false;
        if (this.readedAt == null) {
            if (other.readedAt != null)
                return false;
        }
        else if (!this.readedAt.equals(other.readedAt))
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
        result = prime * result + ((this.notiSeq == null) ? 0 : this.notiSeq.hashCode());
        result = prime * result + ((this.createdAt == null) ? 0 : this.createdAt.hashCode());
        result = prime * result + ((this.notiContent == null) ? 0 : this.notiContent.hashCode());
        result = prime * result + ((this.notiSenderUserName == null) ? 0 : this.notiSenderUserName.hashCode());
        result = prime * result + ((this.notiSenderUserPart == null) ? 0 : this.notiSenderUserPart.hashCode());
        result = prime * result + ((this.notiType == null) ? 0 : this.notiType.hashCode());
        result = prime * result + ((this.notiUrl == null) ? 0 : this.notiUrl.hashCode());
        result = prime * result + ((this.readedAt == null) ? 0 : this.readedAt.hashCode());
        result = prime * result + ((this.flagSeq == null) ? 0 : this.flagSeq.hashCode());
        result = prime * result + ((this.userSeq == null) ? 0 : this.userSeq.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Notify (");

        sb.append(notiSeq);
        sb.append(", ").append(createdAt);
        sb.append(", ").append(notiContent);
        sb.append(", ").append(notiSenderUserName);
        sb.append(", ").append(notiSenderUserPart);
        sb.append(", ").append(notiType);
        sb.append(", ").append(notiUrl);
        sb.append(", ").append(readedAt);
        sb.append(", ").append(flagSeq);
        sb.append(", ").append(userSeq);

        sb.append(")");
        return sb.toString();
    }
}
