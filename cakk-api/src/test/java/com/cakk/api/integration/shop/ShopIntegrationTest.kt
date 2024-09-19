package com.cakk.api.integration.shop

import com.cakk.api.common.annotation.TestWithDisplayName
import com.cakk.api.common.base.IntegrationTest
import com.cakk.api.dto.param.operation.ShopOperationParam
import com.cakk.api.dto.request.link.UpdateLinkRequest
import com.cakk.api.dto.request.operation.UpdateShopOperationRequest
import com.cakk.api.dto.request.shop.UpdateShopAddressRequest
import com.cakk.api.dto.request.shop.UpdateShopRequest
import com.cakk.api.dto.request.user.CertificationRequest
import com.cakk.api.dto.response.like.HeartResponse
import com.cakk.api.dto.response.shop.*
import com.cakk.common.enums.ReturnCode
import com.cakk.common.response.ApiResponse
import com.cakk.core.facade.cake.CakeShopReadFacade
import com.cakk.domain.mysql.dto.param.shop.CakeShopLinkParam
import com.cakk.domain.mysql.dto.param.shop.CakeShopLocationResponseParam
import com.cakk.domain.mysql.dto.param.shop.CakeShopOperationParam
import com.cakk.domain.mysql.dto.param.shop.CakeShopSearchResponseParam
import com.cakk.domain.mysql.entity.shop.CakeShopLink
import com.cakk.domain.mysql.entity.shop.CakeShopOperation
import com.cakk.domain.redis.repository.CakeShopViewsRedisRepository
import com.cakk.domain.redis.repository.CakeViewsRedisRepository
import io.kotest.matchers.shouldBe
import net.jqwik.api.Arbitraries
import org.assertj.core.api.Assertions
import org.junit.Assert
import org.junit.jupiter.api.AfterEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatusCode
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.Sql.ExecutionPhase
import org.springframework.test.context.jdbc.SqlGroup
import org.springframework.web.util.UriComponentsBuilder
import java.util.function.Consumer
import java.util.stream.Collectors

