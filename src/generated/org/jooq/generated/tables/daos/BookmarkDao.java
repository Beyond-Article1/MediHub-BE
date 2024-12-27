/*
 * This file is generated by jOOQ.
 */
package org.jooq.generated.tables.daos;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.jooq.Configuration;
import org.jooq.generated.tables.Bookmark;
import org.jooq.generated.tables.records.BookmarkRecord;
import org.jooq.impl.DAOImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class BookmarkDao extends DAOImpl<BookmarkRecord, org.jooq.generated.tables.pojos.Bookmark, Long> {

    /**
     * Create a new BookmarkDao without any configuration
     */
    public BookmarkDao() {
        super(Bookmark.BOOKMARK, org.jooq.generated.tables.pojos.Bookmark.class);
    }

    /**
     * Create a new BookmarkDao with an attached configuration
     */
    public BookmarkDao(Configuration configuration) {
        super(Bookmark.BOOKMARK, org.jooq.generated.tables.pojos.Bookmark.class, configuration);
    }

    @Override
    public Long getId(org.jooq.generated.tables.pojos.Bookmark object) {
        return object.getBookmarkSeq();
    }

    /**
     * Fetch records that have <code>bookmark_seq BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.jooq.generated.tables.pojos.Bookmark> fetchRangeOfBookmarkSeq(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(Bookmark.BOOKMARK.BOOKMARK_SEQ, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>bookmark_seq IN (values)</code>
     */
    public List<org.jooq.generated.tables.pojos.Bookmark> fetchByBookmarkSeq(Long... values) {
        return fetch(Bookmark.BOOKMARK.BOOKMARK_SEQ, values);
    }

    /**
     * Fetch a unique record that has <code>bookmark_seq = value</code>
     */
    public org.jooq.generated.tables.pojos.Bookmark fetchOneByBookmarkSeq(Long value) {
        return fetchOne(Bookmark.BOOKMARK.BOOKMARK_SEQ, value);
    }

    /**
     * Fetch a unique record that has <code>bookmark_seq = value</code>
     */
    public Optional<org.jooq.generated.tables.pojos.Bookmark> fetchOptionalByBookmarkSeq(Long value) {
        return fetchOptional(Bookmark.BOOKMARK.BOOKMARK_SEQ, value);
    }

    /**
     * Fetch records that have <code>created_at BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.jooq.generated.tables.pojos.Bookmark> fetchRangeOfCreatedAt(LocalDateTime lowerInclusive, LocalDateTime upperInclusive) {
        return fetchRange(Bookmark.BOOKMARK.CREATED_AT, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>created_at IN (values)</code>
     */
    public List<org.jooq.generated.tables.pojos.Bookmark> fetchByCreatedAt(LocalDateTime... values) {
        return fetch(Bookmark.BOOKMARK.CREATED_AT, values);
    }

    /**
     * Fetch records that have <code>flag_seq BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.jooq.generated.tables.pojos.Bookmark> fetchRangeOfFlagSeq(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(Bookmark.BOOKMARK.FLAG_SEQ, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>flag_seq IN (values)</code>
     */
    public List<org.jooq.generated.tables.pojos.Bookmark> fetchByFlagSeq(Long... values) {
        return fetch(Bookmark.BOOKMARK.FLAG_SEQ, values);
    }

    /**
     * Fetch records that have <code>user_seq BETWEEN lowerInclusive AND
     * upperInclusive</code>
     */
    public List<org.jooq.generated.tables.pojos.Bookmark> fetchRangeOfUserSeq(Long lowerInclusive, Long upperInclusive) {
        return fetchRange(Bookmark.BOOKMARK.USER_SEQ, lowerInclusive, upperInclusive);
    }

    /**
     * Fetch records that have <code>user_seq IN (values)</code>
     */
    public List<org.jooq.generated.tables.pojos.Bookmark> fetchByUserSeq(Long... values) {
        return fetch(Bookmark.BOOKMARK.USER_SEQ, values);
    }
}
