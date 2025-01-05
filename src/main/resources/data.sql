INSERT INTO journal (journal_pmid, journal_title, journal_korean_title, journal_authors, journal_journal, journal_date, journal_size, journal_doi) VALUES
                                                                                                                                 ('저널 PMID 1', '저널 제목 1', '저널 한글 제목 1', '저자 1', '저널 1', '2024-01-01', 'A4', '10.1234/journal1'),
                                                                                                                                 ('저널 PMID 2', '저널 제목 2', '저널 한글 제목 2', '저자 2', '저널 2', '2024-01-02', 'A4', '10.1234/journal2'),
                                                                                                                                 ('저널 PMID 3', '저널 제목 3', '저널 한글 제목 3', '저자 3', '저널 3', '2024-01-03', 'A4', '10.1234/journal3'),
                                                                                                                                 ('저널 PMID 4', '저널 제목 4', '저널 한글 제목 4', '저자 4', '저널 4', '2024-01-04', 'A4', '10.1234/journal4'),
                                                                                                                                 ('저널 PMID 5', '저널 제목 5', '저널 한글 제목 5', '저자 5', '저널 5', '2024-01-05', 'A4', '10.1234/journal5'),
                                                                                                                                 ('저널 PMID 6', '저널 제목 6', '저널 한글 제목 6', '저자 6', '저널 6', '2024-01-06', 'A4', '10.1234/journal6'),
                                                                                                                                 ('저널 PMID 7', '저널 제목 7', '저널 한글 제목 7', '저자 7', '저널 7', '2024-01-07', 'A4', '10.1234/journal7'),
                                                                                                                                 ('저널 PMID 8', '저널 제목 8', '저널 한글 제목 8', '저자 8', '저널 8', '2024-01-08', 'A4', '10.1234/journal8'),
                                                                                                                                 ('저널 PMID 9', '저널 제목 9', '저널 한글 제목 9', '저자 9', '저널 9', '2024-01-09', 'A4', '10.1234/journal9'),
                                                                                                                                 ('저널 PMID 10', '저널 제목 10', '저널 한글 제목 10', '저자 10', '저널 10', '2024-01-10', 'A4', '10.1234/journal10');

INSERT INTO case_sharing_group (created_at, updated_at, deleted_at) VALUES
                                                                        (NOW(), NOW(), NULL),
                                                                        (NOW(), NOW(), NULL),
                                                                        (NOW(), NOW(), NULL),
                                                                        (NOW(), NULL, NULL),
                                                                        (NOW(), NULL, NULL),
                                                                        (NOW(), NULL, NULL),
                                                                        (NOW(), NULL, NULL),
                                                                        (NOW(), NULL, NULL),
                                                                        (NOW(), NULL, NULL),
                                                                        (NOW(), NULL, NULL);

# INSERT INTO follow (user_from_seq, user_to_seq) VALUES
# (1, 2),
# (1, 3),
# (2, 4),
# (2, 5),
# (3, 6),
# (3, 7),
# (4, 8),
# (4, 9),
# (5, 10),
# (5, 1);

INSERT INTO dept (dept_name) VALUES
                                 ('진료과'),
                                 ('Neurology'),
                                 ('Pediatrics'),
                                 ('부서 이름 4'),
                                 ('부서 이름 5'),
                                 ('부서 이름 6'),
                                 ('부서 이름 7'),
                                 ('부서 이름 8'),
                                 ('부서 이름 9'),
                                 ('부서 이름 10');

INSERT INTO ranking (dept_seq, ranking_num, ranking_name) VALUES
                                                              (1, 1, 'Resident'),
                                                              (1, 2, 'Fellow'),
                                                              (1, 3, 'Consultant'),
                                                              (2, 1, 'Junior Doctor'),
                                                              (2, 2, 'Specialist'),
                                                              (2, 3, 'Senior Specialist'),
                                                              (3, 1, 'Pediatrician'),
                                                              (3, 2, 'Senior Pediatrician'),
                                                              (3, 3, 'Chief Pediatrician'),
                                                              (10, 10, '직급 10');

