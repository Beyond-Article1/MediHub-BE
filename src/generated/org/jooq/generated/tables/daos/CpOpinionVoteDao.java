/*
 * This file is generated by jOOQ.
 */
package org.jooq.generated.tables.daos;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.jooq.Configuration;
import org.jooq.generated.tables.JCpOpinionVote;
import org.jooq.generated.tables.pojos.CpOpinionVote;
import org.jooq.generated.tables.records.CpOpinionVoteRecord;
import org.jooq.impl.DAOImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class CpOpinionVoteDao extends DAOImpl<CpOpinionVoteRecord, CpOpinionVote, Long> {

    /**
     * Create a new CpOpinionVoteDao without any configuration
     */
    public CpOpinionVoteDao() {
        super(JCpOpinionVote.CP_OPINION_VOTE, CpOpinionVote.class);
    }

    /**
     * Create a new CpOpinionVoteDao with an attached configuration
     */
    public CpOpinionVoteDao(Configuration configuration) {
        super(JCpOpinionVote.CP_OPINION_VOTE, CpOpinionVote.class, configuration);
    }

    @Override
    public Long getId(CpOpinionVote object) {
        return object.getCpOpinionVoteSeq();
    }

    /**
     * Fetch records that have <code>cp_opinion_vote_seq BETWEEN lowerInclusive
     * AND upperInclusive</code>
     */
    public List<CpOpinionVote> fetchRangeOfJCpOpinionVoteSeq(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(JCpOpinionVote.CP_OPINION_VOTE.CP_OPINION_VOTE_SEQ, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>cp_opinion_vote_seq IN (values)</code>
     */
    public List<CpOpinionVote> fetchByJCpOpinionVoteSeq(Long... values) {
        return fetch(JCpOpinionVote.CP_OPINION_VOTE.CP_OPINION_VOTE_SEQ, values);
    }

    /**
     * Fetch a unique record that has <code>cp_opinion_vote_seq = value</code>
     */
    public CpOpinionVote fetchOneByJCpOpinionVoteSeq(Long value) {
        return fetchOne(JCpOpinionVote.CP_OPINION_VOTE.CP_OPINION_VOTE_SEQ, value);
    }

    /**
     * Fetch a unique record that has <code>cp_opinion_vote_seq = value</code>
     */
    public Optional<CpOpinionVote> fetchOptionalByJCpOpinionVoteSeq(Long value) {
        return fetchOptional(JCpOpinionVote.CP_OPINION_VOTE.CP_OPINION_VOTE_SEQ, value);
    }

    /**
     * Fetch records that have <code>created_at BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<CpOpinionVote> fetchRangeOfJCreatedAt(LocalDateTime lowerInclusive, LocalDateTime upperInclusive) {
        return fetchRange(JCpOpinionVote.CP_OPINION_VOTE.CREATED_AT, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>created_at IN (values)</code>
     */
    public List<CpOpinionVote> fetchByJCreatedAt(LocalDateTime... values) {
        return fetch(JCpOpinionVote.CP_OPINION_VOTE.CREATED_AT, values);
    }

    /**
     * Fetch records that have <code>cp_opinion_seq BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<CpOpinionVote> fetchRangeOfJCpOpinionSeq(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(JCpOpinionVote.CP_OPINION_VOTE.CP_OPINION_SEQ, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>cp_opinion_seq IN (values)</code>
     */
    public List<CpOpinionVote> fetchByJCpOpinionSeq(Long... values) {
        return fetch(JCpOpinionVote.CP_OPINION_VOTE.CP_OPINION_SEQ, values);
    }

    /**
     * Fetch records that have <code>cp_opinion_vote BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<CpOpinionVote> fetchRangeOfJCpOpinionVote(Boolean lowerInclusive, Boolean upperInclusive) {
        return fetchRange(JCpOpinionVote.CP_OPINION_VOTE.CP_OPINION_VOTE_, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>cp_opinion_vote IN (values)</code>
     */
    public List<CpOpinionVote> fetchByJCpOpinionVote(Boolean... values) {
        return fetch(JCpOpinionVote.CP_OPINION_VOTE.CP_OPINION_VOTE_, values);
    }

    /**
     * Fetch records that have <code>user_seq BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<CpOpinionVote> fetchRangeOfJUserSeq(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(JCpOpinionVote.CP_OPINION_VOTE.USER_SEQ, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>user_seq IN (values)</code>
     */
    public List<CpOpinionVote> fetchByJUserSeq(Long... values) {
        return fetch(JCpOpinionVote.CP_OPINION_VOTE.USER_SEQ, values);
    }
}