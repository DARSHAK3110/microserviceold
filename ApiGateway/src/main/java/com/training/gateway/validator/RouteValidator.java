package com.training.gateway.validator;

import java.util.List;
import java.util.function.Predicate;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;


@Component
public class RouteValidator {
	
	public static final List<String> authenticatedUrls = List.of("/api/v1/users","/eureka");
	
	public Predicate<ServerHttpRequest> isAuthenticated = 
			request-> 	authenticatedUrls.stream().noneMatch(uri ->
		request.getURI().getPath().contains(uri)
		);
}