INSERT INTO part (dept_seq, part_name) VALUES
                                           (1, '내과'),
                                           (1, '응급의학과'),
                                           (1, '외과'),
                                           (1, '마취통증의학과'),
                                           (1, '소아청소년과'),
                                           (1, '비뇨의학과'),
                                           (1, '산부인과'),
                                           (1, '피부과'),
                                           (1, '재활의학과'),
                                           (1, '진단검산의학과'),
                                           (1, '이비인후과'),
                                           (1, '병리과'),
                                           (1, '치과'),
                                           (1, '영상의학과'),
                                           (1, '가정의학과'),
                                           (1, '안과');


INSERT INTO flag (flag_type, flag_entity_seq) VALUES
                                                      ('CASE_SHARING', 1),
                                                      ('CASE_SHARING', 2),
                                                      ('CASE_SHARING', 3),
                                                      ('MEDICAL_LIFE', 1),
                                                      ('MEDICAL_LIFE', 2),
                                                      ('MEDICAL_LIFE', 3),
                                                      ('ANONYMOUS_BOARD', 1),
                                                      ('ANONYMOUS_BOARD', 2),
                                                      ('ANONYMOUS_BOARD', 3),
                                                      ('FLAG_10', 10);

INSERT INTO picture (flag_seq, picture_name, picture_changed_name, picture_url, picture_type, picture_is_deleted,
                     created_at, deleted_at) VALUES
                                                 (1, '사진 1', '변경된 사진 1', 'http://example.com/pic1.jpg', 'image/jpeg', false, NOW(), NULL),
                                                 (2, '사진 2', '변경된 사진 2', 'http://example.com/pic2.jpg', 'image/jpeg', false, NOW(), NULL),
                                                 (3, '사진 3', '변경된 사진 3', 'http://example.com/pic3.jpg', 'image/jpeg', false, NOW(), NULL),
                                                 (4, '사진 4', '변경된 사진 4', 'http://example.com/pic4.jpg', 'image/jpeg', false, NOW(), NULL),
                                                 (5, '사진 5', '변경된 사진 5', 'http://example.com/pic5.jpg', 'image/jpeg', false, NOW(), NULL),
                                                 (6, '사진 6', '변경된 사진 6', 'http://example.com/pic6.jpg', 'image/jpeg', false, NOW(), NULL),
                                                 (7, '사진 7', '변경된 사진 7', 'http://example.com/pic7.jpg', 'image/jpeg', false, NOW(), NULL),
                                                 (8, '사진 8', '변경된 사진 8', 'http://example.com/pic8.jpg', 'image/jpeg', false, NOW(), NULL),
                                                 (9, '사진 9', '변경된 사진 9', 'http://example.com/pic9.jpg', 'image/jpeg', false, NOW(), NULL),
                                                 (10, '사진 10', '변경된 사진 10', 'http://example.com/pic10.jpg', 'image/jpeg', false, NOW(), NULL);

INSERT INTO user (part_seq, ranking_seq, user_name, user_id, user_password, user_email, user_phone,
                  user_auth, user_status, created_at, updated_at, deleted_at) VALUES
                                                                                  (1, 1,  'john_doe', 'encrypted_password1', 'John Doe', 'john.doe@example.com', '010-1234-5678', 'USER',
                                                                                   'ACTIVE', NOW(), NULL, NULL),
                                                                                  (2, 2,  'jane_smith', 'encrypted_password2', 'Jane Smith', 'jane.smith@example.com', '010-9876-5432', 'ADMIN',
                                                                                   'ACTIVE', NOW(), NULL, NULL),
                                                                                  (3, 3,  'alice_jones', 'encrypted_password3', 'Alice Jones', 'alice.jones@example.com', '010-5678-1234',
                                                                                   'USER', 'ACTIVE', NOW(), NULL, NULL),
                                                                                  (3, 3,  '1234', '12345', '$2a$10$SewX3/pNujIKpRYr.ie6TO5tu9jEo4alIivKrZ1zlK6IO1fcbb8Jq', 'alice.jones@example.com', '010-5678-1234', 'USER', 'ACTIVE', NOW(),
                                                                                   NULL, NULL),
                                                                                  (5, 5,  '사용자 5', 'user5', 'password5', 'user5@example.com', '010-0000-0005', 'USER', 'ACTIVE', NOW(), NULL,
                                                                                   NULL),
                                                                                  (6, 6,  '사용자 6', 'user6', 'password6', 'user6@example.com', '010-0000-0006', 'USER', 'ACTIVE', NOW(), NULL,
                                                                                   NULL),
                                                                                  (7, 7,  '사용자 7', 'user7', 'password7', 'user7@example.com', '010-0000-0007', 'USER', 'ACTIVE', NOW(), NULL,
                                                                                   NULL),
                                                                                  (8, 8, '사용자 8', 'user8', 'password8', 'user8@example.com', '010-0000-0008', 'USER', 'ACTIVE', NOW(), NULL,
                                                                                   NULL),
                                                                                  (9, 9,  '사용자 9', 'user9', 'password9', 'user9@example.com', '010-0000-0009', 'USER', 'ACTIVE', NOW(), NULL,
                                                                                   NULL),
                                                                                  (10, 10, '사용자 10', 'user10', 'password10', 'user10@example.com', '010-0000-0010', 'USER', 'ACTIVE', NOW(),
                                                                                   NULL, NULL),
                                                                                  (1, 1,  '박상현', 'psh950519', '12345', 'tkdgus0519@naver.com', '010-7474-5101', 'USER', 'ACTIVE', NOW(),
                                                                                   NULL, NULL);

