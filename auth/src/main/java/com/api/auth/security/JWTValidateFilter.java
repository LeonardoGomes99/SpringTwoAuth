package com.api.auth.security;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

public class JWTValidateFilter extends BasicAuthenticationFilter{
	
	public static final String HEADER_ATTRIBUTE = "Authorization";
	public static final String ATTRIBUTE_PREFIX = "Bearer";
	
	public JWTValidateFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response,
			FilterChain chain)
			throws IOException, ServletException {
		
		String atributo = request.getHeader(HEADER_ATTRIBUTE);
		
		if(atributo == null) {
			chain.doFilter(request, response);
			return;
		}
		
		if(!atributo.startsWith(ATTRIBUTE_PREFIX)) {
			chain.doFilter(request, response);
			return;
		}
		
		String token = atributo.replace(ATTRIBUTE_PREFIX, "");		
		UsernamePasswordAuthenticationToken authenticationToken = getAuthenticationToken(token);
		
		SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		chain.doFilter(request, response);
	}
	
	private UsernamePasswordAuthenticationToken getAuthenticationToken(String token) {
		String usuario = JWT.require(Algorithm.HMAC512(JWTAuthFilter.TOKEN_PASSWORD))
				.build()
				.verify(token)
				.getSubject();
		
		if(usuario == null) {
			return null;
		}
		
		return new UsernamePasswordAuthenticationToken(usuario, null, new ArrayList<>());
	}

}
