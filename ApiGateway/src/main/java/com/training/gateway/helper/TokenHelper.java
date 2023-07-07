package com.training.gateway.helper;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class TokenHelper {
	private static final String SECRET_KEY = "368E615852A5885E69A591AD26B3711111111111111111111111111111111111111";

	public void validateToken(String token) {
		Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY))).build().parseClaimsJws(token);
	}

	public String getRole(String token) {
		Claims claims = extractAllCliams(token);
		return claims.get("role").toString();
	}

	public String getSubject(String token) {
		Claims cliams = extractAllCliams(token);
		return cliams.getSubject();

	}
	
	Claims extractAllCliams(String token) {
		Jws<Claims> parseClaimsJws = Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(Decoders.BASE64.decode(SECRET_KEY))).build().parseClaimsJws(token);
		Claims body = parseClaimsJws.getBody();
		return body;
	}

}