INSERT INTO medical_life (user_seq, medical_life_title, medical_life_content,
                          medical_life_is_deleted, medical_life_view_count, created_at, updated_at, deleted_at) VALUES
                                                                                         (1,'의료 생활 제목 1', '의료 생활 내용 1', false, 0, NOW(), NULL, NULL),
                                                                                         (2,'의료 생활 제목 2', '의료 생활 내용 2', false, 0, NOW(), NULL, NULL),
                                                                                         (3,'의료 생활 제목 3', '의료 생활 내용 3', false, 0, NOW(), NULL, NULL),
                                                                                         (4,'의료 생활 제목 4', '의료 생활 내용 4', false, 0, NOW(), NULL, NULL),
                                                                                         (5,'의료 생활 제목 5', '의료 생활 내용 5', false, 0, NOW(), NULL, NULL),
                                                                                         (6,'의료 생활 제목 6', '의료 생활 내용 6', false, 0, NOW(), NULL, NULL),
                                                                                         (7,'의료 생활 제목 7', '의료 생활 내용 7', false, 0, NOW(), NULL, NULL),
                                                                                         (8,'의료 생활 제목 8', '의료 생활 내용 8', false, 0, NOW(), NULL, NULL),
                                                                                         (9,'의료 생활 제목 9', '의료 생활 내용 9', false, 0, NOW(), NULL, NULL),
                                                                                         (10,'의료 생활 제목 10', '의료 생활 내용 10', false, 0, NOW(), NULL, NULL);

INSERT INTO journal_search (journal_seq, user_seq, created_at, updated_at) VALUES
                                                                   (1, 1, NOW(), NOW()),
                                                                   (1, 2, NOW(), NOW()),
                                                                   (2, 3, NOW(), NOW()),
                                                                   (2, 4, NOW(), NOW()),
                                                                   (3, 5, NOW(), NOW()),
                                                                   (3, 6, NOW(), NOW()),
                                                                   (4, 7, NOW(), NOW()),
                                                                   (4, 8, NOW(), NOW()),
                                                                   (5, 9, NOW(), NOW()),
                                                                   (5, 10, NOW(), NOW());

INSERT INTO anonymous_board (user_seq, anonymous_board_title, anonymous_board_content, anonymous_board_is_deleted,
                             anonymous_board_view_count, created_at, updated_at, deleted_at) VALUES
                                                                                                 (1, '익명 게시글 제목 1', '익명 게시글 내용 1', false, 0, NOW(), NULL, NULL),
                                                                                                 (2, '익명 게시글 제목 2', '익명 게시글 내용 2', false, 0, NOW(), NULL, NULL),
                                                                                                 (3, '익명 게시글 제목 3', '익명 게시글 내용 3', false, 0, NOW(), NULL, NULL),
                                                                                                 (4, '익명 게시글 제목 4', '익명 게시글 내용 4', false, 0, NOW(), NULL, NULL),
                                                                                                 (5, '익명 게시글 제목 5', '익명 게시글 내용 5', false, 0, NOW(), NULL, NULL),
                                                                                                 (6, '익명 게시글 제목 6', '익명 게시글 내용 6', false, 0, NOW(), NULL, NULL),
                                                                                                 (7, '익명 게시글 제목 7', '익명 게시글 내용 7', false, 0, NOW(), NULL, NULL),
                                                                                                 (8, '익명 게시글 제목 8', '익명 게시글 내용 8', false, 0, NOW(), NULL, NULL),
                                                                                                 (9, '익명 게시글 제목 9', '익명 게시글 내용 9', false, 0, NOW(), NULL, NULL),
                                                                                                 (10, '익명 게시글 제목 10', '익명 게시글 내용 10', false, 0, NOW(), NULL, NULL);

