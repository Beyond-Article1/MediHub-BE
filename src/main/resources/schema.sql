SHOW DATABASES;

# CREATE DATABASE medihub CHARACTER SET 'utf8';

USE medihub;

# DROP TABLE IF EXISTS chatbot_message;
# DROP TABLE IF EXISTS chatbot_history;
DROP TABLE IF EXISTS cp_opinion_vote;
DROP TABLE IF EXISTS cp_opinion;
DROP TABLE IF EXISTS medical_life;
DROP TABLE IF EXISTS journal_search;
DROP TABLE IF EXISTS journal;
DROP TABLE IF EXISTS keyword;
DROP TABLE IF EXISTS anonymous_board;
DROP TABLE IF EXISTS prefer;
DROP TABLE IF EXISTS cp_search_data;
DROP TABLE IF EXISTS cp_search_category_data;
DROP TABLE IF EXISTS cp_search_category;
DROP TABLE IF EXISTS case_sharing_comment;
DROP TABLE IF EXISTS case_sharing;
DROP TABLE IF EXISTS template;
DROP TABLE IF EXISTS comment;
DROP TABLE IF EXISTS case_sharing_group;
DROP TABLE IF EXISTS bookmark;
DROP TABLE IF EXISTS notify;
DROP TABLE IF EXISTS follow;
DROP TABLE IF EXISTS cp_opinion_location;
DROP TABLE IF EXISTS cp_version;
DROP TABLE IF EXISTS cp;
DROP TABLE IF EXISTS chat;
DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS ranking;
DROP TABLE IF EXISTS part;
DROP TABLE IF EXISTS dept;
DROP TABLE IF EXISTS picture;
DROP TABLE IF EXISTS flag;
DROP TABLE IF EXISTS chatroom;


CREATE TABLE cp_opinion_vote
(
    cp_opinion_vote_seq bigint   NOT NULL AUTO_INCREMENT COMMENT 'AUTO_INCREMENT',
    cp_opinion_seq      bigint   NOT NULL,
    cp_opinion_vote     boolean  NOT NULL COMMENT 'true, false',
    created_at          datetime NOT NULL DEFAULT NOW(),
    updated_at          datetime NULL,
    PRIMARY KEY (cp_opinion_vote_seq)
);

CREATE TABLE cp_opinion
(
    cp_opinion_seq          bigint   NOT NULL AUTO_INCREMENT COMMENT 'AUTO_INCREMENT',
    user_seq                bigint   NOT NULL,
    cp_opinion_location_seq bigint   NOT NULL,
    keyword_seq             bigint   NOT NULL COMMENT 'AUTO_INCREMENT',
    cp_opinion_content      text     NOT NULL,
    created_at              datetime NOT NULL DEFAULT NOW(),
    updated_at              datetime NULL,
    deleted_at              datetime NULL,
    cp_opinion_view_count   bigint   NOT NULL DEFAULT 0,
    PRIMARY KEY (cp_opinion_seq)
);

CREATE TABLE medical_life (
                              medical_life_seq	bigint	NOT NULL AUTO_INCREMENT COMMENT 'AUTO_INCREMENT',
                              user_seq	bigint	NOT NULL,
                              medical_life_title	varchar(255)	NOT NULL,
                              medical_life_content	text	NOT NULL,
                              medical_life_is_deleted boolean NOT NULL DEFAULT FALSE,
                              medical_life_view_count bigint NOT NULL DEFAULT 0,
                              created_at	datetime	NOT NULL	DEFAULT NOW(),
                              updated_at	datetime	NULL,
                              deleted_at	datetime	NULL,
                              PRIMARY KEY (medical_life_seq)
);

CREATE TABLE journal_search
(
    journal_search_seq bigint   NOT NULL AUTO_INCREMENT COMMENT 'AUTO_INCREMENT',
    journal_seq        bigint   NOT NULL,
    user_seq           bigint   NOT NULL,
    created_at         datetime NOT NULL DEFAULT NOW(),
    updated_at         datetime NOT NULL,
    PRIMARY KEY (journal_search_seq)
);

