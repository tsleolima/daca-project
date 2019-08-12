package com.ufcg.br.listapramim.model;

public class ProdutoDAO {
	
	private String nome;
	private String categoria;
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