INSERT INTO cp_search_category (user_seq, cp_search_category_name, created_at, updated_at, deleted_at) VALUES
                                                                                                           (1, '카테고리 1', NOW(), NULL, NULL),
                                                                                                           (2, '카테고리 2', NOW(), NULL, NULL),
                                                                                                           (3, '카테고리 3', NOW(), NULL, NULL),
                                                                                                           (4, '카테고리 4', NOW(), NULL, NULL),
                                                                                                           (5, '카테고리 5', NOW(), NULL, NULL),
                                                                                                           (6, '카테고리 6', NOW(), NULL, NULL),
                                                                                                           (7, '카테고리 7', NOW(), NULL, NULL),
                                                                                                           (8, '카테고리 8', NOW(), NULL, NULL),
                                                                                                           (9, '카테고리 9', NOW(), NULL, NULL),
                                                                                                           (10, '카테고리 10', NOW(), NULL, NULL);

INSERT INTO cp_search_category_data (user_seq, cp_search_category_seq, cp_search_category_data_name, created_at,
                                     updated_at, deleted_at) VALUES
                                                                 (1, 1, '카테고리 데이터 1', NOW(), NULL, NULL),
                                                                 (2, 1, '카테고리 데이터 2', NOW(), NULL, NULL),
                                                                 (3, 2, '카테고리 데이터 3', NOW(), NULL, NULL),
                                                                 (4, 2, '카테고리 데이터 4', NOW(), NULL, NULL),
                                                                 (5, 3, '카테고리 데이터 5', NOW(), NULL, NULL),
                                                                 (6, 3, '카테고리 데이터 6', NOW(), NULL, NULL),
                                                                 (7, 4, '카테고리 데이터 7', NOW(), NULL, NULL),
                                                                 (8, 4, '카테고리 데이터 8', NOW(), NULL, NULL),
                                                                 (9, 5, '카테고리 데이터 9', NOW(), NULL, NULL),
                                                                 (10, 5, '카테고리 데이터 10', NOW(), NULL, NULL);

INSERT INTO template (user_seq, part_seq, template_title, template_content, template_open_scope, created_at,
                      updated_at, deleted_at) VALUES
                                                  (1, 1, 'Template 1', 'This is the content of template 1.', 'PUBLIC', NOW(), NOW(), NULL),
                                                  (2, 2, 'Template 2', 'This is the content of template 2.', 'CLASS_OPEN', NOW(), NOW(), NULL),
                                                  (3, 3, 'Template 3', 'This is the content of template 3.', 'PRIVATE', NOW(), NOW(), NULL),
                                                  (4, 4, '템플릿 제목 4', '템플릿 내용 4', 'PRIVATE', NOW(), NULL, NULL),
                                                  (5, 5, '템플릿 제목 5', '템플릿 내용 5', 'CLASS_OPEN', NOW(), NULL, NULL),
                                                  (6, 6, '템플릿 제목 6', '템플릿 내용 6', 'PUBLIC', NOW(), NULL, NULL),
                                                  (7, 7, '템플릿 제목 7', '템플릿 내용 7', 'PRIVATE', NOW(), NULL, NULL),
                                                  (8, 8, '템플릿 제목 8', '템플릿 내용 8', 'CLASS_OPEN', NOW(), NULL, NULL),
                                                  (9, 9, '템플릿 제목 9', '템플릿 내용 9', 'PUBLIC', NOW(), NULL, NULL),
                                                  (10, 10, '템플릿 제목 10', '템플릿 내용 10', 'PRIVATE', NOW(), NULL, NULL);

