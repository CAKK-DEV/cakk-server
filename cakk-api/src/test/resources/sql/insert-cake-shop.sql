insert into cake_shop (shop_id, thumbnail_url, shop_name, shop_bio, shop_description, latitude, longitude, like_count, linked_flag,
                       created_at, updated_at)
values (1, 'thumbnail_url1', '케이크 맛집1', '케이크 맛집입니다.', '케이크 맛집입니다.', 37.123456, 127.123456, 0, false, now(), now()),
       (2, 'thumbnail_url2', '케이크 맛집2', '케이크 맛집입니다.', '케이크 맛집입니다.', 38.123456, 128.123456, 0, false, now(), now()),
       (3, 'thumbnail_url3', '케이크 맛집3', '케이크 맛집입니다.', '케이크 맛집입니다.', 39.123456, 129.123456, 0, false, now(), now());

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
