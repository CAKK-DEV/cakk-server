SET @g1 = 'Point(37.197734 127.098190)';
SET @g2 = 'Point(37.201623 127.091568)';
SET @g3 = 'Point(37.209769 127.100107)';

insert into cake_shop (shop_id, thumbnail_url, shop_name, shop_address, shop_bio, shop_description, location, like_count, linked_flag,
                       created_at, updated_at)
values (1, 'thumbnail_url1', '케이크 맛집1', '케이크 맛집입니다.', '서울시 강남구 어쩌고로1', '케이크 맛집입니다.', ST_GeomFromText(@g1, 4326), 0, false, now(), now()),
       (2, 'thumbnail_url2', '케이크 맛집2', '케이크 맛집입니다.', '서울시 강남구 어쩌고로2', '케이크 맛집입니다.', ST_GeomFromText(@g2, 4326), 0, false, now(), now()),
       (3, 'thumbnail_url3', '케이크 맛집3', '케이크 맛집입니다.', '서울시 강남구 어쩌고로3', '케이크 맛집입니다.', ST_GeomFromText(@g3, 4326), 0, false, now(), now());

insert into cake (cake_id, shop_id, cake_image_url, like_count, created_at, updated_at)
values (1, 1, 'cake_image_url1', 0, now(), now()),
       (2, 1, 'cake_image_url2', 0, now(), now()),
       (3, 1, 'cake_image_url3', 0, now(), now()),
       (4, 2, 'cake_image_url4', 0, now(), now()),
       (5, 2, 'cake_image_url5', 0, now(), now()),
       (6, 2, 'cake_image_url6', 0, now(), now()),
       (7, 3, 'cake_image_url7', 0, now(), now()),
       (8, 3, 'cake_image_url8', 0, now(), now()),
       (9, 3, 'cake_image_url9', 0, now(), now()),
       (10, 3, 'cake_image_url10', 0, now(), now());

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
       (10, 3, 6, '7:00:00', '19:00:00', now(), now());

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
       ('010-3375-5555', 3, 3)
