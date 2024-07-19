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
values (1, 'thumbnail_url1', '케이크 맛집1', '케이크 맛집입니다.', '서울시 강남구 어쩌고로1', '케이크 맛집입니다.', ST_GeomFromText(@g1, 4326), 0, 0, false, now(), now()),
       (2, 'thumbnail_url2', '케이크 맛집2', '케이크 맛집입니다.', '서울시 강남구 어쩌고로2', '케이크 맛집입니다.', ST_GeomFromText(@g2, 4326), 0, 0, false, now(), now()),
       (3, 'thumbnail_url3', '케이크 맛집3', '케이크 맛집입니다.', '서울시 강남구 어쩌고로3', '케이크 맛집입니다.', ST_GeomFromText(@g3, 4326), 0, 0, false, now(), now()),
       (4, 'thumbnail_url4', '케이크 맛집4', '케이크 맛집입니다.', '서울시 강남구 어쩌고로3', '케이크 맛집입니다.', ST_GeomFromText(@g4, 4326), 0, 0, false, now(), now()),
       (5, 'thumbnail_url5', '케이크 맛집5', '케이크 맛집입니다.', '서울시 강남구 어쩌고로3', '케이크 맛집입니다.', ST_GeomFromText(@g5, 4326), 0, 0, false, now(), now()),
       (6, 'thumbnail_url6', '케이크 맛집6', '케이크 맛집입니다.', '서울시 강남구 어쩌고로3', '케이크 맛집입니다.', ST_GeomFromText(@g6, 4326), 0, 0, false, now(), now()),
       (7, 'thumbnail_url7', '케이크 맛집7', '케이크 맛집입니다.', '서울시 강남구 어쩌고로3', '케이크 맛집입니다.', ST_GeomFromText(@g7, 4326), 0, 0, false, now(), now()),
       (8, 'thumbnail_url8', '케이크 맛집8', '케이크 맛집입니다.', '서울시 강남구 어쩌고로3', '케이크 맛집입니다.', ST_GeomFromText(@g8, 4326), 0, 0, false, now(), now()),
       (9, 'thumbnail_url9', '케이크 맛집9', '케이크 맛집입니다.', '서울시 강남구 어쩌고로3', '케이크 맛집입니다.', ST_GeomFromText(@g9, 4326), 0, 0, false, now(), now()),
       (10, 'thumbnail_url10', '케이크 맛집10', '케이크 맛집입니다.', '서울시 강남구 어쩌고로3', '케이크 맛집입니다.', ST_GeomFromText(@g10, 4326), 0, 0, false, now(), now()),
       (11, 'thumbnail_url10', '케이크 맛집11', '케이크 맛집입니다.', '서울시 강남구 어쩌고로3', '케이크 맛집입니다.', ST_GeomFromText(@g10, 4326), 0, 0, false, now(), now());

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
       (13, 4, 'cake_image_url13', 0, now(), now()),
       (14, 4, 'cake_image_url14', 0, now(), now()),
       (15, 5, 'cake_image_url15', 0, now(), now()),
       (16, 5, 'cake_image_url16', 0, now(), now()),
       (17, 5, 'cake_image_url17', 0, now(), now()),
       (18, 5, 'cake_image_url18', 0, now(), now()),
       (19, 5, 'cake_image_url19', 0, now(), now()),
       (20, 6, 'cake_image_url20', 0, now(), now()),
       (21, 6, 'cake_image_url21', 0, now(), now()),
       (22, 6, 'cake_image_url22', 0, now(), now()),
       (23, 7, 'cake_image_url23', 0, now(), now()),
       (24, 7, 'cake_image_url24', 0, now(), now()),
       (25, 7, 'cake_image_url25', 0, now(), now()),
       (26, 8, 'cake_image_url26', 0, now(), now()),
       (27, 8, 'cake_image_url27', 0, now(), now()),
       (28, 8, 'cake_image_url28', 0, now(), now()),
       (29, 9, 'cake_image_url29', 0, now(), now()),
       (30, 10, 'cake_image_url30', 0, now(), now()),
       (31, 10, 'cake_image_url31', 0, now(), now()),
       (32, 10, 'cake_image_url32', 0, now(), now()),
       (33, 10, 'cake_image_url33', 0, now(), now()),
       (34, 10, 'cake_image_url34', 0, now(), now());

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

insert into cake_shop_link (link_id, shop_id, link_kind, link_path, created_at, updated_at)
values (1, 1, 'web', 'https://www.cake-shop1.com', now(), now()),
       (2, 1, 'instagram', 'https://www.instagram.com/cake-shop1', now(), now()),
       (3, 1, 'kakaotalk', 'https://www.kakaotalk.com/cake-shop1', now(), now()),
       (4, 2, 'web', 'https://www.cake-shop2.com', now(), now()),
       (5, 2, 'instagram', 'https://www.instagram.com/cake-shop2', now(), now()),
       (6, 3, 'web', 'https://www.cake-shop3.com', now(), now()),
       (7, 3, 'kakaotalk', 'https://www.kakaotalk.com/cake-shop3', now(), now());

insert into business_information(business_number, shop_id, user_id)
values ('010-3375-5555', 1, 1),
       ('010-3375-5555', 2, 2),
       ('010-3375-5555', 3, 3),
       ('010-3375-5555', 4, 4),
       ('010-3375-5555', 5, 5),
       ('010-3375-5555', 6, 6),
       ('010-3375-5555', 7, 7),
       ('010-3375-5555', 8, 8),
       ('010-3375-5555', 9, 9),
       ('010-3375-5555', 10, 10),
       ('010-3375-5555', 11, null);