CREATE TABLE journal
(
    journal_seq          bigint       NOT NULL AUTO_INCREMENT COMMENT 'AUTO_INCREMENT',
    journal_pmid         varchar(50)  NOT NULL COMMENT 'UNIQUE',
    journal_title        varchar(255) NOT NULL COMMENT 'UNIQUE',
    journal_korean_title varchar(255) NOT NULL,
    journal_authors      text         NOT NULL,
    journal_journal      varchar(255) NOT NULL,
    journal_date         varchar(50)  NOT NULL,
    journal_size         varchar(255) NOT NULL,
    journal_doi          varchar(255) NOT NULL COMMENT 'UNIQUE',
    PRIMARY KEY (journal_seq)
);

CREATE TABLE keyword
(
    keyword_seq  bigint      NOT NULL AUTO_INCREMENT COMMENT 'AUTO_INCREMENT',
    flag_seq     bigint      NOT NULL,
    keyword_name varchar(50) NOT NULL,
    PRIMARY KEY (keyword_seq)
);

CREATE TABLE anonymous_board
(
    anonymous_board_seq        bigint       NOT NULL AUTO_INCREMENT COMMENT 'AUTO_INCREMENT',
    user_seq                   bigint       NOT NULL,
    anonymous_board_title      varchar(255) NOT NULL,
    anonymous_board_content    text         NOT NULL,
    anonymous_board_is_deleted boolean      NOT NULL DEFAULT false,
    anonymous_board_view_count bigint       NOT NULL DEFAULT 0,
    created_at                 datetime     NOT NULL DEFAULT NOW(),
    updated_at                 datetime     NULL,
    deleted_at                 datetime     NULL,
    PRIMARY KEY (anonymous_board_seq)
);

CREATE TABLE prefer
(
    prefer_seq bigint   NOT NULL AUTO_INCREMENT COMMENT 'AUTO_INCREMENT',
    user_seq   bigint   NOT NULL,
    flag_seq   bigint   NOT NULL,
    created_at datetime NOT NULL DEFAULT NOW(),
    PRIMARY KEY (prefer_seq)
);

CREATE TABLE cp_search_data
(
    cp_search_data_seq          bigint NOT NULL AUTO_INCREMENT COMMENT 'AUTO_INCREMENT',
    cp_version_seq              bigint NOT NULL,
    cp_search_category_data_seq bigint NOT NULL,
    PRIMARY KEY (cp_search_data_seq)
);

CREATE TABLE cp_search_category_data
(
    cp_search_category_data_seq  bigint      NOT NULL AUTO_INCREMENT
        COMMENT 'AUTO_INCREMENT',
    user_seq                     bigint      NOT NULL,
    cp_search_category_seq       bigint      NOT NULL,
    cp_search_category_data_name varchar(50) NOT NULL,
    created_at                   datetime    NOT NULL DEFAULT NOW(),
    updated_at                   datetime    NULL,
    deleted_at                   datetime    NULL,
    PRIMARY KEY (cp_search_category_data_seq)
);

CREATE TABLE cp_search_category
(
    cp_search_category_seq  bigint      NOT NULL AUTO_INCREMENT COMMENT 'AUTO_INCREMENT',
    user_seq                bigint      NOT NULL,
    cp_search_category_name varchar(50) NOT NULL,
    created_at              datetime    NOT NULL DEFAULT NOW(),
    updated_at              datetime    NULL,
    deleted_at              datetime    NULL,
    PRIMARY KEY (cp_search_category_seq)
);

CREATE TABLE case_sharing_comment
(
    case_sharing_comment_seq          bigint   NOT NULL AUTO_INCREMENT
        COMMENT 'AUTO_INCREMENT',
    user_seq                          bigint   NOT NULL,
    case_sharing_seq                  bigint   NOT NULL,
    case_sharing_comment_content      text     NOT NULL,
    case_sharing_comment_start_offset bigint   NOT NULL,
    case_sharing_comment_end_offset   bigint   NOT NULL,
    created_at                        datetime NOT NULL DEFAULT NOW(),
    updated_at                        datetime NULL,
    deleted_at                        datetime NULL,
    PRIMARY KEY (case_sharing_comment_seq)
);

CREATE TABLE case_sharing
(
    case_sharing_seq        bigint       NOT NULL AUTO_INCREMENT COMMENT 'AUTO_INCREMENT',
    user_seq                bigint       NOT NULL,
    part_seq                bigint       NOT NULL,
    template_seq            bigint       NOT NULL,
    case_sharing_group_seq  bigint       NOT NULL,
    case_sharing_title      varchar(255) NOT NULL,
    case_sharing_content    longtext     NOT NULL,
    case_sharing_is_draft   boolean      NOT NULL,
    case_sharing_is_latest  boolean      NULL,
    created_at              datetime     NOT NULL DEFAULT NOW(),
    deleted_at              datetime     NULL,
    case_sharing_view_count bigint       NOT NULL DEFAULT 0,
    PRIMARY KEY (case_sharing_seq)
);

