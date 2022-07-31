package com.api.auth.repository;

import com.api.auth.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
	Optional<Usuario> findByEmail(String email);
}
