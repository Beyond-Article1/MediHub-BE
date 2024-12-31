/*
 * This file is generated by jOOQ.
 */
package org.jooq.generated.tables.daos;


import java.util.List;
import java.util.Optional;

import org.jooq.Configuration;
import org.jooq.generated.tables.JDept;
import org.jooq.generated.tables.pojos.Dept;
import org.jooq.generated.tables.records.DeptRecord;
import org.jooq.impl.DAOImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class DeptDao extends DAOImpl<DeptRecord, Dept, Long> {

    /**
     * Create a new DeptDao without any configuration
     */
    public DeptDao() {
        super(JDept.DEPT, Dept.class);
    }

    /**
     * Create a new DeptDao with an attached configuration
     */
    public DeptDao(Configuration configuration) {
        super(JDept.DEPT, Dept.class, configuration);
    }

    @Override
    public Long getId(Dept object) {
        return object.getDeptSeq();
    }

    /**
     * Fetch records that have <code>dept_seq BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<Dept> fetchRangeOfJDeptSeq(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(JDept.DEPT.DEPT_SEQ, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>dept_seq IN (values)</code>
     */
    public List<Dept> fetchByJDeptSeq(Long... values) {
        return fetch(JDept.DEPT.DEPT_SEQ, values);
    }

    /**
     * Fetch a unique record that has <code>dept_seq = value</code>
     */
    public Dept fetchOneByJDeptSeq(Long value) {
        return fetchOne(JDept.DEPT.DEPT_SEQ, value);
    }

    /**
     * Fetch a unique record that has <code>dept_seq = value</code>
     */
    public Optional<Dept> fetchOptionalByJDeptSeq(Long value) {
        return fetchOptional(JDept.DEPT.DEPT_SEQ, value);
    }

    /**
     * Fetch records that have <code>dept_name BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<Dept> fetchRangeOfJDeptName(String lowerInclusive, String upperInclusive) {
        return fetchRange(JDept.DEPT.DEPT_NAME, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>dept_name IN (values)</code>
     */
    public List<Dept> fetchByJDeptName(String... values) {
        return fetch(JDept.DEPT.DEPT_NAME, values);
    }
}
