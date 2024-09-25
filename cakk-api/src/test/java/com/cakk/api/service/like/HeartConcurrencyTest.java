package com.cakk.api.service.like;

import static org.junit.Assert.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.*;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import com.cakk.api.common.annotation.TestWithDisplayName;
import com.cakk.core.facade.cake.CakeReadFacade;
import com.cakk.core.facade.cake.CakeShopReadFacade;
import com.cakk.core.facade.user.UserReadFacade;
import com.cakk.core.service.like.HeartService;
import com.cakk.domain.mysql.entity.cake.Cake;
import com.cakk.domain.mysql.entity.shop.CakeShop;
import com.cakk.domain.mysql.entity.user.User;

@SpringBootTest(properties = "spring.profiles.active=test")
@SqlGroup({
	@Sql(scripts = {
		"/sql/insert-heart-for-concurrency-test.sql"
	}, executionPhase = BEFORE_TEST_METHOD),
	@Sql(scripts = "/sql/delete-all.sql", executionPhase = AFTER_TEST_METHOD)
})
class HeartConcurrencyTest {

	@Autowired
	private HeartService heartService;

	@Autowired
	private CakeReadFacade cakeReadFacade;

	@Autowired
	private CakeShopReadFacade cakeShopReadFacade;

	@Autowired
	private UserReadFacade userReadFacade;

	private List<User> userList;

	@BeforeEach
	void initUserList() {
		userList = userReadFacade.findAll();
	}

	@TestWithDisplayName("케이크 하트 동작 시, 동시성 문제가 발생하지 않는다.")
	void executeHeartCakeWithLock() throws InterruptedException {
		// given
		final int threadCount = 100;
		final Long cakeId = 1L;

		ExecutorService executorService = Executors.newFixedThreadPool(32);
		CountDownLatch latch = new CountDownLatch(threadCount);

		// when
		for (int i = 0; i < threadCount; i++) {
			final int index = i;

			executorService.submit(() -> {
				try {
					heartService.heartCake(userList.get(index), cakeId);
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();

		// then
		final Cake cake = cakeReadFacade.findById(cakeId);
		assertEquals(100, cake.getHeartCount().intValue());
	}

	@TestWithDisplayName("케이크 샵 하트 동작 시, 동시성 문제가 발생하지 않는다.")
	void executeHeartCakeShopWithLock() throws InterruptedException {
		// given
		final int threadCount = 100;
		final Long cakeShopId = 1L;

		ExecutorService executorService = Executors.newFixedThreadPool(32);
		CountDownLatch latch = new CountDownLatch(threadCount);

		// when
		for (int i = 0; i < threadCount; i++) {
			final int index = i;

			executorService.submit(() -> {
				try {
					heartService.heartCakeShop(userList.get(index), cakeShopId);
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();

		// then
		final CakeShop cakeShop = cakeShopReadFacade.findById(cakeShopId);
		assertEquals(100, cakeShop.getHeartCount().intValue());
	}
}
