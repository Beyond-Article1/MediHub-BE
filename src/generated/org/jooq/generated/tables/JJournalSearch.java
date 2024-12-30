/*
 * This file is generated by jOOQ.
 */
package org.jooq.generated.tables;


import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.InverseForeignKey;
import org.jooq.Name;
import org.jooq.Path;
import org.jooq.PlainSQL;
import org.jooq.QueryPart;
import org.jooq.Record;
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
import org.jooq.generated.tables.JJournal.JournalPath;
import org.jooq.generated.tables.JUser.UserPath;
import org.jooq.generated.tables.records.JournalSearchRecord;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class JJournalSearch extends TableImpl<JournalSearchRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>medihub.journal_search</code>
     */
    public static final JJournalSearch JOURNAL_SEARCH = new JJournalSearch();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<JournalSearchRecord> getRecordType() {
        return JournalSearchRecord.class;
    }

    /**
     * The column <code>medihub.journal_search.journal_search_seq</code>.
     */
    public final TableField<JournalSearchRecord, Long> JOURNAL_SEARCH_SEQ = createField(DSL.name("journal_search_seq"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>medihub.journal_search.created_at</code>.
     */
    public final TableField<JournalSearchRecord, LocalDateTime> CREATED_AT = createField(DSL.name("created_at"), SQLDataType.LOCALDATETIME(6).defaultValue(DSL.field(DSL.raw("NULL"), SQLDataType.LOCALDATETIME)), this, "");

    /**
     * The column <code>medihub.journal_search.updated_at</code>.
     */
    public final TableField<JournalSearchRecord, LocalDateTime> UPDATED_AT = createField(DSL.name("updated_at"), SQLDataType.LOCALDATETIME(6).defaultValue(DSL.field(DSL.raw("NULL"), SQLDataType.LOCALDATETIME)), this, "");

    /**
     * The column <code>medihub.journal_search.journal_seq</code>.
     */
    public final TableField<JournalSearchRecord, Long> JOURNAL_SEQ = createField(DSL.name("journal_seq"), SQLDataType.BIGINT.defaultValue(DSL.field(DSL.raw("NULL"), SQLDataType.BIGINT)), this, "");

    /**
     * The column <code>medihub.journal_search.user_seq</code>.
     */
    public final TableField<JournalSearchRecord, Long> USER_SEQ = createField(DSL.name("user_seq"), SQLDataType.BIGINT.defaultValue(DSL.field(DSL.raw("NULL"), SQLDataType.BIGINT)), this, "");

    private JJournalSearch(Name alias, Table<JournalSearchRecord> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private JJournalSearch(Name alias, Table<JournalSearchRecord> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table(), where);
    }

    /**
     * Create an aliased <code>medihub.journal_search</code> table reference
     */
    public JJournalSearch(String alias) {
        this(DSL.name(alias), JOURNAL_SEARCH);
    }

    /**
     * Create an aliased <code>medihub.journal_search</code> table reference
     */
    public JJournalSearch(Name alias) {
        this(alias, JOURNAL_SEARCH);
    }

    /**
     * Create a <code>medihub.journal_search</code> table reference
     */
    public JJournalSearch() {
        this(DSL.name("journal_search"), null);
    }

    public <O extends Record> JJournalSearch(Table<O> path, ForeignKey<O, JournalSearchRecord> childPath, InverseForeignKey<O, JournalSearchRecord> parentPath) {
        super(path, childPath, parentPath, JOURNAL_SEARCH);
    }

    /**
     * A subtype implementing {@link Path} for simplified path-based joins.
     */
    public static class JournalSearchPath extends JJournalSearch implements Path<JournalSearchRecord> {

        private static final long serialVersionUID = 1L;
        public <O extends Record> JournalSearchPath(Table<O> path, ForeignKey<O, JournalSearchRecord> childPath, InverseForeignKey<O, JournalSearchRecord> parentPath) {
            super(path, childPath, parentPath);
        }
        private JournalSearchPath(Name alias, Table<JournalSearchRecord> aliased) {
            super(alias, aliased);
        }

        @Override
        public JournalSearchPath as(String alias) {
            return new JournalSearchPath(DSL.name(alias), this);
        }

        @Override
        public JournalSearchPath as(Name alias) {
            return new JournalSearchPath(alias, this);
        }

        @Override
        public JournalSearchPath as(Table<?> alias) {
            return new JournalSearchPath(alias.getQualifiedName(), this);
        }
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : JMedihub.MEDIHUB;
    }

    @Override
    public Identity<JournalSearchRecord, Long> getIdentity() {
        return (Identity<JournalSearchRecord, Long>) super.getIdentity();
    }

    @Override
    public UniqueKey<JournalSearchRecord> getPrimaryKey() {
        return Keys.KEY_JOURNAL_SEARCH_PRIMARY;
    }

    @Override
    public List<ForeignKey<JournalSearchRecord, ?>> getReferences() {
        return Arrays.asList(Keys.FK259KT22NOLO91PB2XXOW9V7E, Keys.FKMV4OM6DI160KT2N0P88VWV5WW);
    }

    private transient JournalPath _journal;

    /**
     * Get the implicit join path to the <code>medihub.journal</code> table.
     */
    public JournalPath journal() {
        if (_journal == null)
            _journal = new JournalPath(this, Keys.FK259KT22NOLO91PB2XXOW9V7E, null);

        return _journal;
    }

    private transient UserPath _user;

    /**
     * Get the implicit join path to the <code>medihub.user</code> table.
     */
    public UserPath user() {
        if (_user == null)
            _user = new UserPath(this, Keys.FKMV4OM6DI160KT2N0P88VWV5WW, null);

        return _user;
    }

    @Override
    public JJournalSearch as(String alias) {
        return new JJournalSearch(DSL.name(alias), this);
    }

    @Override
    public JJournalSearch as(Name alias) {
        return new JJournalSearch(alias, this);
    }

    @Override
    public JJournalSearch as(Table<?> alias) {
        return new JJournalSearch(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public JJournalSearch rename(String name) {
        return new JJournalSearch(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public JJournalSearch rename(Name name) {
        return new JJournalSearch(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public JJournalSearch rename(Table<?> name) {
        return new JJournalSearch(name.getQualifiedName(), null);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public JJournalSearch where(Condition condition) {
        return new JJournalSearch(getQualifiedName(), aliased() ? this : null, null, condition);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public JJournalSearch where(Collection<? extends Condition> conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public JJournalSearch where(Condition... conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public JJournalSearch where(Field<Boolean> condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public JJournalSearch where(SQL condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public JJournalSearch where(@Stringly.SQL String condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public JJournalSearch where(@Stringly.SQL String condition, Object... binds) {
        return where(DSL.condition(condition, binds));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public JJournalSearch where(@Stringly.SQL String condition, QueryPart... parts) {
        return where(DSL.condition(condition, parts));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public JJournalSearch whereExists(Select<?> select) {
        return where(DSL.exists(select));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public JJournalSearch whereNotExists(Select<?> select) {
        return where(DSL.notExists(select));
    }
}