insert into category(category_name) values ('패션');
insert into category(category_name) values ('음식');
insert into category(category_name) values ('가전');
insert into category(category_name) values ('도서');

insert into maker (created_at, updated_at, maker_email, maker_home_url, maker_kakao_url, maker_name, maker_phone_number, maker_sns_url, member_id) values ('2023-12-11T23:59:59', '2023-12-11T23:59:59', 'cso6005@nvaer.com', '/home', '/kakao', 'soyoung', '01033344455','/sns', 1);

insert into project (created_at, updated_at, category_id, maker_id, project_description, project_due_date, project_is_authorized, project_name, project_status, project_story_image_url, project_tag, project_target_amount) values ('2023-12-11T23:59:59', '2023-12-11T23:59:59', 1, 1, '좋아요', '2023-12-11T23:59:59', true, '소라색 셔츠 판매', 'PROCESSING', '/story-image', '셔츠', 10000000 );
insert into project (created_at, updated_at, category_id, maker_id, project_description, project_due_date, project_is_authorized, project_name, project_status, project_story_image_url, project_tag, project_target_amount) values ('2023-12-11T23:59:59', '2023-12-11T23:59:59', 1, 1, '좋아요', '2023-12-11T23:59:59', true, '파란색 셔츠 판매', 'PROCESSING', '/story-image', '셔츠', 10000000 );
insert into project (created_at, updated_at, category_id, maker_id, project_description, project_due_date, project_is_authorized, project_name, project_status, project_story_image_url, project_tag, project_target_amount) values ('2023-12-11T23:59:59', '2023-12-11T23:59:59', 1, 1, '좋아요', '2023-12-11T23:59:59', true, '와인색 셔츠 판매', 'PROCESSING', '/story-image', '셔츠', 10000000 );
insert into project (created_at, updated_at, category_id, maker_id, project_description, project_due_date, project_is_authorized, project_name, project_status, project_story_image_url, project_tag, project_target_amount) values ('2023-12-11T23:59:59', '2023-12-11T23:59:59', 1, 1, '좋아요', '2023-12-11T23:59:59', false, '반바지 판매', 'PROCESSING', '/story-image', '셔츠', 10000000 );
insert into project (created_at, updated_at, category_id, maker_id, project_description, project_due_date, project_is_authorized, project_name, project_status, project_story_image_url, project_tag, project_target_amount) values ('2023-12-11T23:59:59', '2023-12-11T23:59:59', 2, 1, '좋아요', '2023-12-11T23:59:59', true, '맛있는 딸기', 'PROCESSING', '/story-image', '셔츠', 10000000 );

insert into lotdeal(created_at, updated_at, lotdeal_due_time, lotdeal_start_time, project_id) values ('2023-10-09T23:59:59', '2023-10-09T23:59:59', '2023-12-09T10:59:59', '2023-11-08T23:59:59', 1);
insert into lotdeal(created_at, updated_at, lotdeal_due_time, lotdeal_start_time, project_id) values ('2023-10-08T23:59:59', '2023-10-08T23:59:59', '2023-12-09T10:59:59', '2023-10-08T23:59:59', 2);

insert into product(created_at, updated_at, product_current_stock_quantity, product_description, product_name, product_price, product_registered_stock_quantity, project_id) values ('2023-10-09T23:59:59','2023-10-09T23:59:59', 20L, '옷 좋아요!!', '긴긴 셔츠', 100000L, 50L, 1);
insert into product(created_at, updated_at, product_current_stock_quantity, product_description, product_name, product_price, product_registered_stock_quantity, project_id) values ('2023-10-09T23:59:59','2023-10-09T23:59:59', 50L, '옷 좋아요!!', '짧은 셔츠', 100000L, 50L, 1);
insert into product(created_at, updated_at, product_current_stock_quantity, product_description, product_name, product_price, product_registered_stock_quantity, project_id) values ('2023-10-09T23:59:59','2023-10-09T23:59:59', 0L, '옷 좋아요!!', '색이 더 진한 셔츠', 100000L, 50L, 1);

insert into project_image(created_at, updated_at, project_image_is_thumbnail, project_image_url, project_id) values ('2023-10-09T23:59:59', '2023-10-09T23:59:59', true, '/thum-url', 1);
insert into project_image(created_at, updated_at, project_image_is_thumbnail, project_image_url, project_id) values ('2023-10-09T23:59:59', '2023-10-09T23:59:59', false, '/img-url-1', 1);
insert into project_image(created_at, updated_at, project_image_is_thumbnail, project_image_url, project_id) values ('2023-10-09T23:59:59', '2023-10-09T23:59:59', false, '/img-url-2', 1);
insert into project_image(created_at, updated_at, project_image_is_thumbnail, project_image_url, project_id) values ('2023-10-09T23:59:59', '2023-10-09T23:59:59', false, '/img-url-3', 1);
insert into project_image(created_at, updated_at, project_image_is_thumbnail, project_image_url, project_id) values ('2023-10-09T23:59:59', '2023-10-09T23:59:59', false, '/img-url-4', 1);

insert into support_signature(created_at, updated_at, member_id, support_signature_content, project_id) values ('2023-10-09T23:59:59', '2023-10-09T23:59:59', 20, '기대됩니다.!!', 1);
insert into support_signature(created_at, updated_at, member_id, support_signature_content, project_id) values ('2023-10-09T23:59:59', '2023-10-09T23:59:59', 30, '기대됩니다.!!', 1);
