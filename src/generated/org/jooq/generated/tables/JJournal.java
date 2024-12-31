/*
 * This file is generated by jOOQ.
 */
package org.jooq.generated.tables;


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
import org.jooq.generated.tables.JJournalSearch.JournalSearchPath;
import org.jooq.generated.tables.records.JournalRecord;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class JJournal extends TableImpl<JournalRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>medihub.journal</code>
     */
    public static final JJournal JOURNAL = new JJournal();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<JournalRecord> getRecordType() {
        return JournalRecord.class;
    }

    /**
     * The column <code>medihub.journal.journal_seq</code>.
     */
    public final TableField<JournalRecord, Long> JOURNAL_SEQ = createField(DSL.name("journal_seq"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>medihub.journal.journal_authors</code>.
     */
    public final TableField<JournalRecord, String> JOURNAL_AUTHORS = createField(DSL.name("journal_authors"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>medihub.journal.journal_date</code>.
     */
    public final TableField<JournalRecord, String> JOURNAL_DATE = createField(DSL.name("journal_date"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>medihub.journal.journal_doi</code>.
     */
    public final TableField<JournalRecord, String> JOURNAL_DOI = createField(DSL.name("journal_doi"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>medihub.journal.journal_journal</code>.
     */
    public final TableField<JournalRecord, String> JOURNAL_JOURNAL = createField(DSL.name("journal_journal"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>medihub.journal.journal_korean_title</code>.
     */
    public final TableField<JournalRecord, String> JOURNAL_KOREAN_TITLE = createField(DSL.name("journal_korean_title"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>medihub.journal.journal_pmid</code>.
     */
    public final TableField<JournalRecord, String> JOURNAL_PMID = createField(DSL.name("journal_pmid"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>medihub.journal.journal_size</code>.
     */
    public final TableField<JournalRecord, String> JOURNAL_SIZE = createField(DSL.name("journal_size"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>medihub.journal.journal_title</code>.
     */
    public final TableField<JournalRecord, String> JOURNAL_TITLE = createField(DSL.name("journal_title"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    private JJournal(Name alias, Table<JournalRecord> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private JJournal(Name alias, Table<JournalRecord> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table(), where);
    }

    /**
     * Create an aliased <code>medihub.journal</code> table reference
     */
    public JJournal(String alias) {
        this(DSL.name(alias), JOURNAL);
    }

    /**
     * Create an aliased <code>medihub.journal</code> table reference
     */
    public JJournal(Name alias) {
        this(alias, JOURNAL);
    }

    /**
     * Create a <code>medihub.journal</code> table reference
     */
    public JJournal() {
        this(DSL.name("journal"), null);
    }

    public <O extends Record> JJournal(Table<O> path, ForeignKey<O, JournalRecord> childPath, InverseForeignKey<O, JournalRecord> parentPath) {
        super(path, childPath, parentPath, JOURNAL);
    }

    /**
     * A subtype implementing {@link Path} for simplified path-based joins.
     */
    public static class JournalPath extends JJournal implements Path<JournalRecord> {

        private static final long serialVersionUID = 1L;
        public <O extends Record> JournalPath(Table<O> path, ForeignKey<O, JournalRecord> childPath, InverseForeignKey<O, JournalRecord> parentPath) {
            super(path, childPath, parentPath);
        }
        private JournalPath(Name alias, Table<JournalRecord> aliased) {
            super(alias, aliased);
        }

        @Override
        public JournalPath as(String alias) {
            return new JournalPath(DSL.name(alias), this);
        }

        @Override
        public JournalPath as(Name alias) {
            return new JournalPath(alias, this);
        }

        @Override
        public JournalPath as(Table<?> alias) {
            return new JournalPath(alias.getQualifiedName(), this);
        }
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : JMedihub.MEDIHUB;
    }

    @Override
    public Identity<JournalRecord, Long> getIdentity() {
        return (Identity<JournalRecord, Long>) super.getIdentity();
    }

    @Override
    public UniqueKey<JournalRecord> getPrimaryKey() {
        return Keys.KEY_JOURNAL_PRIMARY;
    }

    @Override
    public List<UniqueKey<JournalRecord>> getUniqueKeys() {
        return Arrays.asList(Keys.KEY_JOURNAL_UK6QDJ06OW81AE53PLROBTBVXIB, Keys.KEY_JOURNAL_UKE41MC38B5A5FTY8JNBR6MW8GG, Keys.KEY_JOURNAL_UKLQNI2RF2OP2M10PX5GX2MTT3A);
    }

    private transient JournalSearchPath _journalSearch;

    /**
     * Get the implicit to-many join path to the
     * <code>medihub.journal_search</code> table
     */
    public JournalSearchPath journalSearch() {
        if (_journalSearch == null)
            _journalSearch = new JournalSearchPath(this, null, Keys.FK259KT22NOLO91PB2XXOW9V7E.getInverseKey());

        return _journalSearch;
    }

    @Override
    public JJournal as(String alias) {
        return new JJournal(DSL.name(alias), this);
    }

    @Override
    public JJournal as(Name alias) {
        return new JJournal(alias, this);
    }

    @Override
    public JJournal as(Table<?> alias) {
        return new JJournal(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public JJournal rename(String name) {
        return new JJournal(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public JJournal rename(Name name) {
        return new JJournal(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public JJournal rename(Table<?> name) {
        return new JJournal(name.getQualifiedName(), null);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public JJournal where(Condition condition) {
        return new JJournal(getQualifiedName(), aliased() ? this : null, null, condition);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public JJournal where(Collection<? extends Condition> conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public JJournal where(Condition... conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public JJournal where(Field<Boolean> condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public JJournal where(SQL condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public JJournal where(@Stringly.SQL String condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public JJournal where(@Stringly.SQL String condition, Object... binds) {
        return where(DSL.condition(condition, binds));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public JJournal where(@Stringly.SQL String condition, QueryPart... parts) {
        return where(DSL.condition(condition, parts));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public JJournal whereExists(Select<?> select) {
        return where(DSL.exists(select));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public JJournal whereNotExists(Select<?> select) {
        return where(DSL.notExists(select));
    }
}