INSERT INTO case_sharing (user_seq, part_seq, template_seq, case_sharing_group_seq, case_sharing_title,
                          case_sharing_content, case_sharing_is_draft, case_sharing_is_latest, created_at, deleted_at,
                          case_sharing_view_count) VALUES
                                                       (1, 1, 1, 1, 'Case Study 1', 'This is the content for case study 1.', TRUE, FALSE, NOW(), NOW(), 1),
                                                       (2, 2, 2, 2, 'Case Study 2', 'This is the content for case study 2.', TRUE, FALSE, NOW(), NOW(), 2),
                                                       (3, 3, 3, 3, 'Case Study 3', 'This is the content for case study 3.', FALSE, FALSE, NOW(), NOW(), 3),
                                                       (4, 4, 4, 4, 'Case Study 4', 'This is the content for case study 4.', TRUE, FALSE, NOW(), NOW(), 4),
                                                       (5, 1, 1, 5, '사례 공유 제목 5', '사례 공유 내용 5', false, true, NOW(), NULL, 0),
                                                       (6, 1, 1, 6, '사례 공유 제목 6', '사례 공유 내용 6', false, true, NOW(), NULL, 0),
                                                       (7, 1, 1, 7, '사례 공유 제목 7', '사례 공유 내용 7', false, true, NOW(), NULL, 0),
                                                       (8, 1, 1, 8, '사례 공유 제목 8', '사례 공유 내용 8', false, true, NOW(), NULL, 0),
                                                       (9, 1, 1, 9, '사례 공유 제목 9', '사례 공유 내용 9', false, true, NOW(), NULL, 0),
                                                       (10, 1, 1, 10, '사례 공유 제목 10', '사례 공유 내용 10', false, true, NOW(), NULL, 0);


INSERT INTO cp (user_seq, cp_name, cp_description, cp_view_count) VALUES
                                                                      (1, 'CP 이름 1', 'CP 설명 1', 0),
                                                                      (2, 'CP 이름 2', 'CP 설명 2', 0),
                                                                      (3, 'CP 이름 3', 'CP 설명 3', 0),
                                                                      (4, 'CP 이름 4', 'CP 설명 4', 0),
                                                                      (5, 'CP 이름 5', 'CP 설명 5', 0),
                                                                      (6, 'CP 이름 6', 'CP 설명 6', 0),
                                                                      (7, 'CP 이름 7', 'CP 설명 7', 0),
                                                                      (8, 'CP 이름 8', 'CP 설명 8', 0),
                                                                      (9, 'CP 이름 9', 'CP 설명 9', 0),
                                                                      (10, 'CP 이름 10', 'CP 설명 10', 0);

INSERT INTO cp_version (cp_seq, user_seq, cp_version, cp_version_description, cp_url, created_at) VALUES
                                                                                                      (1, 1, '버전 1.0', '초기 버전', 'http://example.com/version1', NOW()),
                                                                                                      (2, 2, '버전 1.1', '수정 사항 반영', 'http://example.com/version2', NOW()),
                                                                                                      (3, 3, '버전 1.2', '기능 추가', 'http://example.com/version3', NOW()),
                                                                                                      (4, 4, '버전 1.3', '버그 수정', 'http://example.com/version4', NOW()),
                                                                                                      (5, 5, '버전 1.4', 'UI 개선', 'http://example.com/version5', NOW()),
                                                                                                      (6, 6, '버전 1.5', '성능 향상', 'http://example.com/version6', NOW()),
                                                                                                      (7, 7, '버전 1.6', '기능 개선', 'http://example.com/version7', NOW()),
                                                                                                      (8, 8, '버전 1.7', '문서화 작업', 'http://example.com/version8', NOW()),
                                                                                                      (9, 9, '버전 1.8', '보안 패치', 'http://example.com/version9', NOW()),
                                                                                                      (10, 10, '버전 1.9', '최종 버전', 'http://example.com/version10', NOW());

INSERT INTO cp_search_data (cp_version_seq, cp_search_category_data_seq) VALUES
                                                                             (1, 1),
                                                                             (1, 2),
                                                                             (2, 1),
                                                                             (2, 2),
                                                                             (3, 1),
                                                                             (3, 2),
                                                                             (4, 1),
                                                                             (4, 2),
                                                                             (5, 1),
                                                                             (5, 2);

