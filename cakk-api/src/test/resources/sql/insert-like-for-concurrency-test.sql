-- users
INSERT INTO users (user_id, provider, provider_id, nickname, profile_image_url, email, gender, birthday, role, created_at, updated_at, deleted_at)
VALUES
    (1, 'GOOGLE', '1', '테스트 유저1', 'image_url1', 'test1@google.com', 'MALE', '1998-01-01', 'USER', NOW(), NOW(), NULL);


-- cake_shop
SET @g1 = 'Point(37.197734 127.098190)';

insert into cake_shop (shop_id, thumbnail_url, shop_name, shop_bio, shop_description, location, like_count, heart_count,
                       created_at, updated_at)
values (1, 'thumbnail_url', '케이크 맛집', '케이크 맛집입니다.', '케이크 맛집입니다.', ST_GeomFromText(@g1, 4326), 0, 0, now(), now());
