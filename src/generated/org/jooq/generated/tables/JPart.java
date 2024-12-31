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
import org.jooq.generated.tables.JCaseSharing.CaseSharingPath;
import org.jooq.generated.tables.JDept.DeptPath;
import org.jooq.generated.tables.JTemplate.TemplatePath;
import org.jooq.generated.tables.JUser.UserPath;
import org.jooq.generated.tables.records.PartRecord;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class JPart extends TableImpl<PartRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>medihub.part</code>
     */
    public static final JPart PART = new JPart();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<PartRecord> getRecordType() {
        return PartRecord.class;
    }

    /**
     * The column <code>medihub.part.part_seq</code>.
     */
    public final TableField<PartRecord, Long> PART_SEQ = createField(DSL.name("part_seq"), SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>medihub.part.created_at</code>.
     */
    public final TableField<PartRecord, LocalDateTime> CREATED_AT = createField(DSL.name("created_at"), SQLDataType.LOCALDATETIME(6).defaultValue(DSL.field(DSL.raw("NULL"), SQLDataType.LOCALDATETIME)), this, "");

    /**
     * The column <code>medihub.part.deleted_at</code>.
     */
    public final TableField<PartRecord, LocalDateTime> DELETED_AT = createField(DSL.name("deleted_at"), SQLDataType.LOCALDATETIME(6).defaultValue(DSL.field(DSL.raw("NULL"), SQLDataType.LOCALDATETIME)), this, "");

    /**
     * The column <code>medihub.part.updated_at</code>.
     */
    public final TableField<PartRecord, LocalDateTime> UPDATED_AT = createField(DSL.name("updated_at"), SQLDataType.LOCALDATETIME(6).defaultValue(DSL.field(DSL.raw("NULL"), SQLDataType.LOCALDATETIME)), this, "");

    /**
     * The column <code>medihub.part.part_name</code>.
     */
    public final TableField<PartRecord, String> PART_NAME = createField(DSL.name("part_name"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>medihub.part.dept_seq</code>.
     */
    public final TableField<PartRecord, Long> DEPT_SEQ = createField(DSL.name("dept_seq"), SQLDataType.BIGINT.nullable(false), this, "");

    private JPart(Name alias, Table<PartRecord> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private JPart(Name alias, Table<PartRecord> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table(), where);
    }

    /**
     * Create an aliased <code>medihub.part</code> table reference
     */
    public JPart(String alias) {
        this(DSL.name(alias), PART);
    }

    /**
     * Create an aliased <code>medihub.part</code> table reference
     */
    public JPart(Name alias) {
        this(alias, PART);
    }

    /**
     * Create a <code>medihub.part</code> table reference
     */
    public JPart() {
        this(DSL.name("part"), null);
    }

    public <O extends Record> JPart(Table<O> path, ForeignKey<O, PartRecord> childPath, InverseForeignKey<O, PartRecord> parentPath) {
        super(path, childPath, parentPath, PART);
    }

    /**
     * A subtype implementing {@link Path} for simplified path-based joins.
     */
    public static class PartPath extends JPart implements Path<PartRecord> {

        private static final long serialVersionUID = 1L;
        public <O extends Record> PartPath(Table<O> path, ForeignKey<O, PartRecord> childPath, InverseForeignKey<O, PartRecord> parentPath) {
            super(path, childPath, parentPath);
        }
        private PartPath(Name alias, Table<PartRecord> aliased) {
            super(alias, aliased);
        }

        @Override
        public PartPath as(String alias) {
            return new PartPath(DSL.name(alias), this);
        }

        @Override
        public PartPath as(Name alias) {
            return new PartPath(alias, this);
        }

        @Override
        public PartPath as(Table<?> alias) {
            return new PartPath(alias.getQualifiedName(), this);
        }
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : JMedihub.MEDIHUB;
    }

    @Override
    public Identity<PartRecord, Long> getIdentity() {
        return (Identity<PartRecord, Long>) super.getIdentity();
    }

    @Override
    public UniqueKey<PartRecord> getPrimaryKey() {
        return Keys.KEY_PART_PRIMARY;
    }

    @Override
    public List<ForeignKey<PartRecord, ?>> getReferences() {
        return Arrays.asList(Keys.FK2EOOGX1T34AWD7KBJCL88TVJ0);
    }

    private transient DeptPath _dept;

    /**
     * Get the implicit join path to the <code>medihub.dept</code> table.
     */
    public DeptPath dept() {
        if (_dept == null)
            _dept = new DeptPath(this, Keys.FK2EOOGX1T34AWD7KBJCL88TVJ0, null);

        return _dept;
    }

    private transient CaseSharingPath _caseSharing;

    /**
     * Get the implicit to-many join path to the
     * <code>medihub.case_sharing</code> table
     */
    public CaseSharingPath caseSharing() {
        if (_caseSharing == null)
            _caseSharing = new CaseSharingPath(this, null, Keys.FKNE3TL9WBDYCGY68HPVH9F58O1.getInverseKey());

        return _caseSharing;
    }

    private transient UserPath _user;

    /**
     * Get the implicit to-many join path to the <code>medihub.user</code> table
     */
    public UserPath user() {
        if (_user == null)
            _user = new UserPath(this, null, Keys.FKO1YYRBI2OTTUXO2ROHOV35S8Q.getInverseKey());

        return _user;
    }

    private transient TemplatePath _template;

    /**
     * Get the implicit to-many join path to the <code>medihub.template</code>
     * table
     */
    public TemplatePath template() {
        if (_template == null)
            _template = new TemplatePath(this, null, Keys.FKRIQUJL7WVKJ4TPVOOFRQ67OUI.getInverseKey());

        return _template;
    }

    @Override
    public JPart as(String alias) {
        return new JPart(DSL.name(alias), this);
    }

    @Override
    public JPart as(Name alias) {
        return new JPart(alias, this);
    }

    @Override
    public JPart as(Table<?> alias) {
        return new JPart(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public JPart rename(String name) {
        return new JPart(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public JPart rename(Name name) {
        return new JPart(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public JPart rename(Table<?> name) {
        return new JPart(name.getQualifiedName(), null);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public JPart where(Condition condition) {
        return new JPart(getQualifiedName(), aliased() ? this : null, null, condition);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public JPart where(Collection<? extends Condition> conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public JPart where(Condition... conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public JPart where(Field<Boolean> condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public JPart where(SQL condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public JPart where(@Stringly.SQL String condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public JPart where(@Stringly.SQL String condition, Object... binds) {
        return where(DSL.condition(condition, binds));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public JPart where(@Stringly.SQL String condition, QueryPart... parts) {
        return where(DSL.condition(condition, parts));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public JPart whereExists(Select<?> select) {
        return where(DSL.exists(select));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public JPart whereNotExists(Select<?> select) {
        return where(DSL.notExists(select));
    }
}