INSERT INTO cp_opinion_location (cp_version_seq, cp_opinion_location_page_num ,cp_opinion_location_x, cp_opinion_location_y) VALUES
                                                                                                   (1, 1, 37.5665, 126.978),
                                                                                                   (1, 1, 35.1796, 129.0756),
                                                                                                   (2, 1, 35.9078, 127.7669),
                                                                                                   (2, 1, 37.4563, 126.7052),
                                                                                                   (3, 1, 35.1595, 129.0555),
                                                                                                   (3, 1, 36.3504, 127.3845),
                                                                                                   (4, 1, 37.5326, 126.995),
                                                                                                   (4, 1, 35.1595, 129.0555),
                                                                                                   (5, 1, 37.5665, 126.978),
                                                                                                   (5, 1, 35.1796, 129.0756);

INSERT INTO keyword (flag_seq, keyword_name) VALUES
                                                 (1, 'Orthopedics'),
                                                 (2, 'Surgery'),
                                                 (3, 'Rehabilitation'),
                                                 (4, 'Heart'),
                                                 (5, 'Transplant'),
                                                 (6, '키워드 6'),
                                                 (7, '키워드 7'),
                                                 (8, '키워드 8'),
                                                 (9, '키워드 9'),
                                                 (10, '키워드 10');

INSERT INTO cp_opinion (user_seq, cp_opinion_location_seq, keyword_seq, cp_opinion_content, created_at, updated_at,
                        deleted_at, cp_opinion_view_count) VALUES
                                                               (1, 1, 1, '의견 내용 1', NOW(), NULL, NULL, 0),
                                                               (2, 1, 2, '의견 내용 2', NOW(), NULL, NULL, 0),
                                                               (3, 1, 3, '의견 내용 3', NOW(), NULL, NULL, 0),
                                                               (4, 1, 4, '의견 내용 4', NOW(), NULL, NULL, 0),
                                                               (5, 1, 5, '의견 내용 5', NOW(), NULL, NULL, 0),
                                                               (6, 1, 6, '의견 내용 6', NOW(), NULL, NULL, 0),
                                                               (7, 1, 7, '의견 내용 7', NOW(), NULL, NULL, 0),
                                                               (8, 1, 8, '의견 내용 8', NOW(), NULL, NULL, 0),
                                                               (9, 1, 9, '의견 내용 9', NOW(), NULL, NULL, 0),
                                                               (10, 1, 10, '의견 내용 10', NOW(), NULL, NULL, 0);

INSERT INTO cp_opinion_vote (cp_opinion_seq, cp_opinion_vote, created_at, updated_at) VALUES
                                                                                          (1, true, NOW(), NULL),
                                                                                          (1, false, NOW(), NULL),
                                                                                          (2, true, NOW(), NULL),
                                                                                          (2, true, NOW(), NULL),
                                                                                          (3, false, NOW(), NULL),
                                                                                          (3, true, NOW(), NULL),
                                                                                          (4, false, NOW(), NULL),
                                                                                          (4, true, NOW(), NULL),
                                                                                          (5, true, NOW(), NULL),
                                                                                          (5, false, NOW(), NULL);

INSERT INTO prefer (user_seq, flag_seq, created_at) VALUES
                                                        (1, 1, NOW()),
                                                        (2, 1, NOW()),
                                                        (3, 2, NOW()),
                                                        (4, 2, NOW()),
                                                        (5, 3, NOW()),
                                                        (6, 3, NOW()),
                                                        (7, 4, NOW()),
                                                        (8, 4, NOW()),
                                                        (9, 5, NOW()),
                                                        (10, 5, NOW());

INSERT INTO comment (user_seq, flag_seq, comment_content, comment_is_deleted, created_at, updated_at, deleted_at) VALUES
                                                                                                                      (1, 1, '댓글 내용 1', false, NOW(), NULL, NULL),
                                                                                                                      (2, 2, '댓글 내용 2', false, NOW(), NULL, NULL),
                                                                                                                      (3, 3, '댓글 내용 3', false, NOW(), NULL, NULL),
                                                                                                                      (4, 4, '댓글 내용 4', false, NOW(), NULL, NULL),
                                                                                                                      (5, 5, '댓글 내용 5', false, NOW(), NULL, NULL),
                                                                                                                      (6, 6, '댓글 내용 6', false, NOW(), NULL, NULL),
                                                                                                                      (7, 7, '댓글 내용 7', false, NOW(), NULL, NULL),
                                                                                                                      (8, 8, '댓글 내용 8', false, NOW(), NULL, NULL),
                                                                                                                      (9, 9, '댓글 내용 9', false, NOW(), NULL, NULL),
                                                                                                                      (10, 10, '댓글 내용 10', false, NOW(), NULL, NULL);

