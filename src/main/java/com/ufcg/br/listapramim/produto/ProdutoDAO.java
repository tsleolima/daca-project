package com.ufcg.br.listapramim.produto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ProdutoDAO {
	
	@NotEmpty(message = "O nome não pode ser vazio")
	private String nome;
	
	@NotNull
	private String categoria;
	
	@NotNull
	private String tipo;

	private String quantidade;
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getQuantidade() {
		return this.quantidade;
	}
}
