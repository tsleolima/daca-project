package com.ufcg.br.listapramim.usuario;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="usuario")
public class UserCredentials {
	
	private String username;
	private String password;
	
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
}
