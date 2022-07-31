package com.api.auth.model;

import java.io.Serializable;
import java.util.UUID;
import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "usuario")
public class Usuario {
		
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Type(type = "pg-uuid")
	@Column(name = "id", columnDefinition = "uuid")
	private UUID id;
	
	@Column(unique = true)
	private String email;
	
	private String password;
	
	public Usuario() {
		this.id = id;
		this.email = email;
		this.password = password;
	}
	
	public Usuario(UUID id, String email, String password) {
		this.id = id;
		this.email = email;
		this.password = password;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	} 
	
	
}
