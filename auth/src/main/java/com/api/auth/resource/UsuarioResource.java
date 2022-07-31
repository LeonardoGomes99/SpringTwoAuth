package com.api.auth.resource;

import com.api.auth.model.Usuario;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.api.auth.repository.UsuarioRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioResource {
	
	private final UsuarioRepository repository;
	private final PasswordEncoder encoder;
	
	public UsuarioResource(UsuarioRepository repository, PasswordEncoder encoder) {
		this.repository = repository;
		this.encoder = encoder;
	}
	
	@GetMapping("/listarTodos")
	public ResponseEntity<List<Usuario>> listarTodos () {
        return ResponseEntity.ok(repository.findAll());
	}
	
	@PostMapping("/salvar")
	public ResponseEntity<Usuario> salvar (@RequestBody Usuario body){
		body.setPassword(encoder.encode(body.getPassword()));
		return ResponseEntity.ok(repository.save(body));
	}
	
	@GetMapping("/login")
	public ResponseEntity<Boolean> validarSenha(@RequestParam String email,
															  String password){
		//Validando email
		Optional<Usuario> optUsuario = repository.findByEmail(email);
		if(optUsuario.isEmpty()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
		}
		
		//Validando password
		Usuario model = optUsuario.get();
		boolean valid = encoder.matches(password, model.getPassword());		
		HttpStatus status = (valid) ? HttpStatus.OK : HttpStatus.UNAUTHORIZED;
		
		return ResponseEntity.status(status).body(valid);
	}

}
