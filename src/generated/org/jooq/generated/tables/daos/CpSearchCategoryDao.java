/*
 * This file is generated by jOOQ.
 */
package org.jooq.generated.tables.daos;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.jooq.Configuration;
import org.jooq.generated.tables.CpSearchCategory;
import org.jooq.generated.tables.records.CpSearchCategoryRecord;
import org.jooq.impl.DAOImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class CpSearchCategoryDao extends DAOImpl<CpSearchCategoryRecord, org.jooq.generated.tables.pojos.CpSearchCategory, Long> {

    /**
     * Create a new CpSearchCategoryDao without any configuration
     */
    public CpSearchCategoryDao() {
        super(CpSearchCategory.CP_SEARCH_CATEGORY, org.jooq.generated.tables.pojos.CpSearchCategory.class);
    }

    /**
     * Create a new CpSearchCategoryDao with an attached configuration
     */
    public CpSearchCategoryDao(Configuration configuration) {
        super(CpSearchCategory.CP_SEARCH_CATEGORY, org.jooq.generated.tables.pojos.CpSearchCategory.class, configuration);
    }

    @Override
    public Long getId(org.jooq.generated.tables.pojos.CpSearchCategory object) {
        return object.getCpSearchCategorySeq();
    }

    /**
     * Fetch records that have <code>cp_search_category_seq BETWEEN
     * lowerInclusive AND upperInclusive</code>
     */
    public List<org.jooq.generated.tables.pojos.CpSearchCategory> fetchRangeOfCpSearchCategorySeq(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(CpSearchCategory.CP_SEARCH_CATEGORY.CP_SEARCH_CATEGORY_SEQ, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>cp_search_category_seq IN (values)</code>
     */
    public List<org.jooq.generated.tables.pojos.CpSearchCategory> fetchByCpSearchCategorySeq(Long... values) {
        return fetch(CpSearchCategory.CP_SEARCH_CATEGORY.CP_SEARCH_CATEGORY_SEQ, values);
    }

    /**
     * Fetch a unique record that has <code>cp_search_category_seq =
     * value</code>
     */
    public org.jooq.generated.tables.pojos.CpSearchCategory fetchOneByCpSearchCategorySeq(Long value) {
        return fetchOne(CpSearchCategory.CP_SEARCH_CATEGORY.CP_SEARCH_CATEGORY_SEQ, value);
    }

    /**
     * Fetch a unique record that has <code>cp_search_category_seq =
     * value</code>
     */
    public Optional<org.jooq.generated.tables.pojos.CpSearchCategory> fetchOptionalByCpSearchCategorySeq(Long value) {
        return fetchOptional(CpSearchCategory.CP_SEARCH_CATEGORY.CP_SEARCH_CATEGORY_SEQ, value);
    }

    /**
     * Fetch records that have <code>created_at BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.jooq.generated.tables.pojos.CpSearchCategory> fetchRangeOfCreatedAt(LocalDateTime lowerInclusive, LocalDateTime upperInclusive) {
        return fetchRange(CpSearchCategory.CP_SEARCH_CATEGORY.CREATED_AT, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>created_at IN (values)</code>
     */
    public List<org.jooq.generated.tables.pojos.CpSearchCategory> fetchByCreatedAt(LocalDateTime... values) {
        return fetch(CpSearchCategory.CP_SEARCH_CATEGORY.CREATED_AT, values);
    }

    /**
     * Fetch records that have <code>deleted_at BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.jooq.generated.tables.pojos.CpSearchCategory> fetchRangeOfDeletedAt(LocalDateTime lowerInclusive, LocalDateTime upperInclusive) {
        return fetchRange(CpSearchCategory.CP_SEARCH_CATEGORY.DELETED_AT, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>deleted_at IN (values)</code>
     */
    public List<org.jooq.generated.tables.pojos.CpSearchCategory> fetchByDeletedAt(LocalDateTime... values) {
        return fetch(CpSearchCategory.CP_SEARCH_CATEGORY.DELETED_AT, values);
    }

    /**
     * Fetch records that have <code>updated_at BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.jooq.generated.tables.pojos.CpSearchCategory> fetchRangeOfUpdatedAt(LocalDateTime lowerInclusive, LocalDateTime upperInclusive) {
        return fetchRange(CpSearchCategory.CP_SEARCH_CATEGORY.UPDATED_AT, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>updated_at IN (values)</code>
     */
    public List<org.jooq.generated.tables.pojos.CpSearchCategory> fetchByUpdatedAt(LocalDateTime... values) {
        return fetch(CpSearchCategory.CP_SEARCH_CATEGORY.UPDATED_AT, values);
    }

    /**
     * Fetch records that have <code>cp_search_category_name BETWEEN
     * lowerInclusive AND upperInclusive</code>
     */
    public List<org.jooq.generated.tables.pojos.CpSearchCategory> fetchRangeOfCpSearchCategoryName(String lowerInclusive, String upperInclusive) {
        return fetchRange(CpSearchCategory.CP_SEARCH_CATEGORY.CP_SEARCH_CATEGORY_NAME, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>cp_search_category_name IN (values)</code>
     */
    public List<org.jooq.generated.tables.pojos.CpSearchCategory> fetchByCpSearchCategoryName(String... values) {
        return fetch(CpSearchCategory.CP_SEARCH_CATEGORY.CP_SEARCH_CATEGORY_NAME, values);
    }

    /**
     * Fetch records that have <code>user_seq BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.jooq.generated.tables.pojos.CpSearchCategory> fetchRangeOfUserSeq(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(CpSearchCategory.CP_SEARCH_CATEGORY.USER_SEQ, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>user_seq IN (values)</code>
     */
    public List<org.jooq.generated.tables.pojos.CpSearchCategory> fetchByUserSeq(Long... values) {
        return fetch(CpSearchCategory.CP_SEARCH_CATEGORY.USER_SEQ, values);
    }
}
