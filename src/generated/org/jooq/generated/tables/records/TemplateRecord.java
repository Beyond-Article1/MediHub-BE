/*
 * This file is generated by jOOQ.
 */
package org.jooq.generated.tables.records;


import java.time.LocalDateTime;

import org.jooq.Record1;
import org.jooq.generated.enums.TemplateTemplateOpenScope;
import org.jooq.generated.tables.JTemplate;
import org.jooq.generated.tables.pojos.Template;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TemplateRecord extends UpdatableRecordImpl<TemplateRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>medihub.template.template_seq</code>.
     */
    public TemplateRecord setTemplateSeq(Long value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>medihub.template.template_seq</code>.
     */
    public Long getTemplateSeq() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>medihub.template.created_at</code>.
     */
    public TemplateRecord setCreatedAt(LocalDateTime value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>medihub.template.created_at</code>.
     */
    public LocalDateTime getCreatedAt() {
        return (LocalDateTime) get(1);
    }

    /**
     * Setter for <code>medihub.template.deleted_at</code>.
     */
    public TemplateRecord setDeletedAt(LocalDateTime value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>medihub.template.deleted_at</code>.
     */
    public LocalDateTime getDeletedAt() {
        return (LocalDateTime) get(2);
    }

    /**
     * Setter for <code>medihub.template.updated_at</code>.
     */
    public TemplateRecord setUpdatedAt(LocalDateTime value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>medihub.template.updated_at</code>.
     */
    public LocalDateTime getUpdatedAt() {
        return (LocalDateTime) get(3);
    }

    /**
     * Setter for <code>medihub.template.template_open_scope</code>.
     */
    public TemplateRecord setTemplateOpenScope(TemplateTemplateOpenScope value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>medihub.template.template_open_scope</code>.
     */
    public TemplateTemplateOpenScope getTemplateOpenScope() {
        return (TemplateTemplateOpenScope) get(4);
    }

    /**
     * Setter for <code>medihub.template.template_content</code>.
     */
    public TemplateRecord setTemplateContent(String value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>medihub.template.template_content</code>.
     */
    public String getTemplateContent() {
        return (String) get(5);
    }

    /**
     * Setter for <code>medihub.template.template_title</code>.
     */
    public TemplateRecord setTemplateTitle(String value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for <code>medihub.template.template_title</code>.
     */
    public String getTemplateTitle() {
        return (String) get(6);
    }

    /**
     * Setter for <code>medihub.template.part_seq</code>.
     */
    public TemplateRecord setPartSeq(Long value) {
        set(7, value);
        return this;
    }

    /**
     * Getter for <code>medihub.template.part_seq</code>.
     */
    public Long getPartSeq() {
        return (Long) get(7);
    }

    /**
     * Setter for <code>medihub.template.user_seq</code>.
     */
    public TemplateRecord setUserSeq(Long value) {
        set(8, value);
        return this;
    }

    /**
     * Getter for <code>medihub.template.user_seq</code>.
     */
    public Long getUserSeq() {
        return (Long) get(8);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached TemplateRecord
     */
    public TemplateRecord() {
        super(JTemplate.TEMPLATE);
    }

    /**
     * Create a detached, initialised TemplateRecord
     */
    public TemplateRecord(Long templateSeq, LocalDateTime createdAt, LocalDateTime deletedAt, LocalDateTime updatedAt, TemplateTemplateOpenScope templateOpenScope, String templateContent, String templateTitle, Long partSeq, Long userSeq) {
        super(JTemplate.TEMPLATE);

        setTemplateSeq(templateSeq);
        setCreatedAt(createdAt);
        setDeletedAt(deletedAt);
        setUpdatedAt(updatedAt);
        setTemplateOpenScope(templateOpenScope);
        setTemplateContent(templateContent);
        setTemplateTitle(templateTitle);
        setPartSeq(partSeq);
        setUserSeq(userSeq);
        resetChangedOnNotNull();
    }

    /**
     * Create a detached, initialised TemplateRecord
     */
    public TemplateRecord(Template value) {
        super(JTemplate.TEMPLATE);

        if (value != null) {
            setTemplateSeq(value.getTemplateSeq());
            setCreatedAt(value.getCreatedAt());
            setDeletedAt(value.getDeletedAt());
            setUpdatedAt(value.getUpdatedAt());
            setTemplateOpenScope(value.getTemplateOpenScope());
            setTemplateContent(value.getTemplateContent());
            setTemplateTitle(value.getTemplateTitle());
            setPartSeq(value.getPartSeq());
            setUserSeq(value.getUserSeq());
            resetChangedOnNotNull();
        }
    }
}