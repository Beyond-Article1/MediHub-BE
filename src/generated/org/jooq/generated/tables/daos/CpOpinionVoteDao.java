/*
 * This file is generated by jOOQ.
 */
package org.jooq.generated.tables.daos;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.jooq.Configuration;
import org.jooq.generated.tables.CpOpinionVote;
import org.jooq.generated.tables.records.CpOpinionVoteRecord;
import org.jooq.impl.DAOImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class CpOpinionVoteDao extends DAOImpl<CpOpinionVoteRecord, org.jooq.generated.tables.pojos.CpOpinionVote, Long> {

    /**
     * Create a new CpOpinionVoteDao without any configuration
     */
    public CpOpinionVoteDao() {
        super(CpOpinionVote.CP_OPINION_VOTE, org.jooq.generated.tables.pojos.CpOpinionVote.class);
    }

    /**
     * Create a new CpOpinionVoteDao with an attached configuration
     */
    public CpOpinionVoteDao(Configuration configuration) {
        super(CpOpinionVote.CP_OPINION_VOTE, org.jooq.generated.tables.pojos.CpOpinionVote.class, configuration);
    }

    @Override
    public Long getId(org.jooq.generated.tables.pojos.CpOpinionVote object) {
        return object.getCpOpinionVoteSeq();
    }

    /**
     * Fetch records that have <code>cp_opinion_vote_seq BETWEEN lowerInclusive
     * AND upperInclusive</code>
     */
    public List<org.jooq.generated.tables.pojos.CpOpinionVote> fetchRangeOfCpOpinionVoteSeq(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(CpOpinionVote.CP_OPINION_VOTE.CP_OPINION_VOTE_SEQ, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>cp_opinion_vote_seq IN (values)</code>
     */
    public List<org.jooq.generated.tables.pojos.CpOpinionVote> fetchByCpOpinionVoteSeq(Long... values) {
        return fetch(CpOpinionVote.CP_OPINION_VOTE.CP_OPINION_VOTE_SEQ, values);
    }

    /**
     * Fetch a unique record that has <code>cp_opinion_vote_seq = value</code>
     */
    public org.jooq.generated.tables.pojos.CpOpinionVote fetchOneByCpOpinionVoteSeq(Long value) {
        return fetchOne(CpOpinionVote.CP_OPINION_VOTE.CP_OPINION_VOTE_SEQ, value);
    }

    /**
     * Fetch a unique record that has <code>cp_opinion_vote_seq = value</code>
     */
    public Optional<org.jooq.generated.tables.pojos.CpOpinionVote> fetchOptionalByCpOpinionVoteSeq(Long value) {
        return fetchOptional(CpOpinionVote.CP_OPINION_VOTE.CP_OPINION_VOTE_SEQ, value);
    }

    /**
     * Fetch records that have <code>created_at BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.jooq.generated.tables.pojos.CpOpinionVote> fetchRangeOfCreatedAt(LocalDateTime lowerInclusive, LocalDateTime upperInclusive) {
        return fetchRange(CpOpinionVote.CP_OPINION_VOTE.CREATED_AT, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>created_at IN (values)</code>
     */
    public List<org.jooq.generated.tables.pojos.CpOpinionVote> fetchByCreatedAt(LocalDateTime... values) {
        return fetch(CpOpinionVote.CP_OPINION_VOTE.CREATED_AT, values);
    }

    /**
     * Fetch records that have <code>cp_opinion_seq BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.jooq.generated.tables.pojos.CpOpinionVote> fetchRangeOfCpOpinionSeq(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(CpOpinionVote.CP_OPINION_VOTE.CP_OPINION_SEQ, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>cp_opinion_seq IN (values)</code>
     */
    public List<org.jooq.generated.tables.pojos.CpOpinionVote> fetchByCpOpinionSeq(Long... values) {
        return fetch(CpOpinionVote.CP_OPINION_VOTE.CP_OPINION_SEQ, values);
    }

    /**
     * Fetch records that have <code>cp_opinion_vote BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.jooq.generated.tables.pojos.CpOpinionVote> fetchRangeOfCpOpinionVote(Boolean lowerInclusive, Boolean upperInclusive) {
        return fetchRange(CpOpinionVote.CP_OPINION_VOTE.CP_OPINION_VOTE_, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>cp_opinion_vote IN (values)</code>
     */
    public List<org.jooq.generated.tables.pojos.CpOpinionVote> fetchByCpOpinionVote(Boolean... values) {
        return fetch(CpOpinionVote.CP_OPINION_VOTE.CP_OPINION_VOTE_, values);
    }

    /**
     * Fetch records that have <code>user_seq BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.jooq.generated.tables.pojos.CpOpinionVote> fetchRangeOfUserSeq(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(CpOpinionVote.CP_OPINION_VOTE.USER_SEQ, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>user_seq IN (values)</code>
     */
    public List<org.jooq.generated.tables.pojos.CpOpinionVote> fetchByUserSeq(Long... values) {
        return fetch(CpOpinionVote.CP_OPINION_VOTE.USER_SEQ, values);
    }
}
