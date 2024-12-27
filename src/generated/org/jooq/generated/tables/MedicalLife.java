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
import org.jooq.generated.Keys;
import org.jooq.generated.Medihub;
import org.jooq.generated.tables.User.UserPath;
import org.jooq.generated.tables.records.MedicalLifeRecord;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class MedicalLife extends TableImpl<MedicalLifeRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>medihub.medical_life</code>
     */
    public static final MedicalLife MEDICAL_LIFE = new MedicalLife();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<MedicalLifeRecord> getRecordType() {
        return MedicalLifeRecord.class;
    }

    /**
     * The column <code>medihub.medical_life.medical_life_seq</code>.
     */
    public final TableField<MedicalLifeRecord, Long> MEDICAL_LIFE_SEQ = createField(DSL.name("medical_life_seq"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>medihub.medical_life.created_at</code>.
     */
    public final TableField<MedicalLifeRecord, LocalDateTime> CREATED_AT = createField(DSL.name("created_at"), SQLDataType.LOCALDATETIME(6).defaultValue(DSL.field(DSL.raw("NULL"), SQLDataType.LOCALDATETIME)), this, "");

    /**
     * The column <code>medihub.medical_life.deleted_at</code>.
     */
    public final TableField<MedicalLifeRecord, LocalDateTime> DELETED_AT = createField(DSL.name("deleted_at"), SQLDataType.LOCALDATETIME(6).defaultValue(DSL.field(DSL.raw("NULL"), SQLDataType.LOCALDATETIME)), this, "");

    /**
     * The column <code>medihub.medical_life.updated_at</code>.
     */
    public final TableField<MedicalLifeRecord, LocalDateTime> UPDATED_AT = createField(DSL.name("updated_at"), SQLDataType.LOCALDATETIME(6).defaultValue(DSL.field(DSL.raw("NULL"), SQLDataType.LOCALDATETIME)), this, "");

    /**
     * The column <code>medihub.medical_life.medical_life_content</code>.
     */
    public final TableField<MedicalLifeRecord, String> MEDICAL_LIFE_CONTENT = createField(DSL.name("medical_life_content"), SQLDataType.CLOB.nullable(false), this, "");

    /**
     * The column <code>medihub.medical_life.medical_life_is_deleted</code>.
     */
    public final TableField<MedicalLifeRecord, Boolean> MEDICAL_LIFE_IS_DELETED = createField(DSL.name("medical_life_is_deleted"), SQLDataType.BIT.nullable(false), this, "");

    /**
     * The column <code>medihub.medical_life.medical_life_title</code>.
     */
    public final TableField<MedicalLifeRecord, String> MEDICAL_LIFE_TITLE = createField(DSL.name("medical_life_title"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>medihub.medical_life.medical_life_view_count</code>.
     */
    public final TableField<MedicalLifeRecord, Long> MEDICAL_LIFE_VIEW_COUNT = createField(DSL.name("medical_life_view_count"), SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>medihub.medical_life.user_seq</code>.
     */
    public final TableField<MedicalLifeRecord, Long> USER_SEQ = createField(DSL.name("user_seq"), SQLDataType.BIGINT.nullable(false), this, "");

    private MedicalLife(Name alias, Table<MedicalLifeRecord> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private MedicalLife(Name alias, Table<MedicalLifeRecord> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table(), where);
    }

    /**
     * Create an aliased <code>medihub.medical_life</code> table reference
     */
    public MedicalLife(String alias) {
        this(DSL.name(alias), MEDICAL_LIFE);
    }

    /**
     * Create an aliased <code>medihub.medical_life</code> table reference
     */
    public MedicalLife(Name alias) {
        this(alias, MEDICAL_LIFE);
    }

    /**
     * Create a <code>medihub.medical_life</code> table reference
     */
    public MedicalLife() {
        this(DSL.name("medical_life"), null);
    }

    public <O extends Record> MedicalLife(Table<O> path, ForeignKey<O, MedicalLifeRecord> childPath, InverseForeignKey<O, MedicalLifeRecord> parentPath) {
        super(path, childPath, parentPath, MEDICAL_LIFE);
    }

    /**
     * A subtype implementing {@link Path} for simplified path-based joins.
     */
    public static class MedicalLifePath extends MedicalLife implements Path<MedicalLifeRecord> {

        private static final long serialVersionUID = 1L;
        public <O extends Record> MedicalLifePath(Table<O> path, ForeignKey<O, MedicalLifeRecord> childPath, InverseForeignKey<O, MedicalLifeRecord> parentPath) {
            super(path, childPath, parentPath);
        }
        private MedicalLifePath(Name alias, Table<MedicalLifeRecord> aliased) {
            super(alias, aliased);
        }

        @Override
        public MedicalLifePath as(String alias) {
            return new MedicalLifePath(DSL.name(alias), this);
        }

        @Override
        public MedicalLifePath as(Name alias) {
            return new MedicalLifePath(alias, this);
        }

        @Override
        public MedicalLifePath as(Table<?> alias) {
            return new MedicalLifePath(alias.getQualifiedName(), this);
        }
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Medihub.MEDIHUB;
    }

    @Override
    public Identity<MedicalLifeRecord, Long> getIdentity() {
        return (Identity<MedicalLifeRecord, Long>) super.getIdentity();
    }

    @Override
    public UniqueKey<MedicalLifeRecord> getPrimaryKey() {
        return Keys.KEY_MEDICAL_LIFE_PRIMARY;
    }

    @Override
    public List<ForeignKey<MedicalLifeRecord, ?>> getReferences() {
        return Arrays.asList(Keys.FK4IXM94NJ6NXCMNOOWL0WS8IOV);
    }

    private transient UserPath _user;

    /**
     * Get the implicit join path to the <code>medihub.user</code> table.
     */
    public UserPath user() {
        if (_user == null)
            _user = new UserPath(this, Keys.FK4IXM94NJ6NXCMNOOWL0WS8IOV, null);

        return _user;
    }

    @Override
    public MedicalLife as(String alias) {
        return new MedicalLife(DSL.name(alias), this);
    }

    @Override
    public MedicalLife as(Name alias) {
        return new MedicalLife(alias, this);
    }

    @Override
    public MedicalLife as(Table<?> alias) {
        return new MedicalLife(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public MedicalLife rename(String name) {
        return new MedicalLife(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public MedicalLife rename(Name name) {
        return new MedicalLife(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public MedicalLife rename(Table<?> name) {
        return new MedicalLife(name.getQualifiedName(), null);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public MedicalLife where(Condition condition) {
        return new MedicalLife(getQualifiedName(), aliased() ? this : null, null, condition);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public MedicalLife where(Collection<? extends Condition> conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public MedicalLife where(Condition... conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public MedicalLife where(Field<Boolean> condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public MedicalLife where(SQL condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public MedicalLife where(@Stringly.SQL String condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public MedicalLife where(@Stringly.SQL String condition, Object... binds) {
        return where(DSL.condition(condition, binds));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public MedicalLife where(@Stringly.SQL String condition, QueryPart... parts) {
        return where(DSL.condition(condition, parts));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public MedicalLife whereExists(Select<?> select) {
        return where(DSL.exists(select));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public MedicalLife whereNotExists(Select<?> select) {
        return where(DSL.notExists(select));
    }
}
