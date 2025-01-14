/*
 * This file is generated by jOOQ.
 */
package org.jooq.generated;


import org.jooq.ForeignKey;
import org.jooq.TableField;
import org.jooq.UniqueKey;
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
import org.jooq.generated.tables.records.AnonymousBoardRecord;
import org.jooq.generated.tables.records.BookmarkRecord;
import org.jooq.generated.tables.records.CaseSharingCommentRecord;
import org.jooq.generated.tables.records.CaseSharingGroupRecord;
import org.jooq.generated.tables.records.CaseSharingRecord;
import org.jooq.generated.tables.records.ChatRecord;
import org.jooq.generated.tables.records.ChatroomRecord;
import org.jooq.generated.tables.records.CommentRecord;
import org.jooq.generated.tables.records.CpOpinionLocationRecord;
import org.jooq.generated.tables.records.CpOpinionRecord;
import org.jooq.generated.tables.records.CpOpinionVoteRecord;
import org.jooq.generated.tables.records.CpRecord;
import org.jooq.generated.tables.records.CpSearchCategoryDataRecord;
import org.jooq.generated.tables.records.CpSearchCategoryRecord;
import org.jooq.generated.tables.records.CpSearchDataRecord;
import org.jooq.generated.tables.records.CpVersionRecord;
import org.jooq.generated.tables.records.DeptRecord;
import org.jooq.generated.tables.records.FlagRecord;
import org.jooq.generated.tables.records.FollowRecord;
import org.jooq.generated.tables.records.JournalRecord;
import org.jooq.generated.tables.records.JournalSearchRecord;
import org.jooq.generated.tables.records.KeywordRecord;
import org.jooq.generated.tables.records.MedicalLifeRecord;
import org.jooq.generated.tables.records.NotifyRecord;
import org.jooq.generated.tables.records.PartRecord;
import org.jooq.generated.tables.records.PictureRecord;
import org.jooq.generated.tables.records.PreferRecord;
import org.jooq.generated.tables.records.RankingRecord;
import org.jooq.generated.tables.records.TemplateRecord;
import org.jooq.generated.tables.records.UserRecord;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;


