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
import com.cakk.core.facade.cake.CakeReadFacade
import com.cakk.core.facade.cake.CakeShopReadFacade
import com.cakk.core.facade.user.UserReadFacade
import com.cakk.core.service.like.HeartService
import com.cakk.domain.mysql.entity.user.User

@SpringBootTest(properties = ["spring.profiles.active=test"])
@SqlGroup(
	Sql(
		scripts = ["/sql/insert-heart-for-concurrency-test.sql"], executionPhase = ExecutionPhase.BEFORE_TEST_METHOD
	), Sql(scripts = ["/sql/delete-all.sql"], executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
)
internal class HeartConcurrencyTest {

	@Autowired
	private lateinit var heartService: HeartService

	@Autowired
	private lateinit var cakeReadFacade: CakeReadFacade

	@Autowired
	private lateinit var cakeShopReadFacade: CakeShopReadFacade

	@Autowired
	private lateinit var userReadFacade: UserReadFacade

	private lateinit var userList: List<User>

	@BeforeEach
	fun initUserList() {
		userList = userReadFacade.findAll()
	}

	@TestWithDisplayName("케이크 하트 동작 시, 동시성 문제가 발생하지 않는다.")
	fun executeHeartCakeWithLock() {
		// given
		val threadCount = 100
		val cakeId = 1L

		val executorService = Executors.newFixedThreadPool(32)
		val latch = CountDownLatch(threadCount)

		// when
		for (i in 0 until threadCount) {
			executorService.submit {
				try {
					heartService.heartCake(userList[i], cakeId)
				} finally {
					latch.countDown()
				}
			}
		}

		latch.await()

		// then
		val cake = cakeReadFacade.findById(cakeId)
		cake.heartCount shouldBe 100
	}

	@TestWithDisplayName("케이크 샵 하트 동작 시, 동시성 문제가 발생하지 않는다.")
	fun executeHeartCakeShopWithLock() {
		// given
		val threadCount = 100
		val cakeShopId = 1L

		val executorService = Executors.newFixedThreadPool(32)
		val latch = CountDownLatch(threadCount)

		// when
		for (i in 0 until threadCount) {
			executorService.submit {
				try {
					heartService.heartCakeShop(userList[i], cakeShopId)
				} finally {
					latch.countDown()
				}
			}
		}

		latch.await()

		// then
		val cakeShop = cakeShopReadFacade.findById(cakeShopId)
		cakeShop.heartCount shouldBe 100
	}
}
