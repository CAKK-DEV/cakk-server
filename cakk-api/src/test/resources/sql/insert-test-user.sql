insert into users (user_id, provider, provider_id, nickname, profile_image_url, email, gender, birthday, role, created_at, updated_at,
                   deleted_at)
values (1, 'GOOGLE', '123456', '테스트 유저', 'image_url', 'test@google.com', 'MALE', '1998-01-01', 'USER', now(), now(), null);
