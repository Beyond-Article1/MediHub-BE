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
import org.jooq.generated.tables.JUser.UserPath;
import org.jooq.generated.tables.records.FollowRecord;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class JFollow extends TableImpl<FollowRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>medihub.follow</code>
     */
    public static final JFollow FOLLOW = new JFollow();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<FollowRecord> getRecordType() {
        return FollowRecord.class;
    }

    /**
     * The column <code>medihub.follow.follow_seq</code>.
     */
    public final TableField<FollowRecord, Long> FOLLOW_SEQ = createField(DSL.name("follow_seq"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>medihub.follow.user_from_seq</code>.
     */
    public final TableField<FollowRecord, Long> USER_FROM_SEQ = createField(DSL.name("user_from_seq"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>medihub.follow.user_to_seq</code>.
     */
    public final TableField<FollowRecord, Long> USER_TO_SEQ = createField(DSL.name("user_to_seq"), SQLDataType.BIGINT.nullable(false), this, "");

    private JFollow(Name alias, Table<FollowRecord> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private JFollow(Name alias, Table<FollowRecord> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table(), where);
    }

    /**
     * Create an aliased <code>medihub.follow</code> table reference
     */
    public JFollow(String alias) {
        this(DSL.name(alias), FOLLOW);
    }

    /**
     * Create an aliased <code>medihub.follow</code> table reference
     */
    public JFollow(Name alias) {
        this(alias, FOLLOW);
    }

    /**
     * Create a <code>medihub.follow</code> table reference
     */
    public JFollow() {
        this(DSL.name("follow"), null);
    }

    public <O extends Record> JFollow(Table<O> path, ForeignKey<O, FollowRecord> childPath, InverseForeignKey<O, FollowRecord> parentPath) {
        super(path, childPath, parentPath, FOLLOW);
    }

    /**
     * A subtype implementing {@link Path} for simplified path-based joins.
     */
    public static class FollowPath extends JFollow implements Path<FollowRecord> {

        private static final long serialVersionUID = 1L;
        public <O extends Record> FollowPath(Table<O> path, ForeignKey<O, FollowRecord> childPath, InverseForeignKey<O, FollowRecord> parentPath) {
            super(path, childPath, parentPath);
        }
        private FollowPath(Name alias, Table<FollowRecord> aliased) {
            super(alias, aliased);
        }

        @Override
        public FollowPath as(String alias) {
            return new FollowPath(DSL.name(alias), this);
        }

        @Override
        public FollowPath as(Name alias) {
            return new FollowPath(alias, this);
        }

        @Override
        public FollowPath as(Table<?> alias) {
            return new FollowPath(alias.getQualifiedName(), this);
        }
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : JMedihub.MEDIHUB;
    }

    @Override
    public Identity<FollowRecord, Long> getIdentity() {
        return (Identity<FollowRecord, Long>) super.getIdentity();
    }

    @Override
    public UniqueKey<FollowRecord> getPrimaryKey() {
        return Keys.KEY_FOLLOW_PRIMARY;
    }

    @Override
    public List<ForeignKey<FollowRecord, ?>> getReferences() {
        return Arrays.asList(Keys.FKP0ENBHV2BXXK5ICFA80WTUPHS, Keys.FKQ274UGFWMEJCGAXO77AKIEN5G);
    }

    private transient UserPath _fkp0enbhv2bxxk5icfa80wtuphs;

    /**
     * Get the implicit join path to the <code>medihub.user</code> table, via
     * the <code>FKp0enbhv2bxxk5icfa80wtuphs</code> key.
     */
    public UserPath fkp0enbhv2bxxk5icfa80wtuphs() {
        if (_fkp0enbhv2bxxk5icfa80wtuphs == null)
            _fkp0enbhv2bxxk5icfa80wtuphs = new UserPath(this, Keys.FKP0ENBHV2BXXK5ICFA80WTUPHS, null);

        return _fkp0enbhv2bxxk5icfa80wtuphs;
    }

    private transient UserPath _fkq274ugfwmejcgaxo77akien5g;

    /**
     * Get the implicit join path to the <code>medihub.user</code> table, via
     * the <code>FKq274ugfwmejcgaxo77akien5g</code> key.
     */
    public UserPath fkq274ugfwmejcgaxo77akien5g() {
        if (_fkq274ugfwmejcgaxo77akien5g == null)
            _fkq274ugfwmejcgaxo77akien5g = new UserPath(this, Keys.FKQ274UGFWMEJCGAXO77AKIEN5G, null);

        return _fkq274ugfwmejcgaxo77akien5g;
    }

    @Override
    public JFollow as(String alias) {
        return new JFollow(DSL.name(alias), this);
    }

    @Override
    public JFollow as(Name alias) {
        return new JFollow(alias, this);
    }

    @Override
    public JFollow as(Table<?> alias) {
        return new JFollow(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public JFollow rename(String name) {
        return new JFollow(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public JFollow rename(Name name) {
        return new JFollow(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public JFollow rename(Table<?> name) {
        return new JFollow(name.getQualifiedName(), null);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public JFollow where(Condition condition) {
        return new JFollow(getQualifiedName(), aliased() ? this : null, null, condition);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public JFollow where(Collection<? extends Condition> conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public JFollow where(Condition... conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public JFollow where(Field<Boolean> condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public JFollow where(SQL condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public JFollow where(@Stringly.SQL String condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public JFollow where(@Stringly.SQL String condition, Object... binds) {
        return where(DSL.condition(condition, binds));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public JFollow where(@Stringly.SQL String condition, QueryPart... parts) {
        return where(DSL.condition(condition, parts));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public JFollow whereExists(Select<?> select) {
        return where(DSL.exists(select));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public JFollow whereNotExists(Select<?> select) {
        return where(DSL.notExists(select));
    }
}
