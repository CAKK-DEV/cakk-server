SET @g1 = 'Point(37.197734 127.098190)';
SET @g2 = 'Point(37.201623 127.091568)';
SET @g3 = 'Point(37.209769 127.100107)';
SET @g4 = 'Point(37.541530 127.054164)';
SET @g5 = 'Point(37.543343 127.052609)';
SET @g6 = 'Point(37.541530 127.054164)';
SET @g7 = 'Point(37.543343 127.052609)';
SET @g8 = 'Point(37.541530 127.054164)';
SET @g9 = 'Point(37.543343 127.052609)';
SET @g10 = 'Point(37.541530 127.054164)';

insert into cake_shop (shop_id, thumbnail_url, shop_name, shop_address, shop_bio, shop_description, location, like_count, heart_count, linked_flag,
                       created_at, updated_at)
values (1, 'thumbnail_url1', '케이크 맛집1', '서울시 강남구 어쩌고로1', '케이크 맛집입니다.', '케이크 맛집입니다.', ST_GeomFromText(@g1, 4326), 0, 0, false, now(), now()),
       (2, 'thumbnail_url2', '케이크 맛집2', '서울시 강남구 어쩌고로2', '케이크 맛집입니다.', '케이크 맛집입니다.', ST_GeomFromText(@g2, 4326), 0, 0, false, now(), now()),
       (3, 'thumbnail_url3', '케이크 맛집3', '서울시 강남구 어쩌고로3', '케이크 맛집입니다.', '케이크 맛집입니다.', ST_GeomFromText(@g3, 4326), 0, 0, false, now(), now()),
       (4, 'thumbnail_url4', '케이크 맛집4', '서울시 강남구 어쩌고로4', '케이크 맛집입니다.', '케이크 맛집입니다.', ST_GeomFromText(@g4, 4326), 0, 0, false, now(), now()),
       (5, 'thumbnail_url5', '케이크 맛집5', '서울시 강남구 어쩌고로5', '케이크 맛집입니다.', '케이크 맛집입니다.', ST_GeomFromText(@g5, 4326), 0, 0, false, now(), now()),
       (6, 'thumbnail_url6', '케이크 맛집6', '서울시 강남구 어쩌고로6', '케이크 맛집입니다.', '케이크 맛집입니다.', ST_GeomFromText(@g6, 4326), 0, 0, false, now(), now()),
       (7, 'thumbnail_url7', '케이크 맛집7', '서울시 강남구 어쩌고로7', '케이크 맛집입니다.', '케이크 맛집입니다.', ST_GeomFromText(@g7, 4326), 0, 0, false, now(), now()),
       (8, 'thumbnail_url8', '케이크 맛집8', '서울시 강남구 어쩌고로8', '케이크 맛집입니다.', '케이크 맛집입니다.', ST_GeomFromText(@g8, 4326), 0, 0, false, now(), now()),
       (9, 'thumbnail_url9', '케이크 맛집9', '서울시 강남구 어쩌고로9', '케이크 맛집입니다.', '케이크 맛집입니다.', ST_GeomFromText(@g9, 4326), 0, 0, false, now(), now()),
       (10, 'thumbnail_url10', '케이크 맛집10', '서울시 강남구 어쩌고로10', '케이크 맛집입니다.', '케이크 맛집입니다.', ST_GeomFromText(@g10, 4326), 0, 0, false, now(), now());

insert into cake_shop_operation (operation_id, shop_id, operation_day, start_time, end_time, created_at, updated_at)
values (1, 1, 0, '10:00:00', '22:00:00', now(), now()),
       (2, 1, 1, '10:00:00', '22:00:00', now(), now()),
       (3, 1, 2, '10:00:00', '20:00:00', now(), now()),
       (4, 1, 3, '10:00:00', '22:00:00', now(), now()),
       (5, 1, 4, '10:00:00', '22:00:00', now(), now()),
       (6, 2, 0, '8:00:00', '21:00:00', now(), now()),
       (7, 2, 2, '8:00:00', '21:00:00', now(), now()),
       (8, 2, 4, '8:00:00', '21:00:00', now(), now()),
       (9, 3, 5, '7:00:00', '19:00:00', now(), now()),
       (10, 4, 6, '7:00:00', '19:00:00', now(), now()),
       (11, 5, 6, '7:00:00', '19:00:00', now(), now()),
       (12, 6, 6, '7:00:00', '19:00:00', now(), now()),
       (13, 7, 6, '7:00:00', '19:00:00', now(), now()),
       (14, 8, 6, '7:00:00', '19:00:00', now(), now()),
       (15, 9, 6, '7:00:00', '19:00:00', now(), now()),
       (16, 10, 6, '7:00:00', '19:00:00', now(), now());