@SqlGroup(
    Sql(
        scripts = ["/sql/insert-test-user.sql", "/sql/insert-cake-shop.sql", "/sql/insert-heart.sql"],
		executionPhase = ExecutionPhase.BEFORE_TEST_METHOD
    ), Sql(
		scripts = ["/sql/delete-all.sql"],
		executionPhase = ExecutionPhase.AFTER_TEST_METHOD
	)
)
internal class ShopIntegrationTest(
	@LocalServerPort private val port: Int
) : IntegrationTest() {

	protected val baseUrl = "$localhost$port/api/v1/shops"

    @Autowired
    private lateinit var cakeShopReadFacade: CakeShopReadFacade

    @Autowired
    private lateinit var cakeViewsRedisRepository: CakeViewsRedisRepository

    @Autowired
    private lateinit var cakeShopViewsRedisRepository: CakeShopViewsRedisRepository

    private fun initializeViews() {
        cakeShopViewsRedisRepository.saveOrIncreaseSearchCount(1L)
        cakeShopViewsRedisRepository.saveOrIncreaseSearchCount(1L)
        cakeShopViewsRedisRepository.saveOrIncreaseSearchCount(1L)
        cakeShopViewsRedisRepository.saveOrIncreaseSearchCount(1L)
        cakeShopViewsRedisRepository.saveOrIncreaseSearchCount(2L)
        cakeShopViewsRedisRepository.saveOrIncreaseSearchCount(2L)
        cakeShopViewsRedisRepository.saveOrIncreaseSearchCount(2L)
        cakeShopViewsRedisRepository.saveOrIncreaseSearchCount(3L)
        cakeShopViewsRedisRepository.saveOrIncreaseSearchCount(3L)
        cakeShopViewsRedisRepository.saveOrIncreaseSearchCount(4L)
        cakeShopViewsRedisRepository.saveOrIncreaseSearchCount(5L)
        cakeShopViewsRedisRepository.saveOrIncreaseSearchCount(6L)
        cakeShopViewsRedisRepository.saveOrIncreaseSearchCount(7L)
        cakeShopViewsRedisRepository.saveOrIncreaseSearchCount(7L)
        cakeShopViewsRedisRepository.saveOrIncreaseSearchCount(1L)
        cakeShopViewsRedisRepository.saveOrIncreaseSearchCount(7L)
        cakeShopViewsRedisRepository.saveOrIncreaseSearchCount(8L)
        cakeShopViewsRedisRepository.saveOrIncreaseSearchCount(9L)
    }

    @AfterEach
    fun setUp() {
        cakeViewsRedisRepository.clear()
        cakeShopViewsRedisRepository.clear()
    }

    @TestWithDisplayName("케이크 샵을 간단 조회에 성공한다.")
    fun simple1() {
        val cakeShopId = 1L
        val uriComponents = UriComponentsBuilder
            .fromUriString(baseUrl)
            .path("/{cakeShopId}/simple")
            .buildAndExpand(cakeShopId)

        // when
        val responseEntity = restTemplate.getForEntity(
            uriComponents.toUriString(),
            ApiResponse::class.java
        )

        // then
        val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)
        val data = objectMapper.convertValue(response.data, CakeShopSimpleResponse::class.java)

		responseEntity.statusCode shouldBe HttpStatusCode.valueOf(200)
		response.returnCode shouldBe ReturnCode.SUCCESS.code
		response.returnMessage shouldBe ReturnCode.SUCCESS.message

        val cakeShop = cakeShopReadFacade.findById(cakeShopId)
		data.cakeShopId shouldBe cakeShop.id
		data.thumbnailUrl shouldBe cakeShop.thumbnailUrl
		data.cakeShopName shouldBe cakeShop.shopName
		data.cakeShopBio shouldBe cakeShop.shopBio
    }

    @TestWithDisplayName("케이크 샵을 간단 조회 시, 케이크 조회수를 올리는 데 성공한다.")
    fun simple2() {
        val cakeShopId = 1L
        val cakeId = 1L
        val uriComponents = UriComponentsBuilder
            .fromUriString(baseUrl)
            .path("/{cakeShopId}/simple")
            .queryParam("cakeId", cakeId)
            .buildAndExpand(cakeShopId)

        // when
        val responseEntity = restTemplate.getForEntity(
            uriComponents.toUriString(),
            ApiResponse::class.java
        )

        // then
        val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)
        val data = objectMapper.convertValue(response.data, CakeShopSimpleResponse::class.java)

        Assert.assertEquals(HttpStatusCode.valueOf(200), responseEntity.statusCode)
        Assert.assertEquals(ReturnCode.SUCCESS.code, response.returnCode)
        Assert.assertEquals(ReturnCode.SUCCESS.message, response.returnMessage)

        val cakeShop = cakeShopReadFacade.findById(cakeShopId)
        Assert.assertEquals(cakeShop.id, data.cakeShopId)
        Assert.assertEquals(cakeShop.thumbnailUrl, data.thumbnailUrl)
        Assert.assertEquals(cakeShop.shopName, data.cakeShopName)
        Assert.assertEquals(cakeShop.shopBio, data.cakeShopBio)

        val viewCakeId = cakeViewsRedisRepository.findTopCakeIdsByOffsetAndCount(0, 10)[0]
        Assert.assertEquals(cakeId, viewCakeId)
    }

    @TestWithDisplayName("없는 케이크 샵일 경우, 간단 조회에 실패한다.")
    fun simple3() {
        val url = "%s%d%s".formatted(BASE_URL, port, API_URL)
        val cakeShopId = 1000L
        val uriComponents = UriComponentsBuilder
            .fromUriString(url)
            .path("/{cakeShopId}/simple")
            .buildAndExpand(cakeShopId)

        // when
        val responseEntity = restTemplate.getForEntity(
            uriComponents.toUriString(),
            ApiResponse::class.java
        )

        // then
        val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)

        Assert.assertEquals(HttpStatusCode.valueOf(400), responseEntity.statusCode)
        Assert.assertEquals(ReturnCode.NOT_EXIST_CAKE_SHOP.code, response.returnCode)
        Assert.assertEquals(ReturnCode.NOT_EXIST_CAKE_SHOP.message, response.returnMessage)
    }

    @TestWithDisplayName("케이크 샵을 상세 조회에 성공한다.")
    fun detail1() {
        val url = "%s%d%s".formatted(BASE_URL, port, API_URL)
        val cakeShopId = 1L
        val uriComponents = UriComponentsBuilder
            .fromUriString(url)
            .path("/{cakeShopId}")
            .buildAndExpand(cakeShopId)

        // when
        val responseEntity = restTemplate.getForEntity(
            uriComponents.toUriString(),
            ApiResponse::class.java
        )

        // then
        val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)
        val data = objectMapper.convertValue(response.data, CakeShopDetailResponse::class.java)

        Assert.assertEquals(HttpStatusCode.valueOf(200), responseEntity.statusCode)
        Assert.assertEquals(ReturnCode.SUCCESS.code, response.returnCode)
        Assert.assertEquals(ReturnCode.SUCCESS.message, response.returnMessage)

        val cakeShop = cakeShopReadFacade!!.findById(cakeShopId)
        Assert.assertEquals(cakeShop.id, data.cakeShopId)
        Assert.assertEquals(cakeShop.thumbnailUrl, data.thumbnailUrl)
        Assert.assertEquals(cakeShop.shopName, data.cakeShopName)
        Assert.assertEquals(cakeShop.shopBio, data.cakeShopBio)
        Assert.assertEquals(cakeShop.shopDescription, data.cakeShopDescription)

        val cakeShopOperations = cakeShopReadFacade.findCakeShopOperationsByCakeShopId(cakeShopId).stream()
            .map { obj: CakeShopOperation -> obj.operationDay }
            .collect(Collectors.toSet())
        Assert.assertEquals(cakeShopOperations, data.operationDays)

        val cakeShopLinks = cakeShopReadFacade.findCakeShopLinksByCakeShopId(cakeShopId).stream()
            .map { it: CakeShopLink -> CakeShopLinkParam(it.linkKind, it.linkPath) }
            .collect(Collectors.toSet())
        Assert.assertEquals(cakeShopLinks, data.links)
    }

    @TestWithDisplayName("없는 케이크 샵일 경우, 상세 조회에 실패한다.")
    fun detail2() {
        val url = "%s%d%s".formatted(BASE_URL, port, API_URL)
        val cakeShopId = 1000L
        val uriComponents = UriComponentsBuilder
            .fromUriString(url)
            .path("/{cakeShopId}")
            .buildAndExpand(cakeShopId)

        // when
        val responseEntity = restTemplate.getForEntity(
            uriComponents.toUriString(),
            ApiResponse::class.java
        )

        // then
        val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)

        Assert.assertEquals(HttpStatusCode.valueOf(400), responseEntity.statusCode)
        Assert.assertEquals(ReturnCode.NOT_EXIST_CAKE_SHOP.code, response.returnCode)
        Assert.assertEquals(ReturnCode.NOT_EXIST_CAKE_SHOP.message, response.returnMessage)
    }

    @TestWithDisplayName("로그인한 사용자는 자신의 케이크샵이 존재하는 상태에서 사장님 인증을 요청한다")
    fun request2() {
        val url = "%s%d%s".formatted(BASE_URL, port, API_URL)
        val uriComponents = UriComponentsBuilder
            .fromUriString(url)
            .path("/certification")
            .build()
        val request = getConstructorMonkey().giveMeBuilder(
            CertificationRequest::class.java
        )
            .set("businessRegistrationImageUrl", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(40).ofMinLength(1))
            .set("idCardImageUrl", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(40).ofMinLength(1))
            .set("cakeShopId", 11L)
            .set("emergencyContact", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(11).ofMinLength(1))
            .set("message", Arbitraries.strings().withCharRange('a', 'z').ofMaxLength(20)).sample()

        val entity = HttpEntity(request, authHeader)

        val responseEntity = restTemplate.postForEntity(
            uriComponents.toUriString(), entity,
            ApiResponse::class.java
        )
        val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)

        Assert.assertEquals(HttpStatusCode.valueOf(200), responseEntity.statusCode)
        Assert.assertEquals(ReturnCode.SUCCESS.code, response.returnCode)
        Assert.assertEquals(ReturnCode.SUCCESS.message, response.returnMessage)
    }

    @TestWithDisplayName("케이크 샵의 상세 정보 조회에 성공한다.")
    fun detailInfo1() {
        val url = "%s%d%s".formatted(BASE_URL, port, API_URL)
        val cakeShopId = 1L
        val uriComponents = UriComponentsBuilder
            .fromUriString(url)
            .path("/{cakeShopId}/info")
            .buildAndExpand(cakeShopId)

        // when
        val responseEntity = restTemplate.getForEntity(
            uriComponents.toUriString(),
            ApiResponse::class.java
        )

        // then
        val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)
        val data = objectMapper.convertValue(response.data, CakeShopInfoResponse::class.java)

        Assert.assertEquals(HttpStatusCode.valueOf(200), responseEntity.statusCode)
        Assert.assertEquals(ReturnCode.SUCCESS.code, response.returnCode)
        Assert.assertEquals(ReturnCode.SUCCESS.message, response.returnMessage)

        val cakeShop = cakeShopReadFacade!!.findById(cakeShopId)
        val longitude = cakeShop.location.x
        val latitude = cakeShop.location.y

        Assert.assertEquals(cakeShop.shopAddress, data.shopAddress)
        Assert.assertEquals(latitude, data.latitude)
        Assert.assertEquals(longitude, data.longitude)

        val cakeShopOperations = cakeShopReadFacade.findCakeShopOperationsByCakeShopId(cakeShopId).stream()
            .map { it: CakeShopOperation -> CakeShopOperationParam(it.operationDay, it.operationStartTime, it.operationEndTime) }
            .toList()
        Assert.assertEquals(cakeShopOperations, data.shopOperationDays)
    }

    @TestWithDisplayName("없는 케이크 샵일 경우, 상세정보 조회에 실패한다.")
    fun detailInfo2() {
        val url = "%s%d%s".formatted(BASE_URL, port, API_URL)
        val cakeShopId = 1000L
        val uriComponents = UriComponentsBuilder
            .fromUriString(url)
            .path("/{cakeShopId}/info")
            .buildAndExpand(cakeShopId)

        // when
        val responseEntity = restTemplate.getForEntity(
            uriComponents.toUriString(),
            ApiResponse::class.java
        )

        // then
        val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)

        Assert.assertEquals(HttpStatusCode.valueOf(400), responseEntity.statusCode)
        Assert.assertEquals(ReturnCode.NOT_EXIST_CAKE_SHOP.code, response.returnCode)
        Assert.assertEquals(ReturnCode.NOT_EXIST_CAKE_SHOP.message, response.returnMessage)
    }

    @TestWithDisplayName("테스트 sql script 기준으로 사용자 위치를 중심으로 반경 10km 이내의 가게들을 조회한다")
    fun findAllShopsByLocationBased1() {
        val url = "%s%d%s".formatted(BASE_URL, port, API_URL)
        val uriComponents = UriComponentsBuilder
            .fromUriString(url)
            .path("/location-based")
            .queryParam("latitude", 37.2096575)
            .queryParam("longitude", 127.0998228)
            .build()

        //when
        val responseEntity = restTemplate.getForEntity(
            uriComponents.toUriString(),
            ApiResponse::class.java
        )

        //then
        val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)
        val data = objectMapper.convertValue(response.data, CakeShopByMapResponse::class.java)

        Assert.assertEquals(HttpStatusCode.valueOf(200), responseEntity.statusCode)
        Assert.assertEquals(ReturnCode.SUCCESS.code, response.returnCode)
        Assert.assertEquals(ReturnCode.SUCCESS.message, response.returnMessage)

        Assertions.assertThat(data.cakeShops.size).isGreaterThanOrEqualTo(0)
        data.cakeShops.forEach(Consumer { cakeShop: CakeShopLocationResponseParam ->
            Assertions.assertThat(cakeShop.cakeImageUrls.size).isLessThanOrEqualTo(4)
            Assertions.assertThat(cakeShop.cakeShopName).isNotNull()
        })
    }

    @get:TestWithDisplayName("해당 id의 이전에 하트 누른 케이크샵에 대하여 하트 상태인지 조회에 성공한다.")
    val isHeartShop1: Unit
        get() {
            // given
            val cakeShopId = 1L
            val url = "%s%d%s/{cakeShopId}/heart".formatted(BASE_URL, port, API_URL)
            val uriComponents = UriComponentsBuilder
                .fromUriString(url)
                .buildAndExpand(cakeShopId)

            // when
            val responseEntity = restTemplate.exchange(
                uriComponents.toUriString(),
                HttpMethod.GET,
                HttpEntity<Any>(authHeader),
                ApiResponse::class.java
            )

            // then
            val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)
            val data = objectMapper.convertValue(response.data, HeartResponse::class.java)

            Assert.assertEquals(HttpStatusCode.valueOf(200), responseEntity.statusCode)
            Assert.assertEquals(ReturnCode.SUCCESS.code, response.returnCode)
            Assert.assertEquals(ReturnCode.SUCCESS.message, response.returnMessage)

            Assert.assertEquals(true, data.isHeart)
        }

    @get:TestWithDisplayName("해당 id의 이전에 하트 누르지 않은 케이크샵에 대하여 하트 상태인지 조회에 성공한다.")
    val isHeartCake2: Unit
        get() {
            // given
            val cakeId = 2L
            val url = "%s%d%s/{cakeId}/heart".formatted(BASE_URL, port, API_URL)
            val uriComponents = UriComponentsBuilder
                .fromUriString(url)
                .buildAndExpand(cakeId)

            // when
            val responseEntity = restTemplate.exchange(
                uriComponents.toUriString(),
                HttpMethod.GET,
                HttpEntity<Any>(authHeader),
                ApiResponse::class.java
            )

            // then
            val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)
            val data = objectMapper.convertValue(response.data, HeartResponse::class.java)

            Assert.assertEquals(HttpStatusCode.valueOf(200), responseEntity.statusCode)
            Assert.assertEquals(ReturnCode.SUCCESS.code, response.returnCode)
            Assert.assertEquals(ReturnCode.SUCCESS.message, response.returnMessage)

            Assert.assertEquals(false, data.isHeart)
        }

    @TestWithDisplayName("해당 id의 케이크 샵 하트에 성공한다.")
    fun heartCakeShop() {
        // given
        val cakeShopId = 2L
        val url = "%s%d%s/{cakeShopId}/heart".formatted(BASE_URL, port, API_URL)
        val uriComponents = UriComponentsBuilder
            .fromUriString(url)
            .buildAndExpand(cakeShopId)

        // when
        val responseEntity = restTemplate.exchange(
            uriComponents.toUriString(),
            HttpMethod.PUT,
            HttpEntity<Any>(authHeader),
            ApiResponse::class.java
        )

        // then
        val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)

        Assert.assertEquals(HttpStatusCode.valueOf(200), responseEntity.statusCode)
        Assert.assertEquals(ReturnCode.SUCCESS.code, response.returnCode)
        Assert.assertEquals(ReturnCode.SUCCESS.message, response.returnMessage)
        Assert.assertNull(response.data)
    }

    @TestWithDisplayName("해당 id의 케이크 샵 하트 취소에 성공한다.")
    fun heartCancelCake() {
        // given
        val cakeShopId = 1L
        val url = "%s%d%s/{cakeShopId}/heart".formatted(BASE_URL, port, API_URL)
        val uriComponents = UriComponentsBuilder
            .fromUriString(url)
            .buildAndExpand(cakeShopId)

        // when
        val responseEntity = restTemplate.exchange(
            uriComponents.toUriString(),
            HttpMethod.PUT,
            HttpEntity<Any>(authHeader),
            ApiResponse::class.java
        )

        // then
        val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)

        Assert.assertEquals(HttpStatusCode.valueOf(200), responseEntity.statusCode)
        Assert.assertEquals(ReturnCode.SUCCESS.code, response.returnCode)
        Assert.assertEquals(ReturnCode.SUCCESS.message, response.returnMessage)
        Assert.assertNull(response.data)
    }

    @TestWithDisplayName("해당 id의 케이크 샵 좋아요에 성공한다.")
    fun likeCakeShop() {
        // given
        val cakeShopId = 1L
        val url = "%s%d%s/{cakeShopId}/like".formatted(BASE_URL, port, API_URL)
        val uriComponents = UriComponentsBuilder
            .fromUriString(url)
            .buildAndExpand(cakeShopId)

        // when
        val responseEntity = restTemplate.exchange(
            uriComponents.toUriString(),
            HttpMethod.PUT,
            HttpEntity<Any>(authHeader),
            ApiResponse::class.java
        )

        // then
        val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)

        Assert.assertEquals(HttpStatusCode.valueOf(200), responseEntity.statusCode)
        Assert.assertEquals(ReturnCode.SUCCESS.code, response.returnCode)
        Assert.assertEquals(ReturnCode.SUCCESS.message, response.returnMessage)
        Assert.assertNull(response.data)
    }

    @TestWithDisplayName("테스트 sql script 기준으로 사용자 위치를 중심으로 반경 10km 이내의 가게들을 조회한다")
    fun findAllShopsByLocationBased2() {
        val url = "%s%d%s".formatted(BASE_URL, port, API_URL)
        val uriComponents = UriComponentsBuilder
            .fromUriString(url)
            .path("/location-based")
            .queryParam("latitude", 37.543343)
            .queryParam("longitude", 127.052609)
            .build()

        //when
        val responseEntity = restTemplate.getForEntity(
            uriComponents.toUriString(),
            ApiResponse::class.java
        )

        //then
        val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)
        val data = objectMapper.convertValue(response.data, CakeShopByMapResponse::class.java)

        Assert.assertEquals(HttpStatusCode.valueOf(200), responseEntity.statusCode)
        Assert.assertEquals(ReturnCode.SUCCESS.code, response.returnCode)
        Assert.assertEquals(ReturnCode.SUCCESS.message, response.returnMessage)

        Assert.assertEquals(8, data.cakeShops.size.toLong())
        data.cakeShops.forEach(Consumer { cakeShop: CakeShopLocationResponseParam ->
            Assertions.assertThat(cakeShop.cakeShopId).isIn(4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L)
            Assertions.assertThat(cakeShop.cakeImageUrls.size).isLessThanOrEqualTo(4)
            Assertions.assertThat(cakeShop.cakeShopName).isNotNull()
        })
    }

    @TestWithDisplayName("테스트 sql script 기준 7개의 케이크 샵이 조회된다")
    fun searchCakeShopsByKeywordsWithConditions1() {
        val url = "%s%d%s/search/shops".formatted(BASE_URL, port, API_URL)
        val uriComponents = UriComponentsBuilder
            .fromUriString(url)
            .queryParam("cakeShopId", 11L)
            .queryParam("latitude", 37.543343)
            .queryParam("longitude", 127.052609)
            .queryParam("pageSize", 10)
            .build()

        //when
        val responseEntity = restTemplate.getForEntity(
            uriComponents.toUriString(),
            ApiResponse::class.java
        )

        //then
        val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)
        val data = objectMapper.convertValue(response.data, CakeShopSearchResponse::class.java)

        Assert.assertEquals(HttpStatusCode.valueOf(200), responseEntity.statusCode)
        Assert.assertEquals(ReturnCode.SUCCESS.code, response.returnCode)
        Assert.assertEquals(ReturnCode.SUCCESS.message, response.returnMessage)

        Assert.assertEquals(7, data.size.toLong())
        data.cakeShops.forEach(Consumer { cakeShop: CakeShopSearchResponseParam ->
            Assertions.assertThat(cakeShop.cakeImageUrls.size).isLessThanOrEqualTo(4)
            Assertions.assertThat(cakeShop.cakeShopId).isNotNull()
            Assertions.assertThat(cakeShop.cakeShopName).isNotNull()
            Assertions.assertThat(cakeShop.operationDays).isNotNull()
        })
    }

    @TestWithDisplayName("케이크샵 검색 시, 다양한 동적 조건에 따라 조회된다")
    fun searchCakeShopsByKeywordWithConditions2() {
        val url = "%s%d%s/search/shops".formatted(BASE_URL, port, API_URL)
        val uriComponents = UriComponentsBuilder
            .fromUriString(url)
            .queryParam("keyword", "케이크")
            .queryParam("latitude", 37.2096575)
            .queryParam("longitude", 127.0998228)
            .queryParam("pageSize", 10)
            .build()

        //when
        val responseEntity = restTemplate.getForEntity(
            uriComponents.toUriString(),
            ApiResponse::class.java
        )

        //then
        val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)
        val data = objectMapper.convertValue(response.data, CakeShopSearchResponse::class.java)

        Assert.assertEquals(HttpStatusCode.valueOf(200), responseEntity.statusCode)
        Assert.assertEquals(ReturnCode.SUCCESS.code, response.returnCode)
        Assert.assertEquals(ReturnCode.SUCCESS.message, response.returnMessage)

        Assertions.assertThat(data.size).isGreaterThanOrEqualTo(0)
        data.cakeShops.forEach(Consumer { cakeShop: CakeShopSearchResponseParam ->
            Assertions.assertThat(cakeShop.cakeImageUrls.size).isLessThanOrEqualTo(4)
            Assertions.assertThat(cakeShop.cakeShopId).isNotNull()
            Assertions.assertThat(cakeShop.cakeShopName).isNotNull()
            Assertions.assertThat(cakeShop.operationDays).isNotNull()
        })
    }

    @TestWithDisplayName("위치 정보 없이 4개의 케이크샵이 조회된다")
    fun searchCakeShopsByKeywordWithConditions3() {
        val url = "%s%d%s/search/shops".formatted(BASE_URL, port, API_URL)
        val uriComponents = UriComponentsBuilder
            .fromUriString(url)
            .queryParam("cakeShopId", 5)
            .queryParam("keyword", "케이크")
            .queryParam("pageSize", 10)
            .build()

        //when
        val responseEntity = restTemplate.getForEntity(
            uriComponents.toUriString(),
            ApiResponse::class.java
        )

        //then
        val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)
        val data = objectMapper.convertValue(response.data, CakeShopSearchResponse::class.java)

        Assert.assertEquals(HttpStatusCode.valueOf(200), responseEntity.statusCode)
        Assert.assertEquals(ReturnCode.SUCCESS.code, response.returnCode)
        Assert.assertEquals(ReturnCode.SUCCESS.message, response.returnMessage)

        Assert.assertEquals(4, data.size.toLong())
        data.cakeShops.forEach(Consumer { cakeShop: CakeShopSearchResponseParam ->
            Assertions.assertThat(cakeShop.cakeImageUrls.size).isLessThanOrEqualTo(4)
            Assertions.assertThat(cakeShop.cakeShopId).isNotNull()
            Assertions.assertThat(cakeShop.cakeShopName).isNotNull()
            Assertions.assertThat(cakeShop.operationDays).isNotNull()
        })
    }

    @TestWithDisplayName("케이크 샵 기본 정보 업데이트에 성공한다")
    fun updateCakeShopDefaultInfo() {
        // given
        val cakeShopId = 1L
        val url = "%s%d%s/{cakeShopId}".formatted(BASE_URL, port, API_URL)
        val uriComponents = UriComponentsBuilder
            .fromUriString(url)
            .buildAndExpand(cakeShopId)
        val request = getConstructorMonkey().giveMeBuilder(UpdateShopRequest::class.java).sample()

        // when
        val responseEntity = restTemplate.exchange(
            uriComponents.toUriString(),
            HttpMethod.PUT,
            HttpEntity(request, authHeader),
            ApiResponse::class.java
        )

        // then
        val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)

        Assert.assertEquals(HttpStatusCode.valueOf(200), responseEntity.statusCode)
        Assert.assertEquals(ReturnCode.SUCCESS.code, response.returnCode)
        Assert.assertEquals(ReturnCode.SUCCESS.message, response.returnMessage)
    }

    @TestWithDisplayName("어드민에 의해 케이크 샵 기본 정보 업데이트에 성공한다")
    fun updateCakeShopDefaultInfoByAdmin() {
        // given
        val cakeShopId = 1L
        val url = "%s%d%s/{cakeShopId}".formatted(BASE_URL, port, API_URL)
        val uriComponents = UriComponentsBuilder
            .fromUriString(url)
            .buildAndExpand(cakeShopId)
        val request = getConstructorMonkey().giveMeBuilder(UpdateShopRequest::class.java).sample()

        // when
        val responseEntity = restTemplate.exchange(
            uriComponents.toUriString(),
            HttpMethod.PUT,
            HttpEntity(request, getAuthHeaderById(10L)),
            ApiResponse::class.java
        )

        // then
        val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)

        Assert.assertEquals(HttpStatusCode.valueOf(200), responseEntity.statusCode)
        Assert.assertEquals(ReturnCode.SUCCESS.code, response.returnCode)
        Assert.assertEquals(ReturnCode.SUCCESS.message, response.returnMessage)
    }

    @TestWithDisplayName("케이크샵 외부 링크 업데이트에 성공한다")
    fun updateShopLinks() {
        // given
        val cakeShopId = 1L
        val url = "%s%d%s/{cakeShopId}/links".formatted(BASE_URL, port, API_URL)
        val uriComponents = UriComponentsBuilder
            .fromUriString(url)
            .buildAndExpand(cakeShopId)
        val request = getConstructorMonkey().giveMeBuilder(UpdateLinkRequest::class.java).sample()

        // when
        val responseEntity = restTemplate.exchange(
            uriComponents.toUriString(),
            HttpMethod.PUT,
            HttpEntity(request, authHeader),
            ApiResponse::class.java
        )

        // then
        val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)

        Assert.assertEquals(HttpStatusCode.valueOf(200), responseEntity.statusCode)
        Assert.assertEquals(ReturnCode.SUCCESS.code, response.returnCode)
        Assert.assertEquals(ReturnCode.SUCCESS.message, response.returnMessage)
    }

    @TestWithDisplayName("어드민에 의해 케이크샵 외부 링크 업데이트에 성공한다")
    fun updateShopLinksByAdmin() {
        // given
        val cakeShopId = 1L
        val url = "%s%d%s/{cakeShopId}/links".formatted(BASE_URL, port, API_URL)
        val uriComponents = UriComponentsBuilder
            .fromUriString(url)
            .buildAndExpand(cakeShopId)
        val request = getConstructorMonkey().giveMeBuilder(UpdateLinkRequest::class.java).sample()

        // when
        val responseEntity = restTemplate.exchange(
            uriComponents.toUriString(),
            HttpMethod.PUT,
            HttpEntity(request, getAuthHeaderById(10L)),
            ApiResponse::class.java
        )

        // then
        val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)

        Assert.assertEquals(HttpStatusCode.valueOf(200), responseEntity.statusCode)
        Assert.assertEquals(ReturnCode.SUCCESS.code, response.returnCode)
        Assert.assertEquals(ReturnCode.SUCCESS.message, response.returnMessage)
    }

    @TestWithDisplayName("케이크 샵 주소 업데이트에 성공한다")
    fun updateShopAddress() {
        // given
        val cakeShopId = 1L
        val url = "%s%d%s/{cakeShopId}/address".formatted(BASE_URL, port, API_URL)
        val uriComponents = UriComponentsBuilder
            .fromUriString(url)
            .buildAndExpand(cakeShopId)
        val request = getConstructorMonkey().giveMeBuilder(UpdateShopAddressRequest::class.java).sample()

        // when
        val responseEntity = restTemplate.exchange(
            uriComponents.toUriString(),
            HttpMethod.PUT,
            HttpEntity(request, authHeader),
            ApiResponse::class.java
        )

        // then
        val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)

        Assert.assertEquals(HttpStatusCode.valueOf(200), responseEntity.statusCode)
        Assert.assertEquals(ReturnCode.SUCCESS.code, response.returnCode)
        Assert.assertEquals(ReturnCode.SUCCESS.message, response.returnMessage)
    }

    @TestWithDisplayName("어드민에 의해 케이크 샵 주소 업데이트에 성공한다")
    fun updateShopAddressByAdmin() {
        // given
        val cakeShopId = 1L
        val url = "%s%d%s/{cakeShopId}/address".formatted(BASE_URL, port, API_URL)
        val uriComponents = UriComponentsBuilder
            .fromUriString(url)
            .buildAndExpand(cakeShopId)
        val request = getConstructorMonkey().giveMeBuilder(UpdateShopAddressRequest::class.java).sample()

        // when
        val responseEntity = restTemplate.exchange(
            uriComponents.toUriString(),
            HttpMethod.PUT,
            HttpEntity(request, getAuthHeaderById(10L)),
            ApiResponse::class.java
        )

        // then
        val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)

        Assert.assertEquals(HttpStatusCode.valueOf(200), responseEntity.statusCode)
        Assert.assertEquals(ReturnCode.SUCCESS.code, response.returnCode)
        Assert.assertEquals(ReturnCode.SUCCESS.message, response.returnMessage)
    }

    @TestWithDisplayName("케이크 샵 영업일 업데이트에 성공한다")
    fun updateShopOperationDays() {
        // given
        val cakeShopId = 1L
        val url = "%s%d%s/{cakeShopId}/operation-days".formatted(BASE_URL, port, API_URL)
        val uriComponents = UriComponentsBuilder
            .fromUriString(url)
            .buildAndExpand(cakeShopId)
        val shopOperationParams = getConstructorMonkey().giveMeBuilder(
            ShopOperationParam::class.java
        )
            .setNotNull("operationDay")
            .setNotNull("operationStartTime")
            .setNotNull("operationEndTime").sampleList(6)
        val request = getConstructorMonkey().giveMeBuilder(
            UpdateShopOperationRequest::class.java
        )
            .set("operationDays", shopOperationParams)
            .sample()

        // when
        val responseEntity = restTemplate.exchange(
            uriComponents.toUriString(),
            HttpMethod.PUT,
            HttpEntity(request, authHeader),
            ApiResponse::class.java
        )

        // then
        val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)

        Assert.assertEquals(HttpStatusCode.valueOf(200), responseEntity.statusCode)
        Assert.assertEquals(ReturnCode.SUCCESS.code, response.returnCode)
        Assert.assertEquals(ReturnCode.SUCCESS.message, response.returnMessage)
    }

    @TestWithDisplayName("케이크 샵 영업일 업데이트에 성공한다")
    fun updateShopOperationDaysByAdmin() {
        // given
        val cakeShopId = 1L
        val url = "%s%d%s/{cakeShopId}/operation-days".formatted(BASE_URL, port, API_URL)
        val uriComponents = UriComponentsBuilder
            .fromUriString(url)
            .buildAndExpand(cakeShopId)
        val shopOperationParams = getConstructorMonkey().giveMeBuilder(
            ShopOperationParam::class.java
        )
            .setNotNull("operationDay")
            .setNotNull("operationStartTime")
            .setNotNull("operationEndTime").sampleList(6)
        val request = getConstructorMonkey().giveMeBuilder(
            UpdateShopOperationRequest::class.java
        )
            .set("operationDays", shopOperationParams)
            .sample()

        // when
        val responseEntity = restTemplate.exchange(
            uriComponents.toUriString(),
            HttpMethod.PUT,
            HttpEntity(request, getAuthHeaderById(10L)),
            ApiResponse::class.java
        )

        // then
        val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)

        Assert.assertEquals(HttpStatusCode.valueOf(200), responseEntity.statusCode)
        Assert.assertEquals(ReturnCode.SUCCESS.code, response.returnCode)
        Assert.assertEquals(ReturnCode.SUCCESS.message, response.returnMessage)
    }

    @get:TestWithDisplayName("케이크 샵 사업자 연동 여부 True 확인에 성공한다")
    val isOwnedCakeShop1: Unit
        get() {
            // given
            val cakeShopId = 1L
            val url = "%s%d%s/{cakeShopId}/owner".formatted(BASE_URL, port, API_URL)
            val uriComponents = UriComponentsBuilder
                .fromUriString(url)
                .buildAndExpand(cakeShopId)

            // when
            val responseEntity = restTemplate.exchange(
                uriComponents.toUriString(),
                HttpMethod.GET,
                HttpEntity<Any>(authHeader),
                ApiResponse::class.java
            )

            // then
            val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)
            val data = objectMapper.convertValue(response.data, CakeShopOwnerResponse::class.java)

            Assert.assertEquals(HttpStatusCode.valueOf(200), responseEntity.statusCode)
            Assert.assertEquals(ReturnCode.SUCCESS.code, response.returnCode)
            Assert.assertEquals(ReturnCode.SUCCESS.message, response.returnMessage)
            Assert.assertEquals(true, data.isOwned)
        }

    @get:TestWithDisplayName("케이크 샵 사업자 연동 여부 True 확인에 성공한다")
    val isOwnedCakeShop2: Unit
        get() {
            // given
            val cakeShopId = 2L
            val url = "%s%d%s/{cakeShopId}/owner".formatted(BASE_URL, port, API_URL)
            val uriComponents = UriComponentsBuilder
                .fromUriString(url)
                .buildAndExpand(cakeShopId)

            // when
            val responseEntity = restTemplate.exchange(
                uriComponents.toUriString(),
                HttpMethod.GET,
                HttpEntity<Any>(authHeader),
                ApiResponse::class.java
            )

            // then
            val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)
            val data = objectMapper.convertValue(response.data, CakeShopOwnerResponse::class.java)

            Assert.assertEquals(HttpStatusCode.valueOf(200), responseEntity.statusCode)
            Assert.assertEquals(ReturnCode.SUCCESS.code, response.returnCode)
            Assert.assertEquals(ReturnCode.SUCCESS.message, response.returnMessage)
            Assert.assertEquals(false, data.isOwned)
        }

    @get:TestWithDisplayName("나의 케이크 샵 아이디 조회에 성공한다")
    val myShopIds: Unit
        get() {
            // given
            val url = "%s%d%s/mine".formatted(BASE_URL, port, API_URL)
            val uriComponents = UriComponentsBuilder
                .fromUriString(url)
                .build()

            // when
            val responseEntity = restTemplate.exchange(
                uriComponents.toUriString(),
                HttpMethod.GET,
                HttpEntity<Any>(authHeader),
                ApiResponse::class.java
            )

            // then
            val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)
            val data = objectMapper.convertValue(response.data, CakeShopByMineResponse::class.java)

            Assert.assertEquals(HttpStatusCode.valueOf(200), responseEntity.statusCode)
            Assert.assertEquals(ReturnCode.SUCCESS.code, response.returnCode)
            Assert.assertEquals(ReturnCode.SUCCESS.message, response.returnMessage)
            Assert.assertEquals(true, data.isExist)
            Assert.assertEquals(1L, data.cakeShopId)
        }

    @TestWithDisplayName("조회수로 케이크 샵 조회에 성공한다")
    fun searchByViews1() {
        // given
        val url = "%s%d%s/search/views".formatted(BASE_URL, port, API_URL)
        val uriComponents = UriComponentsBuilder
            .fromUriString(url)
            .queryParam("offset", 0L)
            .queryParam("pageSize", 4)
            .build()

        initializeViews()

        // when
        val responseEntity = restTemplate.getForEntity(
            uriComponents.toUriString(),
            ApiResponse::class.java
        )

        // then
        val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)
        val data = objectMapper.convertValue(response.data, CakeShopSearchResponse::class.java)

        Assert.assertEquals(HttpStatusCode.valueOf(200), responseEntity.statusCode)
        Assert.assertEquals(ReturnCode.SUCCESS.code, response.returnCode)
        Assert.assertEquals(ReturnCode.SUCCESS.message, response.returnMessage)

        Assert.assertEquals(4, data.size.toLong())
    }

    @TestWithDisplayName("조회한 케이크 샵이 없을 시, 인기 케이크 샵 조회에 빈 배열을 리턴한다")
    fun searchByViews2() {
        // given
        val url = "%s%d%s/search/views".formatted(BASE_URL, port, API_URL)
        val uriComponents = UriComponentsBuilder
            .fromUriString(url)
            .queryParam("pageSize", 4)
            .build()

        // when
        val responseEntity = restTemplate.getForEntity(
            uriComponents.toUriString(),
            ApiResponse::class.java
        )

        // then
        val response = objectMapper.convertValue(responseEntity.body, ApiResponse::class.java)
        val data = objectMapper.convertValue(response.data, CakeShopSearchResponse::class.java)

        Assert.assertEquals(HttpStatusCode.valueOf(200), responseEntity.statusCode)
        Assert.assertEquals(ReturnCode.SUCCESS.code, response.returnCode)
        Assert.assertEquals(ReturnCode.SUCCESS.message, response.returnMessage)

        Assert.assertEquals(0, data.size.toLong())
    }
}
