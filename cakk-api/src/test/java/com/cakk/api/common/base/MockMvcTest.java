package com.cakk.api.common.base;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.cakk.api.controller.advice.GlobalControllerAdvice;
import com.cakk.api.controller.s3.AwsS3Controller;
import com.cakk.api.controller.shop.ShopController;
import com.cakk.api.controller.user.SignController;
import com.cakk.api.filter.JwtAuthenticationFilter;
import com.cakk.api.service.like.HeartService;
import com.cakk.api.service.like.LikeService;
import com.cakk.api.service.shop.ShopService;
import com.cakk.api.service.slack.SlackService;
import com.cakk.api.service.user.EmailVerificationService;
import com.cakk.api.service.user.SignService;
import com.cakk.api.service.views.ViewsService;
import com.cakk.external.service.S3Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.BuilderArbitraryIntrospector;
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin;

@AutoConfigureMockMvc()
@ActiveProfiles("test")
@WebMvcTest(
	properties = "spring.profiles.active=test",
	value = {
		SignController.class,
		ShopController.class,
		AwsS3Controller.class,
		GlobalControllerAdvice.class
	}
)
public abstract class MockMvcTest {

	@Autowired
	protected MockMvc mockMvc;

	@Autowired
	protected ObjectMapper objectMapper;

	@MockBean
	protected JwtAuthenticationFilter jwtAuthenticationFilter;

	@MockBean
	protected SlackService slackService;

	@MockBean
	protected S3Service s3Service;

	@MockBean
	protected SignService signService;

	@MockBean
	protected EmailVerificationService emailVerificationService;

	@MockBean
	protected ShopService shopService;

	@MockBean
	protected HeartService heartService;

	@MockBean
	protected LikeService likeService;

	@MockBean
	protected ViewsService viewsService;

	@BeforeEach
	void setup(WebApplicationContext webApplicationContext) {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
			.addFilters(new CharacterEncodingFilter("UTF-8", true))
			.alwaysDo(print())
			.build();
	}

	protected final FixtureMonkey getConstructorMonkey() {
		return FixtureMonkey.builder()
			.plugin(new JakartaValidationPlugin())
			.objectIntrospector(ConstructorPropertiesArbitraryIntrospector.INSTANCE)
			.build();
	}

	protected final FixtureMonkey getReflectionMonkey() {
		return FixtureMonkey.builder()
			.plugin(new JakartaValidationPlugin())
			.objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
			.build();
	}

	protected final FixtureMonkey getBuilderMonkey() {
		return FixtureMonkey.builder()
			.plugin(new JakartaValidationPlugin())
			.objectIntrospector(BuilderArbitraryIntrospector.INSTANCE)
			.build();
	}
}