insert into cake (cake_id, shop_id, cake_image_url, heart_count, created_at, updated_at)
values (1, 1, 'cake_image_url1', 0, now(), now()),
       (2, 1, 'cake_image_url2', 0, now(), now()),
       (3, 1, 'cake_image_url3', 0, now(), now()),
       (4, 2, 'cake_image_url4', 0, now(), now()),
       (5, 2, 'cake_image_url5', 0, now(), now()),
       (6, 2, 'cake_image_url6', 0, now(), now()),
       (7, 3, 'cake_image_url7', 0, now(), now()),
       (8, 3, 'cake_image_url8', 0, now(), now()),
       (9, 3, 'cake_image_url9', 0, now(), now()),
       (10, 3, 'cake_image_url10', 0, now(), now()),
       (11, 3, 'cake_image_url11', 0, now(), now()),
       (12, 4, 'cake_image_url12', 0, now(), now()),
       (13, 5, 'cake_image_url13', 0, now(), now()),
       (14, 6, 'cake_image_url14', 0, now(), now()),
       (15, 7, 'cake_image_url15', 0, now(), now()),
       (16, 8, 'cake_image_url16', 0, now(), now()),
       (17, 9, 'cake_image_url17', 0, now(), now()),
       (18, 10, 'cake_image_url18', 0, now(), now()),
       (19, 10, 'cake_image_url19', 0, now(), now());

insert into business_information(business_number, shop_id, user_id)
values ('010-3375-5555', 1, 1),
       ('010-3375-5555', 2, 2),
       ('010-3375-5555', 3, 3);

insert into tag(tag_id, tag_name, created_at)
values (1, 'tag_name1', now()),
       (2, 'tag_name2', now()),
       (3, 'tag_name3', now()),
       (4, 'tag_name4', now()),
       (5, 'tag_name5', now()),
       (6, 'tag_name6', now()),
       (7, 'tag_name7', now()),
       (8, 'tag_name8', now()),
       (9, 'tag_name9', now()),
       (10, 'tag_name10', now());

insert into cake_tag(cake_tag_id, cake_id, tag_id, created_at)
values (1, 1, 1, now()),
       (2, 2, 2, now()),
       (3, 3, 3, now()),
       (4, 4, 4, now()),
       (5, 5, 5, now()),
       (6, 6, 6, now()),
       (7, 7, 7, now()),
       (8, 8, 8, now()),
       (9, 9, 9, now()),
       (10, 10, 1, now()),
       (11, 10, 2, now()),
       (12, 10, 3, now()),
       (13, 11, 4, now()),
       (14, 11, 5, now()),
       (15, 11, 6, now()),
       (16, 12, 7, now()),
       (17, 12, 8, now()),
       (18, 12, 9, now()),
       (19, 13, 10, now()),
       (20, 13, 1, now()),
       (21, 13, 1, now()),
       (22, 14, 2, now()),
       (23, 15, 3, now()),
       (24, 16, 4, now()),
       (25, 17, 5, now()),
       (26, 1, 5, now()),
       (27, 1, 4, now()),
       (28, 2, 3, now()),
       (29, 3, 2, now()),
       (30, 3, 1, now()),
       (31, 2, 5, now()),
       (32, 2, 4, now()),
       (33, 1, 3, now()),
       (34, 1, 2, now()),
       (35, 4, 1, now()),
       (36, 5, 5, now()),
       (37, 5, 5, now()),
       (38, 18, 5, now());


insert into cake_category (cake_category_id, cake_id, cake_design_category, created_at)
values (1, 1, 'FLOWER', now()),
       (2, 2, 'FLOWER', now()),
       (3, 3, 'FLOWER', now()),
       (4, 4, 'FLOWER', now()),
       (5, 5, 'FLOWER', now()),
       (6, 6, 'FLOWER', now()),
       (7, 7, 'FLOWER', now()),
       (8, 8, 'FLOWER', now()),
       (9, 9, 'FLOWER', now()),
       (10, 10, 'FLOWER', now());
