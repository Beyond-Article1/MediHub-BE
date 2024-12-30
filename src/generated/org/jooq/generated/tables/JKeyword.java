/*
 * This file is generated by jOOQ.
 */
package org.jooq.generated.tables;


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
import org.jooq.generated.tables.records.KeywordRecord;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class JKeyword extends TableImpl<KeywordRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>medihub.keyword</code>
     */
    public static final JKeyword KEYWORD = new JKeyword();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<KeywordRecord> getRecordType() {
        return KeywordRecord.class;
    }

    /**
     * The column <code>medihub.keyword.keyword_seq</code>.
     */
    public final TableField<KeywordRecord, Long> KEYWORD_SEQ = createField(DSL.name("keyword_seq"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>medihub.keyword.flag_seq</code>.
     */
    public final TableField<KeywordRecord, Long> FLAG_SEQ = createField(DSL.name("flag_seq"), SQLDataType.BIGINT.defaultValue(DSL.field(DSL.raw("NULL"), SQLDataType.BIGINT)), this, "");

    /**
     * The column <code>medihub.keyword.keyword_name</code>.
     */
    public final TableField<KeywordRecord, String> KEYWORD_NAME = createField(DSL.name("keyword_name"), SQLDataType.VARCHAR(255).defaultValue(DSL.field(DSL.raw("NULL"), SQLDataType.VARCHAR)), this, "");

    private JKeyword(Name alias, Table<KeywordRecord> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private JKeyword(Name alias, Table<KeywordRecord> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table(), where);
    }

    /**
     * Create an aliased <code>medihub.keyword</code> table reference
     */
    public JKeyword(String alias) {
        this(DSL.name(alias), KEYWORD);
    }

    /**
     * Create an aliased <code>medihub.keyword</code> table reference
     */
    public JKeyword(Name alias) {
        this(alias, KEYWORD);
    }

    /**
     * Create a <code>medihub.keyword</code> table reference
     */
    public JKeyword() {
        this(DSL.name("keyword"), null);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : JMedihub.MEDIHUB;
    }

    @Override
    public Identity<KeywordRecord, Long> getIdentity() {
        return (Identity<KeywordRecord, Long>) super.getIdentity();
    }

    @Override
    public UniqueKey<KeywordRecord> getPrimaryKey() {
        return Keys.KEY_KEYWORD_PRIMARY;
    }

    @Override
    public JKeyword as(String alias) {
        return new JKeyword(DSL.name(alias), this);
    }

    @Override
    public JKeyword as(Name alias) {
        return new JKeyword(alias, this);
    }

    @Override
    public JKeyword as(Table<?> alias) {
        return new JKeyword(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public JKeyword rename(String name) {
        return new JKeyword(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public JKeyword rename(Name name) {
        return new JKeyword(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public JKeyword rename(Table<?> name) {
        return new JKeyword(name.getQualifiedName(), null);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public JKeyword where(Condition condition) {
        return new JKeyword(getQualifiedName(), aliased() ? this : null, null, condition);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public JKeyword where(Collection<? extends Condition> conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public JKeyword where(Condition... conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public JKeyword where(Field<Boolean> condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public JKeyword where(SQL condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public JKeyword where(@Stringly.SQL String condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public JKeyword where(@Stringly.SQL String condition, Object... binds) {
        return where(DSL.condition(condition, binds));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public JKeyword where(@Stringly.SQL String condition, QueryPart... parts) {
        return where(DSL.condition(condition, parts));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public JKeyword whereExists(Select<?> select) {
        return where(DSL.exists(select));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public JKeyword whereNotExists(Select<?> select) {
        return where(DSL.notExists(select));
    }
}