package com.api.auth.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.api.auth.data.DetailUsuarioData;
import com.api.auth.model.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JWTAuthFilter extends UsernamePasswordAuthenticationFilter{
	
	public static final int TOKEN_EXPIRATION = 6000_000;
	public static final String TOKEN_PASSWORD = "4627a14a-3bf4-4774-bfa4-5dc15e049075";
	
	private final AuthenticationManager authenticationManager;
	
	public JWTAuthFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
			HttpServletResponse response)
			throws AuthenticationException {
		
		try {
			Usuario usuario = new ObjectMapper()
					.readValue(request.getInputStream(), Usuario.class);
			
			return authenticationManager.
					authenticate(new UsernamePasswordAuthenticationToken(
							usuario.getEmail(),
							usuario.getPassword(),
							new ArrayList<>()
					));
		} catch (IOException e) {
			throw new RuntimeException("Falha ao autenticar usuario", e);
		}
	}
	
	@Override
	protected void successfulAuthentication(HttpServletRequest request,
			HttpServletResponse response,
			FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		DetailUsuarioData usuarioData = (DetailUsuarioData) authResult.getPrincipal();
		
		String token = JWT.create().
				withSubject(usuarioData.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION))
				.sign(Algorithm.HMAC512(TOKEN_PASSWORD));
		
		response.getWriter().write(token);
		response.getWriter().flush();
	}

}
