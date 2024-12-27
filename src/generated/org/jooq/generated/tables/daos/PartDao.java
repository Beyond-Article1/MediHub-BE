/*
 * This file is generated by jOOQ.
 */
package org.jooq.generated.tables.daos;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.jooq.Configuration;
import org.jooq.generated.tables.Part;
import org.jooq.generated.tables.records.PartRecord;
import org.jooq.impl.DAOImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class PartDao extends DAOImpl<PartRecord, org.jooq.generated.tables.pojos.Part, Long> {

    /**
     * Create a new PartDao without any configuration
     */
    public PartDao() {
        super(Part.PART, org.jooq.generated.tables.pojos.Part.class);
    }

    /**
     * Create a new PartDao with an attached configuration
     */
    public PartDao(Configuration configuration) {
        super(Part.PART, org.jooq.generated.tables.pojos.Part.class, configuration);
    }

    @Override
    public Long getId(org.jooq.generated.tables.pojos.Part object) {
        return object.getPartSeq();
    }

    /**
     * Fetch records that have <code>part_seq BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.jooq.generated.tables.pojos.Part> fetchRangeOfPartSeq(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(Part.PART.PART_SEQ, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>part_seq IN (values)</code>
     */
    public List<org.jooq.generated.tables.pojos.Part> fetchByPartSeq(Long... values) {
        return fetch(Part.PART.PART_SEQ, values);
    }

    /**
     * Fetch a unique record that has <code>part_seq = value</code>
     */
    public org.jooq.generated.tables.pojos.Part fetchOneByPartSeq(Long value) {
        return fetchOne(Part.PART.PART_SEQ, value);
    }

    /**
     * Fetch a unique record that has <code>part_seq = value</code>
     */
    public Optional<org.jooq.generated.tables.pojos.Part> fetchOptionalByPartSeq(Long value) {
        return fetchOptional(Part.PART.PART_SEQ, value);
    }

    /**
     * Fetch records that have <code>created_at BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.jooq.generated.tables.pojos.Part> fetchRangeOfCreatedAt(LocalDateTime lowerInclusive, LocalDateTime upperInclusive) {
        return fetchRange(Part.PART.CREATED_AT, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>created_at IN (values)</code>
     */
    public List<org.jooq.generated.tables.pojos.Part> fetchByCreatedAt(LocalDateTime... values) {
        return fetch(Part.PART.CREATED_AT, values);
    }

    /**
     * Fetch records that have <code>deleted_at BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.jooq.generated.tables.pojos.Part> fetchRangeOfDeletedAt(LocalDateTime lowerInclusive, LocalDateTime upperInclusive) {
        return fetchRange(Part.PART.DELETED_AT, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>deleted_at IN (values)</code>
     */
    public List<org.jooq.generated.tables.pojos.Part> fetchByDeletedAt(LocalDateTime... values) {
        return fetch(Part.PART.DELETED_AT, values);
    }

    /**
     * Fetch records that have <code>updated_at BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.jooq.generated.tables.pojos.Part> fetchRangeOfUpdatedAt(LocalDateTime lowerInclusive, LocalDateTime upperInclusive) {
        return fetchRange(Part.PART.UPDATED_AT, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>updated_at IN (values)</code>
     */
    public List<org.jooq.generated.tables.pojos.Part> fetchByUpdatedAt(LocalDateTime... values) {
        return fetch(Part.PART.UPDATED_AT, values);
    }

    /**
     * Fetch records that have <code>part_name BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.jooq.generated.tables.pojos.Part> fetchRangeOfPartName(String lowerInclusive, String upperInclusive) {
        return fetchRange(Part.PART.PART_NAME, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>part_name IN (values)</code>
     */
    public List<org.jooq.generated.tables.pojos.Part> fetchByPartName(String... values) {
        return fetch(Part.PART.PART_NAME, values);
    }

    /**
     * Fetch records that have <code>dept_seq BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.jooq.generated.tables.pojos.Part> fetchRangeOfDeptSeq(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(Part.PART.DEPT_SEQ, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>dept_seq IN (values)</code>
     */
    public List<org.jooq.generated.tables.pojos.Part> fetchByDeptSeq(Long... values) {
        return fetch(Part.PART.DEPT_SEQ, values);
    }
}
