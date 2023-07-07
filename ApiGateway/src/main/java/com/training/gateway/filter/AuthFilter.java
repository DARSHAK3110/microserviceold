package com.training.gateway.filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import com.google.common.net.HttpHeaders;
import com.training.gateway.helper.TokenHelper;
import com.training.gateway.validator.RouteValidator;

@Component
public class AuthFilter  extends AbstractGatewayFilterFactory<AuthFilter.Config>{

	@Autowired
	private RouteValidator routeValidator;
	
	@Autowired
	private TokenHelper tokenHelper;
	public AuthFilter() {
		super(Config.class);
	}
	public static class Config {

	}

	@Override
	public GatewayFilter apply(Config config) {
		
		return ((exchange,filter)->{
			ServerHttpRequest req = exchange.getRequest();
			if(routeValidator.isAuthenticated.test(exchange.getRequest())) {
			
				if(!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
					throw new RuntimeException("Token not avail!!");
				}
				
				String token = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
				if(token != null && token.startsWith("Bearer ")) {
					token = token.substring(7);
					try {
					tokenHelper.validateToken(token);
					}catch (Exception e) {
						throw new RuntimeException(e.getMessage());
					}
					req = exchange.getRequest().mutate().header("username", tokenHelper.getSubject(token)).header("role",tokenHelper.getRole(token)).build();
				}
				
				
			}
			
			return filter.filter(exchange.mutate().request(req).build());
		});
		
	}

	
}