CREATE TABLE template
(
    template_seq        bigint       NOT NULL AUTO_INCREMENT COMMENT 'AUTO_INCREMENT',
    user_seq            bigint       NOT NULL,
    part_seq            bigint       NOT NULL,
    template_title      varchar(255) NOT NULL,
    template_content    text         NOT NULL,
    template_open_scope varchar(50)  NULL COMMENT 'PRIVATE, CLASS_OPEN, PUBLIC',
    created_at          datetime     NOT NULL DEFAULT NOW(),
    updated_at          datetime     NULL,
    deleted_at          datetime     NULL,
    PRIMARY KEY (template_seq)
);


CREATE TABLE comment
(
    comment_seq        bigint   NOT NULL AUTO_INCREMENT COMMENT 'AUTO_INCREMENT',
    user_seq           bigint   NOT NULL,
    flag_seq           bigint   NOT NULL,
    comment_content    text     NOT NULL,
    comment_is_deleted boolean  NOT NULL DEFAULT false,
    created_at         datetime NOT NULL DEFAULT NOW(),
    updated_at         datetime NULL,
    deleted_at         datetime NULL,
    PRIMARY KEY (comment_seq)
);

CREATE TABLE case_sharing_group
(
    case_sharing_group_seq bigint   NOT NULL AUTO_INCREMENT COMMENT 'AUTO_INCREMENT',
    created_at             datetime NOT NULL DEFAULT NOW(),
    updated_at             datetime NULL,
    deleted_at             datetime NULL,
    PRIMARY KEY (case_sharing_group_seq)
);

CREATE TABLE bookmark
(
    bookmark_seq bigint   NOT NULL AUTO_INCREMENT COMMENT 'AUTO_INCREMENT',
    user_seq     bigint   NOT NULL,
    flag_seq     bigint   NOT NULL,
    created_at   datetime NOT NULL DEFAULT NOW(),
    PRIMARY KEY (bookmark_seq)
);

CREATE TABLE notify
(
    noti_seq              bigint       NOT NULL AUTO_INCREMENT COMMENT 'AUTO_INCREMENT',
    user_seq              bigint       NOT NULL,
    flag_seq              bigint       NOT NULL,
    noti_sender_user_name varchar(50)  NOT NULL,
    noti_sender_user_part varchar(50)  NOT NULL,
    noti_content          varchar(50)  NOT NULL,
    noti_type             varchar(50)  NOT NULL COMMENT 'BOARD, COMMENT, CASE',
    noti_url              varchar(255) NOT NULL,
    readed_at             varchar(1)   NOT NULL COMMENT 'Y, N',
    created_at            datetime     NOT NULL DEFAULT NOW(),
    PRIMARY KEY (noti_seq)
);

# CREATE TABLE follow (
#                         follow_seq	bigint	NOT NULL AUTO_INCREMENT COMMENT 'AUTO_INCREMENT',
#                         user_from_seq	bigint	NOT NULL,
#                         user_to_seq	bigint	NOT NULL,
#                         PRIMARY KEY (follow_seq)
# );

CREATE TABLE cp_opinion_location
(
    cp_opinion_location_seq      bigint   NOT NULL AUTO_INCREMENT
        COMMENT 'AUTO_INCREMENT',
    cp_version_seq               bigint   NOT NULL,
    cp_opinion_location_page_num bigint   NOT NULL,
    cp_opinion_location_x        double   NOT NULL,
    cp_opinion_location_y        double   NOT NULL,
    created_at                   datetime NOT NULL DEFAULT NOW(),
    deleted_at                   datetime NULL,
    PRIMARY KEY (cp_opinion_location_seq)
);

CREATE TABLE cp_version
(
    cp_version_seq         bigint       NOT NULL AUTO_INCREMENT COMMENT 'AUTO_INCREMENT',
    cp_seq                 bigint       NOT NULL,
    user_seq               bigint       NOT NULL,
    cp_version             varchar(255) NOT NULL,
    cp_version_description varchar(255) NULL COMMENT '변경 내용',
    cp_url                 varchar(255) NULL,
    created_at             datetime     NOT NULL DEFAULT NOW(),
    PRIMARY KEY (cp_version_seq)
);

