package com.api.auth.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.api.auth.data.DetailUsuarioData;
import com.api.auth.model.Usuario;
import com.api.auth.repository.UsuarioRepository;

@Component
public class DetailUsuarioDataServiceImpl implements UserDetailsService {
	
	private final UsuarioRepository repository;
	
	public DetailUsuarioDataServiceImpl(UsuarioRepository repository) {
		this.repository = repository;
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Usuario> usuario = repository.findByEmail(username);
		
		if(usuario.isEmpty()) {
			throw new UsernameNotFoundException("Usuario ["+ username +"] não encontrado ");
		}
		return new DetailUsuarioData(usuario);
	}

}
