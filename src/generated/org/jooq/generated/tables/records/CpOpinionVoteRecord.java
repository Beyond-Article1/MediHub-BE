/*
 * This file is generated by jOOQ.
 */
package org.jooq.generated.tables.records;


import java.time.LocalDateTime;

import org.jooq.Record1;
import org.jooq.generated.tables.JCpOpinionVote;
import org.jooq.generated.tables.pojos.CpOpinionVote;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class CpOpinionVoteRecord extends UpdatableRecordImpl<CpOpinionVoteRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>medihub.cp_opinion_vote.cp_opinion_vote_seq</code>.
     */
    public CpOpinionVoteRecord setCpOpinionVoteSeq(Long value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>medihub.cp_opinion_vote.cp_opinion_vote_seq</code>.
     */
    public Long getCpOpinionVoteSeq() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>medihub.cp_opinion_vote.created_at</code>.
     */
    public CpOpinionVoteRecord setCreatedAt(LocalDateTime value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>medihub.cp_opinion_vote.created_at</code>.
     */
    public LocalDateTime getCreatedAt() {
        return (LocalDateTime) get(1);
    }

    /**
     * Setter for <code>medihub.cp_opinion_vote.cp_opinion_seq</code>.
     */
    public CpOpinionVoteRecord setCpOpinionSeq(Long value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>medihub.cp_opinion_vote.cp_opinion_seq</code>.
     */
    public Long getCpOpinionSeq() {
        return (Long) get(2);
    }

    /**
     * Setter for <code>medihub.cp_opinion_vote.cp_opinion_vote</code>.
     */
    public CpOpinionVoteRecord setCpOpinionVote(Boolean value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>medihub.cp_opinion_vote.cp_opinion_vote</code>.
     */
    public Boolean getCpOpinionVote() {
        return (Boolean) get(3);
    }

    /**
     * Setter for <code>medihub.cp_opinion_vote.user_seq</code>.
     */
    public CpOpinionVoteRecord setUserSeq(Long value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>medihub.cp_opinion_vote.user_seq</code>.
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
     * Create a detached CpOpinionVoteRecord
     */
    public CpOpinionVoteRecord() {
        super(JCpOpinionVote.CP_OPINION_VOTE);
    }

    /**
     * Create a detached, initialised CpOpinionVoteRecord
     */
    public CpOpinionVoteRecord(Long cpOpinionVoteSeq, LocalDateTime createdAt, Long cpOpinionSeq, Boolean cpOpinionVote, Long userSeq) {
        super(JCpOpinionVote.CP_OPINION_VOTE);

        setCpOpinionVoteSeq(cpOpinionVoteSeq);
        setCreatedAt(createdAt);
        setCpOpinionSeq(cpOpinionSeq);
        setCpOpinionVote(cpOpinionVote);
        setUserSeq(userSeq);
        resetChangedOnNotNull();
    }

    /**
     * Create a detached, initialised CpOpinionVoteRecord
     */
    public CpOpinionVoteRecord(CpOpinionVote value) {
        super(JCpOpinionVote.CP_OPINION_VOTE);

        if (value != null) {
            setCpOpinionVoteSeq(value.getCpOpinionVoteSeq());
            setCreatedAt(value.getCreatedAt());
            setCpOpinionSeq(value.getCpOpinionSeq());
            setCpOpinionVote(value.getCpOpinionVote());
            setUserSeq(value.getUserSeq());
            resetChangedOnNotNull();
        }
    }
}
