package com.cakk.api.service.like;

import static org.junit.Assert.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.*;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.transaction.annotation.Transactional;

import com.cakk.api.common.annotation.TestWithDisplayName;
import com.cakk.domain.mysql.entity.cake.Cake;
import com.cakk.domain.mysql.entity.user.User;
import com.cakk.domain.mysql.repository.reader.CakeLikeReader;
import com.cakk.domain.mysql.repository.reader.CakeReader;
import com.cakk.domain.mysql.repository.reader.UserReader;
import com.cakk.domain.mysql.repository.writer.CakeLikeWriter;

@SpringBootTest(
	properties = "spring.profiles.active=test",
	webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
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
	private CakeLikeWriter cakeLikeWriter;

	@Autowired
	private CakeLikeReader cakeLikeReader;

	@Autowired
	private CakeReader cakeReader;

	@Autowired
	private UserReader userReader;

	private List<User> userList;

	@BeforeEach
	void initUserList() {
		userList = userReader.findAll();
	}

	@TestWithDisplayName("동시성 문제가 발생한다.")
	void executeLikeCakeWithoutLock() throws InterruptedException {
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
					likeService.likeCake(userList.get(index), cakeId);
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();

		// then
		final Cake cake = cakeReader.findById(cakeId);
		assertNotEquals(100, cake.getLikeCount().intValue());
	}

	@TestWithDisplayName("동시성 문제가 발생하지 않는다.")
	void executeLikeCakeWithLock() throws InterruptedException {
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
					likeService.likeCakeWithLock(userList.get(index), cakeId);
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();

		// then
		final Cake cake = cakeReader.findById(cakeId);
		assertEquals(100, cake.getLikeCount().intValue());
	}
}