CREATE TABLE cp
(
    cp_seq         bigint       NOT NULL AUTO_INCREMENT COMMENT 'AUTO_INCREMENT',
    user_seq       bigint       NOT NULL,
    cp_name        varchar(255) NOT NULL,
    cp_description varchar(255) NULL,
    cp_view_count  bigint       NULL DEFAULT 0,
    PRIMARY KEY (cp_seq)
);

CREATE TABLE user
(
    user_seq      bigint       NOT NULL AUTO_INCREMENT COMMENT 'AUTO_INCREMENT',
    part_seq      bigint       NOT NULL,
    ranking_seq   bigint       NOT NULL,
    user_name     varchar(50)  NOT NULL,
    user_id       varchar(50)  NOT NULL,
    user_password varchar(255) NOT NULL,
    user_email    varchar(50)  NOT NULL,
    user_phone    varchar(50)  NOT NULL,
    user_auth     varchar(50)  NOT NULL DEFAULT 'USER' COMMENT 'USER, ADMIN',
    user_status   varchar(50)  NOT NULL DEFAULT 'ACTIVE' COMMENT 'ACTIVE, LEAVE, DELETE',
    created_at    datetime     NOT NULL DEFAULT NOW(),
    updated_at    datetime     NULL,
    deleted_at    datetime     NULL,
    PRIMARY KEY (user_seq)
);

CREATE TABLE ranking
(
    ranking_seq  bigint      NOT NULL AUTO_INCREMENT COMMENT 'AUTO_INCREMENT',
    dept_seq     bigint      NOT NULL,
    ranking_num  bigint      NOT NULL COMMENT '낮은 번호가 높은 직급',
    ranking_name varchar(50) NOT NULL,
    PRIMARY KEY (ranking_seq)
);

CREATE TABLE part
(
    part_seq  bigint      NOT NULL AUTO_INCREMENT COMMENT 'AUTO_INCREMENT',
    dept_seq  bigint      NOT NULL,
    part_name varchar(50) NOT NULL,
    PRIMARY KEY (part_seq)
);

CREATE TABLE dept
(
    dept_seq  bigint      NOT NULL AUTO_INCREMENT COMMENT 'AUTO_INCREMENT',
    dept_name varchar(50) NOT NULL,
    PRIMARY KEY (dept_seq)
);

CREATE TABLE picture
(
    picture_seq          bigint       NOT NULL AUTO_INCREMENT COMMENT 'AUTO_INCREMENT',
    flag_seq             bigint       NOT NULL,
    picture_name         varchar(255) NOT NULL,
    picture_changed_name varchar(255) NULL,
    picture_url          varchar(255) NOT NULL,
    picture_type         varchar(50)  NOT NULL,
    picture_is_deleted   boolean      NOT NULL DEFAULT false,
    created_at           datetime     NOT NULL DEFAULT NOW(),
    deleted_at           datetime     NULL,
    PRIMARY KEY (picture_seq)
);

CREATE TABLE flag
(
    flag_seq        bigint      NOT NULL AUTO_INCREMENT COMMENT 'AUTO_INCREMENT',
    flag_type       varchar(50) NOT NULL,
    flag_entity_seq bigint      NOT NULL,
    PRIMARY KEY (flag_seq)
);

CREATE TABLE chatroom
(
    chatroom_seq          bigint       NOT NULL AUTO_INCREMENT COMMENT 'AUTO_INCREMENT',
    chatroom_default_name varchar(255) NOT NULL,
    created_at            datetime     NOT NULL DEFAULT NOW(),
    updated_at            datetime     NULL,
    deleted_at            datetime     NULL,
    PRIMARY KEY (chatroom_seq)
);

CREATE TABLE chat
(
    chat_seq             bigint       NOT NULL AUTO_INCREMENT COMMENT 'AUTO_INCREMENT',
    user_seq             bigint       NOT NULL,
    chatroom_seq         bigint       NOT NULL,
    chatroom_custom_name varchar(255) NULL,
    joined_at            datetime     NOT NULL DEFAULT NOW(),
    PRIMARY KEY (chat_seq)
);


ALTER TABLE cp_opinion_vote
    ADD CONSTRAINT FK_cp_opinion_vote_cp_opinion FOREIGN KEY (cp_opinion_seq)
        REFERENCES cp_opinion (cp_opinion_seq) ON DELETE CASCADE;

