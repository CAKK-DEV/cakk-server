package com.cakk.api.integration.concurrency

import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.Sql.ExecutionPhase
import org.springframework.test.context.jdbc.SqlGroup

import io.kotest.matchers.shouldBe

import com.cakk.api.common.annotation.TestWithDisplayName
import com.cakk.core.facade.cake.CakeShopReadFacade
import com.cakk.core.facade.user.UserReadFacade
import com.cakk.core.service.like.LikeService
import com.cakk.infrastructure.persistence.entity.user.User

@SpringBootTest(properties = ["spring.profiles.active=test"])
@SqlGroup(
	Sql(
		scripts = ["/sql/insert-like-for-concurrency-test.sql"], executionPhase = ExecutionPhase.BEFORE_TEST_METHOD
	), Sql(scripts = ["/sql/delete-all.sql"], executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
)
class LikeConcurrencyTest {

	@Autowired
	private lateinit var likeService: LikeService

	@Autowired
	private lateinit var cakeShopReadFacade: CakeShopReadFacade

	@Autowired
	private lateinit var userReadFacade: UserReadFacade

	private lateinit var user: com.cakk.infrastructure.persistence.entity.user.User

	@BeforeEach
	fun initUser() {
		user = userReadFacade.findByUserId(1L)
	}

	@TestWithDisplayName("케이크샵 좋아요 동작 시, 동시성 문제가 발생하지 않는다.")
	fun executeLikeCakeShopWithLock() {
		// given
		val threadCount = 50
		val cakeShopId = 1L

		val executorService = Executors.newFixedThreadPool(32)
		val latch = CountDownLatch(threadCount)

		// when
		for (i in 0 until threadCount) {
			executorService.submit {
				try {
					likeService.likeCakeShop(user, cakeShopId)
				} finally {
					latch.countDown()
				}
			}
		}

		latch.await()

		// then
		val cakeShop = cakeShopReadFacade.findById(cakeShopId)
		cakeShop.likeCount shouldBe 50
	}
}
