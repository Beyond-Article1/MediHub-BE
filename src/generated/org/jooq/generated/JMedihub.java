/*
 * This file is generated by jOOQ.
 */
package org.jooq.generated;


import java.util.Arrays;
import java.util.List;

import org.jooq.Catalog;
import org.jooq.Table;
import org.jooq.generated.tables.JAnonymousBoard;
import org.jooq.generated.tables.JBookmark;
import org.jooq.generated.tables.JCaseSharing;
import org.jooq.generated.tables.JCaseSharingComment;
import org.jooq.generated.tables.JCaseSharingGroup;
import org.jooq.generated.tables.JChat;
import org.jooq.generated.tables.JChatroom;
import org.jooq.generated.tables.JComment;
import org.jooq.generated.tables.JCp;
import org.jooq.generated.tables.JCpOpinion;
import org.jooq.generated.tables.JCpOpinionLocation;
import org.jooq.generated.tables.JCpOpinionVote;
import org.jooq.generated.tables.JCpSearchCategory;
import org.jooq.generated.tables.JCpSearchCategoryData;
import org.jooq.generated.tables.JCpSearchData;
import org.jooq.generated.tables.JCpVersion;
import org.jooq.generated.tables.JDept;
import org.jooq.generated.tables.JFlag;
import org.jooq.generated.tables.JFollow;
import org.jooq.generated.tables.JJournal;
import org.jooq.generated.tables.JJournalSearch;
import org.jooq.generated.tables.JKeyword;
import org.jooq.generated.tables.JMedicalLife;
import org.jooq.generated.tables.JNotify;
import org.jooq.generated.tables.JPart;
import org.jooq.generated.tables.JPicture;
import org.jooq.generated.tables.JPrefer;
import org.jooq.generated.tables.JRanking;
import org.jooq.generated.tables.JTemplate;
import org.jooq.generated.tables.JUser;
import org.jooq.impl.SchemaImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class JMedihub extends SchemaImpl {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>medihub</code>
     */
    public static final JMedihub MEDIHUB = new JMedihub();

    /**
     * The table <code>medihub.anonymous_board</code>.
     */
    public final JAnonymousBoard ANONYMOUS_BOARD = JAnonymousBoard.ANONYMOUS_BOARD;

    /**
     * The table <code>medihub.bookmark</code>.
     */
    public final JBookmark BOOKMARK = JBookmark.BOOKMARK;

    /**
     * The table <code>medihub.case_sharing</code>.
     */
    public final JCaseSharing CASE_SHARING = JCaseSharing.CASE_SHARING;

    /**
     * The table <code>medihub.case_sharing_comment</code>.
     */
    public final JCaseSharingComment CASE_SHARING_COMMENT = JCaseSharingComment.CASE_SHARING_COMMENT;

    /**
     * The table <code>medihub.case_sharing_group</code>.
     */
    public final JCaseSharingGroup CASE_SHARING_GROUP = JCaseSharingGroup.CASE_SHARING_GROUP;

    /**
     * The table <code>medihub.chat</code>.
     */
    public final JChat CHAT = JChat.CHAT;

    /**
     * The table <code>medihub.chatroom</code>.
     */
    public final JChatroom CHATROOM = JChatroom.CHATROOM;

    /**
     * The table <code>medihub.comment</code>.
     */
    public final JComment COMMENT = JComment.COMMENT;

    /**
     * The table <code>medihub.cp</code>.
     */
    public final JCp CP = JCp.CP;

    /**
     * The table <code>medihub.cp_opinion</code>.
     */
    public final JCpOpinion CP_OPINION = JCpOpinion.CP_OPINION;

    /**
     * The table <code>medihub.cp_opinion_location</code>.
     */
    public final JCpOpinionLocation CP_OPINION_LOCATION = JCpOpinionLocation.CP_OPINION_LOCATION;

    /**
     * The table <code>medihub.cp_opinion_vote</code>.
     */
    public final JCpOpinionVote CP_OPINION_VOTE = JCpOpinionVote.CP_OPINION_VOTE;

    /**
     * The table <code>medihub.cp_search_category</code>.
     */
    public final JCpSearchCategory CP_SEARCH_CATEGORY = JCpSearchCategory.CP_SEARCH_CATEGORY;

    /**
     * The table <code>medihub.cp_search_category_data</code>.
     */
    public final JCpSearchCategoryData CP_SEARCH_CATEGORY_DATA = JCpSearchCategoryData.CP_SEARCH_CATEGORY_DATA;

    /**
     * The table <code>medihub.cp_search_data</code>.
     */
    public final JCpSearchData CP_SEARCH_DATA = JCpSearchData.CP_SEARCH_DATA;

    /**
     * The table <code>medihub.cp_version</code>.
     */
    public final JCpVersion CP_VERSION = JCpVersion.CP_VERSION;

    /**
     * The table <code>medihub.dept</code>.
     */
    public final JDept DEPT = JDept.DEPT;

    /**
     * The table <code>medihub.flag</code>.
     */
    public final JFlag FLAG = JFlag.FLAG;

    /**
     * The table <code>medihub.follow</code>.
     */
    public final JFollow FOLLOW = JFollow.FOLLOW;

    /**
     * The table <code>medihub.journal</code>.
     */
    public final JJournal JOURNAL = JJournal.JOURNAL;

    /**
     * The table <code>medihub.journal_search</code>.
     */
    public final JJournalSearch JOURNAL_SEARCH = JJournalSearch.JOURNAL_SEARCH;

    /**
     * The table <code>medihub.keyword</code>.
     */
    public final JKeyword KEYWORD = JKeyword.KEYWORD;

    /**
     * The table <code>medihub.medical_life</code>.
     */
    public final JMedicalLife MEDICAL_LIFE = JMedicalLife.MEDICAL_LIFE;

    /**
     * The table <code>medihub.notify</code>.
     */
    public final JNotify NOTIFY = JNotify.NOTIFY;

    /**
     * The table <code>medihub.part</code>.
     */
    public final JPart PART = JPart.PART;

    /**
     * The table <code>medihub.picture</code>.
     */
    public final JPicture PICTURE = JPicture.PICTURE;

    /**
     * The table <code>medihub.prefer</code>.
     */
    public final JPrefer PREFER = JPrefer.PREFER;

    /**
     * The table <code>medihub.ranking</code>.
     */
    public final JRanking RANKING = JRanking.RANKING;

    /**
     * The table <code>medihub.template</code>.
     */
    public final JTemplate TEMPLATE = JTemplate.TEMPLATE;

    /**
     * The table <code>medihub.user</code>.
     */
    public final JUser USER = JUser.USER;

    /**
     * No further instances allowed
     */
    private JMedihub() {
        super("medihub", null);
    }


    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<Table<?>> getTables() {
        return Arrays.asList(
            JAnonymousBoard.ANONYMOUS_BOARD,
            JBookmark.BOOKMARK,
            JCaseSharing.CASE_SHARING,
            JCaseSharingComment.CASE_SHARING_COMMENT,
            JCaseSharingGroup.CASE_SHARING_GROUP,
            JChat.CHAT,
            JChatroom.CHATROOM,
            JComment.COMMENT,
            JCp.CP,
            JCpOpinion.CP_OPINION,
            JCpOpinionLocation.CP_OPINION_LOCATION,
            JCpOpinionVote.CP_OPINION_VOTE,
            JCpSearchCategory.CP_SEARCH_CATEGORY,
            JCpSearchCategoryData.CP_SEARCH_CATEGORY_DATA,
            JCpSearchData.CP_SEARCH_DATA,
            JCpVersion.CP_VERSION,
            JDept.DEPT,
            JFlag.FLAG,
            JFollow.FOLLOW,
            JJournal.JOURNAL,
            JJournalSearch.JOURNAL_SEARCH,
            JKeyword.KEYWORD,
            JMedicalLife.MEDICAL_LIFE,
            JNotify.NOTIFY,
            JPart.PART,
            JPicture.PICTURE,
            JPrefer.PREFER,
            JRanking.RANKING,
            JTemplate.TEMPLATE,
            JUser.USER
        );
    }
}
