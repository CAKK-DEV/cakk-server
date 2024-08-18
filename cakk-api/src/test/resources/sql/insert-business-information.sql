SET @g1 = 'Point(37.197734 127.098190)';
SET @g2 = 'Point(37.201623 127.091568)';
SET @g3 = 'Point(37.209769 127.100107)';

insert into cake_shop (shop_id, thumbnail_url, shop_name, shop_address, shop_bio, shop_description, location, like_count, heart_count, created_at, updated_at)
values (1, 'thumbnail_url1', '케이크 맛집1', '서울시 강남구 어쩌고로1', '케이크 맛집입니다.', '케이크 맛집입니다.', ST_GeomFromText(@g1, 4326), 0, 0, now(), now()),
       (2, 'thumbnail_url2', '케이크 맛집2', '서울시 강남구 어쩌고로2', '케이크 맛집입니다.', '케이크 맛집입니다.', ST_GeomFromText(@g2, 4326), 0, 0, now(), now()),
       (3, 'thumbnail_url3', '케이크 맛집3', '서울시 강남구 어쩌고로3', '케이크 맛집입니다.', '케이크 맛집입니다.', ST_GeomFromText(@g3, 4326), 0, 0, now(), now());

insert into business_information(business_number, business_registration_image_url, id_card_image_url, emergency_contact, shop_id, user_id)
values ('010-3375-5556', 'https://business_registration_image_url2', 'https://id_card_image_url2', '010-0000-0001', 2, 2),
       ('010-3375-5557', 'https://business_registration_image_url3', 'https://id_card_image_url3', '010-0000-0002', 3, 3);

insert into business_information(business_number, business_registration_image_url, id_card_image_url, emergency_contact, shop_id, user_id, verification_status)
values ('010-3375-5555', 'https://business_registration_image_url1', 'https://id_card_image_url1', '010-0000-0000', 1, 1, 3);