ALTER TABLE cp_opinion
    ADD CONSTRAINT FK_cp_opinion_user FOREIGN KEY (user_seq) REFERENCES user (user_seq) ON DELETE CASCADE,
    ADD CONSTRAINT FK_cp_opinion_cp_opinion_location FOREIGN KEY (cp_opinion_location_seq)
        REFERENCES cp_opinion_location (cp_opinion_location_seq) ON DELETE CASCADE,
    ADD CONSTRAINT FK_cp_opinion_keyword FOREIGN KEY (keyword_seq) REFERENCES keyword (keyword_seq) ON DELETE CASCADE;

ALTER TABLE medical_life
    ADD CONSTRAINT FK_medical_life_user_seq FOREIGN KEY (user_seq) REFERENCES user (user_seq) ON DELETE CASCADE,
    ADD CONSTRAINT FK_medical_life_dept_seq FOREIGN KEY (dept_seq) REFERENCES dept (dept_seq) ON DELETE CASCADE,
    ADD CONSTRAINT FK_medical_life_part_seq FOREIGN KEY (part_seq) REFERENCES part (part_seq) ON DELETE CASCADE;

ALTER TABLE journal_search
    ADD CONSTRAINT FK_journal_search_journal FOREIGN KEY (journal_seq)
        REFERENCES journal (journal_seq) ON DELETE CASCADE,
    ADD CONSTRAINT FK_journal_search_user FOREIGN KEY (user_seq) REFERENCES user (user_seq) ON DELETE CASCADE;

ALTER TABLE keyword
    ADD CONSTRAINT FK_keyword_flag FOREIGN KEY (flag_seq) REFERENCES flag (flag_seq) ON DELETE CASCADE;

ALTER TABLE anonymous_board
    ADD CONSTRAINT FK_anonymous_board_user FOREIGN KEY (user_seq) REFERENCES user (user_seq) ON DELETE CASCADE;

ALTER TABLE prefer
    ADD CONSTRAINT FK_prefer_user FOREIGN KEY (user_seq) REFERENCES user (user_seq) ON DELETE CASCADE,
    ADD CONSTRAINT FK_prefer_flag FOREIGN KEY (flag_seq) REFERENCES flag (flag_seq) ON DELETE CASCADE;

ALTER TABLE cp_search_data
    ADD CONSTRAINT FK_cp_search_data_cp_version FOREIGN KEY (cp_version_seq)
        REFERENCES cp_version (cp_version_seq) ON DELETE CASCADE,
    ADD CONSTRAINT FK_cp_search_data_cp_search_category_data FOREIGN KEY (cp_search_category_data_seq)
        REFERENCES cp_search_category_data (cp_search_category_data_seq) ON DELETE CASCADE;

ALTER TABLE cp_search_category_data
    ADD CONSTRAINT FK_cp_search_category_data_user FOREIGN KEY (user_seq) REFERENCES user (user_seq) ON DELETE CASCADE,
    ADD CONSTRAINT FK_cp_search_category_data_cp_search_category FOREIGN KEY (cp_search_category_seq)
        REFERENCES cp_search_category (cp_search_category_seq) ON DELETE CASCADE;

ALTER TABLE cp_search_category
    ADD CONSTRAINT FK_cp_search_category_user FOREIGN KEY (user_seq) REFERENCES user (user_seq) ON DELETE CASCADE;

ALTER TABLE case_sharing_comment
    ADD CONSTRAINT FK_case_sharing_comment_user FOREIGN KEY (user_seq) REFERENCES user (user_seq) ON DELETE CASCADE,
    ADD CONSTRAINT FK_case_sharing_comment_case_sharing FOREIGN KEY (case_sharing_seq)
        REFERENCES case_sharing (case_sharing_seq) ON DELETE CASCADE;

ALTER TABLE case_sharing
    ADD CONSTRAINT FK_case_sharing_user FOREIGN KEY (user_seq) REFERENCES user (user_seq) ON DELETE CASCADE,
    ADD CONSTRAINT FK_case_sharing_part FOREIGN KEY (part_seq) REFERENCES part (part_seq) ON DELETE CASCADE,
    ADD CONSTRAINT FK_case_sharing_template FOREIGN KEY (template_seq)
        REFERENCES template (template_seq) ON DELETE CASCADE,
    ADD CONSTRAINT FK_case_sharing_case_sharing_group FOREIGN KEY (case_sharing_group_seq)
        REFERENCES case_sharing_group (case_sharing_group_seq) ON DELETE CASCADE;

