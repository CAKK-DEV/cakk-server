package com.cakk.api.advice;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.sql.SQLException;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.cakk.api.common.annotation.TestWithDisplayName;
import com.cakk.api.controller.advice.GlobalControllerAdvice;
import com.cakk.api.controller.shop.ShopController;
import com.cakk.api.filter.JwtAuthenticationFilter;
import com.cakk.api.service.like.HeartService;
import com.cakk.api.service.like.LikeService;
import com.cakk.api.service.shop.ShopService;
import com.cakk.api.service.slack.SlackService;
import com.cakk.api.service.views.ViewsService;
import com.cakk.common.enums.ReturnCode;
import com.cakk.common.exception.CakkException;

@AutoConfigureMockMvc()
@ActiveProfiles("test")
@WebMvcTest(
	properties = "spring.profiles.active=test",
	value = {ShopController.class, GlobalControllerAdvice.class}
)
public class GlobalControllerAdviceTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ShopService shopService;

	@MockBean
	private HeartService heartService;

	@MockBean
	private LikeService likeService;

	@MockBean
	private ViewsService viewsService;

	@MockBean
	private SlackService slackService;

	@MockBean
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	@BeforeEach
	void setup(WebApplicationContext webApplicationContext) {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
			.addFilters(new CharacterEncodingFilter("UTF-8", true))
			.alwaysDo(print())
			.build();
	}

	@TestWithDisplayName("CakkException이 발생하면 BAD_REQUEST를 반환한다.")
	void handleCakkException1() throws Exception {
		// given
		final ReturnCode returnCode = ReturnCode.WRONG_JWT_TOKEN;
		doThrow(new CakkException(returnCode)).when(shopService).searchDetailById(1L);

		// when & then
		mockMvc.perform(get("/shops/1"))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.returnCode").value(returnCode.getCode()))
			.andExpect(jsonPath("$.returnMessage").value(returnCode.getMessage()));
	}

	@TestWithDisplayName("CakkException이 발생했고 INTERNAL_SERVER_ERROR 일 때 BAD_REQUEST를 반환한다.")
	void handleCakkException2() throws Exception {
		// given
		final ReturnCode returnCode = ReturnCode.INTERNAL_SERVER_ERROR;

		doThrow(new CakkException(returnCode)).when(shopService).searchDetailById(1L);
		doNothing().when(slackService).sendSlackForError(any(CakkException.class), any());

		// when & then
		mockMvc.perform(get("/shops/1"))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.returnCode").value(returnCode.getCode()))
			.andExpect(jsonPath("$.returnMessage").value(returnCode.getMessage()));

		verify(slackService, times(1)).sendSlackForError(any(CakkException.class), any());
	}

	@TestWithDisplayName("CakkException이 발생했고 EXTERNAL_SERVER_ERROR 일 때 BAD_REQUEST를 반환한다.")
	void handleCakkException3() throws Exception {
		// given
		final ReturnCode returnCode = ReturnCode.EXTERNAL_SERVER_ERROR;

		doThrow(new CakkException(returnCode)).when(shopService).searchDetailById(1L);
		doNothing().when(slackService).sendSlackForError(any(CakkException.class), any());

		// when & then
		mockMvc.perform(get("/shops/1"))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.returnCode").value(returnCode.getCode()))
			.andExpect(jsonPath("$.returnMessage").value(returnCode.getMessage()));

		verify(slackService, times(1)).sendSlackForError(any(CakkException.class), any());
	}

	@TestWithDisplayName("HttpMessageNotReadableException이 발생하면 BAD_REQUEST를 반환한다.")
	void handleRequestException1() throws Exception {
		// given
		doThrow(HttpMessageNotReadableException.class).when(shopService).searchDetailById(1L);

		// when & then
		mockMvc.perform(get("/shops/1"))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.returnCode").value(ReturnCode.WRONG_PARAMETER.getCode()))
			.andExpect(jsonPath("$.returnMessage").value(ReturnCode.WRONG_PARAMETER.getMessage()));
	}

	@TestWithDisplayName("HttpMessageNotReadableException이 발생하면 BAD_REQUEST를 반환한다.")
	void handleRequestException2() throws Exception {
		// given
		doThrow(HttpMessageNotReadableException.class).when(shopService).searchDetailById(1L);

		// when & then
		mockMvc.perform(get("/shops/1"))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.returnCode").value(ReturnCode.WRONG_PARAMETER.getCode()))
			.andExpect(jsonPath("$.returnMessage").value(ReturnCode.WRONG_PARAMETER.getMessage()));
	}

	@TestWithDisplayName("HttpRequestMethodNotSupportedException 발생하면 BAD_REQUEST를 반환한다.")
	void handleMethodNotSupportedException() throws Exception {
		// when & then
		mockMvc.perform(post("/shops/1"))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.returnCode").value(ReturnCode.METHOD_NOT_ALLOWED.getCode()))
			.andExpect(jsonPath("$.returnMessage").value(ReturnCode.METHOD_NOT_ALLOWED.getMessage()));
	}

	@TestWithDisplayName("HttpRequestMethodNotSupportedException 발생하면 BAD_REQUEST를 반환한다.")
	void handleMethodArgNotValidException() throws Exception {
		// when & then
		mockMvc.perform(get("/shops/location-based"))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.returnCode").value(ReturnCode.WRONG_PARAMETER.getCode()))
			.andExpect(jsonPath("$.returnMessage").value(ReturnCode.WRONG_PARAMETER.getMessage()));
	}

	@TestWithDisplayName("SQLException이 발생하면 BAD_REQUEST를 반환한다.")
	void handleServerException1() throws Exception {
		// given
		doThrow(new RuntimeException()).when(shopService).searchDetailById(1L);

		// when & then
		mockMvc.perform(get("/shops/1"))
			.andExpect(status().isInternalServerError())
			.andExpect(jsonPath("$.returnCode").value(ReturnCode.INTERNAL_SERVER_ERROR.getCode()))
			.andExpect(jsonPath("$.returnMessage").value(ReturnCode.INTERNAL_SERVER_ERROR.getMessage()));
	}
}
