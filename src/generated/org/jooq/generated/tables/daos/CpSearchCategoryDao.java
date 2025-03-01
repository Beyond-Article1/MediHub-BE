/*
 * This file is generated by jOOQ.
 */
package org.jooq.generated.tables.daos;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.jooq.Configuration;
import org.jooq.generated.tables.JCpSearchCategory;
import org.jooq.generated.tables.pojos.CpSearchCategory;
import org.jooq.generated.tables.records.CpSearchCategoryRecord;
import org.jooq.impl.DAOImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class CpSearchCategoryDao extends DAOImpl<CpSearchCategoryRecord, CpSearchCategory, Long> {

    /**
     * Create a new CpSearchCategoryDao without any configuration
     */
    public CpSearchCategoryDao() {
        super(JCpSearchCategory.CP_SEARCH_CATEGORY, CpSearchCategory.class);
    }

    /**
     * Create a new CpSearchCategoryDao with an attached configuration
     */
    public CpSearchCategoryDao(Configuration configuration) {
        super(JCpSearchCategory.CP_SEARCH_CATEGORY, CpSearchCategory.class, configuration);
    }

    @Override
    public Long getId(CpSearchCategory object) {
        return object.getCpSearchCategorySeq();
    }

    /**
     * Fetch records that have <code>cp_search_category_seq BETWEEN
     * lowerInclusive AND upperInclusive</code>
     */
    public List<CpSearchCategory> fetchRangeOfJCpSearchCategorySeq(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(JCpSearchCategory.CP_SEARCH_CATEGORY.CP_SEARCH_CATEGORY_SEQ, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>cp_search_category_seq IN (values)</code>
     */
    public List<CpSearchCategory> fetchByJCpSearchCategorySeq(Long... values) {
        return fetch(JCpSearchCategory.CP_SEARCH_CATEGORY.CP_SEARCH_CATEGORY_SEQ, values);
    }

    /**
     * Fetch a unique record that has <code>cp_search_category_seq =
     * value</code>
     */
    public CpSearchCategory fetchOneByJCpSearchCategorySeq(Long value) {
        return fetchOne(JCpSearchCategory.CP_SEARCH_CATEGORY.CP_SEARCH_CATEGORY_SEQ, value);
    }

    /**
     * Fetch a unique record that has <code>cp_search_category_seq =
     * value</code>
     */
    public Optional<CpSearchCategory> fetchOptionalByJCpSearchCategorySeq(Long value) {
        return fetchOptional(JCpSearchCategory.CP_SEARCH_CATEGORY.CP_SEARCH_CATEGORY_SEQ, value);
    }

    /**
     * Fetch records that have <code>created_at BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<CpSearchCategory> fetchRangeOfJCreatedAt(LocalDateTime lowerInclusive, LocalDateTime upperInclusive) {
        return fetchRange(JCpSearchCategory.CP_SEARCH_CATEGORY.CREATED_AT, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>created_at IN (values)</code>
     */
    public List<CpSearchCategory> fetchByJCreatedAt(LocalDateTime... values) {
        return fetch(JCpSearchCategory.CP_SEARCH_CATEGORY.CREATED_AT, values);
    }

    /**
     * Fetch records that have <code>deleted_at BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<CpSearchCategory> fetchRangeOfJDeletedAt(LocalDateTime lowerInclusive, LocalDateTime upperInclusive) {
        return fetchRange(JCpSearchCategory.CP_SEARCH_CATEGORY.DELETED_AT, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>deleted_at IN (values)</code>
     */
    public List<CpSearchCategory> fetchByJDeletedAt(LocalDateTime... values) {
        return fetch(JCpSearchCategory.CP_SEARCH_CATEGORY.DELETED_AT, values);
    }

    /**
     * Fetch records that have <code>updated_at BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<CpSearchCategory> fetchRangeOfJUpdatedAt(LocalDateTime lowerInclusive, LocalDateTime upperInclusive) {
        return fetchRange(JCpSearchCategory.CP_SEARCH_CATEGORY.UPDATED_AT, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>updated_at IN (values)</code>
     */
    public List<CpSearchCategory> fetchByJUpdatedAt(LocalDateTime... values) {
        return fetch(JCpSearchCategory.CP_SEARCH_CATEGORY.UPDATED_AT, values);
    }

    /**
     * Fetch records that have <code>cp_search_category_name BETWEEN
     * lowerInclusive AND upperInclusive</code>
     */
    public List<CpSearchCategory> fetchRangeOfJCpSearchCategoryName(String lowerInclusive, String upperInclusive) {
        return fetchRange(JCpSearchCategory.CP_SEARCH_CATEGORY.CP_SEARCH_CATEGORY_NAME, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>cp_search_category_name IN (values)</code>
     */
    public List<CpSearchCategory> fetchByJCpSearchCategoryName(String... values) {
        return fetch(JCpSearchCategory.CP_SEARCH_CATEGORY.CP_SEARCH_CATEGORY_NAME, values);
    }

    /**
     * Fetch records that have <code>user_seq BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<CpSearchCategory> fetchRangeOfJUserSeq(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(JCpSearchCategory.CP_SEARCH_CATEGORY.USER_SEQ, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>user_seq IN (values)</code>
     */
    public List<CpSearchCategory> fetchByJUserSeq(Long... values) {
        return fetch(JCpSearchCategory.CP_SEARCH_CATEGORY.USER_SEQ, values);
    }
}
