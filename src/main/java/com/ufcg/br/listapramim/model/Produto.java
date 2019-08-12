package com.ufcg.br.listapramim.model;

import java.util.ArrayList;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="produto")
public class Produto {

	private static final String VALORPADRAO = "1";

	@Id
	private long id;
	
	private String nome;
	private Categoria categoria;
	private Tipo tipo;
	private ArrayList<String> mapaDePrecos;
	private String quantidade;
	
	public Produto () {}
	
	public Produto (String nome, String categoria,String tipo) {
		this.nome = nome;
		this.categoria = Categoria.valueOf(categoria);
		this.tipo = Tipo.valueOf(tipo);
		this.mapaDePrecos = new ArrayList<String>();
		this.quantidade = VALORPADRAO;
	}

	public Produto (String nome, String categoria,String tipo, String quantidade) {
		this.nome = nome;
		this.categoria = Categoria.valueOf(categoria);
		this.tipo = Tipo.valueOf(tipo);
		this.mapaDePrecos = new ArrayList<String>();
		this.quantidade = quantidade;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public ArrayList<String> getMapaDePrecos() {
		return mapaDePrecos;
	}

	public void setMapaDePrecos(ArrayList<String> mapaDePrecos) {
		this.mapaDePrecos = mapaDePrecos;
	}
	
	public String getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(String quantidade) {
		this.quantidade = quantidade;
	}
	
	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Produto other = (Produto) obj;
		if (id != other.id)
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}
	
}
