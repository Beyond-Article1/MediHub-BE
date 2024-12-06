-- 기존 데이터 삭제
DELETE FROM keyword;
DELETE FROM flag;
DELETE FROM case_sharing_comment;
DELETE FROM case_sharing;

DELETE FROM case_sharing_group;
DELETE FROM template;
DELETE FROM user;
DELETE FROM part;
DELETE FROM dept;


INSERT INTO dept (dept_seq, dept_name) VALUES
                                           (1, '진료과'),
                                           (2, 'Neurology'),
                                           (3, 'Pediatrics');
-- Part 데이터 삽입
INSERT INTO part (part_seq, dept_seq, part_name) VALUES
                                                     (1, 1, 'Interventional Cardiology'),
                                                     (2, 2, 'Neuro Surgery'),
                                                     (3, 3, 'Child Care');

-- Ranking 데이터 삽입
INSERT INTO ranking (dept_seq, ranking_num, ranking_name) VALUES
                                                              (1, 1, 'Resident'),
                                                              (1, 2, 'Fellow'),
                                                              (1, 3, 'Consultant'),
                                                              (2, 1, 'Junior Doctor'),
                                                              (2, 2, 'Specialist'),
                                                              (2, 3, 'Senior Specialist'),
                                                              (3, 1, 'Pediatrician'),
                                                              (3, 2, 'Senior Pediatrician'),
                                                              (3, 3, 'Chief Pediatrician');

-- User 데이터 삽입
INSERT INTO user (
   user_seq,part_seq, ranking_seq, picture_seq, user_id, user_password, user_name, user_email, user_phone, user_status, user_auth
) VALUES
      (1,1, 1, NULL, 'john_doe', 'encrypted_password1', 'John Doe', 'john.doe@example.com', '010-1234-5678', 'ACTIVE', 'USER'),
      (2,2, 2, NULL, 'jane_smith', 'encrypted_password2', 'Jane Smith', 'jane.smith@example.com', '010-9876-5432', 'ACTIVE', 'ADMIN'),
      (3,3, 3, NULL, 'alice_jones', 'encrypted_password3', 'Alice Jones', 'alice.jones@example.com', '010-5678-1234', 'ACTIVE', 'USER'),
(4,3, 3, NULL, '1234', '12345', 'Alice Jones', 'alice.jones@example.com', '010-5678-1234', 'ACTIVE', 'USER');


-- template 테이블 데이터 삽입
INSERT INTO template (
    template_seq, user_seq, part_seq, template_title, template_content, open_scope, created_at, updated_at
) VALUES
      (1,1, 1, 'Template 1', 'This is the content of template 1.', 'PUBLIC', NOW(), NOW()),
      (2,2, 1, 'Template 2', 'This is the content of template 2.', 'CLASS_OPEN', NOW(), NOW()),
      (3,3, 2, 'Template 3', 'This is the content of template 3.', 'PRIVATE', NOW(), NOW());

-- case_sharing_group 테이블 데이터 삽입
INSERT INTO case_sharing_group (case_sharing_group_seq, created_at, updated_at) VALUES
                                                                                    (1, NOW(), NOW()),
                                                                                    (2, NOW(), NOW()),
                                                                                    (3, NOW(), NOW());


-- case_sharing 테이블 데이터 삽입
-- case_sharing 테이블 데이터 삽입
INSERT INTO case_sharing (
    case_sharing_seq, case_sharing_group_seq, user_seq, part_seq, template_seq, case_sharing_title,
    case_sharing_content, case_sharing_is_latest, case_sharing_is_draft, created_at, updated_at
) VALUES
      (1, 1, 1, 1, 1, 'Case Study 1', 'This is the content for case study 1.', TRUE, FALSE, NOW(), NOW()),
      (2, 2, 2, 1, 2, 'Case Study 2', 'This is the content for case study 2.', TRUE, FALSE, NOW(), NOW()),
      (3, 3, 3, 2, 3, 'Case Study 3', 'This is the content for case study 3.', FALSE, FALSE, NOW(), NOW()),
      (4, 3, 3, 2, 3, 'Case Study 4', 'This is the content for case study 4.', TRUE, FALSE, NOW(), NOW());

-- case_sharing_comment 테이블 데이터 삽입
INSERT INTO case_sharing_comment (
    user_seq, case_sharing_seq, case_sharing_comment_content, case_sharing_comment_start_offset, case_sharing_comment_end_offset, created_at, updated_at
) VALUES
      (1, 1, 'This is a comment on case study 1.', 10, 50, NOW(), NOW()),
      (2, 2, 'This is a comment on case study 2.', 20, 60, NOW(), NOW()),
      (3, 1, 'Another comment on case study 1.', 30, 70, NOW(), NOW());


-- flag 테이블 데이터 삽입
INSERT INTO flag (
    flag_seq, flag_board_flag, flag_post_seq
) VALUES
      (1, 'CASE_SHARING', 1),
      (2, 'CASE_SHARING', 2),
      (3, 'CASE_SHARING', 3);

-- keyword 테이블 데이터 삽입
INSERT INTO keyword (
    keyword_seq, flag_seq, keyword_name
) VALUES
      (1, 1, 'Orthopedics'),
      (2, 1, 'Surgery'),
      (3, 2, 'Rehabilitation'),
      (4, 3, 'Heart'),
      (5, 3, 'Transplant');