ALTER TABLE template
    ADD CONSTRAINT FK_template_user FOREIGN KEY (user_seq) REFERENCES user (user_seq) ON DELETE CASCADE,
    ADD CONSTRAINT FK_template_part FOREIGN KEY (part_seq) REFERENCES part (part_seq) ON DELETE CASCADE;

ALTER TABLE comment
    ADD CONSTRAINT FK_comment_user FOREIGN KEY (user_seq) REFERENCES user (user_seq) ON DELETE CASCADE,
    ADD CONSTRAINT FK_comment_flag FOREIGN KEY (flag_seq) REFERENCES flag (flag_seq) ON DELETE CASCADE;

ALTER TABLE bookmark
    ADD CONSTRAINT FK_bookmark_user FOREIGN KEY (user_seq) REFERENCES user (user_seq) ON DELETE CASCADE,
    ADD CONSTRAINT FK_bookmark_flag FOREIGN KEY (flag_seq) REFERENCES flag (flag_seq) ON DELETE CASCADE;

ALTER TABLE notify
    ADD CONSTRAINT FK_notify_user FOREIGN KEY (user_seq) REFERENCES user (user_seq) ON DELETE CASCADE,
    ADD CONSTRAINT FK_notify_flag FOREIGN KEY (flag_seq) REFERENCES flag (flag_seq) ON DELETE CASCADE;

# ALTER TABLE follow
#     ADD CONSTRAINT FK_follow_user FOREIGN KEY (user_seq) REFERENCES user (user_seq) ON DELETE CASCADE,
#     ADD CONSTRAINT FK_follow_user FOREIGN KEY (user_seq) REFERENCES user (user_seq) ON DELETE CASCADE;

ALTER TABLE cp_opinion_location
    ADD CONSTRAINT FK_cp_opinion_location_cp_version FOREIGN KEY (cp_version_seq)
        REFERENCES cp_version (cp_version_seq) ON DELETE CASCADE;

ALTER TABLE cp_version
    ADD CONSTRAINT FK_cp_version_cp FOREIGN KEY (cp_seq) REFERENCES cp (cp_seq) ON DELETE CASCADE,
    ADD CONSTRAINT FK_cp_version_user FOREIGN KEY (user_seq) REFERENCES user (user_seq) ON DELETE CASCADE;

ALTER TABLE cp
    ADD CONSTRAINT FK_cp_user FOREIGN KEY (user_seq) REFERENCES user (user_seq) ON DELETE CASCADE;

ALTER TABLE chat
    ADD CONSTRAINT FK_chat_user FOREIGN KEY (user_seq) REFERENCES user (user_seq) ON DELETE CASCADE,
    ADD CONSTRAINT FK_chat_chatroom FOREIGN KEY (chatroom_seq) REFERENCES chatroom (chatroom_seq)
        ON DELETE CASCADE;

ALTER TABLE user
    ADD CONSTRAINT FK_user_part FOREIGN KEY (part_seq) REFERENCES part (part_seq) ON DELETE CASCADE,
    ADD CONSTRAINT FK_user_ranking FOREIGN KEY (ranking_seq) REFERENCES ranking (ranking_seq) ON DELETE CASCADE;

ALTER TABLE ranking
    ADD CONSTRAINT FK_ranking_dept FOREIGN KEY (dept_seq) REFERENCES dept (dept_seq) ON DELETE CASCADE;

ALTER TABLE part
    ADD CONSTRAINT FK_part_dept FOREIGN KEY (dept_seq) REFERENCES dept (dept_seq) ON DELETE CASCADE;

ALTER TABLE picture
    ADD CONSTRAINT FK_picture_flag FOREIGN KEY (flag_seq) REFERENCES flag (flag_seq) ON DELETE CASCADE;

ALTER TABLE cp_opinion_vote
    DROP COLUMN updated_at;

ALTER TABLE cp_opinion
    DROP FOREIGN KEY FK_cp_opinion_keyword;

ALTER TABLE cp_opinion
    DROP COLUMN keyword_seq;

ALTER TABLE cp_opinion
    MODIFY COLUMN cp_opinion_content longtext;