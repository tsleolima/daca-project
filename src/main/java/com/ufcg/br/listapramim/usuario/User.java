package com.ufcg.br.listapramim.usuario;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="usuario")
public class User {
	
	@Id
	private ObjectId id;
	
	private String username;
	private String password;
	private Role role;
	
	public User (String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public User () {}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public Role getRole () { 
		return role;
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public void setRoles (Role roles) {
		this.role = roles;
	}
	
	
	
}
