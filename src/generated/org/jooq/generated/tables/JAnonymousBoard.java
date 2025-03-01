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
import org.jooq.generated.tables.JUser.UserPath;
import org.jooq.generated.tables.records.AnonymousBoardRecord;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class JAnonymousBoard extends TableImpl<AnonymousBoardRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>medihub.anonymous_board</code>
     */
    public static final JAnonymousBoard ANONYMOUS_BOARD = new JAnonymousBoard();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<AnonymousBoardRecord> getRecordType() {
        return AnonymousBoardRecord.class;
    }

    /**
     * The column <code>medihub.anonymous_board.anonymous_board_seq</code>.
     */
    public final TableField<AnonymousBoardRecord, Long> ANONYMOUS_BOARD_SEQ = createField(DSL.name("anonymous_board_seq"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>medihub.anonymous_board.created_at</code>.
     */
    public final TableField<AnonymousBoardRecord, LocalDateTime> CREATED_AT = createField(DSL.name("created_at"), SQLDataType.LOCALDATETIME(6).defaultValue(DSL.field(DSL.raw("NULL"), SQLDataType.LOCALDATETIME)), this, "");

    /**
     * The column <code>medihub.anonymous_board.deleted_at</code>.
     */
    public final TableField<AnonymousBoardRecord, LocalDateTime> DELETED_AT = createField(DSL.name("deleted_at"), SQLDataType.LOCALDATETIME(6).defaultValue(DSL.field(DSL.raw("NULL"), SQLDataType.LOCALDATETIME)), this, "");

    /**
     * The column <code>medihub.anonymous_board.updated_at</code>.
     */
    public final TableField<AnonymousBoardRecord, LocalDateTime> UPDATED_AT = createField(DSL.name("updated_at"), SQLDataType.LOCALDATETIME(6).defaultValue(DSL.field(DSL.raw("NULL"), SQLDataType.LOCALDATETIME)), this, "");

    /**
     * The column <code>medihub.anonymous_board.anonymous_board_content</code>.
     */
    public final TableField<AnonymousBoardRecord, String> ANONYMOUS_BOARD_CONTENT = createField(DSL.name("anonymous_board_content"), SQLDataType.CLOB.defaultValue(DSL.field(DSL.raw("NULL"), SQLDataType.CLOB)), this, "");

    /**
     * The column
     * <code>medihub.anonymous_board.anonymous_board_is_deleted</code>.
     */
    public final TableField<AnonymousBoardRecord, Boolean> ANONYMOUS_BOARD_IS_DELETED = createField(DSL.name("anonymous_board_is_deleted"), SQLDataType.BIT.defaultValue(DSL.field(DSL.raw("NULL"), SQLDataType.BIT)), this, "");

    /**
     * The column <code>medihub.anonymous_board.anonymous_board_title</code>.
     */
    public final TableField<AnonymousBoardRecord, String> ANONYMOUS_BOARD_TITLE = createField(DSL.name("anonymous_board_title"), SQLDataType.VARCHAR(255).defaultValue(DSL.field(DSL.raw("NULL"), SQLDataType.VARCHAR)), this, "");

    /**
     * The column
     * <code>medihub.anonymous_board.anonymous_board_view_count</code>.
     */
    public final TableField<AnonymousBoardRecord, Long> ANONYMOUS_BOARD_VIEW_COUNT = createField(DSL.name("anonymous_board_view_count"), SQLDataType.BIGINT.defaultValue(DSL.field(DSL.raw("NULL"), SQLDataType.BIGINT)), this, "");

    /**
     * The column <code>medihub.anonymous_board.user_seq</code>.
     */
    public final TableField<AnonymousBoardRecord, Long> USER_SEQ = createField(DSL.name("user_seq"), SQLDataType.BIGINT.nullable(false), this, "");

    private JAnonymousBoard(Name alias, Table<AnonymousBoardRecord> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private JAnonymousBoard(Name alias, Table<AnonymousBoardRecord> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table(), where);
    }

    /**
     * Create an aliased <code>medihub.anonymous_board</code> table reference
     */
    public JAnonymousBoard(String alias) {
        this(DSL.name(alias), ANONYMOUS_BOARD);
    }

    /**
     * Create an aliased <code>medihub.anonymous_board</code> table reference
     */
    public JAnonymousBoard(Name alias) {
        this(alias, ANONYMOUS_BOARD);
    }

    /**
     * Create a <code>medihub.anonymous_board</code> table reference
     */
    public JAnonymousBoard() {
        this(DSL.name("anonymous_board"), null);
    }

    public <O extends Record> JAnonymousBoard(Table<O> path, ForeignKey<O, AnonymousBoardRecord> childPath, InverseForeignKey<O, AnonymousBoardRecord> parentPath) {
        super(path, childPath, parentPath, ANONYMOUS_BOARD);
    }

    /**
     * A subtype implementing {@link Path} for simplified path-based joins.
     */
    public static class AnonymousBoardPath extends JAnonymousBoard implements Path<AnonymousBoardRecord> {

        private static final long serialVersionUID = 1L;
        public <O extends Record> AnonymousBoardPath(Table<O> path, ForeignKey<O, AnonymousBoardRecord> childPath, InverseForeignKey<O, AnonymousBoardRecord> parentPath) {
            super(path, childPath, parentPath);
        }
        private AnonymousBoardPath(Name alias, Table<AnonymousBoardRecord> aliased) {
            super(alias, aliased);
        }

        @Override
        public AnonymousBoardPath as(String alias) {
            return new AnonymousBoardPath(DSL.name(alias), this);
        }

        @Override
        public AnonymousBoardPath as(Name alias) {
            return new AnonymousBoardPath(alias, this);
        }

        @Override
        public AnonymousBoardPath as(Table<?> alias) {
            return new AnonymousBoardPath(alias.getQualifiedName(), this);
        }
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : JMedihub.MEDIHUB;
    }

    @Override
    public Identity<AnonymousBoardRecord, Long> getIdentity() {
        return (Identity<AnonymousBoardRecord, Long>) super.getIdentity();
    }

    @Override
    public UniqueKey<AnonymousBoardRecord> getPrimaryKey() {
        return Keys.KEY_ANONYMOUS_BOARD_PRIMARY;
    }

    @Override
    public List<ForeignKey<AnonymousBoardRecord, ?>> getReferences() {
        return Arrays.asList(Keys.FKP7MW3D7RB4HHIX2R1A3LQXA0O);
    }

    private transient UserPath _user;

    /**
     * Get the implicit join path to the <code>medihub.user</code> table.
     */
    public UserPath user() {
        if (_user == null)
            _user = new UserPath(this, Keys.FKP7MW3D7RB4HHIX2R1A3LQXA0O, null);

        return _user;
    }

    @Override
    public JAnonymousBoard as(String alias) {
        return new JAnonymousBoard(DSL.name(alias), this);
    }

    @Override
    public JAnonymousBoard as(Name alias) {
        return new JAnonymousBoard(alias, this);
    }

    @Override
    public JAnonymousBoard as(Table<?> alias) {
        return new JAnonymousBoard(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public JAnonymousBoard rename(String name) {
        return new JAnonymousBoard(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public JAnonymousBoard rename(Name name) {
        return new JAnonymousBoard(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public JAnonymousBoard rename(Table<?> name) {
        return new JAnonymousBoard(name.getQualifiedName(), null);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public JAnonymousBoard where(Condition condition) {
        return new JAnonymousBoard(getQualifiedName(), aliased() ? this : null, null, condition);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public JAnonymousBoard where(Collection<? extends Condition> conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public JAnonymousBoard where(Condition... conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public JAnonymousBoard where(Field<Boolean> condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public JAnonymousBoard where(SQL condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public JAnonymousBoard where(@Stringly.SQL String condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public JAnonymousBoard where(@Stringly.SQL String condition, Object... binds) {
        return where(DSL.condition(condition, binds));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public JAnonymousBoard where(@Stringly.SQL String condition, QueryPart... parts) {
        return where(DSL.condition(condition, parts));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public JAnonymousBoard whereExists(Select<?> select) {
        return where(DSL.exists(select));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public JAnonymousBoard whereNotExists(Select<?> select) {
        return where(DSL.notExists(select));
    }
}
