/*
 * This file is generated by jOOQ.
 */
package org.jooq.generated.tables;


import java.time.LocalDateTime;
import java.util.Collection;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Identity;
import org.jooq.Name;
import org.jooq.PlainSQL;
import org.jooq.QueryPart;
import org.jooq.SQL;
import org.jooq.Schema;
import org.jooq.Select;
import org.jooq.Stringly;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.generated.JMedihub;
import org.jooq.generated.Keys;
import org.jooq.generated.tables.records.CpVersionRecord;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class JCpVersion extends TableImpl<CpVersionRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>medihub.cp_version</code>
     */
    public static final JCpVersion CP_VERSION = new JCpVersion();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<CpVersionRecord> getRecordType() {
        return CpVersionRecord.class;
    }

    /**
     * The column <code>medihub.cp_version.cp_version_seq</code>.
     */
    public final TableField<CpVersionRecord, Long> CP_VERSION_SEQ = createField(DSL.name("cp_version_seq"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>medihub.cp_version.created_at</code>.
     */
    public final TableField<CpVersionRecord, LocalDateTime> CREATED_AT = createField(DSL.name("created_at"), SQLDataType.LOCALDATETIME(6).defaultValue(DSL.field(DSL.raw("NULL"), SQLDataType.LOCALDATETIME)), this, "");

    /**
     * The column <code>medihub.cp_version.cp_seq</code>.
     */
    public final TableField<CpVersionRecord, Long> CP_SEQ = createField(DSL.name("cp_seq"), SQLDataType.BIGINT.defaultValue(DSL.field(DSL.raw("NULL"), SQLDataType.BIGINT)), this, "");

    /**
     * The column <code>medihub.cp_version.cp_url</code>.
     */
    public final TableField<CpVersionRecord, String> CP_URL = createField(DSL.name("cp_url"), SQLDataType.VARCHAR(255).defaultValue(DSL.field(DSL.raw("NULL"), SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>medihub.cp_version.cp_version</code>.
     */
    public final TableField<CpVersionRecord, String> CP_VERSION_ = createField(DSL.name("cp_version"), SQLDataType.VARCHAR(255).defaultValue(DSL.field(DSL.raw("NULL"), SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>medihub.cp_version.cp_version_description</code>.
     */
    public final TableField<CpVersionRecord, String> CP_VERSION_DESCRIPTION = createField(DSL.name("cp_version_description"), SQLDataType.VARCHAR(255).defaultValue(DSL.field(DSL.raw("NULL"), SQLDataType.VARCHAR)), this, "");

    /**
     * The column <code>medihub.cp_version.user_seq</code>.
     */
    public final TableField<CpVersionRecord, Long> USER_SEQ = createField(DSL.name("user_seq"), SQLDataType.BIGINT.defaultValue(DSL.field(DSL.raw("NULL"), SQLDataType.BIGINT)), this, "");

    private JCpVersion(Name alias, Table<CpVersionRecord> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private JCpVersion(Name alias, Table<CpVersionRecord> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table(), where);
    }

    /**
     * Create an aliased <code>medihub.cp_version</code> table reference
     */
    public JCpVersion(String alias) {
        this(DSL.name(alias), CP_VERSION);
    }

    /**
     * Create an aliased <code>medihub.cp_version</code> table reference
     */
    public JCpVersion(Name alias) {
        this(alias, CP_VERSION);
    }

    /**
     * Create a <code>medihub.cp_version</code> table reference
     */
    public JCpVersion() {
        this(DSL.name("cp_version"), null);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : JMedihub.MEDIHUB;
    }

    @Override
    public Identity<CpVersionRecord, Long> getIdentity() {
        return (Identity<CpVersionRecord, Long>) super.getIdentity();
    }

    @Override
    public UniqueKey<CpVersionRecord> getPrimaryKey() {
        return Keys.KEY_CP_VERSION_PRIMARY;
    }

    @Override
    public JCpVersion as(String alias) {
        return new JCpVersion(DSL.name(alias), this);
    }

    @Override
    public JCpVersion as(Name alias) {
        return new JCpVersion(alias, this);
    }

    @Override
    public JCpVersion as(Table<?> alias) {
        return new JCpVersion(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public JCpVersion rename(String name) {
        return new JCpVersion(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public JCpVersion rename(Name name) {
        return new JCpVersion(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public JCpVersion rename(Table<?> name) {
        return new JCpVersion(name.getQualifiedName(), null);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public JCpVersion where(Condition condition) {
        return new JCpVersion(getQualifiedName(), aliased() ? this : null, null, condition);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public JCpVersion where(Collection<? extends Condition> conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public JCpVersion where(Condition... conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public JCpVersion where(Field<Boolean> condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public JCpVersion where(SQL condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public JCpVersion where(@Stringly.SQL String condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public JCpVersion where(@Stringly.SQL String condition, Object... binds) {
        return where(DSL.condition(condition, binds));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public JCpVersion where(@Stringly.SQL String condition, QueryPart... parts) {
        return where(DSL.condition(condition, parts));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public JCpVersion whereExists(Select<?> select) {
        return where(DSL.exists(select));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public JCpVersion whereNotExists(Select<?> select) {
        return where(DSL.notExists(select));
    }
}