/*
 * This file is generated by jOOQ.
 */
package org.jooq.generated.tables;


import java.util.Collection;

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
import org.jooq.generated.tables.JUser.UserPath;
import org.jooq.generated.tables.records.RankingRecord;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class JRanking extends TableImpl<RankingRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>medihub.ranking</code>
     */
    public static final JRanking RANKING = new JRanking();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<RankingRecord> getRecordType() {
        return RankingRecord.class;
    }

    /**
     * The column <code>medihub.ranking.ranking_seq</code>.
     */
    public final TableField<RankingRecord, Long> RANKING_SEQ = createField(DSL.name("ranking_seq"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>medihub.ranking.dept_seq</code>.
     */
    public final TableField<RankingRecord, Long> DEPT_SEQ = createField(DSL.name("dept_seq"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>medihub.ranking.ranking_name</code>.
     */
    public final TableField<RankingRecord, String> RANKING_NAME = createField(DSL.name("ranking_name"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>medihub.ranking.ranking_num</code>.
     */
    public final TableField<RankingRecord, Integer> RANKING_NUM = createField(DSL.name("ranking_num"), SQLDataType.INTEGER.nullable(false), this, "");

    private JRanking(Name alias, Table<RankingRecord> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private JRanking(Name alias, Table<RankingRecord> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table(), where);
    }

    /**
     * Create an aliased <code>medihub.ranking</code> table reference
     */
    public JRanking(String alias) {
        this(DSL.name(alias), RANKING);
    }

    /**
     * Create an aliased <code>medihub.ranking</code> table reference
     */
    public JRanking(Name alias) {
        this(alias, RANKING);
    }

    /**
     * Create a <code>medihub.ranking</code> table reference
     */
    public JRanking() {
        this(DSL.name("ranking"), null);
    }

    public <O extends Record> JRanking(Table<O> path, ForeignKey<O, RankingRecord> childPath, InverseForeignKey<O, RankingRecord> parentPath) {
        super(path, childPath, parentPath, RANKING);
    }

    /**
     * A subtype implementing {@link Path} for simplified path-based joins.
     */
    public static class RankingPath extends JRanking implements Path<RankingRecord> {

        private static final long serialVersionUID = 1L;
        public <O extends Record> RankingPath(Table<O> path, ForeignKey<O, RankingRecord> childPath, InverseForeignKey<O, RankingRecord> parentPath) {
            super(path, childPath, parentPath);
        }
        private RankingPath(Name alias, Table<RankingRecord> aliased) {
            super(alias, aliased);
        }

        @Override
        public RankingPath as(String alias) {
            return new RankingPath(DSL.name(alias), this);
        }

        @Override
        public RankingPath as(Name alias) {
            return new RankingPath(alias, this);
        }

        @Override
        public RankingPath as(Table<?> alias) {
            return new RankingPath(alias.getQualifiedName(), this);
        }
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : JMedihub.MEDIHUB;
    }

    @Override
    public Identity<RankingRecord, Long> getIdentity() {
        return (Identity<RankingRecord, Long>) super.getIdentity();
    }

    @Override
    public UniqueKey<RankingRecord> getPrimaryKey() {
        return Keys.KEY_RANKING_PRIMARY;
    }

    private transient UserPath _user;

    /**
     * Get the implicit to-many join path to the <code>medihub.user</code> table
     */
    public UserPath user() {
        if (_user == null)
            _user = new UserPath(this, null, Keys.FK83755D2A40VEIG3NR8D96CE8S.getInverseKey());

        return _user;
    }

    @Override
    public JRanking as(String alias) {
        return new JRanking(DSL.name(alias), this);
    }

    @Override
    public JRanking as(Name alias) {
        return new JRanking(alias, this);
    }

    @Override
    public JRanking as(Table<?> alias) {
        return new JRanking(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public JRanking rename(String name) {
        return new JRanking(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public JRanking rename(Name name) {
        return new JRanking(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public JRanking rename(Table<?> name) {
        return new JRanking(name.getQualifiedName(), null);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public JRanking where(Condition condition) {
        return new JRanking(getQualifiedName(), aliased() ? this : null, null, condition);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public JRanking where(Collection<? extends Condition> conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public JRanking where(Condition... conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public JRanking where(Field<Boolean> condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public JRanking where(SQL condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public JRanking where(@Stringly.SQL String condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public JRanking where(@Stringly.SQL String condition, Object... binds) {
        return where(DSL.condition(condition, binds));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public JRanking where(@Stringly.SQL String condition, QueryPart... parts) {
        return where(DSL.condition(condition, parts));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public JRanking whereExists(Select<?> select) {
        return where(DSL.exists(select));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public JRanking whereNotExists(Select<?> select) {
        return where(DSL.notExists(select));
    }
}
