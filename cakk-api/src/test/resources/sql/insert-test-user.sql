insert into users (user_id, provider, provider_id, nickname, profile_image_url, email, gender, birthday, role, created_at, updated_at,
                   deleted_at)
values (1, 'GOOGLE', '123456', '테스트 유저1', 'image_url1', 'test1@google.com', 'MALE', '1998-01-01', 'USER', now(), now(), null),
       (2, 'GOOGLE', '123457', '테스트 유저2', 'image_url2', 'test2@google.com', 'MALE', '1998-01-02', 'USER', now(), now(), null),
       (3, 'GOOGLE', '123458', '테스트 유저3', 'image_url3', 'test3@google.com', 'MALE', '1998-01-03', 'USER', now(), now(), null);
