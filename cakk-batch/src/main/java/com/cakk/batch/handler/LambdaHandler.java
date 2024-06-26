package com.cakk.batch.handler;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringBootLambdaContainerHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.cakk.batch.BatchApplication;

public class LambdaHandler implements RequestHandler<AwsProxyRequest, AwsProxyResponse> {

	private static final SpringBootLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;

	static {
		try {
			handler = SpringBootLambdaContainerHandler.getAwsProxyHandler(BatchApplication.class);
		} catch (ContainerInitializationException e) {
			throw new RuntimeException("Spring Boot Application 실행 실패", e);
		}
	}

	@Override
	public AwsProxyResponse handleRequest(AwsProxyRequest input, Context context) {
		return handler.proxy(input, context);
	}
}
