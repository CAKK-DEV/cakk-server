insert into cake_shop (shop_id, thumbnail_url, shop_name, shop_bio, shop_description, latitude, longitude, like_count, linked_flag,
                       created_at, updated_at)
values (1, 'thumbnail_url', '케이크 맛집', '케이크 맛집입니다.', '케이크 맛집입니다.', 37.123456, 127.123456, 0, false, now(), now());

insert into cake (cake_id, shop_id, cake_image_url, like_count, created_at, updated_at)
values (1, 1, 'cake_image_url1', 0, now(), now()),
       (2, 1, 'cake_image_url2', 0, now(), now()),
       (3, 1, 'cake_image_url3', 0, now(), now()),
       (4, 1, 'cake_image_url4', 0, now(), now()),
       (5, 1, 'cake_image_url5', 0, now(), now()),
       (6, 1, 'cake_image_url6', 0, now(), now()),
       (7, 1, 'cake_image_url7', 0, now(), now()),
       (8, 1, 'cake_image_url8', 0, now(), now()),
       (9, 1, 'cake_image_url9', 0, now(), now()),
       (10, 1, 'cake_image_url10', 0, now(), now());

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