INSERT INTO bookmark (user_seq, flag_seq, created_at) VALUES
                                                          (1, 1, NOW()),
                                                          (2, 1, NOW()),
                                                          (3, 2, NOW()),
                                                          (4, 2, NOW()),
                                                          (5, 3, NOW()),
                                                          (6, 3, NOW()),
                                                          (7, 4, NOW()),
                                                          (8, 4, NOW()),
                                                          (9, 5, NOW()),
                                                          (10, 5, NOW());

INSERT INTO notify (user_seq, flag_seq, noti_sender_user_name, noti_sender_user_part, noti_content, noti_type,
                    noti_url, readed_at, created_at) VALUES
                                                         (1, 1, '보내는 사용자 1', '부서 1', '알림 내용 1', 'BOARD', 'http://example.com/1', 'N', NOW()),
                                                         (2, 1, '보내는 사용자 2', '부서 2', '알림 내용 2', 'COMMENT', 'http://example.com/2', 'N', NOW()),
                                                         (3, 2, '보내는 사용자 3', '부서 3', '알림 내용 3', 'CASE', 'http://example.com/3', 'N', NOW()),
                                                         (4, 2, '보내는 사용자 4', '부서 4', '알림 내용 4', 'BOARD', 'http://example.com/4', 'N', NOW()),
                                                         (5, 3, '보내는 사용자 5', '부서 5', '알림 내용 5', 'COMMENT', 'http://example.com/5', 'N', NOW()),
                                                         (6, 3, '보내는 사용자 6', '부서 6', '알림 내용 6', 'CASE', 'http://example.com/6', 'N', NOW()),
                                                         (7, 4, '보내는 사용자 7', '부서 7', '알림 내용 7', 'BOARD', 'http://example.com/7', 'N', NOW()),
                                                         (8, 4, '보내는 사용자 8', '부서 8', '알림 내용 8', 'COMMENT', 'http://example.com/8', 'N', NOW()),
                                                         (9, 5, '보내는 사용자 9', '부서 9', '알림 내용 9', 'CASE', 'http://example.com/9', 'N', NOW()),
                                                         (10, 5, '보내는 사용자 10', '부서 10', '알림 내용 10', 'BOARD', 'http://example.com/10', 'N', NOW());

INSERT INTO chatroom (chatroom_default_name, created_at, updated_at, deleted_at) VALUES
                                                                                     ('일상 대화', NOW(), NULL, NULL),
                                                                                     ('게임 토론', NOW(), NULL, NULL),
                                                                                     ('영화 추천', NOW(), NULL, NULL),
                                                                                     ('음악 감상', NOW(), NULL, NULL),
                                                                                     ('책 이야기', NOW(), NULL, NULL),
                                                                                     ('여행 계획', NOW(), NULL, NULL),
                                                                                     ('기술 공유', NOW(), NULL, NULL),
                                                                                     ('운동 동아리', NOW(), NULL, NULL),
                                                                                     ('요리 레시피', NOW(), NULL, NULL),
                                                                                     ('스포츠 소식', NOW(), NULL, NULL);

INSERT INTO chat (user_seq, chatroom_seq, chatroom_custom_name, joined_at) VALUES
                                                                               (1, 1, '친구 방', NOW()),
                                                                               (2, 1, '가족 방', NOW()),
                                                                               (3, 2, '게임 방', NOW()),
                                                                               (4, 2, '영화 방', NOW()),
                                                                               (5, 3, '음악 방', NOW()),
                                                                               (6, 3, '책 방', NOW()),
                                                                               (7, 4, '여행 방', NOW()),
                                                                               (8, 4, '기술 방', NOW()),
                                                                               (9, 5, '운동 방', NOW()),
                                                                               (10, 5, '요리 방', NOW());