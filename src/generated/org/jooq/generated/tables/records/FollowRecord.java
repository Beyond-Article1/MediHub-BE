/*
 * This file is generated by jOOQ.
 */
package org.jooq.generated.tables.records;


import org.jooq.Record1;
import org.jooq.generated.tables.JFollow;
import org.jooq.generated.tables.pojos.Follow;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class FollowRecord extends UpdatableRecordImpl<FollowRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>medihub.follow.follow_seq</code>.
     */
    public FollowRecord setFollowSeq(Long value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>medihub.follow.follow_seq</code>.
     */
    public Long getFollowSeq() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>medihub.follow.user_from_seq</code>.
     */
    public FollowRecord setUserFromSeq(Long value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>medihub.follow.user_from_seq</code>.
     */
    public Long getUserFromSeq() {
        return (Long) get(1);
    }

    /**
     * Setter for <code>medihub.follow.user_to_seq</code>.
     */
    public FollowRecord setUserToSeq(Long value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>medihub.follow.user_to_seq</code>.
     */
    public Long getUserToSeq() {
        return (Long) get(2);
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
     * Create a detached FollowRecord
     */
    public FollowRecord() {
        super(JFollow.FOLLOW);
    }

    /**
     * Create a detached, initialised FollowRecord
     */
    public FollowRecord(Long followSeq, Long userFromSeq, Long userToSeq) {
        super(JFollow.FOLLOW);

        setFollowSeq(followSeq);
        setUserFromSeq(userFromSeq);
        setUserToSeq(userToSeq);
        resetChangedOnNotNull();
    }

    /**
     * Create a detached, initialised FollowRecord
     */
    public FollowRecord(Follow value) {
        super(JFollow.FOLLOW);

        if (value != null) {
            setFollowSeq(value.getFollowSeq());
            setUserFromSeq(value.getUserFromSeq());
            setUserToSeq(value.getUserToSeq());
            resetChangedOnNotNull();
        }
    }
}
