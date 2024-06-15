package com.cakk.api.service.like;

import static org.junit.Assert.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import com.cakk.api.common.annotation.TestWithDisplayName;
import com.cakk.domain.mysql.entity.shop.CakeShop;
import com.cakk.domain.mysql.entity.user.User;
import com.cakk.domain.mysql.repository.reader.CakeShopReader;
import com.cakk.domain.mysql.repository.reader.UserReader;

@SpringBootTest(properties = "spring.profiles.active=test")
@SqlGroup({
	@Sql(scripts = {
		"/sql/insert-like-for-concurrency-test.sql"
	}, executionPhase = BEFORE_TEST_METHOD),
	@Sql(scripts = "/sql/delete-all.sql", executionPhase = AFTER_TEST_METHOD)
})
public class LikeConcurrencyTest {

	@Autowired
	private LikeService likeService;

	@Autowired
	private CakeShopReader cakeShopReader;

	@Autowired
	private UserReader userReader;

	private User user;

	@BeforeEach
	void initUser() {
		user = userReader.findByUserId(1L);
	}

	@TestWithDisplayName("케이크샵 좋아요 동작 시, 동시성 문제가 발생하지 않는다.")
	void executeLikeCakeShopWithLock() throws InterruptedException {
		// given
		final int threadCount = 50;
		final Long cakeShopId = 1L;

		ExecutorService executorService = Executors.newFixedThreadPool(32);
		CountDownLatch latch = new CountDownLatch(threadCount);

		// when
		for (int i = 0; i < threadCount; i++) {
			executorService.submit(() -> {
				try {
					likeService.likeCakeShop(user, cakeShopId);
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();

		// then
		final CakeShop cakeShop = cakeShopReader.findById(cakeShopId);
		assertEquals(50, cakeShop.getLikeCount().intValue());
	}
}
