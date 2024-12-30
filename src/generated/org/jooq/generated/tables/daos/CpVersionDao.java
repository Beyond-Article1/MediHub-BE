/*
 * This file is generated by jOOQ.
 */
package org.jooq.generated.tables.daos;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.jooq.Configuration;
import org.jooq.generated.tables.JCpVersion;
import org.jooq.generated.tables.pojos.CpVersion;
import org.jooq.generated.tables.records.CpVersionRecord;
import org.jooq.impl.DAOImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class CpVersionDao extends DAOImpl<CpVersionRecord, CpVersion, Long> {

    /**
     * Create a new CpVersionDao without any configuration
     */
    public CpVersionDao() {
        super(JCpVersion.CP_VERSION, CpVersion.class);
    }

    /**
     * Create a new CpVersionDao with an attached configuration
     */
    public CpVersionDao(Configuration configuration) {
        super(JCpVersion.CP_VERSION, CpVersion.class, configuration);
    }

    @Override
    public Long getId(CpVersion object) {
        return object.getCpVersionSeq();
    }

    /**
     * Fetch records that have <code>cp_version_seq BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<CpVersion> fetchRangeOfJCpVersionSeq(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(JCpVersion.CP_VERSION.CP_VERSION_SEQ, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>cp_version_seq IN (values)</code>
     */
    public List<CpVersion> fetchByJCpVersionSeq(Long... values) {
        return fetch(JCpVersion.CP_VERSION.CP_VERSION_SEQ, values);
    }

    /**
     * Fetch a unique record that has <code>cp_version_seq = value</code>
     */
    public CpVersion fetchOneByJCpVersionSeq(Long value) {
        return fetchOne(JCpVersion.CP_VERSION.CP_VERSION_SEQ, value);
    }

    /**
     * Fetch a unique record that has <code>cp_version_seq = value</code>
     */
    public Optional<CpVersion> fetchOptionalByJCpVersionSeq(Long value) {
        return fetchOptional(JCpVersion.CP_VERSION.CP_VERSION_SEQ, value);
    }

    /**
     * Fetch records that have <code>created_at BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<CpVersion> fetchRangeOfJCreatedAt(LocalDateTime lowerInclusive, LocalDateTime upperInclusive) {
        return fetchRange(JCpVersion.CP_VERSION.CREATED_AT, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>created_at IN (values)</code>
     */
    public List<CpVersion> fetchByJCreatedAt(LocalDateTime... values) {
        return fetch(JCpVersion.CP_VERSION.CREATED_AT, values);
    }

    /**
     * Fetch records that have <code>cp_seq BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<CpVersion> fetchRangeOfJCpSeq(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(JCpVersion.CP_VERSION.CP_SEQ, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>cp_seq IN (values)</code>
     */
    public List<CpVersion> fetchByJCpSeq(Long... values) {
        return fetch(JCpVersion.CP_VERSION.CP_SEQ, values);
    }

    /**
     * Fetch records that have <code>cp_url BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<CpVersion> fetchRangeOfJCpUrl(String lowerInclusive, String upperInclusive) {
        return fetchRange(JCpVersion.CP_VERSION.CP_URL, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>cp_url IN (values)</code>
     */
    public List<CpVersion> fetchByJCpUrl(String... values) {
        return fetch(JCpVersion.CP_VERSION.CP_URL, values);
    }

    /**
     * Fetch records that have <code>cp_version BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<CpVersion> fetchRangeOfJCpVersion(String lowerInclusive, String upperInclusive) {
        return fetchRange(JCpVersion.CP_VERSION.CP_VERSION_, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>cp_version IN (values)</code>
     */
    public List<CpVersion> fetchByJCpVersion(String... values) {
        return fetch(JCpVersion.CP_VERSION.CP_VERSION_, values);
    }

    /**
     * Fetch records that have <code>cp_version_description BETWEEN
     * lowerInclusive AND upperInclusive</code>
     */
    public List<CpVersion> fetchRangeOfJCpVersionDescription(String lowerInclusive, String upperInclusive) {
        return fetchRange(JCpVersion.CP_VERSION.CP_VERSION_DESCRIPTION, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>cp_version_description IN (values)</code>
     */
    public List<CpVersion> fetchByJCpVersionDescription(String... values) {
        return fetch(JCpVersion.CP_VERSION.CP_VERSION_DESCRIPTION, values);
    }

    /**
     * Fetch records that have <code>user_seq BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<CpVersion> fetchRangeOfJUserSeq(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(JCpVersion.CP_VERSION.USER_SEQ, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>user_seq IN (values)</code>
     */
    public List<CpVersion> fetchByJUserSeq(Long... values) {
        return fetch(JCpVersion.CP_VERSION.USER_SEQ, values);
    }
}