/**
 * A class modelling foreign key relationships and constraints of tables in
 * medihub.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<AnonymousBoardRecord> KEY_ANONYMOUS_BOARD_PRIMARY = Internal.createUniqueKey(JAnonymousBoard.ANONYMOUS_BOARD, DSL.name("KEY_anonymous_board_PRIMARY"), new TableField[] { JAnonymousBoard.ANONYMOUS_BOARD.ANONYMOUS_BOARD_SEQ }, true);
    public static final UniqueKey<BookmarkRecord> KEY_BOOKMARK_PRIMARY = Internal.createUniqueKey(JBookmark.BOOKMARK, DSL.name("KEY_bookmark_PRIMARY"), new TableField[] { JBookmark.BOOKMARK.BOOKMARK_SEQ }, true);
    public static final UniqueKey<CaseSharingRecord> KEY_CASE_SHARING_PRIMARY = Internal.createUniqueKey(JCaseSharing.CASE_SHARING, DSL.name("KEY_case_sharing_PRIMARY"), new TableField[] { JCaseSharing.CASE_SHARING.CASE_SHARING_SEQ }, true);
    public static final UniqueKey<CaseSharingCommentRecord> KEY_CASE_SHARING_COMMENT_PRIMARY = Internal.createUniqueKey(JCaseSharingComment.CASE_SHARING_COMMENT, DSL.name("KEY_case_sharing_comment_PRIMARY"), new TableField[] { JCaseSharingComment.CASE_SHARING_COMMENT.CASE_SHARING_COMMENT_SEQ }, true);
    public static final UniqueKey<CaseSharingGroupRecord> KEY_CASE_SHARING_GROUP_PRIMARY = Internal.createUniqueKey(JCaseSharingGroup.CASE_SHARING_GROUP, DSL.name("KEY_case_sharing_group_PRIMARY"), new TableField[] { JCaseSharingGroup.CASE_SHARING_GROUP.CASE_SHARING_GROUP_SEQ }, true);
    public static final UniqueKey<ChatRecord> KEY_CHAT_PRIMARY = Internal.createUniqueKey(JChat.CHAT, DSL.name("KEY_chat_PRIMARY"), new TableField[] { JChat.CHAT.CHAT_SEQ }, true);
    public static final UniqueKey<ChatroomRecord> KEY_CHATROOM_PRIMARY = Internal.createUniqueKey(JChatroom.CHATROOM, DSL.name("KEY_chatroom_PRIMARY"), new TableField[] { JChatroom.CHATROOM.CHATROOM_SEQ }, true);
    public static final UniqueKey<CommentRecord> KEY_COMMENT_PRIMARY = Internal.createUniqueKey(JComment.COMMENT, DSL.name("KEY_comment_PRIMARY"), new TableField[] { JComment.COMMENT.COMMENT_SEQ }, true);
    public static final UniqueKey<CpRecord> KEY_CP_PRIMARY = Internal.createUniqueKey(JCp.CP, DSL.name("KEY_cp_PRIMARY"), new TableField[] { JCp.CP.CP_SEQ }, true);
    public static final UniqueKey<CpOpinionRecord> KEY_CP_OPINION_PRIMARY = Internal.createUniqueKey(JCpOpinion.CP_OPINION, DSL.name("KEY_cp_opinion_PRIMARY"), new TableField[] { JCpOpinion.CP_OPINION.CP_OPINION_SEQ }, true);
    public static final UniqueKey<CpOpinionLocationRecord> KEY_CP_OPINION_LOCATION_PRIMARY = Internal.createUniqueKey(JCpOpinionLocation.CP_OPINION_LOCATION, DSL.name("KEY_cp_opinion_location_PRIMARY"), new TableField[] { JCpOpinionLocation.CP_OPINION_LOCATION.CP_OPINION_LOCATION_SEQ }, true);
    public static final UniqueKey<CpOpinionVoteRecord> KEY_CP_OPINION_VOTE_PRIMARY = Internal.createUniqueKey(JCpOpinionVote.CP_OPINION_VOTE, DSL.name("KEY_cp_opinion_vote_PRIMARY"), new TableField[] { JCpOpinionVote.CP_OPINION_VOTE.CP_OPINION_VOTE_SEQ }, true);
    public static final UniqueKey<CpSearchCategoryRecord> KEY_CP_SEARCH_CATEGORY_PRIMARY = Internal.createUniqueKey(JCpSearchCategory.CP_SEARCH_CATEGORY, DSL.name("KEY_cp_search_category_PRIMARY"), new TableField[] { JCpSearchCategory.CP_SEARCH_CATEGORY.CP_SEARCH_CATEGORY_SEQ }, true);
    public static final UniqueKey<CpSearchCategoryDataRecord> KEY_CP_SEARCH_CATEGORY_DATA_PRIMARY = Internal.createUniqueKey(JCpSearchCategoryData.CP_SEARCH_CATEGORY_DATA, DSL.name("KEY_cp_search_category_data_PRIMARY"), new TableField[] { JCpSearchCategoryData.CP_SEARCH_CATEGORY_DATA.CP_SEARCH_CATEGORY_DATA_SEQ }, true);
    public static final UniqueKey<CpSearchDataRecord> KEY_CP_SEARCH_DATA_PRIMARY = Internal.createUniqueKey(JCpSearchData.CP_SEARCH_DATA, DSL.name("KEY_cp_search_data_PRIMARY"), new TableField[] { JCpSearchData.CP_SEARCH_DATA.CP_SEARCH_DATA_SEQ }, true);
    public static final UniqueKey<CpVersionRecord> KEY_CP_VERSION_PRIMARY = Internal.createUniqueKey(JCpVersion.CP_VERSION, DSL.name("KEY_cp_version_PRIMARY"), new TableField[] { JCpVersion.CP_VERSION.CP_VERSION_SEQ }, true);
    public static final UniqueKey<DeptRecord> KEY_DEPT_PRIMARY = Internal.createUniqueKey(JDept.DEPT, DSL.name("KEY_dept_PRIMARY"), new TableField[] { JDept.DEPT.DEPT_SEQ }, true);
    public static final UniqueKey<FlagRecord> KEY_FLAG_PRIMARY = Internal.createUniqueKey(JFlag.FLAG, DSL.name("KEY_flag_PRIMARY"), new TableField[] { JFlag.FLAG.FLAG_SEQ }, true);
    public static final UniqueKey<FollowRecord> KEY_FOLLOW_PRIMARY = Internal.createUniqueKey(JFollow.FOLLOW, DSL.name("KEY_follow_PRIMARY"), new TableField[] { JFollow.FOLLOW.FOLLOW_SEQ }, true);
    public static final UniqueKey<JournalRecord> KEY_JOURNAL_PRIMARY = Internal.createUniqueKey(JJournal.JOURNAL, DSL.name("KEY_journal_PRIMARY"), new TableField[] { JJournal.JOURNAL.JOURNAL_SEQ }, true);
    public static final UniqueKey<JournalRecord> KEY_JOURNAL_UK6QDJ06OW81AE53PLROBTBVXIB = Internal.createUniqueKey(JJournal.JOURNAL, DSL.name("KEY_journal_UK6qdj06ow81ae53plrobtbvxib"), new TableField[] { JJournal.JOURNAL.JOURNAL_DOI }, true);
    public static final UniqueKey<JournalRecord> KEY_JOURNAL_UKE41MC38B5A5FTY8JNBR6MW8GG = Internal.createUniqueKey(JJournal.JOURNAL, DSL.name("KEY_journal_UKe41mc38b5a5fty8jnbr6mw8gg"), new TableField[] { JJournal.JOURNAL.JOURNAL_PMID }, true);
    public static final UniqueKey<JournalRecord> KEY_JOURNAL_UKLQNI2RF2OP2M10PX5GX2MTT3A = Internal.createUniqueKey(JJournal.JOURNAL, DSL.name("KEY_journal_UKlqni2rf2op2m10px5gx2mtt3a"), new TableField[] { JJournal.JOURNAL.JOURNAL_TITLE }, true);
    public static final UniqueKey<JournalSearchRecord> KEY_JOURNAL_SEARCH_PRIMARY = Internal.createUniqueKey(JJournalSearch.JOURNAL_SEARCH, DSL.name("KEY_journal_search_PRIMARY"), new TableField[] { JJournalSearch.JOURNAL_SEARCH.JOURNAL_SEARCH_SEQ }, true);
    public static final UniqueKey<KeywordRecord> KEY_KEYWORD_PRIMARY = Internal.createUniqueKey(JKeyword.KEYWORD, DSL.name("KEY_keyword_PRIMARY"), new TableField[] { JKeyword.KEYWORD.KEYWORD_SEQ }, true);
    public static final UniqueKey<MedicalLifeRecord> KEY_MEDICAL_LIFE_PRIMARY = Internal.createUniqueKey(JMedicalLife.MEDICAL_LIFE, DSL.name("KEY_medical_life_PRIMARY"), new TableField[] { JMedicalLife.MEDICAL_LIFE.MEDICAL_LIFE_SEQ }, true);
    public static final UniqueKey<NotifyRecord> KEY_NOTIFY_PRIMARY = Internal.createUniqueKey(JNotify.NOTIFY, DSL.name("KEY_notify_PRIMARY"), new TableField[] { JNotify.NOTIFY.NOTI_SEQ }, true);
    public static final UniqueKey<PartRecord> KEY_PART_PRIMARY = Internal.createUniqueKey(JPart.PART, DSL.name("KEY_part_PRIMARY"), new TableField[] { JPart.PART.PART_SEQ }, true);
    public static final UniqueKey<PictureRecord> KEY_PICTURE_PRIMARY = Internal.createUniqueKey(JPicture.PICTURE, DSL.name("KEY_picture_PRIMARY"), new TableField[] { JPicture.PICTURE.PICTURE_SEQ }, true);
    public static final UniqueKey<PreferRecord> KEY_PREFER_PRIMARY = Internal.createUniqueKey(JPrefer.PREFER, DSL.name("KEY_prefer_PRIMARY"), new TableField[] { JPrefer.PREFER.PREFER_SEQ }, true);
    public static final UniqueKey<RankingRecord> KEY_RANKING_PRIMARY = Internal.createUniqueKey(JRanking.RANKING, DSL.name("KEY_ranking_PRIMARY"), new TableField[] { JRanking.RANKING.RANKING_SEQ }, true);
    public static final UniqueKey<TemplateRecord> KEY_TEMPLATE_PRIMARY = Internal.createUniqueKey(JTemplate.TEMPLATE, DSL.name("KEY_template_PRIMARY"), new TableField[] { JTemplate.TEMPLATE.TEMPLATE_SEQ }, true);
    public static final UniqueKey<UserRecord> KEY_USER_PRIMARY = Internal.createUniqueKey(JUser.USER, DSL.name("KEY_user_PRIMARY"), new TableField[] { JUser.USER.USER_SEQ }, true);
    public static final UniqueKey<UserRecord> KEY_USER_UKA3IMLF41L37UTMXIQUUKK8AJC = Internal.createUniqueKey(JUser.USER, DSL.name("KEY_user_UKa3imlf41l37utmxiquukk8ajc"), new TableField[] { JUser.USER.USER_ID }, true);

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------

    public static final ForeignKey<AnonymousBoardRecord, UserRecord> FKP7MW3D7RB4HHIX2R1A3LQXA0O = Internal.createForeignKey(JAnonymousBoard.ANONYMOUS_BOARD, DSL.name("FKp7mw3d7rb4hhix2r1a3lqxa0o"), new TableField[] { JAnonymousBoard.ANONYMOUS_BOARD.USER_SEQ }, Keys.KEY_USER_PRIMARY, new TableField[] { JUser.USER.USER_SEQ }, true);
    public static final ForeignKey<BookmarkRecord, FlagRecord> FK45D3D2AN1Y5E1391IEJE9BW16 = Internal.createForeignKey(JBookmark.BOOKMARK, DSL.name("FK45d3d2an1y5e1391ieje9bw16"), new TableField[] { JBookmark.BOOKMARK.FLAG_SEQ }, Keys.KEY_FLAG_PRIMARY, new TableField[] { JFlag.FLAG.FLAG_SEQ }, true);
    public static final ForeignKey<BookmarkRecord, UserRecord> FKDIOFORSYBMBSYBYS952JABR7B = Internal.createForeignKey(JBookmark.BOOKMARK, DSL.name("FKdioforsybmbsybys952jabr7b"), new TableField[] { JBookmark.BOOKMARK.USER_SEQ }, Keys.KEY_USER_PRIMARY, new TableField[] { JUser.USER.USER_SEQ }, true);
    public static final ForeignKey<CaseSharingRecord, UserRecord> FK6I0YQPN0J36CBOS886NCI05FJ = Internal.createForeignKey(JCaseSharing.CASE_SHARING, DSL.name("FK6i0yqpn0j36cbos886nci05fj"), new TableField[] { JCaseSharing.CASE_SHARING.USER_SEQ }, Keys.KEY_USER_PRIMARY, new TableField[] { JUser.USER.USER_SEQ }, true);
    public static final ForeignKey<CaseSharingRecord, CaseSharingGroupRecord> FK7M2OX3YPINXH9ESPJWVTFTERI = Internal.createForeignKey(JCaseSharing.CASE_SHARING, DSL.name("FK7m2ox3ypinxh9espjwvtfteri"), new TableField[] { JCaseSharing.CASE_SHARING.CASE_SHARING_GROUP_SEQ }, Keys.KEY_CASE_SHARING_GROUP_PRIMARY, new TableField[] { JCaseSharingGroup.CASE_SHARING_GROUP.CASE_SHARING_GROUP_SEQ }, true);
    public static final ForeignKey<CaseSharingRecord, TemplateRecord> FKKUNPU49SKX3XR8E8RBOUKW9KD = Internal.createForeignKey(JCaseSharing.CASE_SHARING, DSL.name("FKkunpu49skx3xr8e8rboukw9kd"), new TableField[] { JCaseSharing.CASE_SHARING.TEMPLATE_SEQ }, Keys.KEY_TEMPLATE_PRIMARY, new TableField[] { JTemplate.TEMPLATE.TEMPLATE_SEQ }, true);
    public static final ForeignKey<CaseSharingRecord, PartRecord> FKNE3TL9WBDYCGY68HPVH9F58O1 = Internal.createForeignKey(JCaseSharing.CASE_SHARING, DSL.name("FKne3tl9wbdycgy68hpvh9f58o1"), new TableField[] { JCaseSharing.CASE_SHARING.PART_SEQ }, Keys.KEY_PART_PRIMARY, new TableField[] { JPart.PART.PART_SEQ }, true);
    public static final ForeignKey<CaseSharingCommentRecord, CaseSharingRecord> FKGESKGLAJMW9KKSF82EBCODCQY = Internal.createForeignKey(JCaseSharingComment.CASE_SHARING_COMMENT, DSL.name("FKgeskglajmw9kksf82ebcodcqy"), new TableField[] { JCaseSharingComment.CASE_SHARING_COMMENT.CASE_SHARING_SEQ }, Keys.KEY_CASE_SHARING_PRIMARY, new TableField[] { JCaseSharing.CASE_SHARING.CASE_SHARING_SEQ }, true);
    public static final ForeignKey<CaseSharingCommentRecord, UserRecord> FKT3A3UVFN0828VATF15QTHK40K = Internal.createForeignKey(JCaseSharingComment.CASE_SHARING_COMMENT, DSL.name("FKt3a3uvfn0828vatf15qthk40k"), new TableField[] { JCaseSharingComment.CASE_SHARING_COMMENT.USER_SEQ }, Keys.KEY_USER_PRIMARY, new TableField[] { JUser.USER.USER_SEQ }, true);
    public static final ForeignKey<ChatRecord, ChatroomRecord> FK53X2GHIO91G1T1AFMGXM1VCE8 = Internal.createForeignKey(JChat.CHAT, DSL.name("FK53x2ghio91g1t1afmgxm1vce8"), new TableField[] { JChat.CHAT.CHATROOM_SEQ }, Keys.KEY_CHATROOM_PRIMARY, new TableField[] { JChatroom.CHATROOM.CHATROOM_SEQ }, true);
    public static final ForeignKey<ChatRecord, UserRecord> FKS2MTMSBS4FN42FLQEGGUXLHUM = Internal.createForeignKey(JChat.CHAT, DSL.name("FKs2mtmsbs4fn42flqegguxlhum"), new TableField[] { JChat.CHAT.USER_SEQ }, Keys.KEY_USER_PRIMARY, new TableField[] { JUser.USER.USER_SEQ }, true);
    public static final ForeignKey<CommentRecord, FlagRecord> FKLAUGTV9AVDAV02AJDXJ3KU3C6 = Internal.createForeignKey(JComment.COMMENT, DSL.name("FKlaugtv9avdav02ajdxj3ku3c6"), new TableField[] { JComment.COMMENT.FLAG_SEQ }, Keys.KEY_FLAG_PRIMARY, new TableField[] { JFlag.FLAG.FLAG_SEQ }, true);
    public static final ForeignKey<CommentRecord, UserRecord> FKM3LBKSC4VEX8FH6IHX90WCB3B = Internal.createForeignKey(JComment.COMMENT, DSL.name("FKm3lbksc4vex8fh6ihx90wcb3b"), new TableField[] { JComment.COMMENT.USER_SEQ }, Keys.KEY_USER_PRIMARY, new TableField[] { JUser.USER.USER_SEQ }, true);
    public static final ForeignKey<FollowRecord, UserRecord> FKP0ENBHV2BXXK5ICFA80WTUPHS = Internal.createForeignKey(JFollow.FOLLOW, DSL.name("FKp0enbhv2bxxk5icfa80wtuphs"), new TableField[] { JFollow.FOLLOW.USER_FROM_SEQ }, Keys.KEY_USER_PRIMARY, new TableField[] { JUser.USER.USER_SEQ }, true);
    public static final ForeignKey<FollowRecord, UserRecord> FKQ274UGFWMEJCGAXO77AKIEN5G = Internal.createForeignKey(JFollow.FOLLOW, DSL.name("FKq274ugfwmejcgaxo77akien5g"), new TableField[] { JFollow.FOLLOW.USER_TO_SEQ }, Keys.KEY_USER_PRIMARY, new TableField[] { JUser.USER.USER_SEQ }, true);
    public static final ForeignKey<JournalSearchRecord, JournalRecord> FK259KT22NOLO91PB2XXOW9V7E = Internal.createForeignKey(JJournalSearch.JOURNAL_SEARCH, DSL.name("FK259kt22nolo91pb2xxow9v7e"), new TableField[] { JJournalSearch.JOURNAL_SEARCH.JOURNAL_SEQ }, Keys.KEY_JOURNAL_PRIMARY, new TableField[] { JJournal.JOURNAL.JOURNAL_SEQ }, true);
    public static final ForeignKey<JournalSearchRecord, UserRecord> FKMV4OM6DI160KT2N0P88VWV5WW = Internal.createForeignKey(JJournalSearch.JOURNAL_SEARCH, DSL.name("FKmv4om6di160kt2n0p88vwv5ww"), new TableField[] { JJournalSearch.JOURNAL_SEARCH.USER_SEQ }, Keys.KEY_USER_PRIMARY, new TableField[] { JUser.USER.USER_SEQ }, true);
    public static final ForeignKey<MedicalLifeRecord, UserRecord> FK4IXM94NJ6NXCMNOOWL0WS8IOV = Internal.createForeignKey(JMedicalLife.MEDICAL_LIFE, DSL.name("FK4ixm94nj6nxcmnoowl0ws8iov"), new TableField[] { JMedicalLife.MEDICAL_LIFE.USER_SEQ }, Keys.KEY_USER_PRIMARY, new TableField[] { JUser.USER.USER_SEQ }, true);
    public static final ForeignKey<NotifyRecord, FlagRecord> FK15AYB2U2LQFK9T0ULVJC6HWYQ = Internal.createForeignKey(JNotify.NOTIFY, DSL.name("FK15ayb2u2lqfk9t0ulvjc6hwyq"), new TableField[] { JNotify.NOTIFY.FLAG_SEQ }, Keys.KEY_FLAG_PRIMARY, new TableField[] { JFlag.FLAG.FLAG_SEQ }, true);
    public static final ForeignKey<NotifyRecord, UserRecord> FKNCYR447UELDA64ISI8KBDE5C9 = Internal.createForeignKey(JNotify.NOTIFY, DSL.name("FKncyr447uelda64isi8kbde5c9"), new TableField[] { JNotify.NOTIFY.USER_SEQ }, Keys.KEY_USER_PRIMARY, new TableField[] { JUser.USER.USER_SEQ }, true);
    public static final ForeignKey<PartRecord, DeptRecord> FK2EOOGX1T34AWD7KBJCL88TVJ0 = Internal.createForeignKey(JPart.PART, DSL.name("FK2eoogx1t34awd7kbjcl88tvj0"), new TableField[] { JPart.PART.DEPT_SEQ }, Keys.KEY_DEPT_PRIMARY, new TableField[] { JDept.DEPT.DEPT_SEQ }, true);
    public static final ForeignKey<PictureRecord, FlagRecord> FK4YBDG8J8SBNT5OHB4SY75RC82 = Internal.createForeignKey(JPicture.PICTURE, DSL.name("FK4ybdg8j8sbnt5ohb4sy75rc82"), new TableField[] { JPicture.PICTURE.FLAG_SEQ }, Keys.KEY_FLAG_PRIMARY, new TableField[] { JFlag.FLAG.FLAG_SEQ }, true);
    public static final ForeignKey<PreferRecord, FlagRecord> FK8MDYUHI16SI3QB537GEU33AL0 = Internal.createForeignKey(JPrefer.PREFER, DSL.name("FK8mdyuhi16si3qb537geu33al0"), new TableField[] { JPrefer.PREFER.FLAG_SEQ }, Keys.KEY_FLAG_PRIMARY, new TableField[] { JFlag.FLAG.FLAG_SEQ }, true);
    public static final ForeignKey<PreferRecord, UserRecord> FK9XLDB9EIRMVD0UTTB3ATTLN73 = Internal.createForeignKey(JPrefer.PREFER, DSL.name("FK9xldb9eirmvd0uttb3attln73"), new TableField[] { JPrefer.PREFER.USER_SEQ }, Keys.KEY_USER_PRIMARY, new TableField[] { JUser.USER.USER_SEQ }, true);
    public static final ForeignKey<TemplateRecord, UserRecord> FKFS2KGGRMBYH5TRR1HKTULICXH = Internal.createForeignKey(JTemplate.TEMPLATE, DSL.name("FKfs2kggrmbyh5trr1hktulicxh"), new TableField[] { JTemplate.TEMPLATE.USER_SEQ }, Keys.KEY_USER_PRIMARY, new TableField[] { JUser.USER.USER_SEQ }, true);
    public static final ForeignKey<TemplateRecord, PartRecord> FKRIQUJL7WVKJ4TPVOOFRQ67OUI = Internal.createForeignKey(JTemplate.TEMPLATE, DSL.name("FKriqujl7wvkj4tpvoofrq67oui"), new TableField[] { JTemplate.TEMPLATE.PART_SEQ }, Keys.KEY_PART_PRIMARY, new TableField[] { JPart.PART.PART_SEQ }, true);
    public static final ForeignKey<UserRecord, RankingRecord> FK83755D2A40VEIG3NR8D96CE8S = Internal.createForeignKey(JUser.USER, DSL.name("FK83755d2a40veig3nr8d96ce8s"), new TableField[] { JUser.USER.RANKING_SEQ }, Keys.KEY_RANKING_PRIMARY, new TableField[] { JRanking.RANKING.RANKING_SEQ }, true);
    public static final ForeignKey<UserRecord, PartRecord> FKO1YYRBI2OTTUXO2ROHOV35S8Q = Internal.createForeignKey(JUser.USER, DSL.name("FKo1yyrbi2ottuxo2rohov35s8q"), new TableField[] { JUser.USER.PART_SEQ }, Keys.KEY_PART_PRIMARY, new TableField[] { JPart.PART.PART_SEQ }, true);
}
