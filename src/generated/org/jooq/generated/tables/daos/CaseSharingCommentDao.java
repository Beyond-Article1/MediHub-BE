/*
 * This file is generated by jOOQ.
 */
package org.jooq.generated.tables.daos;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.jooq.Configuration;
import org.jooq.generated.tables.CaseSharingComment;
import org.jooq.generated.tables.records.CaseSharingCommentRecord;
import org.jooq.impl.DAOImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class CaseSharingCommentDao extends DAOImpl<CaseSharingCommentRecord, org.jooq.generated.tables.pojos.CaseSharingComment, Long> {

    /**
     * Create a new CaseSharingCommentDao without any configuration
     */
    public CaseSharingCommentDao() {
        super(CaseSharingComment.CASE_SHARING_COMMENT, org.jooq.generated.tables.pojos.CaseSharingComment.class);
    }

    /**
     * Create a new CaseSharingCommentDao with an attached configuration
     */
    public CaseSharingCommentDao(Configuration configuration) {
        super(CaseSharingComment.CASE_SHARING_COMMENT, org.jooq.generated.tables.pojos.CaseSharingComment.class, configuration);
    }

    @Override
    public Long getId(org.jooq.generated.tables.pojos.CaseSharingComment object) {
        return object.getCaseSharingCommentSeq();
    }

    /**
     * Fetch records that have <code>case_sharing_comment_seq BETWEEN
     * lowerInclusive AND upperInclusive</code>
     */
    public List<org.jooq.generated.tables.pojos.CaseSharingComment> fetchRangeOfCaseSharingCommentSeq(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(CaseSharingComment.CASE_SHARING_COMMENT.CASE_SHARING_COMMENT_SEQ, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>case_sharing_comment_seq IN (values)</code>
     */
    public List<org.jooq.generated.tables.pojos.CaseSharingComment> fetchByCaseSharingCommentSeq(Long... values) {
        return fetch(CaseSharingComment.CASE_SHARING_COMMENT.CASE_SHARING_COMMENT_SEQ, values);
    }

    /**
     * Fetch a unique record that has <code>case_sharing_comment_seq =
     * value</code>
     */
    public org.jooq.generated.tables.pojos.CaseSharingComment fetchOneByCaseSharingCommentSeq(Long value) {
        return fetchOne(CaseSharingComment.CASE_SHARING_COMMENT.CASE_SHARING_COMMENT_SEQ, value);
    }

    /**
     * Fetch a unique record that has <code>case_sharing_comment_seq =
     * value</code>
     */
    public Optional<org.jooq.generated.tables.pojos.CaseSharingComment> fetchOptionalByCaseSharingCommentSeq(Long value) {
        return fetchOptional(CaseSharingComment.CASE_SHARING_COMMENT.CASE_SHARING_COMMENT_SEQ, value);
    }

    /**
     * Fetch records that have <code>created_at BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.jooq.generated.tables.pojos.CaseSharingComment> fetchRangeOfCreatedAt(LocalDateTime lowerInclusive, LocalDateTime upperInclusive) {
        return fetchRange(CaseSharingComment.CASE_SHARING_COMMENT.CREATED_AT, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>created_at IN (values)</code>
     */
    public List<org.jooq.generated.tables.pojos.CaseSharingComment> fetchByCreatedAt(LocalDateTime... values) {
        return fetch(CaseSharingComment.CASE_SHARING_COMMENT.CREATED_AT, values);
    }

    /**
     * Fetch records that have <code>deleted_at BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.jooq.generated.tables.pojos.CaseSharingComment> fetchRangeOfDeletedAt(LocalDateTime lowerInclusive, LocalDateTime upperInclusive) {
        return fetchRange(CaseSharingComment.CASE_SHARING_COMMENT.DELETED_AT, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>deleted_at IN (values)</code>
     */
    public List<org.jooq.generated.tables.pojos.CaseSharingComment> fetchByDeletedAt(LocalDateTime... values) {
        return fetch(CaseSharingComment.CASE_SHARING_COMMENT.DELETED_AT, values);
    }

    /**
     * Fetch records that have <code>updated_at BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.jooq.generated.tables.pojos.CaseSharingComment> fetchRangeOfUpdatedAt(LocalDateTime lowerInclusive, LocalDateTime upperInclusive) {
        return fetchRange(CaseSharingComment.CASE_SHARING_COMMENT.UPDATED_AT, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>updated_at IN (values)</code>
     */
    public List<org.jooq.generated.tables.pojos.CaseSharingComment> fetchByUpdatedAt(LocalDateTime... values) {
        return fetch(CaseSharingComment.CASE_SHARING_COMMENT.UPDATED_AT, values);
    }

    /**
     * Fetch records that have <code>case_sharing_block_id BETWEEN
     * lowerInclusive AND upperInclusive</code>
     */
    public List<org.jooq.generated.tables.pojos.CaseSharingComment> fetchRangeOfCaseSharingBlockId(String lowerInclusive, String upperInclusive) {
        return fetchRange(CaseSharingComment.CASE_SHARING_COMMENT.CASE_SHARING_BLOCK_ID, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>case_sharing_block_id IN (values)</code>
     */
    public List<org.jooq.generated.tables.pojos.CaseSharingComment> fetchByCaseSharingBlockId(String... values) {
        return fetch(CaseSharingComment.CASE_SHARING_COMMENT.CASE_SHARING_BLOCK_ID, values);
    }

    /**
     * Fetch records that have <code>case_sharing_comment_content BETWEEN
     * lowerInclusive AND upperInclusive</code>
     */
    public List<org.jooq.generated.tables.pojos.CaseSharingComment> fetchRangeOfCaseSharingCommentContent(String lowerInclusive, String upperInclusive) {
        return fetchRange(CaseSharingComment.CASE_SHARING_COMMENT.CASE_SHARING_COMMENT_CONTENT, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>case_sharing_comment_content IN
     * (values)</code>
     */
    public List<org.jooq.generated.tables.pojos.CaseSharingComment> fetchByCaseSharingCommentContent(String... values) {
        return fetch(CaseSharingComment.CASE_SHARING_COMMENT.CASE_SHARING_COMMENT_CONTENT, values);
    }

    /**
     * Fetch records that have <code>case_sharing_seq BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.jooq.generated.tables.pojos.CaseSharingComment> fetchRangeOfCaseSharingSeq(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(CaseSharingComment.CASE_SHARING_COMMENT.CASE_SHARING_SEQ, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>case_sharing_seq IN (values)</code>
     */
    public List<org.jooq.generated.tables.pojos.CaseSharingComment> fetchByCaseSharingSeq(Long... values) {
        return fetch(CaseSharingComment.CASE_SHARING_COMMENT.CASE_SHARING_SEQ, values);
    }

    /**
     * Fetch records that have <code>user_seq BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.jooq.generated.tables.pojos.CaseSharingComment> fetchRangeOfUserSeq(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(CaseSharingComment.CASE_SHARING_COMMENT.USER_SEQ, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>user_seq IN (values)</code>
     */
    public List<org.jooq.generated.tables.pojos.CaseSharingComment> fetchByUserSeq(Long... values) {
        return fetch(CaseSharingComment.CASE_SHARING_COMMENT.USER_SEQ, values);
    }
}
